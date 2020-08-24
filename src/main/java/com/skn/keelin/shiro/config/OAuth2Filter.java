package com.skn.keelin.shiro.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义业务过滤器
 */
/* * 自定义一个Filter，用来拦截所有的请求判断是否携带Token
* * isAccessAllowed()判断是否携带了有效的JwtToken
* * onAccessDenied()是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问 * */
public class OAuth2Filter extends AuthenticatingFilter {
    
/*    isAccessAllowed方法和onAccessDenied方法，
    只要两者有一个可以就可以了，从名字中我们也可以理解，
   逻辑是这样：先调用isAccessAllowed，如果返回的是true，则直接放行执行后面的filter和servlet，
   如果返回的是false，则继续执行后面的onAccessDenied方法，如果后面返回的是true则也可以有权限继续执行后面的filter和servelt。
    只有两个函数都返回false才会阻止后面的filter和servlet的执行。*/
    /* * 1. 返回true，shiro就直接允许访问url
    * * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        return false;
    }

    /**
     * 返回结果为true表明登录通过
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
        String token = getRequestToken((HttpServletRequest) request);
        /*if (StringUtils.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
            String json = new Gson().toJson(JsonResult.error(HttpStatus.SC_UNAUTHORIZED, "invalid token"));

            httpResponse.getWriter().print(json);
            return false;
        }*/

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
       /*// httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            JsonResult r = JsonResult.error(HttpStatus.SC_UNAUTHORIZED, throwable.getMessage());

            String json = new Gson().toJson(r);
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/

        return false;
    }

    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        /*if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter("token");
        }
*/
        return token;
    }

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
