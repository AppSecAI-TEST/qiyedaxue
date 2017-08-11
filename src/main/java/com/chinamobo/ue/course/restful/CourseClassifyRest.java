package com.chinamobo.ue.course.restful;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.service.CourseClassifyService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程分类rest
 *
 * 作者: JCX
 * 创建时间: 2016年3月1日 上午10:02:00
 */
@Path("/courseClassify") 
@Scope("request") 
@Component
public class CourseClassifyRest {
	
	@Autowired
	private CourseClassifyService courseClassifyService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	ShiroUser user=ShiroUtils.getCurrentUser();
	/**
	 * 
	 * 功能描述：[添加]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月9日 上午11:29:39
	 * @param courseClassify
	 * @return
	 */
	@POST
	@Path("/add")
	public String addCourseClassify(@BeanParam TomCourseClassify courseClassify){
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
			courseClassify.setCreator(user.getName());
			courseClassify.setCreatorId(user.getCode());
			courseClassify.setOperator(user.getName());
			courseClassifyService.addCourseClassify(courseClassify);
			return "success";	
	}
	
	/**
	 * 
	 * 功能描述：[按id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午1:15:50
	 * @param classifyId
	 * @return
	 */
	@GET
	@Path("/find")
	public String findCourseClassify (@QueryParam("classifyId") int classifyId){	
		
		try {
			return mapper.writeValueAsString(courseClassifyService.selectById(classifyId));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午1:16:41
	 * @param pageNum
	 * @param pageSize
	 * @param classifyName
	 * @param classifyId
	 * @return
	 */
	@GET
	@Path("/findPage")
	public String findByPage (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("classifyName") String classifyName,@DefaultValue("0") @QueryParam("classifyId")int classifyId){
		
		try {
			String json = mapper.writeValueAsString(courseClassifyService.selectByPage(pageNum, pageSize,classifyName,classifyId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[修改]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月9日 上午11:32:44
	 * @param courseClassify
	 * @return
	 */
	@PUT
	@Path("/edit")
	public String editCourseClassify(@BeanParam TomCourseClassify courseClassify){
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		courseClassify.setOperator(user.getName());
		courseClassifyService.updateCourseClassify(courseClassify);
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 
	 * 功能描述：[删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午1:17:11
	 * @param classifyId
	 * @return
	 */
	@DELETE
	@Path("/delete")
	public String deleteCourseClassify (@QueryParam("classifyId") int classifyId){
		try {
			//设置主库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			TomCourseClassify courseClassify=new TomCourseClassify();
//			courseClassify.setOperator(user.getName());
			courseClassify.setClassifyId(classifyId);	
			String status=courseClassifyService.updateStatus(courseClassify);
			return "{\"result\":\""+status+"\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[查询一级分类]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月8日 下午3:37:39
	 * @return
	 */
	@GET
	@Path("/findHighest")
	public String findHighest(){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomCourseClassify> list=courseClassifyService.selectByParentClassifyId(0);
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[获取课程分类树形菜单]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月12日 上午10:33:33
	 * @return
	 */
	@GET
	@Path("/tree")
	public String getTree(){
		Tree tree=courseClassifyService.getClassifyTree();
		String json;
		try {
			json = mapper.writeValueAsString(tree);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
}
