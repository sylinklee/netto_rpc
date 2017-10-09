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
import org.apache.log4j.Logger;

import com.netto.client.api.ServiceAPIClient;
import com.netto.core.context.ServiceAddress;

public class TcpConnectPool implements ConnectPool<Socket> {
	private static Logger logger = Logger.getLogger(TcpConnectPool.class);
	private List<ServiceAddress> servers = new ArrayList<ServiceAddress>();
	private GenericObjectPool<Socket> pool;

	public TcpConnectPool(List<ServiceAddress> servers, GenericObjectPoolConfig config) {
		this.servers = servers;
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

	public List<ServiceAddress> getServers() {
		return servers;
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
		public ClientSocketPoolFactory() {

		}

		public PooledObject<Socket> makeObject() throws Exception {
			// 简单策略随机取服务器，没有考虑权重
			List<ServiceAddress> temps = new ArrayList<ServiceAddress>();
			temps.addAll(servers);

			for (int i = temps.size() - 1; i >= 0; i--) {
				int index = new Random(System.currentTimeMillis()).nextInt(temps.size());
				ServiceAddress server = temps.get(index);
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
	}
}
