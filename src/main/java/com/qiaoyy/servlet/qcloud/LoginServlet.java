package com.qiaoyy.servlet.qcloud;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;
import com.qiaoyy.mannger.UserManager;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.servlet.test.TestServlet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 6585319986631669934L;
    private static Logger logger = Logger.getLogger(LoginServlet.class);

    /**
     * 处理登录请求
     * */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // 通过 ServletRequest 和 ServletResponse 初始化登录服务
        LoginService service = new LoginService(request, response);
        try {
            // 调用登录接口，如果登录成功可以获得登录信息
            UserInfo userInfo = service.login();
            UserModel userModel=new UserModel();
            try {
                BeanUtils.copyProperties(userModel, userInfo);
                UserManager.getInstance().loginSaveUser(userModel);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
            logger.debug("========= LoginSuccess, UserInfo: ==========");
            logger.debug(userInfo.toString());
        } catch (LoginServiceException e) {
            // 登录失败会抛出登录失败异常
            e.printStackTrace();
        } catch (ConfigurationException e) {
            // SDK 如果还没有配置会抛出配置异常
            e.printStackTrace();
        }
    }

}
