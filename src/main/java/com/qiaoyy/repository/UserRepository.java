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

    @Query(value = "from UserModel where nickName=:name")
    public UserModel findByName(@Param("name") String name);
}
