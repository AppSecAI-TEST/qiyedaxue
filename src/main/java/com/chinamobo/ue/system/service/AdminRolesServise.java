package com.chinamobo.ue.system.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.system.dao.TomAdminRolesMapper;
import com.chinamobo.ue.system.entity.TomAdminRoles;

@Service
public class AdminRolesServise {
	@Autowired
	private TomAdminRolesMapper tomAdminRolesMapper;

	@Transactional
	public void insertSelective(TomAdminRoles tomAdminRoles) {
		Date date = new Date();
		tomAdminRoles.setCreateTime(date);
		tomAdminRoles.setUpdateTime(date);
		tomAdminRolesMapper.insertSelective(tomAdminRoles);
	}

	@Transactional
	public void deleteByAdminId(Integer adminid) {
		tomAdminRolesMapper.deleteByAdminId(adminid);

	}

}
