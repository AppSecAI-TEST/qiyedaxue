package com.chinamobo.ue.system.dto;

import javax.ws.rs.FormParam;

public class EmpDto {
	
	@FormParam("userid")
	private String userid;
	@FormParam("name")
	private String name;
	@FormParam("department")
	private String[] department;
	@FormParam("mobile")
	private String mobile;
	@FormParam("email")
	private String email;
	@FormParam("gender")
	private String gender;
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
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
	public String[] getDepartment() {
		return department;
	}
	public void setDepartment(String[] department) {
		this.department = department;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
