package com.chinamobo.ue.course.entity;

import java.util.Date;

/**
 * 收藏课程
 * @author ZXM
 *
 */
public class TomFavoriteCourse {
	
	private String code;//员工代码
	private Integer courseId;//课程id
	private Date createTime;//创建日期
	private String status;//状态
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
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
