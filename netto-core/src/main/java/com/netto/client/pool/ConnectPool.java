package com.netto.client.pool;

public interface ConnectPool<T> {
	T getResource();

	void release(T resource);
}
