package com.chinamobo.ue.activity.dto;

import java.util.Date;

/**
 * 描述 [证书管理实体类：记录每个学员获取的证书]
 * 创建者 LXT
 * 创建时间 2017年6月6日 下午5:37:24
 */
public class TomCertificateManageDto {
	private Long id;//id主键
	private Integer certificateId;//证书主键 目前只做展示用
	private String code;//人员code
	private String certificateAddress;//学员证书地址，粗粒度到月份
	private String afourCertificateAddress;//A4 学员证书地址
	private Integer activityId;//活动id
	private Date createTime;//创建时间
	private Integer exportCount;//下载次数
	private String state;//状态 Y N 代表证书是否可以继续领取
	
	public Integer getExportCount() {
		return exportCount;
	}
	public void setExportCount(Integer exportCount) {
		this.exportCount = exportCount;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public String getAfourCertificateAddress() {
		return afourCertificateAddress;
	}
	public void setAfourCertificateAddress(String afourCertificateAddress) {
		this.afourCertificateAddress = afourCertificateAddress;
	}
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
