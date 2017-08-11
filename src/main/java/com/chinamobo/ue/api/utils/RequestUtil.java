/**
 * 
 */
package com.chinamobo.ue.api.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author WChao 2017年6月9日
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class RequestUtil {
	/**
	 * 将request中的所有参数添加到Map中
	 * @param request
	 * @return
	*/
	public static Map<String,Object> getParams(HttpServletRequest request){
	 try {
			Map<String,Object> returnParams = new HashMap<String,Object>();
			//把请求中的参数取出
			Map allParams = request.getParameterMap();
			Set entries = allParams.entrySet();
			for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String name = (String)entry.getKey();
				Object[] value = (Object[])entry.getValue();
				if(value != null){
					if(value.length == 1){
						returnParams.put(name, value[0]);
					}else{
						returnParams.put(name, value);
					}
				}
			}
			returnParams.putAll(getHeaders(request));
			return returnParams;
		} catch (Exception e) {
			e.printStackTrace();
	}
	 return null;
	}
	/**
	 * 获取所有的Header并添加到Map中;
	 * @param request
	 * @return
	 */
	public static Map<String,String> getHeaders(HttpServletRequest request){
		Map<String,String> headers = new HashMap<String,String>();
		Enumeration<String> enumsHeaders  =  request.getHeaderNames();
		while(enumsHeaders.hasMoreElements()){
			String name = enumsHeaders.nextElement().toString();
			headers.put(name, request.getHeader(name));
		}
		return headers;
	}
}
