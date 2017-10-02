package com.netto.core.context;

import java.util.List;

public class ServiceRequest {
	private String serviceName;
	private String methodName;
	private int argsLength = 0;

	public int getArgsLength() {
		return argsLength;
	}

	private List<Object> args;

	public void setArgs(List<Object> args) {
		this.args = args;
		if (args != null) {
			argsLength = args.size();
		}
	}

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

	public List<Object> getArgs() {
		return args;
	}

}
