package com.qiaoyy.controller.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.log.AppLog;
import com.qiaoyy.mannger.user.UserManager;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.util.MBRequest;
import com.qiaoyy.util.MBResponse;
import com.qiaoyy.util.MBResponseCode;

@Controller
public class TestController {

    @Autowired
    private UserManager userManager;

    public TestController() {
        super();

    }

    @RequestMapping(value = "/test/test")
    protected void test(HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject jsonObject = MBRequest.getContent(request);
            UserModel userModel =userManager.findByUserid(jsonObject.getLong("userid"));
            MBResponse responseModel = null;
            if (userModel != null) {
                responseModel = MBResponse.getMBResponse(MBResponseCode.SUCCESS, userModel);
            } else {
                responseModel = MBResponse.getMBResponse(MBResponseCode.ERROR);
            }
            MBResponse.sendResponse(request, response, responseModel);
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
