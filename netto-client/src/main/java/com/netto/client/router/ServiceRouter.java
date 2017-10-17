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
	private String serverApp;
	private String serverGroup;

	public ServiceRouter(String serverApp, String serverGroup, List<ServiceProvider> providers,
			Map<String, String> routers) {
		this.serverApp = serverApp;
		this.serverGroup = serverGroup;
		this.routers = routers;
		for (ServiceProvider obj : providers) {
			AbstractServiceProvider provider = (AbstractServiceProvider) obj;
			providerMap.put(this.serverApp + "." + provider.getServerGroup(), provider);
		}

	}

	public ServiceProvider findProvider() {
		return this.findProvider(this.serverGroup);
	}

	private ServiceProvider findProvider(String serverGroup) {
		if (serverGroup == null) {
			if (RpcContext.getRouterContext() != null) {
				if (routers != null) {
					serverGroup = this.routers.get(RpcContext.getRouterContext());
				}
			}
		}
		if (serverGroup == null) {
			serverGroup = "*";
		}
		return this.providerMap.get(serverApp + "." + serverGroup);
	}
}
