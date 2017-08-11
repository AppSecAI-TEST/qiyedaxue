package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.system.entity.TomLabelEmpRelation;

public interface TomLabelEmpRelationMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteByPrimary(String tagId);
   
    //删除全部微信标签人员关联表
    int deleteWxLabelEmpRelation();

    int insert(TomLabelEmpRelation record);

    int insertSelective(TomLabelEmpRelation record);

    TomLabelEmpRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TomLabelEmpRelation record);

    int updateByPrimaryKey(TomLabelEmpRelation record);
    
    int count(Map<Object, Object> map);
    
    List<TomLabelEmpRelation> selectLabelEmpList(Map<Object, Object> map);
    
    List<TomLabelEmpRelation> selectBytagIdList(String tagId);
    
    TomLabelEmpRelation selectByTagIdCode(Map<Object, Object> map);
}