package com.chinamobo.ue.activity.restful;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.chinamobo.ue.activity.dto.TomActivityDto;
import com.chinamobo.ue.activity.entity.TomActivityFeesKey;
import com.chinamobo.ue.activity.service.ActivityService;
import com.chinamobo.ue.system.service.DeptServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 版本: [1.0]
 * 功能说明: 培训活动
 *
 * 作者: WangLg
 * 创建时间: 2016年3月10日 上午9:45:10
 */
@Path("/activity") 
@Scope("request") 
@Component
public class ActivityRest {
	
	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private DeptServise deptServise;
	private static Logger logger = LoggerFactory.getLogger(ActivityRest.class);
	ObjectMapper mapper = new ObjectMapper();
	String basePath=Config.getProperty("file_path");
	
	@GET
	@Path("/view")
	public void view(@Context HttpServletResponse response,@Context HttpServletRequest request){
		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/activity/add_activity.html");
		try {
			dispatcher.forward(request, response);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
	}
	
	/**
	 * 功能描述：[添加培训活动包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:44:35
	 * @param activity
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Path("/add")
	public String addActivity(String jsonStr,@Context HttpServletResponse response){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			String status=activityService.addActivity(jsonStr);
			if(status.equals("protected")){
				return "{\"result\":\"protected\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\""+e.getMessage()+"\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	
	/**
	 * 功能描述：[修改培训活动包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:52:20
	 * @param activity
	 * @param response
	 * @return
	 */
	@POST
	@Path("/update")
	public String updateActivity(String jsonStr,@Context HttpServletResponse response){//@BeanParam TomActivityDto dto
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			String status=activityService.updateActivity(jsonStr);
			if(status.equals("protected")){
				return "{\"result\":\"protected\"}";
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 功能描述：[删除培训活动包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:52:09
	 * @param activity
	 * @param response
	 * @return
	 */
	@DELETE
	@Path("/delete")
	public String deleteActivity(@QueryParam(value = "activityId") int activityId,@Context HttpServletResponse response){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			String status= activityService.deleteActivity(activityId);
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
	 * 功能描述：[查询培训活动列表]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:59:16
	 * @param activity
	 * @param response
	 * @return
	 * @throws JsonProcessingException 
	 */
	@GET
	@Path("/findList")
	@Produces({MediaType.APPLICATION_JSON})
	public String findTaskList (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("activityName") String activityName){
		JsonMapper jsonMapper = new JsonMapper();
		String json;
		
			json= jsonMapper.toJson(activityService.selectListByParam(pageNum, pageSize, activityName));
			return json;
	}
	/**
	 * 功能描述：[查询培训活动列表]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:59:16
	 * @param activity
	 * @param response
	 * @return
	 * @throws JsonProcessingException 
	 */
	@GET
	@Path("/findDeptList")
	@Produces({MediaType.APPLICATION_JSON})
	public String findDeptList (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("name") String name){
		JsonMapper jsonMapper = new JsonMapper();
		String json;
			json= jsonMapper.toJson(deptServise.selectListByParam(pageNum, pageSize, name));
			return json;
	}
	
	/**
	 * 功能描述：[查询培训活动明细]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:58:20
	 * @param activity
	 * @param response
	 * @return
	 */
	@GET
	@Path("/findDetails")
	@Produces({MediaType.APPLICATION_JSON})
	public String findActivityDetails(@QueryParam(value = "activityId") int activityId){
		JsonMapper jsonMapper = new JsonMapper();
		String json;
		TomActivityDto tomActivityDto = activityService.findActivityDetails(activityId);
		json= jsonMapper.toJson(tomActivityDto);
		return json;
	}
	/**
	 * 
	 * 功能描述：[复制培训活动]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年6月20日 下午3:10:29
	 * @param activityId
	 * @return
	 */
	@GET
	@Path("/copyDetails")
	@Produces({MediaType.APPLICATION_JSON})
	public String copyActivityDetails(@QueryParam(value = "activityId") int activityId){
		JsonMapper jsonMapper = new JsonMapper();
		String json;
		TomActivityDto tomActivityDto = activityService.copyActDetails(activityId);
		tomActivityDto.setActivityEndTime(null);
		tomActivityDto.setActivityStartTime(null);
		json= jsonMapper.toJson(tomActivityDto);
		return json;
	}
	
	/**
	 * 功能描述：[查询费用统计-项目花费记录]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午10:59:52
	 * @param activity
	 * @param response
	 * @return
	 */
	@GET
	@Path("/findCost")
	@Produces({MediaType.APPLICATION_JSON})
	public String findActivityCost(@QueryParam(value = "activityId") int activityId,@Context HttpServletResponse response){
		JsonMapper jsonMapper = new JsonMapper();
		String json;
		json= jsonMapper.toJson(activityService.findActivityCostList(activityId));
		return json;
	}
	
	/**
	 * 功能描述：[添加项目花费记录]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月16日 下午2:27:07
	 * @param activity
	 * @param response
	 * @return
	 */
	@POST
	@Path("/addCost")
	public String addActivityCost(String jsonStr,@Context HttpServletResponse response){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			activityService.addActivityCost(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 功能描述：[删除项目花费记录]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月16日 下午2:46:09
	 * @param activityId
	 * @param response
	 * @return
	 */
	@DELETE
	@Path("/deleteCost")
	public String deleteActivityCost(@QueryParam(value = "activityId") int activityId,@QueryParam(value = "feeId") int feeId,@Context HttpServletResponse response){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomActivityFeesKey dto = new TomActivityFeesKey();
		dto.setFeeId(feeId);
		dto.setActivityId(activityId);
		activityService.deleteActivityCost(dto);
		return "{\"result\":\"success\"}";
	}
	/**
	 * 
	 * 功能描述：[分页查询员工]
	 *
	 * 创建者：cjx 创建时间: 2016年3月3日 下午2:20:27
	 * 
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/findEmpPage")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findEmpPage(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("name") String name,
			@QueryParam("empcode") String empcode,
			@QueryParam("statuss") String statuss, @QueryParam("cityname") String cityname,
			@QueryParam("taskState") String taskState, @QueryParam("packageId") String packageId,
			@QueryParam("sex") String sex, @QueryParam("code") String code,@QueryParam("country") String country,
			@QueryParam("begindate") String begindate,@Context HttpServletResponse response,@Context HttpServletRequest request) {
		try {
			if (statuss == null) {
				statuss = "1";
			}
			if ("1".equals(statuss)) {
				String json = mapper.writeValueAsString(activityService.selectByCode(pageNum, pageSize, code));
				return json;
			} else {
				String json = mapper.writeValueAsString(activityService.selectByPage(pageNum, pageSize, empcode,name, cityname, sex, code, statuss,taskState,packageId,country,begindate));
				return json;
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[导入活动人员]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月5日 下午5:16:03
	 * @param examId
	 * @param filePath
	 * @return
	 */
	@GET
	@Path("/importEmps")
	public String importEmps(@QueryParam("filePath") String filePath){
		try {
			return activityService.importEmps(basePath+filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
}
