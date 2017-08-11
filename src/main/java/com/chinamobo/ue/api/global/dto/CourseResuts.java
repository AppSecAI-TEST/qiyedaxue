package com.chinamobo.ue.api.global.dto;

import com.chinamobo.ue.activity.common.DataEntity;

public class CourseResuts  extends DataEntity<CourseResuts>{
 private String courseId;
 private String courseName;
 private String classifyName;
 private String commentCount;
 private String thumbUpCount;
 private String courseImg;
 private String status;
 private String isExcellentCourse;
 private String online;
public String getCourseId() {
	return courseId;
}
public void setCourseId(String courseId) {
	this.courseId = courseId;
}
public String getCourseName() {
	return courseName;
}
public void setCourseName(String courseName) {
	this.courseName = courseName;
}
public String getClassifyName() {
	return classifyName;
}
public void setClassifyName(String classifyName) {
	this.classifyName = classifyName;
}
public String getCommentCount() {
	return commentCount;
}
public void setCommentCount(String commentCount) {
	this.commentCount = commentCount;
}
public String getThumbUpCount() {
	return thumbUpCount;
}
public void setThumbUpCount(String thumbUpCount) {
	this.thumbUpCount = thumbUpCount;
}
public String getCourseImg() {
	return courseImg;
}
public void setCourseImg(String courseImg) {
	this.courseImg = courseImg;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getIsExcellentCourse() {
	return isExcellentCourse;
}
public void setIsExcellentCourse(String isExcellentCourse) {
	this.isExcellentCourse = isExcellentCourse;
}
public String getOnline() {
	return online;
}
public void setOnline(String online) {
	this.online = online;
}
 
}
