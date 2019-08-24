package com.skn.keelin.shiro.config;

import org.apache.shiro.authc.AuthenticationException;

public class CustomAuthenticationException extends AuthenticationException {

    // 异常信息
    private String msg;

    public CustomAuthenticationException(String msg){
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}