package com.chinamobo.ue.system.entity;

public class WxMessage {

	private String title;
	
	private String description;
	
	private String url;
	
	
	private String picurl;


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getPicurl() {
		return picurl;
	}


	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}


	public WxMessage(String title, String description, String url, String picurl) {
		super();
		this.title = title;
		this.description = description;
		this.url = url;
		this.picurl = picurl;
	}


	public WxMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
