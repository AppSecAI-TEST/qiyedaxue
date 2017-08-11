package com.chinamobo.ue.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.system.dao.TomAuthoritiesTypeMapper;
import com.chinamobo.ue.system.entity.TomAuthoritiesType;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class AuthoritiesTypeServise {
	@Autowired
	private TomAuthoritiesTypeMapper tomAuthoritiesTypeMapper;

	public List<TomAuthoritiesType> selectAuthoritiesByRoilId(Integer role_id) {

		return tomAuthoritiesTypeMapper.selectAuthoritiesByRoilId(role_id);
	}

	public List<TomAuthoritiesType> findAll() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		// TODO Auto-generated method stub
		return tomAuthoritiesTypeMapper.findAll();
	}

}
