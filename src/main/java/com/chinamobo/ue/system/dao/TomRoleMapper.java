package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.system.entity.TomRole;

public interface TomRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(TomRole record);

    Integer  insertSelective(TomRole tomRole);

    TomRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(TomRole record);

    void updateByPrimaryKey(TomRole record);

	int countByExample(Map<Object, Object> map1);

	List<TomRole> selectByMany(Map<Object, Object> map);

	List<TomRole> findAll();

	void updateStatus(TomRole tomRole);
	
	List<TomRole> selectByAdlinRoles(int adminId);

	List<TomRole> selectByCreator(String code);
}