package com.chinamobo.ue.system.entity;

import java.util.Date;

public class TomAppVersions {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.V_ID
     *
     * @mbggenerated
     */
    private Integer vId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.PLATFORM
     *
     * @mbggenerated
     */
    private String platform;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.VERSION_NUMBER
     *
     * @mbggenerated
     */
    private String versionNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.IS_NOW_VERSION
     *
     * @mbggenerated
     */
    private String isNowVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.DOWNLOAD_URL
     *
     * @mbggenerated
     */
    private String downloadUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.FORCE_UPDATE
     *
     * @mbggenerated
     */
    private String forceUpdate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.VERSION_DESC
     *
     * @mbggenerated
     */
    private String versionDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.CREATER
     *
     * @mbggenerated
     */
    private String creater;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.OPERATOR
     *
     * @mbggenerated
     */
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_APP_VERSIONS.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.V_ID
     *
     * @return the value of TOM_APP_VERSIONS.V_ID
     *
     * @mbggenerated
     */
    public Integer getvId() {
        return vId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.V_ID
     *
     * @param vId the value for TOM_APP_VERSIONS.V_ID
     *
     * @mbggenerated
     */
    public void setvId(Integer vId) {
        this.vId = vId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.PLATFORM
     *
     * @return the value of TOM_APP_VERSIONS.PLATFORM
     *
     * @mbggenerated
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.PLATFORM
     *
     * @param platform the value for TOM_APP_VERSIONS.PLATFORM
     *
     * @mbggenerated
     */
    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.VERSION_NUMBER
     *
     * @return the value of TOM_APP_VERSIONS.VERSION_NUMBER
     *
     * @mbggenerated
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.VERSION_NUMBER
     *
     * @param versionNumber the value for TOM_APP_VERSIONS.VERSION_NUMBER
     *
     * @mbggenerated
     */
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber == null ? null : versionNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.IS_NOW_VERSION
     *
     * @return the value of TOM_APP_VERSIONS.IS_NOW_VERSION
     *
     * @mbggenerated
     */
    public String getIsNowVersion() {
        return isNowVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.IS_NOW_VERSION
     *
     * @param isNowVersion the value for TOM_APP_VERSIONS.IS_NOW_VERSION
     *
     * @mbggenerated
     */
    public void setIsNowVersion(String isNowVersion) {
        this.isNowVersion = isNowVersion == null ? null : isNowVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.DOWNLOAD_URL
     *
     * @return the value of TOM_APP_VERSIONS.DOWNLOAD_URL
     *
     * @mbggenerated
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.DOWNLOAD_URL
     *
     * @param downloadUrl the value for TOM_APP_VERSIONS.DOWNLOAD_URL
     *
     * @mbggenerated
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl == null ? null : downloadUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.FORCE_UPDATE
     *
     * @return the value of TOM_APP_VERSIONS.FORCE_UPDATE
     *
     * @mbggenerated
     */
    public String getForceUpdate() {
        return forceUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.FORCE_UPDATE
     *
     * @param forceUpdate the value for TOM_APP_VERSIONS.FORCE_UPDATE
     *
     * @mbggenerated
     */
    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate == null ? null : forceUpdate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.VERSION_DESC
     *
     * @return the value of TOM_APP_VERSIONS.VERSION_DESC
     *
     * @mbggenerated
     */
    public String getVersionDesc() {
        return versionDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.VERSION_DESC
     *
     * @param versionDesc the value for TOM_APP_VERSIONS.VERSION_DESC
     *
     * @mbggenerated
     */
    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc == null ? null : versionDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.CREATER
     *
     * @return the value of TOM_APP_VERSIONS.CREATER
     *
     * @mbggenerated
     */
    public String getCreater() {
        return creater;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.CREATER
     *
     * @param creater the value for TOM_APP_VERSIONS.CREATER
     *
     * @mbggenerated
     */
    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.CREATE_TIME
     *
     * @return the value of TOM_APP_VERSIONS.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.CREATE_TIME
     *
     * @param createTime the value for TOM_APP_VERSIONS.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.OPERATOR
     *
     * @return the value of TOM_APP_VERSIONS.OPERATOR
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.OPERATOR
     *
     * @param operator the value for TOM_APP_VERSIONS.OPERATOR
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_APP_VERSIONS.UPDATE_TIME
     *
     * @return the value of TOM_APP_VERSIONS.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_APP_VERSIONS.UPDATE_TIME
     *
     * @param updateTime the value for TOM_APP_VERSIONS.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}