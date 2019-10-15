package com.skn.keelin.annotation;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.skn.keelin.redis.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {
    @Autowired
    private RedisUtil redisutil;

    
    private static final long time = 5;//5秒内不得重复调用
    
    @Pointcut("@annotation(repeatSubmitCheck)")
    public void requestPointcut(RepeatSubmitCheck repeatSubmitCheck) {
    }

    
    @Before("requestPointcut(repeatSubmitCheck)")
    public void aroundCheck(JoinPoint joinPoint, RepeatSubmitCheck repeatSubmitCheck) {
    	String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
    	Object[] args = joinPoint.getArgs();
        HttpServletRequest request = httpServletRequest();

        if(request!=null){
        	String token = request.getParameter("token");
        	log.info("RepeatSubmitAspect---token={token}",token);
        	if(token!=null){
        		//token = md5Utils.encrypt(token);
            	long x = redisutil.incr(token, 1, time);
            	if(x > 1){
            		throw new RepeatSubmitCheckException(100, "重复提交,请稍后");
            	}
        	}else{
        		throw new RepeatSubmitCheckException(300, "token异常");
        	}
        	
        	
        }else{
        	throw new RepeatSubmitCheckException(200, "request can not be null");
        }
    }
    
    /**
     * 获得request对象
     *
     * @return
     */
    private HttpServletRequest httpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }
}