package com.chinamobo.ue.activity.dto;

import java.util.Date;

/**
 * 描述 [证书模版实体类]
 * 创建者 LXT
 * 创建时间 2017年6月6日 下午5:20:58
 */
public class TomCertificateDto {
	private Integer id;//证书id
	private String name;//证书名称
	private String address;//图片地址；基础模版存一路径；自定义模版存一路径；学员证书存一路径，粗粒度为月份
	private String isEnable;//状态  是否启用 Y：是，N：否 ；默认Y
	private String isBase;//是否为基础模版 Y：是， N：否  默认N
	private String attribute;//属性 key:{x：坐标 y：坐标 width：宽度 height：高度 fontSize：字体大小 fontStyle：字体风格}
	private Integer imageWidth;//图片宽度
	private Integer imageHeight;//图片高度
	private Integer multiple;//倍数，A4纸  属性值乘以5
	private String afourAddress;//A4图片地址
	private String afourAttribute;//A4 属性 key:{x：坐标 y：坐标 width：宽度 height：高度 fontSize：字体大小 fontStyle：字体风格}
	private Integer afourImageWidth;//A4图片宽度
	private Integer afourImageHeight;//A4图片高度
	private String createUserId;//创建人id
	private	String updateUserId;
	private Date createTime;//创建时间
	private Date updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	public String getIsBase() {
		return isBase;
	}
	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public Integer getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}
	public Integer getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}
	public Integer getMultiple() {
		return multiple;
	}
	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}
	public String getAfourAddress() {
		return afourAddress;
	}
	public void setAfourAddress(String afourAddress) {
		this.afourAddress = afourAddress;
	}
	public String getAfourAttribute() {
		return afourAttribute;
	}
	public void setAfourAttribute(String afourAttribute) {
		this.afourAttribute = afourAttribute;
	}
	public Integer getAfourImageWidth() {
		return afourImageWidth;
	}
	public void setAfourImageWidth(Integer afourImageWidth) {
		this.afourImageWidth = afourImageWidth;
	}
	public Integer getAfourImageHeight() {
		return afourImageHeight;
	}
	public void setAfourImageHeight(Integer afourImageHeight) {
		this.afourImageHeight = afourImageHeight;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	


	


	
	
	
}
