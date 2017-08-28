package com.netto.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.netto.client.pool.TcpConnectPool;
import com.netto.client.provider.ServiceProvider;
import com.netto.context.ServiceRequest;
import com.netto.context.ServiceResponse;
import com.netto.filter.InvokeMethodFilter;

public class RpcTcpClient extends AbstactRpcClient {
	private static Logger logger = Logger.getLogger(RpcTcpClient.class);
	private TcpConnectPool pool;
	private static Gson gson = new Gson();

	public RpcTcpClient(ServiceProvider provider, List<InvokeMethodFilter> filters, String serviceName, int timeout) {
		super(provider, filters, serviceName, timeout);
		this.pool = (TcpConnectPool) provider.getPool("tcp");
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
		Socket socket = null;
		try {
			socket = this.pool.getResource();
			socket.setSoTimeout(this.getTimeout());
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			osw.write(gson.toJson(req));
			osw.flush();

			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String body = br.readLine();

			ServiceResponse res = gson.fromJson(body, ServiceResponse.class);
			if (res.getSuccess()) {
				return gson.fromJson(res.getBody(), method.getGenericReturnType());
			} else {
				throw new Exception(res.getBody());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof SocketException) {
				try {
					socket.close();
				} catch (Throwable t) {
					;
				}
			}
			throw e;
		} finally {
			this.pool.release(socket);
		}
	}
}
