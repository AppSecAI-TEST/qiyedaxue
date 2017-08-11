package com.chinamobo.ue.statistics.entity;

import java.util.Date;

import com.qcloud.cosapi.http.ResponseBodyKey.Data;

public class TomDetailedAttendanceStatistics {
	
	private Integer activityId;
	
	private String signTime;
	
	private String name;
	
	private String code;
	
	private String deptName;
	
	private String oneDeptName;
	
	private String attendance;
	
	private String phoneNumber;
	
	private String email;
	
	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getOneDeptName() {
		return oneDeptName;
	}

	public void setOneDeptName(String oneDeptName) {
		this.oneDeptName = oneDeptName;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
