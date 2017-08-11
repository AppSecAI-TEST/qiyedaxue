package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseOtherRelation;

public interface TomCourseOtherRelationMapper {
    int insert(TomCourseOtherRelation record);

    int insertSelective(TomCourseOtherRelation record);
    
    void deleteByCourseId(Integer courseId);
    
    void falseDeletion(TomCourseOtherRelation record);
    
    List<TomCourseOtherRelation> selectOtherList(Map<Object,Object> map);
}