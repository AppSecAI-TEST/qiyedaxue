package com.chinamobo.ue.statistics.entity;

public class TomActivityCostDetailStatistics {
    
	private Integer activityId;//活动ID
	
	private String feeName;//项目名称
	
	private Integer fee;//花费
	
	

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}
	
	
}
