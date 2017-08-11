package com.chinamobo.ue.statistics.entity;

import java.util.Date;

public class TomExamStatistics {
     //考试ID
	// 考试名称
	// 考试时间
	// 考试类型
	// 应考人数
	// 实考人数
	// 通过人数
	// 通过率
	// 平均分
	private Integer examId;
	private String examName;
	private Date beginTime;
	private String examType;
	private String planNum;
	private String realNum;
	private String passNum;
	private String passRate;
	private String allPoints;
	private Double averageScore;
	
	

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
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

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getRealNum() {
		return realNum;
	}

	public void setRealNum(String realNum) {
		this.realNum = realNum;
	}

	public String getAllPoints() {
		return allPoints;
	}

	public void setAllPoints(String allPoints) {
		this.allPoints = allPoints;
	}

	public String getPlanNum() {
		return planNum;
	}

	public void setPlanNum(String planNum) {
		this.planNum = planNum;
	}

	public String getPassNum() {
		return passNum;
	}

	public void setPassNum(String passNum) {
		this.passNum = passNum;
	}

	public String getPassRate() {
		return passRate;
	}

	public void setPassRate(String passRate) {
		this.passRate = passRate;
	}

	public Double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}

}
