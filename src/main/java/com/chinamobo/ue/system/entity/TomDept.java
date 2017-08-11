package com.chinamobo.ue.system.entity;

import javax.ws.rs.FormParam;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TomDept {

	@JsonProperty("pk_dept")
	@FormParam("pkDept")
    private int pkDept;
	
	@FormParam("name")
    private String name;
	@FormParam("code")
    private String code;
	@FormParam("innercode")
    private String innercode;
	@FormParam("topcode")
    private String topcode;
	@FormParam("topname")
    private String topname;
	
	@JsonProperty("pk_fatherorg")
	@FormParam("pkFatherorg")
    private String pkFatherorg;
	
	@FormParam("orgcode")
    private String orgcode;
	@FormParam("orgname")
    private String orgname;
	
	@JsonProperty("pk_org")
	@FormParam("pkOrg")
    private String pkOrg;
	
	@FormParam("typecode")
    private String typecode;
	@FormParam("typename")
    private String typename;
	
	@JsonProperty("pk_typedefdoc")
	@FormParam("pkTypedefdoc")
    private String pkTypedefdoc;
	
	@FormParam("hrcanceled")
    private String hrcanceled;
	
	@JsonProperty("pk_psndoc")
	@FormParam("pkPsndoc")
    private String pkPsndoc;
	
	@FormParam("psncode")
    private String psncode;
	@FormParam("psnname")
    private String psnname;
	@FormParam("creationtime")
    private String creationtime;
	@FormParam("modifiedtime")
    private String modifiedtime;
	@FormParam("levelcode")
    private String levelcode;
	@FormParam("levelname")
    private String levelname;
	
	@JsonProperty("pk_leveldefdoc")
	@FormParam("pkLeveldefdoc")
    private String pkLeveldefdoc;
	
	@FormParam("jobcode")
    private String jobcode;
	@FormParam("jobname")
    private String jobname;
	
	@JsonProperty("pk_jobdeptdoc")
	@FormParam("pkJobdeptdoc")
    private String pkJobdeptdoc;
	
	@FormParam("citycode")
    private String citycode;
	@FormParam("cityname")
    private String cityname;
	
	public int getPkDept() {
        return pkDept;
    }

    public void setPkDept(int pkDept) {
        this.pkDept = pkDept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getInnercode() {
        return innercode;
    }

    public void setInnercode(String innercode) {
        this.innercode = innercode == null ? null : innercode.trim();
    }

    public String getTopcode() {
        return topcode;
    }

    public void setTopcode(String topcode) {
        this.topcode = topcode == null ? null : topcode.trim();
    }

    public String getTopname() {
        return topname;
    }

    public void setTopname(String topname) {
        this.topname = topname == null ? null : topname.trim();
    }

    public String getPkFatherorg() {
        return pkFatherorg;
    }

    public void setPkFatherorg(String pkFatherorg) {
        this.pkFatherorg = pkFatherorg == null ? null : pkFatherorg.trim();
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode == null ? null : orgcode.trim();
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname == null ? null : orgname.trim();
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg == null ? null : pkOrg.trim();
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode == null ? null : typecode.trim();
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename == null ? null : typename.trim();
    }

    public String getPkTypedefdoc() {
        return pkTypedefdoc;
    }

    public void setPkTypedefdoc(String pkTypedefdoc) {
        this.pkTypedefdoc = pkTypedefdoc == null ? null : pkTypedefdoc.trim();
    }

    public String getHrcanceled() {
        return hrcanceled;
    }

    public void setHrcanceled(String hrcanceled) {
        this.hrcanceled = hrcanceled == null ? null : hrcanceled.trim();
    }

    public String getPkPsndoc() {
        return pkPsndoc;
    }

    public void setPkPsndoc(String pkPsndoc) {
        this.pkPsndoc = pkPsndoc == null ? null : pkPsndoc.trim();
    }

    public String getPsncode() {
        return psncode;
    }

    public void setPsncode(String psncode) {
        this.psncode = psncode == null ? null : psncode.trim();
    }

    public String getPsnname() {
        return psnname;
    }

    public void setPsnname(String psnname) {
        this.psnname = psnname == null ? null : psnname.trim();
    }

    public String getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime == null ? null : creationtime.trim();
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime == null ? null : modifiedtime.trim();
    }

    public String getLevelcode() {
        return levelcode;
    }

    public void setLevelcode(String levelcode) {
        this.levelcode = levelcode == null ? null : levelcode.trim();
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname == null ? null : levelname.trim();
    }

    public String getPkLeveldefdoc() {
        return pkLeveldefdoc;
    }

    public void setPkLeveldefdoc(String pkLeveldefdoc) {
        this.pkLeveldefdoc = pkLeveldefdoc == null ? null : pkLeveldefdoc.trim();
    }

    public String getJobcode() {
        return jobcode;
    }

    public void setJobcode(String jobcode) {
        this.jobcode = jobcode == null ? null : jobcode.trim();
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname == null ? null : jobname.trim();
    }

    public String getPkJobdeptdoc() {
        return pkJobdeptdoc;
    }

    public void setPkJobdeptdoc(String pkJobdeptdoc) {
        this.pkJobdeptdoc = pkJobdeptdoc == null ? null : pkJobdeptdoc.trim();
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode == null ? null : citycode.trim();
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname == null ? null : cityname.trim();
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TomDept [pkDept=").append(pkDept).append(", name=").append(name).append(", code=").append(code)
				.append(", innercode=").append(innercode).append(", topcode=").append(topcode).append(", topname=")
				.append(topname).append(", pkFatherorg=").append(pkFatherorg).append(", orgcode=").append(orgcode)
				.append(", orgname=").append(orgname).append(", pkOrg=").append(pkOrg).append(", typecode=")
				.append(typecode).append(", typename=").append(typename).append(", pkTypedefdoc=").append(pkTypedefdoc)
				.append(", hrcanceled=").append(hrcanceled).append(", pkPsndoc=").append(pkPsndoc).append(", psncode=")
				.append(psncode).append(", psnname=").append(psnname).append(", creationtime=").append(creationtime)
				.append(", modifiedtime=").append(modifiedtime).append(", levelcode=").append(levelcode)
				.append(", levelname=").append(levelname).append(", pkLeveldefdoc=").append(pkLeveldefdoc)
				.append(", jobcode=").append(jobcode).append(", jobname=").append(jobname).append(", pkJobdeptdoc=")
				.append(pkJobdeptdoc).append(", citycode=").append(citycode).append(", cityname=").append(cityname)
				.append("]");
		return builder.toString();
	}

    
}