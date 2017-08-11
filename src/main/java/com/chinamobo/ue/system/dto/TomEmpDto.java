package com.chinamobo.ue.system.dto;

import java.util.Date;

public class TomEmpDto {

	private Integer courseId;
	

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	private String courseName;
	
	private Integer eCurrency;
	
	private Date finishTime;
	
	private String finishStatus;
	
	private String examName;
	
	private Date beginTime;
	
	private String  address;
	
	private String gradeState;
	
private Integer totalPoints;

private String join;

private Integer examId;

public Integer getExamId() {
	return examId;
}

public void setExamId(Integer examId) {
	this.examId = examId;
}

public String getJoin() {
	return join;
}

public void setJoin(String join) {
	this.join = join;
}

public String getExamName() {
	return examName;
}

public void setExamName(String examName) {
	this.examName = examName;
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

public String getGradeState() {
	return gradeState;
}

public void setGradeState(String gradeState) {
	this.gradeState = gradeState;
}



public Integer getTotalPoints() {
	return totalPoints;
}

public void setTotalPoints(Integer totalPoints) {
	this.totalPoints = totalPoints;
}

public Integer getExamEb() {
	return examEb;
}

public void setExamEb(Integer examEb) {
	this.examEb = examEb;
}

public void setFinishTime(Date finishTime) {
	this.finishTime = finishTime;
}

private Integer examEb;
	
private Integer notPassEb;

	public Integer getNotPassEb() {
	return notPassEb;
}

public void setNotPassEb(Integer notPassEb) {
	this.notPassEb = notPassEb;
}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer geteCurrency() {
		return eCurrency;
	}

	public void seteCurrency(Integer eCurrency) {
		this.eCurrency = eCurrency;
	}



	public Date getFinishTime() {
		return finishTime;
	}

	public String getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
}
