package com.netto.demo.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.netto.client.bean.ReferenceBean;
import com.netto.client.pool.TcpConnectPool;
import com.netto.client.provider.LocalServiceProvider;
import com.netto.client.provider.NginxServiceProvider;
import com.netto.client.router.ServiceRouter;
import com.netto.context.ServiceAddress;
import com.netto.demo.Book;
import com.netto.demo.HelloService;
import com.netto.demo.User;

public class NettoClient {
	public static void main(String[] args) throws Exception {
		local_tcp();
	}

	public static void nginx_http_tcp() throws Exception {
		NginxServiceProvider provider = new NginxServiceProvider("http://192.168.150.150:8080/", "netto-demo", "*");
		ServiceRouter router = new ServiceRouter(provider);

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceUri("netto-demo/helloService");
		refer.setRouter(router);
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
		NginxServiceProvider provider = new NginxServiceProvider("http://192.168.150.150:8080/", "netto-demo", "*");
		ServiceRouter router = new ServiceRouter(provider);

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceUri("netto-demo/helloService");
		refer.setRouter(router);
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
		TcpConnectPool pool = new TcpConnectPool(servers, new GenericObjectPoolConfig());

		LocalServiceProvider provider = new LocalServiceProvider(null, "netto-demo", "*", pool);

		ServiceRouter router = new ServiceRouter(provider);

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceUri("netto-demo/helloService");
		refer.setRouter(router);
		refer.setInterfaceClazz(HelloService.class);
		refer.setTimeout(20 * 1000);
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
