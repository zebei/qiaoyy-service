package com.qiaoyy.repository;

import com.qiaoyy.model.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Henry on 2017/5/30.
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query(value = "from UserModel where id=:userid")
    public UserModel findByUserid(@Param("userid") Long userid);
    @Query(value="from UserModel where open_id=:openId")
    public UserModel findByOpenId(@Param("openId")String openId);
    
    
}
