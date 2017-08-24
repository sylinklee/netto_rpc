package com.netto.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.netto.client.provider.ServiceProvider;
import com.netto.filter.InvokeMethodFilter;

public abstract class AbstactRpcClient implements InvocationHandler {
	protected static Logger logger = Logger.getLogger(AbstactRpcClient.class);
	private final String serviceName;
	private int timeout = 10 * 1000;
	protected static Gson gson = new Gson();
	private List<InvokeMethodFilter> filters;

	public AbstactRpcClient(ServiceProvider provider, List<InvokeMethodFilter> filters, String serviceName,
			int timeout) {
		this.serviceName = serviceName;
		this.timeout = timeout;
		this.filters = filters;
	}

	public String getServiceName() {
		return serviceName;
	}

	public int getTimeout() {
		return timeout;
	}

	private void invokeFiltersBefore(Object proxy, Method method, Object[] args) {
		if (this.filters == null) {
			return;
		}
		for (InvokeMethodFilter filter : filters) {
			filter.invokeBefore(proxy, method, args);
		}
	}

	private void invokeFiltersAfter(Object proxy, Method method, Object[] args) {
		if (this.filters == null) {
			return;
		}
		for (InvokeMethodFilter filter : filters) {
			filter.invokeAfter(proxy, method, args);
		}
	}

	private void invokeFiltersException(Object proxy, Method method, Object[] args, Throwable t) {
		if (this.filters == null) {
			return;
		}
		for (InvokeMethodFilter filter : filters) {
			filter.invokeException(proxy, method, args, t);
		}
	}

	protected abstract Object invokeMethod(Method method, Object[] args) throws Throwable;

	@Override
	public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			this.invokeFiltersBefore(proxy, method, args);
			return this.invokeMethod(method, args);
		} catch (Exception e) {
			this.invokeFiltersException(proxy, method, args, e);
			throw e;
		} finally {
			try {
				this.invokeFiltersAfter(proxy, method, args);
			} catch (Exception e) {
				;
			}
		}
	}

}
