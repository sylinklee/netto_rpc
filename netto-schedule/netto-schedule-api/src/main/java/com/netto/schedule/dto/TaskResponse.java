package com.netto.schedule.dto;

import java.io.Serializable;

public class TaskResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String taskType;
    private String taskKey1; // 查询使用
    private String taskKey2; // 查询使用
    private T taskObject;
    private String taskBody;
    private String fingerPrint; // 指纹字段，校验唯一性
    private int serverId;
    private int retryCount;
    private int status;
	private String owner;//数据来源，仓库/城市或者其他

    public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskKey1() {
        return taskKey1;
    }

    public void setTaskKey1(String taskKey1) {
        this.taskKey1 = taskKey1;
    }

    public String getTaskKey2() {
        return taskKey2;
    }

    public void setTaskKey2(String taskKey2) {
        this.taskKey2 = taskKey2;
    }

    public T getTaskObject() {
        return taskObject;
    }

    public void setTaskObject(T taskObject) {
        this.taskObject = taskObject;
    }

    public String getTaskBody() {
        return taskBody;
    }

    public void setTaskBody(String taskBody) {
        this.taskBody = taskBody;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
