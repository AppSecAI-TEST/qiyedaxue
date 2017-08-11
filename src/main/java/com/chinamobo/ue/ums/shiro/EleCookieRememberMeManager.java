package com.chinamobo.ue.ums.shiro;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.util.WebUtils;

public class EleCookieRememberMeManager extends CookieRememberMeManager{

	public class PhisCookieRememberMeManager extends CookieRememberMeManager {
		protected byte[] getRememberedSerializedIdentity(SubjectContext subjectContext) {
			HttpServletRequest request = WebUtils.getHttpRequest(subjectContext);
			if(request != null){
				String auth = WebUtils.getCleanParam(request, ApiUserAuthenticationFilter.DEFAULT_AUTH_PARAM);
				if(StringUtils.isBlank(auth)){
					return super.getRememberedSerializedIdentity(subjectContext);
				}else{
					forgetIdentity(subjectContext);
				}
			}
			return null;
		}
	}
}
