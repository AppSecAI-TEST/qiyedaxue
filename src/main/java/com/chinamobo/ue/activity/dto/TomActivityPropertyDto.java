package com.chinamobo.ue.activity.dto;

import java.util.List;

import javax.ws.rs.FormParam;

public class TomActivityPropertyDto {
	@FormParam("taskId")
	private String taskId;
	@FormParam("courseAddress")
    private String courseAddress;
	@FormParam("courseId")
    private Integer courseId;
	@FormParam("sort")
    private Integer sort;
	@FormParam("courseName")
    private String courseName;
	@FormParam("examName")
    private String examName;
	@FormParam("examId")
    private Integer examId;
	@FormParam("examPaperId")
    private Integer examPaperId;
	@FormParam("examTime")
    private Integer examTime;
	@FormParam("courseTime")
    private Integer courseTime;
	@FormParam("unitPrice")
    private String unitPrice;
	@FormParam("totalPrice")
    private String totalPrice;
	@FormParam("startTime")
    private String startTime;
	@FormParam("endTime")
    private String endTime;
	@FormParam("preName")
    private String preName;
	@FormParam("pretaskId")
    private String pretaskId;
	@FormParam("retakingExamBeginTime")
	private String retakingExamBeginTime;
	@FormParam("retakingExamEndTime")
	private String retakingExamEndTime;
	@FormParam("retakingExamTimes")
	private String retakingExamTimes;
	@FormParam("isTaskType")
	private String isTaskType;
	@FormParam("lecturerId")
	private String lecturerId;
	private String lecturerName;
	public String getLecturerName() {
		return lecturerName;
	}

	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}
	@FormParam("retakingExamBeginTimeList[]")
	private List<String> retakingExamBeginTimeList;
	@FormParam("retakingExamEndTimeList[]")
	private List<String> retakingExamEndTimeList;
	@FormParam("offlineExam")
	private String offlineExam;
	@FormParam("examAddress")
	private String examAddress;
	@FormParam("signInTwoDimensionCode")
    private String signInTwoDimensionCode;

	@FormParam("gradeTwoDimensionCode")
    private String gradeTwoDimensionCode;
    
	public String getSignInTwoDimensionCode() {
		return signInTwoDimensionCode;
	}

	public void setSignInTwoDimensionCode(String signInTwoDimensionCode) {
		this.signInTwoDimensionCode = signInTwoDimensionCode;
	}

	public String getGradeTwoDimensionCode() {
		return gradeTwoDimensionCode;
	}

	public void setGradeTwoDimensionCode(String gradeTwoDimensionCode) {
		this.gradeTwoDimensionCode = gradeTwoDimensionCode;
	}
	
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCourseAddress() {
		return courseAddress;
	}
	public void setCourseAddress(String courseAddress) {
		this.courseAddress = courseAddress;
	}
	
	public Integer getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(Integer courseTime) {
		this.courseTime = courseTime;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getPreName() {
		return preName;
	}
	public void setPreName(String preName) {
		this.preName = preName;
	}
	public String getPretaskId() {
		return pretaskId;
	}
	public void setPretaskId(String pretaskId) {
		this.pretaskId = pretaskId;
	}
	public String getRetakingExamBeginTime() {
		return retakingExamBeginTime;
	}
	public void setRetakingExamBeginTime(String retakingExamBeginTime) {
		this.retakingExamBeginTime = retakingExamBeginTime;
	}
	public String getRetakingExamEndTime() {
		return retakingExamEndTime;
	}
	public void setRetakingExamEndTime(String retakingExamEndTime) {
		this.retakingExamEndTime = retakingExamEndTime;
	}
	public String getRetakingExamTimes() {
		return retakingExamTimes;
	}
	public void setRetakingExamTimes(String retakingExamTimes) {
		this.retakingExamTimes = retakingExamTimes;
	}
	public String getIsTaskType() {
		return isTaskType;
	}
	public void setIsTaskType(String isTaskType) {
		this.isTaskType = isTaskType;
	}
	
	public String getLecturerId() {
		return lecturerId;
	}
	public void setLecturerId(String lecturerId) {
		this.lecturerId = lecturerId;
	}
	public List<String> getRetakingExamBeginTimeList() {
		return retakingExamBeginTimeList;
	}
	public void setRetakingExamBeginTimeList(List<String> retakingExamBeginTimeList) {
		this.retakingExamBeginTimeList = retakingExamBeginTimeList;
	}
	public List<String> getRetakingExamEndTimeList() {
		return retakingExamEndTimeList;
	}
	public void setRetakingExamEndTimeList(List<String> retakingExamEndTimeList) {
		this.retakingExamEndTimeList = retakingExamEndTimeList;
	}
	public String getOfflineExam() {
		return offlineExam;
	}
	public void setOfflineExam(String offlineExam) {
		this.offlineExam = offlineExam;
	}
	public String getExamAddress() {
		return examAddress;
	}
	public void setExamAddress(String examAddress) {
		this.examAddress = examAddress;
	}

	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	
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

	public Integer getExamTime() {
		return examTime;
	}
	public void setExamTime(Integer examTime) {
		this.examTime = examTime;
	}
	
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getExamPaperId() {
		return examPaperId;
	}
	public void setExamPaperId(Integer examPaperId) {
		this.examPaperId = examPaperId;
	}
	@Override
	public String toString() {
		return "TomActivityPropertyDto [taskId=" + taskId + ", courseAddress=" + courseAddress + ", courseTime="
				+ courseTime + ", unitPrice=" + unitPrice + ", totalPrice=" + totalPrice + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", preName=" + preName + ", pretaskId=" + pretaskId
				+ ", retakingExamBeginTime=" + retakingExamBeginTime + ", retakingExamEndTime=" + retakingExamEndTime
				+ ", retakingExamTimes=" + retakingExamTimes + ", isTaskType=" + isTaskType + ", lecturerId="
				+ lecturerId + ", retakingExamBeginTimeList=" + retakingExamBeginTimeList + ", retakingExamEndTimeList="
				+ retakingExamEndTimeList + ", offlineExam=" + offlineExam + ", examAddress=" + examAddress + "]";
	}
	
	
}