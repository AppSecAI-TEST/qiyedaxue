package com.chinamobo.ue.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.system.dao.TomRoleAuthoritiesMapper;
import com.chinamobo.ue.system.entity.TomRoleAuthorities;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class RoleAuthoritiesServise {
	@Autowired
	private TomRoleAuthoritiesMapper tomRoleAuthoritiesMapper;
	@Transactional
	public void insert(TomRoleAuthorities tomRoleAuthorities) {
		tomRoleAuthoritiesMapper.insert(tomRoleAuthorities);
	}
	@Transactional
	public void deleteByPrimaryKey(Integer roleid) {
		tomRoleAuthoritiesMapper.deleteByPrimaryKey(roleid);

	}

	public List<TomRoleAuthorities> selectAuthoritiesByRoilId(Integer role_id) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomRoleAuthoritiesMapper.selectAuthoritiesByRoilId(role_id);
	}

}
