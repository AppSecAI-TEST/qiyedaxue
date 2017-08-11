package com.chinamobo.ue.system.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomAdminRolesMapper;
import com.chinamobo.ue.system.dao.TomRoleMapper;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomAdminRoles;
import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;

@Service
public class AdminServise {
	@Autowired
	private TomAdminMapper tomAdminMapper;
	@Autowired
	private TomRoleMapper tomRoleMapper;
	@Autowired
	private TomAdminRolesMapper tomAdminRoleMapper;

	@Autowired
	private AdminRolesServise adminRolesServise;
	
	String superCode = Config.getProperty("superCode");
	/**
	 * 
	 * 功能描述：[新增管理员]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年7月21日 下午4:38:52
	 * @param tomAdmin
	 */
	@Transactional
	public void insertSelective(TomAdmin tomAdmin) {
		Date date = new Date();
		tomAdmin.setCreateTime(date);
		tomAdmin.setUpdateTime(date);
		tomAdmin.setStatus("Y");
		tomAdminMapper.insertSelective(tomAdmin);
	}

	@Transactional
	public void updateByPrimaryKey(TomAdmin tomAdmin) {
		Date date = new Date();
		tomAdmin.setUpdateTime(date);
		tomAdminMapper.updateByPrimaryKeySelective(tomAdmin);

	}

	public PageData<TomAdmin> findPage(int pageNum, int pageSize, String name) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomAdmin> page = new PageData<TomAdmin>();
		// if
		Map<Object, Object> map1 = new HashMap<Object, Object>();

//		if (name != null) {
//			name = name.replaceAll("/", "//");
//			name = name.replaceAll("%", "/%");
//			name = name.replaceAll("_", "/_");
//		}

		map1.put("name", name);
		map1.put("superCode", superCode);
		int count = tomAdminMapper.countByExample(map1);

		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("name", name);
		map.put("superCode", superCode);
		List<TomAdmin> list = tomAdminMapper.selectByMany(map);
		// 把角色名称查询出来放到roles里
		for (TomAdmin tomadmin : list) {
			List<TomRole> selectByAdlinRoles = tomRoleMapper.selectByAdlinRoles(tomadmin.getAdminId());
			String roles = "";
			for (int i = 0; i < selectByAdlinRoles.size(); i++) {
				TomRole tomrole = selectByAdlinRoles.get(i);
				if (i == selectByAdlinRoles.size() - 1) {
					roles = roles + tomrole.getRoleName();
				} else {
					roles = roles + tomrole.getRoleName() + ",";
				}
			}
			tomadmin.setRoles(roles);
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;

	}

	@Transactional
	public void deleteByPrimaryKey(int adminId) {
		tomAdminMapper.deleteByPrimaryKey(adminId);
		tomAdminRoleMapper.deleteByAdminId(adminId);

	}

	public TomAdmin selectByAdminId(int adminId) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomAdminMapper.selectByAdminId(adminId);

	}

	@Transactional
	public void updateStatus(TomAdmin tomAdmin) {
		Date date = new Date();
		tomAdmin.setUpdateTime(date);
		tomAdminMapper.updateStatus(tomAdmin);

	}

	public PageData<TomAdmin> selectAdmin(int pageNum, int pageSize, String name) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomAdmin> page = new PageData<TomAdmin>();
		// if
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		map1.put("superCode", superCode);
		map1.put("name", name);
		int count = tomAdminMapper.countSelectAdmin(map1);

		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("name", name);
		map.put("superCode", superCode);
		List<TomAdmin> list = tomAdminMapper.selectAdmin(map);
		// 把角色名称查询出来放到roles里
		for (TomAdmin tomadmin : list) {
			List<TomRole> selectByAdlinRoles = tomRoleMapper.selectByAdlinRoles(tomadmin.getAdminId());
			String roles = "";
			for (int i = 0; i < selectByAdlinRoles.size(); i++) {
				TomRole tomrole = selectByAdlinRoles.get(i);
				if (i == selectByAdlinRoles.size() - 1) {
					roles = roles + tomrole.getRoleName();
				} else {
					roles = roles + tomrole.getRoleName() + ",";
				}
			}
			tomadmin.setRoles(roles);
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;

	}

	public TomAdmin selectByCode(String code) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomAdminMapper.getAdminByCode(code);
	}
	@Transactional
	public void insertSelectiveService(String code,String name, String roleid){
		ShiroUser user = ShiroUtils.getCurrentUser();
		TomAdmin tomAdmin = new TomAdmin();
		tomAdmin.setCode(code);
		tomAdmin.setName(name);
		tomAdmin.setCreator(user.getName());
		tomAdmin.setOperator(user.getName());
		tomAdmin.setCreatorId(user.getCode());
		Date date = new Date();
		tomAdmin.setCreateTime(date);
		tomAdmin.setUpdateTime(date);
		tomAdmin.setStatus("Y");
		tomAdminMapper.insertSelective(tomAdmin);

		String role[] = roleid.split(",");
		for (int i = 0; i < role.length; i++) {

			TomAdminRoles tomAdminRoles = new TomAdminRoles();
			tomAdminRoles.setAdminId(tomAdmin.getAdminId());
			tomAdminRoles.setRoleId(Integer.parseInt(role[i].toString()));

			tomAdminRoles.setCreator(user.getName());
			tomAdminRoles.setOperator(user.getName());
			tomAdminRoles.setCreatorId(user.getCode());
			adminRolesServise.insertSelective(tomAdminRoles);
		}
	}
	@Transactional
	public void updateUserInFoMaTion(String code, String name, String roleid, Integer adminid) {
		ShiroUser user = ShiroUtils.getCurrentUser();
		TomAdmin tomAdmin = new TomAdmin();
		tomAdmin.setCode(code);
		tomAdmin.setName(name);
		tomAdmin.setOperator(user.getName());
		tomAdmin.setAdminId(adminid);
		Date date = new Date();
		tomAdmin.setUpdateTime(date);
		tomAdminMapper.updateByPrimaryKeySelective(tomAdmin);

		adminRolesServise.deleteByAdminId(adminid);
		String role[] = roleid.split(",");
		for (int i = 0; i < role.length; i++) {
			TomAdminRoles tomAdminRoles = new TomAdminRoles();
			tomAdminRoles.setAdminId(adminid);
			tomAdminRoles.setRoleId(Integer.parseInt(role[i].toString()));
			tomAdminRoles.setCreator(user.getName());
			adminRolesServise.insertSelective(tomAdminRoles);
		
	}
	
	}
}
