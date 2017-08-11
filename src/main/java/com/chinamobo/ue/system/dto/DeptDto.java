package com.chinamobo.ue.system.dto;

import javax.ws.rs.FormParam;

public class DeptDto {
	
	@FormParam("id")
	private String id;
	@FormParam("name")
	private String name;
	@FormParam("parentid")
	private String parentid;
	@FormParam("order")
	private String order;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
}
