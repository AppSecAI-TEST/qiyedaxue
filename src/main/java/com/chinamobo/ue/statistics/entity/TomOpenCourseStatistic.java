package com.chinamobo.ue.statistics.entity;

public class TomOpenCourseStatistic {

	private Integer courseId;//课程编码
	
	private String courseNumber;
	
	private String courseName;//课程名称
	
	private String courseType;//类型
	
	private Integer learningNumber;//学习人数

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public Integer getLearningNumber() {
		return learningNumber;
	}

	public void setLearningNumber(Integer learningNumber) {
		this.learningNumber = learningNumber;
	}
	
}
