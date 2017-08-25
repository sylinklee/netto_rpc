package com.netto.client;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.netto.client.channel.SyncChannel;
import com.netto.client.pool.NettyConnectPool;
import com.netto.client.provider.ServiceProvider;
import com.netto.context.ServiceRequest;
import com.netto.context.ServiceResponse;
import com.netto.filter.InvokeMethodFilter;

public class RpcNettyClient extends AbstactRpcClient {
	private static Logger logger = Logger.getLogger(RpcTcpClient.class);
	private NettyConnectPool pool;
	private static Gson gson = new Gson();

	public RpcNettyClient(ServiceProvider provider, List<InvokeMethodFilter> filters, String serviceName, int timeout) {
		super(provider, filters, serviceName, timeout);
		this.pool = (NettyConnectPool) provider.getPool("tcp");
		this.pool.setTimeout(timeout);
	}

	@Override
	protected Object invokeMethod(Method method, Object[] args) throws Throwable {
		ServiceRequest req = new ServiceRequest();
		req.setMethodName(method.getName());
		req.setServiceName(this.getServiceName());
		if (args != null) {
			for (Object arg : args) {
				if (arg != null) {
					req.getArgs().add(gson.toJson(arg));
				} else {
					req.getArgs().add(null);
				}
			}
		}
		SyncChannel channel = null;
		try {
			channel = this.pool.getResource();
			channel.writeAndFlush(gson.toJson(req));
			String body = channel.readLine();
			ServiceResponse res = gson.fromJson(body, ServiceResponse.class);
			if (res.getSuccess()) {
				return gson.fromJson(res.getBody(), method.getGenericReturnType());
			} else {
				throw new Exception(res.getBody());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			this.pool.release(channel);
		}
	}

}
