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
	private int serverCount = 1;// 任务服务器数量
	private String serverArg;
	private int fetchCount = 100;
	private int executeCount = 20;
	private int clientThreadCount = 5;
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

	public int getServerCount() {
		return serverCount;
	}

	public void setServerCount(int serverCount) {
		this.serverCount = serverCount;
	}

	public int getClientThreadCount() {
		return clientThreadCount;
	}

	public void setClientThreadCount(int clientThreadCount) {
		this.clientThreadCount = clientThreadCount;
	}

	public String getServerArg() {
		return serverArg;
	}

	public void setServerArg(String serverArg) {
		this.serverArg = serverArg;
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