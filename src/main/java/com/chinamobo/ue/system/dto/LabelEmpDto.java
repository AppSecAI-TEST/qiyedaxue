package com.chinamobo.ue.system.dto;

import javax.ws.rs.FormParam;

public class LabelEmpDto {
	@FormParam("userid")
	private String userid;
	@FormParam("name")
	private String name;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
