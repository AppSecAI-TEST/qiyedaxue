package com.chinamobo.ue.exam.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.service.TopicOptionService;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 题目选项rest
 *
 * 作者: JCX
 * 创建时间: 2016年4月5日 上午11:50:28
 */
@Path("/topicOption") 
@Scope("request") 
@Component
public class TopicOptionRest {
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	@Autowired
	private TopicOptionService topicOptionService;
	
	/**
	 * 
	 * 功能描述：[根据id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月5日 上午11:50:43
	 * @param id
	 * @return
	 */
	@GET
	@Path("/findOne")
	public String findOne(@QueryParam("id") int id){
		String json;
		try {
			json = mapper.writeValueAsString(topicOptionService.selectById(id));
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
		
	}
}
