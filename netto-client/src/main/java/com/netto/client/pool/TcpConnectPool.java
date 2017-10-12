package com.netto.client.pool;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netto.client.api.ServiceAPIClient;
import com.netto.client.util.JsonMapperUtil;
import com.netto.core.context.ServerAddressGroup;
import com.netto.core.util.Constants;
import com.netto.core.context.ServerAddress;

public class TcpConnectPool implements ConnectPool<Socket> {
	private static Logger logger = Logger.getLogger(TcpConnectPool.class);
	private ServerAddressGroup serverGroup;
	private GenericObjectPool<Socket> pool;

	public TcpConnectPool(ServerAddressGroup serverGroup, GenericObjectPoolConfig config) {
		this.serverGroup = serverGroup;
		if (config == null) {
			config = new GenericObjectPoolConfig();
			config.setMaxTotal(200);
			config.setMaxIdle(50);
			config.setMinIdle(10);
			// 从池中取连接的最大等待时间，单位ms.
			config.setMaxWaitMillis(1000);
			// 指明连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除.
			config.setTestWhileIdle(true);
			// 每30秒运行一次空闲连接回收器
			config.setTimeBetweenEvictionRunsMillis(30000);
			// 池中的连接空闲10分钟后被回收
			config.setMinEvictableIdleTimeMillis(600000);
			// 在每次空闲连接回收器线程(如果有)运行时检查的连接数量
			config.setNumTestsPerEvictionRun(10);

		}
		pool = new GenericObjectPool<Socket>(new ClientSocketPoolFactory(), config);
	}

	public Socket getResource() {

		try {
			return this.pool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void invalidate(Socket resource) {
		if (resource == null)
			return;

		try {
			this.pool.invalidateObject(resource);
		} catch (Exception e) {
			logger.error("tcp Pool destroy", e);
		}
	}

	public void release(Socket resource) {
		if (resource == null)
			return;
		this.pool.returnObject(resource);
	}

	private class ClientSocketPoolFactory implements PooledObjectFactory<Socket> {
		private HttpConnectPool httpPool;

		public ClientSocketPoolFactory() {
			this.httpPool = new HttpConnectPool();
		}

		public PooledObject<Socket> makeObject() throws Exception {
			// 简单策略随机取服务器，没有考虑权重
			List<ServerAddress> temps = new ArrayList<ServerAddress>();
			temps.addAll(serverGroup.getServers());

			for (int i = temps.size() - 1; i >= 0; i--) {
				int index = new Random(System.currentTimeMillis()).nextInt(temps.size());
				ServerAddress server = temps.get(index);
				try {
					PooledObject<Socket> po = new DefaultPooledObject<Socket>(
							new Socket(server.getIp(), server.getPort()));
					if (logger.isDebugEnabled()) {
						logger.debug("tcpPool makeObject[" + server.getIp() + ":" + server.getPort() + "] success!");
					}
					return po;
				} catch (Exception e) {
					temps.remove(i);
					logger.error("tcpPool makeObject[" + server.getIp() + ":" + server.getPort() + "] failed! "
							+ e.getMessage());
				}
			}
			// 没有可用服务器时，请求一下registry更新服务器信息
			this.updateServerGroup();
			throw new Exception("No server available!");
		}

		public void destroyObject(PooledObject<Socket> p) throws Exception {
			p.getObject().close();

		}

		public boolean validateObject(PooledObject<Socket> p) {
			boolean validate = p.getObject().isConnected() && !p.getObject().isClosed();
			if (!validate) {
				// 确认无效的直接返回就可以了
				return validate;
			} else {
				// 无法确认无效需要进一步处理
				return this.ping(p.getObject(), 1000);
			}
		}

		public void activateObject(PooledObject<Socket> p) throws Exception {
			if (p.getObject().isClosed()) {
				p.getObject().connect(p.getObject().getRemoteSocketAddress(), p.getObject().getSoTimeout());
			}

		}

		public void passivateObject(PooledObject<Socket> p) throws Exception {
		}

		private boolean ping(Socket socket, int timeout) {
			try {
				String data = "ping";
				int len = new ServiceAPIClient(socket, timeout).pingService(data);
				if (logger.isDebugEnabled()) {
					logger.debug("ping server[" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort()
							+ "] success! return  len=" + len);
				}
				return len == data.length();
			} catch (Throwable t) {
				logger.error("ping service[" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort()
						+ "] failed! " + t.getMessage());
				return false;
			}
		}

		private synchronized void updateServerGroup() {
			if (serverGroup.getRegistry() == null)
				return;
			if (!serverGroup.getRegistry().startsWith("http"))
				return;
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Constants.DEFAULT_TIMEOUT)
					.setConnectionRequestTimeout(Constants.DEFAULT_TIMEOUT).setSocketTimeout(Constants.DEFAULT_TIMEOUT)
					.build();
			HttpClient httpClient = this.httpPool.getResource();
			try {
				StringBuilder sb = new StringBuilder(50);
				sb.append(serverGroup.getRegistry()).append(serverGroup.getRegistry().endsWith("/") ? "" : "/")
						.append(serverGroup.getServerApp()).append("/servers");
				HttpGet get = new HttpGet(sb.toString());
				get.setConfig(requestConfig);
				// 创建参数队列
				HttpResponse response = httpClient.execute(get);
				HttpEntity entity = response.getEntity();
				String body = EntityUtils.toString(entity, "UTF-8");
				ObjectMapper mapper = JsonMapperUtil.getJsonMapper();
				List<ServerAddressGroup> servers = mapper.readValue(body,
						mapper.getTypeFactory().constructParametricType(List.class,
								mapper.getTypeFactory().constructType(ServerAddressGroup.class)));

				if (servers != null && servers.size() > 0) {
					serverGroup.getServers().clear();
					serverGroup.getServers().addAll(servers.get(0).getServers());
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			} finally {
				this.httpPool.release(httpClient);
			}
		}
	}
}
