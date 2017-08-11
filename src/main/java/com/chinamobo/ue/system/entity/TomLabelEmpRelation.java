package com.chinamobo.ue.system.entity;

import java.util.Date;

public class TomLabelEmpRelation {
    private Integer id;

    private String tagId;

    private String tagName;

    private String code;

    private String name;

    private String cityname;

    private String secretEmail;

    private String mobile;

    private String orgcode;

    private String orgname;

    private String deptcode;

    private String deptname;

    private String onedeptcode;

    private String onedeptname;

    private String sex;

    private String poststat;

    private Date createTime;

    private Date updateTime;

    private String type;
    
    private Integer classId;

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId == null ? null : tagId.trim();
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
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

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname == null ? null : cityname.trim();
    }

    public String getSecretEmail() {
        return secretEmail;
    }

    public void setSecretEmail(String secretEmail) {
        this.secretEmail = secretEmail == null ? null : secretEmail.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode == null ? null : orgcode.trim();
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname == null ? null : orgname.trim();
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode == null ? null : deptcode.trim();
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname == null ? null : deptname.trim();
    }

    public String getOnedeptcode() {
        return onedeptcode;
    }

    public void setOnedeptcode(String onedeptcode) {
        this.onedeptcode = onedeptcode == null ? null : onedeptcode.trim();
    }

    public String getOnedeptname() {
        return onedeptname;
    }

    public void setOnedeptname(String onedeptname) {
        this.onedeptname = onedeptname == null ? null : onedeptname.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getPoststat() {
        return poststat;
    }

    public void setPoststat(String poststat) {
        this.poststat = poststat == null ? null : poststat.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
    
    public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}
}