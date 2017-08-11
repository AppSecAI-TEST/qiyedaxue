package com.chinamobo.ue.exam.dto;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;

public class ExamPaperDto {
	@FormParam("examPaperId")
	private Integer examPaperId;
	@FormParam("examPaperNumber")
	private String examPaperNumber;
	@FormParam("examPaperName")
	private String examPaperName;
	@FormParam("examPaperType")
	private String examPaperType;
	@FormParam("examPaperPicture")
	private String examPaperPicture;
	@FormParam("examTime")
	private Integer examTime;
	@FormParam("fullMark")
	private Integer fullMark;
	@FormParam("passMark")
	private Integer passMark;
	@FormParam("testQuestionCount")
	private Integer testQuestionCount;
	@FormParam("passEb")
	private Integer passEb;
	@FormParam("notPassEb")
	private Integer notPassEb;
	@FormParam("showScore")
	private String showScore;
	@FormParam("showQualifiedStandard")
	private String showQualifiedStandard;
	@FormParam("immediatelyShow")
	private String immediatelyShow;
	@FormParam("admins")
	private String admins;
	@FormParam("adminNames")
	private String adminNames;
//	@FormParam("createTime")
	private Date createTime;
	@FormParam("creator")
	private String creator;
	@FormParam("lastOperator")
	private String lastOperator;
//	@FormParam("updateTime")
	private Date updateTime;
	@FormParam("questionBankId[]")
	private List<Integer> questionBankId;
	@FormParam("sort[]")
	private List<String> sort;
	@FormParam("questionType[]")
	private List<String> questionType;
	@FormParam("count[]")
	private List<Integer> count;
	@FormParam("score[]")
	private List<Integer> score;
	@FormParam("questionBankName[]")
	private List<String> questionBankName;

	public List<String> getQuestionBankName() {
		return questionBankName;
	}

	public void setQuestionBankName(List<String> questionBankName) {
		this.questionBankName = questionBankName;
	}

	public List<String> getQuestionType() {
		return questionType;
	}

	public void setQuestionType(List<String> questionType) {
		this.questionType = questionType;
	}

	public List<Integer> getCount() {
		return count;
	}

	public void setCount(List<Integer> count) {
		this.count = count;
	}

	public List<Integer> getScore() {
		return score;
	}

	public void setScore(List<Integer> score) {
		this.score = score;
	}

	public Integer getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(Integer examPaperId) {
		this.examPaperId = examPaperId;
	}

	public String getExamPaperNumber() {
		return examPaperNumber;
	}

	public void setExamPaperNumber(String examPaperNumber) {
		this.examPaperNumber = examPaperNumber;
	}

	public String getExamPaperName() {
		return examPaperName;
	}

	public void setExamPaperName(String examPaperName) {
		this.examPaperName = examPaperName;
	}

	public String getExamPaperType() {
		return examPaperType;
	}

	public void setExamPaperType(String examPaperType) {
		this.examPaperType = examPaperType;
	}

	public String getExamPaperPicture() {
		return examPaperPicture;
	}

	public void setExamPaperPicture(String examPaperPicture) {
		this.examPaperPicture = examPaperPicture;
	}

	public Integer getExamTime() {
		return examTime;
	}

	public void setExamTime(Integer examTime) {
		this.examTime = examTime;
	}

	public Integer getFullMark() {
		return fullMark;
	}

	public void setFullMark(Integer fullMark) {
		this.fullMark = fullMark;
	}

	public Integer getPassMark() {
		return passMark;
	}

	public void setPassMark(Integer passMark) {
		this.passMark = passMark;
	}

	public Integer getTestQuestionCount() {
		return testQuestionCount;
	}

	public void setTestQuestionCount(Integer testQuestionCount) {
		this.testQuestionCount = testQuestionCount;
	}

	public Integer getPassEb() {
		return passEb;
	}

	public void setPassEb(Integer passEb) {
		this.passEb = passEb;
	}

	public Integer getNotPassEb() {
		return notPassEb;
	}

	public void setNotPassEb(Integer notPassEb) {
		this.notPassEb = notPassEb;
	}

	public String getShowScore() {
		return showScore;
	}

	public void setShowScore(String showScore) {
		this.showScore = showScore;
	}

	public String getShowQualifiedStandard() {
		return showQualifiedStandard;
	}

	public void setShowQualifiedStandard(String showQualifiedStandard) {
		this.showQualifiedStandard = showQualifiedStandard;
	}

	public String getImmediatelyShow() {
		return immediatelyShow;
	}

	public void setImmediatelyShow(String immediatelyShow) {
		this.immediatelyShow = immediatelyShow;
	}


	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getAdminNames() {
		return adminNames;
	}

	public void setAdminNames(String adminNames) {
		this.adminNames = adminNames;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public List<Integer> getQuestionBankId() {
		return questionBankId;
	}

	public void setQuestionBankId(List<Integer> questionBankId) {
		this.questionBankId = questionBankId;
	}

	public List<String> getSort() {
		return sort;
	}

	public void setSort(List<String> sort) {
		this.sort = sort;
	}

}
