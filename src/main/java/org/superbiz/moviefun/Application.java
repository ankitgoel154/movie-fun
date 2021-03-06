package org.superbiz.moviefun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.swing.*;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean ServletRegistrationBean(ActionServlet actionServlet){
        ServletRegistrationBean bean = new ServletRegistrationBean(
                actionServlet, "/moviefun/*");
        bean.setLoadOnStartup(1);
        return bean;
    }
}