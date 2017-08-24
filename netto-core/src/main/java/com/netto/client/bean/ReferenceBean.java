package com.netto.client.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.netto.client.RpcHttpClient;
import com.netto.client.RpcTcpClient;
import com.netto.client.router.ServiceRouterFactory;

public class ReferenceBean implements FactoryBean<Object> {
	private String protocol = "tcp"; // tcp,http
	private Class<?> interfaceClazz;
	private String serviceUri; // app1/service1
	private String serviceApp;
	private String serviceName;
	private int timeout;
	private ServiceRouterFactory routerFactory;

	public ServiceRouterFactory getRouterFactory() {
		return routerFactory;
	}

	public void setRouterFactory(ServiceRouterFactory routerFactory) {
		this.routerFactory = routerFactory;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Class<?> getInterfaceClazz() {
		return interfaceClazz;
	}

	public void setInterfaceClazz(Class<?> interfaceClazz) {
		this.interfaceClazz = interfaceClazz;
	}

	public String getServiceUri() {
		return this.serviceUri;
	}

	public void setServiceUri(String serviceUri) {
		String[] temps = serviceUri.split("/");
		this.serviceName = temps[1];
		this.serviceApp = temps[0];
		this.serviceUri = serviceUri;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Object getObject() throws Exception {
		InvocationHandler client;
		if (protocol.equals("tcp")) {
			client = new RpcTcpClient(this.getRouterFactory().getObject().findProvider(serviceApp, null),
					this.routerFactory.getFilters(), serviceName, this.timeout);
		} else {
			client = new RpcHttpClient(this.getRouterFactory().getObject().findProvider(serviceApp, null),
					this.routerFactory.getFilters(), this.serviceName, this.timeout);
		}
		Object proxy = Proxy.newProxyInstance(interfaceClazz.getClassLoader(), new Class<?>[] { interfaceClazz },
				client);
		return proxy;
	}

	public Class<?> getObjectType() {
		return this.interfaceClazz;
	}

	public boolean isSingleton() {
		return true;
	}
}
