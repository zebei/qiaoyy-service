package com.qiaoyy;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.qiaoyy.util.DBConnectionManager;

/**
 * Application Lifecycle Listener implementation class Init
 *
 */
@WebListener
public class InitListener implements ServletContextListener {


	/**
	 * Default constructor.
	 */
	public InitListener() {
		System.out.println(this.getClass().getSimpleName()+ " InitListener");
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		
			try {
                DBConnectionManager.init();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			System.out.println("--------------------------------------------");
			System.out.println("------------------- 启动成功  ------------------");
			System.out.println("--------------------------------------------");
	}

	
	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println(this.getClass().getSimpleName()+" contextDestroyed");
	}

}
