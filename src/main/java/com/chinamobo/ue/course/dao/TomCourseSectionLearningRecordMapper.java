package com.chinamobo.ue.course.dao;

import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseSectionLearningRecord;

public interface TomCourseSectionLearningRecordMapper {

	 /**
     * 
     * 功能描述：添加课程章节学习记录
     *
     * 创建者：ZXM
     * 创建时间: 2016年3月23日下午4:03:24
     * @param example
     */
    void insertSectionLearning(TomCourseSectionLearningRecord example);

	int countByExample(TomCourseSectionLearningRecord courseSectionLearningRecord);
	/**
	 * 
	 * 功能描述：[累计播放次数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月14日 下午2:35:50
	 * @param map
	 * @return
	 */
	int countTotalViews(Map<Object, Object> map);
	
	TomCourseSectionLearningRecord selectByPrimaryKey(Map<Object, Object> map);
}
