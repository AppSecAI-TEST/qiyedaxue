package com.chinamobo.ue.api.myInfo.dto;

import javax.ws.rs.FormParam;

public class UserInfo {
	@FormParam("userId")
    private String userId;
	@FormParam("userName")
	private String userName;
	@FormParam("userImg")
    private String userImg;
	@FormParam("totalTime")
    private String totalTime;
	@FormParam("signInTotal")
    private Integer signInTotal;
	@FormParam("signInEbCount")
    private Integer signInEbCount;
	@FormParam("eCoin")
    private Integer eCoin;
	@FormParam("eCoinRanking")
    private Integer eCoinRanking;
	private Integer learnTimeRanking;
	@FormParam("courseCount")
    private String courseCount;
	@FormParam("userTel")
    private String userTel;
	@FormParam("isSignIn")
    private String isSignIn;
	@FormParam("address")
    private String address;
	@FormParam("addUpENumber")
    private Integer addUpENumber;
	@FormParam("consigneeName")
    private String consigneeName;
	@FormParam("consigneeGender")
    private String consigneeGender;
	@FormParam("consigneeTel")
    private String consigneeTel;
	@FormParam("consigneeAddress")
    private String consigneeAddress;
	@FormParam("anonymity")
    private String anonymity;
	private String deptName;
	
	public String getAnonymity() {
		return anonymity;
	}
	public void setAnonymity(String anonymity) {
		this.anonymity = anonymity;
	}
	public String getIsSignIn() {
		return isSignIn;
	}
	public void setIsSignIn(String isSignIn) {
		this.isSignIn = isSignIn;
	}
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
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public Integer getSignInTotal() {
		return signInTotal;
	}
	public void setSignInTotal(Integer signInTotal) {
		this.signInTotal = signInTotal;
	}
	public Integer getSignInEbCount() {
		return signInEbCount;
	}
	public void setSignInEbCount(Integer signInEbCount) {
		this.signInEbCount = signInEbCount;
	}
	public Integer geteCoin() {
		return eCoin;
	}
	public void seteCoin(Integer eCoin) {
		this.eCoin = eCoin;
	}
	public Integer geteCoinRanking() {
		return eCoinRanking;
	}
	public void seteCoinRanking(Integer eCoinRanking) {
		this.eCoinRanking = eCoinRanking;
	}
	public String getCourseCount() {
		return courseCount;
	}
	public void setCourseCount(String courseCount) {
		this.courseCount = courseCount;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getAddUpENumber() {
		return addUpENumber;
	}
	public void setAddUpENumber(Integer addUpENumber) {
		this.addUpENumber = addUpENumber;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	
	public String getConsigneeTel() {
		return consigneeTel;
	}
	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}
	
	public Integer getLearnTimeRanking() {
		return learnTimeRanking;
	}
	public void setLearnTimeRanking(Integer learnTimeRanking) {
		this.learnTimeRanking = learnTimeRanking;
	}
	public String getConsigneeGender() {
		return consigneeGender;
	}
	public void setConsigneeGender(String consigneeGender) {
		this.consigneeGender = consigneeGender;
	}
	public String getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
