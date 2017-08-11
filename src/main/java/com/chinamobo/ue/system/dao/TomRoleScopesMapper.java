package com.chinamobo.ue.system.dao;

import com.chinamobo.ue.system.entity.TomRoleScopes;

public interface TomRoleScopesMapper {
    void insert(TomRoleScopes record);

    int insertSelective(TomRoleScopes record);

	void updateByPrimaryKeySelective(TomRoleScopes tomRoleScopes);

	TomRoleScopes selectScopeByRoilId(Integer role_id);
	
	void deleteByRoleId(Integer roleId);
}