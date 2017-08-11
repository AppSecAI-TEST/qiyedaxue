package com.chinamobo.ue.statistics.entity;

import java.util.Date;

public class TomActivityStatistics {
	
	private String activityNumber;
	
	private Integer activityId;
	
	private String activityName;
	
	private String needApply;
	
	private Date activityStartTime;
	
	private Date activityEndTime;
	private String openNumber;
	
	private String totalEnrollment;
	
	private String numberOfParticipants;
	
	private String registrationRate;
	
	private String admins;
	
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getNeedApply() {
		return needApply;
	}

	public void setNeedApply(String needApply) {
		this.needApply = needApply;
	}

	public Date getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public String getOpenNumber() {
		return openNumber;
	}

	public void setOpenNumber(String openNumber) {
		this.openNumber = openNumber;
	}

	public String getTotalEnrollment() {
		return totalEnrollment;
	}

	public void setTotalEnrollment(String totalEnrollment) {
		this.totalEnrollment = totalEnrollment;
	}

	public String getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public void setNumberOfParticipants(String numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	public String getRegistrationRate() {
		return registrationRate;
	}

	public void setRegistrationRate(String registrationRate) {
		this.registrationRate = registrationRate;
	}

	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getActivityNumber() {
		return activityNumber;
	}

	public void setActivityNumber(String activityNumber) {
		this.activityNumber = activityNumber;
	}

	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}
	
}
