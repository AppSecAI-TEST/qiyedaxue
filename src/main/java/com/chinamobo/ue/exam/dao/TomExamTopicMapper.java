package com.chinamobo.ue.exam.dao;

import java.util.List;

import com.chinamobo.ue.exam.entity.TomExamTopic;

public interface TomExamTopicMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_TOPIC
     *
     * @mbggenerated
     */
    int insert(TomExamTopic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_TOPIC
     *
     * @mbggenerated
     */
    int insertSelective(TomExamTopic record);
    
    int insertList(List<TomExamTopic> record);
    
    int deleteAll(Integer examPaperId);
}