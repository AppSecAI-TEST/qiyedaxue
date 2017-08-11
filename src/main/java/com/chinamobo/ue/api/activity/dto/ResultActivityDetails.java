package com.chinamobo.ue.api.activity.dto;

import com.chinamobo.ue.utils.DateUtil;

public class ResultActivityDetails {
	private String courseId;
	private String examId;
	private String courseOnline;
//	private String courseImg;
	private String courseName;
	private String examName;
	private String applyStatus;
	private String starttime;
	private String endtime;
	private String finishStatus;
	private String courseExamImg;
	private String courseExam;
	private String gradeState;
	private String examType;
	private String taskProgress;
	private String preTask;
	private String taskStatus;
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getCourseOnline() {
		return courseOnline;
	}
	public void setCourseOnline(String courseOnline) {
		this.courseOnline = courseOnline;
	}
	
//	public String getCourseImg() {
//		return courseImg;
//	}
//	public void setCourseImg(String courseImg) {
//		this.courseImg = courseImg;
//	}
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
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getStarttime() {
		if(null != starttime){
			return starttime.substring(0, 16);
		}else{
			return DateUtil.getDateTime();
		}
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime.substring(0, 16);
	}
	public String getEndtime() {
		if(null != endtime){
			return endtime.substring(0, 16);
		}else{
			return DateUtil.getDateTime();
		}
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime.substring(0, 16);
	}
	public String getCourseExamImg() {
		return courseExamImg;
	}
	public void setCourseExamImg(String courseExamImg) {
		this.courseExamImg = courseExamImg;
	}
	public String getTaskProgress() {
		return taskProgress;
	}
	public void setTaskProgress(String taskProgress) {
		this.taskProgress = taskProgress;
	}
	public String getPreTask() {
		return preTask;
	}
	public void setPreTask(String preTask) {
		this.preTask = preTask;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getCourseExam() {
		return courseExam;
	}
	public void setCourseExam(String courseExam) {
		this.courseExam = courseExam;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getGradeState() {
		return gradeState;
	}
	public void setGradeState(String gradeState) {
		this.gradeState = gradeState;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	
}
