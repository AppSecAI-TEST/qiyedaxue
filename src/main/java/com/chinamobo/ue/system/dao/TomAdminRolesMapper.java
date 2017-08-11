package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.system.entity.TomAdminRoles;
import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.ums.shiro.PermissionTemp;

public interface TomAdminRolesMapper {
    int insert(TomAdminRoles record);

    int insertSelective(TomAdminRoles record);
    
    List<TomRole> selectRoleByAminId(int id);
    
    List<PermissionTemp> selectPermissionTempByAdminId(int id);
    List<PermissionTemp> selectPermByAdminId(int id);
	void deleteByAdminId(Integer adminId);
	void deleteByRoleId(Integer roleId);
	TomAdminRoles selectByRoleid(Integer roleId);
	
	int updateByRoleId(TomAdminRoles record);
	
	 List<TomAdminRoles> selectRoleByRoleId(Integer roleId);
}