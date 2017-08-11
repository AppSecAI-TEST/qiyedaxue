package com.chinamobo.ue.ums.util;

import org.springframework.core.io.Resource;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.context.support.XmlWebApplicationContext;


public class ApplicationContextUtils {
	private static XmlWebApplicationContext applicationContext;
	private static Repositories repositories;
	private static String basePath;
	
	public static void init(XmlWebApplicationContext applicationContext){
		ApplicationContextUtils.applicationContext = applicationContext;
		repositories = new Repositories(applicationContext);
		basePath = applicationContext.getServletContext().getRealPath("/");
	}
	
	
	public static String getBasePath(){
		return basePath;
	}
	
	public static final Repositories getRepositories(){
		return repositories;
	}
	
	public static <T> T getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}
	
	public static <T> T getBean(String name, Class<T> clazz){
		return applicationContext.getBean(name, clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		return (T)applicationContext.getBean(name);
	}
	
	public static Resource getResource(String location){
		return applicationContext.getResource(location);
	}
}