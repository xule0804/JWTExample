package com.xule.JWTExample.web.vo;

import lombok.Data;

/**
 * Base request class for the Project.Other request can extend it.
 *
 * @Author：xu.le
 * @Package：com.xule.JWTExample.web.vo
 * @Project：JWTExample
 * @name：LoginVo
 * @Date：2024/5/4 14:47
 * @Filename：BaseRequest
 **/
@Data
public class Request {

    private String token;

}

