package com.netto.client.pool;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

public class HttpConnectPool implements ConnectPool<HttpClient> {
	private static Logger logger = Logger.getLogger(HttpConnectPool.class);

	public HttpClient getResource() {
		return HttpClients.createDefault();
	}

	public void release(HttpClient resource) {
		if (resource instanceof CloseableHttpClient) {
			try {
				((CloseableHttpClient) resource).close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
