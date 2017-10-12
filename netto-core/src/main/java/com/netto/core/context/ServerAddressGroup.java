package com.netto.core.context;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServerAddressGroup {
	private List<ServerAddress> servers;
	private String serverApp;
	private String serverGroup;
	private String registry;

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public String getServerApp() {
		return serverApp;
	}

	public void setServerApp(String serverApp) {
		this.serverApp = serverApp;
	}

	public String getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(String serverGroup) {
		this.serverGroup = serverGroup;
	}

	public List<ServerAddress> getServers() {
		return servers;
	}

	public void setServers(String servers) {
		List<String> serverList = Arrays.asList(servers.split(";"));
		List<ServerAddress> addresses = serverList.stream().map(server -> {
			String[] s = server.split(":");
			String host = s[0];
			int port = 1234;
			if (s.length > 1) {
				port = Integer.parseInt(s[1]);
			}

			ServerAddress address = new ServerAddress();
			address.setIp(host);
			address.setPort(port);
			return address;

		}).collect(Collectors.toList());
		this.servers = addresses;

	}
}
