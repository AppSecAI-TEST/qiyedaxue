package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.system.entity.TomLabel;

public interface TomLabelMapper {
    int insert(TomLabel record);

    int insertSelective(TomLabel record);
    
    TomLabel selectByPrimaryKey(String tagId);
    
    //查询最大标签id排除微信标签
    int maxTagId();
    
    //修改标签
    int updateByPrimaryKeySelective(TomLabel record);
    
    int deleteByPrimaryKey(String tagId);
    
    //删除全部微信标签
    int deleteWX();
    
    //查询全部标签
    List<TomLabel> selectAll();
}