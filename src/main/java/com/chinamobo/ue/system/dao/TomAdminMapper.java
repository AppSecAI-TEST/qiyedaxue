package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.system.entity.TomAdmin;

public interface TomAdminMapper {
    int deleteByPrimaryKey(Integer adminId);

    int insert(TomAdmin record);

    void insertSelective(TomAdmin record);

    TomAdmin selectByPrimaryKey(Integer adminId);

    int updateByPrimaryKeySelective(TomAdmin record);

    int updateByPrimaryKey(TomAdmin record);
    
    TomAdmin getAdminByName(String name);

	int countByExample(Map<Object, Object> map1);
	
	int countByExampleAll(Map<Object, Object> map1);

	List<TomAdmin> selectByMany(Map<Object, Object> map);

	TomAdmin selectByAdminId(int adminId);

	void updateStatus(TomAdmin tomAdmin);
	TomAdmin getAdminByCode(String code);

	int countSelectAdmin(Map<Object, Object> map1);

	List<TomAdmin> selectAdmin(Map<Object, Object> map);
}