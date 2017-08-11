package com.chinamobo.ue.statistics.entity;

public class TomWholeStatistics {

	//活跃用户数、累计用户数、活跃率、累计课程数、累计播放次数、累计考试数、签到数
	private int activeUsers;//活跃用户数
	private int totalUsers;//累计用户数
	private String activeRate;//活跃率
	private int totalCourses;//累计课程数
	private int totalViews;//累计播放次数
	private int totalExams;//累计考试数
	private int totalSignIn;//签到数
	public int getActiveUsers() {
		return activeUsers;
	}
	public void setActiveUsers(int activeUsers) {
		this.activeUsers = activeUsers;
	}
	public int getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}
	public String getActiveRate() {
		return activeRate;
	}
	public void setActiveRate(String activeRate) {
		this.activeRate = activeRate;
	}
	public int getTotalCourses() {
		return totalCourses;
	}
	public void setTotalCourses(int totalCourses) {
		this.totalCourses = totalCourses;
	}
	public int getTotalViews() {
		return totalViews;
	}
	public void setTotalViews(int totalViews) {
		this.totalViews = totalViews;
	}
	public int getTotalExams() {
		return totalExams;
	}
	public void setTotalExams(int totalExams) {
		this.totalExams = totalExams;
	}
	public int getTotalSignIn() {
		return totalSignIn;
	}
	public void setTotalSignIn(int totalSignIn) {
		this.totalSignIn = totalSignIn;
	}
}
