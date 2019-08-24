package com.skn.keelin.shiro.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skn.keelin.shiro.entity.BackAdminResult;

/**
 * 用于捕获和处理Controller抛出的异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomAuthenticationException.class)
    @ResponseBody
    public BackAdminResult handleAuthentication(Exception ex){
        LOG.info("Authentication Exception handler  " + ex.getMessage() );
        return BackAdminResult.build(1, ex.getMessage());
    }
}
