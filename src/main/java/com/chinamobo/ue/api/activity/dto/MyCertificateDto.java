package com.chinamobo.ue.api.activity.dto;

import java.util.Date;

public class MyCertificateDto {
	private Integer certificateId;//证书id
	private String code;//用户名
	private String certificateAddress;//证书图片地址
	private Date createTime;//创建时间
	private String createTimeS;//创建时间
	private Integer activityId;//活动id
	private String activityName;//活动名称
	private String state;//状态 Y N
	
	
	public Integer getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(Integer certificateId) {
		this.certificateId = certificateId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCertificateAddress() {
		return certificateAddress;
	}
	public void setCertificateAddress(String certificateAddress) {
		this.certificateAddress = certificateAddress;
	}
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeS() {
		return createTimeS;
	}
	public void setCreateTimeS(String createTimeS) {
		this.createTimeS = createTimeS;
	}
	

}
