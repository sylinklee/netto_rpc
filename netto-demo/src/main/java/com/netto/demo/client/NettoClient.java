package com.netto.demo.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.netto.client.bean.ReferenceBean;
import com.netto.client.router.ServiceRouterFactory;
import com.netto.core.context.ServiceAddressGroup;
import com.netto.core.util.DesUtil;
import com.netto.demo.Book;
import com.netto.demo.HelloService;
import com.netto.demo.User;
import com.netto.service.desc.MethodDesc;
import com.netto.service.desc.ServerDesc;
import com.netto.service.desc.ServiceDesc;
import com.netto.service.desc.ServiceDescApi;

public class NettoClient {
	private static ServiceRouterFactory routerFactory;

	public static void main(String[] args) throws Exception {
		// nginx_schedule();
		local_tcp();
		CountDownLatch latch = new CountDownLatch(1);
		latch.await();
	}

	public static void nginx_http_tcp() throws Exception {

		routerFactory = new ServiceRouterFactory();

		ServiceAddressGroup serverGroup = new ServiceAddressGroup();
		routerFactory.setServerGroup(serverGroup);
		serverGroup.setRegistry("http://192.168.2.38:8330/api/");
		serverGroup.setServiceApp("netto-demo");
		serverGroup.setServiceGroup("base");
		routerFactory.afterPropertiesSet();

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceName("helloService");
		refer.setRouter(routerFactory.getObject());
		refer.setInterfaceClazz(HelloService.class);
		refer.setTimeout(20 * 1000);
		refer.setProtocol("http");

		System.out.println("nginx_http_tcp begin-----------");
		HelloService helloProxy = (HelloService) refer.getObject();
		String res = helloProxy.sayByeBye(8);
		res = helloProxy.sayHello("netto");
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
		routerFactory = new ServiceRouterFactory();
		ServiceAddressGroup serverGroup = new ServiceAddressGroup();
		routerFactory.setServerGroup(serverGroup);
		serverGroup.setRegistry("http://127.0.0.1:8330/api/");
		serverGroup.setServiceApp("netto-demo");
		serverGroup.setServiceGroup("base");
		routerFactory.setNeedSignature(true);
		routerFactory.afterPropertiesSet();

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceName("helloService");
		refer.setRouter(routerFactory.getObject());
		refer.setInterfaceClazz(HelloService.class);
		refer.setTimeout(20 * 1000);
		refer.setProtocol("tcp");
		System.out.println("nginx_tcp begin-----------");
		HelloService helloProxy = (HelloService) refer.getObject();
		String res = helloProxy.sayByeBye(8);
		res = helloProxy.sayHello("netto");
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

		routerFactory = new ServiceRouterFactory();
		ServiceAddressGroup serverGroup = new ServiceAddressGroup();
		serverGroup.setServiceApp("netto-demo");
		serverGroup.setServiceGroup("base");
		serverGroup.setServers("127.0.0.1:9229");
		routerFactory.setServerGroup(serverGroup);
		routerFactory.afterPropertiesSet();

		ReferenceBean refer = new ReferenceBean();
		refer.setServiceName("helloService");
		refer.setRouter(routerFactory.getObject());
		refer.setInterfaceClazz(HelloService.class);
		// refer.setTimeout(2000 * 1000);
		refer.setProtocol("tcp");

		System.out.println("local_tcp begin-----------");
		HelloService helloProxy = (HelloService) refer.getObject();

		List<User> users1 = helloProxy.queryUsers();
		System.out.println("无参数的返回值为" + users1.size());
		// String res = helloProxy.sayHello("netto");
		// System.out.println(res);
		Map<String, List<User>> users = new HashMap<String, List<User>>();
		users.put("abc", new ArrayList<User>());
		User u = new User();
		u.setName("test1");
		u.setAge(10);
		List<Book> books = new ArrayList<Book>();
		Book book = new Book();
		book.setName("{\"name\":\"test\"}");
		books.add(book);
		u.setBooks(books);
		users.get("abc").add(u);
		List<User> list = helloProxy.saveUsers(users);
		// List<User> list = helloProxy.updateUsers(users.get("abc"));
		for (User u1 : list) {
			System.out.println("name=" + u1.getName() + ",age=" + u1.getAge());
		}
		System.out.println("local_tcp end-----------");

		System.out.println("service desc api begin------------");

		refer = new ReferenceBean();
		refer.setServiceName("$serviceDesc");
		refer.setRouter(routerFactory.getObject());
		refer.setInterfaceClazz(ServiceDescApi.class);
		// refer.setTimeout(2 * 1000);
		refer.setProtocol("tcp");

		ServiceDescApi descObj = (ServiceDescApi) refer.getObject();

		ServerDesc serverDesc = descObj.getServerDesc();
		System.out.println(serverDesc);

		Set<ServiceDesc> descList = descObj.findServices();
		for (ServiceDesc desc : descList) {
			System.out.println(desc);
		}
		List<MethodDesc> methodDecss = descObj.findServiceMethods("helloService");
		for (MethodDesc desc : methodDecss) {
			System.out.println(desc);
		}

		System.out.println("findServicesByInterface " + HelloService.class.getName());
		Set<String> services = descObj.findServicesByInterface(HelloService.class.getName());
		for (String service : services) {
			System.out.println(service);
		}

		System.out.println("service desc api end------------");
	}

	public static String getDescToken() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String content = format.format(new Date());
		return DesUtil.encrypt(content.getBytes(), content);

	}

}
