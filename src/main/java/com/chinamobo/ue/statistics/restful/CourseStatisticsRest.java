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

import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.statistics.entity.TomAttendanceStatistics;
import com.chinamobo.ue.statistics.entity.TomDetailedAttendanceStatistics;
import com.chinamobo.ue.statistics.entity.TomOpenCourseStatistic;
import com.chinamobo.ue.statistics.service.CourseStatisticsService;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/attendanceStatistics")
@Scope("request") 
@Component
public class CourseStatisticsRest {
	
	@Autowired
	private CourseStatisticsService attendanceStatisticsService;
	
	@Autowired
	private CourseService courseService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	/**
	 * 
	 * 功能描述：[活动内课程统计分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月30日
	 * @param pageNum
	 * @param pageSize
	 * @param attendanceStatistics
	 * @return
	 */
	@GET
	@Path("/attendanceStatisticsList")
	public String attendanceStatisticsList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName,
			@QueryParam("courseOnline") String courseOnline){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(attendanceStatisticsService.queryAttendanceStatistics(pageNum, pageSize, courseName,courseOnline));
	}
	
	/**
     * 
     * 功能描述：[生成课程统计并下载]
     *
     * 创建者：JCX
     * 创建时间: 2016年5月30日
     * @param
     * @return
     */
	@GET
    @Path("/downloadAttendanceStatisticsExcel")
	public Response downloadAttendanceStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName,
			@QueryParam("courseOnline") String courseOnline){
		try{
			List<TomAttendanceStatistics> attendanceStatistics = attendanceStatisticsService.queryAttendanceStatistics(pageNum, pageSize, courseName, courseOnline).getData();
			String fileName = "活动课程.xls";
			String path = attendanceStatisticsService.AttendanceStatisticsExcel(attendanceStatistics, fileName);
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
	 * 功能描述：[查看课程学习统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月2日
	 * @param pageNum
	 * @param pageSize
	 * @param
	 * @return
	 */
	@GET
	@Path("/seeAttendanceStatistics")
	public String seeAttendanceStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@QueryParam("courseId") int courseId){
		String json;
		try {
			json = mapper.writeValueAsString(attendanceStatisticsService.seeAttendanceStatistics(pageNum,pageSize,activityId,courseId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
     * 
     * 功能描述：[生成课程详细统计并下载]
     *
     * 创建者：GW
     * 创建时间: 2016年6月3日
     * @param
     * @return
     */
	@GET
    @Path("/downloadAttendanceStatisticsDetailedExcel")
	public Response downloadAttendanceStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@QueryParam("courseId") int courseId){
		try{
			List<TomDetailedAttendanceStatistics> detailedAttendanceStatistics = attendanceStatisticsService.seeAttendanceStatistics(pageNum,pageSize, activityId, courseId).getData();
			TomCourses courses = courseService.selectCourseById(courseId);
			String fileName = "《"+courses.getCourseName()+"》统计.xls";
			String path = attendanceStatisticsService.AttendanceStatisticsDetailedExcel(detailedAttendanceStatistics, fileName);
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
	 * 功能描述：[公开课统计]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 上午11:25:57
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @return
	 */
	@GET
	@Path("/openCourseStatisticsList")
	public String openCourseStatisticsList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(attendanceStatisticsService.openCourseStatistics(pageNum, pageSize, courseName));
	}
	
	/**
	 * 
	 * 功能描述：[公开课学习信息查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午12:28:12
	 * @param pageNum
	 * @param pageSize
	 * @param courseId
	 * @return
	 */
	@GET
	@Path("/openCourseLearnStatistics")
	public String openCourseLearnStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		String json;
		try {
			json = mapper.writeValueAsString(attendanceStatisticsService.openCourseLearnStatistics(pageNum,pageSize,courseId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[生成公开课统计并下载]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午12:50:20
	 * @return
	 */
	@GET
    @Path("/downloadOpenCourseExcel")
	public Response downloadOpenCourseExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName){
		try{
			List<TomOpenCourseStatistic> openCourseStatistics = attendanceStatisticsService.openCourseStatistics(pageNum, pageSize, courseName).getData();
			String fileName = "线上课程.xls";
			String path = attendanceStatisticsService.openCourseStatisticsExcel(openCourseStatistics, fileName);
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
	 * 功能描述：[公开课学习记录导出]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午1:18:02
	 * @param pageNum
	 * @param pageSize
	 * @param courseId
	 * @return
	 */
	@GET
    @Path("/downloadOpenCourseLearnExcel")
	public Response downloadOpenCourseLearnExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		try{
			List<TomDetailedAttendanceStatistics> detailedAttendanceStatistics = attendanceStatisticsService.openCourseLearnStatistics(pageNum, pageSize, courseId).getData();
			TomCourses courses = courseService.selectCourseById(courseId);
			String fileName = "《"+courses.getCourseName()+"》统计.xls";
			String path = attendanceStatisticsService.openCourseLearnExcel(detailedAttendanceStatistics, fileName);
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
