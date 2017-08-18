package com.netto.schedule.domain;

import java.io.Serializable;
import java.util.Date;

public class ScheduleTask implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Date createTime;
	private String taskType;
	private String taskKey1; // 查询使用
	private String taskKey2; // 查询使用
	private String taskBody;
	private String bodyClass;
	private int status;
	private int executeCount;
	private Date lastTime;
	private int regionNo;
	private String remark;
	private String fingerprint; // 指纹字段，校验唯一性
	private int tableFix;
	private String owner;// 数据来源，仓库/城市或者其他

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getTableFix() {
		return tableFix;
	}

	public void setTableFix(int tableFix) {
		this.tableFix = tableFix;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
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

	public String getBodyClass() {
		return bodyClass;
	}

	public void setBodyClass(String bodyClass) {
		this.bodyClass = bodyClass;
	}

	public Date getCreateTime() {
		return this.createTime == null ? null : (Date) this.createTime.clone();
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime == null ? null : (Date) createTime.clone();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getRegionNo() {
		return regionNo;
	}

	public void setRegionNo(int regionNo) {
		this.regionNo = regionNo;
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

	public String getTaskBody() {
		return taskBody;
	}

	public void setTaskBody(String taskBody) {
		this.taskBody = taskBody;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getExecuteCount() {
		return executeCount;
	}

	public void setExecuteCount(int executeCount) {
		this.executeCount = executeCount;
	}

	public Date getLastTime() {
		return this.lastTime == null ? null : (Date) this.lastTime.clone();
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime == null ? null : (Date) lastTime.clone();
	}
}
