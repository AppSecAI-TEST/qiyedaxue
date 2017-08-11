package com.chinamobo.ue.statistics.entity;

public class TomExamEmployeeStatistics {
	//考试ID
	//员工代码
   //姓名
	//手机
	//邮箱
	//一级部门
	//当前部门
	//得分
	//通过 (Y合格 N不合格 P未审阅 D未考试)
	//补考次数
	private Integer examId;
	private String code;
	private String empName;
	private String mobile;
	private String email;
	private String onedeptname;
	private String deptname;
	private Integer totalPoints;
	private String  gradeState;
	private Integer examCount;
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOnedeptname() {
		return onedeptname;
	}
	public void setOnedeptname(String onedeptname) {
		this.onedeptname = onedeptname;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public Integer getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}
	

	public String getGradeState() {
		return gradeState;
	}
	public void setGradeState(String gradeState) {
		this.gradeState = gradeState;
	}
	public Integer getExamCount() {
		return examCount;
	}
	public void setExamCount(Integer examCount) {
		this.examCount = examCount;
	}
	
	
}
