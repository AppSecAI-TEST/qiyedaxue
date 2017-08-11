package com.chinamobo.ue.course.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.course.entity.TomCourseSection;
import com.chinamobo.ue.course.service.CourseSectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程章节
 *
 * 作者: JCX
 * 创建时间: 2016年3月31日 上午10:17:46
 */
@Path("/courseSection") 
@Scope("request") 
@Component
public class CourseSectionRest {

	@Autowired
	private CourseSectionService courseSectionService; 
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	@GET
	@Path("/findList")
	public String findCourseSection (@QueryParam("courseId") int courseId){	
		try {
			TomCourseSection courseSection=new TomCourseSection();
			courseSection.setCourseId(courseId);
			String json= mapper.writeValueAsString(courseSectionService.selectListByEXample(courseSection));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}	
		return "{\"result\":\"error\"}";
	}
}
