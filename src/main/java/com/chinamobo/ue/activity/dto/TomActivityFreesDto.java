package com.chinamobo.ue.activity.dto;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.activity.entity.TomActivityFees;

public class TomActivityFreesDto {
	
	@FormParam("listFrees[]")
	private List<TomActivityFees> listFrees;
	
	@FormParam("feeName")
    private String feeName;

	@FormParam("fee")
    private Double fee;

	@FormParam("remark")
    private String remark;

	@FormParam("status")
    private String status;

	@FormParam("creator")
    private String creator;

	@FormParam("operator")
    private String operator;
	
	@FormParam("updateTime")
    private Date updateTime;
    
    
	@FormParam("createTime")
    private Date createTime;
    
    @FormParam("tip")
    private String tip;
    
    @FormParam("activityId")
    private Integer activityId;

	@FormParam("feeId")
    private Integer feeId;

    private Integer feeIndex;
	
    public Date getCreateTime() {
    	return createTime;
    }
    
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName == null ? null : feeName.trim();
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public List<TomActivityFees> getListFrees() {
		return listFrees;
	}

	public void setListFrees(List<TomActivityFees> listFrees) {
		this.listFrees = listFrees;
	}

	public Integer getFeeIndex() {
		return feeIndex;
	}

	public void setFeeIndex(Integer feeIndex) {
		this.feeIndex = feeIndex;
	}
    
}