package com.netto.service.desc;

public class ServerDesc {
	private String serviceApp;
	private String serviceGroup;

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("app:").append(serviceApp).append(" group:").append(serviceGroup);
		return sb.toString();
	}

}
