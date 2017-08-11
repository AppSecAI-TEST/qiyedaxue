package com.chinamobo.ue.statistics.entity;

public class ExamAnswerDto {
	
//	"考试名称"
	private String examName;
//	"员工编号"
	private String code;
//	"姓名"
	private String name;
//	"题型"
	private String type;
	//题目编号
	private int topicId;
//	"试题内容"
	private String topicName;
//	"所有答案"
	private String allOption;
//	"正确答案"
	private String rightOption;
//	"用户答案"
	private String answer;
//	"是否正确"
	private String isRight;
	
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getAllOption() {
		return allOption;
	}
	public void setAllOption(String allOption) {
		this.allOption = allOption;
	}
	public String getRightOption() {
		return rightOption;
	}
	public void setRightOption(String rightOption) {
		this.rightOption = rightOption;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getIsRight() {
		return isRight;
	}
	public void setIsRight(String isRight) {
		this.isRight = isRight;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
}
