package com.xule.JWTExample.web.interceptor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePathLists = new ArrayList<>();
        //Exclude two path,login and refreshToken
        excludePathLists.add("/common/login");
        excludePathLists.add("/common/refreshToken");


        registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePathLists);
    }

    @Bean
    FilterRegistrationBean<MyRequestFilter> myRequestFilterFilterRegistrationBean(){
        FilterRegistrationBean<MyRequestFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new MyRequestFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }


}
