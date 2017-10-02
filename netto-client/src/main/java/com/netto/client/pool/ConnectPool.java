package com.netto.client.pool;

public interface ConnectPool<T> {
	T getResource();
	
	void invalidate(T resource);

	void release(T resource);
}
