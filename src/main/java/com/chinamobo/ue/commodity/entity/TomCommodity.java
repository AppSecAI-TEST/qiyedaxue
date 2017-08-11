package com.chinamobo.ue.commodity.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomCommodity {
	@FormParam("commodityId")
    private Integer commodityId;
	@FormParam("commodityName")
    private String commodityName;
	@FormParam("commodityPrice")
    private Integer commodityPrice;
	@FormParam("commodityNumber")
    private Integer commodityNumber;
	@FormParam("commodityPicture")
    private String commodityPicture;
	@FormParam("commoditySynopsis")
    private String commoditySynopsis;
    private Date createTime;
	@FormParam("admins")
    private String admins;
    private String creator;
    private String lastOperator;
    private Date updateTime;
	@FormParam("arayacak")
    private String arayacak;
	@FormParam("post")
    private String post;
	@FormParam("arayacakAddress")
    private String arayacakAddress;
	@FormParam("commodityGrounding")
    private String commodityGrounding;
    private String creatorId;
    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName == null ? null : commodityName.trim();
    }

    public Integer getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(Integer commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public Integer getCommodityNumber() {
        return commodityNumber;
    }

    public void setCommodityNumber(Integer commodityNumber) {
        this.commodityNumber = commodityNumber;
    }

    public String getCommodityPicture() {
        return commodityPicture;
    }

    public void setCommodityPicture(String commodityPicture) {
        this.commodityPicture = commodityPicture == null ? null : commodityPicture.trim();
    }

    public String getCommoditySynopsis() {
        return commoditySynopsis;
    }

    public void setCommoditySynopsis(String commoditySynopsis) {
        this.commoditySynopsis = commoditySynopsis == null ? null : commoditySynopsis.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getLastOperator() {
        return lastOperator;
    }

    public void setLastOperator(String lastOperator) {
        this.lastOperator = lastOperator == null ? null : lastOperator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getArayacak() {
        return arayacak;
    }

    public void setArayacak(String arayacak) {
        this.arayacak = arayacak == null ? null : arayacak.trim();
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post == null ? null : post.trim();
    }

    public String getArayacakAddress() {
        return arayacakAddress;
    }

    public void setArayacakAddress(String arayacakAddress) {
        this.arayacakAddress = arayacakAddress == null ? null : arayacakAddress.trim();
    }

    public String getCommodityGrounding() {
        return commodityGrounding;
    }

    public void setCommodityGrounding(String commodityGrounding) {
        this.commodityGrounding = commodityGrounding == null ? null : commodityGrounding.trim();
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }    
}