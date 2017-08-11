package com.chinamobo.ue.exam.dto;

import java.util.List;

import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomTopic;



public class RandomTopicDto extends TomExamQuestion{

	private List<TomTopic> topicList;

	public List<TomTopic> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<TomTopic> topicList) {
		this.topicList = topicList;
	}
	
}
