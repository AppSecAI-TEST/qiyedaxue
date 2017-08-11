package com.chinamobo.ue.api.activity.dto;

import java.util.Date;

public class TaskData {
	private String type;
	private String name;
	private Integer id;
	private Integer sort;
	private String taskProgress;
	private String online;
	private String taskStatus;
	private String preTask;
	private Date startTime;
	private Date endTime;
	private String startTimeS;
	private String endTimeS;
	private String courseExamImg;
	private String address;
	
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTaskProgress() {
		return taskProgress;
	}
	public void setTaskProgress(String taskProgress) {
		this.taskProgress = taskProgress;
	}
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getPreTask() {
		return preTask;
	}
	public void setPreTask(String preTask) {
		this.preTask = preTask;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getStartTimeS() {
		return startTimeS;
	}
	public void setStartTimeS(String startTimeS) {
		this.startTimeS = startTimeS;
	}
	public String getEndTimeS() {
		return endTimeS;
	}
	public void setEndTimeS(String endTimeS) {
		this.endTimeS = endTimeS;
	}
	public String getCourseExamImg() {
		return courseExamImg;
	}
	public void setCourseExamImg(String courseExamImg) {
		this.courseExamImg = courseExamImg;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
