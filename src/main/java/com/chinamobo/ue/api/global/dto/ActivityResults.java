package com.chinamobo.ue.api.global.dto;

import com.chinamobo.ue.activity.common.DataEntity;

public class ActivityResults  extends DataEntity<ActivityResults>{
 private String activityId;
 private String applyStatus;
 private String introduction;
 private String activityName;
 private String activityStartTime;
 private String activityEndTime;
 private String city;
 private String applyType;
 private String actPicture;
public String getActivityId() {
	return activityId;
}
public void setActivityId(String activityId) {
	this.activityId = activityId;
}
public String getApplyStatus() {
	return applyStatus;
}
public void setApplyStatus(String applyStatus) {
	this.applyStatus = applyStatus;
}
public String getIntroduction() {
	return introduction;
}
public void setIntroduction(String introduction) {
	this.introduction = introduction;
}
public String getActivityName() {
	return activityName;
}
public void setActivityName(String activityName) {
	this.activityName = activityName;
}
public String getActivityStartTime() {
	return activityStartTime;
}
public void setActivityStartTime(String activityStartTime) {
	this.activityStartTime = activityStartTime;
}
public String getActivityEndTime() {
	return activityEndTime;
}
public void setActivityEndTime(String activityEndTime) {
	this.activityEndTime = activityEndTime;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getApplyType() {
	return applyType;
}
public void setApplyType(String applyType) {
	this.applyType = applyType;
}
public String getActPicture() {
	return actPicture;
}
public void setActPicture(String actPicture) {
	this.actPicture = actPicture;
}
 
 
}
