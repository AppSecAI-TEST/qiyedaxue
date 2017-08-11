package com.chinamobo.ue.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;



public class MessagePost {
 
	public static String account=Config.getProperty("account");
	public static String pswd=Config.getProperty("pswd");
	public static String url=Config.getProperty("url");
//	public static String account="mobokj_ele";
//	public static String pswd="Chinamobo123";
//	public static String url="http://222.73.117.156/msg/";
	public static boolean needstatus=true;
	/**
	 * 功能介绍：[公共短信推送]
	 * 创建人：LG
	 * 创建时间：2016年6月27日 9：33
	 * @param mobile 
	 * @param msg
	 */
	public static String putMessage(String mobile ,String msg)throws Exception{
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		/**
		 * 创蓝短信平台帐号
		 */
		map.put("account", account);
		
		/**
		 * 创蓝短信平台密码
		 */
		map.put("pswd", pswd);
		
		/**
		 * 手机号码(多个号码用英文逗号分割)
		 */
		map.put("mobile", mobile);
		
		/**
		 * 短信内容(短信内容，短信内容长度不能超过585个字符。使用URL方式编码为UTF-8格式。短信内容超过70个字符（企信通是60个字符）时，会被拆分成多条，然后以长短信的格式发送。)
		 */
		map.put("msg", msg);
		
		/**
		 * 状态报告
		 */
		map.put("needstatus", needstatus);
		
		
		HttpClient client=new HttpClient();
		GetMethod method=new GetMethod();
		
		try {
			URI base=new URI(url, false);
			method.setURI(new URI(base, "HttpBatchSendSM", false));
			method.setQueryString(new NameValuePair[]{
					new NameValuePair("account", account),
					new NameValuePair("pswd", pswd),
					new NameValuePair("mobile", mobile),
					new NameValuePair("needstatus", String.valueOf(needstatus)),
					new NameValuePair("msg", msg),
					
			});
			int result=client.executeMethod(method);
			if(result==HttpStatus.SC_OK){
				InputStream input=method.getResponseBodyAsStream();
				ByteArrayOutputStream output=new ByteArrayOutputStream();
				byte[] buffer=new byte[1024];
				int len=0;
				while((len=input.read(buffer))!=-1){
					output.write(buffer, 0, len);
				}
				return URLDecoder.decode(output.toString(), "UTF-8");
				
			}else{
				throw new Exception("HTTP ERROR Status: "+method.getStatusCode()+":"+method.getStatusText());
			}
		
		}finally{
			method.releaseConnection();
		}
		
	}
}
