package com.netto.client.provider;

import com.netto.client.pool.ConnectPool;

public interface ServiceProvider {
	ConnectPool<?> getPool(String protocol);
	
	boolean needSignature();
}
