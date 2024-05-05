package com.xule.JWTExample.web.controller;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.xule.JWTExample.util.TokenUtil;
import com.xule.JWTExample.web.vo.Response;
import com.xule.JWTExample.web.vo.Request;
import com.xule.JWTExample.web.vo.LoginVo;
import com.xule.JWTExample.web.vo.TokenVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author：xu.le
 * @Package：com.xule.JWTExample.web.controller
 * @Project：JWTExample
 * @name：LoginController
 * @Date：2024/5/4 14:47
 * @Filename：LoginController
 **/
@RestController
public class LoginController {

    Logger logger= LoggerFactory.getLogger(LoginController.class);

    @PostMapping(value = "/common/login")
    public Response login(@RequestBody LoginVo loginVo){

        logger.info("User login,loginVo="+loginVo);

        //1. Login authentication.
        boolean result=true;
        String userName= loginVo.getUserName();

        //2. Login failed, don't create token.
        if(!result){
            Response response=new Response();
            response.setRespCode("01");
            response.setErrorMsg("login failed,password error");
            logger.info("User login failed,userName="+userName+",response="+response);
            return response;
        }

        //3. Login successful, query user information from db such as role.
        String role="01";

        //4. Create a token and return the userName and role to the front end through the token.
        TokenVo tVo=new TokenVo();
        String token=TokenUtil.createToken(userName,role);
        tVo.setToken(token);

        //5. Encapsulated response.
        Response response=new Response();
        response.setData(tVo);

        logger.info("User login successful,userName="+userName+",response="+response);
        return response;
    }

    @PostMapping(value = "/common/refreshToken")
    public Response refreshToken(@RequestBody Request request) {
        Response response = new Response();
        String token=request.getToken();

        try {
            logger.info("Start to refreshToken,old token=" + token);
            //1. refreshToken
            String newToken = TokenUtil.refreshToken(token);

            //2. Encapsulated response.
            TokenVo tVo = new TokenVo();
            tVo.setToken(newToken);
            response.setData(tVo);

            logger.info("Finish refreshToken,token="+token+",response=" + response);
        } catch (SignatureVerificationException e) {
            logger.error("RefreshToken Exception,token="+ token,e);
            response.setRespCode("01");
            response.setErrorMsg("signature error");
        } catch (AlgorithmMismatchException e) {
            logger.error("RefreshToken Exception,token="+ request.getToken(),e);
            response.setRespCode("03");
            response.setErrorMsg("AlgorithmMismatch error");
        } catch (InvalidClaimException e) {
            logger.error("RefreshToken Exception,token="+ request.getToken(),e);
            response.setRespCode("04");
            response.setErrorMsg("Payload error");
        } catch (Exception e) {
            logger.error("RefreshToken Exception,token="+ request.getToken(),e);
            response.setRespCode("99");
            response.setErrorMsg("Unknown error");
        }
        return response;
    }
}