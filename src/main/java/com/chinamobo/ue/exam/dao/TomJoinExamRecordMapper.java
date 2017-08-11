package com.chinamobo.ue.exam.dao;

import java.util.Map;

import com.chinamobo.ue.exam.entity.TomJoinExamRecord;

public interface TomJoinExamRecordMapper {
    int insert(TomJoinExamRecord record);

    int insertSelective(TomJoinExamRecord record);

    /**
     * 
     * 功能描述：[发生考试次数]
     *
     * 创建者：JCX
     * 创建时间: 2016年7月14日 下午2:49:02
     * @param map
     * @return
     */
	int countTotalExams(Map<Object, Object> map);
}