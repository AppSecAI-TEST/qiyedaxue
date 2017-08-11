package com.chinamobo.ue.system.entity;

import javax.ws.rs.FormParam;

public class TomGrpOrgRelation {
	@FormParam("code")
    private String code;
	@FormParam("grpCode")
    private String grpCode;
	@FormParam("administrator")
    private String administrator;
	@FormParam("phone")
    private String phone;
	@FormParam("email")
    private String email;
	@FormParam("status")
    private String status;
	@FormParam("tomOrg")
    private TomOrg tomOrg;

    public TomOrg getTomOrg() {
		return tomOrg;
	}

	public void setTomOrg(TomOrg tomOrg) {
		this.tomOrg = tomOrg;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getGrpCode() {
        return grpCode;
    }

    public void setGrpCode(String grpCode) {
        this.grpCode = grpCode == null ? null : grpCode.trim();
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator == null ? null : administrator.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}