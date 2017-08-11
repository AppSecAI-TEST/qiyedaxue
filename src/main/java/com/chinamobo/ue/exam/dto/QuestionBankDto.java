package com.chinamobo.ue.exam.dto;

import java.util.Date;

public class QuestionBankDto {
	private Integer questionBankId;
	private String questionBankNumber;
	private String questionBankName;
	private Date createTime;
	private Integer questionClassificationId;
	private String questionClassificationName;
	private String creator;
	private String lastOperator;
	private Date updateTime;
	private Integer singleCount;//单选数量
	private Integer mcqCount;//多选数量
	private Integer tOrFCount;//判断题数量
	private Integer gapCount;//填空题数量
	private Integer essayCount;//问答题数量
	
	public String getQuestionBankNumber() {
		return questionBankNumber;
	}
	public void setQuestionBankNumber(String questionBankNumber) {
		this.questionBankNumber = questionBankNumber;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getQuestionClassificationId() {
		return questionClassificationId;
	}
	public void setQuestionClassificationId(Integer questionClassificationId) {
		this.questionClassificationId = questionClassificationId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getLastOperator() {
		return lastOperator;
	}
	public void setLastOperator(String lastOperator) {
		this.lastOperator = lastOperator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getQuestionBankId() {
		return questionBankId;
	}
	public void setQuestionBankId(Integer questionBankId) {
		this.questionBankId = questionBankId;
	}
	public String getQuestionBankName() {
		return questionBankName;
	}
	public void setQuestionBankName(String questionBankName) {
		this.questionBankName = questionBankName;
	}
	public Integer getSingleCount() {
		return singleCount;
	}
	public void setSingleCount(Integer singleCount) {
		this.singleCount = singleCount;
	}
	public Integer getMcqCount() {
		return mcqCount;
	}
	public void setMcqCount(Integer mcqCount) {
		this.mcqCount = mcqCount;
	}
	public Integer gettOrFCount() {
		return tOrFCount;
	}
	public void settOrFCount(Integer tOrFCount) {
		this.tOrFCount = tOrFCount;
	}
	public Integer getGapCount() {
		return gapCount;
	}
	public void setGapCount(Integer gapCount) {
		this.gapCount = gapCount;
	}
	public Integer getEssayCount() {
		return essayCount;
	}
	public void setEssayCount(Integer essayCount) {
		this.essayCount = essayCount;
	}
	public String getQuestionClassificationName() {
		return questionClassificationName;
	}
	public void setQuestionClassificationName(String questionClassificationName) {
		this.questionClassificationName = questionClassificationName;
	}
	
}
