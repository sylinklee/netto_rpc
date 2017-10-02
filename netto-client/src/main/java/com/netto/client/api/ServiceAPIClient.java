package com.netto.client.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netto.client.bean.ReferenceBean;
import com.netto.client.provider.ServiceProvider;
import com.netto.core.context.ServiceRequest;
import com.netto.core.context.ServiceResponse;
import com.netto.core.message.NettoFrame;
import com.netto.core.util.JsonMapperUtil;
import com.netto.service.desc.ServiceDescApi;

/**
 * 獲取服務器端服務配置信息
 * 
 * @author admin
 *
 */
public class ServiceAPIClient {
//	private static Logger logger = Logger.getLogger(ServiceAPIClient.class);
	private ServiceDescApi api;
	private Socket socket;
	private int timeout;

	public ServiceAPIClient(Socket socket, int timeout) {
		this.socket = socket;
		this.timeout = timeout;
	}

	public ServiceAPIClient(ServiceProvider provider, ReferenceBean refer) {
		ReferenceBean apiRefer = new ReferenceBean();
		apiRefer.setInterfaceClazz(ServiceDescApi.class);
		apiRefer.setProtocol(refer.getProtocol());
		apiRefer.setRouter(refer.getRouter());
		apiRefer.setServiceName("$serviceDesc");
		apiRefer.setTimeout(1000);
		try {
			api = (ServiceDescApi) apiRefer.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int getServerTimeout(String serviceName) {
		if (api == null)
			throw new RuntimeException("api is null,not suppport!");
		return api.queryServiceTimeout(serviceName);
	}

	public int pingService(String data) {
		if (api != null) {
			return api.pingService(data);
		}
		try {
			ServiceRequest req = new ServiceRequest();
			req.setMethodName("pingService");
			req.setServiceName("$serviceDesc");
			req.setArgs(Arrays.asList(new Object[] { data }));
			socket.setSoTimeout(timeout);
			OutputStream os = socket.getOutputStream();
			ObjectMapper mapper = JsonMapperUtil.getJsonMapper();
			String requestBody = mapper.writeValueAsString(req);
			byte[] byteBody = requestBody.getBytes("UTF-8");
			StringWriter headerWriter = new StringWriter(128);
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
					ServiceResponse.class, mapper.getTypeFactory().constructType(Integer.class)));
			if (res.getSuccess()) {
				return (Integer) res.getRetObject();

			} else {
				throw new RuntimeException(res.getErrorMessage());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
