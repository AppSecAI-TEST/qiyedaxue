package com.chinamobo.ue.ums.util;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


public class TestSend
{
	
	public static void main(String[] args) throws AddressException, UnsupportedEncodingException, MessagingException {
		
		
        List<String> to = new ArrayList<String>();
        to.add("dk100080102@163.com");
 
        String title="哈喽啊";
        Date date = new Date();
        String form="WSJ";
        String text="请勿回复";
	    
		SendMail sm= SendMailUtil.getMail(to,title,date,form,text );
		sm.sendMessage();
			
	}
	

}