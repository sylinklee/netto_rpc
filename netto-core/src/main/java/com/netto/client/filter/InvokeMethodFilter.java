package com.netto.client.filter;

import java.lang.reflect.Method;

public interface InvokeMethodFilter {
	void invokeBefore(Object proxy, Method method, Object[] args);

	void invokeAfter(Object proxy, Method method, Object[] args);

	void invokeException(Object proxy, Method method, Object[] args, Throwable t);
}
