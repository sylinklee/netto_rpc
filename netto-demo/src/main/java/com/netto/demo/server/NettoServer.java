package com.netto.demo.server;

import java.util.HashMap;
import java.util.Map;

import com.netto.demo.impl.HelloServiceImpl;
import com.netto.server.NettyServer;

public class NettoServer {

	public static void main(String[] args) throws Exception {
		Map<String, Object> serviceBeans = new HashMap<String, Object>();
		serviceBeans.put("helloService", new HelloServiceImpl());
		new NettyServer(12345, serviceBeans).afterPropertiesSet();

	}

}
