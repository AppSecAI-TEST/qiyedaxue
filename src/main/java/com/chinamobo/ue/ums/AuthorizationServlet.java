package com.chinamobo.ue.ums;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinamobo.ue.utils.StringUtil;
import com.para.esc.sdk.oauth.OAuthService;
import com.para.esc.sdk.oauth.utils.OAuthConfigUtil;

public class AuthorizationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6141729519391049460L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestContextPath = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		requestContextPath = requestContextPath.substring(0,
				requestContextPath.indexOf(contextPath) + contextPath.length());


		String issuer = request.getParameter(OAuthConfigUtil.IDP);
		if(StringUtil.isEmpty(issuer)){
			OAuthConfigUtil oauthConfig = new OAuthConfigUtil("ecp");
			response.sendRedirect(oauthConfig.getValue("config.base.url"));
		}else {
	//		issuer="ecp";
			OAuthConfigUtil oauthConfig = new OAuthConfigUtil(issuer);
			request.getSession().setAttribute(OAuthConfigUtil.IDP, issuer);
			OAuthService service = new OAuthService(oauthConfig);
			if (request.getSession().getAttribute(issuer) == null) {
				request.getSession().setAttribute(issuer, service);
			}
			response.sendRedirect(service.getAuthorizationUrl());
		}
	}
}
