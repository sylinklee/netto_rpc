package com.netto.demo.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.netto.client.bean.ReferenceBean;
import com.netto.client.router.ServiceRouterFactory;
import com.netto.context.ServiceAddress;
import com.netto.context.ServiceAddressGroup;
import com.netto.demo.Book;
import com.netto.demo.HelloService;
import com.netto.demo.User;

public class NettoClient {
	private static ServiceRouterFactory routerFactory;

	public static void main(String[] args) throws Exception {
		local_tcp();
		CountDownLatch latch = new CountDownLatch(1);
		latch.await();
	}

	public static void nginx_http_tcp() throws Exception {
		ServiceAddressGroup serverGroup = new ServiceAddressGroup();
		serverGroup.setRegistry("http://192.168.150.150:8080/");
		serverGroup.setServiceApp("netto-demo");
		serverGroup.setServiceGroup("*");

		routerFactory = new ServiceRouterFactory();
		routerFactory.setServerGroup(serverGroup);

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceUri("netto-demo/helloService");
		refer.setRouterFactory(routerFactory);
		refer.setInterfaceClazz(HelloService.class);
		refer.setTimeout(20 * 1000);
		refer.setProtocol("http");

		System.out.println("nginx_http_tcp begin-----------");
		HelloService helloProxy = (HelloService) refer.getObject();
		String res = helloProxy.sayHello("netto");
		System.out.println(res);
		Map<String, List<User>> users = new HashMap<String, List<User>>();
		users.put("abc", new ArrayList<User>());
		User u = new User();
		u.setName("test1");
		u.setAge(10);
		List<Book> books = new ArrayList<Book>();
		Book book = new Book();
		book.setName("人类简史");
		books.add(book);
		u.setBooks(books);
		users.get("abc").add(u);
		List<User> list = helloProxy.saveUsers(users);
		// List<User> list = helloProxy.updateUsers(users.get("abc"));
		for (User u1 : list) {
			System.out.println("name=" + u1.getName() + ",age=" + u1.getAge());
		}
		System.out.println("nginx_http_tcp end-----------");
	}

	public static void nginx_tcp() throws Exception {
		ServiceAddressGroup serverGroup = new ServiceAddressGroup();
		serverGroup.setRegistry("http://192.168.150.150:8080/");
		serverGroup.setServiceApp("netto-demo");
		serverGroup.setServiceGroup("*");

		routerFactory = new ServiceRouterFactory();
		routerFactory.setServerGroup(serverGroup);

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceUri("netto-demo/helloService");
		refer.setRouterFactory(routerFactory);
		refer.setInterfaceClazz(HelloService.class);
		refer.setTimeout(20 * 1000);
		refer.setProtocol("tcp");
		System.out.println("nginx_tcp begin-----------");
		HelloService helloProxy = (HelloService) refer.getObject();
		String res = helloProxy.sayHello("netto");
		System.out.println(res);
		Map<String, List<User>> users = new HashMap<String, List<User>>();
		users.put("abc", new ArrayList<User>());
		User u = new User();
		u.setName("test1");
		u.setAge(10);
		List<Book> books = new ArrayList<Book>();
		Book book = new Book();
		book.setName("人类简史");
		books.add(book);
		u.setBooks(books);
		users.get("abc").add(u);
		List<User> list = helloProxy.saveUsers(users);
		// List<User> list = helloProxy.updateUsers(users.get("abc"));
		for (User u1 : list) {
			System.out.println("name=" + u1.getName() + ",age=" + u1.getAge());
		}
		System.out.println("nginx_tcp end-----------");
	}

	public static void local_tcp() throws Exception {
		List<ServiceAddress> servers = new ArrayList<ServiceAddress>();
		ServiceAddress address = new ServiceAddress();
		address.setIp("localhost");
		address.setPort(12345);
		servers.add(address);

		ServiceAddressGroup serverGroup = new ServiceAddressGroup();
		serverGroup.setRegistry(null);
		serverGroup.setServiceApp("netto-demo");
		serverGroup.setServiceGroup("*");
		serverGroup.setServers(servers);

		routerFactory = new ServiceRouterFactory();
		routerFactory.setServerGroup(serverGroup);

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceUri("netto-demo/helloService");
		refer.setRouterFactory(routerFactory);
		refer.setInterfaceClazz(HelloService.class);
		refer.setTimeout(2 * 1000);
		refer.setProtocol("tcp");

		System.out.println("local_tcp begin-----------");
		HelloService helloProxy = (HelloService) refer.getObject();
		String res = helloProxy.sayHello("netto");
		System.out.println(res);
		Map<String, List<User>> users = new HashMap<String, List<User>>();
		users.put("abc", new ArrayList<User>());
		User u = new User();
		u.setName("test1");
		u.setAge(10);
		List<Book> books = new ArrayList<Book>();
		Book book = new Book();
		book.setName("人类简史");
		books.add(book);
		u.setBooks(books);
		users.get("abc").add(u);
		List<User> list = helloProxy.saveUsers(users);
		// List<User> list = helloProxy.updateUsers(users.get("abc"));
		for (User u1 : list) {
			System.out.println("name=" + u1.getName() + ",age=" + u1.getAge());
		}
		System.out.println("local_tcp end-----------");
	}

}
