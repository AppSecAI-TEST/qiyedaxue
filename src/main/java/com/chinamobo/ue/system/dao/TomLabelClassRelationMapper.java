package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.entity.TomLabelClassRelation;
import com.chinamobo.ue.system.entity.TomLabelEmpRelation;

public interface TomLabelClassRelationMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteByPrimary(Integer classId);
    
    int deleteByTagId(String tagId);
    
    //删除全部微信标签分类关联表
    int deleteWxLabelClassRelation();

    int insert(TomLabelClassRelation record);

    int insertSelective(TomLabelClassRelation record);

    TomLabelClassRelation selectByPrimaryKey(Integer id);
    
    TomLabelClassRelation selectByPrimary(String tagId);

    int updateByPrimaryKeySelective(TomLabelClassRelation record);

    int updateByPrimaryKey(TomLabelClassRelation record);
    
    List<TomLabelClassRelation> selectByLabelClassRelation(Integer classId);
    
    int countByLabelClass(Map<Object,Object> map);
    
    List<Tree> selectLabelAsTree(Integer classId);
    
    int countBytagId(String tagId);
    
    //查询标签分类下的人员
    List<TomLabelEmpRelation> selectLabelClassEmpList(Integer classId);
}