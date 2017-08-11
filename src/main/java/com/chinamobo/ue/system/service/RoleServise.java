package com.chinamobo.ue.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.dao.TomAdminRolesMapper;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.dao.TomRoleMapper;
import com.chinamobo.ue.system.entity.TomAdminRoles;
import com.chinamobo.ue.system.entity.TomAuthoritiesType;
import com.chinamobo.ue.system.entity.TomAuthoritiesTypeRole;
import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.system.entity.TomRoleAuthorities;
import com.chinamobo.ue.system.entity.TomRoleScopes;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;

@Service
public class RoleServise {
	@Autowired
	private DeptServise deptServise;
	@Autowired
	private OrgServise orgServise;
	@Autowired
	private TomRoleMapper TomRoleMapper;
	@Autowired
	private TomAdminRolesMapper tomAdminRolesMapper;
	@Autowired
	private TomOrgMapper tomOrgMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private RoleScopesServise roleScopesServise;
	@Autowired
	private AuthoritiesTypeRoleServise authoritiesTypeRoleServise;
	@Autowired
	private AuthoritiesServise authoritiesServise;
	@Autowired
	private RoleAuthoritiesServise roleAuthoritiesServise;


	@Transactional
	public Integer insert(TomRole tomRole) {
		tomRole.setStatus("Y");
		return TomRoleMapper.insert(tomRole);

	}

	@Transactional
	public void updateByPrimaryKeySelective(TomRole tomRole) {
		Date date = new Date();
		tomRole.setUpdateTime(date);
		TomRoleMapper.updateByPrimaryKeySelective(tomRole);

	}

	public PageData<TomRole> findPage(int pageNum, int pageSize, String role_name, Integer authority_id, String scope) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomRole> page = new PageData<TomRole>();
		// if
		Map<Object, Object> map1 = new HashMap<Object, Object>();

//		if (role_name != null) {
//			role_name = role_name.replaceAll("/", "//");
//			role_name = role_name.replaceAll("%", "/%");
//			role_name = role_name.replaceAll("_", "/_");
//		}

		map1.put("role_name", role_name);
		map1.put("authority_id", authority_id);
		map1.put("scope", scope);
		int count = TomRoleMapper.countByExample(map1);

		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("role_name", role_name);
		map.put("authority_id", authority_id);
		map.put("scope", scope);
		List<TomRole> list = TomRoleMapper.selectByMany(map);
		for (TomRole tomRole : list) {

			if (tomRole.getRoleType().equals("1")) {
				tomRole.setRoleType("公司级");
				String str = tomRole.getTomRoleScopes().getRoleScope();
				String[] st = str.split(",");
				String name = "";
				for (int j = 0; j < st.length; j++) {
					if (j == st.length - 1) {
						name = name + orgServise.selectById2(st[j]).getName();
					} else {
						name = name + orgServise.selectById2(st[j]).getName() + ",";
					}
					tomRole.getTomRoleScopes().setRoleScope(name);
				}
			} else {
				tomRole.setRoleType("部门级");
				String str = tomRole.getTomRoleScopes().getRoleScope();
				String[] st = str.split(",");
				String name = "";
				for (int j = 0; j < st.length; j++) {
					if (j == st.length - 1) {
						name = name + deptServise.selectByPKDept(st[j]).getName();
					} else {
						name = name + deptServise.selectByPKDept(st[j]).getName() + ",";
					}
					tomRole.getTomRoleScopes().setRoleScope(name);
				}
			}
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;
	}

