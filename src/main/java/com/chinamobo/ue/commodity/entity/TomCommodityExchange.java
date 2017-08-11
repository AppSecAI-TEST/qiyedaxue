package com.chinamobo.ue.commodity.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomCommodityExchange {
	@FormParam("code")
	private String code;
	@FormParam("empName")
    private String empName;
	@FormParam("empNumber")
    private String empNumber;
	@FormParam("commodityName")
    private String commodityName;
	@FormParam("cdkey")
    private String cdkey;
	@FormParam("postMethod")
    private String postMethod;
	@FormParam("exchangeTimeS")
	private String exchangeTimeS;
    private Date exchangeTime;
	@FormParam("expressCompany")
    private String expressCompany;
	@FormParam("expressNumber")
    private String expressNumber;
	@FormParam("exchangeState")
    private String exchangeState;
	@FormParam("phoneNumber")
    private String phoneNumber;
	@FormParam("name")
    private String name;
	@FormParam("sex")
    private String sex;
	@FormParam("address")
    private String address;
	@FormParam("commodityId")
    private Integer commodityId;

    public Integer getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(Integer commodityId) {
		this.commodityId = commodityId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber == null ? null : empNumber.trim();
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName == null ? null : commodityName.trim();
    }

    public String getCdkey() {
        return cdkey;
    }

    public void setCdkey(String cdkey) {
        this.cdkey = cdkey == null ? null : cdkey.trim();
    }

    public String getPostMethod() {
        return postMethod;
    }

    public void setPostMethod(String postMethod) {
        this.postMethod = postMethod == null ? null : postMethod.trim();
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
        this.expressCompany = expressCompany == null ? null : expressCompany.trim();
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber == null ? null : expressNumber.trim();
    }

    public String getExchangeState() {
        return exchangeState;
    }

    public void setExchangeState(String exchangeState) {
        this.exchangeState = exchangeState == null ? null : exchangeState.trim();
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExchangeTimeS() {
		return exchangeTimeS;
	}

	public void setExchangeTimeS(String exchangeTimeS) {
		this.exchangeTimeS = exchangeTimeS;
	}
    
}