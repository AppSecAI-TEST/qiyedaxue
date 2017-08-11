package com.chinamobo.ue.api.global.dto;




import com.chinamobo.ue.api.activity.dto.ActResults;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.entity.TomExam;

public class SerchResults {
  private PageData<TomCourses> coursrList;
  private  PageData<TomExam> examList;
  private PageData<ActResults> activityList;
public PageData<TomCourses> getCoursrList() {
	return coursrList;
}
public void setCoursrList(PageData<TomCourses> coursrList) {
	this.coursrList = coursrList;
}


public PageData<TomExam> getExamList() {
	return examList;
}
public void setExamList(PageData<TomExam> examList) {
	this.examList = examList;
}
public PageData<ActResults> getActivityList() {
	return activityList;
}
public void setActivityList(PageData<ActResults> activityList) {
	this.activityList = activityList;
}


}
