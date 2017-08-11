package com.chinamobo.ue.system.dto;

import javax.ws.rs.FormParam;

public class LabelDto {
	@FormParam("tagid")
	private String tagid;
	@FormParam("tagname")
	private String tagname;
	
	public String getTagid() {
		return tagid;
	}
	public void setTagid(String tagid) {
		this.tagid = tagid;
	}
	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	
}
