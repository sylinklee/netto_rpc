package com.netto.schedule;

/**
 * schedule service interface
 * 
 * @author sylink
 * 
 */
public interface IScheduleTaskProcess {
	public static int TaskStatus_cancel = -1;
	public static int TaskStatus_Init = 0;
	public static int TaskStatus_executing = 1;
	public static int TaskStatus_executed = 2;
	public static int TaskStatus_his = 5;
	public static int TaskStatus_error = 10;

	int execute(ScheduleParam param, Integer invokerNumber);
}
