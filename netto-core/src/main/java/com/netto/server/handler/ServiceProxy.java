package com.netto.server.handler;

import java.lang.reflect.Method;
import java.util.List;

import com.google.gson.Gson;
import com.netto.context.ServiceRequest;
import com.netto.filter.Invocation;
import com.netto.filter.InvokeMethodFilter;

public class ServiceProxy {
	private ServiceRequest req;
	private Object serviceBean;
	private static Gson gson = new Gson();
	private List<InvokeMethodFilter> filters;

	public ServiceProxy(ServiceRequest req, Object serviceBean, List<InvokeMethodFilter> filters) {
		this.req = req;
		this.serviceBean = serviceBean;
		this.filters = filters;
	}

	public String callService() throws Throwable {

		Method m = getMethod(this.serviceBean.getClass(), this.req.getMethodName(), req.getArgs().size());
		Object[] args = new Object[req.getArgs().size()];
		Invocation invocation = new Invocation(this.req.getServiceName(), this.serviceBean, m, args);
		try {
			this.invokeFiltersBefore(invocation);
			for (int i = 0; i < req.getArgs().size(); i++) {
				args[i] = gson.fromJson(req.getArgs().get(i), m.getGenericParameterTypes()[i]);
			}
			Object res = m.invoke(this.serviceBean, args);
			return gson.toJson(res);
		} catch (Throwable t) {
			this.invokeFiltersException(invocation, t);
			throw t;
		} finally {
			try {
				this.invokeFiltersAfter(invocation);
			} catch (Exception e) {
				;
			}
		}
	}

	private static Method getMethod(Class<?> clazz, String methodName, int argCount) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName) && method.getParameterTypes().length == argCount)
				return method;
		}
		return null;
	}

	private void invokeFiltersBefore(Invocation invocation) {
		if (this.filters == null) {
			return;
		}
		for (InvokeMethodFilter filter : filters) {
			filter.invokeBefore(invocation);
		}
	}

	private void invokeFiltersAfter(Invocation invocation) {
		if (this.filters == null) {
			return;
		}
		for (InvokeMethodFilter filter : filters) {
			filter.invokeAfter(invocation);
		}
	}

	private void invokeFiltersException(Invocation invocation, Throwable t) {
		if (this.filters == null) {
			return;
		}
		for (InvokeMethodFilter filter : filters) {
			filter.invokeException(invocation, t);
		}
	}
}
