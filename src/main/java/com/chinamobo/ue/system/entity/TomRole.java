package com.chinamobo.ue.system.entity;

import java.util.Date;
import java.util.List;

public class TomRole{


	private Integer roleId;

	private String roleName;

	private String roleType;

	private String status;

	private String creator;

	private Date createTime;

	private String operator;

	private Date updateTime;

	private TomRoleScopes tomRoleScopes;

	private List<TomRoleAuthorities> tomRoleAuthorities;
	
	private List<TomAdminRoles> adminroles;
	
	private String creatorId;

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public List<TomAdminRoles> getAdminroles() {
		return adminroles;
	}

	public void setAdminroles(List<TomAdminRoles> adminroles) {
		this.adminroles = adminroles;
	}

	public TomRoleScopes getTomRoleScopes() {
		return tomRoleScopes;
	}

	public void setTomRoleScopes(TomRoleScopes tomRoleScopes) {
		this.tomRoleScopes = tomRoleScopes;
	}

	public List<TomRoleAuthorities> getTomRoleAuthorities() {
		return tomRoleAuthorities;
	}

	public void setTomRoleAuthorities(List<TomRoleAuthorities> tomRoleAuthorities) {
		this.tomRoleAuthorities = tomRoleAuthorities;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName == null ? null : roleName.trim();
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType == null ? null : roleType.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
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