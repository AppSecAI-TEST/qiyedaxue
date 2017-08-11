package com.chinamobo.ue.system.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomOrgGroups {
	@FormParam("code")
    private String code;
	@FormParam("name")
    private String name;
	@FormParam("status")
    private String status;
	@FormParam("creator")
    private String creator;
	@FormParam("createTime")
    private Date createTime;
	@FormParam("operator")
    private String operator;
	@FormParam("updateTime")
    private Date updateTime;
    
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