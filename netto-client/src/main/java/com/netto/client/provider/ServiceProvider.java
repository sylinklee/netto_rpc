package com.netto.client.provider;

import com.netto.client.pool.ConnectPool;
import com.netto.core.context.RouteConfig;
import com.netto.service.desc.ServerDesc;

public interface ServiceProvider {
	ServerDesc getServerDesc();

	RouteConfig getRouteConfig();

	void setRouteConfig(RouteConfig routeConfig);

	ConnectPool<?> getPool(String protocol);

	boolean needSignature();
}
