package com.qiaoyy.mannger.dispather;

import com.qiaoyy.thread.ThreadType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 何金成
 * @ClassName: Api
 */
public enum Api implements ApiEnum {

    TEST(10000, "测试", ThreadType.PLAYER_THREAD),
    STONE(10001, "石头", ThreadType.PLAYER_THREAD),;

    private int code;
    private String note;
    private final ThreadType threadType;

    private Api(int code, String note) {
        this.code = code;
        this.note = note;
        this.threadType = null;
    }

    private Api(int code, String note, ThreadType threadType) {
        this.code = code;
        this.note = note;
        this.threadType = threadType;
    }

    private static Map<Integer, Api> idMap;

    static {
        idMap = new HashMap<Integer, Api>();
        for (Api api : values()) {
            idMap.put(api.getCode(), api);
        }
    }

    public static Api getByApiId(int code) {
        return idMap.get(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return String.format("[%d#%s", code, note);
    }

    public ThreadType getThreadType() {
        return threadType;
    }
}
