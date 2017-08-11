package com.chinamobo.ue.ums.shiro.realm;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.shiro.ShiroPermission;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.shiro.UeToken;
import com.chinamobo.ue.ums.shiro.realm.EleFormRealm.SysUser;


public class UeRealm extends AuthorizingRealm {
	
	@Autowired
	private SystemService systemService;


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		if (primaryPrincipal instanceof SysUser) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			ShiroUser shiroUser = (ShiroUser) primaryPrincipal;
			for (ShiroPermission perm : shiroUser.getShiroPermissions()) {
				info.addRole(perm.getAuthorityurl());
			}
			return info;
		}
		return null;
	}


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		if(token instanceof UeToken){
			UeToken ueToken= (UeToken) token;
			String userName=ueToken.getUsername();
			String password=new String(ueToken.getPassword());
			TomUserLog user=systemService.getUserbyCep(userName);
			if(user == null){
				throw new UnknownAccountException();
			}else if(user.getPassword().equals(password)){
				TomAdmin admin = systemService.getAdminByCode(user.getCode());
				if (admin != null && "Y".equals(admin.getStatus())) {
					AuthenticationInfo authcInfo = null;
					try {
						authcInfo = new SimpleAuthenticationInfo(
								new SysUser(admin.getAdminId(), admin.getCode(), ueToken.getUsername(), admin.getName(),
										systemService.listRoleByAminId(admin.getAdminId()),
										systemService.listShiroPermissionByAdminId(admin.getAdminId()),user),
								user.getPassword(), getName());
					} catch (Exception e) {
						System.out.println("ex");
						e.printStackTrace();
					}
					return authcInfo;
				}else {
					// 密码锁定或不是管理员
					throw new LockedAccountException();
				}
			}else {
				throw new IncorrectCredentialsException();
			}
		}
		return null;
	}


}

