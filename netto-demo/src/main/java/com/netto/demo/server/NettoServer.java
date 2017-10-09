package com.netto.demo.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.netto.demo.impl.HelloServiceImpl;
import com.netto.server.NettyServer;
import com.netto.server.bean.NettoServiceBean;
import com.netto.server.bean.ServiceBean;

public class NettoServer {

	public static void main(String[] args) throws Exception {
		Map<String, NettoServiceBean> serviceBeans = new HashMap<String, NettoServiceBean>();
		ServiceBean serviceBean = new ServiceBean();
		serviceBean.setRefName("helloService");
		serviceBean.setTimeout(2000);
		serviceBean.setServiceName("helloService");
		Object refBean = new HelloServiceImpl();

		NettoServiceBean nettoBean = new NettoServiceBean(serviceBean, refBean);
		serviceBeans.put("helloService", nettoBean);

		NettyServer server = new NettyServer("netto-demo", "base", 9229);
		server.setMaxWaitingQueueSize(2);
		server.setNumOfHandlerWorker(1);
		server.setMaxRequestSize(1024 * 1024);
		server.setNumOfIOWorkerThreads(1);
		server.setServiceBeans(serviceBeans);
		server.afterPropertiesSet();
		CountDownLatch latch = new CountDownLatch(1);
		latch.await();
	}

}
