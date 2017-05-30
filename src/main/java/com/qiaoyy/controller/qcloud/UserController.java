package com.qiaoyy.controller.qcloud;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 6579706670441711811L;

    /**
     * 从请求中获取会话中的用户信息
     */
    @RequestMapping(value = "user")
    @ResponseBody
    public String getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        LoginService service = new LoginService(request, response);
        JSONObject result = new JSONObject();
        try {
            // 调用检查登录接口，成功后可以获得用户信息，进行正常的业务请求
            UserInfo userInfo = service.check();

            // 获取会话成功，输出获得的用户信息
            JSONObject data = new JSONObject();
            data.put("userInfo", new JSONObject(userInfo));
            result.put("code", 0);
            result.put("message", "OK");
            result.put("data", data);
            return result.toString();
        } catch (LoginServiceException e) {
            e.printStackTrace();
            result.put("code", 1);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 2);
            return result.toString();
        }
    }
}
