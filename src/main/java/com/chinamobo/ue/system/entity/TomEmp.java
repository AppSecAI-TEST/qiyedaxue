package com.chinamobo.ue.system.entity;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.entity.TomExam;

public class TomEmp {
	@FormParam("code")
    private String code;
	@FormParam("name")
    private String name;
	@FormParam("id")
    private String id;
	@FormParam("pkPsncl")
    private String pkPsncl;
	@FormParam("psnclcode")
    private String psnclcode;
	@FormParam("psnclcname")
    private String psnclcname;
	@FormParam("shortname")
    private String shortname;
	@FormParam("jobglbdef17")
    private String jobglbdef17;
	@FormParam("topcode")
    private String topcode;
	@FormParam("topname")
    private String topname;
	@FormParam("modifiedtime")
    private String modifiedtime;
	@FormParam("begindate")
    private String begindate;
	@FormParam("enddate")
    private String enddate;
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
	@FormParam("pkPost")
    private String pkPost;
	@FormParam("jobcode")
    private String jobcode;
	@FormParam("jobname")
    private String jobname;
	@FormParam("poststat")
	private String poststat;
	@FormParam("hiendflag")
    private String hiendflag;
	@FormParam("pkPsnjob")
    private String pkPsnjob;
	@FormParam("pkBaspost")
    private String pkBaspost;
	@FormParam("mobile")
    private String mobile;
	@FormParam("orgendflag")
    private String orgendflag;
	@FormParam("pkDeptpsn")
    private String pkDeptpsn;
	@FormParam("deptpsncode")
    private String deptpsncode;
	@FormParam("deptpsnname")
    private String deptpsnname;
	@FormParam("pkPsndoc")
    private String pkPsndoc;
	@FormParam("onedeptcode")
    private String onedeptcode;
	@FormParam("onedeptname")
    private String onedeptname;
	@FormParam("cityname")
    private String cityname;
	@FormParam("jobendflag")
    private String jobendflag;
	@FormParam("joblastflag")
    private String joblastflag;
	@FormParam("orglastflag")
    private String orglastflag;
	@FormParam("jobglbdef10")
    private String jobglbdef10;
	@FormParam("pkOrg")
    private String pkOrg;
	@FormParam("pkDept")
    private String pkDept;
	@FormParam("oneDeptPk")
    private String oneDeptPk;
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
	@FormParam("pkJobrank")
	private String pkJobrank;
	@FormParam("pkJobgrade")
	private String pkJobgrade;
	@FormParam("glbdef24")
	private String glbdef24;
	@FormParam("orgbegindate")
	private String orgbegindate;
	@FormParam("orgenddate")
	private String orgenddate;
	@FormParam("createTime")
	private Date createTime;
	@FormParam("updateTime")
	private Date updateTime;
	
	
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

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getOneDeptPk() {
		return oneDeptPk;
	}

	public void setOneDeptPk(String oneDeptPk) {
		this.oneDeptPk = oneDeptPk;
	}

	private TomUserInfo tomUserInfo;
    
    private List<TomActivity> tomActivity;
    
    private List<TomCourses> tomCourses;
    
    private List<TomExam> tomExam;
    
    private TomUserLog tomUserLog;
    
    private String orExam;
    
    private Integer eB;
    
    private List<Object> tomCourseEmpRelation;



	public List<Object> getTomCourseEmpRelation() {
		return tomCourseEmpRelation;
	}

	public void setTomCourseEmpRelation(List<Object> tomCourseEmpRelation) {
		this.tomCourseEmpRelation = tomCourseEmpRelation;
	}

	public String getOrExam() {
		return orExam;
	}

	public void setOrExam(String orExam) {
		this.orExam = orExam;
	}

	public Integer geteB() {
		return eB;
	}

	public void seteB(Integer eB) {
		this.eB = eB;
	}

	public TomUserLog getTomUserLog() {
		return tomUserLog;
	}

	public void setTomUserLog(TomUserLog tomUserLog) {
		this.tomUserLog = tomUserLog;
	}

	public List<TomCourses> getTomCourses() {
		return tomCourses;
	}

	public void setTomCourses(List<TomCourses> tomCourses) {
		this.tomCourses = tomCourses;
	}

	public List<TomExam> getTomExam() {
		return tomExam;
	}

	public void setTomExam(List<TomExam> tomExam) {
		this.tomExam = tomExam;
	}

	public List<TomActivity> getTomActivity() {
		return tomActivity;
	}

	public void setTomActivity(List<TomActivity> tomActivity) {
		this.tomActivity = tomActivity;
	}

	public TomUserInfo getTomUserInfo() {
		return tomUserInfo;
	}

