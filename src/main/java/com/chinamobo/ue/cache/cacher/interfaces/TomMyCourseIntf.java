package com.chinamobo.ue.cache.cacher.interfaces;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourses;

/**
 * 
 * @ClassName: TomFinishedCourseIntf 
 * @Description: 查已完成的课程接口 
 * @author Acemon
 * @date 2017年7月6日 上午9:38:37
 */
public interface TomMyCourseIntf {
	public List<TomCourses> initFinishedCourses(Map<Object,Object> paramMap);
	public List<TomCourses> initUnFinishedCourses(Map<Object,Object> paramMap);
	public List<TomCourses> initFavouriteCourses(Map<Object,Object> paramMap);
}
