package com.qiaoyy.schedule;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.qiaoyy.log.AppLog;
import com.qiaoyy.thread.ThreadPool;
import com.qiaoyy.thread.ThreadType;

/**
 * 调度各种消息
 *
 * @author hejincheng
 */
public class ScheduleServiceImp implements ScheduleService {
    private final ScheduledExecutorService scheduler;
    /**
     * 是否已经停止
     */
    private volatile boolean shutdown = false;

    public ScheduleServiceImp() {
        scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void shutdown() {
        this.shutdown = true;
        this.scheduler.shutdownNow();
    }

    @Override
    public void scheduleOnce(ThreadType threadType, Runnable runnable, long delay) {
        if (AppLog.LOG_QUEUE.isDebugEnabled()) {
            AppLog.LOG_QUEUE.debug("scheduleOnce delay:" + delay + " msg:" + runnable);
        }
        MessageCarrier carrier = new MessageCarrier(threadType, runnable);
        scheduler.schedule(carrier, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void scheduleOnce(ThreadType threadType, Runnable runnable, Date d) {
        if (AppLog.LOG_QUEUE.isDebugEnabled()) {
            AppLog.LOG_QUEUE.debug("scheduleOnce date:" + d + " msg:" + runnable);
        }
        MessageCarrier carrier = new MessageCarrier(threadType, runnable);
        scheduler.schedule(carrier, (d.getTime() - System.currentTimeMillis()),
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void scheduleWithFixedDelay(ThreadType threadType, Runnable runnable, long delay,
                                       long period) {
        if (AppLog.LOG_QUEUE.isDebugEnabled()) {
            AppLog.LOG_QUEUE.debug("scheduleWithFixedDelay delay:" + delay + " period:"
                    + period + " msg:" + runnable);
        }
        MessageCarrier carrier = new MessageCarrier(threadType, runnable);
        scheduler.scheduleAtFixedRate(carrier, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行消息调度任务，分发到指定线程执行
     */
    private final class MessageCarrier implements Runnable {
        private final Runnable runnable;
        private final ThreadType threadType;

        public MessageCarrier(ThreadType threadType, Runnable runnable) {
            this.runnable = runnable;
            this.threadType = threadType;
        }

        @Override
        public void run() {
            if (shutdown) {
                return;
            }
            ThreadPool.dispatch(threadType, runnable);
        }
    }

}
