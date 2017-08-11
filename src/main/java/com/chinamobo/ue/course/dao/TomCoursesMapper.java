package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseThumbUp;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomFavoriteCourse;

public interface TomCoursesMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_COURSES
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer courseId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_COURSES
     *
     * @mbggenerated
     */
    int insert(TomCourses record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_COURSES
     *
     * @mbggenerated
     */
    int insertSelective(TomCourses record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_COURSES
     *
     * @mbggenerated
     */
    TomCourses selectByPrimaryKey(Integer courseId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_COURSES
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomCourses record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_COURSES
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomCourses record);

    /**
     * 
     * 功能描述：[统计符合条件的记录数]
     *
     * 创建者：JCX
     * 创建时间: 2016年3月3日 下午3:42:36
     * @param example
     * @return
     */
	int countByExample(TomCourses example);

	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:42:49
	 * @param map
	 * @return
	 */
	List<TomCourses> selectListByPage(Map<Object, Object> map);
	
	/**
	 * 功能描述：[课程统计]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月9日 上午10:15:16
	 * @param courseName
	 * @return
	 */
	List<TomCourses> courseCountsList(String courseName);
	/**
	 * 
	 * 功能描述：[根据讲师id查询课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月15日 下午6:11:47
	 * @param lecturerId
	 * @return
	 */
	List<TomCourses> selectByLecturerId(int lecturerId);
	
	/**
	 * 功能描述：[查询课程]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午5:49:00
	 * @return
	 */
	List<TomCourses> findCourses();
	
	/**
	 * 
	 * 功能描述：收藏课程
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日上午10:16:17
	 * @param favoriteCourse
	 */
	void insertFavorite(TomFavoriteCourse favoriteCourse);
	
	/**
	 * 
	 * 功能描述：更新收藏状态
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日上午10:17:37
	 * @param favoriteCourse
	 */
	void updateByCourseIdandCode(TomFavoriteCourse favoriteCourse);
	
	/**
	 * 
	 * 功能描述：查看是否收藏课程
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日上午10:20:34
	 * @param favoriteCourse
	 * @return
	 */
	int countByCourseIdAndCode(TomFavoriteCourse favoriteCourse);
	
	/**
	 * 
		 * 功能描述：[查询所有收藏的课程]
		 * 创建者：WChao 创建时间: 2017年6月21日 下午3:47:18
		 * @param favoriteMap
		 * @return
		 *
	 */
	List<TomCourses> selectAllFavoriteCourseByCourseIdAndCode(Map<Object,Object> favoriteMap);
	
	/**
	 * 
	 * 功能描述：根据员工代码和课程id查询收藏课程
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日上午11:08:22
	 * @param favoriteCourse
	 * @return
	 */
	TomFavoriteCourse selectByCourseIdAndCode (TomFavoriteCourse favoriteCourse);
	/**
	 * 
	 * 功能描述：添加点赞
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日下午2:50:19
	 * @param courseThumbUp
	 */
	void insertThumbUp(TomCourseThumbUp courseThumbUp);
	
	/**
	 * 
	 * 功能描述：更新点赞状态
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日下午2:52:31
	 * @param courseThumbUp
	 */
	void updateThumbUpByCourseIdandCode(TomCourseThumbUp courseThumbUp);
	
	/**
	 * 
	 * 功能描述：查看是否已经为课程点赞
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日下午2:54:29
	 * @param courseThumbUp
	 * @return
	 */
	int countThumbUpByCourseIdAndCode(TomCourseThumbUp courseThumbUp);
	
	/**
	 * 
	 * 功能描述：查询课程点赞信息
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月21日下午2:55:38
	 * @param courseThumbUp
	 * @return
	 */
	TomCourseThumbUp selectThumbUpByCourseIdAndCode(TomCourseThumbUp courseThumbUp);
	
	/**
	 * 
	 * 功能描述：查询当前登陆人可看到的课程条数
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月22日下午5:40:54
	 * @param example
	 * @return
	 */
	int countByExampleApi(TomCourses example);
	
	int countByExampleApi2(TomCourses example);

	/**
	 * 
	 * 功能描述：分页查询当前登陆人可看到的数据
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月22日下午5:39:48
	 * @param map
	 * @return
	 */
	List<TomCourses> selectListByPageApi(Map<Object, Object> map);
	
	List<TomCourses> selectListByPageApi2(Map<Object, Object> map);
	
	/**
	 * 
	 * 功能描述：查询课程学习最新的前三条学习记录
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月23日下午5:58:57
	 * @return
	 */
	List<TomCourseLearningRecord> selectCourseLearningNO3();
	
	
	/**
	 * 
	 * 功能描述：查询当前用户所收藏的课程信息
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月29日下午5:49:22
	 * @param map
	 * @return
	 */
	List<TomCourses> selectByCode(Map<Object, Object> map);
	
	List<TomCourses> selectByPackageId(int packageId);
	List<TomCourses> selectByPackageIdTwo(int packageId);
	List<TomCourses> selectOpen();

	/**
	 * 
	 * 功能描述：[任务中课程数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月3日 上午10:36:53
	 * @param example
	 * @return
	 */
	int countTaskCourse(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[任务中课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月3日 上午10:37:21
	 * @param map
	 * @return
	 */
	List<TomCourses> selectTaskCourse(Map<Object, Object> map);
	/**
	 * 
		 * 功能描述：[查询所有已完成可成数]
		 * 创建者：WChao 创建时间: 2017年6月21日 下午2:50:54
		 * @param code
		 * @return
		 *
	 */
	List<TomCourses> selectAllFinishedCourse(String code);
	/**
	 * 
	 * 功能描述：[统计已完成课程数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月5日 上午10:36:21
	 * @param map
	 * @return
	 */
	int countFinishedCourse(Map<Object, Object> map);
	/**
	 * 
		 * 功能描述：[查询所有未完成课程数]
		 * 创建者：WChao 创建时间: 2017年6月21日 下午2:51:48
		 * @param code
		 * @return
		 *
	 */
	List<TomCourses> selectAllUnfinishedCourse(String code);
	/**
	 * 
	 * 功能描述：[统计未完成课程数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月5日 上午10:48:08
	 * @param map
	 * @return
	 */
	int countUnfinishedCourse(Map<Object, Object> map);
	

	/**
	 * 
	 * 功能描述：[查询已完成线上课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月5日 上午10:48:18
	 * @param map
	 * @return
	 */
	List<TomCourses> selectFinishedCourse(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[查询未完成课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月5日 上午11:10:25
	 * @param map
	 * @return
	 */
	List<TomCourses> selectUnfinishedCourse(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[查询签到的线下课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月16日 下午4:02:10
	 * @param map
	 * @return
	 */
	List<TomCourses> selectFinishedOfflineCourse(Map<Object, Object> map);
	/**
	 * 
		 * 功能描述：[查询当前用户所有拥有的课程]
		 * 创建者：WChao 创建时间: 2017年6月19日 下午1:49:44
		 * @param code
		 * @return
		 *
	 */
	List<TomCourses> selectAllCourseByUserCodeApi(String code);

	int countByExampleForRooling(TomCourses example);
	
	List<TomCourses> selectListByPageForRooling(Map<Object, Object> map);
	/**
	 * 
	 * 功能描述：[统计累计创建课程数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月14日 下午2:08:16
	 * @param map
	 * @return
	 */
	int countTotalCourses(Map<Object, Object> map);
}