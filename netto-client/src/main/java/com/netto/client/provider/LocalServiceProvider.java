package com.netto.client.provider;

import com.netto.client.pool.ConnectPool;
import com.netto.client.pool.TcpConnectPool;

public class LocalServiceProvider extends AbstractServiceProvider {
	private TcpConnectPool pool;

	public LocalServiceProvider(String registry, String serverApp, String serverGroup, TcpConnectPool pool,
			boolean needSignature) {
		super(registry, serverApp, serverGroup, needSignature);
		this.pool = pool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netto.client.provider.ServiceProvider#getPool(java.lang.String)
	 */
	public ConnectPool<?> getPool(String protocol) {
		return pool;
	}
}
