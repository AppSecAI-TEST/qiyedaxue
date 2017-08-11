package com.chinamobo.ue.system.dto;

import com.chinamobo.ue.system.entity.TomUserLog;

public class AdminDto extends TomUserLog{

	private String adminStatus;

	public String getAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(String adminStatus) {
		this.adminStatus = adminStatus;
	}
}
