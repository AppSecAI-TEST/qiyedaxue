package com.chinamobo.ue.system.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomUserLog {
	@FormParam("code")
    private String code;
	@FormParam("name")
    private String name;
	@FormParam("password")
    private String password;
	@FormParam("email")
    private String email;
	@FormParam("phone")
    private String phone;
	@FormParam("status")
    private String status;
	@FormParam("eNumber")
	private Integer eNumber;
	@FormParam("token")
	private String token;
	@FormParam("validity")
	private Date validity;
    public Integer geteNumber() {
		return eNumber;
	}

	public void seteNumber(Integer eNumber) {
		this.eNumber = eNumber;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	@Override
	public String toString() {
		return "TomUserLog [code=" + code + ", name=" + name + ", password=" + password + ", email=" + email
				+ ", phone=" + phone + ", status=" + status + ", eNumber=" + eNumber + ", token=" + token
				+ ", validity=" + validity + "]";
	}
    
}