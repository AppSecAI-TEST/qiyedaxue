package com.chinamobo.ue.course.dto;

import java.util.Date;
import java.util.List;
import javax.ws.rs.FormParam;

import com.chinamobo.ue.course.entity.TomLecturer;


public class TomCoursesDto {
	
	@FormParam("lecturerLists[]")
	private List<TomLecturer> lecturerLists;
	@FormParam("courseId")
    private Integer courseId;

	@FormParam("courseNumber")
    private String courseNumber;

	@FormParam("courseName")
    private String courseName;

	@FormParam("courseOnline")
    private String courseOnline;

	@FormParam("courseIntroduction")
    private String courseIntroduction;

	@FormParam("courseType")
    private String courseType;

	@FormParam("eCurrency")
    private Integer eCurrency;

	@FormParam("courseGradeDimensions")
    private String courseGradeDimensions;

	@FormParam("lecturerGradeDimensions")
    private String lecturerGradeDimensions;

	@FormParam("lecturers")
    private String lecturers;

	@FormParam("courseImg")
    private String courseImg;

	@FormParam("courseDownloadable")
    private String courseDownloadable;

	@FormParam("isExcellentCourse")
    private String isExcellentCourse;

	@FormParam("status")
    private String status;
	
	@FormParam("openComment")
    private String openComment;

	@FormParam("openCourse")
    private String openCourse;

	@FormParam("admins")
    private String admins;

	@FormParam("adminNames")
    private String adminNames;
	@FormParam("viewers")
    private Integer viewers;

	@FormParam("thumbUpCount")
    private Integer thumbUpCount;

	@FormParam("commentCount")
    private Integer commentCount;

	@FormParam("ratetimes")
    private Integer ratetimes;

	@FormParam("courseAverage")
    private Double courseAverage;

	@FormParam("totScore")
    private Double totScore;

	@FormParam("lecturerAverage")
    private Double lecturerAverage;

	@FormParam("courseTimes")
    private Double courseTimes;

	@FormParam("signInTwoDimensionCode")
    private String signInTwoDimensionCode;

	@FormParam("gradeTwoDimensionCode")
    private String gradeTwoDimensionCode;

	@FormParam("creator")
    private String creator;
	
	private String creatorId;

    private Date createTime;

    private Integer numberOfParticipants;
	@FormParam("operator")
    private String operator;

    private Date updateTime;
	/**
	 * 讲师名称
	 */
	@FormParam("lecturerName")
    private String lecturerName;
	
	/**
	 * 讲师ID
	 */
	@FormParam("lecturerId")
    private String lecturerId;
	
	/**
	 * 点赞状态
	 */
	@FormParam("praise")
    private String praise;
	
	/**
	 * 收藏状态
	 */
	@FormParam("collect")
    private String collect;
	
	/**
	 * 当前登陆人Code
	 */
	private String code;

	private Integer learnedNum;
	
	@FormParam("sectionId[]")
	private List<Integer> sectionId;
	
	@FormParam("sectionNameList[]")
	private List<String> sectionNameList;
	
	@FormParam("sectionAddressList[]")
	private List<String> sectionAddressList;

	@FormParam("sectionType[]")
	private List<String> sectionType;
	
	@FormParam("sectionSize[]")
	private List<Integer> sectionSize;
	
	@FormParam("sectionSort[]")
	private List<Integer> sectionSort;
	
	@FormParam("empIds[]")
	private List<String> empIds;
	
	@FormParam("empNames[]")
	private List<String> empNames;
	
	@FormParam("classifyIds[]")
	private List<Integer> classifyIds;
	
	@FormParam("classifyNames[]")
	private List<String> classifyNames;
	
	@FormParam("IsCourse")
	private String IsCourse;
    public Integer getLearnedNum() {
		return learnedNum;
	}

	public void setLearnedNum(Integer learnedNum) {
		this.learnedNum = learnedNum;
	}

