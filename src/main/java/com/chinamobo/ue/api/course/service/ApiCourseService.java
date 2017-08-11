package com.chinamobo.ue.api.course.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomCertificateManageMapper;
import com.chinamobo.ue.activity.dao.TomCertificateMapper;
import com.chinamobo.ue.activity.dto.TomCertificateDto;
import com.chinamobo.ue.activity.dto.TomCertificateManageDto;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.activity.service.CertificateManageService;
import com.chinamobo.ue.api.activity.service.ActivityApiService;
import com.chinamobo.ue.api.constant.ApiConstant;
import com.chinamobo.ue.api.course.dto.CourseSectionLearningRecord;
import com.chinamobo.ue.api.course.dto.CourseTeacherGradeResults;
import com.chinamobo.ue.api.course.dto.GradeResult;
import com.chinamobo.ue.api.course.dto.MyCourse;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.result.Resultc;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.cacher.TomCourseCacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.cache.redis.RedisCacheManager;
import com.chinamobo.ue.cache.redis.RedisGetData;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.dao.TomCourseCommentMapper;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSectionLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSectionMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomGradeDimensionMapper;
import com.chinamobo.ue.course.dao.TomGradeRecordsMapper;
import com.chinamobo.ue.course.dao.TomLearnTimeLogMapper;
import com.chinamobo.ue.course.dao.TomLecturerMapper;
import com.chinamobo.ue.course.dao.TomTestMapper;
import com.chinamobo.ue.course.dto.TomExportTestDto;
import com.chinamobo.ue.course.dto.TomTestDto;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourseComment;
import com.chinamobo.ue.course.entity.TomCourseCommentThumbUp;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSection;
import com.chinamobo.ue.course.entity.TomCourseSectionLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourseThumbUp;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomFavoriteCourse;
import com.chinamobo.ue.course.entity.TomGradeDimension;
import com.chinamobo.ue.course.entity.TomGradeRecords;
import com.chinamobo.ue.course.entity.TomLearnTimeLog;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.course.entity.TomTest;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程接口实现
 *
 * 作者: ZXM
 * 创建时间: 2016年3月16日 下午4:36:48
 */
@Service
public class ApiCourseService {
	
	@Autowired
	private TomCoursesMapper coursesMapper;
	
	@Autowired
	private TomCourseClassifyMapper CourseClassifyMapper;
	
	@Autowired
	private TomLecturerMapper lecturerMapper;
	
	@Autowired
	private TomCourseCommentMapper courseCommentMapper;
	
	@Autowired
	private TomUserInfoMapper userInfoMapper;
	
	@Autowired
	private TomUserLogMapper userLogMapper;
	
	@Autowired
	private TomCourseSectionMapper courseSectionMapper;
	
	@Autowired
	private TomEmpMapper empMapper;
	
	@Autowired
	private TomGradeRecordsMapper gradeRecordsMapper;
	
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	
	@Autowired
	private TomGradeDimensionMapper gradeDimensionMapper;
	
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	
	@Autowired
	private TomActivityMapper activityMapper;
	
	@Autowired
	private TomCourseSectionLearningRecordMapper courseSectionLearningRecordMapper;
	@Autowired
	private TomEbRecordMapper ebRecordMapper ;
	
	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	@Autowired
	private ActivityApiService activityApiService;
	
	@Autowired
	private TomLearnTimeLogMapper learnTimeLogMapper;
	
	@Autowired
	private TomTestMapper tomTestMapper;
	
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	
	@Autowired
	private TomCertificateManageMapper tomCertificateManageMapper;
	@Autowired
	private TomCertificateMapper tomCertificateMapper;
	@Autowired
	private RedisCacheManager redisCacheManager;
	@Autowired
	private CertificateManageService certificateManageService;
	ObjectMapper mapper=new ObjectMapper();

