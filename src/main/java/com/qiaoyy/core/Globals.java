package com.qiaoyy.core;

import org.springframework.stereotype.Component;

import com.qiaoyy.log.AppLog;
import com.qiaoyy.mannger.dispather.GameDispather;
import com.qiaoyy.mannger.user.Player;
import com.qiaoyy.netty.ChannelMgr;
import com.qiaoyy.schedule.ScheduleService;
import com.qiaoyy.schedule.ScheduleServiceImp;
import com.qiaoyy.thread.ThreadType;
import com.qiaoyy.time.SystemTimeService;
import com.qiaoyy.time.TimeService;

/**
 * Created by Henry on 2017/6/4.
 */
@Component
public class Globals {


    /**
     * 时间服务
     */
    private static TimeService timeService;

    /**
     * 定时调度任务
     */
    private static ScheduleService scheduleService;

    /**
     * 心跳时间(默认0.5s)
     */
    private static int heartbeat = 500;

    public static void init() throws Exception {
        initService();
    }

    /**
     * 初始化服务
     *
     * @throws Exception
     */
    private static void initService() throws Exception {
        AppLog.LOG_COMMON.info("globals.service.init.start");
        AppLog.LOG_COMMON.info("time.service.init");
        timeService = new SystemTimeService(true);
        AppLog.LOG_COMMON.info("schedule.service.init");
        scheduleService = new ScheduleServiceImp();
        AppLog.LOG_COMMON.info("global.tick.start");
        startTick();
        AppLog.LOG_COMMON.info("globals.service.init.finish");
    }

    /**
     * 开始全局tick
     */
    public static void startTick() {
        if (AppInit.run.getEnvironment().containsProperty("app.heartbeat")) {
            heartbeat = Integer.parseInt(AppInit.run.getEnvironment().getProperty("app.heartbeat"));
        }
        scheduleService.scheduleWithFixedDelay(ThreadType.SYS_THREAD, () -> tick(), heartbeat, heartbeat);
    }

    /**
     * 关闭服务
     */
    public static void stop() {
        AppLog.LOG_COMMON.info("globals.service.stop.start");
        AppLog.LOG_COMMON.info("schedule.service.stop");
        scheduleService.shutdown();
        AppLog.LOG_COMMON.info("globals.service.stop.finish");
    }

    public static void tick() {
        // 更新时间缓存
        timeService.update();
        // channel心跳
        ChannelMgr.getInstance().tick();
    }

    public static TimeService getTimeService() {
        return timeService;
    }

    public static ScheduleService getScheduleService() {
        return scheduleService;
    }

    public static Player getPlayer(long userId) {
        return ChannelMgr.getInstance().playerMap.get(userId);
    }
    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static  GameDispather getGameDispather() {
        
        return AppInit.run.getBean(GameDispather.class);
    }
}
