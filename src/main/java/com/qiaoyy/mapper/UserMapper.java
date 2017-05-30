package com.qiaoyy.mapper;

import com.qiaoyy.model.UserModel;

import java.util.List;

/**
 * Created by Henry on 2017/5/30.
 */
public interface UserMapper {
    /**
     * 查找UserModel
     * @param id
     * @return
     */
    public UserModel findOne(Long id);

    public List<UserModel> findAll();

    public void insert(UserModel user);
}
