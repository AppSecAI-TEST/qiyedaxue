package com.chinamobo.ue.api.course.dto;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.entity.TomCourses;

public class MyCourse {
	private Integer courseCount0;
	
	private Integer courseCount1;
	
	private Integer courseCount2;
	
	private PageData<TomCourses> courseList;

	public Integer getCourseCount0() {
		return courseCount0;
	}

	public void setCourseCount0(Integer courseCount0) {
		this.courseCount0 = courseCount0;
	}

	public Integer getCourseCount1() {
		return courseCount1;
	}

	public void setCourseCount1(Integer courseCount1) {
		this.courseCount1 = courseCount1;
	}

	public Integer getCourseCount2() {
		return courseCount2;
	}

	public void setCourseCount2(Integer courseCount2) {
		this.courseCount2 = courseCount2;
	}

	public PageData<TomCourses> getCourseList() {
		return courseList;
	}

	public void setCourseList(PageData<TomCourses> courseList) {
		this.courseList = courseList;
	}
	
	
}
