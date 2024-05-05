package com.xule.JWTExample.web.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xule.JWTExample.util.TokenUtil;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {


    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        System.out.println("进入拦截器"+httpServletRequest.toString());
        //实际这个名字可以指定为别的，token太没有辨识度---
        //这个header是在创建完token返回给前端时指定的头部的key，vakue就是token内容
        //String token=httpServletRequest.getParameter("token");
        System.out.println("------进入prehandle-----");
        Map<String, Object> map = new HashMap<>();

        try {
            JSONObject jsonReq=this.getJson(httpServletRequest);
            System.out.println("---"+jsonReq.toJSONString());
            String token=jsonReq.getString("token");


            //这里尽行token验证，捕获异常，正常的话也不需要处理，直接抛出异常，由统一异常处理类进行处理，然后返回给前端统一数据类型。
            System.out.println("开始验证token:"+token);
            TokenUtil.verify(token);
            System.out.println("完成token验证:"+token);
            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("respCode","01");
            map.put("errorMsg", "签名不一致");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("respCode","02");
            map.put("errorMsg", "令牌过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("respCode","03");
            map.put("errorMsg", "算法不匹配");
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            map.put("respCode","04");
            map.put("errorMsg", "失效的Payload");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("respCode","99");
            map.put("errorMsg", "系统异常");
        }
        //根据自己所需选择所需的异常处理
        //map.put("state", false);
        //响应到前台: 将map转为json
        String json = new ObjectMapper().writeValueAsString(map);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().println(json);
        System.out.println("拦截器返回的应答报文:"+json);
        return false;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("----------postHandle----------");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("----------afterCompletion----------");

    }


    public JSONObject getJson(HttpServletRequest request){

        try{
            //Get inputStream from http request.
            ServletInputStream requestInputStream = request.getInputStream();
            //Read byte stream and set charset to UTF-8
            InputStreamReader ir = new InputStreamReader(requestInputStream,"utf-8");
            //Read by BufferedReader
            BufferedReader br = new BufferedReader(ir);
            //Append to json
            String line = null;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine())!=null) {
                sb.append(line);
            }

            JSONObject json = JSONObject.parseObject(sb.toString());
            return json;
        }catch(Exception e){
            System.out.println("AuthenticationInterceptor--parse json Exception，"+e);
            return null;
        }
    }


}
