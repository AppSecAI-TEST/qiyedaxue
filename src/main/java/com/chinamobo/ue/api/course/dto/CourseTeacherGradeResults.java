package com.chinamobo.ue.api.course.dto;

import java.util.List;

/**
 * 评分课程、讲师接口参数
 * @author ZXM
 *
 */
public class CourseTeacherGradeResults {
	private String userId;
	
	private String token;
	
	private Integer courseId;
	
	private String gradeType;
	
	private List<GradeResult> GradeResult;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getGradeType() {
		return gradeType;
	}

	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}

	public List<GradeResult> getGradeResult() {
		return GradeResult;
	}

	public void setGradeResult(List<GradeResult> gradeResult) {
		GradeResult = gradeResult;
	}
	

}
