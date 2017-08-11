package com.chinamobo.ue.system.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TomEmpOne {
	@FormParam("name")
	private String name;
	@FormParam("code")
	private String code;
	@FormParam("id")
	private String id;
	@JsonProperty("pk_psncl")
	private String pkPsncl;
	@FormParam("psnclcode")
	private String psnclcode;
	@FormParam("psnclcname")
	private String psnclcname;
	@FormParam("shortname")
	private String shortname;
	@FormParam("jobglbdef17")
	private String jobglbdef17;
	private String topcode;
	private String topname;
	@FormParam("modifiedtime")
	private String modifiedtime;
	@FormParam("begindate")
	private String begindate;
	@FormParam("enddate")
	private String enddate;
//	@JsonProperty("secret_email")
	@FormParam("secretEmail")
	private String secretEmail;
	@FormParam("officephone")
	private String officephone;
	@FormParam("orgcode")
	private String orgcode;
	@FormParam("orgname")
	private String orgname;
	@FormParam("deptcode")
	private String deptcode;
	@FormParam("deptname")
	private String deptname;
	@FormParam("depttopcode")
	private String depttopcode;
	@FormParam("depttopname")
	private String depttopname;
	@FormParam("citycode")
	private String citycode;
	@FormParam("cityname")
	private String cityname;
	@FormParam("postcode")
	private String postcode;
	@FormParam("postname")
	private String postname;
	@FormParam("bankcode")
	private String bankcode;
	@FormParam("bankname")
	private String bankname;
	@FormParam("banktypecode")
	private String banktypecode;
	@FormParam("banktypename")
	private String banktypename;
	@FormParam("ismainjob")
	private String ismainjob;
	@FormParam("creationtime")
	private String creationtime;
	@FormParam("birthdate")
	private String birthdate;
	@FormParam("sex")
	private String sex;
	@FormParam("idtype")
	private String idtype;
	@JsonProperty("pk_post")
	private String pkPost;
	@FormParam("jobcode")
	private String jobcode;
	@FormParam("jobname")
	private String jobname;
	@FormParam("poststat")
	private String poststat;
	@FormParam("hiendflag")
	private String hiendflag;
	@JsonProperty("pk_psnjob")
	private String pkPsnjob;
	@JsonProperty("pk_baspost")
	private String pkBaspost;
	@FormParam("mobile")
	private String mobile;
	@FormParam("orgendflag")
	private String orgendflag;
	@JsonProperty("pk_deptpsn")
	private String pkDeptpsn;
	@FormParam("deptpsncode")
	private String deptpsncode;
	@FormParam("deptpsnname")
	private String deptpsnname;
	@JsonProperty("pk_psndoc")
	private String pkPsndoc;
	@FormParam("onedeptcode")
	private String onedeptcode;
	@FormParam("onedeptname")
	private String onedeptname;
	@FormParam("jobglbdef20")
	private String jobglbdef20;
	@FormParam("jobglbdef21")
	private String jobglbdef21;
	@FormParam("jobglbdef22")
	private String jobglbdef22;
	@FormParam("jobglbdef23")
	private String jobglbdef23;
	@FormParam("jobglbdef24")
	private String jobglbdef24;
	@FormParam("jobglbdef25")
	private String jobglbdef25;
	@JsonProperty("pk_jobrank")
	private String pkJobrank;
	@JsonProperty("pk_jobgrade")
	private String pkJobgrade;
	@FormParam("joblastflag")
	private String joblastflag;
	@FormParam("orglastflag")
	private String orglastflag;
	@FormParam("jobglbdef10")
	private String jobglbdef10;
//	@JsonProperty("pk_dept")
	@FormParam("pkDept")
	private String pkDept;
//	@JsonProperty("pk_org")
	@FormParam("pkOrg")
	private String pkOrg;
	@FormParam("onedeptpk")
	private String onedeptpk;
	@FormParam("jobendflag")
	private String jobendflag;
	@FormParam("glbdef24")
	private String glbdef24;
	@FormParam("orgbegindate")
	private String orgbegindate;
	@FormParam("orgenddate")
	private String orgenddate;
	private Date createTime;
	private Date updateTime;
	@FormParam("status")
    private String status;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPkPsncl() {
		return pkPsncl;
	}
	public void setPkPsncl(String pkPsncl) {
		this.pkPsncl = pkPsncl;
	}
	public String getPsnclcode() {
		return psnclcode;
	}
	public void setPsnclcode(String psnclcode) {
		this.psnclcode = psnclcode;
	}
	public String getPsnclcname() {
		return psnclcname;
	}
	public void setPsnclcname(String psnclcname) {
		this.psnclcname = psnclcname;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getJobglbdef17() {
		return jobglbdef17;
	}
	public void setJobglbdef17(String jobglbdef17) {
		this.jobglbdef17 = jobglbdef17;
	}
	public String getTopcode() {
		return topcode;
	}
	public void setTopcode(String topcode) {
		this.topcode = topcode;
	}
	public String getTopname() {
		return topname;
	}
	public void setTopname(String topname) {
		this.topname = topname;
	}
	public String getModifiedtime() {
		return modifiedtime;
	}
	public void setModifiedtime(String modifiedtime) {
		this.modifiedtime = modifiedtime;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getSecretEmail() {
		return secretEmail;
	}
	public void setSecretEmail(String secretEmail) {
		this.secretEmail = secretEmail;
	}
	public String getOfficephone() {
		return officephone;
	}
	public void setOfficephone(String officephone) {
		this.officephone = officephone;
	}
	public String getOrgcode() {
		return orgcode;
	}
	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getDepttopcode() {
		return depttopcode;
	}
	public void setDepttopcode(String depttopcode) {
		this.depttopcode = depttopcode;
	}
	public String getDepttopname() {
		return depttopname;
	}
	public void setDepttopname(String depttopname) {
		this.depttopname = depttopname;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getPostname() {
		return postname;
	}
	public void setPostname(String postname) {
		this.postname = postname;
	}
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBanktypecode() {
		return banktypecode;
	}
	public void setBanktypecode(String banktypecode) {
		this.banktypecode = banktypecode;
	}
	public String getBanktypename() {
		return banktypename;
	}
	public void setBanktypename(String banktypename) {
		this.banktypename = banktypename;
	}
	public String getIsmainjob() {
		return ismainjob;
	}
	public void setIsmainjob(String ismainjob) {
		this.ismainjob = ismainjob;
	}
	public String getCreationtime() {
		return creationtime;
	}
	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getPkPost() {
		return pkPost;
	}
	public void setPkPost(String pkPost) {
		this.pkPost = pkPost;
	}
	public String getJobcode() {
		return jobcode;
	}
	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getPoststat() {
		return poststat;
	}
	public void setPoststat(String poststat) {
		this.poststat = poststat;
	}
	public String getHiendflag() {
		return hiendflag;
	}
	public void setHiendflag(String hiendflag) {
		this.hiendflag = hiendflag;
	}
	public String getPkPsnjob() {
		return pkPsnjob;
	}
	public void setPkPsnjob(String pkPsnjob) {
		this.pkPsnjob = pkPsnjob;
	}
	public String getPkBaspost() {
		return pkBaspost;
	}
	public void setPkBaspost(String pkBaspost) {
		this.pkBaspost = pkBaspost;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOrgendflag() {
		return orgendflag;
	}
	public void setOrgendflag(String orgendflag) {
		this.orgendflag = orgendflag;
	}
	public String getPkDeptpsn() {
		return pkDeptpsn;
	}
	public void setPkDeptpsn(String pkDeptpsn) {
		this.pkDeptpsn = pkDeptpsn;
	}
	public String getDeptpsncode() {
		return deptpsncode;
	}
	public void setDeptpsncode(String deptpsncode) {
		this.deptpsncode = deptpsncode;
	}
	public String getDeptpsnname() {
		return deptpsnname;
	}
	public void setDeptpsnname(String deptpsnname) {
		this.deptpsnname = deptpsnname;
	}
	public String getPkPsndoc() {
		return pkPsndoc;
	}
	public void setPkPsndoc(String pkPsndoc) {
		this.pkPsndoc = pkPsndoc;
	}
	public String getOnedeptcode() {
		return onedeptcode;
	}
	public void setOnedeptcode(String onedeptcode) {
		this.onedeptcode = onedeptcode;
	}
	public String getOnedeptname() {
		return onedeptname;
	}
	public void setOnedeptname(String onedeptname) {
		this.onedeptname = onedeptname;
	}
	public String getJobglbdef20() {
		return jobglbdef20;
	}
	public void setJobglbdef20(String jobglbdef20) {
		this.jobglbdef20 = jobglbdef20;
	}
	public String getJobglbdef21() {
		return jobglbdef21;
	}
	public void setJobglbdef21(String jobglbdef21) {
		this.jobglbdef21 = jobglbdef21;
	}
	public String getJobglbdef22() {
		return jobglbdef22;
	}
	public void setJobglbdef22(String jobglbdef22) {
		this.jobglbdef22 = jobglbdef22;
	}
	public String getJobglbdef23() {
		return jobglbdef23;
	}
	public void setJobglbdef23(String jobglbdef23) {
		this.jobglbdef23 = jobglbdef23;
	}
	public String getJobglbdef24() {
		return jobglbdef24;
	}
	public void setJobglbdef24(String jobglbdef24) {
		this.jobglbdef24 = jobglbdef24;
	}
	public String getJobglbdef25() {
		return jobglbdef25;
	}
	public void setJobglbdef25(String jobglbdef25) {
		this.jobglbdef25 = jobglbdef25;
	}
	public String getPkJobrank() {
		return pkJobrank;
	}
	public void setPkJobrank(String pkJobrank) {
		this.pkJobrank = pkJobrank;
	}
	public String getPkJobgrade() {
		return pkJobgrade;
	}
	public void setPkJobgrade(String pkJobgrade) {
		this.pkJobgrade = pkJobgrade;
	}
	public String getJoblastflag() {
		return joblastflag;
	}
	public void setJoblastflag(String joblastflag) {
		this.joblastflag = joblastflag;
	}
	public String getOrglastflag() {
		return orglastflag;
	}
	public void setOrglastflag(String orglastflag) {
		this.orglastflag = orglastflag;
	}
	public String getJobglbdef10() {
		return jobglbdef10;
	}
	public void setJobglbdef10(String jobglbdef10) {
		this.jobglbdef10 = jobglbdef10;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getOnedeptpk() {
		return onedeptpk;
	}
	public void setOnedeptpk(String onedeptpk) {
		this.onedeptpk = onedeptpk;
	}
	public String getJobendflag() {
		return jobendflag;
	}
	public void setJobendflag(String jobendflag) {
		this.jobendflag = jobendflag;
	}
	public String getGlbdef24() {
		return glbdef24;
	}
	public void setGlbdef24(String glbdef24) {
		this.glbdef24 = glbdef24;
	}
	public String getOrgbegindate() {
		return orgbegindate;
	}
	public void setOrgbegindate(String orgbegindate) {
		this.orgbegindate = orgbegindate;
	}
	public String getOrgenddate() {
		return orgenddate;
	}
	public void setOrgenddate(String orgenddate) {
		this.orgenddate = orgenddate;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TomEmpOne [name=").append(name).append(", code=").append(code).append(", id=").append(id)
				.append(", pkPsncl=").append(pkPsncl).append(", psnclcode=").append(psnclcode).append(", psnclcname=")
				.append(psnclcname).append(", shortname=").append(shortname).append(", jobglbdef17=")
				.append(jobglbdef17).append(", topcode=").append(topcode).append(", topname=").append(topname)
				.append(", modifiedtime=").append(modifiedtime).append(", begindate=").append(begindate)
				.append(", enddate=").append(enddate).append(", secretEmail=").append(secretEmail)
				.append(", officephone=").append(officephone).append(", orgcode=").append(orgcode).append(", orgname=")
				.append(orgname).append(", deptcode=").append(deptcode).append(", deptname=").append(deptname)
				.append(", depttopcode=").append(depttopcode).append(", depttopname=").append(depttopname)
				.append(", citycode=").append(citycode).append(", cityname=").append(cityname).append(", postcode=")
				.append(postcode).append(", postname=").append(postname).append(", bankcode=").append(bankcode)
				.append(", bankname=").append(bankname).append(", banktypecode=").append(banktypecode)
				.append(", banktypename=").append(banktypename).append(", ismainjob=").append(ismainjob)
				.append(", creationtime=").append(creationtime).append(", birthdate=").append(birthdate)
				.append(", sex=").append(sex).append(", idtype=").append(idtype).append(", pkPost=").append(pkPost)
				.append(", jobcode=").append(jobcode).append(", jobname=").append(jobname).append(", poststat=")
				.append(poststat).append(", hiendflag=").append(hiendflag).append(", pkPsnjob=").append(pkPsnjob)
				.append(", pkBaspost=").append(pkBaspost).append(", mobile=").append(mobile).append(", orgendflag=")
				.append(orgendflag).append(", pkDeptpsn=").append(pkDeptpsn).append(", deptpsncode=")
				.append(deptpsncode).append(", deptpsnname=").append(deptpsnname).append(", pkPsndoc=").append(pkPsndoc)
				.append(", onedeptcode=").append(onedeptcode).append(", onedeptname=").append(onedeptname)
				.append(", jobglbdef20=").append(jobglbdef20).append(", jobglbdef21=").append(jobglbdef21)
				.append(", jobglbdef22=").append(jobglbdef22).append(", jobglbdef23=").append(jobglbdef23)
				.append(", jobglbdef24=").append(jobglbdef24).append(", jobglbdef25=").append(jobglbdef25)
				.append(", pkJobrank=").append(pkJobrank).append(", pkJobgrade=").append(pkJobgrade)
				.append(", joblastflag=").append(joblastflag).append(", orglastflag=").append(orglastflag)
				.append(", jobglbdef10=").append(jobglbdef10).append(", pkDept=").append(pkDept).append(", pkOrg=")
				.append(pkOrg).append(", onedeptpk=").append(onedeptpk).append(", jobendflag=").append(jobendflag)
				.append(", glbdef24=").append(glbdef24).append(", orgbegindate=").append(orgbegindate)
				.append(", orgenddate=").append(orgenddate).append("]");
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankcode == null) ? 0 : bankcode.hashCode());
		result = prime * result + ((bankname == null) ? 0 : bankname.hashCode());
		result = prime * result + ((banktypecode == null) ? 0 : banktypecode.hashCode());
		result = prime * result + ((banktypename == null) ? 0 : banktypename.hashCode());
		result = prime * result + ((begindate == null) ? 0 : begindate.hashCode());
		result = prime * result + ((birthdate == null) ? 0 : birthdate.hashCode());
		result = prime * result + ((citycode == null) ? 0 : citycode.hashCode());
		result = prime * result + ((cityname == null) ? 0 : cityname.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((creationtime == null) ? 0 : creationtime.hashCode());
		result = prime * result + ((deptcode == null) ? 0 : deptcode.hashCode());
		result = prime * result + ((deptname == null) ? 0 : deptname.hashCode());
		result = prime * result + ((deptpsncode == null) ? 0 : deptpsncode.hashCode());
		result = prime * result + ((deptpsnname == null) ? 0 : deptpsnname.hashCode());
		result = prime * result + ((depttopcode == null) ? 0 : depttopcode.hashCode());
		result = prime * result + ((depttopname == null) ? 0 : depttopname.hashCode());
		result = prime * result + ((enddate == null) ? 0 : enddate.hashCode());
		result = prime * result + ((glbdef24 == null) ? 0 : glbdef24.hashCode());
		result = prime * result + ((hiendflag == null) ? 0 : hiendflag.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idtype == null) ? 0 : idtype.hashCode());
		result = prime * result + ((ismainjob == null) ? 0 : ismainjob.hashCode());
		result = prime * result + ((jobcode == null) ? 0 : jobcode.hashCode());
		result = prime * result + ((jobendflag == null) ? 0 : jobendflag.hashCode());
		result = prime * result + ((jobglbdef10 == null) ? 0 : jobglbdef10.hashCode());
		result = prime * result + ((jobglbdef17 == null) ? 0 : jobglbdef17.hashCode());
		result = prime * result + ((jobglbdef20 == null) ? 0 : jobglbdef20.hashCode());
		result = prime * result + ((jobglbdef21 == null) ? 0 : jobglbdef21.hashCode());
		result = prime * result + ((jobglbdef22 == null) ? 0 : jobglbdef22.hashCode());
		result = prime * result + ((jobglbdef23 == null) ? 0 : jobglbdef23.hashCode());
		result = prime * result + ((jobglbdef24 == null) ? 0 : jobglbdef24.hashCode());
		result = prime * result + ((jobglbdef25 == null) ? 0 : jobglbdef25.hashCode());
		result = prime * result + ((joblastflag == null) ? 0 : joblastflag.hashCode());
		result = prime * result + ((jobname == null) ? 0 : jobname.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((modifiedtime == null) ? 0 : modifiedtime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((officephone == null) ? 0 : officephone.hashCode());
		result = prime * result + ((onedeptcode == null) ? 0 : onedeptcode.hashCode());
		result = prime * result + ((onedeptname == null) ? 0 : onedeptname.hashCode());
		result = prime * result + ((onedeptpk == null) ? 0 : onedeptpk.hashCode());
		result = prime * result + ((orgbegindate == null) ? 0 : orgbegindate.hashCode());
		result = prime * result + ((orgcode == null) ? 0 : orgcode.hashCode());
		result = prime * result + ((orgenddate == null) ? 0 : orgenddate.hashCode());
		result = prime * result + ((orgendflag == null) ? 0 : orgendflag.hashCode());
		result = prime * result + ((orglastflag == null) ? 0 : orglastflag.hashCode());
		result = prime * result + ((orgname == null) ? 0 : orgname.hashCode());
		result = prime * result + ((pkBaspost == null) ? 0 : pkBaspost.hashCode());
		result = prime * result + ((pkDept == null) ? 0 : pkDept.hashCode());
		result = prime * result + ((pkDeptpsn == null) ? 0 : pkDeptpsn.hashCode());
		result = prime * result + ((pkJobgrade == null) ? 0 : pkJobgrade.hashCode());
		result = prime * result + ((pkJobrank == null) ? 0 : pkJobrank.hashCode());
		result = prime * result + ((pkOrg == null) ? 0 : pkOrg.hashCode());
		result = prime * result + ((pkPost == null) ? 0 : pkPost.hashCode());
		result = prime * result + ((pkPsncl == null) ? 0 : pkPsncl.hashCode());
		result = prime * result + ((pkPsndoc == null) ? 0 : pkPsndoc.hashCode());
		result = prime * result + ((pkPsnjob == null) ? 0 : pkPsnjob.hashCode());
		result = prime * result + ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((postname == null) ? 0 : postname.hashCode());
		result = prime * result + ((poststat == null) ? 0 : poststat.hashCode());
		result = prime * result + ((psnclcname == null) ? 0 : psnclcname.hashCode());
		result = prime * result + ((psnclcode == null) ? 0 : psnclcode.hashCode());
		result = prime * result + ((secretEmail == null) ? 0 : secretEmail.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((shortname == null) ? 0 : shortname.hashCode());
		result = prime * result + ((topcode == null) ? 0 : topcode.hashCode());
		result = prime * result + ((topname == null) ? 0 : topname.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TomEmpOne other = (TomEmpOne) obj;
		if (bankcode == null) {
			if (other.bankcode != null)
				return false;
		} else if (!bankcode.equals(other.bankcode))
			return false;
		if (bankname == null) {
			if (other.bankname != null)
				return false;
		} else if (!bankname.equals(other.bankname))
			return false;
		if (banktypecode == null) {
			if (other.banktypecode != null)
				return false;
		} else if (!banktypecode.equals(other.banktypecode))
			return false;
		if (banktypename == null) {
			if (other.banktypename != null)
				return false;
		} else if (!banktypename.equals(other.banktypename))
			return false;
		if (begindate == null) {
			if (other.begindate != null)
				return false;
		} else if (!begindate.equals(other.begindate))
			return false;
		if (birthdate == null) {
			if (other.birthdate != null)
				return false;
		} else if (!birthdate.equals(other.birthdate))
			return false;
		if (citycode == null) {
			if (other.citycode != null)
				return false;
		} else if (!citycode.equals(other.citycode))
			return false;
		if (cityname == null) {
			if (other.cityname != null)
				return false;
		} else if (!cityname.equals(other.cityname))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (creationtime == null) {
			if (other.creationtime != null)
				return false;
		} else if (!creationtime.equals(other.creationtime))
			return false;
		if (deptcode == null) {
			if (other.deptcode != null)
				return false;
		} else if (!deptcode.equals(other.deptcode))
			return false;
		if (deptname == null) {
			if (other.deptname != null)
				return false;
		} else if (!deptname.equals(other.deptname))
			return false;
		if (deptpsncode == null) {
			if (other.deptpsncode != null)
				return false;
		} else if (!deptpsncode.equals(other.deptpsncode))
			return false;
		if (deptpsnname == null) {
			if (other.deptpsnname != null)
				return false;
		} else if (!deptpsnname.equals(other.deptpsnname))
			return false;
		if (depttopcode == null) {
			if (other.depttopcode != null)
				return false;
		} else if (!depttopcode.equals(other.depttopcode))
			return false;
		if (depttopname == null) {
			if (other.depttopname != null)
				return false;
		} else if (!depttopname.equals(other.depttopname))
			return false;
		if (enddate == null) {
			if (other.enddate != null)
				return false;
		} else if (!enddate.equals(other.enddate))
			return false;
		if (glbdef24 == null) {
			if (other.glbdef24 != null)
				return false;
		} else if (!glbdef24.equals(other.glbdef24))
			return false;
		if (hiendflag == null) {
			if (other.hiendflag != null)
				return false;
		} else if (!hiendflag.equals(other.hiendflag))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idtype == null) {
			if (other.idtype != null)
				return false;
		} else if (!idtype.equals(other.idtype))
			return false;
		if (ismainjob == null) {
			if (other.ismainjob != null)
				return false;
		} else if (!ismainjob.equals(other.ismainjob))
			return false;
		if (jobcode == null) {
			if (other.jobcode != null)
				return false;
		} else if (!jobcode.equals(other.jobcode))
			return false;
		if (jobendflag == null) {
			if (other.jobendflag != null)
				return false;
		} else if (!jobendflag.equals(other.jobendflag))
			return false;
		if (jobglbdef10 == null) {
			if (other.jobglbdef10 != null)
				return false;
		} else if (!jobglbdef10.equals(other.jobglbdef10))
			return false;
		if (jobglbdef17 == null) {
			if (other.jobglbdef17 != null)
				return false;
		} else if (!jobglbdef17.equals(other.jobglbdef17))
			return false;
		if (jobglbdef20 == null) {
			if (other.jobglbdef20 != null)
				return false;
		} else if (!jobglbdef20.equals(other.jobglbdef20))
			return false;
		if (jobglbdef21 == null) {
			if (other.jobglbdef21 != null)
				return false;
		} else if (!jobglbdef21.equals(other.jobglbdef21))
			return false;
		if (jobglbdef22 == null) {
			if (other.jobglbdef22 != null)
				return false;
		} else if (!jobglbdef22.equals(other.jobglbdef22))
			return false;
		if (jobglbdef23 == null) {
			if (other.jobglbdef23 != null)
				return false;
		} else if (!jobglbdef23.equals(other.jobglbdef23))
			return false;
		if (jobglbdef24 == null) {
			if (other.jobglbdef24 != null)
				return false;
		} else if (!jobglbdef24.equals(other.jobglbdef24))
			return false;
		if (jobglbdef25 == null) {
			if (other.jobglbdef25 != null)
				return false;
		} else if (!jobglbdef25.equals(other.jobglbdef25))
			return false;
		if (joblastflag == null) {
			if (other.joblastflag != null)
				return false;
		} else if (!joblastflag.equals(other.joblastflag))
			return false;
		if (jobname == null) {
			if (other.jobname != null)
				return false;
		} else if (!jobname.equals(other.jobname))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (modifiedtime == null) {
			if (other.modifiedtime != null)
				return false;
		} else if (!modifiedtime.equals(other.modifiedtime))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (officephone == null) {
			if (other.officephone != null)
				return false;
		} else if (!officephone.equals(other.officephone))
			return false;
		if (onedeptcode == null) {
			if (other.onedeptcode != null)
				return false;
		} else if (!onedeptcode.equals(other.onedeptcode))
			return false;
		if (onedeptname == null) {
			if (other.onedeptname != null)
				return false;
		} else if (!onedeptname.equals(other.onedeptname))
			return false;
		if (onedeptpk == null) {
			if (other.onedeptpk != null)
				return false;
		} else if (!onedeptpk.equals(other.onedeptpk))
			return false;
		if (orgbegindate == null) {
			if (other.orgbegindate != null)
				return false;
		} else if (!orgbegindate.equals(other.orgbegindate))
			return false;
		if (orgcode == null) {
			if (other.orgcode != null)
				return false;
		} else if (!orgcode.equals(other.orgcode))
			return false;
		if (orgenddate == null) {
			if (other.orgenddate != null)
				return false;
		} else if (!orgenddate.equals(other.orgenddate))
			return false;
		if (orgendflag == null) {
			if (other.orgendflag != null)
				return false;
		} else if (!orgendflag.equals(other.orgendflag))
			return false;
		if (orglastflag == null) {
			if (other.orglastflag != null)
				return false;
		} else if (!orglastflag.equals(other.orglastflag))
			return false;
		if (orgname == null) {
			if (other.orgname != null)
				return false;
		} else if (!orgname.equals(other.orgname))
			return false;
		if (pkBaspost == null) {
			if (other.pkBaspost != null)
				return false;
		} else if (!pkBaspost.equals(other.pkBaspost))
			return false;
		if (pkDept == null) {
			if (other.pkDept != null)
				return false;
		} else if (!pkDept.equals(other.pkDept))
			return false;
		if (pkDeptpsn == null) {
			if (other.pkDeptpsn != null)
				return false;
		} else if (!pkDeptpsn.equals(other.pkDeptpsn))
			return false;
		if (pkJobgrade == null) {
			if (other.pkJobgrade != null)
				return false;
		} else if (!pkJobgrade.equals(other.pkJobgrade))
			return false;
		if (pkJobrank == null) {
			if (other.pkJobrank != null)
				return false;
		} else if (!pkJobrank.equals(other.pkJobrank))
			return false;
		if (pkOrg == null) {
			if (other.pkOrg != null)
				return false;
		} else if (!pkOrg.equals(other.pkOrg))
			return false;
		if (pkPost == null) {
			if (other.pkPost != null)
				return false;
		} else if (!pkPost.equals(other.pkPost))
			return false;
		if (pkPsncl == null) {
			if (other.pkPsncl != null)
				return false;
		} else if (!pkPsncl.equals(other.pkPsncl))
			return false;
		if (pkPsndoc == null) {
			if (other.pkPsndoc != null)
				return false;
		} else if (!pkPsndoc.equals(other.pkPsndoc))
			return false;
		if (pkPsnjob == null) {
			if (other.pkPsnjob != null)
				return false;
		} else if (!pkPsnjob.equals(other.pkPsnjob))
			return false;
		if (postcode == null) {
			if (other.postcode != null)
				return false;
		} else if (!postcode.equals(other.postcode))
			return false;
		if (postname == null) {
			if (other.postname != null)
				return false;
		} else if (!postname.equals(other.postname))
			return false;
		if (poststat == null) {
			if (other.poststat != null)
				return false;
		} else if (!poststat.equals(other.poststat))
			return false;
		if (psnclcname == null) {
			if (other.psnclcname != null)
				return false;
		} else if (!psnclcname.equals(other.psnclcname))
			return false;
		if (psnclcode == null) {
			if (other.psnclcode != null)
				return false;
		} else if (!psnclcode.equals(other.psnclcode))
			return false;
		if (secretEmail == null) {
			if (other.secretEmail != null)
				return false;
		} else if (!secretEmail.equals(other.secretEmail))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (shortname == null) {
			if (other.shortname != null)
				return false;
		} else if (!shortname.equals(other.shortname))
			return false;
		if (topcode == null) {
			if (other.topcode != null)
				return false;
		} else if (!topcode.equals(other.topcode))
			return false;
		if (topname == null) {
			if (other.topname != null)
				return false;
		} else if (!topname.equals(other.topname))
			return false;
		return true;
	}

	
}
