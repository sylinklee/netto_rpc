package com.netto.client.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.netto.client.pool.TcpConnectPool;
import com.netto.client.provider.LocalServiceProvider;
import com.netto.client.provider.NginxServiceProvider;
import com.netto.client.provider.ServiceProvider;
import com.netto.core.context.ServiceAddressGroup;
import com.netto.core.filter.InvokeMethodFilter;

public class ServiceRouterFactory implements FactoryBean<ServiceRouter>, InitializingBean {
	private ServiceAddressGroup serverGroup;
	private Map<String, String> routers;
	private GenericObjectPoolConfig poolConfig;
	private List<InvokeMethodFilter> filters;

	private boolean needSignature = false;

	public boolean doNeedSignature() {
		return needSignature;
	}

	public void setNeedSignature(boolean needSignature) {
		this.needSignature = needSignature;
	}

	public GenericObjectPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public List<InvokeMethodFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<InvokeMethodFilter> filters) {
		this.filters = filters;
	}

	public Map<String, String> getRouters() {
		return routers;
	}

	public void setRouters(Map<String, String> routers) {
		this.routers = routers;
	}

	public ServiceRouter getObject() throws Exception {

		List<ServiceProvider> providers = new ArrayList<ServiceProvider>();

		ServiceProvider provider = null;
		if (serverGroup.getRegistry() != null && serverGroup.getRegistry().startsWith("http")) {

			provider = new NginxServiceProvider(serverGroup.getRegistry(), serverGroup.getServiceApp(),
					serverGroup.getServiceGroup(), needSignature).setPoolConfig(this.poolConfig);

		} else {
			TcpConnectPool pool = new TcpConnectPool(serverGroup.getServers(), this.poolConfig);
			provider = new LocalServiceProvider(serverGroup.getRegistry(), serverGroup.getServiceApp(),
					serverGroup.getServiceGroup(), pool, needSignature);

		}
		providers.add(provider);

		return new ServiceRouter(serverGroup.getServiceApp(), serverGroup.getServiceGroup(), providers,
				this.getRouters());
	}

	public Class<?> getObjectType() {
		return ServiceRouter.class;
	}

	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.serverGroup == null) {
			throw new Exception("exception:serverGroup is null!");
		}

	}

	public void setServerGroup(ServiceAddressGroup serverGroup) {
		this.serverGroup = serverGroup;
	}

}
