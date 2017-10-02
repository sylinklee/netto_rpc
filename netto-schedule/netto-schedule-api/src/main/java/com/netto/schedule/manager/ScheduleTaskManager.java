package com.netto.schedule.manager;

import com.netto.schedule.ScheduleParam;
import com.netto.schedule.dto.TaskRequest;
import com.netto.schedule.dto.TaskResponse;

import java.util.List;

/**
 * 调度任务管理器，提交任务，锁定任务，完成任务，标记错误，查询任务等
 *
 * @author sylink
 */
public interface ScheduleTaskManager {
	<T> TaskResponse<T> queryTask(String taskType, long taskId);

	<T> TaskResponse<T> submitTask(TaskRequest<T> request);

	<T> void batchSubmitTask(List<TaskRequest<T>> requests, String taskType);

	int getInsertMaxBatchSize();

	void deleteTask(String taskType, long taskId);

	<T> void lockTask(TaskResponse<T> task);

	<T> void lockTasks(List<TaskResponse<T>> tasks);

	<T> void doneTask(TaskResponse<T> task);

	<T> void errorTask(TaskResponse<T> task, Throwable e);

	<T> List<TaskResponse<T>> queryExecuteTasks(String taskType, ScheduleParam param, Integer curServer);

	<T> TaskResponse<T> queryTaskByFingerprint(String taskType, String fingerprint);

	void resetTask(String taskType, String fingerPrint, int status, int executeCount);

	int cleanUp(String taskType, int batchSize);

	int cleanUp(String taskType, int batchSize, int backupDays);

	boolean optimize(String taskType);

}
