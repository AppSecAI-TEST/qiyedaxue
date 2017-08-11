package com.chinamobo.ue.api.exam.dto;

import java.util.List;

public class AnswerResults {
	private String userId;
	
	private String token;
	
	private Integer examId;
	
	private Integer examTotalTime;
	 
	private List<EmpAnswer> empAnswers;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<EmpAnswer> getEmpAnswers() {
		return empAnswers;
	}

	public void setEmpAnswers(List<EmpAnswer> empAnswers) {
		this.empAnswers = empAnswers;
	}

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public Integer getExamTotalTime() {
		return examTotalTime;
	}

	public void setExamTotalTime(Integer examTotalTime) {
		this.examTotalTime = examTotalTime;
	}

	@Override
	public String toString() {
		return "AnswerResults [userId=" + userId + ", token=" + token
				+ ", examId=" + examId + ", empAnswers=" + empAnswers + "]";
	}
	
}
