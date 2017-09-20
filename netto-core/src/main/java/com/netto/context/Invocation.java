package com.netto.context;

import java.lang.reflect.Method;

public class Invocation {
	private String serviceName;
	private Object proxy;
	private Method method;
	private Object[] args;

	public Invocation(String serviceName, Object proxy, Method method, Object[] args) {
		this.serviceName = serviceName;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
	}

	public String getServiceName() {
		return serviceName;
	}

	public Object getProxy() {
		return proxy;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}

}
