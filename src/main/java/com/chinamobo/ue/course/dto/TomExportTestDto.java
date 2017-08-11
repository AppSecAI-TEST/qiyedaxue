package com.chinamobo.ue.course.dto;

import java.util.Date;

public class TomExportTestDto {
	//@FormParam("testUserId")
	private String testUserId;
	//@FormParam("testUserName")
	private String testUserName;
	
	
	//@FormParam("testQuestionId")
	private int testQuestionId;
	//@FormParam("testQuestionName")
	private String testQuestionName;
	//@FormParam("testUserAnswer")
	private String testUserAnswer;
	//@FormParam("endTime")
	private Date testEndTime;
	//@FormParam("useTime")
	private double testUseTime;
	
	//@FormParam("testId")
	private int testId;
	//@FormParam("testName")
	private String testName;
	//@FormParam("courseId")
	private int courseId;
	//@FormParam("startTime")
	private Date testStartTime;
	
	
	
	
	
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	
	
	public String getTestUserId() {
		return testUserId;
	}
	public void setTestUserId(String testUserId) {
		this.testUserId = testUserId;
	}
	public int getTestQuestionId() {
		return testQuestionId;
	}
	public void setTestQuestionId(int testQuestionId) {
		this.testQuestionId = testQuestionId;
	}
	public String getTestQuestionName() {
		return testQuestionName;
	}
	public void setTestQuestionName(String testQuestionName) {
		this.testQuestionName = testQuestionName;
	}
	public String getTestUserAnswer() {
		return testUserAnswer;
	}
	public void setTestUserAnswer(String testUserAnswer) {
		this.testUserAnswer = testUserAnswer;
	}
	
	
	
	
	public double getTestUseTime() {
		return testUseTime;
	}
	public void setTestUseTime(double testUseTime) {
		this.testUseTime = testUseTime;
	}
	public String getTestUserName() {
		return testUserName;
	}
	public void setTestUserName(String testUserName) {
		this.testUserName = testUserName;
	}
	public Date getTestEndTime() {
		return testEndTime;
	}
	public void setTestEndTime(Date testEndTime) {
		this.testEndTime = testEndTime;
	}
	public Date getTestStartTime() {
		return testStartTime;
	}
	public void setTestStartTime(Date testStartTime) {
		this.testStartTime = testStartTime;
	}

}
