package com.qiaoyy.thread;

import com.qiaoyy.util.EnumUtil;
import com.qiaoyy.util.IndexedEnum;

import java.util.List;

/**
 * Created by Henry on 2017/4/23.
 */
public enum ThreadType implements IndexedEnum {

    MAIN_THREAD(1, "主线程"),

    MAIN_IO_THREAD(2, "主IO线程"),

    LOGIN_THREAD(3, "登陆线程"),

    PLAYER_THREAD(4, "玩家线程"),

    MAIL_THREAD(5, "邮件线程"),

    COUNTRY_THREAD(6, "国战线程"),

    CHANNEL_HEART_BEAT_THREAD(7, "通道心跳检测线程"),

    SYS_THREAD(8, "系统内部线程"),

    CHAT_THREAD(9, "聊天线程"),

    PAY_THREAD(10, "支付线程"),

    PK_THREAD(11, "竞技场线程"),

    MAIN_LOG_THREAD(12, "主日志线程"),

    PAY_LOG_THREAD(13, "支付日志线程"),

    IP_LOG_THREAD(14, "IP日志线程"),;

    private int threadType;
    private String threadName;

    private ThreadType(int threadType, String threadName) {
        this.threadType = threadType;
        this.threadName = threadName;
    }

    private static final List<ThreadType> values = IndexedEnum.IndexedEnumUtil
            .toIndexes(ThreadType.values());

    public static ThreadType valueOf(int index) {
        return EnumUtil.valueOf(values, index);
    }

    public int getThreadType() {
        return threadType;
    }

    public void setThreadType(int threadType) {
        this.threadType = threadType;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public String toString() {
        return String.format("threadType: %d, threadName: %s", threadType, threadName);
    }

    @Override
    public int getIndex() {
        return threadType;
    }


    }
