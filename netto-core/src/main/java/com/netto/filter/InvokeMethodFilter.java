package com.netto.filter;

public interface InvokeMethodFilter {
	void invokeBefore(Invocation invocation);

	void invokeAfter(Invocation invocation);

	void invokeException(Invocation invocation, Throwable t);
}
