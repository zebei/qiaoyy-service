package com.qiaoyy.core;

import com.qiaoyy.log.AppLog;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class Init
 */
@WebListener
public class InitListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public InitListener() {
        AppLog.LOG_COMMON.info("app.initlistener.init - {}", this.getClass().getSimpleName());
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        AppLog.LOG_COMMON.info("app.initlistener.init");
    }


    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        AppLog.LOG_COMMON.info(this.getClass().getSimpleName() + " contextDestroyed");
        AppLog.LOG_COMMON.info("app.initlistener.destoryed - {}", this.getClass().getSimpleName());
    }

}