	public TomRole selectByPrimaryKey(Integer role_id) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return TomRoleMapper.selectByPrimaryKey(role_id);
	}

	@Transactional
	public String deleteByPrimaryKey(int roleId) {
		List<TomAdminRoles> tomAdminRolesList = tomAdminRolesMapper.selectRoleByRoleId(roleId);
		if (tomAdminRolesList.size() == 0) {
			TomRoleMapper.deleteByPrimaryKey(roleId);
			return "success";
		} else {
			return "wrong";
		}
	}

	public List<TomRole> findAll() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return TomRoleMapper.findAll();

	}

	@Transactional
	public String updateStatus(TomRole tomRole) {
		List<TomAdminRoles> tomAdminRolesList = tomAdminRolesMapper.selectRoleByRoleId(tomRole.getRoleId());
		if (tomAdminRolesList.size() == 0) {
			TomRoleMapper.updateStatus(tomRole);
			return "success";
		} else {
			return "wrong";
		}
	}

	public List<Tree> findAllScope() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<Tree> list = new ArrayList<Tree>();
		for (Tree tree1 : tomDeptMapper.selectByAllTree()) {
			list.add(tree1);
		}
		for (Tree tree2 : tomOrgMapper.selectByAllTree()) {
			list.add(tree2);
		}
		return list;
	}

	@Transactional
	public void insertRole(String roleName, String roleType, String roleScope, String auid) {
		ShiroUser user = ShiroUtils.getCurrentUser();
		Date date = new Date();
		TomRole tomRole = new TomRole();
		TomRoleScopes tomRoleScopes = new TomRoleScopes();
		tomRole.setCreateTime(date);
		tomRole.setUpdateTime(date);
		tomRole.setCreator(user.getName());
		tomRole.setOperator(user.getName());
		tomRole.setCreatorId(user.getCode());
		tomRole.setRoleName(roleName);
		tomRole.setRoleType(roleType);
		insert(tomRole);

		tomRoleScopes.setCreator(user.getName());
		tomRoleScopes.setOperator(user.getName());
		tomRoleScopes.setRoleId(tomRole.getRoleId());
		tomRoleScopes.setRoleScope(roleScope);
		tomRoleScopes.setRoleType(roleType);
		tomRoleScopes.setCreatorId(user.getCode());
		roleScopesServise.insert(tomRoleScopes);
		String[] str = auid.split(",");
		Set set = new HashSet<String>();
		for (int i = 0; i < str.length; i++) {
			TomAuthoritiesTypeRole tomAuthoritiesTypeRole = new TomAuthoritiesTypeRole();
			tomAuthoritiesTypeRole.setAuthorityTypeId(Integer.parseInt(str[i].toString()));
			tomAuthoritiesTypeRole.setRoleId(tomRole.getRoleId());
			tomAuthoritiesTypeRole.setCreateTime(date);
			authoritiesTypeRoleServise.insert(tomAuthoritiesTypeRole);
			TomAuthoritiesType tomAuthoritiesType = authoritiesServise
					.selectByPrimaryKey(Integer.parseInt(str[i].toString()));
			String authorityId = tomAuthoritiesType.getAuthorityId();
			String[] str1 = authorityId.split(",");
			for (int j = 0; j < str1.length; j++) {
				set.add(str1[j]);
			}
		}
		Iterator i = set.iterator();
		String[] arrays = new String[set.size()];
		int num = 0;
		while (i.hasNext()) {
			String a = (String) i.next();
			arrays[num] = a;
			num = num + 1;
		}
		for (int j = 0; j < arrays.length; j++) {
			TomRoleAuthorities tomRoleAuthorities = new TomRoleAuthorities();
			tomRoleAuthorities.setAuthorityId(Integer.parseInt(arrays[j].toString()));
			tomRoleAuthorities.setCreateTime(date);
			tomRoleAuthorities.setCreator(user.getName());
			tomRoleAuthorities.setOperator(user.getName());
			tomRoleAuthorities.setRoleId(tomRole.getRoleId());
			tomRoleAuthorities.setStatus("Y");
			tomRoleAuthorities.setUpdateTime(date);
			tomRoleAuthorities.setCreatorId(user.getCode());
			roleAuthoritiesServise.insert(tomRoleAuthorities);

		}

	}

	@Transactional
	public void updateRole(String roleName, String roleType, String roleScope, String auid, Integer roleid) {
		ShiroUser user = ShiroUtils.getCurrentUser();
		Date date = new Date();
		TomRole tomRole = new TomRole();
		tomRole.setRoleName(roleName);
		tomRole.setRoleType(roleType);
		tomRole.setOperator(user.getName());
		tomRole.setRoleId(roleid);
		updateByPrimaryKeySelective(tomRole);
		TomRoleScopes tomRoleScopes = new TomRoleScopes();
		tomRoleScopes.setOperator(user.getName());
		tomRoleScopes.setRoleScope(roleScope);
		tomRoleScopes.setRoleType(roleType);
		tomRoleScopes.setRoleId(roleid);
		roleScopesServise.updateByPrimaryKeySelective(tomRoleScopes);
		roleAuthoritiesServise.deleteByPrimaryKey(roleid);
		authoritiesTypeRoleServise.deleteByPrimaryKey(roleid);
		String[] str = auid.split(",");

		for (int i = 0; i < str.length; i++) {
			TomAuthoritiesTypeRole tomAuthoritiesTypeRole = new TomAuthoritiesTypeRole();
			tomAuthoritiesTypeRole.setAuthorityTypeId(Integer.parseInt(str[i].toString()));
			tomAuthoritiesTypeRole.setRoleId(tomRole.getRoleId());
			tomAuthoritiesTypeRole.setCreateTime(date);
			authoritiesTypeRoleServise.insert(tomAuthoritiesTypeRole);
			TomAuthoritiesType tomAuthoritiesType = authoritiesServise
					.selectByPrimaryKey(Integer.parseInt(str[i].toString()));
			String authorityId = tomAuthoritiesType.getAuthorityId();
			String[] str1 = authorityId.split(",");
			for (int j = 0; j < str1.length; j++) {
				TomRoleAuthorities tomRoleAuthorities = new TomRoleAuthorities();
				tomRoleAuthorities.setAuthorityId(Integer.parseInt(str1[j].toString()));
				tomRoleAuthorities.setCreateTime(date);
				tomRoleAuthorities.setCreator(user.getName());
				tomRoleAuthorities.setOperator(user.getName());
				tomRoleAuthorities.setRoleId(tomRole.getRoleId());
				tomRoleAuthorities.setStatus("Y");
				tomRoleAuthorities.setUpdateTime(date);
				roleAuthoritiesServise.insert(tomRoleAuthorities);
			}
		}

	}
	public List<TomRole>  findRoleByCreator() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		ShiroUser user = ShiroUtils.getCurrentUser();
		List<TomRole> roles = user.getRoles();
		List<TomRole> roles2 = TomRoleMapper.selectByCreator(user.getCode());
		List<TomRole> list = new ArrayList<TomRole>();
		for(TomRole tomRole :roles){
			if(!user.getName().equals(tomRole.getCreator())){
				list.add(tomRole);
			}
		}
		for(TomRole tomRole2:roles2){
			list.add(tomRole2);
		}
		return list;
		
	}

}
