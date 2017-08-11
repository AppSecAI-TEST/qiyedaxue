package com.chinamobo.ue.ums.shiro;

import java.io.Serializable;
import java.util.List;

import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.system.entity.TomUserLog;

public class ShiroUser implements Serializable{

	private Integer id;
	private String code;
	private String loginName;
	private String name;
	private TomUserLog user;
	private List<TomRole> roles;
	private List<ShiroPermission> ShiroPermissions;
	public Integer getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public String getLoginName() {
		return loginName;
	}
	public String getName() {
		return name;
	}
	public List<TomRole> getRoles() {
		return roles;
	}
	public List<ShiroPermission> getShiroPermissions() {
		return ShiroPermissions;
	}
	public ShiroUser(Integer id, String code, String loginName, String name, List<TomRole> roles,List<ShiroPermission> shiroPermissions , TomUserLog user) {
		super();
		this.id = id;
		this.code = code;
		this.loginName = loginName;
		this.name = name;
		this.roles = roles;
		this.user = user;
		ShiroPermissions = shiroPermissions;
	}
	public TomUserLog getUser() {
		return user;
	}
	public void setUser(TomUserLog user) {
		this.user = user;
	}
	
}
