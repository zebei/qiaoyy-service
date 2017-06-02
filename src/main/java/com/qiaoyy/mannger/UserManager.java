package com.qiaoyy.mannger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qiaoyy.core.AppInit;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.repository.UserRepository;

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

    public UserModel loginSaveUser(UserModel userModel) {
        long currentTime = System.currentTimeMillis();
        userModel.setLastLoginTime(currentTime);
        userModel.setRegisTime(currentTime);
        UserModel user = AppInit.run.getBean(UserRepository.class).findByOpenId(userModel.getOpenId());   
        if (user!=null) {
           userModel.setId(user.getId());
        }
        AppInit.run.getBean(UserRepository.class).saveAndFlush(userModel);
        return userModel;
        //loginSaveUserThread(userModel);
    }

    public UserModel findByUserid(Long userId) {
        return userRepository.findByUserid(userId);
       
        
    }

//    private void loginSaveUserThread(UserModel user) {
//        final UserModel userModel = user;
//        ThreadPool.dispatch(ThreadType.PLAYER_THREAD, new Runnable() {
//            @Override
//            public void run() {
//                 UserModel user = AppInit.run.getBean(UserRepository.class).findByOpenId(userModel.getOpenId());   
//                 if (user!=null) {
//                    userModel.setId(user.getId());
//                 }
//                 AppInit.run.getBean(UserRepository.class).saveAndFlush(userModel);
//                 
//            }
//        });
//
//    }
}
