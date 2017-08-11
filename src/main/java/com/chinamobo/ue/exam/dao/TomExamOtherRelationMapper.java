package com.chinamobo.ue.exam.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.exam.entity.TomExamOtherRelation;

public interface TomExamOtherRelationMapper {
    int insert(TomExamOtherRelation record);

    int insertSelective(TomExamOtherRelation record);
    
    void deleteByExamId(Integer examId);
    
    List<TomExamOtherRelation> selectOtherList(Map<Object,Object> map);
}