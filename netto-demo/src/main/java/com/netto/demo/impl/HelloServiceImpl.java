package com.netto.demo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.netto.demo.Book;
import com.netto.demo.HelloService;
import com.netto.demo.User;

@Service
public class HelloServiceImpl implements HelloService {

	public String sayHello(String name) {
		return "hello \r\n " + name + "!";
	}
	
	


	public List<User> saveUsers(Map<String, List<User>> users) {
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


    @Override
    public String sayByeBye(int i) {
        return "88 \r\n ";
    }




	@Override
	public List<User> queryUsers() {
		List<User> users=new ArrayList<User>();
		return users;
	}
}
