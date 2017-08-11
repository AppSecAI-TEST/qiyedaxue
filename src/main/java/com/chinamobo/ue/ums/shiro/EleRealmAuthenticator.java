package com.chinamobo.ue.ums.shiro;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;


public class EleRealmAuthenticator extends ModularRealmAuthenticator {
	protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
		AuthenticationInfo info=null;
		for (Realm realm : realms) {
			if (realm.supports(token)) {//仅验证被支持的token。系统中，一个token对于一个realm
				info= realm.getAuthenticationInfo(token);
				if(info!=null){
					return info;
				}
			}
		}
		return null;
	}
	
}
