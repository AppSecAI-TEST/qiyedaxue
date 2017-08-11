package com.chinamobo.ue.ums.shiro;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.ums.shiro.realm.UeApiRealm;
import com.chinamobo.ue.ums.shiro.token.TokenProtocol;
import com.chinamobo.ue.ums.util.ShiroUtils;


public class ApiUserAuthenticationFilter extends AuthenticatingFilter implements TokenProtocol{

	@Autowired
	private ApiUserToken apiUserToken;
	@Autowired
	private UeApiRealm ueApiRealm;
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse arg1) throws Exception {
		return apiUserToken.createToken(request);
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		AuthenticationInfo authInfo = null;
		try{
			authInfo = ueApiRealm.getAuthenticationInfo(apiUserToken.createToken(request));
		}finally {
			if (authInfo == null) {
				HttpServletRequest res=(HttpServletRequest) request;
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.setStatus(200);
				httpResponse.setCharacterEncoding("UTF-8");
				httpResponse.setContentType("application/json; charset=utf-8");
				PrintWriter out = null;

				try {
					out = httpResponse.getWriter();
					out.append(res.getParameter("callback")+"({\"error_code\":\"401\"})");
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						out.close();
					}
				}

			}
		}
		/*boolean fa = executeLogin(request, response);
		if (!fa) {
			HttpServletRequest res=(HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(200);
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.setContentType("application/json; charset=utf-8");
			PrintWriter out = null;

			try {
				out = httpResponse.getWriter();
				out.append(res.getParameter("callback")+"({\"error_code\":\"401\"})");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}

		}*/
		return true;
	}
	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		ShiroUser shiroUser = ShiroUtils.getCurrentUser();
		super.issueSuccessRedirect(request, response);
	}
	
}
