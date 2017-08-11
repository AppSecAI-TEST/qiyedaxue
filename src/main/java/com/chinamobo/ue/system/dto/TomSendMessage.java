package com.chinamobo.ue.system.dto;

import java.util.Date;

public class TomSendMessage {
	
	private Integer id;

	private String name;
	
	private Date time;
	
	private String phone;
	
	private String email;
	
	private String code;
	
	private String address;

	private Integer courseId;
	
	private Integer examId;

	private String preTask;
	
	private Integer sort;
	
	private String needApply;
	
	private Date beginTime;
	
	
	
	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public String getPreTask() {
		return preTask;
	}

	public void setPreTask(String preTask) {
		this.preTask = preTask;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getNeedApply() {
		return needApply;
	}

	public void setNeedApply(String needApply) {
		this.needApply = needApply;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
