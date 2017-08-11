package com.chinamobo.ue.activity.restful;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.dto.TomTaskPackageDto;
import com.chinamobo.ue.activity.entity.TomTaskCoursesRelation;
import com.chinamobo.ue.activity.service.TaskPackageService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import jersey.repackaged.com.google.common.collect.Lists;


/**
 * 版本: [1.0]
 * 功能说明: 任务包
 *
 * 作者: WangLg
 * 创建时间: 2016年3月10日 上午10:05:27
 */
@Path("/taskPackage") 
@Scope("request") 
@Component
public class TaskPackRest{/// extends RestController
	
	private static Logger logger = LoggerFactory.getLogger(TaskPackRest.class);
	
	@Autowired
	private TaskPackageService taskPackageService;
	
	@GET
	@Path("/view")
	public void view(@Context HttpServletResponse response,@Context HttpServletRequest request){
		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/activity/add_taskpackage.html");
		try {
			dispatcher .forward(request, response);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
	}
	
	public static TomTaskPackageDto getBeans(TomTaskPackageDto activityDto){
		activityDto.setPackageId(1001);
		activityDto.setPackageNumber("TD11111");
		activityDto.setPackageName("任务包1");
		
		TomTaskCoursesRelation taskcourse = new TomTaskCoursesRelation();
		taskcourse.setPackageId(1001);
		taskcourse.setCourseId(1001);
		
		List<Integer> taskcourseList = Lists.newArrayList();
		taskcourseList.add(1001);
		
		List<Integer> taskexamList = Lists.newArrayList();
		taskexamList.add(1001);

		activityDto.setTaskCoursesId(taskexamList);
		activityDto.setTaskExamId(taskexamList);
		return activityDto;
	}
	
	/**
	 * 功能描述：[添加任务包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:44:35
	 * @param activity
	 * @param response
	 * @return
	 */
	//@POST
	@POST
	@Path("/add")
	public String addTask(@BeanParam TomTaskPackageDto tomTaskPackageDto,@Context HttpServletResponse response){//
		try{

			DBContextHolder.setDbType(DBContextHolder.MASTER);
			taskPackageService.addTask(tomTaskPackageDto);
			return "{\"result\":\"success\"}";
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 功能描述：[修改任务包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:52:20
	 * @param activity
	 * @param response
	 * @return
	 */
	@POST
	@Path("/update")
	public String updateTask(@BeanParam TomTaskPackageDto tomTaskPackageDto){
		try{
			String status=taskPackageService.updateTask(tomTaskPackageDto);
			if(status.equals("protected")){
				return "{\"result\":\"protected\"}";
			}
			return "{\"result\":\"success\"}";
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 功能描述：[删除任务包]
	 *
	 * 创建者：Wanghb
	 * 创建时间: 2016年4月19日 
	 * @param activity
	 * @param response
	 * @return
	 */
	@DELETE
	@Path("/delete")
	public String deleteTask(@QueryParam(value = "packageId") int packageId,@Context HttpServletResponse response){
		try {

			DBContextHolder.setDbType(DBContextHolder.MASTER);
			String status=taskPackageService.deleteActivity(packageId);
			if(status.equals("protected")){
				return "{\"result\":\"protected\"}";
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	
	/**
	 * 功能描述：[查询任务包明细]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:58:20
	 * @param activity
	 * @param response
	 * @return
	 * @throws JsonProcessingException 
	 */
	@GET
	@Path("/findDetails")
	@Produces({MediaType.APPLICATION_JSON})
	public String findTaskDetails(@QueryParam(value = "packageId") int packageId) throws JsonProcessingException{
		JsonMapper jsonMapper = new JsonMapper();
		String json= jsonMapper.toJson(taskPackageService.findTaskDetails(packageId));
		if(json!=null){
			return json;
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 功能描述：[查询任务包列表]
	 *
	 * 创建者：Wanghb
	 * 创建时间: 2016年4月19日 
	 * @param activity
	 * @param response
	 * @return
	 * @throws JsonProcessingException 
	 */
	@GET
	@Path("/findTaskList")
	@Produces({MediaType.APPLICATION_JSON})
	public String findTaskList (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("packageName") String packageName,@Context HttpServletResponse response) throws Exception{

		JsonMapper jsonMapper = new JsonMapper();
		String json;
			json= jsonMapper.toJson(taskPackageService.selectListByParam(pageNum, pageSize, packageName));
//			System.out.println(json);
		return json;
	}
	/**
	 * 功能描述：[查询添加活动页面的任务包列表]
	 *
	 * 创建者：Wanghb
	 * 创建时间: 2016年4月19日 
	 * @param activity
	 * @param response
	 * @return
	 * @throws JsonProcessingException 
	 */
	@GET
	@Path("/findActivityTaskList")
	@Produces({MediaType.APPLICATION_JSON})
	public String findActivityTaskList (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("packageName") String packageName,@Context HttpServletResponse response) throws Exception{

		JsonMapper jsonMapper = new JsonMapper();
		String json;
			json= jsonMapper.toJson(taskPackageService.selectActivityTaskListByParam(pageNum, pageSize, packageName));
//			System.out.println(json);
		return json;
	}
}
