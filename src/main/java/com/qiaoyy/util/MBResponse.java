package com.qiaoyy.util;


import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.log.AppLog;

public class MBResponse {

    public String code;
    public Object data;
    public Map<String, Object> map;


    private MBResponse(String code, Object data, Map<String, Object> map) {
        this.code = code;
        this.data = data;
        this.map = map;
    }


    public static MBResponse getMBResponse(String code) {
        return new MBResponse(code, null, null);
    }

    public static MBResponse getMBResponse(String code, Object data) {
        return new MBResponse(code, data, null);
    }

    public static MBResponse getMBResponse(String code, Object data, Map<String, Object> map) {
        return new MBResponse(code, data, map);
    }

    public static void sendResponse(HttpServletRequest request, HttpServletResponse response, MBResponse responseModel) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/json; charset=UTF-8");
        response.getOutputStream().write(JSONObject.toJSONString(responseModel).getBytes("UTF-8"));
        AppLog.LOG_INTERFACE.info("[resp][{}] - {}", MBRequest.getUUID(request), JSON.toJSONString(responseModel));
    }
}
