package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.system.entity.TomAuthorities;
import com.chinamobo.ue.system.entity.TomRoleAuthorities;

public interface TomRoleAuthoritiesMapper {
    void insert(TomRoleAuthorities record);

    int insertSelective(TomRoleAuthorities record);
    
    List<TomAuthorities> selectAuthoritiesByAdminId(int id);
    
    List<TomAuthorities> selectAuthoritiesByRoleId(int id);

	void deleteByPrimaryKey(Integer roleid);

	List<TomAuthorities> selectAll();

	List<TomRoleAuthorities> selectAuthoritiesByRoilId(Integer role_id);
}