package com.chinamobo.ue.activity.dto;

import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;


/**
 * 版本: [1.0]
 * 功能说明: 培训活动包
 *
 * 作者: Wanghb
 * 创建时间: 2016年3月16日 上午10:15:08
 */
public class TomActivityDto {
	
		@FormParam("city")
		private String city;
		@FormParam("packageId")
		private Integer packageId;
		@FormParam("introduction")
		private String introduction;
		@FormParam("activityName")
		private String activityName;
		@FormParam("preTaskInfo[]")
		private List<TomActivityPropertyDto> preTaskInfo;
		@FormParam("protocol")
		private String protocol;
		@FormParam("trainFee")
		private String trainFee;
		@FormParam("deptCodes")
	   	private String deptCodes;
		@FormParam("employeeGradeStr")
	   	private String employeeGradeStr;
		@FormParam("empIds[]")
	   	private List<String> empIds;
		@FormParam("empNames[]")
	   	private List<String> empNames;
		@FormParam("deptManagerIds[]")
	   	private List<String> deptManagerIds;
		@FormParam("deptManagerNames[]")
	   	private List<String> deptManagerNames;
		@FormParam("code[]")
	   	private List<String> code;
		@FormParam("admins")
	   	private String admins;
		@FormParam("activityImg")
		private String activityImg;
		@FormParam("needApply")
	   	private String needApply;
		@FormParam("numberOfParticipants")
	   	private String numberOfParticipants;
		@FormParam("applicationStartTime")
	   	private String applicationStartTime;
		@FormParam("applicationDeadline")
	   	private String applicationDeadline;
		@FormParam("activityStartTime")
	   	private String activityStartTime;
		@FormParam("activityEndTime")
	   	private String activityEndTime;
		@FormParam("protocolStartTime")
		private String protocolStartTime;
		@FormParam("protocolEndTime")
		private String protocolEndTime;
		@FormParam("isPrincipal")
		private String isPrincipal;
		@FormParam("deptManagers")
		private List<Map<String,String>> deptManagers;
		@FormParam("deptManagers")
		private List<Map<String,String>> emps;
		
		//证书相关属性
		@FormParam("certificateState")
		private String certificateState;//证书状态  Y：有  Y：无
		@FormParam("certificateId")
		private Integer certificateId;//证书ID
		@FormParam("certificateAddress")
		private String certificateAddress;//证书地址
		@FormParam("receiveState")
		private String receiveState;//领取状态  Y：线上领取  N：线下领取
		@FormParam("receiveAddress")
		private String receiveAddress;//领取地址
		@FormParam("certificateValidState")
		private String certificateValidState;//活动 证书有效状态 Y：有效  N：失效 默认：Y。 在证书管理页删除
		
		
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
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
		public List<TomActivityPropertyDto> getPreTaskInfo() {
			return preTaskInfo;
		}
		public void setPreTaskInfo(List<TomActivityPropertyDto> preTaskInfo) {
			this.preTaskInfo = preTaskInfo;
		}
		public String getProtocol() {
			return protocol;
		}
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
		public String getTrainFee() {
			return trainFee;
		}
		public void setTrainFee(String trainFee) {
			this.trainFee = trainFee;
		}
		public String getDeptCodes() {
			return deptCodes;
		}
		public void setDeptCodes(String deptCodes) {
			this.deptCodes = deptCodes;
		}
		public String getEmployeeGradeStr() {
			return employeeGradeStr;
		}
		public void setEmployeeGradeStr(String employeeGradeStr) {
			this.employeeGradeStr = employeeGradeStr;
		}
		public List<String> getEmpIds() {
			return empIds;
		}
		public void setEmpIds(List<String> empIds) {
			this.empIds = empIds;
		}
		public List<String> getEmpNames() {
			return empNames;
		}
		public void setEmpNames(List<String> empNames) {
			this.empNames = empNames;
		}
		public List<String> getDeptManagerIds() {
			return deptManagerIds;
		}
		public void setDeptManagerIds(List<String> deptManagerIds) {
			this.deptManagerIds = deptManagerIds;
		}
		public List<String> getDeptManagerNames() {
			return deptManagerNames;
		}
		public void setDeptManagerNames(List<String> deptManagerNames) {
			this.deptManagerNames = deptManagerNames;
		}
		public String getAdmins() {
			return admins;
		}
		public void setAdmins(String admins) {
			this.admins = admins;
		}
		public String getNeedApply() {
			return needApply;
		}
		public void setNeedApply(String needApply) {
			this.needApply = needApply;
		}
		public String getNumberOfParticipants() {
			return numberOfParticipants;
		}
		public void setNumberOfParticipants(String numberOfParticipants) {
			this.numberOfParticipants = numberOfParticipants;
		}
		public String getApplicationStartTime() {
			return applicationStartTime;
		}
		public void setApplicationStartTime(String applicationStartTime) {
			this.applicationStartTime = applicationStartTime;
		}
		public String getApplicationDeadline() {
			return applicationDeadline;
		}
		public void setApplicationDeadline(String applicationDeadline) {
			this.applicationDeadline = applicationDeadline;
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
		public String getProtocolStartTime() {
			return protocolStartTime;
		}
		public void setProtocolStartTime(String protocolStartTime) {
			this.protocolStartTime = protocolStartTime;
		}
		public String getProtocolEndTime() {
			return protocolEndTime;
		}
		public void setProtocolEndTime(String protocolEndTime) {
			this.protocolEndTime = protocolEndTime;
		}
		public String getActivityImg() {
			return activityImg;
		}
		public void setActivityImg(String activityImg) {
			this.activityImg = activityImg;
		}
		public String getIsPrincipal() {
			return isPrincipal;
		}
		public void setIsPrincipal(String isPrincipal) {
			this.isPrincipal = isPrincipal;
		}
		public List<String> getCode() {
			return code;
		}
		public void setCode(List<String> code) {
			this.code = code;
		}
		public Integer getPackageId() {
			return packageId;
		}
		public void setPackageId(Integer packageId) {
			this.packageId = packageId;
		}
		public List<Map<String, String>> getDeptManagers() {
			return deptManagers;
		}
		public void setDeptManagers(List<Map<String, String>> deptManagers) {
			this.deptManagers = deptManagers;
		}
		public List<Map<String, String>> getEmps() {
			return emps;
		}
		public void setEmps(List<Map<String, String>> emps) {
			this.emps = emps;
		}
		public String getCertificateState() {
			return certificateState;
		}
		public void setCertificateState(String certificateState) {
			this.certificateState = certificateState;
		}
		public Integer getCertificateId() {
			return certificateId;
		}
		public void setCertificateId(Integer certificateId) {
			this.certificateId = certificateId;
		}
		public String getCertificateAddress() {
			return certificateAddress;
		}
		public void setCertificateAddress(String certificateAddress) {
			this.certificateAddress = certificateAddress;
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
		public String getCertificateValidState() {
			return certificateValidState;
		}
		public void setCertificateValidState(String certificateValidState) {
			this.certificateValidState = certificateValidState;
		}
}