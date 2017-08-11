package com.chinamobo.ue.statistics.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.dto.TomActivityDto;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.service.ActivityService;
import com.chinamobo.ue.statistics.entity.TomActivityStatistics;
import com.chinamobo.ue.statistics.service.ActivityStatisticsService;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/activityStatistics")
@Scope("request") 
@Component
public class ActivityStatisticsRest {
	
	@Autowired
	private ActivityStatisticsService activityStatisticsService;
	
	@Autowired
	private ActivityService activityService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	/**
	 * 
	 * 功能描述：[活动统计分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月26日
	 * @param pageNum
	 * @param pageSize
	 * @param activityStatistics
	 * @return
	 */
	@GET
	@Path("/activityStatisticsList")
	public String activityStatisticsList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityName") String activityName){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(activityStatisticsService.queryActivityStatistics(pageNum, pageSize, activityName));
	}
	
	/**
     * 
     * 功能描述：[生成活动统计并下载]
     *
     * 创建者：GW
     * 创建时间: 2016年5月27日
     * @param
     * @return
     */
	@GET
    @Path("/downloadActivityStatisticsExcel")
	public Response downloadActivityStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityName") String activityName){
		try {
			List<TomActivityStatistics> activityStatistics = activityStatisticsService.queryActivityStatistics(pageNum, pageSize, activityName).getData();
			String fileName = "活动统计.xls";
			String path = activityStatisticsService.ActivityStatisticsExcel(activityStatistics, fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			String downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
	        return response.build();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * 
     * 功能描述：[查看活动统计]
     *
     * 创建者：GW
     * 创建时间: 2016年6月1日
     * @param
     * @return
     */
	@GET
	@Path("/seeActivityStatistics")
	public String seeActivityStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.seeActivityStatistics(pageNum,pageSize,activityId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
     * 
     * 功能描述：[生成活动详细统计并下载]
     *
     * 创建者：GW
     * 创建时间: 2016年6月1日
     * @param
     * @return
     */
	@GET
    @Path("/downloadActivityStatisticsDetailedExcel")
	public Response downloadActivityStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId){
		try{
			List<TomActivityEmpsRelation> activityEmpsRelation = activityStatisticsService.seeActivityStatistics(pageNum,pageSize, activityId).getData();
			TomActivityDto activity = activityService.findActivityDetails(activityId);
			String fileName = "《"+activity.getActivityName()+"》统计.xls";
			String path = activityStatisticsService.ActivityStatisticsDetailedExcel(activityEmpsRelation, fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			String downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
	        return response.build();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
}
