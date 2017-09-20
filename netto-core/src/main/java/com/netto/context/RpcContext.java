package com.netto.context;

import java.util.HashMap;
import java.util.Map;

public class RpcContext {
	private final static ThreadLocal<Map<String, String>> _context = new ThreadLocal<Map<String, String>>();
	private final static String routerKey = "$netto_router";

	public static void setContext(String key, String value) {
		if (_context.get() == null)
			_context.set(new HashMap<String, String>());
		_context.get().put(key, value);
	}

	public static String getContext(String key) {
		if (_context.get() == null)
			return null;
		return _context.get().get(key);
	}

	public static String getRouterContext() {
		return getContext(routerKey);
	}

	public static void setRouterContext(String value) {
		setContext(routerKey, value);
	}
}
