package com.netto.core.context;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

	public List<ServiceAddress> getServers() {
		return servers;
	}

	public void setServers(String servers) {
		List<String> serverList = Arrays.asList(servers.split(";"));
		List<ServiceAddress> addresses = serverList.stream().map(server -> {
			String[] s = server.split(":");
			String host = s[0];
			int port = 1234;
			if (s.length > 1) {
				port = Integer.parseInt(s[1]);
			}

			ServiceAddress address = new ServiceAddress();
			address.setIp(host);
			address.setPort(port);
			return address;

		}).collect(Collectors.toList());
		this.servers = addresses;

	}
}
