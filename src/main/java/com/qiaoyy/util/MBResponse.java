package com.qiaoyy.util;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class MBResponse {

	public String code;
	public Object data;
	public String updatetime;
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
	

	
	public static void sendResponse(HttpServletResponse response,String result) throws UnsupportedEncodingException, IOException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/json; charset=UTF-8");
		response.getOutputStream().write(result.getBytes("UTF-8"));
	}
}