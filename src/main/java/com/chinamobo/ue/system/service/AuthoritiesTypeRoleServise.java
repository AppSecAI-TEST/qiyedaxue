package com.chinamobo.ue.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.system.dao.TomAuthoritiesTypeRoleMapper;
import com.chinamobo.ue.system.entity.TomAuthoritiesTypeRole;

@Service
public class AuthoritiesTypeRoleServise {
	@Autowired
	private TomAuthoritiesTypeRoleMapper tomAuthoritiesTypeRoleMapper;

	@Transactional
	public void insert(TomAuthoritiesTypeRole tomAuthoritiesTypeRole) {
		tomAuthoritiesTypeRoleMapper.insert(tomAuthoritiesTypeRole);
	}

	@Transactional
	public void deleteByPrimaryKey(Integer roleid) {
		// TODO Auto-generated method stub
		tomAuthoritiesTypeRoleMapper.deleteByPrimaryKey(roleid);
	}

}
