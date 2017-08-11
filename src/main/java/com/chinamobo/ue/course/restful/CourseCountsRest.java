package com.chinamobo.ue.course.restful;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.course.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 版本: [1.0]
 * 功能说明: 课程统计
 *
 * 作者: WangLg
 * 创建时间: 2016年3月9日 上午10:00:46
 */

@Path("/courseCounts") 
@Scope("request") 
@Component
public class CourseCountsRest {
	@Autowired
	private CourseService courseService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	/**
	 * 
	 * 功能描述：[通过id获取讲师]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:54:09
	 * @param lecturerId
	 * @param response
	 * @param request
	 * @return
	 */
	@GET
	@Path("/find")
	public String courseCountsList(@QueryParam("courseName") String courseName,@Context HttpServletResponse response){	
			courseService.courseCountsList(courseName);
			return "";
	}

}
