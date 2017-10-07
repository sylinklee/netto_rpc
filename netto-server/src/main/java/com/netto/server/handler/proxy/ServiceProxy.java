package com.netto.server.handler.proxy;

import java.lang.reflect.Method;
import java.util.List;

import com.netto.core.context.Invocation;
import com.netto.core.filter.InvokeMethodFilter;
import com.netto.server.bean.NettoServiceBean;
import com.netto.server.bean.ServiceRequest;

public class ServiceProxy {
	private ServiceRequest req;
	private NettoServiceBean serviceBean;

	private List<InvokeMethodFilter> filters;

	public ServiceProxy(ServiceRequest req, NettoServiceBean serviceBean, List<InvokeMethodFilter> filters) {
		this.req = req;
		this.serviceBean = serviceBean;
		this.filters = filters;
	}

	public Object callService() throws Throwable {
		int size = req.getArgs() == null ? 0 : req.getArgs().length;

		Method m = getMethod(this.serviceBean.getObject().getClass(), this.req.getMethodName(), size);
		Invocation invocation = new Invocation(this.req.getServiceName(), this.serviceBean, m, req.getArgs());
		try {
			this.invokeFiltersBefore(invocation);
			// for (int i = 0; i < req.getArgs().size(); i++) {
			// args[i] = gson.fromJson(req.getArgs().get(i),
			// m.getGenericParameterTypes()[i]);
			// }
			Object res = m.invoke(this.serviceBean.getObject(), req.getArgs());
			return res;
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
