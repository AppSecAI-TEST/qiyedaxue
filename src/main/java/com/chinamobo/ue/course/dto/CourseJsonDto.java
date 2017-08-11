package com.chinamobo.ue.course.dto;

import java.util.List;

import com.chinamobo.ue.course.entity.TomCourseSection;
import com.chinamobo.ue.course.entity.TomCourses;

public class CourseJsonDto extends TomCourses{
 
	private List<TomCourseSection> sectionList;

	public List<TomCourseSection> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<TomCourseSection> sectionList) {
		this.sectionList = sectionList;
	}
}
