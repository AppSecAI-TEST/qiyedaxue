package com.chinamobo.ue.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomAdminRolesMapper;
import com.chinamobo.ue.system.dao.TomAuthoritiesMapper;
import com.chinamobo.ue.system.dao.TomRoleAuthoritiesMapper;
import com.chinamobo.ue.system.dao.TomRoleMapper;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomAuthorities;
import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.PermissionTemp;
import com.chinamobo.ue.ums.shiro.ShiroPermission;

@Service
public class SystemService {
	@Autowired
	private TomAdminMapper tomAdminMapper;
	
	@Autowired
	private TomUserLogMapper tomUserLogMapper;
	
	@Autowired
	private TomAdminRolesMapper tomAdminRolesMapper;
	
	@Autowired
	private TomRoleAuthoritiesMapper tomRoleAuthoritiesMapper;
	
	@Autowired
	private TomRoleMapper tomRoleMapper;
	@Autowired
	private TomAuthoritiesMapper authMapper;
	/**
	 * @param name
	 * @return
	 */
	public TomUserLog getUserbyName(String name){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomUserLogMapper.getUserbyName(name);
	}
	/**
	 * 
	 * 功能描述：[email查询]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年6月7日 下午3:59:46
	 * @param email
	 * @return
	 */
	public TomUserLog getUserByEmail(String email){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomUserLogMapper.getUserByEmail(email);
	}
	/**根据用户code获取用户
	 * @param name
	 * @return
	 */
	public TomUserLog getUserbyCode(String code){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomUserLogMapper.getUserByCode(code);
	}
	
	public TomUserLog getUserbyCep(String code){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomUserLogMapper.getUserByEMphone(code);
	}
	
	/**根据用户code获取管理员用户
	 * @param name
	 * @return
	 */
	public TomAdmin getAdminByCode(String code){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomAdminMapper.getAdminByCode(code);
	}
	/**保存登陆的用户token,失效时间
	 * @param userLog
	 * @return
	 */
	public int insertUser(TomUserLog userLog){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomUserLogMapper.insert(userLog);
	}
	/**根据token查用户
	 * @param userLog
	 * @return
	 */
	public TomUserLog getUserByToken(String token) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomUserLogMapper.getUserByToken(token);
	}
	/**根据用户名获取管理员用户
	 * @param name
	 * @return
	 */
	public TomAdmin getAdminByName(String name){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomAdminMapper.getAdminByName(name);
	}
	
	/**根据管理员id获取所属角色
	 * @param id
	 * @return
	 */
	public List<TomRole> listRoleByAminId(int id){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomAdminRolesMapper.selectRoleByAminId(id);
	}
	
	/**根据管理员id获取权限功能
	 * @param id
	 * @return
	 */
	public List<TomAuthorities> listAuthoritiesByAdminId(int id){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomRoleAuthoritiesMapper.selectAuthoritiesByAdminId(id);
	}
	
	/**根据角色id获取权限功能
	 * @param id
	 * @return
	 */
	public List<TomAuthorities> listAuthoritiesByRoleId(int id){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		return tomRoleAuthoritiesMapper.selectAuthoritiesByRoleId(id);
	}
	
	/**
	 * 功能描述：根据管理员id获取对应的权限对象
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月1日 下午5:36:45
	 * @param id
	 * @return
	 */
	public List<ShiroPermission> listShiroPermissionByAdminId(int id){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		List<PermissionTemp> perms=tomAdminRolesMapper.selectPermByAdminId(id);
		List<ShiroPermission> shiroPermissions=new ArrayList<ShiroPermission>();
		if(perms!=null&&!perms.isEmpty()){
			for(PermissionTemp p:perms){
				if(p.getRolescope()!=null){
					String[] scopestr=p.getRolescope().split(",");
					if(scopestr.length>0){
						for(String orgid:scopestr){
							ShiroPermission sPermission=new ShiroPermission();
							sPermission.setAuthorityid(p.getAuthorityid());
							sPermission.setAuthoritymethod(p.getAuthoritymethod());
							sPermission.setAuthorityname(p.getAuthorityname());
							sPermission.setAuthoritytype(p.getAuthoritytype());
							sPermission.setAuthorityurl(p.getAuthorityurl());
							sPermission.setroleid(p.getroleid());
							sPermission.setRoletype(p.getRoletype());
							sPermission.setOrgdeptcode(orgid);
							shiroPermissions.add(sPermission);
						}
					}
				}
				
			}
		}
		return shiroPermissions;
	}
	
	public Result apiLogin(String userName,String password){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		Result result=new Result();
		TomUserLog userLog=tomUserLogMapper.selectByLoginName(userName);
		if(userLog!=null){
			if(userLog.getPassword().equals(password)){
				
			}else{
				result.setStatus("N");
				result.setError_code(ErrorCode.SERVICE_DATA_ERROR);
				result.setError_msg("用户名密码错误");
			}
		}else {
			result.setStatus("N");
			result.setError_code(ErrorCode.USER_ILLEGAL);
			result.setError_msg("用户不存在");
		}
		return result;
	}
	/**
	 * 
	 * 功能描述：[构造api权限]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年5月12日 下午7:33:35
	 * @return
	 */
	public List<ShiroPermission> apiPermission(){
		List<ShiroPermission> permissionList=new ArrayList<ShiroPermission>();
		ShiroPermission get=new ShiroPermission();
		ShiroPermission post=new ShiroPermission();
		get.setAuthorityid("146");
		get.setAuthoritymethod("1");
		get.setAuthorityname("apiGet");
		get.setAuthoritytype("-");
		get.setAuthorityurl("/enterpriseuniversity/services/api/getMethod");
		get.setroleid("-1");
		get.setRoletype("2");
		permissionList.add(get);
		post.setAuthorityid("147");
		post.setAuthoritymethod("2");
		post.setAuthorityname("apiPost");
		post.setAuthoritytype("-");
		post.setAuthorityurl("/enterpriseuniversity/services/api/postMethod");
		post.setroleid("-1");
		post.setRoletype("2");
		permissionList.add(post);
		return permissionList;
	}
	/**
	 * 
	 * 功能描述：[构造学员角色]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年5月12日 下午7:33:51
	 * @return
	 */
	public List<TomRole> apiRole(){
		List<TomRole> roleList=new ArrayList<TomRole>();
		TomRole role=new TomRole();
		role.setRoleId(-1);
		role.setRoleName("学员");
		role.setRoleType("1");
		role.setStatus("Y");
		roleList.add(role);
		return roleList;
	}
	
	public void updateByCode(TomUserLog user){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		tomUserLogMapper.updateByCode(user);
	}
}
