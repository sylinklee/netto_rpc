package com.netto.schedule;

import java.io.Serializable;

/**
 * schedule service param
 * 
 * @author sylink
 * 
 */
public class ScheduleParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int invokerCount = 1;// 任务服务器数量
	private String selfDefined;
	private int fetchCount = 100;
	private int executeCount = 20;
	private int executeThreadCount = 5;
	private int dataRetryCount = 3;
	private int retryTimeInterval = 120; // 数据重试间隔时间秒

	public int getDataRetryCount() {
		return dataRetryCount;
	}

	public void setDataRetryCount(int dataRetryCount) {
		this.dataRetryCount = dataRetryCount;
	}

	public int getRetryTimeInterval() {
		return retryTimeInterval;
	}

	public void setRetryTimeInterval(int retryTimeInterval) {
		this.retryTimeInterval = retryTimeInterval;
	}

	public int getInovkerCount() {
		return invokerCount;
	}

	public void setInvokerCount(int invokerCount) {
		this.invokerCount = invokerCount;
	}

	public int getClientThreadCount() {
		return executeThreadCount;
	}

	public void setClientThreadCount(int clientThreadCount) {
		this.executeThreadCount = clientThreadCount;
	}

	public String getSelfDefined() {
		return selfDefined;
	}

	public void setServerArg(String selfDefined) {
		this.selfDefined = selfDefined;
	}

	public int getFetchCount() {
		return fetchCount;
	}

	public void setFetchCount(int fetchCount) {
		this.fetchCount = fetchCount;
	}

	public int getExecuteCount() {
		return executeCount;
	}

	public void setExecuteCount(int executeCount) {
		this.executeCount = executeCount;
	}
}