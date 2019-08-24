package com.skn.keelin.shiro.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.skn.keelin.shiro.config.redis.RedisSessionDAO;
import com.skn.keelin.shiro.config.redis.ShiroRedisCache;
import com.skn.keelin.shiro.config.redis.ShiroRedisCacheManager;
import com.skn.keelin.shiro.oauth2.realms.PhoneRealm;
import com.skn.keelin.shiro.oauth2.realms.UserRealm;

@Configuration
public class ShiroConfig {
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private String port;
	
	/**
	 * 加密策略
	 */
	@Bean
	public CredentialsMatcher credentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		// 加密算法：MD5、SHA1
		credentialsMatcher.setHashAlgorithmName("md5");
		// 散列次数:md5(md5("")) 默认一次
		credentialsMatcher.setHashIterations(1);
		return credentialsMatcher;
	}

	/**
	 * 自定义Realm
	 */
	@Bean
	public UserRealm userRealm(CredentialsMatcher credentialsMatcher) {
		UserRealm userRealm = new UserRealm();
		userRealm.setCredentialsMatcher(credentialsMatcher);
		// userRealm.setCacheManager(shiroCacheManager());
		return userRealm;
	}

	@Bean
	public PhoneRealm phoneRealm() {
		PhoneRealm phoneRealm = new PhoneRealm();
		// phoneRealm.setCacheManager(shiroCacheManager());
		return phoneRealm;
	}

	/**
	 * 认证器
	 */
	@Bean
	public AbstractAuthenticator abstractAuthenticator(UserRealm userRealm, PhoneRealm phoneRealm) {
		// 自定义模块化认证器，用于解决多realm抛出异常问题
		ModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
		// 认证策略：AtLeastOneSuccessfulStrategy(默认)，AllSuccessfulStrategy，FirstSuccessfulStrategy
		authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
		// 加入realms
		List<Realm> realms = new ArrayList<Realm>();
		realms.add(userRealm);
		realms.add(phoneRealm);
		authenticator.setRealms(realms);
		return authenticator;
	}

	/**
	 * cookie对象; rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
	 * 
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		// System.out.println("ShiroConfiguration.rememberMeCookie()");
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间30天 ,单位秒;-->
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}
	
	/**  
	  * cookie管理对象;  
	  * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中  
	  * @return  
	 */  
	@Bean  
	public CookieRememberMeManager rememberMeManager(){  
	      //System.out.println("ShiroConfiguration.rememberMeManager()");  
	      CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();  
	      cookieRememberMeManager.setCookie(rememberMeCookie());  
	      //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)  
	      cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));  
	      return cookieRememberMeManager;  
	}  

	
	/**
     * redisManager
     *
     * @return
     */
    /*public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(Integer.parseInt(port));
        // 配置过期时间
        redisManager.setExpire(1800);
        return redisManager;
    }
	
    *//**
     * cacheManager 
     *
     * @return
     *//*
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    *//**
     * redisSessionDAO
     *//*
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }*/

    /**
     * sessionManager
     */
	private DefaultWebSessionManager sessionManager(RedisTemplate template) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(new RedisSessionDAO(template));
     // 设置session超时时间，单位为毫秒
        sessionManager.setGlobalSessionTimeout(100000);
        sessionManager.setSessionIdCookie(new SimpleCookie("sid"));
        return sessionManager;
    }
    
	private ShiroRedisCacheManager cacheManager(RedisTemplate template){
        return new ShiroRedisCacheManager(template);
    }
	
	@Bean
	public SecurityManager securityManager(UserRealm userRealm, PhoneRealm phoneRealm,
			AbstractAuthenticator abstractAuthenticator,RedisTemplate<String, Object> redisTemplate) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realms
		List<Realm> realms = new ArrayList<Realm>();
		realms.add(userRealm);
		realms.add(phoneRealm);
		securityManager.setRealms(realms);
		new EnterpriseCacheSessionDAO();
		// 自定义缓存实现，可以使用redis
		securityManager.setCacheManager(cacheManager(redisTemplate));
		// 自定义session管理，可以使用redis
		securityManager.setSessionManager(sessionManager(redisTemplate));
		// 注入记住我管理器
		securityManager.setRememberMeManager(rememberMeManager());
		// 认证器
		securityManager.setAuthenticator(abstractAuthenticator);
		return securityManager;
	}

	/*@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		System.out.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断，因为前端模板采用了thymeleaf，这里不能直接使用 ("/static/**",
		// "anon")来配置匿名访问，必须配置到每个静态目录
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/html/**", "anon");
		filterChainDefinitionMap.put("/user/dologin", "anon");
		filterChainDefinitionMap.put("/user/plogin", "anon");
		// 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		// <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filterChainDefinitionMap.put("/**", "authc");
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");

		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}*/
	
	@Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager, SessionManager sessionManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        //oauth过滤
        Map<String, Filter> filters = new HashMap<String, Filter>();
        shiroFilter.setFilters(filters);
        filters.put("oauth2", new OAuth2Filter());
        filters.put("rememberMe", new RememberAuthenticationFilter());
        
        //过滤链定义，从上向下顺序执行  一般将/**放在最为下边 
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        filterMap.put("/sys/login", "anon");
        filterMap.put("/captcha", "anon");
        // 注册用户
        filterMap.put("/sys/user/reg", "anon");
        //swagger
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/v2/api-docs-ext", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/doc.html", "anon");

        filterMap.put("/druid/**", "anon");
        // api接口放权
        filterMap.put("/api/**", "anon");
        // websocket 放权
        filterMap.put("/ws/**", "anon");
        // 静态资源
        filterMap.put("/static/**", "anon");
        filterMap.put("/user/dologin", "anon");
        
        filterMap.put("/user/plogin", "anon");
        
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilter.setLoginUrl("/login");
     	// 登录成功后要跳转的链接
        shiroFilter.setSuccessUrl("/index");
     	// 未授权界面;
        shiroFilter.setUnauthorizedUrl("/403");  //自己写403页面

        /**
         * 扩展shiro权限
         */
        /*if (adminExtendService != null) {
            Map<String, String> extendMap = adminExtendService.getShiroFilterChainDefinitionMap();
            if (extendMap != null) {
                filterMap.putAll(extendMap);
            }
        }*/
        //filterMap.put("/**", "oauth2");
        filterMap.put("/**", "rememberMe");
        
        
        
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

}
