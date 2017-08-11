package com.chinamobo.ue.course.restful;
/**
 * 功能说明：测试Test
 * @author Acemon
 * 2016年11月16日
 */

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.course.dto.TomExportTestDto;
import com.chinamobo.ue.course.service.TestService;
import com.chinamobo.ue.ums.DBContextHolder;

@Path("/testa")
@Scope("request")
@Component
public class TestRest {
	private static Logger logger = LoggerFactory.getLogger(TestRest.class);
	@Autowired
	private TestService testService;
	
	@POST
	@Path("/addTest")
	public String addTest(String json){
		 
		try {
			//设置主库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			testService.addTest(json);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	
		return "{\"result\":\"success\"}";
		
	}
	
	
	@GET
	@Path("/exportTestExcel")
	public Response exportTestExcel(@QueryParam("courseId") int courseId){
		try {
			//设置从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
    		List<TomExportTestDto> exportExcel=testService.selectByCourseId(courseId);
    		
    		
    		String fileName="测试详情.xls";
	    	String path=testService.TopicsToExcel(exportExcel, fileName,courseId);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			
			String downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.header("Content-Type", "application/text");
	        response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
	        return response.build();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		return null;
		
	}

	
	
	
	
	

}
