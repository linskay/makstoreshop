package ru.shop.makstore.configuration;

import com.vaadin.flow.server.VaadinServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.http.HttpServlet;

@Configuration
public class VaadinServletConfiguration {
    @Bean
    public ServletRegistrationBean<HttpServlet> vaadinServletRegistration() {
        ServletRegistrationBean<HttpServlet> servlet = new ServletRegistrationBean<>(new VaadinServlet(), "*.html");
        servlet.addInitParameter("contextConfigLocation", "classpath:META-INF/spring/context.xml");
        servlet.setLoadOnStartup(1);
        return servlet;
    }
}
