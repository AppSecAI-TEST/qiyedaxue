package com.chinamobo.ue.course.restful;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.course.dao.TomCourseClassifyRelationMapper;
import com.chinamobo.ue.course.entity.TomCourseClassifyRelation;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.system.dao.TomRollingPictureMapper;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程rest
 *
 * 作者: JCX
 * 创建时间: 2016年3月3日 下午3:41:04
 */
@Path("/course") 
@Scope("request") 
@Component
public class CourseRest {

	private static Logger logger = LoggerFactory.getLogger(CourseRest.class);
	@Autowired
	private CourseService courseService;
	@Autowired
	private TomCourseClassifyRelationMapper courseClassifyRelationMapper;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	@Autowired
    private TomRollingPictureMapper rollingPictureMapper;
	
	ObjectMapper mapper = new ObjectMapper(); 
	ShiroUser user=ShiroUtils.getCurrentUser();
	/**
	 * 
	 * 功能描述：[查询课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:41:20
	 * @param courseId
	 * @return
	 */
	@GET
	@Path("/find")
	public String findCourse (@QueryParam("courseId") int courseId){	
		try {
			String json= mapper.writeValueAsString(courseService.selectCourseDetail(courseId));
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return "{\"result\":\"error\"}";
	}
	
	@GET
	@Path("/test")
	public String findCourse1 (@QueryParam("courseId") int courseId){	
		TomCourseClassifyRelation classifyRelationExample=new TomCourseClassifyRelation();
		classifyRelationExample.setCourseId(courseId);
		courseClassifyRelationMapper.deleteByExample(classifyRelationExample);
		return "ok";
	}
	
	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:41:35
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @param courseType
	 * @param courseOnline
	 * @param status
	 * @return
	 */
	@GET
	@Path("/findPage")
	public String findCourseByPage (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("courseName") String courseName,@QueryParam("courseType") String courseType,
			@QueryParam("courseOnline") String courseOnline,@QueryParam("lecturers") String lecturers,@QueryParam("status") String status){

		try {
//			if(courseName!=null){
//				courseName=courseName.replaceAll("/", "//");
//				courseName=courseName.replaceAll("%", "/%");
//				courseName=courseName.replaceAll("_", "/_");
//				
//			}
			
			TomCourses example=new TomCourses();
			example.setLecturers(lecturers);
			example.setCourseName(courseName);
			example.setCourseType(courseType);
			example.setCourseOnline(courseOnline);
			example.setStatus(status);
			
			String json= mapper.writeValueAsString(courseService.selectListByPage(pageNum, pageSize,example));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}	
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[添加]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月8日 下午3:27:46
	 * @param courses
	 * @return
	 */
	@POST
	@Path("/add")
	public String addCourse(String json){
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try {
			courseService.addCourseNew(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 
	 * 功能描述：[修改]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月8日 下午3:28:08
	 * @param courses
	 * @return
	 */
	@POST
	@Path("/edit")
	public String editCourse(String json){
		JSONObject jsonObject=JSONObject.fromObject(json);
		TomActivityProperty example=new TomActivityProperty();
		example.setCourseId(jsonObject.getInt("courseId"));
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(activityPropertyMapper.selectByExample(example).size()>0){
			return "{\"result\":\"inActivity\"}";
		}
		try {
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			courseService.updateCourseNew(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 
	 * 功能描述：[上架/下架]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:42:15
	 * @param courseId
	 * @param operator
	 * @param status
	 * @return
	 */
	@PUT
	@Path("/updateStatus")
	public String downShelf (@QueryParam("courseId") int courseId,@QueryParam("status") String status){
		//是否在活动中
		TomActivityProperty example=new TomActivityProperty();
		example.setCourseId(courseId);
		if(activityPropertyMapper.selectByExample(example).size()>0){
			return "{\"result\":\"inActivity\"}";
		}
		//是否在轮播图中
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("resourceId", courseId);
		map.put("pictureCategory", "C");
		map.put("isRelease", "Y");
		int count=rollingPictureMapper.countByPage(map);
		if(count!=0){
			return "{\"result\":\"protected\"}";
		}
		TomCourses course=new TomCourses();	
		course.setCourseId(courseId);
		course.setStatus(status);	
		course.setOperator(user.getName());
		courseService.updateStatus(course);
		
		return "{\"result\":\"success\"}";
	}
}
