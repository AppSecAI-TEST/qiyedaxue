package com.chinamobo.ue.course.entity;

import java.util.Date;

/**
 * 章节学习记录表
 * @author ZXM
 *
 */
public class TomCourseSectionLearningRecord {
	
	private Integer courseSectionId;//课程章节ID
	private String code;//用户Code
	private Date createTime;//创建时间
	private String learningStates;//学习状态  Y N
	private String courseStudyStates;//课程学习状态 Y N
	private String whetherGrade;//是否评分 Y N
	
	public String getWhetherGrade() {
		return whetherGrade;
	}
	public void setWhetherGrade(String whetherGrade) {
		this.whetherGrade = whetherGrade;
	}
	public String getCourseStudyStates() {
		return courseStudyStates;
	}
	public void setCourseStudyStates(String courseStudyStates) {
		this.courseStudyStates = courseStudyStates;
	}
	public Integer getCourseSectionId() {
		return courseSectionId;
	}
	public void setCourseSectionId(Integer courseSectionId) {
		this.courseSectionId = courseSectionId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getLearningStates() {
		return learningStates;
	}
	public void setLearningStates(String learningStates) {
		this.learningStates = learningStates;
	}

}
