package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseSignInRecords;

public interface TomCourseSignInRecordsMapper {
    int insert(TomCourseSignInRecords record);

    int insertSelective(TomCourseSignInRecords record);
    
    /**
     * 
     * 功能描述：查询签到记录数
     *
     * 创建者：ZXM
     * 创建时间: 2016年4月8日上午11:38:56
     * @param example
     * @return
     */
    int countByExample(TomCourseSignInRecords example);
    
    int countByExamples(Map<Object, Object> map);
    
    int countBySigin(Map<Object, Object> map);

    /**
     * 
     * 功能描述：[查询完成记录]
     *
     * 创建者：JCX
     * 创建时间: 2016年5月27日 下午4:29:37
     * @param example
     * @return 
     */
	List<TomCourseSignInRecords> selectByExample(TomCourseSignInRecords example);

	/**
	 * 
	 * 功能描述：[删除签到记录]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月20日 下午4:06:14
	 * @param courseSignInRecords
	 */
	void deleteByExample(TomCourseSignInRecords courseSignInRecords);
}