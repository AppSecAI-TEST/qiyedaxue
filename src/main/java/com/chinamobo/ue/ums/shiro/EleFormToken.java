package com.chinamobo.ue.ums.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class EleFormToken extends UsernamePasswordToken{
	public EleFormToken(){
		
	}
	public EleFormToken(String userName,String passWord){
		super(userName,passWord);
	}
}
