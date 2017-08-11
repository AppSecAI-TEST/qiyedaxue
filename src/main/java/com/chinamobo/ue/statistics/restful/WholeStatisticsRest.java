package com.chinamobo.ue.statistics.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.statistics.service.WholeStatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/wholeStatistics")
@Scope("request")
@Component
public class WholeStatisticsRest {
	
	@Autowired
	private WholeStatisticsService wholeStatisticsService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	@GET
	@Path("/getWholeStatistics")
	public String getWholeStatistics(@QueryParam("beginTime") String beginTime,@QueryParam("endTime") String endTime){
		try {
			
			return mapper.writeValueAsString(wholeStatisticsService.getWholeStatistics(beginTime, endTime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
}
