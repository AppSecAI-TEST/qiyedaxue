/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.cacher.interfaces.TomMyCourseIntf;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.common.tree.Node;
import com.chinamobo.ue.common.tree.course.CourseClassifyTree;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.utils.JsonMapper;

/**
 * 版本: [1.0]
 * 功能说明: 课程分类缓存;
 * 作者: WChao 创建时间: 2017年6月16日 上午9:25:21
 */
public class TomCourseCacher implements Cacher,TomMyCourseIntf {

	@Autowired
	private CourseClassifyTree courseClassifyTree;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	private AbstractCacheManager cacheManager;

	private JsonMapper mapper = new JsonMapper();
/*	@Autowired
	private TomGradeRecordsMapper gradeRecordsMapper;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;*/
	
	@Override
	public void init() throws Exception {
		
	}
	@Override
	public void doCache(AbstractCacheManager cacheManager) throws Exception {
		this.cacheManager = cacheManager;
		Node<TomCourseClassify> root = new Node<TomCourseClassify>("0","根节点");
		//创建课程分类树结构;
		courseClassifyTree.CreateTree(root);
		JsonMapper mapper = new JsonMapper();
		List<TomCourseClassify> list = root.getChildrens();
		//缓存课程分类;
		cacheManager.addCacheData(new RedisCacheData("courseClassifyTree", mapper.toJson(list)));
		List<TomCourseEmpRelation> courseEmpRelationList = courseEmpRelationMapper.selectCourseEmpRelationByDistinct();
		if(courseEmpRelationList != null){
			for(TomCourseEmpRelation courseEmpRelation : courseEmpRelationList){
				//缓存人员的所有课程;
				List<TomCourses> courseList = coursesMapper.selectAllCourseByUserCodeApi(courseEmpRelation.getCode());
				cacheManager.addCacheData(new RedisCacheData("courseEmpRelation", courseEmpRelation.getCode() , mapper.toJson(courseList)));
			
				/*//缓存人员的所有完成的课程;
				List<TomCourses> courseFinishedList = coursesMapper.selectAllFinishedCourse(courseEmpRelation.getCode());
				cacheManager.addCacheData(new RedisCacheData("courseFinished", courseEmpRelation.getCode() , mapper.toJson(courseFinishedList)));
				
				//缓存人员的所有未完成的课程;
				List<TomCourses> courseUnFinishedList = coursesMapper.selectAllUnfinishedCourse(courseEmpRelation.getCode());
				
				cacheManager.addCacheData(new RedisCacheData("courseUnFinished", courseEmpRelation.getCode() , mapper.toJson(courseUnFinishedList)));
				
				//缓存所有收藏的课程;
				paraMap.put("code", courseEmpRelation.getCode());
				paraMap.put("status", "Y");
				List<TomCourses> favoriteCourseList = coursesMapper.selectAllFavoriteCourseByCourseIdAndCode(paraMap);
				cacheManager.addCacheData(new RedisCacheData("favoriteCourse", courseEmpRelation.getCode() , mapper.toJson(favoriteCourseList)));
				
				paraMap.remove("status");
				//缓存所有已完成线上课程
				List<TomCourses> courseFinishedOnlineList = coursesMapper.selectFinishedCourse(paraMap);
				cacheManager.addCacheData(new RedisCacheData("courseFinishedOnline", courseEmpRelation.getCode() , mapper.toJson(courseFinishedOnlineList)));
				
				//缓存所有未完成课程
				List<TomCourses> courseUnFinishedOnlineList = coursesMapper.selectUnfinishedCourse(paraMap);
				cacheManager.addCacheData(new RedisCacheData("courseUnFinishedOnline", courseEmpRelation.getCode() , mapper.toJson(courseUnFinishedOnlineList)));
				
				if(courseList != null && courseList.size() > 0){
					for(TomCourses course : courseList){
						//缓存所有人员课程是否需要评分;
						TomGradeRecords gradeExample=new TomGradeRecords();
						gradeExample.setCode(courseEmpRelation.getCode());
						gradeExample.setCourseId(course.getCourseId());
						List<TomGradeRecords> gradeRecordsList = gradeRecordsMapper.selectListByRecords(gradeExample);
						cacheManager.addCacheData(new RedisCacheData("gradeRecords", courseEmpRelation.getCode()+"_"+course.getCourseId(), mapper.toJson(gradeRecordsList)));
						//缓存所有人员课程学习记录;
						TomCourseLearningRecord record = new TomCourseLearningRecord();
						record.setCode(courseEmpRelation.getCode());
						record.setCourseId(course.getCourseId());
						Map<Object,Object> param = new HashMap<Object,Object>();
						param.put("code", courseEmpRelation.getCode());
						param.put("courseId", course.getCourseId());
						List<TomCourseLearningRecord> courseLearningRecordList = courseLearningRecordMapper.selectLearnRecord(param);
						cacheManager.addCacheData(new RedisCacheData("courseLearningRecord", courseEmpRelation.getCode()+"_"+course.getCourseId(), mapper.toJson(courseLearningRecordList)));
						//缓存所有人员课程签到记录;
						TomCourseSignInRecords signInExample=new TomCourseSignInRecords();
						signInExample.setCode(courseEmpRelation.getCode());
						signInExample.setCourseId(course.getCourseId());
						List<TomCourseSignInRecords> courseSignInRecordsList = courseSignInRecordsMapper.selectByExample(signInExample);
						cacheManager.addCacheData(new RedisCacheData("courseSignInRecord", courseEmpRelation.getCode()+"_"+course.getCourseId(), mapper.toJson(courseSignInRecordsList)));
					}
				}*/
				
			}
		}
		
		
		
		
	}
	@Override
	public List<TomCourses> initFinishedCourses(Map<Object, Object> paramMap) {
		List<TomCourses> list = coursesMapper.selectFinishedCourse(paramMap);
		if (null != this.cacheManager && null != paramMap.get("code")) {
			try {
				cacheManager.addCacheData(new RedisCacheData("courses", paramMap.get("code").toString(),mapper.toJson(list)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	@Override
	public List<TomCourses> initUnFinishedCourses(Map<Object, Object> paramMap) {
		List<TomCourses> list = coursesMapper.selectUnfinishedCourse(paramMap);
		if (null != this.cacheManager && null != paramMap.get("code")) {
			try {
				cacheManager.addCacheData(new RedisCacheData("courses", paramMap.get("code").toString(),mapper.toJson(list)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	@Override
	public List<TomCourses> initFavouriteCourses(Map<Object, Object> paramMap) {
		List<TomCourses> list = coursesMapper.selectByCode(paramMap);
		try {
			cacheManager.addCacheData(new RedisCacheData("courses", paramMap.get("code").toString(),mapper.toJson(list)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
