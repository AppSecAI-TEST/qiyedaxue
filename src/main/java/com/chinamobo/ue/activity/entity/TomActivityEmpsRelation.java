package com.chinamobo.ue.activity.entity;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.utils.Global;

public class TomActivityEmpsRelation extends TomActivityEmpsRelationKey{
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column TOM_ACTIVITY_EMPS_RELATION.STATUS
	 *    private String creator;
    
    private Date createTime;

    private String operator;

    private Date updateTime;
	 * @mbggenerated
	 */
	@FormParam("status")
	private String status;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column TOM_ACTIVITY_EMPS_RELATION.APPLY_STATUS
	 *
	 * @mbggenerated
	 */
	@FormParam("applyStatus")
	private String applyStatus;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column TOM_ACTIVITY_EMPS_RELATION.APPLY_TYPE
	 *
	 * @mbggenerated
	 */
	@FormParam("applyType")
	private String applyType;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column TOM_ACTIVITY_EMPS_RELATION.IS_REQUIRED
	 *
	 * @mbggenerated
	 */
	@FormParam("isRequired")
	private String isRequired;

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column TOM_ACTIVITY_EMPS_RELATION.STATUS
	 *
	 * @return the value of TOM_ACTIVITY_EMPS_RELATION.STATUS
	 *
	 * @mbggenerated
	 */
	@FormParam("tomEmp")
	private TomEmp tomEmp;

	@FormParam("tomActivity")
	private TomActivity tomActivity;
	
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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
		this.status = Global.GL;//Y 正常  N 取消关联
		this.applyStatus = Global.YBM;
		this.applyType = "E";//员工自行报名
//		if("D".equals(applyType)){
//			this.applyType = "D";//负责人报名
//		}else{
//			this.applyType = "E";//员工自行报名
//		}
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
	/*
	 * 取消关联
	 */
	public void preUpdateStatusData(){
		this.applyStatus = Global.WBM;
		this.status = Global.QXGL;
	}
	

	public TomEmp getTomEmp() {
		return tomEmp;
	}

	public void setTomEmp(TomEmp tomEmp) {
		this.tomEmp = tomEmp;
	}

	public TomActivity getTomActivity() {
		return tomActivity;
	}

	public void setTomActivity(TomActivity tomActivity) {
		this.tomActivity = tomActivity;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column TOM_ACTIVITY_EMPS_RELATION.STATUS
	 *
	 * @param status
	 *            the value for TOM_ACTIVITY_EMPS_RELATION.STATUS
	 *
	 * @mbggenerated
	 */
	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column TOM_ACTIVITY_EMPS_RELATION.APPLY_STATUS
	 *
	 * @return the value of TOM_ACTIVITY_EMPS_RELATION.APPLY_STATUS
	 *
	 * @mbggenerated
	 */
	public String getApplyStatus() {
		return applyStatus;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column TOM_ACTIVITY_EMPS_RELATION.APPLY_STATUS
	 *
	 * @param applyStatus
	 *            the value for TOM_ACTIVITY_EMPS_RELATION.APPLY_STATUS
	 *
	 * @mbggenerated
	 */
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus == null ? null : applyStatus.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column TOM_ACTIVITY_EMPS_RELATION.APPLY_TYPE
	 *
	 * @return the value of TOM_ACTIVITY_EMPS_RELATION.APPLY_TYPE
	 *
	 * @mbggenerated
	 */
	public String getApplyType() {
		return applyType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column TOM_ACTIVITY_EMPS_RELATION.APPLY_TYPE
	 *
	 * @param applyType
	 *            the value for TOM_ACTIVITY_EMPS_RELATION.APPLY_TYPE
	 *
	 * @mbggenerated
	 */
	public void setApplyType(String applyType) {
		this.applyType = applyType == null ? null : applyType.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column TOM_ACTIVITY_EMPS_RELATION.IS_REQUIRED
	 *
	 * @return the value of TOM_ACTIVITY_EMPS_RELATION.IS_REQUIRED
	 *
	 * @mbggenerated
	 */
	public String getIsRequired() {
		return isRequired;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column TOM_ACTIVITY_EMPS_RELATION.IS_REQUIRED
	 *
	 * @param isRequired
	 *            the value for TOM_ACTIVITY_EMPS_RELATION.IS_REQUIRED
	 *
	 * @mbggenerated
	 */
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired == null ? null : isRequired.trim();
	}

}