package com.chinamobo.ue.course.entity;

import java.util.Date;

/**
 * 课程评论点赞表
 * @author zxm
 *
 */
public class TomCourseCommentThumbUp {
	private Integer courseCommentId;//课程评论ID
	private String code;//点赞人
	private Date createTime;//创建日期
	private String status;//状态
	public Integer getCourseCommentId() {
		return courseCommentId;
	}
	public void setCourseCommentId(Integer courseCommentId) {
		this.courseCommentId = courseCommentId;
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
