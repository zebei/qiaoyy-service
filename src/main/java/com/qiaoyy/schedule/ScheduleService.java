package com.qiaoyy.schedule;

import com.qiaoyy.thread.ThreadType;

import java.util.Date;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;

/**
 * 定时任务管理器
 *
 * @author guowei
 */
public interface ScheduleService {

    /**
     * @param runnable
     * @param delay
     */
    public abstract void scheduleOnce(ThreadType threadType, Runnable runnable, long delay);

    /**
     * @param runnable
     * @param d
     */
    public abstract void scheduleOnce(ThreadType threadType, Runnable runnable, Date d);

    /**
     * @param runnable
     * @param delay
     * @param period
     */
    public abstract void scheduleWithFixedDelay(ThreadType threadType, Runnable runnable, long delay, long period);

    /**
     * 关闭服务
     */
    public abstract void shutdown();
}
