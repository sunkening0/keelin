package com.skn.keelin.ConcurrentSkipListSet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 使用ConcurrentSkipListSet
 * ConcurrentSkipListSet是线程安全的有序的集合，适用于高并发的场景。
 */
@Aspect
@Component
public class NotDuplicateAop {

    private static final Set<String> KEY =  new ConcurrentSkipListSet<>();

    @Pointcut("@annotation(com.skn.keelin.ConcurrentSkipListSet.NotDuplicate)")
    public void duplicate() {
    }

    /**
     * 对方法拦截后进行参数验证
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("duplicate()")
    public Object duplicate(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature msig = (MethodSignature) pjp.getSignature();
        Method currentMethod = pjp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //拼接签名
        StringBuilder sb = new StringBuilder(currentMethod.toString());
        Object[] args = pjp.getArgs();
        for (Object object : args) {
            if(object != null){
                sb.append(object.getClass().toString());
                sb.append(object.toString());
            }
        }
        String sign = sb.toString();
        boolean success = KEY.add(sign);
        if(!success){
            throw new RuntimeException("该方法正在执行,不能重复请求");
        }
        try {
            return pjp.proceed();
        } finally {
            KEY.remove(sign);
        }

    }
}
