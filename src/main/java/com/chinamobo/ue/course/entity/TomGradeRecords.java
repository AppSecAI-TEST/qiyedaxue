package com.chinamobo.ue.course.entity;

import java.util.Date;

public class TomGradeRecords {
    private Integer courseId;

    private String code;

    private Date createTime;

    private String gradeType;

    private Integer gradeDimensionId;

    private Double score;
    
    private String gradeDimensionName;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType == null ? null : gradeType.trim();
    }

    public Integer getGradeDimensionId() {
        return gradeDimensionId;
    }

    public void setGradeDimensionId(Integer gradeDimensionId) {
        this.gradeDimensionId = gradeDimensionId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

	public String getGradeDimensionName() {
		return gradeDimensionName;
	}

	public void setGradeDimensionName(String gradeDimensionName) {
		this.gradeDimensionName = gradeDimensionName;
	}
    
    
}