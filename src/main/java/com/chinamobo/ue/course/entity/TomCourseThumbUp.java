package com.chinamobo.ue.course.entity;

import java.util.Date;

/**
 * 课程点赞
 * @author ZXM
 *
 */
public class TomCourseThumbUp {
	private Integer courseId;//课程ID
	private String code;//员工编辑
	private Date createTime;//创建日期
	private String status;//状态
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
		this.code = code;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}

