package com.netto.schedule.dao;

import com.netto.schedule.domain.ScheduleTask;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ScheduleTaskDao {
    ScheduleTask queryTask(@Param("tableFix") int tableFix, @Param("taskId") long taskId);

    void deleteTask(@Param("tableFix") int tableFix, @Param("taskId") long taskId);

    List<ScheduleTask> queryExecuteTasks(Map<String, Object> map);

    int insertTask(ScheduleTask task);
    
    int batchInsertTask(@Param("tableFix") int tableFix, @Param("taskType") String taskType,@Param("tasks") List<ScheduleTask> tasks);

    int lockTasks(Map<String, Object> map);

    int errorTask(Map<String, Object> map);

    int doneTask(ScheduleTask task);

    int insertTableFix(Map<String, Object> map);

    List<Map<String, Object>> queryTableFixs();

    ScheduleTask queryByFingerprint(@Param("tableFix") int tableFix, @Param("fingerprint") String fingerprint);
    
    List<Integer> listDoneTask(@Param("tableFix") int tableFix,@Param("taskType") String taskType, @Param("fetchCount") int fetchCount, @Param("lastDate") Timestamp lastDate);
    
    
    int cleanUp(@Param("tableFix") int tableFix,@Param("ids") List<Integer> ids);

    int queryInitCount(@Param("tableFix") int tableFix,@Param("taskType") String taskType);

    int updateTaskByfingerPrint(@Param("tableFix") int tableFix,
                                 @Param("fingerPrint") String fingerPrint,
                                 @Param("status") int status,
                                 @Param("executeCount") int executeCount);
    
    

    List<Map<String, Object>> optimizeTable(@Param("tableFix") int tableFix);
}
