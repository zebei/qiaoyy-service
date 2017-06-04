package com.qiaoyy.time;

/**
 * 提供时间相关的服务
 *
 * @author hejincheng
 * @since 2017-1-18
 */
public interface TimeService {
    public final long UPDATE_TIME = 500;

    /**
     * 当前时间，相当于返回System.currentTimeMillis
     *
     * @return
     */
    public long now();

    /**
     * 当前时间，以秒为单位
     */
    public int currentTimeInSecond();

    /**
     * 更新当前时间，只有在当前时间服务使用了缓存策略的情况下才需要调用此方法
     */
    public void update();

    /**
     * 是否到达某个时间
     *
     * @param sometime
     * @return
     */
    public boolean timeUp(long sometime);

    /**
     * 获取指定时间与当前时间的时间差
     *
     * @param sometime
     * @return
     */
    public long getInterval(long sometime);

    /**
     * 得到yyyyMMdd格式的当天
     *
     * @return
     */
    public int getVersionOfToday();
}
