package com.chinamobo.ue.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class Config {
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	private static final Config config = new Config();
	private static final Properties appConfigProperties = initProperties("app_config.properties");
	private Config(){}
	
	public static Config getInstance(){
		return config;
	}
	
	private static Properties initProperties(String fileName){
		String appConfDirName = System.getProperty("catalina.base");
		Assert.hasLength(appConfDirName);
		String appConfigFile = appConfDirName + "/appconf/" + fileName;
		Properties properties = new Properties();
		logger.info("读取配置文件：{}...", appConfigFile);
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(appConfigFile);
			properties.load(inputStream);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return properties;
	}
	public static String getProperty(String key) {
		return appConfigProperties.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue) {
		return appConfigProperties.getProperty(key, defaultValue);
	}
	
}
