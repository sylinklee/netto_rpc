package com.netto.client.pool;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.netto.context.ServiceAddress;

public class TcpConnectPool implements ConnectPool<Socket> {
	private List<ServiceAddress> servers = new ArrayList<ServiceAddress>();
	private GenericObjectPool<Socket> pool;

	public TcpConnectPool(List<ServiceAddress> servers, GenericObjectPoolConfig config) {
		this.servers = servers;
		pool = new GenericObjectPool<Socket>(new ClientSocketPoolFactory(), config);
	}

	public List<ServiceAddress> getServers() {
		return servers;
	}

	public Socket getResource() {

		try {
			return this.pool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void release(Socket resource) {
		if (resource == null)
			return;
		this.pool.returnObject(resource);
	}

	private class ClientSocketPoolFactory implements PooledObjectFactory<Socket> {
		public PooledObject<Socket> makeObject() throws Exception {
			// 简单策略随机取服务器，没有考虑权重
			for (int i = 0; i < servers.size(); i++) {
				try {
					int index = new Random(System.currentTimeMillis()).nextInt(servers.size());
					ServiceAddress server = servers.get(index);
					return new DefaultPooledObject<Socket>(new Socket(server.getIp(), server.getPort()));
				} catch (Exception e) {
					;
				}
			}
			throw new Exception("No server available!");
		}

		public void destroyObject(PooledObject<Socket> p) throws Exception {
			p.getObject().close();

		}

		public boolean validateObject(PooledObject<Socket> p) {
			return p.getObject().isConnected() && !p.getObject().isClosed();
		}

		public void activateObject(PooledObject<Socket> p) throws Exception {
			if (p.getObject().isClosed()) {
				p.getObject().connect(p.getObject().getRemoteSocketAddress(), p.getObject().getSoTimeout());
			}

		}

		public void passivateObject(PooledObject<Socket> p) throws Exception {
		}
	}
}
