package com.qiaoyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import com.qiaoyy.core.AppInit;
import com.qiaoyy.log.AppLog;

@ServletComponentScan
@SpringBootApplication
public class MainAppServer {

    public static void main(String[] args) {
        AppLog.stdout("app.spring.main.start");
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
