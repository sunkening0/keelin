package com.skn.keelin.shiro.config;

import java.util.Collection;
import java.util.Iterator;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

/**
 * 多realms时分两种情况：
 * 1、多个realm同时认证一个业务，重写doMultiRealmAuthentication  实现认证策略（AuthenticationStrategy）的修改：AtLeastOneSuccessfulStrategy、AllSuccessfulStrategy、FirstSuccessfulStrategy（按字面意思理解即可）
 * 2、多个realm针对不同的认证方式，重写doMultiRealmAuthentication  实现不同认证方式调用哪个realm
* 类名称：CustomModularRealmAuthenticator   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月26日 上午11:28:39   
* @version
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    /**
     * 重写doMultiRealmAuthentication，抛出异常，便于自定义ExceptionHandler捕获
     */
    @Override
    public AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) throws AuthenticationException {

        AuthenticationStrategy strategy = this.getAuthenticationStrategy();
        
        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

        Iterator var5 = realms.iterator();

        while(var5.hasNext()) {
            Realm realm = (Realm)var5.next();
            aggregate = strategy.beforeAttempt(realm, token, aggregate);
            if (realm.supports(token)) {  //此处调用realms中重写的supports方法

                AuthenticationInfo info = null;
                Throwable t = null;

                info = realm.getAuthenticationInfo(token);

                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
            }
        }
        aggregate = strategy.afterAllAttempts(token, aggregate);
        return aggregate;
    }
    
    
    
    
}
