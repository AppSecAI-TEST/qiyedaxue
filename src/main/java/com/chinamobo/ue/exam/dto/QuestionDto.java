package com.chinamobo.ue.exam.dto;

import java.util.List;

public class QuestionDto {

	private List<OptionDto> questionOptions; 
	private String questionAnswer;
	private List<String> questionKeys;
	private String questionFileUrl;
	private String questionName;
	private String questionType;
	private String questionImgUrl;
	
	public List<OptionDto> getQuestionOptions() {
		return questionOptions;
	}
	public void setQuestionOptions(List<OptionDto> questionOptions) {
		this.questionOptions = questionOptions;
	}
	public String getQuestionAnswer() {
		return questionAnswer;
	}
	public void setQuestionAnswer(String questionAnswer) {
		this.questionAnswer = questionAnswer;
	}
	public List<String> getQuestionKeys() {
		return questionKeys;
	}
	public void setQuestionKeys(List<String> questionKeys) {
		this.questionKeys = questionKeys;
	}
	public String getQuestionFileUrl() {
		return questionFileUrl;
	}
	public void setQuestionFileUrl(String questionFileUrl) {
		this.questionFileUrl = questionFileUrl;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getQuestionImgUrl() {
		return questionImgUrl;
	}
	public void setQuestionImgUrl(String questionImgUrl) {
		this.questionImgUrl = questionImgUrl;
	}
	
}
