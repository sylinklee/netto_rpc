package com.netto.schedule.manager.impl;

import com.netto.schedule.dao.ScheduleTaskDao;
import com.netto.schedule.domain.ScheduleTask;
import com.netto.schedule.dto.TaskRequest;
import com.netto.schedule.dto.TaskResponse;
import com.netto.schedule.manager.ScheduleTaskManager;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netto.schedule.IScheduleTaskProcess;
import com.netto.schedule.ScheduleParam;
import com.netto.schedule.util.ScheduleUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 往数据库中提交调度任务
 */
@Component
public class ScheduleTaskManagerImpl implements ScheduleTaskManager {
	private static final Logger logger = Logger.getLogger(ScheduleTaskManagerImpl.class);
	private static final Map<String, String> tableFixMap = new HashMap<>();

	@Autowired
	private ScheduleTaskDao scheduleTaskDao;

	@Value("${schedule.tableNumber:8}")
	private int tableNumber = 0;

	@Value("${cleanUp.maxBatchSize:500}")
	private int cleanUpMaxBatchSize = 500;

	@Value("${insert.maxBatchSize:100}")
	private int insertMaxBatchSize = 100;

	/*
	 * private int getTableNumber() {
	 * 
	 * if (tableNumber == 0) { synchronized (this) { if (tableNumber == 0) { try
	 * (InputStream stream =
	 * this.getClass().getResourceAsStream("prop/application.properties")) {
	 * Properties properties = new Properties(); properties.load(stream);
	 * tableNumber =
	 * Integer.parseInt(properties.getProperty("schedule.tableNumber")); } catch
	 * (IOException ignored) {
	 * 
	 * } } } } if (tableNumber == 0) { tableNumber = 8; } return this.tableNumber; }
	 */

