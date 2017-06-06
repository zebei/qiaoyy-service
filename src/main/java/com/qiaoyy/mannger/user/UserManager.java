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

    @Autowired
    private UserRepository userRepository;


    public void loginSaveUser(UserModel userModel) {
        UserModel findByOpenId = userRepository.findByOpenId(userModel.getOpenId());
        long currentTime = System.currentTimeMillis();
        if(findByOpenId!=null){
            userModel.setId(findByOpenId.getId());
            userRepository.updateLastLoginTime(currentTime,userModel.getId());
        }else {
            userModel.setLastLoginTime(currentTime);
            userModel.setRegisTime(currentTime);
            loginSaveUserThread(userModel);
        }
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
