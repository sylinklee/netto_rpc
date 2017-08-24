package com.netto.client.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;

import com.netto.client.pool.TcpConnectPool;
import com.netto.client.provider.LocalServiceProvider;
import com.netto.client.provider.NginxServiceProvider;
import com.netto.client.provider.ServiceProvider;
import com.netto.context.ServiceAddressGroup;
import com.netto.filter.InvokeMethodFilter;

public class ServiceRouterFactory implements FactoryBean<ServiceRouter> {
	private List<ServiceAddressGroup> serverGroups;
	private ServiceAddressGroup serverGroup;
	private Map<String, String> routers;
	private GenericObjectPoolConfig poolConfig;
	private List<InvokeMethodFilter> filters;

	public GenericObjectPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public List<ServiceAddressGroup> getServerGroups() {
		return serverGroups;
	}

	public void setServerGroups(List<ServiceAddressGroup> serverGroups) {
		this.serverGroups = serverGroups;
	}

	public List<InvokeMethodFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<InvokeMethodFilter> filters) {
		this.filters = filters;
	}

	public ServiceAddressGroup getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(ServiceAddressGroup serverGroup) {
		// 对设置的服务器信息进行校验
		if (serverGroup.getRegistry() == null) {
			if (serverGroup.getServers() == null || serverGroup.getServers().size() == 0) {
				throw new RuntimeException("no server list!");
			}
		}
		if (serverGroup.getServiceApp() == null) {
			throw new RuntimeException("no app of services!");
		}
		if (serverGroup.getServiceGroup() == null) {
			serverGroup.setServiceGroup("*");
		}
		this.serverGroup = serverGroup;
	}

	public Map<String, String> getRouters() {
		return routers;
	}

	public void setRouters(Map<String, String> routers) {
		this.routers = routers;
	}

	public ServiceRouter getObject() throws Exception {
		if (this.getServerGroups() == null) {
			this.setServerGroups(new ArrayList<ServiceAddressGroup>());
			this.getServerGroups().add(this.getServerGroup());
		}
		List<ServiceProvider> providers = new ArrayList<ServiceProvider>();
		for (ServiceAddressGroup serverGroup : this.serverGroups) {
			ServiceProvider provider = null;
			if (serverGroup.getRegistry() != null && serverGroup.getRegistry().startsWith("http")) {

				provider = new NginxServiceProvider(serverGroup.getRegistry(), serverGroup.getServiceApp(),
						serverGroup.getServiceGroup());

			} else {
				if (this.poolConfig == null) {
					this.poolConfig = new GenericObjectPoolConfig();
					this.poolConfig.setMaxTotal(100);

				}
				TcpConnectPool pool = new TcpConnectPool(serverGroup.getServers(), this.poolConfig);
				provider = new LocalServiceProvider(serverGroup.getRegistry(), serverGroup.getServiceApp(),
						serverGroup.getServiceGroup(), pool);

			}
			providers.add(provider);
		}
		return new ServiceRouter(providers, this.getRouters());
	}

	public Class<?> getObjectType() {
		return ServiceRouter.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
