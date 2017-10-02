package com.netto.service.desc;

import java.util.List;
import java.util.Map;

public class MethodDesc {
	private String methodName;
	private String returnType;
	private List<String> argTypes;
	private Map<String, ClassDesc> dependClasses;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<String> getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(List<String> argTypes) {
		this.argTypes = argTypes;
	}

	public Map<String, ClassDesc> getDependClasses() {
		return dependClasses;
	}

	public void setDependClasses(Map<String, ClassDesc> dependClasses) {
		this.dependClasses = dependClasses;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("method=").append(methodName).append("\r\n");
		sb.append("|__").append("return=").append(returnType).append("\r\n");
		if (this.getArgTypes() != null) {
			for (int i = 0; i < this.getArgTypes().size(); i++) {
				sb.append("|__").append("arg").append(i).append("=").append(this.getArgTypes().get(i)).append("\r\n");
			}
		}
		if (this.getDependClasses() != null) {
			for (String key : this.getDependClasses().keySet()) {
				sb.append("|__").append(this.getDependClasses().get(key));
			}
		}
		return sb.toString();
	}

}
