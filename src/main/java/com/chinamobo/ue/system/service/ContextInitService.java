package com.chinamobo.ue.system.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomAdminRolesMapper;
import com.chinamobo.ue.system.dao.TomAuthoritiesMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.dao.TomRoleAuthoritiesMapper;
import com.chinamobo.ue.system.dao.TomRoleScopesMapper;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomAdminRoles;
import com.chinamobo.ue.system.entity.TomAuthorities;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.system.entity.TomRoleAuthorities;
import com.chinamobo.ue.system.entity.TomRoleScopes;
import com.chinamobo.ue.utils.Config;

@Component
public class ContextInitService {

	@Autowired
	private TomAdminMapper adminMapper;
	@Autowired
	private TomAdminRolesMapper adminRoleMapper;
	@Autowired
	private TomAuthoritiesMapper authMapper;
	@Autowired
	private TomRoleAuthoritiesMapper roleAuthMapper;
	@Autowired
	private TomRoleScopesMapper roleScopeMapper;
	@Autowired
	private TomOrgMapper orgMapper;
	
	@Transactional
	/**
	 * 
	 * 功能描述：[通过配置文件设置超级管理员]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月20日 下午5:02:48
	 */
	public void init(){
		
		if("true".equals(Config.getProperty("init"))){
			roleAuthMapper.deleteByPrimaryKey(0);
			roleScopeMapper.deleteByRoleId(0);
			List<TomAuthorities> authList=authMapper.selectAllAuth();
			for(TomAuthorities auth:authList){
				TomRoleAuthorities roleAuth=new TomRoleAuthorities();
				roleAuth.setRoleId(0);
				roleAuth.setAuthorityId(auth.getAuthorityId());
				roleAuth.setCreateTime(new Date());
				roleAuth.setCreator("sys");
				roleAuth.setOperator("sys");
				roleAuth.setStatus("Y");
				roleAuth.setUpdateTime(new Date());
				roleAuthMapper.insert(roleAuth);
			}
			List<TomOrg> orgList=orgMapper.select();
			StringBuffer scopes=new StringBuffer();
			for(TomOrg org:orgList){
				if(null==org.getPkFatherorg()){
				scopes.append(","+org.getPkOrg());
				}
			}
			TomRoleScopes roleScope=new TomRoleScopes();
			roleScope.setCreateTime(new Date());
			roleScope.setUpdateTime(new Date());
			roleScope.setOperator("sys");
			roleScope.setCreator("sys");
			roleScope.setRoleId(0);
			roleScope.setRoleType("1");
			roleScope.setRoleScope(scopes.substring(1));
			roleScopeMapper.insert(roleScope);
		}
		String superCode=Config.getProperty("superCodeUe");
		if(superCode==null){
			superCode="M00001";
		}
		TomAdmin admin=adminMapper.getAdminByCode(superCode);
		if(admin==null){
			admin=new TomAdmin();
			admin.setCode(superCode);
			admin.setCreateTime(new Date());
			admin.setCreator("sys");
			admin.setName("超级管理员");
			admin.setOperator("sys");
			admin.setStatus("Y");
			admin.setUpdateTime(new Date());
			adminMapper.insertSelective(admin);
		}
		TomAdminRoles adminRole=adminRoleMapper.selectByRoleid(0);
		adminRole.setAdminId(admin.getAdminId());
		adminRole.setUpdateTime(new Date());
		adminRoleMapper.updateByRoleId(adminRole);
	}
}
