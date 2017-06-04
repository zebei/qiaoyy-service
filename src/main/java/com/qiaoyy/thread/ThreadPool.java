package com.qiaoyy.thread;

import com.qiaoyy.log.AppLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * The type Executor pool.
 *
 * @author 何金成
 * @ClassName: ThreadPool
 * @Description: 线程池管理
 * @date 2016年4月18日 下午3:42:42
 */
public class ThreadPool {
    private static ThreadPoolExecutor mainExecutor = null;
    private static ThreadPoolExecutor mainIoExecutor = null;
    private static ThreadPoolExecutor loginExecutor = null;
    private static ThreadPoolExecutor playerExecutor = null;
    private static ThreadPoolExecutor mailExecutor = null;
    private static ThreadPoolExecutor countryExecutor = null;
    private static ThreadPoolExecutor channelHeartBeatExecutor = null;
    private static ThreadPoolExecutor sysExecutor = null;
    private static ThreadPoolExecutor chatExecutor = null;
    private static ThreadPoolExecutor payExecutor = null;
    private static ThreadPoolExecutor pkExecutor = null;
    private static ThreadPoolExecutor mainLogExecutor = null;
    private static ThreadPoolExecutor payLogExecutor = null;
    private static ThreadPoolExecutor ipLogExecutor = null;

    public static Map<String, ThreadPoolExecutor> threadPoolExecutors = new HashMap<String, ThreadPoolExecutor>();

    private static volatile boolean loaded = false;

    public static void initThreadsExecutor() {
        //取出当前CPU核数
        int processorNum = Runtime.getRuntime().availableProcessors();
        mainExecutor = initSingleThread("MAIN_EXECUTOR");
        mainIoExecutor = initSingleThread("MAIN_IO_EXECUTOR");
        loginExecutor = initFixedThreadPool(processorNum, "LOGIN_EXECUTOR");
        playerExecutor = initFixedThreadPool(processorNum * 2, "PLAYER_EXECUTOR");
        mailExecutor = initSingleThread("MAIL_EXECUTOR");
        countryExecutor = initSingleThread("COUNTRY_EXECUTOR");
        channelHeartBeatExecutor = initSingleThread("CHANNEL_HEART_BEAT_EXECUTOR");
        sysExecutor = initSingleThread("SYS_EXECUTOR");
        chatExecutor = initSingleThread("CHAT_EXECUTOR");
        payExecutor = initSingleThread("PAY_EXECUTOR");
        pkExecutor = initSingleThread("PK_EXECUTOR");
        mainLogExecutor = initSingleThread("MAIN_LOG_EXECUTOR");
        payLogExecutor = initSingleThread("PAY_LOG_EXECUTOR");
        ipLogExecutor = initSingleThread("IP_LOG_EXECUTOR");
        loaded = true;
    }

    public static void dispatch(ThreadType threadType, Runnable runnable) {
        if (!loaded) {
            return;
        }
        switch (threadType) {
            case MAIN_THREAD:
                mainExecutor.execute(runnable);
                break;
            case MAIN_IO_THREAD:
                mainIoExecutor.execute(runnable);
                break;
            case LOGIN_THREAD:
                loginExecutor.execute(runnable);
                break;
            case PLAYER_THREAD:
                playerExecutor.execute(runnable);
                break;
            case MAIL_THREAD:
                mailExecutor.execute(runnable);
                break;
            case COUNTRY_THREAD:
                countryExecutor.execute(runnable);
                break;
            case CHANNEL_HEART_BEAT_THREAD:
                channelHeartBeatExecutor.execute(runnable);
                break;
            case SYS_THREAD:
                sysExecutor.execute(runnable);
                break;
            case CHAT_THREAD:
                chatExecutor.execute(runnable);
                break;
            case PAY_THREAD:
                payExecutor.execute(runnable);
                break;
            case PK_THREAD:
                pkExecutor.execute(runnable);
                break;
            case MAIN_LOG_THREAD:
                mainLogExecutor.execute(runnable);
                break;
            case PAY_LOG_THREAD:
                payLogExecutor.execute(runnable);
                break;
            case IP_LOG_THREAD:
                ipLogExecutor.execute(runnable);
                break;
        }
    }

    /**
     * Init single thread executor service.
     *
     * @param name the name
     * @return the executor service
     */
    public static ThreadPoolExecutor initSingleThread(String name) {
        int corePoolSize = 1;
        int maximumPoolSize = 1;
        long keepAliveTime = 0L;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryBuilder().setName(name).setUncaughtExceptionHandler(new ThreadPoolExceptionHandler()).build());
        AppLog.LOG_COMMON.info("线程池{}初始化，核心线程数：{}，最大线程数：{}，阻塞队列：{}", name, corePoolSize, maximumPoolSize, executor.getQueue().getClass().getSimpleName());
        threadPoolExecutors.put(name, executor);
        return executor;
    }

    /**
     * New fixed thread pool executor service.
     *
     * @param threads the threads
     * @param name    the name
     * @return the executor service
     */
    public static ThreadPoolExecutor initFixedThreadPool(int threads, String name) {
        int corePoolSize = threads;
        int maximumPoolSize = threads;
        long keepAliveTime = 0L;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryBuilder().setName(name).setUncaughtExceptionHandler(new ThreadPoolExceptionHandler()).build());
        AppLog.LOG_COMMON.info("线程池{}初始化，核心线程数：{}，最大线程数：{}，阻塞队列：{}", name, corePoolSize, maximumPoolSize, executor.getQueue().getClass().getSimpleName());
        threadPoolExecutors.put(name, executor);
        return executor;
    }

    /**
     * Init cached thread executor service.
     *
     * @param name void
     * @return the executor service
     */
    public static ThreadPoolExecutor initCachedThread(String name) {
        int corePoolSize = 0;
        int maximumPoolSize = Integer.MAX_VALUE;
        long keepAliveTime = 60L;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactoryBuilder().setName(name).setUncaughtExceptionHandler(new ThreadPoolExceptionHandler()).build());
        AppLog.LOG_COMMON.info("线程池{}初始化，核心线程数：{}，最大线程数：{}，阻塞队列：{}", name, corePoolSize, maximumPoolSize, executor.getQueue().getClass().getSimpleName());
        threadPoolExecutors.put(name, executor);
        return executor;
    }

    /**
     * Shutdown.
     */
    public static void shutdown() {
        for (ThreadPoolExecutor executor : threadPoolExecutors.values()) {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        }
    }

    private static class ThreadFactoryBuilder implements ThreadFactory {

        private String name = "Thread";
        private Thread.UncaughtExceptionHandler handler;

        public ThreadFactory build() {
            return this;
        }

        public ThreadFactoryBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
            this.handler = handler;
            return this;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(name);
            thread.setUncaughtExceptionHandler(handler);
            return thread;
        }
    }
}
