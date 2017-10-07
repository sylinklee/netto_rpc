package com.netto.client;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netto.client.pool.HttpConnectPool;
import com.netto.client.provider.ServiceProvider;
import com.netto.client.util.JsonMapperUtil;
import com.netto.core.context.RpcContext;
import com.netto.core.context.ServiceResponse;
import com.netto.core.filter.InvokeMethodFilter;
import com.netto.core.message.NettoFrame;
import com.netto.core.util.Constants;

public class RpcHttpClient extends AbstactRpcClient {
	private HttpConnectPool pool;

	public RpcHttpClient(ServiceProvider provider, List<InvokeMethodFilter> filters, String serviceName, int timeout) {
		super(provider, filters, serviceName, timeout, provider.needSignature());
		this.pool = (HttpConnectPool) provider.getPool("http");
	}

	@Override
	protected Object invokeMethod(Method method, Object[] args) throws Throwable {
		// ServiceRequest req = new ServiceRequest();
		// req.setMethodName(method.getName());
		// req.setServiceName(this.getServiceName());
		// if (args != null)
		// req.setArgs(Arrays.asList(args));

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(this.getTimeout())
				.setConnectionRequestTimeout(this.getTimeout()).setSocketTimeout(this.getTimeout()).build();

		HttpClient httpClient = this.pool.getResource();
		try {
			// 创建httppost
			HttpPost post = new HttpPost(this.getInvokeUrl(method.getName()));
			post.setConfig(requestConfig);
			post.addHeader("$app", this.getProvider().getServiceApp());
			if (RpcContext.getRouterContext() != null) {
				post.addHeader("$router", RpcContext.getRouterContext());
			}

			ObjectMapper mapper = JsonMapperUtil.getJsonMapper();
			// body
			StringWriter writer = new StringWriter();
			mapper.writeValue(writer, args);
			String requestBody = writer.toString();
			if (args != null) {
				post.addHeader(Constants.ARGSLEN_HEADER, args.length + "");
			} else {
				post.addHeader(Constants.ARGSLEN_HEADER, "0");
			}
			if (this.doSignature) {
				String signature = this.createSignature(requestBody);
				post.addHeader(NettoFrame.SIGNATURE_HEADER, signature);
			}

			StringEntity se = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
			post.setEntity(se);

			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity, "UTF-8");
			if (response.getStatusLine().getStatusCode() == 200) {

				ServiceResponse<?> res = mapper.readValue(body, mapper.getTypeFactory().constructParametricType(
						ServiceResponse.class, mapper.getTypeFactory().constructType(method.getGenericReturnType())));

				if (res.getSuccess()) {
					return res.getRetObject();
				} else {
					throw new Exception(String.valueOf(res.getErrorMessage()));
				}
			} else {
				throw new Exception(body);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			this.pool.release(httpClient);
		}
	}

	private String getInvokeUrl(String methodName) {
		StringBuilder sb = new StringBuilder(100);
		sb.append(this.getProvider().getRegistry());
		if (!this.getProvider().getRegistry().endsWith("/")) {
			sb.append("/");
		}
		sb.append(this.getProvider().getServiceApp()).append("/").append(this.getServiceName()).append("/")
				.append(methodName);
		return sb.toString();
	}
}
