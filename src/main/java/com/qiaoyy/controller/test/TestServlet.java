package com.qiaoyy.controller.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.log.AppLog;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.repository.UserRepository;
import com.qiaoyy.util.MBResponse;
import com.qiaoyy.util.MBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
    private UserRepository userRepository;

    public TestServlet() {
        super();

    }

    @RequestMapping(value = "/test")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String reqJsonContent = MBUtil.getRequestBodyJson(request.getInputStream());
            AppLog.LOG_COMMON.debug(this.getClass().getSimpleName() + " Request:" + reqJsonContent);
            AppLog.LOG_COMMON.info("log test>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            AppLog.LOG_COMMON.error("log error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            UserModel userModel = userRepository.findByName("111");
            AppLog.LOG_COMMON.info(JSONObject.toJSONString(userModel));
            MBResponse responseModel = null;
            String out = null;
            out = JSON.toJSONString(responseModel);
            MBResponse.sendResponse(response, out);

//            logger.Debug(this.getClass().getSimpleName() + "[openId:" + commonParm.userid + ",transaction:" + commonParm.transaction + "] Response:" + result);
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
