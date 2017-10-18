package com.netto.service.desc;

public class ServerDesc {
	private String registry;
	private String serverApp;
	private String serverGroup;

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("registry:").append(registry).append(" app:").append(serverApp).append(" group:").append(serverGroup);
		return sb.toString();
	}

}
