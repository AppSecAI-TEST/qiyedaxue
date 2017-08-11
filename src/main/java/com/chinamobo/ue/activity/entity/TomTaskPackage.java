package com.chinamobo.ue.activity.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.activity.common.DataEntity;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Global;
import com.chinamobo.ue.utils.MapManager;

public class TomTaskPackage extends DataEntity<TomTaskPackage>{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.PACKAGE_ID
     *
     * @mbggenerated
     */
	
	private String adminNames;

	private String creatorId;
	
	public String getAdminNames() {
		return adminNames;
	}

	public void setAdminNames(String adminNames) {
		this.adminNames = adminNames;
	}

	public NumberRecordService getNumberRecordService() {
		return numberRecordService;
	}

	public void setNumberRecordService(NumberRecordService numberRecordService) {
		this.numberRecordService = numberRecordService;
	}

	private String activityCount;
	
	public String getActivityCount() {
		return activityCount;
	}

	public void setActivityCount(String activityCount) {
		this.activityCount = activityCount;
	}

	@FormParam("packageId[]")
    private Integer packageId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.PACKAGE_NUMBER
     *
     * @mbggenerated
     */
	@FormParam("packageNumber[]")
    private String packageNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.PACKAGE_NAME
     *
     * @mbggenerated
     */
	@FormParam("packageName[]")
	private String packageName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.ADMINS
     *
     * @mbggenerated
     */
	@FormParam("admins[]")
	private String admins;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.STATUS
     *
     * @mbggenerated
     */
	@FormParam("status[]")
	private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.CREATOR
     *
     * @mbggenerated
     */
	@FormParam("creator[]")
	private String creator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.CREATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("createTime[]")
	private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.OPERATOR
     *
     * @mbggenerated
     */
	@FormParam("operator[]")
	private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TASK_PACKAGE.UPDATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("updateTime[]")
	private Date updateTime;
	
	@Autowired
	private NumberRecordService numberRecordService;
	
	@FormParam("taskCount[]")
    private Integer taskCount;
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
		//this.packageNumber = numberRecordService.getNumber(MapManager.numberType(Global.RWB));TODO
	}
	
	/**
	 * 插入前调用
	 */
	public void preInsertsData(){
		this.status = Global.YES;
		//String user  = ShiroUtils.getCurrentUser().getName();
//		String user  = "4";
//		this.operator = user;
//		this.creator = user;
//		this.updateTime = new Date();
//		this.createTime = new Date();
	}
	
	
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.PACKAGE_ID
     *
     * @return the value of TOM_TASK_PACKAGE.PACKAGE_ID
     *
     * @mbggenerated
     */
    public Integer getPackageId() {
        return packageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.PACKAGE_ID
     *
     * @param packageId the value for TOM_TASK_PACKAGE.PACKAGE_ID
     *
     * @mbggenerated
     */
    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.PACKAGE_NUMBER
     *
     * @return the value of TOM_TASK_PACKAGE.PACKAGE_NUMBER
     *
     * @mbggenerated
     */
    public String getPackageNumber() {
        return packageNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.PACKAGE_NUMBER
     *
     * @param packageNumber the value for TOM_TASK_PACKAGE.PACKAGE_NUMBER
     *
     * @mbggenerated
     */
    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber == null ? null : packageNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.PACKAGE_NAME
     *
     * @return the value of TOM_TASK_PACKAGE.PACKAGE_NAME
     *
     * @mbggenerated
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.PACKAGE_NAME
     *
     * @param packageName the value for TOM_TASK_PACKAGE.PACKAGE_NAME
     *
     * @mbggenerated
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.ADMINS
     *
     * @return the value of TOM_TASK_PACKAGE.ADMINS
     *
     * @mbggenerated
     */
    public String getAdmins() {
        return admins;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.ADMINS
     *
     * @param admins the value for TOM_TASK_PACKAGE.ADMINS
     *
     * @mbggenerated
     */
    public void setAdmins(String admins) {
        this.admins = admins == null ? null : admins.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.STATUS
     *
     * @return the value of TOM_TASK_PACKAGE.STATUS
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.STATUS
     *
     * @param status the value for TOM_TASK_PACKAGE.STATUS
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.CREATOR
     *
     * @return the value of TOM_TASK_PACKAGE.CREATOR
     *
     * @mbggenerated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.CREATOR
     *
     * @param creator the value for TOM_TASK_PACKAGE.CREATOR
     *
     * @mbggenerated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.CREATE_TIME
     *
     * @return the value of TOM_TASK_PACKAGE.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.CREATE_TIME
     *
     * @param createTime the value for TOM_TASK_PACKAGE.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.OPERATOR
     *
     * @return the value of TOM_TASK_PACKAGE.OPERATOR
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.OPERATOR
     *
     * @param operator the value for TOM_TASK_PACKAGE.OPERATOR
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TASK_PACKAGE.UPDATE_TIME
     *
     * @return the value of TOM_TASK_PACKAGE.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TASK_PACKAGE.UPDATE_TIME
     *
     * @param updateTime the value for TOM_TASK_PACKAGE.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public Integer getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(Integer taskCount) {
		this.taskCount = taskCount;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
    
}