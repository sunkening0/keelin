package com.skn.keelin.shiro.oauth2.realms;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.skn.keelin.shiro.config.CustomAuthenticationException;
import com.skn.keelin.shiro.entity.User;
import com.skn.keelin.shiro.service.UserService;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    
    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        /*if(authenticationToken instanceof UsernamePasswordToken){
            token = (UsernamePasswordToken) authenticationToken;
        }else{
            return null;
        }*/

        String username = token.getUsername();

        if(StringUtils.isEmpty(username)){
            return null;
        }

        User user = userService.getUser(token.getUsername());

        // 账号不存在
        if (user == null) {
            throw new CustomAuthenticationException("账号或密码不正确");
        }

        // 账号锁定
        if (user.getStatus() == 1) {
            throw new CustomAuthenticationException("账号已被锁定,请联系管理员");
        }

        // 主体，一般存用户名或用户实例对象，用于在其他地方获取当前认证用户信息
        Object principal = user;
        // 凭证，这里是从数据库取出的加密后的密码，Shiro会用于与token中的密码比对
        String hashedCredentials = user.getPassword();
        // 以用户名作为盐值
        ByteSource credentialsSalt = ByteSource.Util.bytes(token.getUsername());
        
        return new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, this.getName());
    }
    
    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	//因为当有多个realm的时候就会产生多个principal（身份信息），
    	//此处最需要注意的是getPrimaryPrincipal，如果只有一个Principal那么直接返回即可，
    	//如果有多个Principal，则返回第一个（因为内部使用Map存储，所以可以认为是返回任意一个）；
    	//也可以通过PrincipalCollection接口的其他方法如：oneByType / byType根据凭据的类型返回相应的Principal；fromRealm根据Realm名字（每个Principal都与一个Realm关联）获取相应的Principal
    	
    	/*SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //获取用户权限列表  （先从redis中取   取不到在查数据库   然后将数据更新到redis）
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;*/
    	
        return null;
    }
    
    @Override
    public boolean supports(AuthenticationToken var1){
        return var1 instanceof UsernamePasswordToken;
    }
}

