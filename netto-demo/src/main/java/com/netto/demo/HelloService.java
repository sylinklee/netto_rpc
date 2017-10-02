package com.netto.demo;

import java.util.List;
import java.util.Map;

public interface HelloService {
	String sayHello(String name);

	List<User> saveUsers(Map<String, List<User>> users);
	
	List<User> updateUsers(List<User> users);
	
	String sayByeBye(int i);
}
