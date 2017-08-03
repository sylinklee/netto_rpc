package com.netto.context;

public class ServiceResponse {
	private int invokeId;
	private Boolean success;
	private String body;

	public int getInvokeId() {
		return invokeId;
	}

	public void setInvokeId(int invokeId) {
		this.invokeId = invokeId;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
