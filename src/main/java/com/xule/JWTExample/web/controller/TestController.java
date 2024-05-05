package com.xule.JWTExample.web.controller;

import com.xule.JWTExample.util.TokenUtil;
import com.xule.JWTExample.web.vo.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xule.JWTExample.web.vo.Response;

/**
 * @Author：xu.le
 * @Package：com.xule.JWTExample.web.controller
 * @Project：JWTExample
 * @name：TestController
 * @Date：2024/5/4 14:47
 * @Filename：TestController
 **/
@RestController
public class TestController {

    Logger logger= LoggerFactory.getLogger(TestController.class);

    @PostMapping(value = "/testToken")
    public Response testToken(@RequestBody Request request){
        Response response=new Response();

        String userName= TokenUtil.getTokenInfo(request.getToken()).getClaims().get("userName").asString();
        String role= TokenUtil.getTokenInfo(request.getToken()).getClaims().get("role").asString();

        logger.info("Get userName and role from token. userName="+userName+",role="+role);

        //After validating and getting info from token, start to process business logic...


        return response;
    }
}
