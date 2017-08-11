package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomGradeRecords;

public interface TomGradeRecordsMapper {
    int insert(TomGradeRecords record);

    int insertSelective(TomGradeRecords record);
    
    /**
     * 
     * 功能描述：根据所传输条件查找记录
     *
     * 创建者：ZXM
     * 创建时间: 2016年3月25日下午4:09:51
     * @param record
     * @return
     */
    List<TomGradeRecords> selectListByRecords(TomGradeRecords example);

    /**
     * 
     * 功能描述：[计算总分]
     *
     * 创建者：JCX
     * 创建时间: 2016年5月5日 下午2:30:57
     * @param map
     * @return
     */
	Double getTotScore(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[计算平均分]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月5日 下午2:39:53
	 * @param map
	 * @return
	 */
	Double getAverageScore(Map<Object, Object> map);
}