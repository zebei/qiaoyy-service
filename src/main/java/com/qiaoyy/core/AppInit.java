package com.qiaoyy.core;

import com.qiaoyy.log.AppLog;
import com.qiaoyy.netty.WebSocketServer;
import com.qiaoyy.qcloud.QCloud;
import com.qiaoyy.thread.ThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Henry on 2017/5/30.
 */
public class AppInit {
    public static SpringApplication spring;
    public static ConfigurableApplicationContext run;

    /**
     * App初始化入口
     * 加载、初始化、启动相应的组件服务
     *
     * @return
     */
    public static boolean init() {
        try {
            // TODO App的所有组件初始化
            AppLog.stdout("app.server.init.start");
            AppLog.stdout("QCloud.sdk.setup");
            QCloud.setupSDK();
            AppLog.stdout("thread.pool.init");
            ThreadPool.initThreadsExecutor();
            AppLog.stdout("netty.webSocket.start");
            WebSocketServer.getInstance().start();
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.stdout("app.server.init.err - {}", e);
            System.exit(0);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }));
        AppLog.stdout("app.server.init.finish");
        return true;
    }

    /**
     * App关闭钩子
     * 程序关闭退出前的资源清理
     *
     * @return
     */
    public static boolean shutdown() {
        // TODO App的所有组件资源清理
        AppLog.stdout("app.server.shutdown.start");
        AppLog.stdout("thread.pool.shutdown");
        ThreadPool.shutdown();
        AppLog.stdout("netty.webSocket.stop");
        WebSocketServer.getInstance().shut();
        AppLog.stdout("app.server.shutdown.finish");
        return true;
    }
}
