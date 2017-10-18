package com.netto.client.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netto.client.pool.ConnectPool;
import com.netto.client.pool.HttpConnectPool;
import com.netto.client.pool.TcpConnectPool;
import com.netto.client.router.ServiceRouter;
import com.netto.core.context.RouteConfig;
import com.netto.core.context.ServerAddressGroup;
import com.netto.core.util.Constants;
import com.netto.core.util.JsonMapperUtil;
import com.netto.service.desc.ServerDesc;

public class NginxServiceProvider extends AbstractServiceProvider {
	private static Logger logger = Logger.getLogger(NginxServiceProvider.class);
	private HttpConnectPool httpPool;
	private GenericObjectPoolConfig config;

	public NginxServiceProvider(ServerDesc serverDesc, boolean needSignature) {
		super(serverDesc, needSignature);
		this.httpPool = new HttpConnectPool();

	}

	public NginxServiceProvider setPoolConfig(GenericObjectPoolConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * 根据不同协议需要得到不同的pool
	 */
	public ConnectPool<?> getPool(String protocol) {
		if (protocol.equals("http")) {
			return this.httpPool;
		} else {
			// tcp协议需要到nginx上拿到服务器信息
			return this.getTcpPool();
		}
	}

	private TcpConnectPool getTcpPool() {
		ServiceProvider provider = this.getRouter().findProvider();
		return (TcpConnectPool) provider.getPool("tcp");
	}

	private ServiceRouter getRouter() {
		List<ServiceProvider> providers = this.getProviders();
		ServiceRouter router = new ServiceRouter(this.getServerDesc(), providers, this.getRouterMap());
		return router;
	}

	private List<ServiceProvider> getProviders() {
		List<ServiceProvider> providers = new ArrayList<ServiceProvider>();
		List<ServerAddressGroup> serverGroups = this.getServerGroups();
		for (ServerAddressGroup serverGroup : serverGroups) {
			TcpConnectPool pool = new TcpConnectPool(serverGroup, this.config);
			ServiceProvider provider = new LocalServiceProvider(this.getServerDesc(), pool, this.needSignature());
			providers.add(provider);
		}
		return providers;
	}

	@SuppressWarnings("unchecked")
	private Map<String, RouteConfig> getRouterMap() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Constants.DEFAULT_TIMEOUT)
				.setConnectionRequestTimeout(Constants.DEFAULT_TIMEOUT).setSocketTimeout(Constants.DEFAULT_TIMEOUT)
				.build();
		HttpClient httpClient = this.httpPool.getResource();
		try {
			StringBuilder sb = new StringBuilder(50);
			sb.append(this.getServerDesc().getRegistry())
					.append(this.getServerDesc().getRegistry().endsWith("/") ? "" : "/")
					.append(this.getServerDesc().getServerApp()).append("/routers");
			HttpGet get = new HttpGet(sb.toString());
			get.setConfig(requestConfig);
			// 创建参数队列
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity, "UTF-8");
			ObjectMapper mapper = JsonMapperUtil.getJsonMapper();
			return mapper.readValue(body, Map.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			this.httpPool.release(httpClient);
		}
	}

	private List<ServerAddressGroup> getServerGroups() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Constants.DEFAULT_TIMEOUT)
				.setConnectionRequestTimeout(Constants.DEFAULT_TIMEOUT).setSocketTimeout(Constants.DEFAULT_TIMEOUT)
				.build();
		HttpClient httpClient = this.httpPool.getResource();
		try {
			StringBuilder sb = new StringBuilder(50);
			sb.append(this.getServerDesc().getRegistry())
					.append(this.getServerDesc().getRegistry().endsWith("/") ? "" : "/")
					.append(this.getServerDesc().getServerApp()).append("/servers");
			HttpGet get = new HttpGet(sb.toString());
			get.setConfig(requestConfig);
			// 创建参数队列
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity, "UTF-8");
			ObjectMapper mapper = JsonMapperUtil.getJsonMapper();
			return mapper.readValue(body, mapper.getTypeFactory().constructParametricType(List.class,
					mapper.getTypeFactory().constructType(ServerAddressGroup.class)));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			this.httpPool.release(httpClient);
		}
	}
}
