package com.netto.service.desc;

import java.util.List;
import java.util.Set;

public interface ServiceDescApi {
	public List<MethodDesc> findServiceMethods(String token, String serviceName);

	public Set<String> findServices(String token);

	public int queryServiceTimeout(String serviceName);

	public int pingService(String data);

	public Set<String> findServicesByInterface(String token, String interfaceClazz);
}
