package com.qiaoyy.mannger;

import org.apache.log4j.Logger;

import com.qiaoyy.dao.UserDao;
import com.qiaoyy.model.UserModel;



public class UserManager {
    private static final  UserManager userManager=new UserManager();
    private static Logger logger = Logger.getLogger(UserManager.class);
    private UserManager() {

    }

    public static UserManager getInstance() {
        return userManager;
    }
    public void loginSaveUser(UserModel userModel) {
        userModel.setLastLoginTime(System.currentTimeMillis());
        loginSaveUserThread(userModel);
    }
    private void loginSaveUserThread(UserModel user) {
        final UserModel userModel = user;
        new Thread() {
            public void run() {
                try {
                  UserDao.loginSaveUser(userModel);  
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }
}
