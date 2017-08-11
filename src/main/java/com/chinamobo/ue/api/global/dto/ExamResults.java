package com.chinamobo.ue.api.global.dto;

import com.chinamobo.ue.activity.common.DataEntity;

public class ExamResults extends DataEntity<ExamResults> {
  private String examId;
  private String examName;
  private String beginTime;
  private String endTime;
  private String offlineExam;
  private String examTime;
  private String examState;
  private String examPaperPicture;
public String getExamId() {
	return examId;
}
public void setExamId(String examId) {
	this.examId = examId;
}
public String getExamName() {
	return examName;
}
public void setExamName(String examName) {
	this.examName = examName;
}
public String getBeginTime() {
	return beginTime;
}
public void setBeginTime(String beginTime) {
	this.beginTime = beginTime;
}
public String getEndTime() {
	return endTime;
}
public void setEndTime(String endTime) {
	this.endTime = endTime;
}
public String getOfflineExam() {
	return offlineExam;
}
public void setOfflineExam(String offlineExam) {
	this.offlineExam = offlineExam;
}
public String getExamTime() {
	return examTime;
}
public void setExamTime(String examTime) {
	this.examTime = examTime;
}
public String getExamState() {
	return examState;
}
public void setExamState(String examState) {
	this.examState = examState;
}
public String getExamPaperPicture() {
	return examPaperPicture;
}
public void setExamPaperPicture(String examPaperPicture) {
	this.examPaperPicture = examPaperPicture;
}
  
}
