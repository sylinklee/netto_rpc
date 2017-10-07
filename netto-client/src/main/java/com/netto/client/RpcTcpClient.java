package com.netto.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netto.client.pool.TcpConnectPool;
import com.netto.client.provider.ServiceProvider;
import com.netto.client.util.JsonMapperUtil;
import com.netto.core.context.ServiceResponse;
import com.netto.core.filter.InvokeMethodFilter;
import com.netto.core.message.NettoFrame;
import com.netto.core.util.Constants;

public class RpcTcpClient extends AbstactRpcClient {
	private static Logger logger = Logger.getLogger(RpcTcpClient.class);
	private TcpConnectPool pool;

	public RpcTcpClient(ServiceProvider provider, List<InvokeMethodFilter> filters, String serviceName, int timeout) {
		super(provider, filters, serviceName, timeout, provider.needSignature());
		this.pool = (TcpConnectPool) provider.getPool("tcp");
	}

	@Override
	protected Object invokeMethod(Method method, Object[] args) throws Throwable {
		// ServiceRequest req = new ServiceRequest();
		// req.setMethodName(method.getName());
		// req.setServiceName(this.getServiceName());
		// if (args != null)
		// req.setArgs(Arrays.asList(args));

		Socket socket = null;
		try {
			ObjectMapper mapper = JsonMapperUtil.getJsonMapper();
			socket = this.pool.getResource();
			socket.setSoTimeout(this.getTimeout());
			OutputStream os = socket.getOutputStream();
			String requestBody = mapper.writeValueAsString(args);
			byte[] byteBody = requestBody.getBytes("UTF-8");
			StringWriter headerWriter = new StringWriter(128);
			headerWriter.append(Constants.SERVICE_HEADER).append(":").append(this.getServiceName()).append("\r\n");
			headerWriter.append(Constants.METHOD_HEADER).append(":").append(method.getName()).append("\r\n");
			if (args != null) {
				headerWriter.append(Constants.ARGSLEN_HEADER).append(":").append(args.length + "");
			} else {
				headerWriter.append(Constants.ARGSLEN_HEADER).append(":0");
			}

			if (this.doSignature) {
				headerWriter.append("\r\n");
				String signature = this.createSignature(requestBody);
				headerWriter.append(NettoFrame.SIGNATURE_HEADER).append(":").append(signature);
			}

			byte[] headerContentBytes = headerWriter.toString().getBytes("UTF-8");

			String header = String.format("%s:%d/%d/%d", NettoFrame.NETTO_HEADER_START, 2, headerContentBytes.length,
					byteBody.length);
			byte[] headerBytes = new byte[NettoFrame.HEADER_LENGTH];

			Arrays.fill(headerBytes, (byte) ' ');
			System.arraycopy(header.getBytes("utf-8"), 0, headerBytes, 0, header.length());
			os.write(headerBytes);
			os.write(headerContentBytes);
			os.flush();
			os.write(byteBody);

			os.flush();

			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String body = br.readLine();

			ServiceResponse<?> res = mapper.readValue(body, mapper.getTypeFactory().constructParametricType(
					ServiceResponse.class, mapper.getTypeFactory().constructType(method.getGenericReturnType())));
			if (res.getSuccess()) {
				return res.getRetObject();// mapper.readValue(res.getBody(),
											// mapper.getTypeFactory().constructType(method.getGenericReturnType()));
			} else {
				throw new Exception(String.valueOf(res.getErrorMessage()));
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof SocketException) {
				socket.close();
				this.pool.invalidate(socket);
			}
			throw e;
		} finally {
			this.pool.release(socket);
		}
	}
}
