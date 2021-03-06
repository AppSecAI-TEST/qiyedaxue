package com.chinamobo.ue.course.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.common.tree.Node;

public class TomCourseClassify extends Node<TomCourseClassify>{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.CLASSIFY_ID
     *
     * @mbggenerated
     */
	@FormParam("classifyId")
    private Integer classifyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.CLASSIFY_NUMBER
     *
     * @mbggenerated
     */
	@FormParam("classifyNumber")
    private String classifyNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.CLASSIFY_NAME
     *
     * @mbggenerated
     */
	@FormParam("classifyName")
    private String classifyName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.PARENT_CLASSIFY_ID
     *
     * @mbggenerated
     */
	@FormParam("parentClassifyId")
    private Integer parentClassifyId;
    
	@FormParam("parentClassifyName")
    private String parentClassifyName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.CLASSIFY_INTRODUCTION
     *
     * @mbggenerated
     */
	@FormParam("classifyIntroduction")
    private String classifyIntroduction;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.STATUS
     *
     * @mbggenerated
     */
	@FormParam("status")
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.ADMINS
     *
     * @mbggenerated
     */
	@FormParam("admins")
    private String admins;

	@FormParam("adminNames")
    private String adminNames;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.CREATOR
     *
     * @mbggenerated
     */
	@FormParam("creator")
    private String creator;
	
	private String creatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.OPERATOR
     *
     * @mbggenerated
     */
	@FormParam("operator")
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSE_CLASSIFY.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;

    private int courseCounts;
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.CLASSIFY_ID
     *
     * @return the value of TOM_COURSE_CLASSIFY.CLASSIFY_ID
     *
     * @mbggenerated
     */
    public Integer getClassifyId() {
        return classifyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.CLASSIFY_ID
     *
     * @param classifyId the value for TOM_COURSE_CLASSIFY.CLASSIFY_ID
     *
     * @mbggenerated
     */
    public void setClassifyId(Integer classifyId) {
        this.classifyId = classifyId;
        this.id = classifyId.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.CLASSIFY_NUMBER
     *
     * @return the value of TOM_COURSE_CLASSIFY.CLASSIFY_NUMBER
     *
     * @mbggenerated
     */
    public String getClassifyNumber() {
        return classifyNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.CLASSIFY_NUMBER
     *
     * @param classifyNumber the value for TOM_COURSE_CLASSIFY.CLASSIFY_NUMBER
     *
     * @mbggenerated
     */
    public void setClassifyNumber(String classifyNumber) {
        this.classifyNumber = classifyNumber == null ? null : classifyNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.CLASSIFY_NAME
     *
     * @return the value of TOM_COURSE_CLASSIFY.CLASSIFY_NAME
     *
     * @mbggenerated
     */
    public String getClassifyName() {
        return classifyName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.CLASSIFY_NAME
     *
     * @param classifyName the value for TOM_COURSE_CLASSIFY.CLASSIFY_NAME
     *
     * @mbggenerated
     */
    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName == null ? null : classifyName.trim();
        this.name = classifyName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.PARENT_CLASSIFY_ID
     *
     * @return the value of TOM_COURSE_CLASSIFY.PARENT_CLASSIFY_ID
     *
     * @mbggenerated
     */
    public Integer getParentClassifyId() {
        return parentClassifyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.PARENT_CLASSIFY_ID
     *
     * @param parentClassifyId the value for TOM_COURSE_CLASSIFY.PARENT_CLASSIFY_ID
     *
     * @mbggenerated
     */
    public void setParentClassifyId(Integer parentClassifyId) {
        this.parentClassifyId = parentClassifyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.CLASSIFY_INTRODUCTION
     *
     * @return the value of TOM_COURSE_CLASSIFY.CLASSIFY_INTRODUCTION
     *
     * @mbggenerated
     */
    public String getClassifyIntroduction() {
        return classifyIntroduction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.CLASSIFY_INTRODUCTION
     *
     * @param classifyIntroduction the value for TOM_COURSE_CLASSIFY.CLASSIFY_INTRODUCTION
     *
     * @mbggenerated
     */
    public void setClassifyIntroduction(String classifyIntroduction) {
        this.classifyIntroduction = classifyIntroduction == null ? null : classifyIntroduction.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.STATUS
     *
     * @return the value of TOM_COURSE_CLASSIFY.STATUS
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.STATUS
     *
     * @param status the value for TOM_COURSE_CLASSIFY.STATUS
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.ADMINS
     *
     * @return the value of TOM_COURSE_CLASSIFY.ADMINS
     *
     * @mbggenerated
     */
    public String getAdmins() {
        return admins;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.ADMINS
     *
     * @param admins the value for TOM_COURSE_CLASSIFY.ADMINS
     *
     * @mbggenerated
     */
    public void setAdmins(String admins) {
        this.admins = admins == null ? null : admins.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.CREATOR
     *
     * @return the value of TOM_COURSE_CLASSIFY.CREATOR
     *
     * @mbggenerated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.CREATOR
     *
     * @param creator the value for TOM_COURSE_CLASSIFY.CREATOR
     *
     * @mbggenerated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.CREATE_TIME
     *
     * @return the value of TOM_COURSE_CLASSIFY.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.CREATE_TIME
     *
     * @param createTime the value for TOM_COURSE_CLASSIFY.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.OPERATOR
     *
     * @return the value of TOM_COURSE_CLASSIFY.OPERATOR
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.OPERATOR
     *
     * @param operator the value for TOM_COURSE_CLASSIFY.OPERATOR
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSE_CLASSIFY.UPDATE_TIME
     *
     * @return the value of TOM_COURSE_CLASSIFY.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSE_CLASSIFY.UPDATE_TIME
     *
     * @param updateTime the value for TOM_COURSE_CLASSIFY.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    
	public String getParentClassifyName() {
		return parentClassifyName;
	}

	public void setParentClassifyName(String parentClassifyName) {
		this.parentClassifyName = parentClassifyName;
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

	

	public int getCourseCounts() {
		return courseCounts;
	}

	public void setCourseCounts(int courseCounts) {
		this.courseCounts = courseCounts;
	}
	@Override
	public String toString() {
		return "TomCourseClassify [classifyId=" + classifyId
				+ ", classifyNumber=" + classifyNumber + ", classifyName="
				+ classifyName + ", parentClassifyId=" + parentClassifyId
				+ ", classifyIntroduction=" + classifyIntroduction
				+ ", status=" + status + ", admins=" + admins + ", creator="
				+ creator + ", createTime=" + createTime + ", operator="
				+ operator + ", updateTime=" + updateTime + "]";
	}
    
}