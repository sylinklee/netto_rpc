package com.netto.filter;

import com.netto.context.Invocation;

public interface InvokeMethodFilter {
	void invokeBefore(Invocation invocation);

	void invokeAfter(Invocation invocation);

	void invokeException(Invocation invocation, Throwable t);
}
