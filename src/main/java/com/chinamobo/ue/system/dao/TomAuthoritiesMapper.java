package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.system.entity.TomAuthorities;

public interface TomAuthoritiesMapper {
    int deleteByPrimaryKey(Integer authorityId);

    int insert(TomAuthorities record);

    int insertSelective(TomAuthorities record);

    TomAuthorities selectByPrimaryKey(Integer authorityId);

    int updateByPrimaryKeySelective(TomAuthorities record);

    int updateByPrimaryKey(TomAuthorities record);
    
    List<TomAuthorities> selectAllType ();
    
    List<TomAuthorities> selectByRoleId (int roleId);
    
    List<TomAuthorities> selectAllAuth();
}