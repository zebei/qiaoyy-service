package com.qiaoyy.servlet.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.dao.UserDao;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.util.MBResponse;
import com.qiaoyy.util.MBUtil;

@WebServlet("/test/test")
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(TestServlet.class);
    public TestServlet() {
        super();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        try {
            String reqJsonContent = MBUtil.getRequestBodyJson(request.getInputStream());
            logger.debug(this.getClass().getSimpleName() + " Request:" + reqJsonContent);
            logger.info("log test>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            logger.error("log error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            UserModel userModel = UserDao.selectModel();
            System.out.println(JSONObject.toJSONString(userModel));
            MBResponse responseModel = null;
            String out = null;
            out = JSON.toJSONString(responseModel);
            MBResponse.sendResponse(response, out);
           
//            logger.Debug(this.getClass().getSimpleName() + "[openId:" + commonParm.userid + ",transaction:" + commonParm.transaction + "] Response:" + result);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(this.getClass().getSimpleName() + " Exception :", e);
        }
    }

}
