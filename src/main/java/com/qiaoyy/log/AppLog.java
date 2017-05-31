package com.qiaoyy.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppLog {

    /**
     * Net日志
     */
    public static final Logger LOG_NET = LogManager.getLogger("netLog");
    /**
     * 普通日志
     */
    public static final Logger LOG_COMMON = LogManager.getLogger("commonLog");
    /**
     * 资源日志
     */
    public static final Logger LOG_RESOURCE = LogManager.getLogger("resourceLog");
    /**
     * 请求日志请求接口日志
     */
    public static final Logger LOG_INTERFACE = LogManager.getLogger("interfaceLog");
    /**
     * 后台数据分析日志
     */
    public static final Logger LOG_BI = LogManager.getLogger("biLog");
    /**
     * 只打印SQL语句
     */
    public static final Logger LOG_SQL = LogManager.getLogger("sqlLog");
    /**
     * 异常日志
     */
    public static final Logger LOG_ERROR = LogManager.getLogger("errorLog");
    /**
     * 通过HTTP请求其它服务日志
     */
    public static final Logger LOG_HTTP = LogManager.getLogger("httpLog");
    /**
     * 系统统计日志
     */
    public static final Logger LOG_PERF = LogManager.getLogger("perfLog");
    /**
     * 消息队列日志
     */
    public static final Logger LOG_QUEUE = LogManager.getLogger("messageQueueLog");
    /**
     * groovy操作日志
     */
    public static final Logger LOG_GROOVY = LogManager.getLogger("groovyLog");
    /**
     * dbCall日志
     */
    public static final Logger LOG_CALL = LogManager.getLogger("dbCall");
    /**
     * 默认日志
     */
    public final static Log LOG_STDOUT = LogFactory.getLog(AppLog.class);

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    public static void stdout(String content, Object... objects) {
        FormattingTuple format = MessageFormatter.format(content, objects);
        Date date = new Date();
        String time = SDF.format(date);

        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();

        StackTraceElement stack[] = currentThread.getStackTrace();
        String realFileName = "";
        int realLineNum = 0;
        for (int i = 0; i < stack.length; i++) {
            StackTraceElement stackTraceElement = stack[i];
            String className = stackTraceElement.getFileName();
            if (!"Thread.java".equals(className) && !"AppLog.java".equals(className)) {
                realFileName = stackTraceElement.getFileName();
                realLineNum = stackTraceElement.getLineNumber();
                break;
            }
        }

        StringBuffer sb = new StringBuffer(time + " [" + name + "] " + "(" + realFileName + ":" + realLineNum + ")");
        String tmp = sb.toString();
        if (StringUtils.isEmpty(format.getMessage())) {
            System.out.println(tmp + " " + format.getMessage());
        }
        if (format.getThrowable() != null) {
            format.getThrowable().printStackTrace();
        }
    }

}
