package com.netto.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.netto.client.context.RpcContext;
import com.netto.client.pool.HttpConnectPool;
import com.netto.client.provider.AbstractServiceProvider;
import com.netto.client.provider.ServiceProvider;
import com.netto.context.ServiceRequest;
import com.netto.context.ServiceResponse;

public class RpcHttpClient implements InvocationHandler {
	private static Logger logger = Logger.getLogger(RpcHttpClient.class);
	private final String serviceName;
	private int timeout = 10 * 1000;
	private static Gson gson = new Gson();
	private AbstractServiceProvider provider;
	private HttpConnectPool pool;

	public RpcHttpClient(ServiceProvider provider, String serviceName, int timeout) {
		this.provider = (AbstractServiceProvider) provider;
		this.pool = (HttpConnectPool) this.provider.getPool("http");
		this.serviceName = serviceName;
		this.timeout = timeout;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		ServiceRequest req = new ServiceRequest();
		req.setMethodName(method.getName());
		req.setServiceName(serviceName);
		for (Object arg : args) {
			if (arg != null) {
				req.getArgs().add(gson.toJson(arg));
			} else {
				req.getArgs().add(null);
			}
		}
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(this.timeout)
				.setConnectionRequestTimeout(this.timeout).setSocketTimeout(this.timeout).build();

		HttpClient httpClient = this.pool.getResource();
		try {
			// 创建httppost
			HttpPost post = new HttpPost(this.provider.getRegistry());
			post.setConfig(requestConfig);
			post.addHeader("$app", this.provider.getServiceApp());
			if (RpcContext.getRouterContext() != null) {
				post.addHeader("$router", RpcContext.getRouterContext());
			}

			// 创建参数队列
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("data", gson.toJson(req)));
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(uefEntity);
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity, "UTF-8");
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
			this.pool.release(httpClient);
		}
	}

}
