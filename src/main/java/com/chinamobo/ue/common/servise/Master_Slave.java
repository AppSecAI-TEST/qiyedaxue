/**
 * 
 */
package com.chinamobo.ue.common.servise;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.chinamobo.ue.ums.DBContextHolder;

/**
 * 
 * 主从注解;
 * @author WChao
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Master_Slave {
	String value() default DBContextHolder.MASTER;
}
