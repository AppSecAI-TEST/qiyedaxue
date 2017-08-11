package com.chinamobo.ue.statistics.entity;

import java.util.Date;

public class TomAttendanceStatistics {
	
	private Integer activityId;
	
	private Integer courseId;
	
	private String activityName;
	
	private String courseNumber;
	private String courseName;
	
	private Date courseStartTime;
	private Date courseEndTime;
	private Integer toBe;
	
	private Integer to;
	private String courseOnline;
	private String percentage;
	
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public Date getCourseStartTime() {
		return courseStartTime;
	}

	public void setCourseStartTime(Date courseStartTime) {
		this.courseStartTime = courseStartTime;
	}

	public Date getCourseEndTime() {
		return courseEndTime;
	}

	public void setCourseEndTime(Date courseEndTime) {
		this.courseEndTime = courseEndTime;
	}

	public Integer getToBe() {
		return toBe;
	}

	public void setToBe(Integer toBe) {
		this.toBe = toBe;
	}

	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseOnline() {
		return courseOnline;
	}

	public void setCourseOnline(String courseOnline) {
		this.courseOnline = courseOnline;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
}
