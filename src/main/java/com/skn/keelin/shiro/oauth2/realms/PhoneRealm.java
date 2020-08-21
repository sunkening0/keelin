package com.skn.keelin.shiro.oauth2.realms;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.skn.keelin.shiro.config.CustomAuthenticationException;
import com.skn.keelin.shiro.entity.User;
import com.skn.keelin.shiro.oauth2.token.PhoneToken;
import com.skn.keelin.shiro.service.UserService;

public class PhoneRealm extends AuthorizingRealm {

    @Resource
    UserService userService;

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        PhoneToken token = (PhoneToken) authenticationToken;

        String phone = (String) token.getPrincipal();

        User user = userService.selectByPhone(phone);

        if (user == null) {
            throw new CustomAuthenticationException("手机号错误");
        }
        return new SimpleAuthenticationInfo(user, phone, this.getName());
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    
    @Override
    public boolean supports(AuthenticationToken var1){
        return var1 instanceof PhoneToken;
    }
}

