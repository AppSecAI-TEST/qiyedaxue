package com.chinamobo.ue.ums.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.util.WebUtils;

import com.chinamobo.ue.ums.shiro.ShiroUser;

public class ShiroUtils {

	public static ShiroUser getCurrentUser() {
		Object principal = null;
		try {
			principal = SecurityUtils.getSubject().getPrincipal();
		} catch (UnavailableSecurityManagerException e) {
			ThreadContext.bind(ApplicationContextUtils.getBean("securityManager", DefaultWebSecurityManager.class));
			principal = SecurityUtils.getSubject().getPrincipal();
		}
		if(principal instanceof ShiroUser){
			return (ShiroUser)principal;
		}
		return null;
	}
	
	public static String getUserName(){
		return getCurrentUser().getName();
	}
}
