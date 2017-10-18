package com.netto.server.bean;

import com.netto.core.util.Constants;

/**
 * 主要定义服务的规则，如限流，过滤器等
 * 
 * @author lidong
 *
 */
public class ServiceBean {
	private String refName;

	private String serviceName;

	private int timeout = Constants.DEFAULT_TIMEOUT;

	private Boolean gateway = false;

	public Boolean getGateway() {
		return gateway;
	}

	public void setGateway(Boolean gateway) {
		this.gateway = gateway;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String name) {
		this.serviceName = name;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
