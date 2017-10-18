package com.netto.client.provider;

import com.netto.core.context.RouteConfig;
import com.netto.service.desc.ServerDesc;

public abstract class AbstractServiceProvider implements ServiceProvider {
	private ServerDesc serverDesc;
	private RouteConfig routeConfig; // 服务的路由信息
	private boolean needSignature;

	public AbstractServiceProvider(ServerDesc serverDesc, boolean needSignature) {
		this.serverDesc = serverDesc;
		this.needSignature = true;
	}

	public ServerDesc getServerDesc() {
		return this.serverDesc;
	}

	public RouteConfig getRouteConfig() {
		return this.routeConfig;
	}

	public void setRouteConfig(RouteConfig routeConfig) {
		this.routeConfig = routeConfig;
	}

	public boolean needSignature() {
		return this.needSignature;
	}

}
