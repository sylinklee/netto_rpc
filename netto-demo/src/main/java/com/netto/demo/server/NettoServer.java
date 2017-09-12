package com.netto.demo.server;

import java.util.HashMap;
import java.util.Map;

import com.netto.demo.impl.HelloServiceImpl;
import com.netto.server.NettyServer;

public class NettoServer {

	public static void main(String[] args) throws Exception {
		Map<String, Object> refBeans = new HashMap<String, Object>();
		refBeans.put("helloService", new HelloServiceImpl());
		NettyServer server = new NettyServer(12345);
		server.setRefBeans(refBeans);
		server.afterPropertiesSet();
	}

}
