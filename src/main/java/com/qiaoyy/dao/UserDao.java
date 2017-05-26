package com.qiaoyy.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.qiaoyy.model.UserModel;
import com.qiaoyy.util.DBConnectionManager;



public class UserDao {
    public static UserModel selectModel() {
        SqlSession session = null;
        UserModel result = null;
        try {
            SqlSessionFactory sessionFactory = DBConnectionManager.getSessionFactory();
            session = sessionFactory.openSession();
            result = session.selectOne("mapper_qyuser.select");
        } finally {
            session.close();
        }
        return result;
    }
    public static void loginSaveUser(UserModel userModel) {
        SqlSession session = null;
        try {
            SqlSessionFactory sessionFactory = DBConnectionManager.getSessionFactory();

            session = sessionFactory.openSession();
            session.insert("mapper_qyuser.loginSaveUser", userModel);
            session.commit();
        } finally {
            session.close();
        }
    }
}
