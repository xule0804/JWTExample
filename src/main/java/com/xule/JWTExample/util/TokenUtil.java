package com.xule.JWTExample.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author：xu.le
 * @Package：com.xule.JWTExample.util
 * @Project：JWTExample
 * @name：TokenUtil
 * @Date：2024/5/4 14:47
 * @Filename：TokenUtil
 **/
public class TokenUtil {


    static Logger logger= LoggerFactory.getLogger(TokenUtil.class);

    //key
    public static final String SECRET = "youareapig??shabixiangpojie?";

    //EXPIRE TIME:minute
    public static final int EXPIRE = 30;

    //1. create token
    public static String createToken(String userName,String role){
        Calendar nowTime = Calendar.getInstance();
        //Calculate expiration time.
        nowTime.add(Calendar.MINUTE, EXPIRE);
        Date expireDate = nowTime.getTime();
        String token = JWT.create()
                //set user info in token
                .withClaim("userName",userName)
                .withClaim("role",role)
                //set issue time
                .withIssuedAt(new Date())
                //set expire time
                .withExpiresAt(expireDate)
                //sign
                .sign(Algorithm.HMAC256(SECRET));
        logger.info("TokenUtil create , token="+token);
        logger.info("TokenUtil create , expireDate="+expireDate);
        return token;
    }

    //2. validate token
    public static DecodedJWT verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        return decodedJWT;
    }

    public static String refreshToken(String token){
        String newToken="";

        try{
            DecodedJWT decodedJWT =JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
            String userName=decodedJWT.getClaim("userName").asString();
            String role=decodedJWT.getClaim("role").asString();
            newToken=createToken(userName,role);

            System.out.println("The token is not expired.create a new token，old token="+token+",new token="+newToken);
        }catch (TokenExpiredException e){
            DecodedJWT decodedJWT=JWT.decode(token);

            String userName=decodedJWT.getClaim("userName").asString();
            String role=decodedJWT.getClaim("role").asString();

            newToken=createToken(userName,role);

            System.out.println("The token is expired.create a new token，old token="+token+",newToken="+newToken);
        }
        return newToken;
    }



    //Get the payload in the token, which is the second part of the message.
    public static DecodedJWT getTokenInfo(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);

        return decodedJWT;
    }
}