	public int getInsertMaxBatchSize() {
		return insertMaxBatchSize;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T> TaskResponse<T> submitTask(TaskRequest<T> request) {
		checkRequest(request);
		ScheduleTask taskDomain = new ScheduleTask();
		taskDomain.setBodyClass(request.getTaskObject().getClass().getName());
		taskDomain.setFingerprint(request.getFingerPrint());
		taskDomain.setRegionNo(ScheduleUtil.getRegionNo());
		taskDomain.setStatus(IScheduleTaskProcess.TaskStatus_Init);
		taskDomain.setTaskKey1(request.getTaskKey1());
		taskDomain.setTaskKey2(request.getTaskKey2());
		taskDomain.setOwner(request.getOwner());
		taskDomain.setTaskType(request.getTaskType());
		taskDomain.setTableFix(this.getTableFix(request.getTaskType()));
		try {
			taskDomain.setTaskBody(getJsonMapper().writeValueAsString(request.getTaskObject()));
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		int count = scheduleTaskDao.insertTask(taskDomain);
		if (count > 0) {
			return this.getTaskResponse(taskDomain, request.getTaskObject());
		} else {
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T> void batchSubmitTask(List<TaskRequest<T>> requests, String taskType) {
		if (requests.size() > this.insertMaxBatchSize)
			throw new RuntimeException("batch size is overflow");
		int tableFix = this.getTableFix(taskType);
		List<ScheduleTask> tasks = requests.stream().map((request) -> {
			checkRequest(request);
			ScheduleTask taskDomain = new ScheduleTask();
			taskDomain.setBodyClass(request.getTaskObject().getClass().getName());
			taskDomain.setFingerprint(request.getFingerPrint());
			taskDomain.setRegionNo(ScheduleUtil.getRegionNo());
			taskDomain.setStatus(IScheduleTaskProcess.TaskStatus_Init);
			taskDomain.setTaskKey1(request.getTaskKey1());
			taskDomain.setTaskKey2(request.getTaskKey2());
			taskDomain.setOwner(request.getOwner());
			try {
				taskDomain.setTaskBody(getJsonMapper().writeValueAsString(request.getTaskObject()));
			} catch (JsonProcessingException e) {
				logger.error(e);
			}
			taskDomain.setTaskType(taskType);

			return taskDomain;
		}).collect(Collectors.toList());

		this.scheduleTaskDao.batchInsertTask(tableFix, taskType, tasks);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T> void lockTasks(List<TaskResponse<T>> tasks) {
		if (tasks == null || tasks.size() == 0)
			return;
		checkResponse(tasks.get(0));

		Map<String, Object> map = new HashMap<>();
		map.put("status", IScheduleTaskProcess.TaskStatus_executing);

		List<Long> ids = new ArrayList<>();
		for (TaskResponse<T> task : tasks) {
			ids.add(task.getId());
		}
		map.put("ids", ids);
		map.put("tableFix", this.getTableFix(tasks.get(0).getTaskType()));
		this.scheduleTaskDao.lockTasks(map);
	}

	@Override
	public <T> void lockTask(TaskResponse<T> task) {
		if (task == null)
			return;
		checkResponse(task);
		List<TaskResponse<T>> tasks = new ArrayList<>();
		tasks.add(task);
		this.lockTasks(tasks);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T> void doneTask(TaskResponse<T> task) {
		if (task == null)
			return;
		checkResponse(task);
		ScheduleTask taskDomain = new ScheduleTask();
		taskDomain.setId(task.getId());
		taskDomain.setTaskType(task.getTaskType());
		taskDomain.setTableFix(this.getTableFix(task.getTaskType()));
		taskDomain.setStatus(IScheduleTaskProcess.TaskStatus_executed);
		this.scheduleTaskDao.doneTask(taskDomain);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T> void errorTask(TaskResponse<T> task, Throwable e) {
		if (task == null)
			return;
		checkResponse(task);
		Map<String, Object> map = new HashMap<>();
		map.put("id", task.getId());
		map.put("status", IScheduleTaskProcess.TaskStatus_error);
		String remark = e.getMessage();
		if (remark != null && remark.length() > 255) {
			remark = remark.substring(0, 254);
		}
		map.put("remark", remark);
		map.put("tableFix", this.getTableFix(task.getTaskType()));
		this.scheduleTaskDao.errorTask(map);
	}

	@Override
	public <T> List<TaskResponse<T>> queryExecuteTasks(String taskType, ScheduleParam param, Integer curServer) {
		if (StringUtils.isEmpty(taskType)) {
			throw new RuntimeException("taskType is null");
		}
		List<TaskResponse<T>> responses = new ArrayList<>();
		Integer tableFix = this.getTableFix(taskType);

		Map<String, Object> map = new HashMap<>();

		if (param.getInovkerCount() != 1) {
			List<Integer> regions = ScheduleUtil.getTaskRegions(param.getInovkerCount(), curServer);
			if (regions.size() != ScheduleUtil.MaxRegionCount) {
				map.put("regions", ScheduleUtil.getTaskRegions(param.getInovkerCount(), curServer));
			}
		}

		map.put("statusInit", IScheduleTaskProcess.TaskStatus_Init);
		map.put("statusExecuting", IScheduleTaskProcess.TaskStatus_executing);
		map.put("statusError", IScheduleTaskProcess.TaskStatus_error);
		map.put("lastTime", param.getRetryTimeInterval());
		map.put("retryCount", param.getDataRetryCount());
		map.put("fetchCount", param.getFetchCount());
		map.put("taskType", taskType);
		map.put("tableFix", tableFix);
		StringBuilder sb = new StringBuilder(100);
		sb.append("serverCount=").append(param.getInovkerCount()).append(",curServer=").append(curServer)
				.append(",serverArg=").append(param.getSelfDefined()).append(",lastTime=").append(map.get("lastTime"))
				.append(",retryCount=").append(map.get("retryCount")).append(",fetchCount=")
				.append(map.get("fetchCount")).append(",taskType=").append(map.get("taskType")).append(",tableFix=")
				.append(map.get("tableFix"));
		logger.info(sb.toString());
		List<ScheduleTask> list = this.scheduleTaskDao.queryExecuteTasks(map);
		for (ScheduleTask task : list) {
			TaskResponse<T> response = this.getTaskResponse(task);
			response.setRetryCount(param.getDataRetryCount());
			responses.add(response);
		}
		return responses;
	}

	@Override
	public <T> TaskResponse<T> queryTaskByFingerprint(String taskType, String fingerprint) {
		if (StringUtils.isEmpty(taskType)) {
			throw new RuntimeException("taskType is null");
		}
		int tableFix = this.getTableFix(taskType);
		ScheduleTask task = this.scheduleTaskDao.queryByFingerprint(tableFix, fingerprint);
		return task == null ? null : this.getTaskResponse(task);
	}

	@Override
	public void resetTask(String taskType, String fingerPrint, int status, int executeCount) {
		if (StringUtils.isEmpty(taskType)) {
			throw new RuntimeException("taskType is null");
		}
		int tableFix = this.getTableFix(taskType);
		scheduleTaskDao.updateTaskByfingerPrint(tableFix, fingerPrint, status, executeCount);
	}

	@SuppressWarnings("unchecked")
	private <T> TaskResponse<T> getTaskResponse(ScheduleTask task) {
		T taskObject = null;
		try {
			Class<?> bodyClass = Class.forName(task.getBodyClass());
			ObjectMapper mapper = getJsonMapper();
			taskObject = (T) mapper.readValue(task.getTaskBody(), bodyClass);
		} catch (Exception e) {
			logger.error("getTaskResponse:" + e.getMessage(), e);
		}
		return this.getTaskResponse(task, taskObject);
	}

	private static ObjectMapper objectMapper;

	public static ObjectMapper getJsonMapper() {
		if (objectMapper == null) {
			synchronized (ScheduleTaskManager.class) {
				if (objectMapper == null) {
					objectMapper = new ObjectMapper();
					objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					objectMapper.setSerializationInclusion(Include.NON_NULL);
					objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
				}
			}
		}

		return objectMapper;

	}

	private <T> TaskResponse<T> getTaskResponse(ScheduleTask task, T taskObject) {
		TaskResponse<T> response = new TaskResponse<>();
		response.setId(task.getId());
		response.setFingerPrint(task.getFingerprint());
		response.setTaskKey1(task.getTaskKey1());
		response.setTaskKey2(task.getTaskKey2());
		response.setTaskType(task.getTaskType());
		response.setTaskBody(task.getTaskBody());
		response.setStatus(task.getStatus());
		response.setOwner(task.getOwner());
		response.setTaskObject(taskObject);

		return response;
	}

	private synchronized int getTableFix(String taskType) {
		// 先从内存取
		if (tableFixMap.containsKey(taskType)) {
			return Integer.parseInt(tableFixMap.get(taskType));
		}

		List<Map<String, Object>> list = this.scheduleTaskDao.queryTableFixs();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> row : list) {
				String tmpTaskType = (String) row.get("task_type");
				tableFixMap.put(tmpTaskType, row.get("table_fix").toString());
			}
		}
		// 没有就报错
		if (!tableFixMap.containsKey(taskType)) {
			throw new RuntimeException(taskType + "无法找到对应的tableFix，请预制数据！");
		}
		return Integer.parseInt(tableFixMap.get(taskType));
	}

	@Override
	public <T> TaskResponse<T> queryTask(String taskType, long taskId) {
		int tableFix = this.getTableFix(taskType);
		ScheduleTask task = this.scheduleTaskDao.queryTask(tableFix, taskId);
		return task == null ? null : this.getTaskResponse(task);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteTask(String taskType, long taskId) {
		int tableFix = this.getTableFix(taskType);
		this.scheduleTaskDao.deleteTask(tableFix, taskId);
	}

	private <T> void checkRequest(TaskRequest<T> request) {
		if (StringUtils.isEmpty(request.getTaskType())) {
			throw new RuntimeException("TaskType is null!");
		}
		if (StringUtils.isEmpty(request.getFingerPrint())) {
			throw new RuntimeException("fingerprint is null!");
		}
		if (request.getTaskObject() == null) {
			throw new RuntimeException("taskObject is null!");
		}
	}

	private <T> void checkResponse(TaskResponse<T> response) {
		if (StringUtils.isEmpty(response.getTaskType())) {
			throw new RuntimeException("TaskType is null!");
		}
	}

	/* 不清除今天，昨天，前天的数据 */
	public int cleanUp(String taskType, int batchSize) {
		logger.info("start clean up");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -2);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp lastDate = new Timestamp(calendar.getTimeInMillis());
		int tableFix = -1;
		List<Map<String, Object>> list = this.scheduleTaskDao.queryTableFixs();

		if (list != null && list.size() > 0) {
			for (Map<String, Object> row : list) {
				if (taskType.equals(row.get("task_type"))) {
					tableFix = ((Number) row.get("table_fix")).intValue();
					break;
				}
			}
		}

		if (tableFix == -1) {
			logger.warn("can't find table fix for:" + taskType);
			return 0;
		}

		// List msg = this.scheduleTaskDao.optimizeTable(tableFix);

		if (batchSize > cleanUpMaxBatchSize) {
			batchSize = cleanUpMaxBatchSize;
		}

		List<Integer> ids = this.scheduleTaskDao.listDoneTask(tableFix, taskType, batchSize, lastDate);
		// while(!ids.isEmpty()){
		// int updated = this.scheduleTaskDao.cleanUp(tableFix, ids);
		// deletedSize = deletedSize+updated;
		// }
		if (!ids.isEmpty()) {
			int deletedSize = this.scheduleTaskDao.cleanUp(tableFix, ids);
			return deletedSize;
		} else {
			return 0;
		}
	}

	@Override
	public boolean optimize(String taskType) {
		logger.info("start clean up");

		int tableFix = -1;
		List<Map<String, Object>> list = this.scheduleTaskDao.queryTableFixs();

		if (list != null && list.size() > 0) {
			for (Map<String, Object> row : list) {
				if (taskType.equals(row.get("task_type"))) {
					tableFix = ((Number) row.get("table_fix")).intValue();
					break;
				}
			}
		}

		if (tableFix == -1) {
			logger.warn("can't find table fix for:" + taskType);
			return false;
		}

		List<?> msg = this.scheduleTaskDao.optimizeTable(tableFix);

		logger.info("optimize end" + msg.toString());

		return true;
	}

	@Override
	public int cleanUp(String taskType, int batchSize, int backupDays) {
		logger.info("start clean up");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -backupDays);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp lastDate = new Timestamp(calendar.getTimeInMillis());
		int tableFix = -1;
		List<Map<String, Object>> list = this.scheduleTaskDao.queryTableFixs();

		if (list != null && list.size() > 0) {
			for (Map<String, Object> row : list) {
				if (taskType.equals(row.get("task_type"))) {
					tableFix = ((Number) row.get("table_fix")).intValue();
					break;
				}
			}
		}

		if (tableFix == -1) {
			logger.warn("can't find table fix for:" + taskType);
			return 0;
		}

		// List msg = this.scheduleTaskDao.optimizeTable(tableFix);

		if (batchSize > cleanUpMaxBatchSize) {
			batchSize = cleanUpMaxBatchSize;
		}

		List<Integer> ids = this.scheduleTaskDao.listDoneTask(tableFix, taskType, batchSize, lastDate);

		if (!ids.isEmpty()) {
			int deletedSize = this.scheduleTaskDao.cleanUp(tableFix, ids);
			return deletedSize;
		} else {
			return 0;
		}
	}

}
