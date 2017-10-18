package com.netto.core.context;

import com.netto.core.util.Constants;

public class RouteConfig {
	private String serverApp;
	private String routeInfo;
	private String targetRegistry = Constants.DEFAULT_TARGET_REGISTRY;
	private String targetServerGroup = Constants.DEFAULT_SERVER_GROUP;

	public String getServerApp() {
		return serverApp;
	}

	public void setServerApp(String serverApp) {
		this.serverApp = serverApp;
	}

	public String getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(String routeInfo) {
		this.routeInfo = routeInfo;
	}

	public String getTargetRegistry() {
		return targetRegistry;
	}

	public void setTargetRegistry(String targetRegistry) {
		this.targetRegistry = targetRegistry;
	}

	public String getTargetServerGroup() {
		return targetServerGroup;
	}

	public void setTargetServerGroup(String targetServerGroup) {
		this.targetServerGroup = targetServerGroup;
	}

}
