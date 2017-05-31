package com.qiaoyy.controller.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.log.AppLog;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.repository.UserRepository;
import com.qiaoyy.util.MBResponse;
import com.qiaoyy.util.MBResponseCode;
import com.qiaoyy.util.MBUtil;

@Controller
public class TestController {

    @Autowired
    private UserRepository userRepository;

    public TestController() {
        super();

    }

    @RequestMapping(value = "/test/test")
    protected void test(HttpServletRequest request, HttpServletResponse response) {
        try {
            String apiTransaction=MBUtil.getUUID();
            String reqJsonContent = MBUtil.getRequestBodyJson(request);
            String reqJsonHeaders=MBUtil.getRequestHeadersJson(request);
            AppLog.LOG_COMMON.info("RequestTransaction:"+apiTransaction+" RequestHeader:" + reqJsonHeaders+" RequestContent:"+reqJsonContent);
            JSONObject jsonObject=JSON.parseObject(reqJsonContent);
            UserModel userModel = userRepository.findByUserid(jsonObject.getLong("userid"));
            MBResponse responseModel = null;
            if (userModel!=null) {
                responseModel = MBResponse.getMBResponse(MBResponseCode.SUCCESS, userModel);
            }else {
                responseModel=MBResponse.getMBResponse(MBResponseCode.ERROR);
            }
            MBResponse.sendResponse(response, responseModel);
            AppLog.LOG_COMMON.info("ResponseTransaction:"+apiTransaction+" ResponseContent:"+JSONObject.toJSONString(responseModel));
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            AppLog.LOG_COMMON.error(this.getClass().getSimpleName() + " Exception :", e);
        }
    }

}
