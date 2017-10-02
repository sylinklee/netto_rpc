package com.netto.demo.server;

import java.util.HashMap;
import java.util.Map;

import com.netto.demo.impl.HelloServiceImpl;
import com.netto.server.NettyServer;

public class NettoServer {

   
    
    
	public static void main(String[] args) throws Exception {
        Map<String, Object> refBeans = new HashMap<String, Object>();
        refBeans.put("helloService", new HelloServiceImpl());
        NettyServer server = new NettyServer(9229);
        server.setMaxWaitingQueueSize(2);
        server.setNumOfHandlerWorker(1);
        server.setMaxRequestSize(1024*1024);
        server.setNumOfIOWorkerThreads(1);
        server.setRefBeans(refBeans);
        server.afterPropertiesSet();

	}

}
