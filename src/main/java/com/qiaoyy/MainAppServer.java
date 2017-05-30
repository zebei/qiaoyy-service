package com.qiaoyy;

import com.qiaoyy.core.AppInit;
import com.qiaoyy.log.AppLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ServletComponentScan
@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
public class MainAppServer {

    public static void main(String[] args) {
        AppLog.LOG_NET.info("app.spring.main.start");
        AppInit.spring = new SpringApplication(MainAppServer.class);
        AppInit.run = AppInit.spring.run(args);
        AppInit.init();
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        return factory;
    }
}
