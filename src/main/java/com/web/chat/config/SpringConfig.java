package com.web.chat.config;

import com.web.chat.loggingfilter.ClientInfo;
import com.web.chat.loggingfilter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public ClientInfo info() {
        return new ClientInfo();
    }

    @Bean
    public FilterRegistrationBean<MyFilter> someFilterRegistration() {

        FilterRegistrationBean<MyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(someFilter());
        registration.addUrlPatterns("/url/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("someFilter");
        registration.setOrder(1);
        return registration;
    }

    public MyFilter someFilter() {
        return new MyFilter();
    }
}
