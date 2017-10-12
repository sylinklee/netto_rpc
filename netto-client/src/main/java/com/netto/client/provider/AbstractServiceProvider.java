package com.netto.client.provider;

public abstract class AbstractServiceProvider implements ServiceProvider {
	private String registry; // 注册中心
	private String serverApp; // 服务APP
	private String serverGroup; // 服务APP下的服务分组
    private boolean needSignature;

	public AbstractServiceProvider(String registry, String serverApp, String serverGroup,boolean needSignature) {
		this.registry = registry;
		this.serverApp = serverApp;
		this.serverGroup = serverGroup;
		this.needSignature = true;
	}

	public String getRegistry() {
		return registry;
	}

	public String getServerApp() {
		return serverApp;
	}

	public String getServerGroup() {
		return serverGroup;
	}
	
	public boolean needSignature(){
	    return this.needSignature;
	}

}
