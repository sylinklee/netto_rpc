package com.netto.demo.impl;

import java.util.List;
import java.util.Map;

import com.netto.demo.Book;
import com.netto.demo.HelloService;
import com.netto.demo.User;

public class HelloServiceImpl implements HelloService {

	public String sayHello(String name) {
		return "hello \r\n " + name + "!";
	}

	public List<User> saveUsers(Map<String, List<User>> users) {
//		try {
//			Thread.sleep(5 * 1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		for (String key : users.keySet()) {
			for (User u : users.get(key)) {
				u.setAge(12);
			}
		}
		return users.get("abc");
	}

	public List<User> updateUsers(List<User> users) {
		for (User u : users) {
			u.setAge(12);
			for (Book book : u.getBooks()) {
				book.setAuthor("lidong");
			}
		}
		return users;
	}
}
