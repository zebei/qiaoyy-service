package com.qiaoyy.mannger.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qiaoyy.core.AppInit;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.repository.UserRepository;
import com.qiaoyy.thread.ThreadPool;
import com.qiaoyy.thread.ThreadType;

@Component
public class UserManager {
    private static final UserManager userManager = new UserManager();

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
                AppInit.run.getBean(UserRepository.class).saveAndFlush(userModel);

            }
        });
    }

    public UserModel findByUserid(Long userId) {
        return userRepository.findByUserid(userId);
    }
}
