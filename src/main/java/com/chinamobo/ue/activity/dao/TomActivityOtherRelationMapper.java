package com.chinamobo.ue.activity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.entity.TomActivityOtherRelation;

public interface TomActivityOtherRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TomActivityOtherRelation record);

    int insertSelective(TomActivityOtherRelation record);

    TomActivityOtherRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TomActivityOtherRelation record);

    int updateByPrimaryKey(TomActivityOtherRelation record);
    
    int deleteByActivityId(Integer avtivityId);
    
    //培训活动关联信息
    List<TomActivityOtherRelation> selectByList(Map<Object, Object> map);
    
    
}