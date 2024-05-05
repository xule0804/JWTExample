package com.xule.JWTExample.web.interceptor;


import jakarta.servlet.*;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Interceptor for JSON data.
 *
 * @Author：xu.le
 * @Package：com.xule.JWTExample.web.interceptor
 * @Project：JWTExample
 * @name：MyRequestFilter
 * @Date：2024/5/5 21:13
 * @Filename：MyRequestFilter
 **/
public class MyRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest){
            System.out.println("MyRequestFilter doFilter");
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            if(StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)){
                MyRequestWrapper wrapper = new MyRequestWrapper(request,(HttpServletResponse) servletResponse);
                System.out.println("MyRequestWrapper....");
                filterChain.doFilter(wrapper,servletResponse);
                return;
            }
            filterChain.doFilter(request,servletResponse);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
