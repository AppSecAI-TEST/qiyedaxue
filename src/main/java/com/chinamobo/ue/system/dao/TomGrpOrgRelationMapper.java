package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomGrpOrgRelation;

public interface TomGrpOrgRelationMapper {
    int insert(TomGrpOrgRelation record);

    int insertSelective(TomGrpOrgRelation record);
    
    List<TomGrpOrgRelation> selectByCode(Map <Object,Object> map);
    
    int countByExample(TomGrpOrgRelation example);
    
    int insertList(List<TomGrpOrgRelation> list);
}