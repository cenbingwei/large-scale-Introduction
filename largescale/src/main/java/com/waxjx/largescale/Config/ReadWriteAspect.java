package com.waxjx.largescale.Config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReadWriteAspect {
    
    @Around("execution(* com.waxjx.largescale.controller.*.*(..))")//拦截所有来自controller的请求
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();
        try {
            // 根据方法名判断是读操作还是写操作
            if (isReadOperation(methodName)) {
                System.out.println("【操作标记】" + className + "." + methodName + " - 标记为读操作");
                ReadWriteType.markRead();
            } else {
                System.out.println("【操作标记】" + className + "." + methodName + " - 标记为写操作");
                ReadWriteType.markWrite();
            }
            return point.proceed();
        } finally {
            ReadWriteType.clear();
            System.out.println("【操作标记】" + className + "." + methodName + " - 清理标记");
        }
    }
    
    private boolean isReadOperation(String methodName) {
        // 根据方法名前缀判断是否为读操作
        return methodName.startsWith("get") || 
               methodName.startsWith("find") || 
               methodName.startsWith("query") || 
               methodName.startsWith("select") ||
               methodName.startsWith("list") ||
               methodName.startsWith("search");
    }
} 