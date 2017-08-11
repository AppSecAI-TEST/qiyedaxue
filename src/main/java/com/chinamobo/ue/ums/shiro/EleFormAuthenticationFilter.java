package com.chinamobo.ue.ums.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.chinamobo.ue.ums.util.ShiroUtils;

public class EleFormAuthenticationFilter extends FormAuthenticationFilter{



	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
//		String username = getUsername(request);
//		String password = getPassword(request);
//		String orgCode = getOrgCode(request);
//		boolean rememberMe = isRememberMe(request);
//		String host = Servlets.getHost(request);
		String username = getUsername(request);
		String password = getPassword(request);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);  
		//”Remember Me” built-in:  
		token.setRememberMe(true);  
		return super.createToken(request, response);
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		ShiroUser shiroUser = ShiroUtils.getCurrentUser();
		super.issueSuccessRedirect(request, response);
	}

	
}
