package com.netto.client.router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netto.client.context.RpcContext;
import com.netto.client.provider.AbstractServiceProvider;
import com.netto.client.provider.ServiceProvider;

public class ServiceRouter {
	private Map<String, String> routers;
	private Map<String, ServiceProvider> providerMap = new HashMap<String, ServiceProvider>();

	public ServiceRouter(ServiceProvider provider) {
		AbstractServiceProvider obj = (AbstractServiceProvider) provider;
		providerMap.put(obj.getServiceApp() + "." + obj.getServiceGroup(), obj);
	}

	public ServiceRouter(List<ServiceProvider> providers, Map<String, String> routers) {
		this.routers = routers;
		for (ServiceProvider obj : providers) {
			AbstractServiceProvider provider = (AbstractServiceProvider) obj;
			providerMap.put(provider.getServiceApp() + "." + provider.getServiceGroup(), provider);
		}
	}

	public ServiceProvider findProvider(String serviceApp, String serviceGroup) {
		if (serviceGroup == null) {
			if (RpcContext.getRouterContext() != null) {
				if (routers != null) {
					serviceGroup = this.routers.get(RpcContext.getRouterContext());
				}
			}
		}
		if (serviceGroup == null) {
			serviceGroup = "*";
		}
		return this.providerMap.get(serviceApp + "." + serviceGroup);
	}
}
