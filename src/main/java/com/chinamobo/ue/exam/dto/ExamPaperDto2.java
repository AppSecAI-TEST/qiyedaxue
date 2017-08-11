package com.chinamobo.ue.exam.dto;

import java.util.Date;

import javax.ws.rs.FormParam;

public class ExamPaperDto2 {
	@FormParam("examPaperId")
    private Integer examPaperId;

	@FormParam("examPaperNumber")
	private String examPaperNumber;

	@FormParam("examPaperName")
    private String examPaperName;

	@FormParam("examPaperType")
    private String examPaperType;

	@FormParam("examPaperPicture")
    private String examPaperPicture;

	@FormParam("examTime")
    private Integer examTime;

	@FormParam("fullMark")
    private Integer fullMark;

	@FormParam("passMark")
    private Integer passMark;

	@FormParam("testQuestionCount")
    private Integer testQuestionCount;

	@FormParam("passEb")
    private Integer passEb;

	@FormParam("notPassEb")
    private Integer notPassEb;

	@FormParam("showScore")
    private String showScore;

	@FormParam("showQualifiedStandard")
    private String showQualifiedStandard;

	@FormParam("immediatelyShow")
    private String immediatelyShow;

	@FormParam("admins")
	private String admins;
	@FormParam("adminNames")
	private String adminNames;

	@FormParam("createTime")
    private Date createTime;

	@FormParam("creator")
    private String creator;
	
	private String creatorId;

	@FormParam("lastOperator")
    private String lastOperator;
	
	private String lastOperatorId;

	@FormParam("updateTime")
    private Date updateTime;
	@FormParam("IsExamPaper")
	private String IsExamPaper;
	
    public Integer getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(Integer examPaperId) {
        this.examPaperId = examPaperId;
    }

    public String getExamPaperNumber() {
        return examPaperNumber;
    }

    public void setExamPaperNumber(String examPaperNumber) {
        this.examPaperNumber = examPaperNumber == null ? null : examPaperNumber.trim();
    }

    public String getExamPaperName() {
        return examPaperName;
    }

    public void setExamPaperName(String examPaperName) {
        this.examPaperName = examPaperName == null ? null : examPaperName.trim();
    }

    public String getExamPaperType() {
        return examPaperType;
    }

    public void setExamPaperType(String examPaperType) {
        this.examPaperType = examPaperType == null ? null : examPaperType.trim();
    }

    public String getExamPaperPicture() {
        return examPaperPicture;
    }

    public void setExamPaperPicture(String examPaperPicture) {
        this.examPaperPicture = examPaperPicture == null ? null : examPaperPicture.trim();
    }
    public Integer getExamTime() {
        return examTime;
    }

    public void setExamTime(Integer examTime) {
        this.examTime = examTime;
    }

    public Integer getFullMark() {
        return fullMark;
    }

    public void setFullMark(Integer fullMark) {
        this.fullMark = fullMark;
    }

    public Integer getPassMark() {
        return passMark;
    }

    public void setPassMark(Integer passMark) {
        this.passMark = passMark;
    }

    public Integer getTestQuestionCount() {
        return testQuestionCount;
    }

    public void setTestQuestionCount(Integer testQuestionCount) {
        this.testQuestionCount = testQuestionCount;
    }

    public Integer getPassEb() {
        return passEb;
    }

    public void setPassEb(Integer passEb) {
        this.passEb = passEb;
    }

    public Integer getNotPassEb() {
        return notPassEb;
    }

    public void setNotPassEb(Integer notPassEb) {
        this.notPassEb = notPassEb;
    }

    public String getShowScore() {
        return showScore;
    }

    public void setShowScore(String showScore) {
        this.showScore = showScore == null ? null : showScore.trim();
    }

    public String getShowQualifiedStandard() {
        return showQualifiedStandard;
    }

    public void setShowQualifiedStandard(String showQualifiedStandard) {
        this.showQualifiedStandard = showQualifiedStandard == null ? null : showQualifiedStandard.trim();
    }

    public String getImmediatelyShow() {
        return immediatelyShow;
    }

    public void setImmediatelyShow(String immediatelyShow) {
        this.immediatelyShow = immediatelyShow == null ? null : immediatelyShow.trim();
    }

    

    public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getAdminNames() {
		return adminNames;
	}

	public void setAdminNames(String adminNames) {
		this.adminNames = adminNames;
	}

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getLastOperatorId() {
		return lastOperatorId;
	}

	public void setLastOperatorId(String lastOperatorId) {
		this.lastOperatorId = lastOperatorId;
	}

	public String getIsExamPaper() {
		return IsExamPaper;
	}

	public void setIsExamPaper(String isExamPaper) {
		IsExamPaper = isExamPaper;
	}
    
}