package com.chinamobo.ue.activity.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.ums.util.ShiroUtils;


/**
 * 版本: [1.0]
 * 功能说明: 任务包
 *
 * 作者: WangLg
 * 创建时间: 2016年3月16日 上午10:14:53
 */
public class TomTaskPackageDto {//extends DataEntity<TomTaskPackage>{
	
	private	List<Object> objectList;
	
	private List<Map<String,String>> adminMapList;
	private List<String> adminNames;
	private List<String> courseName;
	private List<String> examName;
	private List<Integer> courseId; 
	private Integer courseIds;
	private Integer examIds;
	private List taskCoursesOrExamPapers;
	private String courseNames;
	private String examNames;
	
	public List<Object> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Object> objectList) {
		this.objectList = objectList;
	}

	public List<Map<String, String>> getAdminMapList() {
		return adminMapList;
	}

	public void setAdminMapList(List<Map<String, String>> adminMapList) {
		this.adminMapList = adminMapList;
	}

	public String getCourseNames() {
		return courseNames;
	}

	public void setCourseNames(String courseNames) {
		this.courseNames = courseNames;
	}

	public String getExamNames() {
		return examNames;
	}

	public void setExamNames(String examNames) {
		this.examNames = examNames;
	}

	public Integer getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(Integer courseIds) {
		this.courseIds = courseIds;
	}

	public Integer getExamIds() {
		return examIds;
	}

	public void setExamIds(Integer examIds) {
		this.examIds = examIds;
	}

	public List<String> getAdminNames() {
		return adminNames;
	}

	public void setAdminNames(List<String> adminNames) {
		this.adminNames = adminNames;
	}

	public List<String> getCourseName() {
		return courseName;
	}

	public void setCourseName(List<String> courseName) {
		this.courseName = courseName;
	}

	public List<String> getExamName() {
		return examName;
	}

	public void setExamName(List<String> examName) {
		this.examName = examName;
	}

	public List<Integer> getCourseId() {
		return courseId;
	}

	public void setCourseId(List<Integer> courseId) {
		this.courseId = courseId;
	}
	private String adminId;
	
	private String adminsName;
	
	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminsName() {
		return adminsName;
	}

	public void setAdminsName(String adminsName) {
		this.adminsName = adminsName;
	}
	
	/*@FormParam("adminsNames[]")
	private List<String> adminsNames;*/
	
	/*@FormParam("admins[]")
	private List<String> admins;*/
	
	@FormParam("packageId")
    private Integer packageId;
    
	@FormParam("packageNumber[]")
    private String packageNumber;

	@FormParam("packageName")
	private String packageName;

	@FormParam("status")
	private String status;

	@FormParam("creator")
	private String creator;

//	@FormParam("createTime")
	private Date createTime;

	@FormParam("operator")
	private String operator;

//	@FormParam("updateTime")
	private Date updateTime;
	
	//添加任务包时传的课程id集合
	@FormParam("coursesId[]")
	private List<Integer> coursesId;
	//添加任务包时传的考试试卷id集合
	@FormParam("examPaperId[]")
	private List<Integer> examPaperId;
	//添加任务包时传的管理员id
	@FormParam("admins")
	private String admins;
	
	@FormParam("examId[]")
	private List<Integer> examId;
	
	public List<Integer> getCoursesId() {
		return coursesId;
	}

	public void setCoursesId(List<Integer> coursesId) {
		this.coursesId = coursesId;
	}

	public List<Integer> getExamId() {
		return examId;
	}

	public void setExamId(List<Integer> examId) {
		this.examId = examId;
	}
	
	@FormParam("taskCoursesId[]")
	private List<Integer> taskCoursesId;
	
	
	@FormParam("taskExamId[]")
	private List<Integer> taskExamId;
	@FormParam("taskCount[]")
    private Integer taskCount;

	public List<Integer> getTaskCoursesId() {
		return taskCoursesId;
	}

	public void setTaskCoursesId(List<Integer> taskCoursesId) {
		this.taskCoursesId = taskCoursesId;
	}

	public List<Integer> getTaskExamId() {
		return taskExamId;
	}

	public void setTaskExamId(List<Integer> taskExamId) {
		this.taskExamId = taskExamId;
	}

	/**
	 * 插入前调用
	 */
	public void preInsert(){
	
		String user  = ShiroUtils.getCurrentUser().getName();
		this.operator = user;
		this.creator = user;
		this.updateTime = new Date();
		this.createTime = new Date();
		//this.activityNumber = numberRecordService.getNumber(MapManager.numberType(Global.RWB));
		
	}
	
	/**
	 * 插入前调用
	 */
	public void preInserts(){
	
		String user  = ShiroUtils.getCurrentUser().getName();
		this.operator = user;
		this.creator = user;
		this.updateTime = new Date();
		this.createTime = new Date();
		//this.activityNumber = numberRecordService.getNumber(MapManager.numberType(Global.RWB));
	
	}
	
	/**
	 * 插入后调用
	 */
	public void preUpdate(){
		String user  = ShiroUtils.getCurrentUser().getName();
		this.operator = user;
		this.updateTime = new Date();
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
		this.operator = operator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/*public List<String> getAdminsNames() {
		return adminsNames;
	}

	public void setAdminsNames(List<String> adminsNames) {
		this.adminsNames = adminsNames;
	}*/

	/*public List<String> getAdmins() {
		return admins;
	}

	public void setAdmins(List<String> admins) {
		this.admins = admins;
	}*/

	public List<Integer> getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(List<Integer> examPaperId) {
		this.examPaperId = examPaperId;
	}

	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public List getTaskCoursesOrExamPapers() {
		return taskCoursesOrExamPapers;
	}

	public void setTaskCoursesOrExamPapers(List taskCoursesOrExamPapers) {
		this.taskCoursesOrExamPapers = taskCoursesOrExamPapers;
	}

	public Integer getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(Integer taskCount) {
		this.taskCount = taskCount;
	}
	
}