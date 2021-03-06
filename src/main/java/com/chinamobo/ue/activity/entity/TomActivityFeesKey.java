package com.chinamobo.ue.activity.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.activity.common.DataEntity;

public class TomActivityFeesKey extends DataEntity<TomActivityFeesKey>{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.ACTIVITY_ID
     *
     * @mbggenerated
     */
	@FormParam("activityId")
    private Integer activityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.FEE_ID
     *
     * @mbggenerated
     */
	@FormParam("feeId")
    private Integer feeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.CREATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("createTime")
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.ACTIVITY_ID
     *
     * @return the value of TOM_ACTIVITY_FEES.ACTIVITY_ID
     *
     * @mbggenerated
     */
    public Integer getActivityId() {
        return activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.ACTIVITY_ID
     *
     * @param activityId the value for TOM_ACTIVITY_FEES.ACTIVITY_ID
     *
     * @mbggenerated
     */
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.FEE_ID
     *
     * @return the value of TOM_ACTIVITY_FEES.FEE_ID
     *
     * @mbggenerated
     */
    public Integer getFeeId() {
        return feeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.FEE_ID
     *
     * @param feeId the value for TOM_ACTIVITY_FEES.FEE_ID
     *
     * @mbggenerated
     */
    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.CREATE_TIME
     *
     * @return the value of TOM_ACTIVITY_FEES.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.CREATE_TIME
     *
     * @param createTime the value for TOM_ACTIVITY_FEES.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}