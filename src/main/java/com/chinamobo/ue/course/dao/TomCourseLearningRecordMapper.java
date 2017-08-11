package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseLearningRecord;

public interface TomCourseLearningRecordMapper {
    int insert(TomCourseLearningRecord record);

    int insertSelective(TomCourseLearningRecord record);
    
    /**
     * 
     * 功能描述：查看是否已学习课程
     *
     * 创建者：ZXM
     * 创建时间: 2016年4月8日上午11:23:08
     * @param record
     * @return
     */
    int countByExample(TomCourseLearningRecord example);
    
    List<TomCourseLearningRecord> selectLearnRecord(Map<Object, Object> map);

    /**
     * 
     * 功能描述：[查询线上课程已学人数]
     *
     * 创建者：JCX
     * 创建时间: 2016年6月12日 上午10:08:28
     * @param maps
     * @return
     */
	Integer countByActivity(Map<Object, Object> maps);

	/**
	 * 
	 * 功能描述：[删除学习记录]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月20日 下午4:01:58
	 * @param courseLearningRecord
	 */
	void deleteByExample(TomCourseLearningRecord courseLearningRecord);

	/**
	 * 
	 * 功能描述：[学习记录分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午2:40:06
	 * @param map
	 * @return
	 */
	List<TomCourseLearningRecord> selectByPage(Map<Object, Object> map);
}