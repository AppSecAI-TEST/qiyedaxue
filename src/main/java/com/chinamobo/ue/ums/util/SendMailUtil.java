package com.chinamobo.ue.ums.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.chinamobo.ue.utils.Config;


public class SendMailUtil {
      public static  String use=Config.getProperty("username");
	  public static  String pas=Config.getProperty("password");

	  public static SendMail getMail(List<String> to ,String title,Date date,String form,String text ) throws AddressException, MessagingException, UnsupportedEncodingException{
		  Map<String,String> map= new HashMap<String,String>();
		    SendMail mail = new SendMail(use,pas);
		    map.put("mail.smtp.host", "smtp.exmail.qq.com");//QQ邮箱支持企业邮箱.
//		    map.put("mail.smtp.host", "smtp.163.com"); //163邮箱
	        map.put("mail.smtp.auth", "true");
	        map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        map.put("mail.smtp.port", "465");
	        map.put("mail.smtp.socketFactory.port", "465");
	        map.put("mail.transport.protocol", "stmp");
	        mail.setPros(map);
	        mail.initMessage();	
//          list接收参数
//	        List<String> list = new ArrayList<String>();
//	        String[] split = to.split(",");
//	        for (String string : split) {
//				list.add(string);
//			}
	        mail.setRecipients(to);   //收件人   
	        mail.setSubject(title);	      //标题  
	        mail.setDate(date);       //时间
	        mail.setFrom(form);       //发件人姓名
	        mail.setContent(text, "text/html; charset=UTF-8"); //内容
			return mail;
	       
	  }

	
}