	/**
	 * 
	 * 功能描述：4.3.时长排行榜
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月24日下午1:39:09
	 * @param request
	 * @param response
	 * @return
	 */
	public  Result eleTopTimeList(HttpServletRequest request,HttpServletResponse response)  throws Exception{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			boolean redisOn = "true".equals(Config.getProperty("redisOn"));
			if(redisOn){
				Object eleTopTimeListObj = redisCacheManager.getCacheData(new RedisGetData(ApiConstant.API_COURSE,"eleTopTimeList"));
				if(eleTopTimeListObj != null){
					return new Result("Y", eleTopTimeListObj.toString(), "0", "");
				}
			}
			PageData<TomUserInfo> page= new PageData<TomUserInfo>();
			Map<Object,Object> map = new HashMap<Object,Object>();
			TomUserInfo example = new TomUserInfo();
			map.put("example", example);
			if(request.getParameter("firstIndex")==null){
				map.put("startNum",0);
			}else{
				map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
			}
			if(request.getParameter("pageSize")==null){
				map.put("endNum",10);
				page.setPageSize(10);
			}else{
				map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));
				page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
			}
			map.put("listOrder", request.getParameter("listOrder"));
			map.put("orderBy", request.getParameter("orderBy"));
			int count = userInfoMapper.countByExample(example);
			List<TomUserInfo> list=userInfoMapper.selectTopTime(map);
			for(TomUserInfo userInfo:list){
				TomEmp emp=empMapper.selectByPrimaryKey(userInfo.getCode());
				userInfo.setName(emp.getName());
				userInfo.setDeptName(emp.getDeptname());
			}
			page.setDate(list);
			page.setCount(count);
			JsonMapper jsonMapper = new JsonMapper();
			String pageJson = jsonMapper.toJson(page);
			pageJson=pageJson.replaceAll(":null",":\"\"");
			if(redisOn){
				redisCacheManager.addCacheData(new RedisCacheData(ApiConstant.API_COURSE,"eleTopTimeList",pageJson));
			}
			return new Result("Y", pageJson, "0", "");
		
	}
	
	/**
	 * 
	 * 功能描述：4.4.学习头条
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月23日下午6:01:00
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleLearningHeadlines(HttpServletRequest request,HttpServletResponse response)  throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		boolean redisOn = "true".equals(Config.getProperty("redisOn"));
		if(redisOn){
			Object eleLearningHeadlinesObj = redisCacheManager.getCacheData(new RedisGetData(ApiConstant.API_COURSE,"eleLearningHeadlines"));
			if(eleLearningHeadlinesObj != null){
				return new Result("Y", eleLearningHeadlinesObj.toString(), "0", "");
			}
		}
		List<TomCourseLearningRecord> list=coursesMapper.selectCourseLearningNO3();
		String name = "";
		JSONArray ja = new JSONArray();
		for(TomCourseLearningRecord courseLearning:list){
			JSONObject jo = new JSONObject();
			name = "";
			//查询员工详细信息
			TomEmp emp = empMapper.selectByPrimaryKey(courseLearning.getCode());
			TomCourses courses =coursesMapper.selectByPrimaryKey(courseLearning.getCourseId());
			if(null!= emp&&null!=courses){
				name = "【"+emp.getName()+"】"+emp.getDeptname()+"，刚刚学完《"+courses.getCourseName()+"》";
			}else{
				name = "【未知】，刚刚学完《未知》";
			}
			jo.put("name", name);
			ja.add(jo);
		}
		JSONArray ja1 = new JSONArray();
		JSONObject jo1 = new JSONObject();
		jo1.put("name", "欢迎来到蔚乐学！");
		ja1.add(jo1);
		jo1.put("name", "Welcome to Welearn!");
		ja1.add(jo1);
		String pageJson = mapper.writeValueAsString(ja);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		if(redisOn){
			redisCacheManager.addCacheData(new RedisCacheData(ApiConstant.API_COURSE,"eleLearningHeadlines",pageJson));
		}
		return new Result("Y", pageJson, "0", "");
	}
	/**
	 * 
	 * 功能描述：4.8.在线课程
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月18日上午11:33:53
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleOnlineCourseList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page= new PageData<TomCourses>();
		Map<Object,Object> map = new HashMap<Object,Object>();
		TomCourses example = new TomCourses();
		String keyWord=request.getParameter("KeyWord");
//		if(keyWord!=null){
//			keyWord=keyWord.replaceAll("/", "//");
//			keyWord=keyWord.replaceAll("%", "/%");
//			keyWord=keyWord.replaceAll("_", "/_");
//			
//		}
		String userId = request.getParameter("userId");
		example.setCode(userId);
		example.setCourseName(keyWord);
		example.setCourseType(request.getParameter("classifyId"));
		example.setIsExcellentCourse(request.getParameter("isExcellentCourse"));
		example.setStatus("Y");
		example.setCourseOnline("Y");
		map.put("example", example);
		if(request.getParameter("firstIndex")==null){
			map.put("startNum",0);
		}else{
			map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			map.put("endNum",10);
			page.setPageSize(10);
		}else{
			map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));//(int)map.get("startNum")+
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", request.getParameter("listOrder"));
		map.put("orderBy", request.getParameter("orderBy"));
//		int count = coursesMapper.countByExampleApi(example);
//		List<TomCourses> list=coursesMapper.selectListByPageApi(map);
		int count = coursesMapper.countByExampleApi2(example);
		List<TomCourses> list=coursesMapper.selectListByPageApi2(map);
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
	/**
	 * 
	 * 功能描述：4.9.课程分类
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月18日上午11:46:31
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseClassifyList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		if("true".equals(Config.getProperty("redisOn"))){//是否开启缓存;
			Result result = this.eleCourseClassifyListByCache(request, response);
			if(!"null".equals(result.getResults()))
				return result;
		}
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomCourseClassify> list=CourseClassifyMapper.selectByParentId(0);
		String userId = request.getParameter("userId");
		for(TomCourseClassify courseClassify:list){
			TomCourses courseExample=new TomCourses();
			courseExample.setCourseType(String.valueOf(courseClassify.getClassifyId()));
			
			TomCourses example = new TomCourses();
			example.setCode(userId);
			example.setCourseType(String.valueOf(courseClassify.getClassifyId()));
			example.setStatus("Y");
			example.setCourseOnline("Y");
			if(request.getParameter("isExcellentCourse")!=null){
				example.setIsExcellentCourse(request.getParameter("isExcellentCourse"));
			}
			
			int courseCounts=coursesMapper.countByExampleApi(example);
			courseClassify.setCourseCounts(courseCounts);
		}
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(list);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
	/**
	 * 
		 * 功能描述：[4.9.课程分类从缓存中获取]
		 * 创建者：WChao 创建时间: 2017年6月20日 下午5:29:22
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 *
	 */
	public Result eleCourseClassifyListByCache(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Object courseClassifyTreeJson = redisCacheManager.getCacheData(new RedisGetData("courseClassifyTree"));
		List<TomCourseClassify> list = null;
		if(courseClassifyTreeJson != null){
			list = com.alibaba.fastjson.JSONArray.parseArray(courseClassifyTreeJson.toString(),TomCourseClassify.class);
			String userId = request.getParameter("userId");
			String isExcellentCourse = request.getParameter("isExcellentCourse");
			Iterator<TomCourseClassify> it = list.iterator();
			while(it.hasNext()){
				TomCourseClassify courseClassify = it.next();
				courseClassify.getChildrens().clear();
				TomCourses courseExample=new TomCourses();
				courseExample.setCourseType(String.valueOf(courseClassify.getClassifyId()));
				int courseCount = 0;
				Object courseEmpRelationJson = redisCacheManager.getCacheData(new RedisGetData("courseEmpRelation",userId));
				if(courseEmpRelationJson != null){
					List<TomCourses> courseList = com.alibaba.fastjson.JSONArray.parseArray(courseEmpRelationJson.toString(),TomCourses.class);
					for(TomCourses course : courseList){
					      if(course.getCourseType() != null){//判断课程属不属于该分类;
					    	  boolean isTrue = false;
					    	  for(String classifyId : course.getCourseType().split(",")){
					    		   if(courseClassify.getClassifyId().toString().equals(classifyId)){
					    			   isTrue = true;
					    			   break;
					    		   }
					    	  }
					    	  if(!isTrue)
					    		  continue;
					      }
					      if(!"Y".equals(course.getStatus()) || !"Y".equals(course.getCourseOnline())){
					    	  continue;
					      }
					      if(isExcellentCourse!=null){
					    	  if(!isExcellentCourse.equals(course.getIsExcellentCourse()))
					    		  continue;
					      }
					      courseCount ++ ;
					}
				}
				courseClassify.setCourseCounts(courseCount);
			}
		}
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(list);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
	/**
	 * 
	 * 功能描述：4.6.精品课程
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月18日下午3:57:10
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleBoutiqueCourseList(HttpServletRequest request,HttpServletResponse response)  throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page= new PageData<TomCourses>();
		Map<Object,Object> map = new HashMap<Object,Object>();
		TomCourses example = new TomCourses();
		String userId = request.getParameter("userId");
		example.setCode(userId);
		example.setCourseType(request.getParameter("classifyId"));
		example.setIsExcellentCourse("Y");
		example.setCourseOnline("Y");
		example.setStatus("Y");
		map.put("example", example);
		if(request.getParameter("firstIndex")==null){
			map.put("startNum",0);
		}else{
			map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			map.put("endNum",10);
			page.setPageSize(10);
		}else{
			map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", request.getParameter("listOrder"));
		map.put("orderBy", request.getParameter("orderBy"));
//		int count = coursesMapper.countByExampleApi(example);
//		List<TomCourses> list=coursesMapper.selectListByPageApi(map);
		int count = coursesMapper.countByExampleApi2(example);
		List<TomCourses> list=coursesMapper.selectListByPageApi2(map);
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
	/**
	 * 
	 * 功能描述：4.5.最新课程
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月18日下午4:24:38
	 * @param request
	 * @param response
	 * @return
	 */
	public  Result eleNewCourseList(HttpServletRequest request,HttpServletResponse response)  throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		boolean redisOn = "true".equals(Config.getProperty("redisOn"));
		String userId = request.getParameter("userId");
		if(redisOn){
			Object eleNewCourseListObj = redisCacheManager.getCacheData(new RedisGetData(ApiConstant.API_COURSE, userId+"_eleNewCourseList"));
			if(eleNewCourseListObj != null){
				return new Result("Y", eleNewCourseListObj.toString(), "0", "");
			}
		}
		PageData<TomCourses> page= new PageData<TomCourses>();
		Map<Object,Object> map = new HashMap<Object,Object>();
		TomCourses example = new TomCourses();
		example.setCode(userId);
		example.setCourseOnline("Y");
		example.setStatus("Y");
		map.put("example", example);
		if(request.getParameter("firstIndex")==null){
			map.put("startNum",0);
		}else{
			map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			map.put("endNum",10);
			page.setPageSize(10);
		}else{
			map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", "create_time");
		map.put("orderBy", "desc");
//		int count = coursesMapper.countByExampleApi(example);
//		List<TomCourses> list=coursesMapper.selectListByPageApi(map);
		int count = coursesMapper.countByExampleApi2(example);
		List<TomCourses> list=coursesMapper.selectListByPageApi2(map);
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		if(redisOn){
			redisCacheManager.addCacheData(new RedisCacheData(ApiConstant.API_COURSE,userId+"_eleNewCourseList", pageJson));
		}
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.11.课程列表
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月18日下午4:46:37
	 * @param request
	 * @param response
	 * @return
	 */
	public  Result eleCourseList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page= new PageData<TomCourses>();
		String userId = request.getParameter("userId");
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("code", userId);
		if(request.getParameter("firstIndex")==null){
			map.put("startNum",0);
		}else{
			map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			map.put("endNum",10);
			page.setPageSize(10);
		}else{
			map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", "create_time");
		map.put("orderBy", "desc");
		
		int count = coursesMapper.countTaskCourse(map);
		List<TomCourses> list=coursesMapper.selectTaskCourse(map);
		for(TomCourses course:list){
			TomActivityProperty activityProperty=new TomActivityProperty();
			activityProperty.setCourseId(course.getCourseId());
			List<TomActivityProperty> proList=activityPropertyMapper.selectByExample(activityProperty);
			String preStatus=activityApiService.getPreStatus(proList, userId);
			course.setPreStatus(preStatus);
			
			//判断课程是否需要评分
			TomGradeRecords example=new TomGradeRecords();
			example.setCode(userId);
			example.setCourseId(course.getCourseId());
			example.setGradeType("C");
			List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(example);
			example.setGradeType("L");
			List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(example);
			if(gradeRecords1.size()>0&&gradeRecords2.size()>0){
				course.setIsGrade("Y");
			}else{
				course.setIsGrade("N");
			}
			
			if("Y".equals(course.getCourseOnline())){
				TomCourseLearningRecord record = new TomCourseLearningRecord();
				record.setCode(userId);
				record.setCourseId(course.getCourseId());
				int learningNum = courseLearningRecordMapper.countByExample(record);
				if(learningNum>0){
					course.setStatus("3");
				}else{
					course.setStatus("4");
				}
			}else{
				TomCourseSignInRecords signInExample=new TomCourseSignInRecords();
				signInExample.setCode(userId);
				signInExample.setCourseId(course.getCourseId());
				int signInNum = courseSignInRecordsMapper.countByExample(signInExample);
				if(signInNum>0){
					course.setStatus("1");
				}else{
					course.setStatus("2");
				}
			}
			
			activityProperty.setActivityId(course.getActivityId());
			TomActivityProperty activityProperty1 = activityPropertyMapper.selectByExample(activityProperty).get(0);
			
			if(activityProperty1!=null){
				course.setBeginTime(format.format(activityProperty1.getStartTime()));
				course.setEndTime(format.format(activityProperty1.getEndTime()));
				course.setBeginTimeS(String.valueOf(activityProperty1.getStartTime().getTime()));
				course.setEndTimeS(String.valueOf(activityProperty1.getEndTime().getTime()));
			}
			
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.12.课程详情
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月18日下午4:52:23
	 * @param request
	 * @param response
	 * @return
	 */
	public  Result eleCourseDetails(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page= new PageData<TomCourses>();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//获取当前登陆员工Code
		String code = request.getParameter("userId");
		TomCourses courses=coursesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("courseId")));
		String activityId = request.getParameter("activityId");
		//讲师id与姓名
		courses.setLecturerId(courses.getLecturers());
		
		Map<Object,Object> map1 = new HashMap<Object,Object>();
		map1.put("activityId", activityId);
		map1.put("courseId", courses.getCourseId());
		TomActivityProperty activityProperty = activityPropertyMapper.selectByCourseIdAndActivityId(map1);
		if(activityProperty!=null){
			courses.setBeginTime(format.format(activityProperty.getStartTime()));
			courses.setEndTime(format.format(activityProperty.getEndTime()));
		}
		
		//判断课程是否需要评分
		TomGradeRecords gradeExample=new TomGradeRecords();
		gradeExample.setCode(code);
		gradeExample.setCourseId(courses.getCourseId());
		gradeExample.setGradeType("C");
		List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(gradeExample);
		gradeExample.setGradeType("L");
		List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(gradeExample);
		if(gradeRecords1.size()>0&&gradeRecords2.size()>0){
			courses.setIsGrade("Y");
		}else{
			courses.setIsGrade("N");
		}
		
			if("Y".equals(courses.getCourseOnline())){
			 if(null!=courses.getLecturers() && !"".equals(courses.getLecturers())){
				int lectureKeyId = Integer.parseInt(courses.getLecturers().split(",")[1]);
				TomLecturer lecturer = lecturerMapper.selectByPrimaryKey(lectureKeyId);
				if(null != lecturer){
					courses.setLecturerName(lecturer.getLecturerName());
				}else{
					courses.setLecturerName("");
				}
			  }
				TomCourseSection example=new TomCourseSection();
				example.setCourseId(courses.getCourseId());
				example.setStatus("Y");
				courses.setSectionNum(courseSectionMapper.countByExample(example));
				
				/*start 查询课程学习状态*/
				Map<Object,Object> map = new HashMap<Object,Object>();
				map.put("code", code);
				map.put("courseId", courses.getCourseId());
				List<TomCourseLearningRecord> record=courseLearningRecordMapper.selectLearnRecord(map);
				if(record.size()>0){
					courses.setLearnStatus("Y");
				}else {
					courses.setLearnStatus("N");
				}
			}else{
				if(request.getParameter("activityId")!=null&&!"".equals(request.getParameter("activityId"))){
					TomActivity activity=activityMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("activityId")));
				
					courses.setNumberOfParticipants(activity.getNumberOfParticipants());
				}
				
				if(null!=activityProperty){
				  courses.setLecturerId(activityProperty.getLecturers());
				  if(null!=activityProperty.getLecturers() && !"".equals(activityProperty.getLecturers())){
					TomLecturer lecturer = lecturerMapper.selectByPrimaryKey(Integer.parseInt(activityProperty.getLecturers()));
					if(null != lecturer){
						courses.setLecturerName(lecturer.getLecturerName());
					}else{
						courses.setLecturerName("");
					}
				  }
					courses.setCourseAddress(activityProperty.getCourseAddress());
				}else{
					courses.setLecturerId("");
					courses.setLecturerName("");
				}
				TomCourseSignInRecords signInRecordsExample=new TomCourseSignInRecords();
				signInRecordsExample.setCode(code);
				signInRecordsExample.setCourseId(courses.getCourseId());
				int count=courseSignInRecordsMapper.countByExample(signInRecordsExample);
				if(count>0){
					courses.setLearnStatus("Y");
				}else {
					courses.setLearnStatus("N");
				}
			}
			
		//收藏、点赞状态
		TomFavoriteCourse favoriteCourse = new TomFavoriteCourse();
		favoriteCourse.setCode(code);
		favoriteCourse.setCourseId(courses.getCourseId());
		favoriteCourse.setStatus("Y");
		int countFavorite = coursesMapper.countByCourseIdAndCode(favoriteCourse);
		if(countFavorite>0){
			courses.setCollect("Y");
		}else{
			courses.setCollect("N");
		}
		
		TomCourseThumbUp courseThumbUp = new TomCourseThumbUp();
		courseThumbUp.setCode(code);
		courseThumbUp.setCourseId(courses.getCourseId());
		courseThumbUp.setStatus("Y");
		int countThumbUp = coursesMapper.countThumbUpByCourseIdAndCode(courseThumbUp);
		if(countThumbUp>0){
			courses.setPraise("Y");
		}else{
			courses.setPraise("N");
		}
		
		/*end*/
		List<TomCourses> list = new ArrayList<TomCourses>();;
		list.add(courses);
		page.setDate(list);
		page.setCount(1);
		page.setPageNum(0);
		page.setPageSize(1);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.13.更改课程收藏状态
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月18日下午5:56:17
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public  Result eleCourseCollectStatus(HttpServletRequest request,HttpServletResponse response) throws Exception{
		TomFavoriteCourse favoriteCourse = new TomFavoriteCourse();
		favoriteCourse.setCode(request.getParameter("userId"));
		if(null!=request.getParameter("courseId") || "".equals(request.getParameter("courseId"))){
			favoriteCourse.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		}
		if(null==favoriteCourse.getCode() || "".equals(favoriteCourse.getCode())){
			favoriteCourse.setCode("1");
		}
		if(null==favoriteCourse.getCourseId() || "".equals(favoriteCourse.getCourseId())){
			favoriteCourse.setCourseId(1);
		}
		
		int count = coursesMapper.countByCourseIdAndCode(favoriteCourse);
		//判断是否收藏过课程信息，如果收藏过就更新收藏状态，如果未收藏就添加收藏关联
		if(count>0){
			TomFavoriteCourse favoriteCourseOld = new TomFavoriteCourse();
			favoriteCourseOld = coursesMapper.selectByCourseIdAndCode(favoriteCourse);
			//判断员工已经收藏过的课程是“收藏”状态还是“取消收藏”状态，如果是“收藏”则改为“取消收藏”，如果是“取消收藏”则改为“收藏”
			if("Y".equals(favoriteCourseOld.getStatus())){
				favoriteCourse.setStatus("N");
			}else{
				favoriteCourse.setStatus("Y");
			}
			favoriteCourse.setCreateTime(new Date());
			coursesMapper.updateByCourseIdandCode(favoriteCourse);
		}else{
			favoriteCourse.setStatus("Y");
			favoriteCourse.setCreateTime(new Date());
			coursesMapper.insertFavorite(favoriteCourse);
		}
		return new Result("Y", "", "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.14.更改课程点赞状态
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日下午3:03:15
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public  Result eleCoursePraiseStatus(HttpServletRequest request,HttpServletResponse response)throws Exception {
		TomCourseThumbUp courseThumbUp = new TomCourseThumbUp();
		courseThumbUp.setCode(request.getParameter("userId"));
		if(null!=request.getParameter("courseId") || "".equals(request.getParameter("courseId"))){
			courseThumbUp.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		}
		if(null==courseThumbUp.getCode() || "".equals(courseThumbUp.getCode())){
			courseThumbUp.setCode("1");
		}
		if(null==courseThumbUp.getCourseId() || "".equals(courseThumbUp.getCourseId())){
			courseThumbUp.setCourseId(1);
		}
		TomCourses course=coursesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("courseId")));
		int praiseCount=course.getThumbUpCount();
		int count = coursesMapper.countThumbUpByCourseIdAndCode(courseThumbUp);
		//判断是否为课程信息点赞，如果点过就更新点赞状态，如果未点赞就添加点赞关联
		if(count>0){
			TomCourseThumbUp courseThumbUpOld = new TomCourseThumbUp();
			courseThumbUpOld = coursesMapper.selectThumbUpByCourseIdAndCode(courseThumbUp);
			//判断员工已经点赞过的课程是“点赞”状态还是“取消点赞”状态，如果是“点赞”则改为“取消点赞”，如果是“取消点赞”则改为“点赞”
			if("Y".equals(courseThumbUpOld.getStatus())){
				courseThumbUp.setStatus("N");
				praiseCount--;
			}else{
				courseThumbUp.setStatus("Y");
				praiseCount++;
			}
			coursesMapper.updateThumbUpByCourseIdandCode(courseThumbUp);
		}else{
			courseThumbUp.setStatus("Y");
			courseThumbUp.setCreateTime(new Date());
			coursesMapper.insertThumbUp(courseThumbUp);
			praiseCount++;
		}
		course.setThumbUpCount(praiseCount);
		coursesMapper.updateByPrimaryKey(course);
		return new Result("Y", "", "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.15.查看课程评论
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日下午6:10:15
	 * @param request
	 * @param response
	 * @return
	 */
	public  Result eleCourseCommentList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourseComment> page= new PageData<TomCourseComment>();
		Map<Object,Object> map = new HashMap<Object,Object>();
		String code = request.getParameter("userId");
		TomCourseComment example = new TomCourseComment();
		example.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		example.setStatus("Y");
		map.put("example", example);
		if(request.getParameter("firstIndex")==null){
			map.put("startNum",0);
		}else{
			map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			map.put("endNum",10);
			page.setPageSize(10);
		}else{
			map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		int count = courseCommentMapper.countByExample(example);
		List<TomCourseComment> list=courseCommentMapper.selectListByPage(map);
		//为课程评论人加载头像路径；以及查看当前登陆人是否点过赞
		for(TomCourseComment courseCommentNew:list){
			TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(courseCommentNew.getCode());
			if(courseCommentNew.getAnonymity().equals("N")){
				courseCommentNew.setHeadImg("-");
				courseCommentNew.setName("匿名用户");
			}else if(null!=userInfo){
				courseCommentNew.setHeadImg(userInfo.getHeadImg());
			}
			
			
			TomCourseCommentThumbUp courseCommentThumbUp = new TomCourseCommentThumbUp();
			courseCommentThumbUp.setCode(code);
			courseCommentThumbUp.setCourseCommentId(courseCommentNew.getCourseCommentId());
			int countNew = courseCommentMapper.countThumbUpByCourseIdAndCode(courseCommentThumbUp);
			if(countNew>0){
				courseCommentNew.setPraise("Y");
			}else{
				courseCommentNew.setPraise("N");
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.16.课程评论点赞
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月22日上午11:16:40
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public  Result eleCourseCommentPraiseSubmit(String json)throws Exception {
		JSONObject jsonObject=JSONObject.fromObject(json);
		TomCourseCommentThumbUp courseCommentThumbUp = new TomCourseCommentThumbUp();
		courseCommentThumbUp.setCode(jsonObject.getString("userId"));

		
		if(null!=jsonObject.getString("courseCommentId") || "".equals(jsonObject.getString("courseCommentId"))){
			courseCommentThumbUp.setCourseCommentId(Integer.parseInt(jsonObject.getString("courseCommentId")));
		}
		if(null==courseCommentThumbUp.getCode() || "".equals(courseCommentThumbUp.getCode())){
			courseCommentThumbUp.setCode("1");
		}
		if(null==courseCommentThumbUp.getCourseCommentId() || "".equals(courseCommentThumbUp.getCourseCommentId())){
			courseCommentThumbUp.setCourseCommentId(1);
		}
		
		int count = courseCommentMapper.countThumbUpByCourseIdAndCode(courseCommentThumbUp);
		//判断是否为课程评论信息点过赞，如果点过就更新点赞状态，如果未点赞就添加点赞关联
		if(count>0){
			TomCourseCommentThumbUp courseCommentThumbUpOld = new TomCourseCommentThumbUp();
			courseCommentThumbUpOld = courseCommentMapper.selectThumbUpByCourseIdAndCode(courseCommentThumbUp);
			//判断员工已经点过赞的课程评论是“点赞”状态还是“取消点赞”状态，如果是“点赞”则改为“取消点赞”，如果是“取消点赞”则改为“点赞”
			if("Y".equals(courseCommentThumbUpOld.getStatus())){
				courseCommentThumbUp.setStatus("N");				
			}else{
				courseCommentThumbUp.setStatus("Y");
			}
			courseCommentMapper.updateThumbUpByCourseIdandCode(courseCommentThumbUp);
		}else{
			courseCommentThumbUp.setStatus("Y");
			courseCommentThumbUp.setCreateTime(new Date());
			courseCommentMapper.insertThumbUp(courseCommentThumbUp);
			
			//点赞人增加EB
			TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
			Map<Object, Object> map2 = new HashMap<Object, Object>();
			SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
			map2.put("userId", jsonObject.getString("userId"));
			map2.put("road", "7");
			map2.put("date", simple.format(new Date()));
			List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map2);
			int ebCount=0;
			for(TomEbRecord er:ebRList){
				ebCount+=er.getExchangeNumber();
			}
			if(ebCount<100){
				userInfo.seteNumber(userInfo.geteNumber()+ 1);
				userInfo.setAddUpENumber(userInfo.getAddUpENumber()+ 1);
				TomEbRecord ebRecord = new TomEbRecord();
				ebRecord.setCode(jsonObject.getString("userId"));
				ebRecord.setUpdateTime(new Date());
				ebRecord.setExchangeNumber(1);
				ebRecord.setRoad("7");
				ebRecordMapper.insertSelective(ebRecord);
				userInfoMapper.updateByCode(userInfo);
			}
			
			//被点赞的课程的人增加EB			
			Integer courseId=courseCommentThumbUp.getCourseCommentId();
			TomCourseComment userIn=courseCommentMapper.selectByPrimaryKey(courseId);
			String code=userIn.getCode();
			TomUserInfo userInfo1=userInfoMapper.selectByPrimaryKey(code);
			Map<Object, Object> map3 = new HashMap<Object, Object>();
			SimpleDateFormat simple1 = new SimpleDateFormat("yyyy-MM-dd");
			map3.put("userId", code);
			map3.put("road", "8");
			map3.put("date", simple1.format(new Date()));
			List<TomEbRecord> ebRList1=ebRecordMapper.selectByRode(map3);
			int ebCount1=0;
			for(TomEbRecord er:ebRList1){
				ebCount1+=er.getExchangeNumber();
			}
			if(ebCount1<100){
				userInfo1.seteNumber(userInfo1.geteNumber()+ 2);
				userInfo1.setAddUpENumber(userInfo1.getAddUpENumber()+ 2);
				TomEbRecord ebRecord = new TomEbRecord();
				ebRecord.setCode(code);
				ebRecord.setUpdateTime(new Date());
				ebRecord.setExchangeNumber(2);
				ebRecord.setRoad("8");
				ebRecordMapper.insertSelective(ebRecord);
				userInfoMapper.updateByCode(userInfo1);
			}
		}
		TomCourseComment comment=courseCommentMapper.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseCommentId")));
		int thumbUpNumber=comment.getThumbUpNumber()+1;
		comment.setThumbUpNumber(thumbUpNumber);
		courseCommentMapper.updateByPrimaryKey(comment);
		return new Result("Y", "{\"thumbUpNumber\":"+thumbUpNumber+"}", "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.17.评论课程
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月23日上午9:58:45
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public  Result eleCourseCommentSubmit(String json) throws Exception {
		JSONObject jsonObject=JSONObject.fromObject(json);
		TomCourseComment courseComment = new TomCourseComment();
		courseComment.setCode(jsonObject.getString("userId"));
		TomUserLog userLog = userLogMapper.getUserByCode(courseComment.getCode());
		TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
		Map<Object, Object> map = new HashMap<Object, Object>();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		map.put("userId", jsonObject.getString("userId"));
		map.put("road", "11");
		map.put("date", simple.format(new Date()));
		List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map);
		int ebCount=0;
		for(TomEbRecord er:ebRList){
			ebCount+=er.getExchangeNumber();
		}
		if(ebCount<100){
			userInfo.seteNumber(userInfo.geteNumber()+ 2);
			userInfo.setAddUpENumber(userInfo.getAddUpENumber()+ 2);
			TomEbRecord ebRecord = new TomEbRecord();
			ebRecord.setCode(jsonObject.getString("userId"));
			ebRecord.setUpdateTime(new Date());
			ebRecord.setExchangeNumber(2);
			ebRecord.setRoad("11");
			ebRecordMapper.insertSelective(ebRecord);
			userInfoMapper.updateByCode(userInfo);
		}
		if(null!=userLog){
			courseComment.setName(userLog.getName());
		}
		courseComment.setCourseId(Integer.parseInt(jsonObject.getString("courseId")));
		courseComment.setCourseCommentContent(jsonObject.getString("courseCommentContent"));
		courseComment.setCreateTime(new Date());
		courseComment.setThumbUpNumber(0);
		courseComment.setStatus("Y");
		courseComment.setAnonymity(userInfo.getAnonymity());
		courseCommentMapper.insertSelective(courseComment);
		TomCourses course=coursesMapper.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseId")));
		course.setCommentCount(course.getCommentCount()+1);
		coursesMapper.updateByPrimaryKey(course);
		return new Result("Y", "", "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.18.课程章节列表
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月23日上午10:59:28
	 * @param request
	 * @param response
	 * @return
	 */
	public  Result eleCourseSectionsList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourseSection> page= new PageData<TomCourseSection>();
		Map<Object,Object> map = new HashMap<Object,Object>();
		TomCourseSection example = new TomCourseSection();
		String code = request.getParameter("userId");
		example.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		example.setStatus("Y");
		map.put("example", example);
		if(request.getParameter("firstIndex")==null){
			map.put("startNum",0);
		}else{
			map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			map.put("endNum",10);
			page.setPageSize(10);
		}else{
			map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		int count = courseSectionMapper.countByExample(example);
		List<TomCourseSection> list=courseSectionMapper.selectListByPage(map);
		for(TomCourseSection courseSectionNew:list){
			TomCourseSectionLearningRecord sectionLearning = new TomCourseSectionLearningRecord();
			sectionLearning.setCourseSectionId(courseSectionNew.getSectionId());
			sectionLearning.setCode(code);
			//课程内容
			courseSectionNew.setCourseFileuploadAddress(courseSectionNew.getSectionAddress());
			//查询是否学习过
			int sectionLearnCount = courseSectionMapper.countByCodeAndSectionId(sectionLearning);
			if(sectionLearnCount>0){
				courseSectionNew.setLearningStates("Y");
			}else{
				courseSectionNew.setLearningStates("N");
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.22.章节学习记录
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月23日下午3:38:42
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public  Result eleSectionsLearnningRecord(String json) throws Exception {
		JSONObject jsonObject=JSONObject.fromObject(json); 
		boolean confer = false;
		String certificateJson = "";
		if("N".equals(jsonObject.getString("learningStates"))){
			return new Resultc("Y", "", "0","", confer,certificateJson);
		}
		String pageJson = null;
		TomCourseSection courseSection = courseSectionMapper.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseSectionId")));
		TomCourses courses=coursesMapper.selectByPrimaryKey(courseSection.getCourseId());
		if (courses.getCourseOnline().equals("Y")) {
			TomCourseSectionLearningRecord sectionLearning = new TomCourseSectionLearningRecord();
			sectionLearning.setCode(jsonObject.getString("userId"));
			sectionLearning.setCourseSectionId(Integer.parseInt(jsonObject.getString("courseSectionId")));
			int flag = courseSectionLearningRecordMapper.countByExample(sectionLearning);
			if (flag == 0) {
				sectionLearning.setCreateTime(new Date());
				sectionLearning.setLearningStates(jsonObject.getString("learningStates"));
				courseSectionLearningRecordMapper.insertSectionLearning(sectionLearning);
			}
			TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
			TomEmp emp=empMapper.selectByPrimaryKey(jsonObject.getString("userId"));
			if(Integer.parseInt(jsonObject.getString("learningTime"))<3600*24){
				userInfo.setLearningTime(String.valueOf(Integer.parseInt(userInfo.getLearningTime())+ Integer.parseInt(jsonObject.getString("learningTime"))));
			}
			
			//插入操作日志
			TomLearnTimeLog learnTimeLog=new TomLearnTimeLog();
			learnTimeLog.setCreateTime(new Date());
			learnTimeLog.setCode(emp.getCode());
			learnTimeLog.setName(emp.getName());
			learnTimeLog.setCourseId(courses.getCourseId());
			learnTimeLog.setSectionId(courseSection.getSectionId());
			learnTimeLog.setTime(Integer.parseInt(jsonObject.getString("learningTime")));
			learnTimeLog.setUseragent(jsonObject.getString("userAgent"));
			learnTimeLogMapper.insertSelective(learnTimeLog);
			
			//判断课程是否需要评分
			TomGradeRecords examples=new TomGradeRecords();
			TomCourses course=new TomCourses();
			examples.setCode(jsonObject.getString("userId"));
			examples.setCourseId(courseSection.getCourseId());
			examples.setGradeType("C");
			List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(examples);
			examples.setGradeType("L");
			List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(examples);
			if(gradeRecords1.size()>0&&gradeRecords2.size()>0){
				course.setIsGrade("Y");
			}else{
				course.setIsGrade("N");
			}
			
			if (flag == 0) {
				TomCourseSection example = new TomCourseSection();
				example.setCourseId(courseSection.getCourseId());
				List<TomCourseSection> courseSections = courseSectionMapper.selectListByExample(example);
				for (TomCourseSection courseSection2 : courseSections) {
					TomCourseSectionLearningRecord courseSectionLearningRecord = new TomCourseSectionLearningRecord();
					courseSectionLearningRecord.setCourseSectionId(courseSection2.getSectionId());
					courseSectionLearningRecord.setCode(jsonObject.getString("userId"));
					if (courseSectionLearningRecordMapper.countByExample(courseSectionLearningRecord) == 0) {
						userInfoMapper.updateByCode(userInfo);
						CourseSectionLearningRecord page = new CourseSectionLearningRecord();
						page.setCourseStudyStates("N");
						page.setWhetherGrade(course.getIsGrade());
						pageJson = mapper.writeValueAsString(page);
						pageJson = pageJson.replaceAll(":null", ":\"\"");
						return new Resultc("Y", pageJson, "0", "",confer,certificateJson);
//						return new Result("Y", "", "0", "");
					}
				}
				TomCourseLearningRecord courseLearningRecord = new TomCourseLearningRecord();
				courseLearningRecord.setCode(jsonObject.getString("userId"));
				courseLearningRecord.setCourseId(courseSection.getCourseId());
				if (courseLearningRecordMapper.countByExample(courseLearningRecord) == 0) {
					courseLearningRecord.setLearningDate(new Date());
					courseLearningRecordMapper.insertSelective(courseLearningRecord);//学习完课程
					userInfo.setCourseNumber(String.valueOf(Integer.parseInt(userInfo.getCourseNumber())+1));
					userInfo.seteNumber(userInfo.geteNumber()+ courses.geteCurrency());
					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+ courses.geteCurrency());
					TomEbRecord ebRecord = new TomEbRecord();
					ebRecord.setCode(jsonObject.getString("userId"));
					ebRecord.setUpdateTime(new Date());
					ebRecord.setExchangeNumber(courses.geteCurrency());
					ebRecord.setRoad("2");
					ebRecordMapper.insertSelective(ebRecord);
					
				}
				TomCourseEmpRelation courseEmpRelationExample = new TomCourseEmpRelation();
				courseEmpRelationExample.setCourseId(courseSection.getCourseId());
				courseEmpRelationExample.setCode(jsonObject.getString("userId"));
				List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper.selectByExample(courseEmpRelationExample);
				if (courseEmpRelations.size() != 0) {
					TomCourseEmpRelation courseEmpRelation = courseEmpRelations.get(0);
					courseEmpRelation.setFinishStatus("Y");
					courseEmpRelation.setFinishTime(new Date());
					courseEmpRelation.setUpdateTime(new Date());
					courseEmpRelationMapper.update(courseEmpRelation);
				}
			}
			userInfoMapper.updateByCode(userInfo);
			//扫描活动下所有任务完成进度begin
			List<TomActivityProperty> activityProperty = activityPropertyMapper.selectByCourseId(courses.getCourseId());
			if (activityProperty.size() > 0) {//判断是原生态课程还是活动下的课程
			 if (jsonObject.has("activityId")) {
			int activityId = Integer.parseInt(jsonObject.getString("activityId"));
			TomActivity activity = activityMapper.selectByPrimaryKey(activityId);
			if ("Y".equals(activity.getCertificateState())) {//判断当前活动是不是有证书
				TomActivityProperty activityPropertyExample = new TomActivityProperty();
				TomExamScore scoreExample = new TomExamScore();
				activityPropertyExample.setActivityId(activityId);
				List<TomActivityProperty> activityPropertys = activityPropertyMapper.selectByExample(activityPropertyExample);
				for (TomActivityProperty tomActivityProperty : activityPropertys) {
					TomCourses tomCourses = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
					if (null != tomActivityProperty.getCourseId()) {
						if ("Y".equals(tomCourses.getCourseOnline())) {//线上课程
							TomCourseLearningRecord courseLearningRecordsExample = new TomCourseLearningRecord();
							courseLearningRecordsExample.setCode(jsonObject.getString("userId"));
							courseLearningRecordsExample.setCourseId(tomCourses.getCourseId());
							courseLearningRecordsExample.setLearningDate(tomActivityProperty.getEndTime());
							if (courseLearningRecordMapper.countByExample(courseLearningRecordsExample) > 0) {
								confer = true;
							}else{
								confer = false;
								break;
							}
						}else{//线下课程
							TomCourseSignInRecords signInRecordsExample = new TomCourseSignInRecords();
							signInRecordsExample.setCode(jsonObject.getString("userId"));
							signInRecordsExample.setCourseId(tomCourses.getCourseId());
							signInRecordsExample.setCreateDate(tomActivityProperty.getEndTime());
							if (courseSignInRecordsMapper.countByExample(signInRecordsExample) > 0) {
								confer = true;
							}else{
								confer = false;
								break;
							}
						}
					}
					
					if (null != tomActivityProperty.getExamId()) {//考试
						scoreExample.setCode(jsonObject.getString("userId"));
						scoreExample.setExamId(tomActivityProperty.getExamId());
						TomExamScore tomExamScore = tomExamScoreMapper.selectOneByExample(scoreExample);
						if (tomExamScore.getGradeState().equals("Y")) {
							confer = true;
						}else{
							confer = false;
							break;
						}
					}
				} 
				if (confer == true) {//完成活动下所有任务，发证书
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2.put("code", jsonObject.getString("userId"));
					jsonObject2.put("activityId", activityId);
					TomActivity tomActivity = activityMapper.selectByPrimaryKey(activityId);
					jsonObject2.put("certificateId", tomActivity.getCertificateId());
					Map<Object,Object> map = (Map<Object, Object>) jsonObject2.toBean(jsonObject2, Map.class);
					if ("Y".equals(tomActivity.getCertificateState())) {
						/*TomCertificateManageDto tomCertificateManageDto= new TomCertificateManageDto();
						tomCertificateManageDto.setActivityId(activityId);
						tomCertificateManageDto.setCertificateAddress(tomCertificateDto.getAddress());
						tomCertificateManageDto.setAfourCertificateAddress(tomCertificateDto.getAfourAddress());
						tomCertificateManageDto.setCertificateId(tomCertificateDto.getId());
						tomCertificateManageDto.setCode(jsonObject.getString("userId"));
						tomCertificateManageDto.setCreateTime(new Date());
						if (tomCertificateManageMapper.countByCodeAct(map) < 1) {
							tomCertificateManageMapper.insert(tomCertificateManageDto);
						}*/
						if (tomCertificateManageMapper.countByCodeAct(map) < 1) {
							certificateManageService.insert(jsonObject2.toString());
						}
						TomCertificateDto tomCertificateDto = tomCertificateMapper.selectByAct(activityId);
						certificateJson = mapper.writeValueAsString(tomCertificateDto);
						certificateJson = certificateJson.replaceAll(":null", ":\"\"");
					}
				}
			}
				}
			}
			//扫描活动任务进度end
			TomCourseSection tomCourseSection = new TomCourseSection();
			tomCourseSection = courseSectionMapper.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseSectionId")));
			List<TomCourseSection> list = courseSectionMapper.selectListByCourseId(tomCourseSection.getCourseId());
			int count = courseSectionMapper.countByExample(tomCourseSection);
			TomCourseSectionLearningRecord tomCourseSectionLearningRecord1 = new TomCourseSectionLearningRecord();
			tomCourseSectionLearningRecord1.setWhetherGrade(course.getIsGrade());
			for(TomCourseSection tomCourseSections:list){
				if("Y".equals(tomCourseSections.getStatus())){
					Map<Object,Object> map=new HashMap<Object,Object>();
					map.put("code", jsonObject.getString("userId"));
					map.put("courseSectionId", tomCourseSections.getSectionId());
					TomCourseSectionLearningRecord tomCourseSectionLearningRecord = new TomCourseSectionLearningRecord();
					tomCourseSectionLearningRecord = courseSectionLearningRecordMapper.selectByPrimaryKey(map);
					CourseSectionLearningRecord page = new CourseSectionLearningRecord();
					if(null!=tomCourseSectionLearningRecord){
						if("N".equals(tomCourseSectionLearningRecord.getLearningStates())){
							page.setCourseStudyStates("N");
							page.setWhetherGrade(course.getIsGrade());
							pageJson = mapper.writeValueAsString(page);
							pageJson = pageJson.replaceAll(":null", ":\"\"");
							return new Resultc("Y", pageJson, "0", "",confer,certificateJson);
						}else{
							page.setCourseStudyStates("Y");
							page.setWhetherGrade(course.getIsGrade());
							count--;
							if(count==0){
								pageJson = mapper.writeValueAsString(page);
								pageJson = pageJson.replaceAll(":null", ":\"\"");
								return new Resultc("Y", pageJson, "0", "",confer,certificateJson);
							}
						}
					}else{
						page.setCourseStudyStates("N");
						page.setWhetherGrade(course.getIsGrade());
						pageJson = mapper.writeValueAsString(page);
						pageJson = pageJson.replaceAll(":null", ":\"\"");
						return new Resultc("Y", pageJson, "0","", confer,certificateJson);
					}
				}
			}
		}
		return new Resultc("Y", "", "0", "",confer,certificateJson);
	}
		
	/**
	 * 
	 * 功能描述：5.21.获取讲师详情
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月24日下午3:32:15
	 * @param request
	 * @param response
	 * @return
	 */
	public  Result eleTeacherDetails(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomLecturer> page= new PageData<TomLecturer>();
		Map<Object,Object> map = new HashMap<Object,Object>();
		TomLecturer example = new TomLecturer();
		String lecturerId = request.getParameter("lecturerId");
		if (null != lecturerId && !"".equals(lecturerId) && !"undefined".equals(lecturerId)) {
			example.setLecturerId(Integer.parseInt(lecturerId));
		}
		map.put("example", example);
		if(request.getParameter("firstIndex")==null){
			map.put("startNum",0);
		}else{
			map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			map.put("endNum",10);
			page.setPageSize(10);
		}else{
			map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", request.getParameter("listOrder"));
		map.put("orderBy", request.getParameter("orderBy"));
		int count = lecturerMapper.countByExample(example);
		List<TomLecturer> list=lecturerMapper.selectListByPage(map);
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.19.课程/讲师评分
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月25日下午3:06:06
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public  Result eleCourseTeacherGrade(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String code = request.getParameter("userId");
		String gradeType = request.getParameter("gradeType");
		TomCourses courses=coursesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("courseId")));
		if (null != courses.getLecturers() && !"".equals(courses.getLecturers())) {
			courses.setLecturerName(lecturerMapper.selectByPrimaryKey(Integer.parseInt(courses.getLecturers().split(",")[1])).getLecturerName());
		}
		JSONObject jo = new JSONObject();

		jo.put("courseName", courses.getCourseName());
		jo.put("lecturerName", courses.getLecturerName());
		jo.put("courseAverage", courses.getCourseAverage());
		if (null != courses.getLecturers() && !"".equals(courses.getLecturers())) {
			jo.put("lecturerAverage", lecturerMapper.selectLecturerAvg(Integer.parseInt(courses.getLecturers().split(",")[1])));
		}
		List<TomGradeRecords> list = new ArrayList<TomGradeRecords>();
		TomGradeRecords example = new TomGradeRecords();
		example.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		example.setGradeType(gradeType);
		example.setCode(code);
		list = gradeRecordsMapper.selectListByRecords(example);
		JSONArray ja = new JSONArray();
		if(list.size()==0){
			String gradeIds[]=null;
			if("C".equals(gradeType)){
				gradeIds=courses.getCourseGradeDimensions().split(",");
			}else if("L".equals(gradeType)){
				if (null != courses.getLecturerGradeDimensions() && !"".equals(courses.getLecturerGradeDimensions())) {
					gradeIds=courses.getLecturerGradeDimensions().split(",");
				}
			}
			if (null != gradeIds) {
				for(String gradeId:gradeIds){
					TomGradeDimension gd= gradeDimensionMapper.selectByPrimaryKey(Integer.parseInt(gradeId));
					JSONObject graderjo = new JSONObject();
					graderjo.put("gradeDimensionId", gd.getGradeDimensionId());
					graderjo.put("score", "");
					graderjo.put("gradeDimensionName", gd.getGradeDimensionName());
					ja.add(graderjo);
				}
			}
		}else {
			for(TomGradeRecords gradeRecord:list){
				JSONObject graderjo = new JSONObject();
				graderjo.put("gradeDimensionId", gradeRecord.getGradeDimensionId());
				graderjo.put("score", gradeRecord.getScore());
				graderjo.put("gradeDimensionName", gradeRecord.getGradeDimensionName());
				ja.add(graderjo);
			}
		}
		jo.put("gradeDimensionList", ja);
		String pageJson = mapper.writeValueAsString(jo);
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：5.20.课程/讲师评分提交
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月31日上午10:28:29
	 * @param json
	 * @return
	 */
	@Transactional
	public  Result eleCourseTeacherGradeSubmit(String json) throws Exception {
		JSONObject jsonObject=JSONObject.fromObject(json);
		CourseTeacherGradeResults courseTeacherGradeResults=new CourseTeacherGradeResults();
		courseTeacherGradeResults.setCourseId(Integer.parseInt(jsonObject.getString("courseId")));
		courseTeacherGradeResults.setUserId(jsonObject.getString("userId"));
		courseTeacherGradeResults.setGradeType(jsonObject.getString("gradeType"));
		List<GradeResult> gradeResults=new ArrayList<GradeResult>();
		
		TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		map1.put("userId", jsonObject.getString("userId"));
		if("C".equals(jsonObject.getString("gradeType"))){
			map1.put("road", "5");
		}else if("L".equals(jsonObject.getString("gradeType"))){
			map1.put("road", "6");
		}
		map1.put("date", simple.format(new Date()));
		List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map1);
		int ebCount=0;
		for(TomEbRecord er:ebRList){
			ebCount+=er.getExchangeNumber();
		}
		if(ebCount<100){
			userInfo.seteNumber(userInfo.geteNumber()+ 2);
			userInfo.setAddUpENumber(userInfo.getAddUpENumber()+ 2);
			TomEbRecord ebRecord = new TomEbRecord();
			ebRecord.setCode(jsonObject.getString("userId"));
			ebRecord.setUpdateTime(new Date());
			ebRecord.setExchangeNumber(2);
			if("C".equals(jsonObject.getString("gradeType"))){
				ebRecord.setRoad("5");
			}else if("L".equals(jsonObject.getString("gradeType"))){
				ebRecord.setRoad("6");
			}
			ebRecordMapper.insertSelective(ebRecord);
			userInfoMapper.updateByCode(userInfo);
		}
		GradeResult gradeResult1;
		for(int i=0;i<jsonObject.getJSONArray("gradeDimensionList").size();i++){
			gradeResult1= mapper.readValue(jsonObject.getJSONArray("gradeDimensionList").get(i).toString(), GradeResult.class); 
			gradeResults.add(gradeResult1);
		}
		courseTeacherGradeResults.setGradeResult(gradeResults);
		
		List<GradeResult> list = courseTeacherGradeResults.getGradeResult();
		TomGradeRecords gradeRecord = new TomGradeRecords();
		gradeRecord.setCourseId(courseTeacherGradeResults.getCourseId());
		gradeRecord.setCode(courseTeacherGradeResults.getUserId());
		gradeRecord.setCreateTime(new Date());
		gradeRecord.setGradeType(courseTeacherGradeResults.getGradeType());
		for(GradeResult gradeResult:list){
			gradeRecord.setGradeDimensionId(Integer.parseInt(gradeResult.getGradeDimensionId()));
			gradeRecord.setScore(Double.parseDouble(gradeResult.getScore()));
			TomGradeDimension gradeDimension = gradeDimensionMapper.selectByPrimaryKey(Integer.parseInt(gradeResult.getGradeDimensionId()));
			if(null != gradeDimension){
				gradeRecord.setGradeDimensionName(gradeDimension.getGradeDimensionName());
			}
			gradeRecordsMapper.insertSelective(gradeRecord);
		}
		//更新平均分
		TomCourses courses=coursesMapper.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseId")));
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", Integer.parseInt(jsonObject.getString("courseId")));
		map.put("gradeType", "C");
		courses.setTotScore(gradeRecordsMapper.getTotScore(map));
		courses.setCourseAverage(gradeRecordsMapper.getAverageScore(map));
		map.put("gradeType", "L");
		courses.setLecturerAverage(gradeRecordsMapper.getAverageScore(map));
		coursesMapper.updateByPrimaryKeySelective(courses);
		return new Result("Y", "", "0", "");
	}
	
	
	/**
	 * 
	 * 功能描述：8.5.我的课程
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月29日下午6:22:08
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  Result eleMyCourse(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JsonMapper jsonMapper = new JsonMapper();
		MyCourse myCourse = null;
		boolean redisOn = "true".equals(Config.getProperty("redisOn"));
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page= new PageData<TomCourses>();
		int count0 = 0;
		int count1 = 0;
		int count2 = 0;
		List<TomCourses> list=new ArrayList<TomCourses>();
		String code = request.getParameter("userId");
		String identifying = request.getParameter("identifying");//查询标识
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("code", code);
		if(request.getParameter("firstIndex")==null){
			map.put("startNum",0);
		}else{
			map.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			map.put("endNum",10);
			page.setPageSize(10);
		}else{
			map.put("endNum",Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		/*if(redisOn){//从缓存查询;
			Object eleMyCourseList = redisCacheManager.getCacheData(new RedisGetData(ApiConstant.API_COURSE, code+"_eleMyCourse_list"));
			Object eleMyCoursePage = redisCacheManager.getCacheData(new RedisGetData(ApiConstant.API_COURSE, code+"_eleMyCourse_page"));
			Object eleMyCourse = redisCacheManager.getCacheData(new RedisGetData(ApiConstant.API_COURSE, code+"_eleMyCourse_obj"));
			if(eleMyCourse != null){
				myCourse = com.alibaba.fastjson.JSONObject.parseObject(eleMyCourse.toString(),MyCourse.class);
				page = com.alibaba.fastjson.JSONObject.parseObject(eleMyCoursePage.toString(),PageData.class);
				list = com.alibaba.fastjson.JSONArray.parseArray(eleMyCourseList.toString(), TomCourses.class);
				page.setDate(list);
				myCourse.setCourseList(page);
			}
		}*/
		if(myCourse == null){//否则查询数据库;
			count0 = coursesMapper.countFinishedCourse(map);
			count1 = coursesMapper.countUnfinishedCourse(map);
			TomFavoriteCourse favoriteCourse = new TomFavoriteCourse();
			favoriteCourse.setCode(code);
			favoriteCourse.setStatus("Y");
			count2 = coursesMapper.countByCourseIdAndCode(favoriteCourse);
			if("0".equals(identifying)){
				//list=coursesMapper.selectFinishedCourse(map);
				TomCourseCacher cacher0 = (TomCourseCacher) redisCacheManager.getCacher(TomCourseCacher.class);
				list = cacher0.initFinishedCourses(map);//缓存获取
				page.setCount(count0);
			}else if("1".equals(identifying)){
				//list=coursesMapper.selectUnfinishedCourse(map);
				TomCourseCacher cacher1 = (TomCourseCacher) redisCacheManager.getCacher(TomCourseCacher.class);
				list = cacher1.initUnFinishedCourses(map);//缓存获取
				for(TomCourses course:list){
					TomActivityProperty activityProperty=new TomActivityProperty();
					activityProperty.setCourseId(course.getCourseId());
					List<TomActivityProperty> proList=activityPropertyMapper.selectByExample(activityProperty);
					String preStatus=activityApiService.getPreStatus(proList, code);
					course.setPreStatus(preStatus);
				}
				page.setCount(count1);
			}else if("2".equals(identifying)){
				//list=coursesMapper.selectByCode(map);
				TomCourseCacher cacher2 = (TomCourseCacher) redisCacheManager.getCacher(TomCourseCacher.class);
				list = cacher2.initFavouriteCourses(map);//缓存获取
				page.setCount(count2);
			}
			
			myCourse=new MyCourse();
			myCourse.setCourseCount0(count0);
			myCourse.setCourseCount1(count1);
			myCourse.setCourseCount2(count2);
			
			for(TomCourses course:list){
					TomGradeRecords gradeExample=new TomGradeRecords();
					gradeExample.setCode(code);
					gradeExample.setCourseId(course.getCourseId());
					gradeExample.setGradeType("C");
					List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(gradeExample);
					gradeExample.setGradeType("L");
					List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(gradeExample);
					if(gradeRecords1.size()>0&&gradeRecords2.size()>0){
						course.setIsGrade("Y");
					}else{
						course.setIsGrade("N");
					}
					if("Y".equals(course.getCourseOnline())){
						TomCourseLearningRecord record = new TomCourseLearningRecord();
						record.setCode(code);
						record.setCourseId(course.getCourseId());
						int learningNum = courseLearningRecordMapper.countByExample(record);
						if(learningNum>0){
							course.setCourseStatus("2");
						}else{
							course.setCourseStatus("0");
						}
					}else{
						TomCourseSignInRecords signInExample=new TomCourseSignInRecords();
						signInExample.setCode(code);
						signInExample.setCourseId(course.getCourseId());
						int signInNum = courseSignInRecordsMapper.countByExample(signInExample);
						if(signInNum>0){
							course.setCourseStatus("3");
						}else{
							course.setCourseStatus("1");
						}
					}
				//查询开始结束时间，不查收藏课程
				TomActivityProperty activityProperty = null;
				if(!"2".equals(identifying)){
					Map<Object,Object> map1 = new HashMap<Object,Object>();
					map1.put("code", code);
					map1.put("courseId", course.getCourseId());
					activityProperty = activityPropertyMapper.selectByNewDate(map1);
				}
				if(null != activityProperty){
					DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
					course.setBeginTime(format.format(activityProperty.getStartTime()));
					course.setEndTime(format.format(activityProperty.getEndTime()));
					course.setBeginTimeS(String.valueOf(activityProperty.getStartTime().getTime()));
					course.setEndTimeS(String.valueOf(activityProperty.getEndTime().getTime()));
				}else{
					course.setBeginTime("");
					course.setEndTime("");
				}
				
				if(!"1".equals(identifying)){
					TomActivityProperty example=new TomActivityProperty();
					example.setCourseId(course.getCourseId());
					List<TomActivityProperty> activityProperties=activityPropertyMapper.selectByExample(example);
					if(activityProperties.size()!=0){
						course.setActivityId(activityProperties.get(0).getActivityId());
					}
				}
			}
			page.setDate(list);
			myCourse.setCourseList(page);
			if(redisOn){//将结果放入缓存;
				redisCacheManager.addCacheData(new RedisCacheData(ApiConstant.API_COURSE, code+"_eleMyCourse_list",  jsonMapper.toJson(list)));
				redisCacheManager.addCacheData(new RedisCacheData(ApiConstant.API_COURSE, code+"_eleMyCourse_page",  jsonMapper.toJson(page)));
				redisCacheManager.addCacheData(new RedisCacheData(ApiConstant.API_COURSE, code+"_eleMyCourse_obj",  jsonMapper.toJson(myCourse)));
			}
		}
		String pageJson = mapper.writeValueAsString(myCourse);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
	/**
	 * 
	 * 功能描述：[5.24 课程浏览接口]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年5月5日 下午3:17:48
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseViewers(HttpServletRequest request,HttpServletResponse response) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String courseId=request.getParameter("courseId");
		TomCourses course=coursesMapper.selectByPrimaryKey(Integer.parseInt(courseId));
		course.setViewers(course.getViewers()+1);
		coursesMapper.updateByPrimaryKey(course);
		return new Result();
	}
	
	/**
	 * 
	 * 功能描述：[5.25章节详情接口]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月14日 上午10:44:06
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseSectionDetail(HttpServletRequest request,HttpServletResponse response)throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourseSection courseSection = courseSectionMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("sectionId")));
		courseSection.setCourseFileuploadAddress(courseSection.getSectionAddress());
		String pageJson = mapper.writeValueAsString(courseSection);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：[5.3测试数据导出]
	 * 创建时间：2016年11月23日 下午18:00:00
	 * 
	 * 
	 */
	
	@Transactional
	public Result addTest(String json) throws Exception{
		TomTest tomTest = new TomTest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jo= JSONObject.fromObject(json);
		List<TomExportTestDto> dtoList=new ArrayList<TomExportTestDto>();
		List answerList=jo.getJSONArray("testAnswers");
		JsonMapper mapper =new JsonMapper();
		for (int i = 0; i < answerList.size(); i++) {
			
			TomTestDto tomTestDto= mapper.fromJson(answerList.get(i).toString(), TomTestDto.class);
			tomTest.setCourseId(Integer.parseInt(jo.getString("courseId")));
			tomTest.setTestName(jo.getString("testName"));
			tomTest.setTestUserId(jo.getString("testUserId"));
			tomTest.setTestUserName(jo.getString("testUserName"));
			tomTest.setTestQuestionId(tomTestDto.getTestQuestionId());
			tomTest.setTestQuestionName(tomTestDto.getTestQuestionName());
			tomTest.setTestStartTime(format.parse(jo.getString("testStartTime")));
			tomTest.setTestEndTime(format.parse(jo.getString("testEndTime")));
			tomTest.setTestUserAnswer(tomTestDto.getTestUserAnswer());
			
		   tomTestMapper.insert(tomTest);
	}
		   return new Result("Y", "", "0", "");
	
	}	
}
