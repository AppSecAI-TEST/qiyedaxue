package com.chinamobo.ue.activity.common;


import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.poi.util.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 版本: [1.0]
 * 功能说明: 加载器
 *
 * 作者: WangLg
 * 创建时间: 2016年3月11日 下午4:44:31
 */
public class PropertiesLoader {

	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	private final Properties properties;

	public PropertiesLoader(String... resourcesPaths) {
		properties = loadProperties(resourcesPaths);
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * 功能描述：[取出Property，但以System的Property优先,取不到返回空字符串.]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:42:20
	 * @param key
	 * @return
	 */
	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		if (properties.containsKey(key)) {
	        return properties.getProperty(key);
	    }
	    return "";
	}

	/**
	 * 功能描述：[取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:42:31
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return value;
	}

	/**
	 * 功能描述：[取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:42:42
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 功能描述：[取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:42:53
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}

	/**
	 * 功能描述：[取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:43:08
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 功能描述：[取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:43:08
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * 功能描述：[取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:43:08
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Double getDouble(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 功能描述：[取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:43:08
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 功能描述：[取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:43:08
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

	/**
	 * 功能描述：[载入多个文件, 文件路径使用Spring Resource格式.]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:43:17
	 * @param resourcesPaths
	 * @return
	 */
	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();

		for (String location : resourcesPaths) {
			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException ex) {
				System.out.println("This is PropertiesLoader Exception");
			} finally {
				closeQuietly(is);
			}
		}
		return props;
	}
	
	 public static void closeQuietly(InputStream input)
	   {
	     try
	     {
	       if (input != null)
	         input.close();
	     }
	     catch (IOException localIOException)
	     {
	     }
	   }
}
