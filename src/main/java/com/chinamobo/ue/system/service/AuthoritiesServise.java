package com.chinamobo.ue.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.system.dao.TomAuthoritiesTypeMapper;
import com.chinamobo.ue.system.entity.TomAuthoritiesType;
import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class AuthoritiesServise {
	@Autowired
	private TomAuthoritiesTypeMapper tomAuthoritiesTypeMapper;

	public List<TomAuthoritiesType> selectAll(List<TomRole> role) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomAuthoritiesType> selectAllType = tomAuthoritiesTypeMapper.selectAllType();
		// 查找所有分类
		for (TomAuthoritiesType auth : selectAllType) {
			// 查找所有角色
			List<TomAuthoritiesType> list = new ArrayList<TomAuthoritiesType>();
			for (TomRole tomRole : role) {
				List<TomAuthoritiesType> selectAll = tomAuthoritiesTypeMapper.selectByRoleId(tomRole.getRoleId());
				auth.setTypeFunction(auth.getAuthorityTypeName());
				// auth.setList(new ArrayList<TomAuthorities>());

				// 查找所有权限
				for (TomAuthoritiesType auth1 : selectAll) {
					if (auth.getAuthorityTypeName().equals(auth1.getAuthorityTypeName())) {
						int i = 0;
						// 查找类型下的集合
						if (list.size() == 0) {
							list.add(auth1);
						} else {
							for (TomAuthoritiesType tomAuthorities : list) {

								if (auth1.getTypeFunction().equals(tomAuthorities.getTypeFunction())) {
									i++;
								}
							}
							if (i == 0) {
								list.add(auth1);
							}

						}
					}
				}

			}
			auth.setList(list);
		}
		return selectAllType;
	}

	public TomAuthoritiesType selectByPrimaryKey(int authorityTypeId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomAuthoritiesTypeMapper.selectByPrimaryKey(authorityTypeId);

	}
}
