package com.chinamobo.ue.activity.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.utils.Global;

public class TomTaskExamPaperRelation extends TomTaskExamPaperRelationKey{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_EXAM_RELATION.STATUS
     *
     * @mbggenerated
     */
	@FormParam("status")
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_EXAM_RELATION.CREATOR
     *
     * @mbggenerated
     */
	@FormParam("creator")
    private String creator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_EXAM_RELATION.CREATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("createTime")
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_EXAM_RELATION.OPERATOR
     *
     * @mbggenerated
     */
	@FormParam("operator")
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_EXAM_RELATION.UPDATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("updateTime")
    private Date updateTime;
	
	
	/**
	 * 插入前调用
	 */
	public void preInsertData(){
	
		//String user  = ShiroUtils.getCurrentUser().getName();
//		String user  = "4";
//		this.operator = user;
//		this.creator = user;
//		this.updateTime = new Date();
//		this.createTime = new Date();
		this.status = Global.YES;
	}
	
	/**
	 * 插入后调用
	 */
	public void preUpdateData(){
		//String user  = ShiroUtils.getCurrentUser().getName();
//		String user  = "4";
//		this.operator = user;
//		this.updateTime = new Date();
	}
	

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_EXAM_RELATION.STATUS
     *
     * @return the value of TOM_TASK_EXAM_RELATION.STATUS
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_EXAM_RELATION.STATUS
     *
     * @param status the value for TOM_TASK_EXAM_RELATION.STATUS
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_EXAM_RELATION.CREATOR
     *
     * @return the value of TOM_TASK_EXAM_RELATION.CREATOR
     *
     * @mbggenerated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_EXAM_RELATION.CREATOR
     *
     * @param creator the value for TOM_TASK_EXAM_RELATION.CREATOR
     *
     * @mbggenerated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_EXAM_RELATION.CREATE_TIME
     *
     * @return the value of TOM_TASK_EXAM_RELATION.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_EXAM_RELATION.CREATE_TIME
     *
     * @param createTime the value for TOM_TASK_EXAM_RELATION.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_EXAM_RELATION.OPERATOR
     *
     * @return the value of TOM_TASK_EXAM_RELATION.OPERATOR
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_EXAM_RELATION.OPERATOR
     *
     * @param operator the value for TOM_TASK_EXAM_RELATION.OPERATOR
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_EXAM_RELATION.UPDATE_TIME
     *
     * @return the value of TOM_TASK_EXAM_RELATION.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_EXAM_RELATION.UPDATE_TIME
     *
     * @param updateTime the value for TOM_TASK_EXAM_RELATION.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}