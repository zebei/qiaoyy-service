package com.qiaoyy.mannger;

import com.qiaoyy.model.UserModel;
import com.qiaoyy.repository.UserRepository;
import com.qiaoyy.thread.ThreadPool;
import com.qiaoyy.thread.ThreadType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManager {
    private static final UserManager userManager = new UserManager();
    private static Logger logger = Logger.getLogger(UserManager.class);

    @Autowired
    private UserRepository userRepository;

    private UserManager() {

    }

    public static UserManager getInstance() {
        return userManager;
    }

    public void loginSaveUser(UserModel userModel) {
        long currentTime = System.currentTimeMillis();
        userModel.setLastLoginTime(currentTime);
        userModel.setRegisTime(currentTime);
        loginSaveUserThread(userModel);
    }

    private void loginSaveUserThread(UserModel user) {
        final UserModel userModel = user;
        ThreadPool.dispatch(ThreadType.PLAYER_THREAD, new Runnable() {
            @Override
            public void run() {
                userRepository.save(userModel);
            }
        });

    }
}
