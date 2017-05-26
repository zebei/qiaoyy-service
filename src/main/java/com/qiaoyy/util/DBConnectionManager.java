package com.qiaoyy.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;



public class DBConnectionManager {
	private static SqlSessionFactory sqlSessionFactory;


	private DBConnectionManager() {

	}

	private static final DBConnectionManager connectionManager = new DBConnectionManager();

	public static DBConnectionManager getInstance() {
		return connectionManager;
	}

	public static void init() throws IOException {
		if (sqlSessionFactory == null) {
			InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "production");
		}
	}

	public static void init(String env) throws IOException {
		if (sqlSessionFactory == null) {
			InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,env);
		}
	}
	
	public static SqlSessionFactory getSessionFactory() {
		return sqlSessionFactory;
	}
}
