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
import com.chinamobo.ue.activity.entity.TomActivityFees;
import com.chinamobo.ue.activity.service.ActivityService;
import com.chinamobo.ue.statistics.entity.TomActivityCostDetailStatistics;
import com.chinamobo.ue.statistics.entity.TomActivityCostStatistics;
import com.chinamobo.ue.statistics.service.ActivityCostStatisticsService;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/feeStatistics")
@Scope("request")
@Component
public class ActivityCostStatisticsRest {
  
	@Autowired
	private ActivityCostStatisticsService costService;
	
	@Autowired
	private ActivityService activityService;
	
	ObjectMapper mapper=new ObjectMapper();
	
	/**
	 * 功能介绍：[活动费用统计列表]
	 * 创建人：LG
	 * 创建时间：2016年6月3日 19：50
	 * @param pageNum
	 * @param pageSize
	 * @param activityName
	 * @param feeStatistics
	 * @return
	 */
	@GET
	@Path("/findActityCostList")
	public String findActityCostList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityName") String activityName){
		JsonMapper jsonMapper = new JsonMapper();
		
		return jsonMapper.toJson(costService.findActityCostList(pageNum, pageSize, activityName));
	}
	/**
	 * 功能介绍：[导出活动费用统计列表]
	 * 创建人：LG
	 * 创建时间：2016年6月3日 19：50
	 * @param 
	 * @return excel表格
	 */
	@GET 
	@Path("/downloadFeeStatisticsExcel")
	public Response downloadFeeStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityName") String activityName){
		try {
		List<TomActivityCostStatistics> cost= costService.findActityCostList(pageNum, pageSize, activityName).getData();
		String fileName="费用统计.xls";
		String path=costService.downloadFeeStatisticsExcel(cost, fileName);
		File  file=new File(path);
		
		ResponseBuilder response=Response.ok((Object)file);
			String downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
			return response.build();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 功能介绍：[活动项目明细费用统计列表]
	 * 创建人：LG
	 * 创建时间：2016年6月6日 11：30
	 * @param 
	 * @return
	 */
	@GET
	@Path("/findActityCostDetailList")
	public String findActityCostDetailList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId){
		JsonMapper jsonMapper = new JsonMapper();
		
		return jsonMapper.toJson(costService.findActityCostDetailList(pageNum, pageSize, activityId));
	}
	
	/**
	 * 功能介绍：[导出活动项目费用统计列表]
	 * 创建人：LG
	 * 创建时间：2016年6月3日 19：50
	 * @param 
	 * @return excel表格
	 */
	@GET 
	@Path("/downloadFeeDetailStatisticsExcel")
	public Response downloadFeeDetailStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId){
		try {
		List<TomActivityCostDetailStatistics> costlist= costService.findActityCostDetailList(pageNum, pageSize, activityId).getData();
		TomActivityDto activity = activityService.findActivityDetails(activityId);
		String filename="《"+activity.getActivityName()+"》费用统计.xls";
		String path=costService.downloadFeeDetailStatisticsExcel(costlist, filename);
		File  file=new File(path);
		
		ResponseBuilder response=Response.ok((Object)file);
			String downFileName = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
			return response.build();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
