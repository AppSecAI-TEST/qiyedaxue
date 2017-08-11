package com.chinamobo.ue.api.activity.dto;

import java.util.Date;
import java.util.List;

/**
 * 版本: [1.0]
 * 功能说明: 构造API活动管理所有result
 *
 * 作者: WangLg
 * 创建时间: 2016年3月22日 下午3:53:25
 */
public class ActResults {
	private List<TaskData> taskData;//自定义
	
	private String activityStatus;//自定义
	
	private Date applicationStartTime;//报名开始日期
	private Date applicationDeadline;//报名截至日期
	private String applicationStartTimeS;//报名开始日期
	private String applicationDeadlineS;
	
	private Integer preTaskId;

	private String actPicture;
	private Integer activityId;
	private String needApply;
	private String allow;

	private String examType;
	private String city;
	private String applyType;
	private Integer numberOfParticipants;
	private String applyStatus;
	private String underwaySort ;
	private String introduction;
	
	private String activityName;
	
	private Date activityStartTime;//活动开始时间
	
	private Date activityEndTime;
	
	private String activityStartTimeS;//活动开始时间
	
	private String activityEndTimeS;
	
	private String activityAddress;
	
	private String userId;
	
	private String userName;
	
	private Integer certificateId;
	
	private String certificateAddress;//证书图片地址
	
	private String certificateState;//证书状态
	
	private String receiveState;//领取方式
	
	private String receiveAddress;//领取地址
	
	private String certificateValidState;//证书的有效状态
	
	public ActResults() {
		super();
	}

	public ActResults(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public String getUnderwaySort() {
		return underwaySort;
	}

	public void setUnderwaySort(String underwaySort) {
		this.underwaySort = underwaySort;
	}

	//
//	public ActResults(String applyStatus, String courseExamImg,String introduction, String activityName,
//			String activityStartTime, String activityEndTime, String activityAddress, String userId, String userName) {
//		super();
//		this.applyStatus = applyStatus;
//		this.courseExamImg = courseExamImg;
//		this.introduction = introduction;
//		this.activityName = activityName;
//		this.activityStartTime = activityStartTime;
//		this.activityEndTime = activityEndTime;
//		this.activityAddress = activityAddress;
//		this.userId = userId;
//		this.userName = userName;
//	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getActivityAddress() {
		return activityAddress;
	}

	public void setActivityAddress(String activityAddress) {
		this.activityAddress = activityAddress;
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

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public String getActPicture() {
		return actPicture;
	}

	public void setActPicture(String actPicture) {
		this.actPicture = actPicture;
	}

	public String getNeedApply() {
		return needApply;
	}

	public void setNeedApply(String needApply) {
		this.needApply = needApply;
	}

	private String admins;
	
	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	private String code;
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private String taskProgress;
	
	public String getTaskProgress() {
		return taskProgress;
	}

	public void setTaskProgress(String taskProgress) {
		this.taskProgress = taskProgress;
	}

	public Date getApplicationStartTime() {
		return applicationStartTime;

	}
	
	public void setApplicationStartTime(Date applicationStartTime) {
		this.applicationStartTime = applicationStartTime;

	}

	public Date getApplicationDeadline() {
		return applicationDeadline;

	}

	public void setApplicationDeadline(Date applicationDeadline) {
		this.applicationDeadline = applicationDeadline;
	}

	public Date getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public List<TaskData> getTaskData() {
		return taskData;
	}

	public void setTaskData(List<TaskData> taskData) {
		this.taskData = taskData;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public Integer getPreTaskId() {
		return preTaskId;
	}

	public void setPreTaskId(Integer preTaskId) {
		this.preTaskId = preTaskId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	private String gradeState;

	public String getGradeState() {
		return gradeState;
	}

	public void setGradeState(String gradeState) {
		this.gradeState = gradeState;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getApplicationStartTimeS() {
		return applicationStartTimeS;
	}

	public void setApplicationStartTimeS(String applicationStartTimeS) {
		this.applicationStartTimeS = applicationStartTimeS;
	}

	public String getApplicationDeadlineS() {
		return applicationDeadlineS;
	}

	public void setApplicationDeadlineS(String applicationDeadlineS) {
		this.applicationDeadlineS = applicationDeadlineS;
	}

	public String getActivityStartTimeS() {
		return activityStartTimeS;
	}

	public void setActivityStartTimeS(String activityStartTimeS) {
		this.activityStartTimeS = activityStartTimeS;
	}

	public String getActivityEndTimeS() {
		return activityEndTimeS;
	}

	public void setActivityEndTimeS(String activityEndTimeS) {
		this.activityEndTimeS = activityEndTimeS;
	}

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public Integer getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public void setNumberOfParticipants(Integer numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}
	
	public Integer getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Integer certificateId) {
		this.certificateId = certificateId;
	}

	public String getCertificateState() {
		return certificateState;
	}

	public void setCertificateState(String certificateState) {
		this.certificateState = certificateState;
	}

	public String getReceiveState() {
		return receiveState;
	}

	public void setReceiveState(String receiveState) {
		this.receiveState = receiveState;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getCertificateAddress() {
		return certificateAddress;
	}

	public void setCertificateAddress(String certificateAddress) {
		this.certificateAddress = certificateAddress;
	}

	public String getCertificateValidState() {
		return certificateValidState;
	}

	public void setCertificateValidState(String certificateValidState) {
		this.certificateValidState = certificateValidState;
	}
	
	

	
}
