package com.chinamobo.ue.activity.dto;

import com.chinamobo.ue.api.activity.dto.ActResults;
import com.chinamobo.ue.common.entity.PageData;

public class Myactivity {
	private int count0;
	private int count1;
	private int count2;
	private int count3;
    private PageData<ActResults> activityList;
    
    
	public PageData<ActResults> getActivityList() {
		return activityList;
	}

	public void setActivityList(PageData<ActResults> activityList) {
		this.activityList = activityList;
	}

	public int getCount0() {
		return count0;
	}

	public void setCount0(int count0) {
		this.count0 = count0;
	}

	public int getCount1() {
		return count1;
	}

	public void setCount1(int count1) {
		this.count1 = count1;
	}

	public int getCount2() {
		return count2;
	}

	public void setCount2(int count2) {
		this.count2 = count2;
	}

	public int getCount3() {
		return count3;
	}

	public void setCount3(int count3) {
		this.count3 = count3;
	}
	
}
