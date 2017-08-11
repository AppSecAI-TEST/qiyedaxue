package com.chinamobo.ue.system.dto;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomRollingPictureDto {
	@FormParam("pictureId")
    private Integer pictureId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.RESOURCE_ID
     *
     * @mbggenerated
     */
	@FormParam("resourceId")
    private Integer resourceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.PICTURE_CATEGORY
     *
     * @mbggenerated
     */
	@FormParam("pictureCategory")
    private String pictureCategory;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.PICTURE_URL
     *
     * @mbggenerated
     */
	@FormParam("pictureUrl")
    private String pictureUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.RELEASE_TIME
     *
     * @mbggenerated
     */
	@FormParam("releaseTime")
    private Date releaseTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.VIEW_COUNT
     *
     * @mbggenerated
     */
	@FormParam("viewCount")
    private Integer viewCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.IS_RELEASE
     *
     * @mbggenerated
     */
	@FormParam("isRelease")
    private String isRelease;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.IS_TOP
     *
     * @mbggenerated
     */
	@FormParam("isTop")
    private String isTop;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.ADMINISTRATORS
     *
     * @mbggenerated
     */
	@FormParam("admins")
    private String admins;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.CREATER
     *
     * @mbggenerated
     */
	@FormParam("creater")
    private String creater;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.CREATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("createTime")
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.OPERATOR
     *
     * @mbggenerated
     */
	@FormParam("operator")
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.UPDATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("updateTime")
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE.DELETED
     *
     * @mbggenerated
     */
	@FormParam("deleted")
    private String deleted;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ROLLING_PICTURE.PICTURE_ID
     *
     * @return the value of TOM_ROLLING_PICTURE.PICTURE_ID
     *
     * @mbggenerated
     */
	@FormParam("adminNames")
    private String adminNames;
	@FormParam("creatorId")
    private String creatorId;
	@FormParam("resourceName")
	private String resourceName;
	
	private String details;

	public Integer getPictureId() {
		return pictureId;
	}

	public void setPictureId(Integer pictureId) {
		this.pictureId = pictureId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getPictureCategory() {
		return pictureCategory;
	}

	public void setPictureCategory(String pictureCategory) {
		this.pictureCategory = pictureCategory;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public String getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(String isRelease) {
		this.isRelease = isRelease;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getAdminNames() {
		return adminNames;
	}

	public void setAdminNames(String adminNames) {
		this.adminNames = adminNames;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
}