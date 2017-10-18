package com.netto.client.router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netto.client.provider.AbstractServiceProvider;
import com.netto.client.provider.ServiceProvider;
import com.netto.core.context.RouteConfig;
import com.netto.core.context.RpcContext;
import com.netto.core.util.Constants;
import com.netto.service.desc.ServerDesc;

public class ServiceRouter {
	private Map<String, RouteConfig> routers;
	private Map<String, ServiceProvider> providerMap = new HashMap<String, ServiceProvider>();
	private ServerDesc serverDesc;

	public ServiceRouter(ServerDesc serverDesc, List<ServiceProvider> providers, Map<String, RouteConfig> routers) {
		this.serverDesc = serverDesc;
		this.routers = routers;
		for (ServiceProvider obj : providers) {
			AbstractServiceProvider provider = (AbstractServiceProvider) obj;
			providerMap.put(provider.getServerDesc().toString(), provider);
		}

	}

	public ServiceProvider findProvider() {
		return this.findProvider(this.serverDesc);
	}

	private ServiceProvider findProvider(ServerDesc serverDesc) {
		RouteConfig routeConfig = null;
		if (serverDesc.getServerGroup() == null) {
			if (RpcContext.getRouterContext() != null) {
				if (routers != null) {
					routeConfig = this.routers.get(RpcContext.getRouterContext());
				}
			}
		}
		if (serverDesc.getServerGroup() == null) {
			if (routeConfig == null) {
				serverDesc.setServerGroup(Constants.DEFAULT_SERVER_GROUP);
			} else {
				serverDesc.setServerGroup(routeConfig.getTargetServerGroup());
			}
		}
		ServiceProvider provider = this.providerMap.get(serverDesc.toString());
		if (provider != null) {
			provider.setRouteConfig(routeConfig);
		}
		return provider;
	}
}
