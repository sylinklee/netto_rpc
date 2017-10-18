package com.netto.client.provider;

import com.netto.client.pool.ConnectPool;
import com.netto.client.pool.TcpConnectPool;
import com.netto.service.desc.ServerDesc;

public class LocalServiceProvider extends AbstractServiceProvider {
	private TcpConnectPool pool;

	public LocalServiceProvider(ServerDesc serverDesc, TcpConnectPool pool, boolean needSignature) {
		super(serverDesc, needSignature);
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
