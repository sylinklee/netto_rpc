package com.netto.core.filter;

import com.netto.core.context.Invocation;

public interface InvokeMethodFilter {
	void invokeBefore(Invocation invocation);

	void invokeAfter(Invocation invocation);

	void invokeException(Invocation invocation, Throwable t);
}
