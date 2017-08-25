package com.netto.server.desc;

import java.util.List;
import java.util.Set;

public interface ServiceDescApi {
	public List<MethodDesc> findServiceMethods(String serviceName);

	public Set<String> findServices();
}
