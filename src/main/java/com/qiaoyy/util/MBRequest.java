package com.qiaoyy.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Henry on 2017/5/31.
 */
public class MBRequest {

    /**
     * request uuid key
     */
    public final static String REQ_UUID = "uuid";
    /**
     * request header key
     */
    public final static String REQ_HEADER = "header";
    /**
     * request content key
     */
    public final static String REQ_CONTENT = "content";

    /**
     * request userid key
     */
    public final static String REQ_USERID = "userid";

    /**
     * 获取请求UUID
     *
     * @param request
     * @return
     */
    public static String getUUID(HttpServletRequest request) {
        return (String) request.getAttribute(REQ_UUID);
    }

    /**
     * 获取请求内容
     *
     * @param request
     * @return
     */
    public static JSONObject getContent(HttpServletRequest request) {
        JSONObject reqJson = JSONObject.parseObject((String) request.getAttribute(REQ_CONTENT));
        return reqJson;
    }

    /**
     * 获取请求头部
     *
     * @param request
     * @return
     */
    public static JSONObject getHeader(HttpServletRequest request) {
        JSONObject reqJson = JSONObject.parseObject((String) request.getAttribute(REQ_HEADER));
        return reqJson;
    }
}
