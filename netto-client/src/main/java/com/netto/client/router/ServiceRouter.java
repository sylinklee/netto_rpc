package com.netto.client.router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netto.client.provider.AbstractServiceProvider;
import com.netto.client.provider.ServiceProvider;
import com.netto.core.context.RpcContext;

public class ServiceRouter {
	private Map<String, String> routers;
	private Map<String, ServiceProvider> providerMap = new HashMap<String, ServiceProvider>();

//	public ServiceRouter(ServiceProvider provider) {
//		AbstractServiceProvider obj = (AbstractServiceProvider) provider;
//		providerMap.put(obj.getServiceApp() + "." + obj.getServiceGroup(), obj);
//	}
	private String serviceApp;

	public ServiceRouter(String serviceApp,List<ServiceProvider> providers, Map<String, String> routers) {
		this.routers = routers;
	    this.serviceApp = serviceApp;
		for (ServiceProvider obj : providers) {
			AbstractServiceProvider provider = (AbstractServiceProvider) obj;
			providerMap.put(this.serviceApp + "." + provider.getServiceGroup(), provider);
		}

	}
	
    public ServiceProvider findProvider() {
        return this.findProvider(null);
    }	

	public ServiceProvider findProvider(String serviceGroup) {
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
