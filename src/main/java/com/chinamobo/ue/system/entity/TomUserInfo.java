package com.chinamobo.ue.system.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomUserInfo {
	@FormParam("code")
    private String code;
	@FormParam("headImg")
    private String headImg;
	@FormParam("continuousSignInNumber")
    private Integer continuousSignInNumber;
	@FormParam("lastSignInTime")
    private Date lastSignInTime;
	@FormParam("courseNumber")
    private String courseNumber;
	@FormParam("learningTime")
    private String learningTime;
	@FormParam("eNumber")
    private Integer eNumber;
	@FormParam("sumSignInTimes")
    private Integer sumSignInTimes;
	@FormParam("address")
    private String address;
	@FormParam("addUpENumber")
    private Integer addUpENumber;
	@FormParam("anonymity")
    private String anonymity;	
	@FormParam("phoneNumber")
	private String phoneNumber;
	@FormParam("sex")
	private String sex;
	@FormParam("name")
	private String name;
	@FormParam("status")
	private String status;
	@FormParam("deptName")
	private String deptName;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private TomUserLog tomUserLog;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TomUserLog getTomUserLog() {
		return tomUserLog;
	}

	public void setTomUserLog(TomUserLog tomUserLog) {
		this.tomUserLog = tomUserLog;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public Integer getContinuousSignInNumber() {
        return continuousSignInNumber;
    }

    public void setContinuousSignInNumber(Integer continuousSignInNumber) {
        this.continuousSignInNumber = continuousSignInNumber;
    }

    public Date getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(Date lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber == null ? null : courseNumber.trim();
    }

    public String getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(String learningTime) {
        this.learningTime = learningTime == null ? null : learningTime.trim();
    }

    public Integer geteNumber() {
        return eNumber;
    }

    public void seteNumber(Integer eNumber) {
        this.eNumber = eNumber;
    }

    public Integer getSumSignInTimes() {
        return sumSignInTimes;
    }

    public void setSumSignInTimes(Integer sumSignInTimes) {
        this.sumSignInTimes = sumSignInTimes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getAddUpENumber() {
        return addUpENumber;
    }

    public void setAddUpENumber(Integer addUpENumber) {
        this.addUpENumber = addUpENumber;
    }

	public String getAnonymity() {
		return anonymity;
	}

	public void setAnonymity(String anonymity) {
		this.anonymity = anonymity;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

    
}