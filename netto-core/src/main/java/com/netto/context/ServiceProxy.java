package com.netto.context;

import java.lang.reflect.Method;

import com.google.gson.Gson;

public class ServiceProxy {
	private ServiceRequest req;
	private Object serviceBean;
	private static Gson gson = new Gson();

	public ServiceProxy(ServiceRequest req, Object serviceBean) {
		this.req = req;
		this.serviceBean = serviceBean;
	}

	public String callService() throws Exception {
		Method m = getMethod(this.serviceBean.getClass(), this.req.getMethodName(), req.getArgs().size());
		Object[] args = new Object[req.getArgs().size()];
		for (int i = 0; i < req.getArgs().size(); i++) {
			args[i] = gson.fromJson(req.getArgs().get(i), m.getGenericParameterTypes()[i]);
		}
		Object res = m.invoke(this.serviceBean, args);
		return gson.toJson(res);
	}

	private static Method getMethod(Class<?> clazz, String methodName, int argCount) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName) && method.getParameterTypes().length == argCount)
				return method;
		}
		return null;
	}
}