	public void setTomUserInfo(TomUserInfo tomUserInfo) {
		this.tomUserInfo = tomUserInfo;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPkPsncl() {
        return pkPsncl;
    }

    public void setPkPsncl(String pkPsncl) {
        this.pkPsncl = pkPsncl == null ? null : pkPsncl.trim();
    }

    public String getPsnclcode() {
        return psnclcode;
    }

    public void setPsnclcode(String psnclcode) {
        this.psnclcode = psnclcode == null ? null : psnclcode.trim();
    }

    public String getPsnclcname() {
        return psnclcname;
    }

    public void setPsnclcname(String psnclcname) {
        this.psnclcname = psnclcname == null ? null : psnclcname.trim();
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname == null ? null : shortname.trim();
    }

    public String getJobglbdef17() {
        return jobglbdef17;
    }

    public void setJobglbdef17(String jobglbdef17) {
        this.jobglbdef17 = jobglbdef17 == null ? null : jobglbdef17.trim();
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

   

    public String getSecretEmail() {
        return secretEmail;
    }

    public void setSecretEmail(String secretEmail) {
        this.secretEmail = secretEmail == null ? null : secretEmail.trim();
    }

    public String getOfficephone() {
        return officephone;
    }

    public void setOfficephone(String officephone) {
        this.officephone = officephone == null ? null : officephone.trim();
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

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode == null ? null : deptcode.trim();
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname == null ? null : deptname.trim();
    }

    public String getDepttopcode() {
        return depttopcode;
    }

    public void setDepttopcode(String depttopcode) {
        this.depttopcode = depttopcode == null ? null : depttopcode.trim();
    }

    public String getDepttopname() {
        return depttopname;
    }

    public void setDepttopname(String depttopname) {
        this.depttopname = depttopname == null ? null : depttopname.trim();
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode == null ? null : citycode.trim();
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode == null ? null : postcode.trim();
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname == null ? null : postname.trim();
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode == null ? null : bankcode.trim();
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname == null ? null : bankname.trim();
    }

    public String getBanktypecode() {
        return banktypecode;
    }

    public void setBanktypecode(String banktypecode) {
        this.banktypecode = banktypecode == null ? null : banktypecode.trim();
    }

    public String getBanktypename() {
        return banktypename;
    }

    public void setBanktypename(String banktypename) {
        this.banktypename = banktypename == null ? null : banktypename.trim();
    }

    public String getIsmainjob() {
        return ismainjob;
    }

    public void setIsmainjob(String ismainjob) {
        this.ismainjob = ismainjob == null ? null : ismainjob.trim();
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
        this.sex = sex == null ? null : sex.trim();
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype == null ? null : idtype.trim();
    }

    public String getPkPost() {
        return pkPost;
    }

    public void setPkPost(String pkPost) {
        this.pkPost = pkPost == null ? null : pkPost.trim();
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

    public String getPoststat() {
        return poststat;
    }

    public void setPoststat(String poststat) {
        this.poststat = poststat == null ? null : poststat.trim();
    }

    public String getHiendflag() {
        return hiendflag;
    }

    public void setHiendflag(String hiendflag) {
        this.hiendflag = hiendflag == null ? null : hiendflag.trim();
    }

    public String getPkPsnjob() {
        return pkPsnjob;
    }

    public void setPkPsnjob(String pkPsnjob) {
        this.pkPsnjob = pkPsnjob == null ? null : pkPsnjob.trim();
    }

    public String getPkBaspost() {
        return pkBaspost;
    }

    public void setPkBaspost(String pkBaspost) {
        this.pkBaspost = pkBaspost == null ? null : pkBaspost.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getOrgendflag() {
        return orgendflag;
    }

    public void setOrgendflag(String orgendflag) {
        this.orgendflag = orgendflag == null ? null : orgendflag.trim();
    }

    public String getPkDeptpsn() {
        return pkDeptpsn;
    }

    public void setPkDeptpsn(String pkDeptpsn) {
        this.pkDeptpsn = pkDeptpsn == null ? null : pkDeptpsn.trim();
    }

    public String getDeptpsncode() {
        return deptpsncode;
    }

    public void setDeptpsncode(String deptpsncode) {
        this.deptpsncode = deptpsncode == null ? null : deptpsncode.trim();
    }

    public String getDeptpsnname() {
        return deptpsnname;
    }

    public void setDeptpsnname(String deptpsnname) {
        this.deptpsnname = deptpsnname == null ? null : deptpsnname.trim();
    }

    public String getPkPsndoc() {
        return pkPsndoc;
    }

    public void setPkPsndoc(String pkPsndoc) {
        this.pkPsndoc = pkPsndoc == null ? null : pkPsndoc.trim();
    }

    public String getOnedeptcode() {
        return onedeptcode;
    }

    public void setOnedeptcode(String onedeptcode) {
        this.onedeptcode = onedeptcode == null ? null : onedeptcode.trim();
    }

    public String getOnedeptname() {
        return onedeptname;
    }

    public void setOnedeptname(String onedeptname) {
        this.onedeptname = onedeptname == null ? null : onedeptname.trim();
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname == null ? null : cityname.trim();
    }

    public String getJobendflag() {
        return jobendflag;
    }

    public void setJobendflag(String jobendflag) {
        this.jobendflag = jobendflag == null ? null : jobendflag.trim();
    }

    public String getJoblastflag() {
        return joblastflag;
    }

    public void setJoblastflag(String joblastflag) {
        this.joblastflag = joblastflag == null ? null : joblastflag.trim();
    }

    public String getOrglastflag() {
        return orglastflag;
    }

    public void setOrglastflag(String orglastflag) {
        this.orglastflag = orglastflag == null ? null : orglastflag.trim();
    }

 

    public String getJobglbdef10() {
        return jobglbdef10;
    }

    public void setJobglbdef10(String jobglbdef10) {
        this.jobglbdef10 = jobglbdef10 == null ? null : jobglbdef10.trim();
    }
}