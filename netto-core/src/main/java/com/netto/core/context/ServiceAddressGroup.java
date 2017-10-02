package com.netto.core.context;

import java.util.List;

public class ServiceAddressGroup {
	private List<ServiceAddress> servers;
	private String serviceApp;
	private String serviceGroup;
	private String registry;

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public List<ServiceAddress> getServers() {
		return servers;
	}

	public void setServers(List<ServiceAddress> servers) {
		this.servers = servers;
	}

	public String getServiceApp() {
		return serviceApp;
	}

	public void setServiceApp(String serviceApp) {
		this.serviceApp = serviceApp;
	}

	public String getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(String serviceGroup) {
		this.serviceGroup = serviceGroup;
	}
}
