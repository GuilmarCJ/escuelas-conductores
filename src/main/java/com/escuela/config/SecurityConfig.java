package com.escuela.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<BasicAuthFilter> basicAuthFilterRegistration(BasicAuthFilter basicAuthFilter) {
        FilterRegistrationBean<BasicAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(basicAuthFilter);
        registrationBean.addUrlPatterns("/api/admin/*");
        registrationBean.setName("basicAuthFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}