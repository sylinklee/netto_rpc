package com.netto.schedule.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScheduleUtil {
    public static final int MaxRegionCount = 64;

    public static int getRegionNo() {
        return new Random(System.currentTimeMillis()).nextInt(MaxRegionCount);
    }

    /**
     * 得到当前服务的任务分片
     *
     * @param serverCount
     * @param curServer
     * @return
     */
    public static List<Integer> getTaskRegions(int serverCount, int curServer) {
        List<Integer> regions = new ArrayList<>();
        for (int i = 0; i < ScheduleUtil.MaxRegionCount; i++) {
            if (isMyTask(i, serverCount, curServer)) {
                regions.add(i);
            }
        }
        return regions;
    }

    /**
     * 判断此任务是否是需要处理得任务
     *
     * @param id
     * @param serverCount
     * @param curServer
     * @return
     */
    private static boolean isMyTask(int id, int serverCount, Integer curServer) {
        int mod = Math.abs(id % serverCount);
        return mod == curServer;
    }
}
