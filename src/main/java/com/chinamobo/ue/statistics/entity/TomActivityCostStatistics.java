package com.chinamobo.ue.statistics.entity;


import com.chinamobo.ue.utils.DateUtil;

public class TomActivityCostStatistics {
	 private Integer actitvtyId;//活动ID
     
	 private String activityName;//活动名称
	 
	 private String activityStarttime;//活动开始时间
	 
	 private Integer allFee;//活动总花费

	public Integer getActitvtyId() {
		return actitvtyId;
	}

	public void setActitvtyId(Integer actitvtyId) {
		this.actitvtyId = actitvtyId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}



	public String getActivityStarttime() {
		if(null != activityStarttime){
			return activityStarttime.substring(0, 16);
		}else{
			return DateUtil.getDateTime();
		}
	}

	public void setActivityStarttime(String activityStarttime) {
		this.activityStarttime = activityStarttime.substring(0, 16);
	}

	public Integer getAllFee() {
		return allFee;
	}

	public void setAllFee(Integer allFee) {
		this.allFee = allFee;
	}


	
	 
}
