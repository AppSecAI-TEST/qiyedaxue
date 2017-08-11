package com.chinamobo.ue.course.restful;

import java.util.Date;

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

import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.course.service.LecturerService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 讲师管理rest
 *
 * 作者: JCX
 * 创建时间: 2016年3月1日 上午9:52:48
 */
@Path("/lecturer") 
@Scope("request") 
@Component
public class LecturerRest {
	@Autowired
	private LecturerService lecturerService;

	ObjectMapper mapper = new ObjectMapper(); 
	
	ShiroUser user=ShiroUtils.getCurrentUser();
	/**
	 * 
	 * 功能描述：[添加]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月9日 上午11:20:10
	 * @param lecturer
	 * @return
	 */
	@POST
	@Path("/add")
	public String addLecturer(@BeanParam TomLecturer lecturer){
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		lecturer.setCreator(user.getName());
		lecturer.setCreatorId(user.getCode());
		lecturer.setOperator(user.getName());
		try {
			lecturerService.addLecturer(lecturer);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[通过id获取讲师]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:54:09
	 * @param lecturerId
	 * @return
	 */
	@GET
	@Path("/find")
	public String findLecturer (@QueryParam("lecturerId") int lecturerId){	
		try {
			String json= mapper.writeValueAsString(lecturerService.selectLecturerById(lecturerId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}	
		return "{\"result\":\"error\"}";
	}
	
	//@Produces(MediaType.TEXT_XML) @Produces(MediaType.APPLICATION_JSON)
	//application/x-www-form-urlencoded
	/**
	 * 
	 * 功能描述：[按条件分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:55:07
	 * @param pageNum
	 * @param pageSize
	 * @param lecturerName
	 * @return
	 */
	@GET
	@Path("/findPage")
	public String findLecturerByPage2 (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("lecturerName") String lecturerName){

		try {
			String json;
			json= mapper.writeValueAsString(lecturerService.selectListByPage(pageNum, pageSize, lecturerName));
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[修改]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月9日 上午11:18:32
	 * @param lecturer
	 * @return
	 */
	@PUT
	@Path("/edit")
	public String editLecturer(@BeanParam TomLecturer lecturer){
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		lecturer.setOperator(user.getName());
		lecturerService.updateLecturer(lecturer);
		return "{\"result\":\"success\"}";
		
	}
	
	/**
	 * 
	 * 功能描述：[禁用/启用]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:55:22
	 * @param lecturerId
	 * @return
	 */
	@DELETE
	@Path("/delete")
	public String deleteLecturer (@QueryParam("lecturerId") int lecturerId,@QueryParam("status") String status){
		TomLecturer lecturer=new TomLecturer();
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		lecturer.setLecturerId(lecturerId);
		lecturer.setOperator(user.getName());
		lecturer.setStatus(status);
		lecturer.setUpdateTime(new Date());
		lecturerService.updateStatus(lecturer);
		
		return "{\"result\":\"success\"}";
	}
}
