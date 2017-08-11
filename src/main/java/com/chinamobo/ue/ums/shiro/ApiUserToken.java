package com.chinamobo.ue.ums.shiro;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.ums.shiro.token.AbstractToken;
import com.chinamobo.ue.ums.util.Encodes;
import com.chinamobo.ue.ums.util.Servlets;

@Component
public class ApiUserToken extends AbstractToken{
	
	private static final long serialVersionUID = -3362783521812330535L;
	private String userId;
	private String userRoleType;
	
	public ApiUserToken() {
	}
	
	public ApiUserToken(String host, String auth, String userName, String userId, String userRoleType) {
		super(userName, auth, true, host);
		this.userId = userId;
		this.userRoleType = userRoleType;
	}
	public ApiUserToken(String userName,String passWord){
		super(userName,passWord);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserRoleType() {
		return userRoleType;
	}

	public void setUserRoleType(String userRoleType) {
		this.userRoleType = userRoleType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String getAuth(ServletRequest request) {
		String auth = WebUtils.getCleanParam(request, DEFAULT_AUTH_PARAM);
		if(StringUtils.isBlank(auth)){
			auth = WebUtils.toHttp(request).getHeader(DEFAULT_AUTH_HEADER);
			if(StringUtils.isNotBlank(auth)){
				if(StringUtils.startsWith(auth, DEFAULT_AUTH_BASIC)){
					auth = StringUtils.trimToEmpty(StringUtils.substringAfter(auth, DEFAULT_AUTH_BASIC));
				}
			}
		}
		return auth;
	}
	/**
	 * 创建ApiUserToken;
	 * Author : Wchao;
	 */
	@Override
	protected AbstractToken createToken(ServletRequest request)throws Exception{
		String auth = getAuth(request);
		if(StringUtils.isNotBlank(auth)){
			String userName = WebUtils.getCleanParam(request, DEFAULT_USER_NAME_PARAM);
			if(StringUtils.isNotBlank(userName)){
				userName = new String(userName.getBytes(DEFAULT_ENCODE_ISO), DEFAULT_ENCODE_GBK);
			}
			String userId = WebUtils.getCleanParam(request, DEFAULT_USER_ID_PARAM);
			
			if(StringUtils.contains(auth, DEFAULT_AUTH_USER_SEP)){
				String authWithUserinfo = auth;
				auth = StringUtils.substringAfter(authWithUserinfo, DEFAULT_AUTH_USER_SEP);
				if(StringUtils.isBlank(userName)){
					String userNameIdBase64Str = StringUtils.substringBefore(authWithUserinfo, DEFAULT_AUTH_USER_SEP);
					String userNameIdStr = Encodes.decodeBase64AsString(userNameIdBase64Str, DEFAULT_ENCODE_GBK);
					if(StringUtils.contains(userNameIdStr, DEFAULT_USER_ID_NAME_SEP)){
						userName = StringUtils.substringAfter(userNameIdStr, DEFAULT_USER_ID_NAME_SEP);
						if(userId == null || "".equals(userId)){
							userId = StringUtils.substringBefore(userNameIdStr, DEFAULT_USER_ID_NAME_SEP);
						}
					}else{
						userName = userNameIdStr;
					}
				}
			}
			String host = Servlets.getHost(request);
			
			String userRoleType = WebUtils.getCleanParam(request, DEFAULT_USER_ROLE_TYPE_PARAM);
			if(StringUtils.isBlank(userRoleType)){
				userRoleType = DEFAULT_USER_ROLE_TYPE;
			}
			return new ApiUserToken(host, auth, userName, userId, userRoleType);
		}
		return new ApiUserToken();
	}
}
