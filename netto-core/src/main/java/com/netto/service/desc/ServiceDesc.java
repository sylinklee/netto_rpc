package com.netto.service.desc;

public class ServiceDesc {
	private String serverApp;
	private String serviceName;
	private String interfaceClazz;
	private int timeout;
	private Boolean gateway;

	public String getServerApp() {
		return serverApp;
	}

	public void setServerApp(String serverApp) {
		this.serverApp = serverApp;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInterfaceClazz() {
		return interfaceClazz;
	}

	public void setInterfaceClazz(String interfaceClazz) {
		this.interfaceClazz = interfaceClazz;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Boolean getGateway() {
		return gateway;
	}

	public void setGateway(Boolean gateway) {
		this.gateway = gateway;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("app:").append(serverApp).append(" name:").append(serviceName).append(" timeout:")
				.append(this.timeout).append(" interface:").append(this.interfaceClazz).append(" gateway:")
				.append(gateway);
		return sb.toString();
	}

}
