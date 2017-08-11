package com.chinamobo.ue.system.entity;

import java.util.Date;

public class TomRoleScopes {
	private Integer roleId;

	private String roleType;

	private String roleScope;

	private String creator;

	private Date createTime;

	private String operator;

	private Date updateTime;
	
	 private String creatorId;

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType == null ? null : roleType.trim();
	}

	public String getRoleScope() {
		return roleScope;
	}

	public void setRoleScope(String roleScope) {
		this.roleScope = roleScope == null ? null : roleScope.trim();
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
}