package com.netto.context;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequest {
	private String serviceName;
	private String methodName;
	private List<String> args = new ArrayList<String>();

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getArgs() {
		return args;
	}
}
