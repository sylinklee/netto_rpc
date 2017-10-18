package com.netto.server;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netto.core.filter.InvokeMethodFilter;
import com.netto.core.util.Constants;
import com.netto.core.util.JsonMapperUtil;
import com.netto.server.bean.NettoServiceBean;
import com.netto.server.bean.ServiceBean;
import com.netto.server.handler.NettoServiceChannelHandler;
import com.netto.server.handler.impl.AsynchronousChannelHandler;
import com.netto.server.handler.impl.NettyNettoMessageHandler;
import com.netto.server.message.NettoFrameDecoder;
import com.netto.server.message.NettoMessageDecoder;
import com.netto.service.desc.ServerDesc;
import com.netto.service.desc.ServiceDesc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer implements InitializingBean, DisposableBean, ApplicationContextAware {
	private static Logger logger = Logger.getLogger(NettyServer.class);
	private int port = 12345;
	private String registry;
	private String serverApp;
	private String serverGroup = Constants.DEFAULT_SERVER_GROUP;
	private List<InvokeMethodFilter> filters;
	private int numOfIOWorkerThreads = 16;

	private int maxRequestSize = 1024 * 1024;
	private ApplicationContext applicationContext;
	private Map<String, NettoServiceBean> serviceBeans;

	private int numOfHandlerWorker = 256;

	private int backlog = 1024 * 1024;
	private int maxWaitingQueueSize = 1024 * 1024;
	private ChannelFuture channel;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public NettyServer() {
	}

	public NettyServer(String serverApp, String serverGroup, int port) {
		this.serverApp = serverApp;
		this.serverGroup = serverGroup;
		this.port = port;

	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public void setServerApp(String serverApp) {
		this.serverApp = serverApp;
	}

	public void setServerGroup(String serverGroup) {
		this.serverGroup = serverGroup;
	}

	public void setMaxWaitingQueueSize(int maxWaitingQueueSize) {
		this.maxWaitingQueueSize = maxWaitingQueueSize;
	}

	public int getMaxRequestSize() {
		return maxRequestSize;
	}

	public void setMaxRequestSize(int maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setServiceBeans(Map<String, NettoServiceBean> serviceBeans) {
		this.serviceBeans = serviceBeans;
	}

	public List<InvokeMethodFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<InvokeMethodFilter> filters) {
		this.filters = filters;
	}

	public void afterPropertiesSet() throws Exception {
		if (this.serverApp == null) {
			throw new Exception("exception:serverApp is null!");
		}
		if (this.serverGroup == null) {
			throw new Exception("exception:serverGroup is null!");
		}
		if (this.serviceBeans == null) {
			this.serviceBeans = new HashMap<String, NettoServiceBean>();
			Map<String, ServiceBean> temps = this.applicationContext.getBeansOfType(ServiceBean.class);
			for (String key : temps.keySet()) {
				ServiceBean bean = temps.get(key);
				if (bean.getServiceName() == null && bean.getRefName() == null) {
					throw new Exception("exception:bean.getServiceName() && bean.getRefName() is all null!");
				}
				if (bean.getTimeout() <= 0) {
					throw new Exception("exception:bean.getTimeout() <=0!");
				}
				Object serviceBean = null;
				String refName = bean.getRefName();

				if (!refName.contains(".")) {
					String refImplName = null;
					if (!refName.endsWith("Impl")) {
						refImplName = refName + "Impl";
					}

					try {
						serviceBean = this.applicationContext.getBean(refName);
					} catch (Exception e) {
						logger.warn(" bean named of " + refName + " does not exists");
					}

					/* 加上impl取 */
					if (serviceBean == null && refImplName != null) {
						try {
							serviceBean = this.applicationContext.getBean(refImplName);
						} catch (Exception e) {
							logger.warn(" bean named of " + refImplName + " does not exists");
						}
					}
				} else {
					try {
						serviceBean = this.applicationContext
								.getBean(this.getClass().getClassLoader().loadClass(refName));
					} catch (Exception e) {
						logger.warn(" bean class  " + refName + " does not exists");
					}
				}

				if (serviceBean == null) {
					throw new BeanCreationException(" bean named of " + refName + " does not exists");
				}
				NettoServiceBean factoryBean = new NettoServiceBean(bean, serviceBean);

				String serviceName = bean.getServiceName();
				if (serviceName == null) {
					serviceName = bean.getRefName();
				}
				this.serviceBeans.put(serviceName, factoryBean);
			}
		}
		this.publish();
		this.run();
	}

	private void run() throws Exception {

		ExecutorService boss = Executors.newCachedThreadPool(new NamedThreadFactory("NettyServerBoss", true));
		ExecutorService worker = Executors.newCachedThreadPool(new NamedThreadFactory("NettyServerWorker", true));
		bossGroup = new NioEventLoopGroup(1, boss); // (1)
		workerGroup = new NioEventLoopGroup(numOfIOWorkerThreads, worker);
		ServerDesc serverDesc = new ServerDesc();
		serverDesc.setRegistry(this.registry);
		serverDesc.setServerApp(this.serverApp);
		serverDesc.setServerGroup(this.serverApp);
		NettoServiceChannelHandler handler = new AsynchronousChannelHandler(serverDesc, serviceBeans, filters,
				this.maxWaitingQueueSize, this.numOfHandlerWorker);

		ServerBootstrap b = new ServerBootstrap(); // (2)
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
				.option(ChannelOption.SO_BACKLOG, backlog).childHandler(new ChannelInitializer<SocketChannel>() { // (4)
					@Override
					public void initChannel(SocketChannel ch) throws Exception {

						ChannelPipeline p = ch.pipeline();
						// p.addLast("framer", new
						// DelimiterBasedFrameDecoder(maxRequestSize,
						// Constants.delimiterAsByteBufArray()));
						// p.addLast("framer",new
						// JsonObjectDecoder(maxRequestSize));
						// p.addLast("decoder", new ByteArrayDecoder());
						p.addLast("framer", new NettoFrameDecoder(maxRequestSize));
						p.addLast("decoder", new NettoMessageDecoder());
						p.addLast("encoder", new StringEncoder());
						p.addLast("handler", new NettyNettoMessageHandler(handler));

						// p.addLast("handler",new
						// NettyServerJsonHandler(serviceBeans, filters));
					}
				});

		// Bind and start to accept incoming connections.
		channel = b.bind(this.port);// .sync(); // (7)
		logger.info("server bind port:" + this.port);

		// Wait until the server socket is closed.
		// f.channel().closeFuture().sync();
	}

	@Override
	public void destroy() throws Exception {
		// Shut down all event loops to terminate all threads.
		if (channel != null) {
			try {
				channel.channel().close();
			} catch (Throwable t) {
				logger.error("channel.close", t);
			}
		}
		if (bossGroup != null) {
			try {
				bossGroup.shutdownGracefully();
			} catch (Throwable t) {
				logger.error("bossGroup.shutdownGracefully", t);
			}
		}
		if (workerGroup != null) {
			try {
				workerGroup.shutdownGracefully();
			} catch (Throwable t) {
				logger.error("workerGroup.shutdownGracefully", t);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public int getNumOfIOWorkerThreads() {
		return numOfIOWorkerThreads;
	}

	public void setNumOfIOWorkerThreads(int numOfIOWorkerThreads) {
		this.numOfIOWorkerThreads = numOfIOWorkerThreads;
	}

	public int getBacklog() {
		return backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	public void setNumOfHandlerWorker(int numOfHandlerWorker) {
		this.numOfHandlerWorker = numOfHandlerWorker;
	}

	private void publish() {
		if (this.registry == null)
			return;
		List<ServiceDesc> services = new ArrayList<ServiceDesc>();
		for (String key : this.serviceBeans.keySet()) {
			ServiceBean serviceBean = this.serviceBeans.get(key).getServiceBean();
			Class<?>[] interfaces = this.serviceBeans.get(key).getObjectType().getInterfaces();
			String interfaceClazz = null;
			if (interfaces != null && interfaces.length > 0) {
				interfaceClazz = interfaces[0].getName();
			}
			if (serviceBean.getGateway()
					|| (interfaceClazz != null && interfaceClazz.equals("com.netto.schedule.IScheduleTaskProcess"))) {
				ServiceDesc desc = new ServiceDesc();
				desc.setServiceName(key);
				desc.setServerApp(serverApp);
				desc.setTimeout(serviceBean.getTimeout());
				desc.setGateway(true);
				desc.setInterfaceClazz(interfaceClazz);
				services.add(desc);
			}
		}
		this.postRegistry(services);

	}

	private void postRegistry(List<ServiceDesc> services) {
		if (services == null || services.size() == 0)
			return;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Constants.DEFAULT_TIMEOUT)
				.setConnectionRequestTimeout(Constants.DEFAULT_TIMEOUT).setSocketTimeout(Constants.DEFAULT_TIMEOUT)
				.build();
		HttpClient httpClient = getHttpClient();
		try {
			StringBuilder sb = new StringBuilder(50);
			sb.append(this.registry).append(this.registry.endsWith("/") ? "" : "/").append("/publish");
			HttpPost post = new HttpPost(sb.toString());
			post.setConfig(requestConfig);

			ObjectMapper mapper = JsonMapperUtil.getJsonMapper();

			String requestBody = mapper.writeValueAsString(services);
			StringEntity se = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
			post.setEntity(se);

			// 创建参数队列
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				logger.info("publish 服务成功！个数：" + services.size());
			} else {
				logger.info("publish 服务失败！");
			}

		} catch (Exception e) {
			logger.error("publish 服务失败！" + e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			if (httpClient instanceof CloseableHttpClient) {
				try {
					((CloseableHttpClient) httpClient).close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	private HttpClient getHttpClient() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (KeyStoreException e) {
			logger.error(e.getMessage(), e);
		}
		return HttpClients.createDefault();
	}

}
