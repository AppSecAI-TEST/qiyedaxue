package com.chinamobo.ue.course.restful;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.entity.TomCourseComment;
import com.chinamobo.ue.course.service.CourseCommentService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程评论管理
 *
 * 作者: JCX
 * 创建时间: 2016年3月15日 下午2:26:19
 */
@Path("/courseComment") 
@Scope("request") 
@Component
public class CourseCommentRest {
	
	@Autowired
	private CourseCommentService courseCommentService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	ShiroUser user=ShiroUtils.getCurrentUser();
	/**
	 * 
	 * 功能描述：[分页查询课程评论]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月15日 下午2:54:54
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GET
	@Path("/findPage")
	public String findCourseCommentByPage (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("courseId") int courseId){

		try {
			PageData<TomCourseComment> page=courseCommentService.selectByPage(pageNum,pageSize,courseId);
			return mapper.writeValueAsString(page);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}	
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[开启/禁用评论]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月15日 下午3:54:58
	 * @param courseCommentId
	 * @param status
	 * @return
	 */
	@PUT
	@Path("/updateStatus")
	public String updateStatus (@QueryParam("courseCommentId") int courseCommentId,@QueryParam("status") String status){
		
		try {
			courseCommentService.update(courseCommentId,status);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[增加评论]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月15日 下午4:21:29
	 * @param courseComment
	 * @return
	 */
	@POST
	@Path("/add")
	public String addCourseComment(@BeanParam TomCourseComment courseComment){
		try {
			//设置主库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
//			courseComment.setCode(user.getCode());
//			courseComment.setName(user.getName());
			courseCommentService.addCourseComment(courseComment);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  "{\"result\":\"error\"}";
	}
}
