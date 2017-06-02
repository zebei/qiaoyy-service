package com.qiaoyy.controller.qcloud;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;
import com.qiaoyy.log.AppLog;
import com.qiaoyy.mannger.UserManager;
import com.qiaoyy.model.UserModel;

@Controller
public class LoginController {

    /**
     * 处理登录请求
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public void login(HttpServletRequest request, HttpServletResponse response) {
        // 通过 ServletRequest 和 ServletResponse 初始化登录服务
        LoginService service = new LoginService(request, response);
        try {
            // 调用登录接口，如果登录成功可以获得登录信息
            JSONObject loginResult = service.login();
            JSONObject json = service.prepareResponseJson();
            JSONObject session = new JSONObject();
            JSONObject userInfo = null;
            try {
                userInfo = loginResult.getJSONObject("user_info");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UserInfo userInfoModel = UserInfo.BuildFromJson(userInfo);
            UserModel userModel = new UserModel();
            try {
                BeanUtils.copyProperties(userModel, userInfoModel);
                UserManager.getInstance().loginSaveUser(userModel);
                session.put("id", loginResult.get("id"));
                session.put("skey", loginResult.get("skey"));
                session.put("userId", userModel.getId());
                session.put("score", userModel.getScore());
                json.put("session", session);
                service.writeJson(json);

            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            AppLog.LOG_COMMON.debug("========= LoginSuccess, UserInfo: ==========");
            AppLog.LOG_COMMON.debug(userInfo.toString());
        } catch (LoginServiceException e) {
            // 登录失败会抛出登录失败异常
            e.printStackTrace();
        } catch (ConfigurationException e) {
            // SDK 如果还没有配置会抛出配置异常
            e.printStackTrace();
        }
    }

}
