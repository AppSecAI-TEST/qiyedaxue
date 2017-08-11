package com.chinamobo.ue.system.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomLabelClass {
	@FormParam("classId")
    private Integer classId;
	@FormParam("className")
    private String className;
    private String creatorId;
    private String creator;
    private Date createTime;
    private String operator;
    private Date updateTime;
    private String admins;
    private String adminNames;
	@FormParam("tagIds")
	private String tagIds;

	public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
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
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAdmins() {
        return admins;
    }

    public void setAdmins(String admins) {
        this.admins = admins == null ? null : admins.trim();
    }

    public String getAdminNames() {
        return adminNames;
    }

    public void setAdminNames(String adminNames) {
        this.adminNames = adminNames == null ? null : adminNames.trim();
    }
    
    public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}
	
}