package com.chinamobo.ue.ums.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UeToken extends UsernamePasswordToken{
	public UeToken(){
	}
	public UeToken(String userName,String passWord){
		super(userName,passWord);
	}

}
