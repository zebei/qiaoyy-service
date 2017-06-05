package com.qiaoyy.time;

import java.util.Date;

/**
 * 一般来说,{@link System#currentTimeMillis()} 的调用代价较高,而多数情况下在某一时刻的缓冲时间是可以满足要求的.
 * 在管理器采用由{@link #isCacheTime()}控制的策略,即如果为true就缓冲时间,此时调用{@link #now}取得由
 * {@link #update()}更新的时间;否则取得系统的实时时间
 */
public class SystemTimeService implements TimeService {
    /**
     * 当前时间
     */
    private long now = 0;
    /**
     * 是否使用缓冲的时间
     */
    private final boolean cacheTime;

    /**
     * 构建一个缓冲时间的时间管理器
     */
    public SystemTimeService() {
        this(false);
    }

    /**
     * 构建一个由cacheTime指定的时间管理里
     *
     * @param cacheTime
     */
    public SystemTimeService(boolean cacheTime) {
        this.cacheTime = cacheTime;
        if (this.cacheTime) {
            this.now = System.currentTimeMillis();
        }
    }

    /**
     * 更新时间,如果当前的TimeManqager是启用了缓冲策略,则需要在较短的间隔内定调用该方法更新被缓冲的时间
     */
    public void update() {
        if (this.cacheTime) {
            now = System.currentTimeMillis();
        }
    }

    /**
     * 是否缓冲时间
     *
     * @return the cacheTime true,缓冲时间;false,不缓冲时间
     */
    public boolean isCacheTime() {
        return cacheTime;
    }

    /**
     * 获取当前的更新时间,如果{@link #isCacheTime()}为true,即使用缓冲的时间,}
     *
     * @return
     */
    @Override
    public long now() {
        if (cacheTime) {
            return now;
        } else {
            return System.currentTimeMillis();
        }
    }

    @Override
    public boolean timeUp(long sometime) {
        if (this.now() > sometime) {
            return true;
        }
        return false;
    }

    @Override
    public long getInterval(long sometime) {
        return sometime - this.now();
    }

    @Override
    public int currentTimeInSecond() {
        return (int) (this.now() / 1000);
    }

    /**
     * 得到yyyyMMdd格式的当天
     *
     * @return
     */
    @Override
    public int getVersionOfToday() {
        return getVersionOfTime(this.now());
    }

    @SuppressWarnings("deprecation")
    public static int getVersionOfTime(long time) {
        Date date = new Date(time);
        return (date.getYear() + 1900) * 10000 + (date.getMonth() + 1) * 100 + date.getDate();
    }
}
