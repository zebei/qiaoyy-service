package com.qiaoyy.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class MBUtil {
    private final static String USERID = "userid";
    private final static String MODEL = "model";
    private final static String VERSION = "version";
    private final static String SYSTEM = "system";
    private final static String PLATFORM = "platform";
    private final static String SDKVERSION = "SDKVersion";

    public static boolean checkParmsValid(Object... objs) {

        for (Object object : objs) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }

    public static String getUUID() {

        UUID uuid = UUID.randomUUID();
        // 得到对象产生的ID
        String a = uuid.toString();
        a = a.toUpperCase();
        // 替换 -
        a = a.replaceAll("-", "");
        return a;
    }


    public static String getRequestHeadersJson(HttpServletRequest request) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put(USERID, request.getHeader(USERID));
        map.put(MODEL, request.getHeader(MODEL));
        map.put(VERSION, request.getHeader(VERSION));
        map.put(SYSTEM, request.getHeader(SYSTEM));
        map.put(PLATFORM, request.getHeader(PLATFORM));
        map.put(SDKVERSION, request.getHeader(SDKVERSION));
        return JSONObject.toJSONString(map);
    }

    public static String getRequestBodyJson(HttpServletRequest request) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = (String) parameterNames.nextElement();

            String paramValue = request.getParameter(paramName);
            map.put(paramName, paramValue);
        }
        return JSONObject.toJSONString(map);
    }
}
