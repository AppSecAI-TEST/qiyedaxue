package com.chinamobo.ue.ums.shiro.realm;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.StringUtil;
import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.cache.redis.RedisGetData;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.shiro.ApiUserToken;
import com.chinamobo.ue.ums.shiro.ShiroPermission;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.shiro.realm.ApiUserRealm.ApiUser;

@Component
public class UeApiRealm extends AuthorizingRealm {

	@Autowired
	private SystemService systemService;
	@Autowired
	private AbstractCacheManager redisCacheManager;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		if (primaryPrincipal instanceof ApiUser) {
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
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		if(arg0 instanceof ApiUserToken){//只验证支持的token
			ApiUserToken apiUserToken = (ApiUserToken) arg0;
			if(apiUserToken.getPassword() == null){
				System.out.println("token为空");
				return null;
			}
			String auth = new String(apiUserToken.getPassword());
			String account = apiUserToken.getUsername();
			String accountPassword = new String(apiUserToken.getPassword());
			if(StringUtils.isNotBlank(auth)){
				Object userLogCache;
				TomUserLog tomUserLog = new TomUserLog();
				try {//从缓存中获取用户信息;
					userLogCache = redisCacheManager.getCacheData(new RedisGetData("tomUserLogToken", accountPassword));
					//TomUserLog tomUserLog=systemService.getUserByToken(auth);
					if (userLogCache != null && StringUtil.isNotBlank(userLogCache.toString())) {
						tomUserLog = JSON.parseObject(userLogCache.toString(), TomUserLog.class);
					}else{
						tomUserLog = systemService.getUserByToken(auth);
						JsonMapper mapper = new JsonMapper();
						if(tomUserLog != null){
							redisCacheManager.addCacheData(new RedisCacheData("tomUserLogCode", tomUserLog.getCode(), mapper.toJson(tomUserLog)));// code为key缓存log
							if (StringUtil.isNotBlank(tomUserLog.getToken())) {
								redisCacheManager.addCacheData(new RedisCacheData("tomUserLogToken", tomUserLog.getToken(), mapper.toJson(tomUserLog)));// token为key缓存log
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(tomUserLog == null){
					throw new UnknownAccountException();
				}else {
					return new SimpleAuthenticationInfo(new ApiUser(0, account, account, tomUserLog.getName(), systemService.apiRole(),
							systemService.apiPermission(),null,accountPassword,null,tomUserLog),auth,getName());
				}
				
			}else{
				System.out.println("token为空");
				throw new UnknownAccountException(auth);
			}
		}
		return null;
	}
	
}