package com.netto.demo.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.netto.schedule.ScheduleParam;
import com.netto.schedule.support.AbstractScheduleTaskProcess;

@Service
public class HelloSchedule extends AbstractScheduleTaskProcess<Object>{

    @Override
    protected List<Object> selectTasks(ScheduleParam param, Integer curServer) {
        
        return new ArrayList<Object>();
    }

    @Override
    protected void executeTasks(ScheduleParam param, List<Object> tasks) {
        // TODO Auto-generated method stub
        
    }

}