	public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber == null ? null : courseNumber.trim();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName == null ? null : courseName.trim();
    }

    public String getCourseOnline() {
        return courseOnline;
    }

    public void setCourseOnline(String courseOnline) {
        this.courseOnline = courseOnline == null ? null : courseOnline.trim();
    }

    public String getCourseIntroduction() {
        return courseIntroduction;
    }

    public void setCourseIntroduction(String courseIntroduction) {
        this.courseIntroduction = courseIntroduction == null ? null : courseIntroduction.trim();
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType == null ? null : courseType.trim();
    }

    public Integer geteCurrency() {
        return eCurrency;
    }

    public void seteCurrency(Integer eCurrency) {
        this.eCurrency = eCurrency;
    }

    public String getCourseGradeDimensions() {
        return courseGradeDimensions;
    }

    public void setCourseGradeDimensions(String courseGradeDimensions) {
        this.courseGradeDimensions = courseGradeDimensions == null ? null : courseGradeDimensions.trim();
    }

    public String getLecturerGradeDimensions() {
        return lecturerGradeDimensions;
    }

    public void setLecturerGradeDimensions(String lecturerGradeDimensions) {
        this.lecturerGradeDimensions = lecturerGradeDimensions == null ? null : lecturerGradeDimensions.trim();
    }

    public String getLecturers() {
        return lecturers;
    }

    public void setLecturers(String lecturers) {
        this.lecturers = lecturers == null ? null : lecturers.trim();
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg == null ? null : courseImg.trim();
    }

    public String getCourseDownloadable() {
        return courseDownloadable;
    }

    public void setCourseDownloadable(String courseDownloadable) {
        this.courseDownloadable = courseDownloadable == null ? null : courseDownloadable.trim();
    }

    public String getIsExcellentCourse() {
        return isExcellentCourse;
    }

    public void setIsExcellentCourse(String isExcellentCourse) {
        this.isExcellentCourse = isExcellentCourse == null ? null : isExcellentCourse.trim();
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getOpenComment() {
        return openComment;
    }
    public void setOpenComment(String openComment) {
        this.openComment = openComment == null ? null : openComment.trim();
    }
    public String getOpenCourse() {
        return openCourse;
    }

    public void setOpenCourse(String openCourse) {
        this.openCourse = openCourse == null ? null : openCourse.trim();
    }
    public String getAdmins() {
        return admins;
    }

    public void setAdmins(String admins) {
        this.admins = admins == null ? null : admins.trim();
    }

    public Integer getViewers() {
        return viewers;
    }

    public void setViewers(Integer viewers) {
        this.viewers = viewers;
    }
    public Integer getThumbUpCount() {
        return thumbUpCount;
    }

    public void setThumbUpCount(Integer thumbUpCount) {
        this.thumbUpCount = thumbUpCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
    public Integer getRatetimes() {
        return ratetimes;
    }

    public void setRatetimes(Integer ratetimes) {
        this.ratetimes = ratetimes;
    }

    public Double getCourseAverage() {
        return courseAverage;
    }

    public void setCourseAverage(Double courseAverage) {
        this.courseAverage = courseAverage;
    }
    public Double getTotScore() {
        return totScore;
    }

    public void setTotScore(Double totScore) {
        this.totScore = totScore;
    }

    public Double getLecturerAverage() {
        return lecturerAverage;
    }

    public void setLecturerAverage(Double lecturerAverage) {
        this.lecturerAverage = lecturerAverage;
    }

    public Double getCourseTimes() {
        return courseTimes;
    }
    public void setCourseTimes(Double courseTimes) {
        this.courseTimes = courseTimes;
    }

    public String getSignInTwoDimensionCode() {
        return signInTwoDimensionCode;
    }

    public void setSignInTwoDimensionCode(String signInTwoDimensionCode) {
        this.signInTwoDimensionCode = signInTwoDimensionCode == null ? null : signInTwoDimensionCode.trim();
    }

    public String getGradeTwoDimensionCode() {
        return gradeTwoDimensionCode;
    }

    public void setGradeTwoDimensionCode(String gradeTwoDimensionCode) {
        this.gradeTwoDimensionCode = gradeTwoDimensionCode == null ? null : gradeTwoDimensionCode.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

	public String getLecturerName() {
		return lecturerName;
	}

	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}

	public String getLecturerId() {
		return lecturerId;
	}

	public void setLecturerId(String lecturerId) {
		this.lecturerId = lecturerId;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public String getCollect() {
		return collect;
	}

	public void setCollect(String collect) {
		this.collect = collect;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Integer> getSectionId() {
		return sectionId;
	}

	public void setSectionId(List<Integer> sectionId) {
		this.sectionId = sectionId;
	}

	public List<String> getSectionNameList() {
		return sectionNameList;
	}

	public void setSectionNameList(List<String> sectionNameList) {
		this.sectionNameList = sectionNameList;
	}

	public List<String> getSectionAddressList() {
		return sectionAddressList;
	}

	public void setSectionAddressList(List<String> sectionAddressList) {
		this.sectionAddressList = sectionAddressList;
	}

	public List<String> getSectionType() {
		return sectionType;
	}

	public void setSectionType(List<String> sectionType) {
		this.sectionType = sectionType;
	}

	public List<String> getEmpIds() {
		return empIds;
	}

	public void setEmpIds(List<String> empIds) {
		this.empIds = empIds;
	}

	public List<String> getEmpNames() {
		return empNames;
	}

	public void setEmpNames(List<String> empNames) {
		this.empNames = empNames;
	}

	public String getAdminNames() {
		return adminNames;
	}

	public void setAdminNames(String adminNames) {
		this.adminNames = adminNames;
	}

	public List<Integer> getClassifyIds() {
		return classifyIds;
	}

	public void setClassifyIds(List<Integer> classifyIds) {
		this.classifyIds = classifyIds;
	}

	public List<String> getClassifyNames() {
		return classifyNames;
	}

	public void setClassifyNames(List<String> classifyNames) {
		this.classifyNames = classifyNames;
	}

	public List<Integer> getSectionSize() {
		return sectionSize;
	}

	public void setSectionSize(List<Integer> sectionSize) {
		this.sectionSize = sectionSize;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public void setNumberOfParticipants(Integer numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	public List<TomLecturer> getLecturerLists() {
		return lecturerLists;
	}

	public void setLecturerLists(List<TomLecturer> lecturerLists) {
		this.lecturerLists = lecturerLists;
	}

	public String getIsCourse() {
		return IsCourse;
	}

	public void setIsCourse(String isCourse) {
		IsCourse = isCourse;
	}

	public List<Integer> getSectionSort() {
		return sectionSort;
	}

	public void setSectionSort(List<Integer> sectionSort) {
		this.sectionSort = sectionSort;
	}
    
}