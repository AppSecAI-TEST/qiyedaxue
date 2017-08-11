package com.chinamobo.ue.api.commodity.dto;

import java.util.Date;

public class CommodityExchangeResult {

	private Integer commodityId;

	private String commodityName;

	private Integer commodityPrice;

	private String commodityPicture;

	private String cdkey;

	private String postMethod;

	private Date exchangeTime;

	private String expressCompany;

	private String expressNumber;

	private String arayacakAddress;

	private String exchange_state;
	
	private String empNumber;
	public String getEmpNumber() {
		return empNumber;
	}

	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}

	public String getExchange_state() {
		return exchange_state;
	}

	public void setExchange_state(String exchange_state) {
		this.exchange_state = exchange_state;
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

	public String getCommodityPicture() {
		return commodityPicture;
	}

	public void setCommodityPicture(String commodityPicture) {
		this.commodityPicture = commodityPicture;
	}

	public String getCdkey() {
		return cdkey;
	}

	public void setCdkey(String cdkey) {
		this.cdkey = cdkey;
	}

	public String getPostMethod() {
		return postMethod;
	}

	public void setPostMethod(String postMethod) {
		this.postMethod = postMethod;
	}

	public Date getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(Date exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public String getArayacakAddress() {
		return arayacakAddress;
	}

	public void setArayacakAddress(String arayacakAddress) {
		this.arayacakAddress = arayacakAddress;
	}

}
