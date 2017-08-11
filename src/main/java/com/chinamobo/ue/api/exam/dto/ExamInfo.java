package com.chinamobo.ue.api.exam.dto;

import java.util.List;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.exam.entity.TomTopic;

public class ExamInfo {
    
	@FormParam("examTime")
    private Integer examTime;

	@FormParam("testQuestionCount")
    private Integer testQuestionCount;

	private List<TomTopic> topics;

	public Integer getExamTime() {
		return examTime;
	}

	public void setExamTime(Integer examTime) {
		this.examTime = examTime;
	}

	public Integer getTestQuestionCount() {
		return testQuestionCount;
	}

	public void setTestQuestionCount(Integer testQuestionCount) {
		this.testQuestionCount = testQuestionCount;
	}

	public List<TomTopic> getTopics() {
		return topics;
	}

	public void setTopics(List<TomTopic> topics) {
		this.topics = topics;
	}

}
