package com.chinamobo.ue.utils;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.ums.DBContextHolder;

/**
 * 描述 []
 * 创建者 LXT
 * 创建时间 2017年7月11日 下午3:54:13
 */
@Aspect
@Component
public class DataMasterSlaveReflex {
	//切点
    @Pointcut("execution(* com.chinamobo.ue.*.dao.*Mapper.*(..))")
    private  void msReflex() {
    }
    
    @Before("msReflex()")
    public void beforeMethod(JoinPoint joinPoint) {
    	MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();  
    	Method method = methodSignature.getMethod();  
    	if(method.isAnnotationPresent(DataMasterSlaveAnnotation.class)){
    		DataMasterSlaveAnnotation obj=method.getAnnotation(DataMasterSlaveAnnotation.class);
    		if(obj.name()!=null){
    			DBContextHolder.setDbType(obj.name());
    		}
    	} 
    }
}
