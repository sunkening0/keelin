package com.skn.keelin.shiro.oauth2.token;

import java.io.Serializable;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

public class PhoneToken implements HostAuthenticationToken, RememberMeAuthenticationToken, Serializable {

    // 手机号码
    private String phone;
    private boolean rememberMe;
    private String host;

    /**
     * 重写getPrincipal方法
     */
    public Object getPrincipal() {
        return phone;
    }

    /**
     * 重写getCredentials方法
     */
    public Object getCredentials() {
        return phone;
    }

    public PhoneToken() { this.rememberMe = false; }

    public PhoneToken(String phone) { this(phone, false, null); }

    public PhoneToken(String phone, boolean rememberMe) { this(phone, rememberMe, null); }

    public PhoneToken(String phone, boolean rememberMe, String host) {
        this.phone = phone;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHost() {
        return host;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }
}

