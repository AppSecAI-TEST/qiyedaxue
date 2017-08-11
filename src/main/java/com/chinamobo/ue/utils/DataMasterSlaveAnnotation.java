package com.chinamobo.ue.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.chinamobo.ue.ums.DBContextHolder;

/**
 * 描述 []
 * 创建者 LXT
 * 创建时间 2017年7月11日 下午4:46:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataMasterSlaveAnnotation {
	public String name() default DBContextHolder.SLAVE; 
}
