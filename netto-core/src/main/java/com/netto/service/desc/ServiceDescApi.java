package com.netto.service.desc;

import java.util.List;
import java.util.Set;

public interface ServiceDescApi {

	public ServerDesc getServerDesc();

	public List<MethodDesc> findServiceMethods(String serviceName);

	public Set<ServiceDesc> findServices();

	public int queryServiceTimeout(String serviceName);

	public int pingService(String data);

	public Set<String> findServicesByInterface(String interfaceClazz);
}
