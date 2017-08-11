package com.chinamobo.ue.system.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.system.dao.TomRoleScopesMapper;
import com.chinamobo.ue.system.entity.TomRoleScopes;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class RoleScopesServise {
	@Autowired
	private TomRoleScopesMapper tomRoleScopesMapper;
	@Transactional
	public void insert(TomRoleScopes tomRoleScopes) {
		Date date = new Date();
		tomRoleScopes.setCreateTime(date);
		tomRoleScopes.setUpdateTime(date);
		tomRoleScopesMapper.insert(tomRoleScopes);

	}
	@Transactional
	public void updateByPrimaryKeySelective(TomRoleScopes tomRoleScopes) {
		Date date = new Date();
		tomRoleScopes.setUpdateTime(date);
		tomRoleScopesMapper.updateByPrimaryKeySelective(tomRoleScopes);

	}

	public TomRoleScopes selectScopeByRoilId(Integer role_id) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomRoleScopesMapper.selectScopeByRoilId(role_id);
	}

}
