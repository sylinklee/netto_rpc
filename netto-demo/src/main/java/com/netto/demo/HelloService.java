package com.netto.demo;

import java.util.List;
import java.util.Map;

public interface HelloService {
	List<User> queryUsers();
	
	String sayHello(String name);

	List<User> saveUsers(Map<String, List<User>> users);
	
	List<User> updateUsers(List<User> users);
	
	String sayByeBye(int i);
}
