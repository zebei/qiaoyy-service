package com.qiaoyy.thread;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Thread pool monitor.
 */
public class ThreadPoolMonitor {

    /**
     * Gets properties.
     *
     * @param pool the pool
     * @return the properties
     */
    public static JSONObject getProperties(ThreadPoolExecutor pool) {
        JSONObject ret = new JSONObject();
        ret.put("activeCount", getActiveCount(pool));
        ret.put("corePoolSize", getCorePoolSize(pool));
        ret.put("largestPoolSize", getLargestPoolSize(pool));
        ret.put("maximumPoolSize", getMaximumPoolSize(pool));
        ret.put("poolSize", getPoolSize(pool));
        ret.put("completedTaskCount", getCompletedTaskCount(pool));
        ret.put("keepAliveTime", getKeepAliveTime(pool));
        ret.put("queue", getQueue(pool));
        ret.put("queueSize", getQueue(pool).length());
        ret.put("rejectedExecutionHandler", getRejectedExecutionHandler(pool));
        ret.put("taskCount", getTaskCount(pool));
        return ret;
    }

    /**
     * Gets active count.
     *
     * @param pool the pool
     * @return the active count
     */
    public static int getActiveCount(ThreadPoolExecutor pool) {
        return pool.getActiveCount();
    }

    /**
     * Gets core pool size.
     *
     * @param pool the pool
     * @return the core pool size
     */
    public static int getCorePoolSize(ThreadPoolExecutor pool) {
        return pool.getCorePoolSize();
    }

    /**
     * Gets largest pool size.
     *
     * @param pool the pool
     * @return the largest pool size
     */
    public static int getLargestPoolSize(ThreadPoolExecutor pool) {
        return pool.getLargestPoolSize();
    }

    /**
     * Gets maximum pool size.
     *
     * @param pool the pool
     * @return the maximum pool size
     */
    public static int getMaximumPoolSize(ThreadPoolExecutor pool) {
        return pool.getMaximumPoolSize();
    }

    /**
     * Gets pool size.
     *
     * @param pool the pool
     * @return the pool size
     */
    public static int getPoolSize(ThreadPoolExecutor pool) {
        return pool.getPoolSize();
    }

    /**
     * Gets completed task count.
     *
     * @param pool the pool
     * @return the completed task count
     */
    public static long getCompletedTaskCount(ThreadPoolExecutor pool) {
        return pool.getCompletedTaskCount();
    }

    /**
     * Gets keep alive time.
     *
     * @param pool the pool
     * @return the keep alive time
     */
    public static long getKeepAliveTime(ThreadPoolExecutor pool) {
        return pool.getKeepAliveTime(TimeUnit.SECONDS);
    }

    /**
     * Gets task count.
     *
     * @param pool the pool
     * @return the task count
     */
    public static long getTaskCount(ThreadPoolExecutor pool) {
        return pool.getTaskCount();
    }

    /**
     * Gets queue.
     *
     * @param pool the pool
     * @return the queue
     */
    public static String getQueue(ThreadPoolExecutor pool) {
        return pool.getQueue().getClass().getSimpleName();
    }

    /**
     * Gets rejected execution handler.
     *
     * @param pool the pool
     * @return the rejected execution handler
     */
    public static String getRejectedExecutionHandler(ThreadPoolExecutor pool) {
        return pool.getRejectedExecutionHandler().getClass().getSimpleName();
    }

}
