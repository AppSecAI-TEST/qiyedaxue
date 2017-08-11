package com.chinamobo.ue.system.entity;

import com.chinamobo.ue.utils.DateUtil;

public class NewsResults {

	private Integer newsId;
	 
	 private String newsTitle;
	 
	 private String newsPicture;
	 
	 private String pkOrg;
	 
	 private String releaseTime;
	 
	 private String viewCount;
	 
	 private String isRelease;
	 
	 private String administrators;
	 
	 private String creater;
	 
	 private String createTime;
	 
	 private String operator;
	 
	 private String updateTime;
	 
	 private String newsDetails;
	 
	 private String releaser;

	

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsPicture() {
		return newsPicture;
	}

	public void setNewsPicture(String newsPicture) {
		this.newsPicture = newsPicture;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getReleaseTime() {
		if(null != releaseTime){
			return releaseTime.substring(0, 16);
		}else{
			return DateUtil.getDateTime();
		}
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime.substring(0, 16);
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(String isRelease) {
		this.isRelease = isRelease;
	}

	public String getAdministrators() {
		return administrators;
	}

	public void setAdministrators(String administrators) {
		this.administrators = administrators;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreateTime() {
		if(null != createTime){
			return createTime.substring(0, 16);
		}else{
			return DateUtil.getDateTime();
		}
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime.substring(0, 16);
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getUpdateTime() {
		if(null != updateTime){
			return updateTime.substring(0, 16);
		}else{
			return DateUtil.getDateTime();
		}
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime.substring(0, 16);
	}

	public String getNewsDetails() {
		return newsDetails;
	}

	public void setNewsDetails(String newsDetails) {
		this.newsDetails = newsDetails;
	}

	public String getReleaser() {
		return releaser;
	}

	public void setReleaser(String releaser) {
		this.releaser = releaser;
	}
	 
	 
}
