package com.netto.server.desc.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netto.server.desc.ServiceDescApi;
import com.netto.server.desc.ClassDesc;
import com.netto.server.desc.FieldDesc;
import com.netto.server.desc.MethodDesc;

public class ServiceDescApiImpl implements ServiceDescApi {
	private Map<String, Object> serviceBeans;

	public ServiceDescApiImpl(Map<String, Object> serviceBeans) {
		this.serviceBeans = serviceBeans;
	}

	public List<MethodDesc> findServiceMethods(String serviceName) {
		Object serviceObj = this.serviceBeans.get(serviceName);
		if (serviceObj == null)
			return null;
		List<MethodDesc> methodDescs = new ArrayList<MethodDesc>();
		Method[] methods = serviceObj.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals("getClass") || method.getName().equals("toString")
					|| method.getName().equals("equals") || method.getName().equals("clone")
					|| method.getName().equals("finalize") || method.getName().equals("hashCode")
					|| method.getName().equals("notify") || method.getName().equals("notifyAll")
					|| method.getName().equals("wait")) {
				continue;
			}
			MethodDesc methodDesc = new MethodDesc();
			methodDesc.setMethodName(method.getName());
			methodDesc.setReturnType(method.getGenericReturnType().getTypeName());
			dependons(methodDesc, method.getGenericReturnType());
			Type[] argTypes = method.getGenericParameterTypes();
			List<String> list1 = new ArrayList<String>();
			for (Type argType : argTypes) {
				list1.add(argType.getTypeName());
				dependons(methodDesc, argType);
			}
			methodDesc.setArgTypes(list1);
			methodDescs.add(methodDesc);

		}
		return methodDescs;
	}

	public Set<String> findServices() {
		return this.serviceBeans.keySet();
	}

	private void dependons(MethodDesc methodDesc, Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType type1 = (ParameterizedType) type;
			Type[] actTypes = type1.getActualTypeArguments();
			for (Type actType : actTypes) {
				if (actType.getTypeName().startsWith("java")) {
					continue;
				}
				Class<?> actClass = (Class<?>) actType;
				if (methodDesc.getDependClasses() != null) {
					if (methodDesc.getDependClasses().containsKey(actClass.getName())) {
						continue;
					}
				}
				ClassDesc classDesc = new ClassDesc();
				classDesc.setClassName(actClass.getName());
				List<FieldDesc> fields = new ArrayList<FieldDesc>();
				classDesc.setFields(fields);
				Field[] actFields = actClass.getDeclaredFields();
				for (Field actField : actFields) {
					FieldDesc fieldDesc = new FieldDesc();
					fieldDesc.setFieldName(actField.getName());
					fieldDesc.setFieldType(actField.getGenericType().getTypeName());
					this.dependons(methodDesc, actField.getGenericType());
					fields.add(fieldDesc);
				}
				if (methodDesc.getDependClasses() == null) {
					methodDesc.setDependClasses(new HashMap<String, ClassDesc>());
				}
				methodDesc.getDependClasses().put(classDesc.getClassName(), classDesc);
			}
		}
	}
}
