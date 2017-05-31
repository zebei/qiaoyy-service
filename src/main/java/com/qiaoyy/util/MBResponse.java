package com.qiaoyy.util;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class MBResponse {

	public String code;
	public Object data;
	public Map<String, Object> map;
	
	
	
	private MBResponse(String code,Object data,Map<String, Object> map){
		this.code=code;
		this.data=data;
		this.map=map;
	}

	
	public static MBResponse getMBResponse(String code){
		return new MBResponse(code,null,null);
	}
	
	public static MBResponse getMBResponse(String code,Object data){
		return new MBResponse(code,data,null);
	}
	
	public static MBResponse getMBResponse(String code,Object data,Map<String, Object> map){
		return new MBResponse(code, data,map);
	}
	

	
	public static void sendResponse(HttpServletResponse response,MBResponse responseModel) throws UnsupportedEncodingException, IOException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/json; charset=UTF-8");
		response.getOutputStream().write(JSONObject.toJSONString(responseModel).getBytes("UTF-8"));
	}
}
