package com.xule.JWTExample.web.vo;


import lombok.Data;


/**
 * Base response class for the Project.Other response can extend it.
 *
 * @Author：xu.le
 * @Package：com.xule.JWTExample.web
 * @Project：JWTExample
 * @name：Response
 * @Date：2024/5/4 14:47
 * @Filename：Response
 **/
@Data
public class Response {

    private String respCode="00";

    private String errorMsg;

    private Object data;

}
