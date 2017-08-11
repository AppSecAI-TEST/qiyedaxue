package com.chinamobo.ue.api.commodity.dto;

public class CommodityResult {

	private String userId;
	
	private String eCoin;
	
	private Integer commodityId;
	
    private String commodityName;

    private Integer commodityPrice;

    private Integer commodityNumber;

    private String commodityPicture;
    
    private String arayacak;
    
    private String post;
    
    private String deptName;
    

	public String getArayacak() {
		return arayacak;
	}

	public void setArayacak(String arayacak) {
		this.arayacak = arayacak;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String geteCoin() {
		return eCoin;
	}

	public void seteCoin(String eCoin) {
		this.eCoin = eCoin;
	}

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
		this.commodityName = commodityName;
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
		this.commodityPicture = commodityPicture;
	}
    private String headImg;//员工头像
    private String name;//员工名称
    private String eNumber;//E币剩余
    private String addUpEName;//E币累计

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String geteNumber() {
		return eNumber;
	}

	public void seteNumber(String eNumber) {
		this.eNumber = eNumber;
	}

	public String getAddUpEName() {
		return addUpEName;
	}

	public void setAddUpEName(String addUpEName) {
		this.addUpEName = addUpEName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
    
    
}
