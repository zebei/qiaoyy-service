package com.qiaoyy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qiaoyy.model.UserModel;

/**
 * Created by Henry on 2017/5/30.
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query(value = "from UserModel where id=:userid")
    public UserModel findByUserid(@Param("userid") Long userid);
    @Query(value="from UserModel where open_id=:openId")
    public UserModel findByOpenId(@Param("openId")String openId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update qyuser  set last_login_time =?1 where id = ?2",nativeQuery = true)
    int updateLastLoginTime( Long lastLoginTime,  Long id);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update qyuser  set score =score+?1 where id = ?2",nativeQuery = true)
    int updateScoreById( Integer changeScore,  Long id);
    
}
