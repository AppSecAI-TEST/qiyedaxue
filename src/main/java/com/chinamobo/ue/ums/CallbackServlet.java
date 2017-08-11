package com.chinamobo.ue.ums;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;

import com.chinamobo.ue.ums.shiro.UeToken;
import com.chinamobo.ue.utils.PathUtil;
import com.para.esc.sdk.oauth.OAuthService;
import com.para.esc.sdk.oauth.client.model.UserInfo;
import com.para.esc.sdk.oauth.exceptions.OAuthApiException;
import com.para.esc.sdk.oauth.model.Token;
import com.para.esc.sdk.oauth.utils.OAuthConfigUtil;

public class CallbackServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7262034286119336180L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OAuthService service = null;
		String issuer = (String) request.getSession().getAttribute(
				OAuthConfigUtil.IDP);
		issuer = "tecp";

		if (request.getSession().getAttribute(issuer) != null) {
			service = (OAuthService) request.getSession().getAttribute(issuer);
		} else {
			OAuthConfigUtil oauthConfig = new OAuthConfigUtil(issuer);
			service = new OAuthService(oauthConfig);
			request.getSession().setAttribute(issuer, service);
		}

		String code = request.getParameter("code");
//		 System.out.println("=====code:" + code + "===========");
		Token accessToken = service.getAccessToken(code);

//		 System.out.println("===========accessToken:" + accessToken +
//		 "===================");

		try {
			UserInfo user_ = new UserInfo(accessToken);
			UserInfo user = user_.requestUserInfo(service.getUserinfoUrl());
			if (user != null) {
				UeToken token = new UeToken(user.getId(), "password");
				token.setRememberMe(true);
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.getSession().setTimeout(3600000);
				try{
					currentUser.login(token);
					response.sendRedirect("/enterpriseuniversity/services/menu");
				}catch (LockedAccountException e) {
					e.printStackTrace();
					System.out.println("密码锁定或不是管理员");
					RequestDispatcher dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH+"/403.html");
					try {
						dispatcher .forward(request, response);					
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}catch (UnknownAccountException e) {
					e.printStackTrace();
					System.out.println("帐号不存在");
					RequestDispatcher dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH+"/404.html");
					try {
						dispatcher .forward(request, response);					
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (OAuthApiException e) {
			e.printStackTrace();
			throw new ServletException(e.getErrorMsg(),e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("user account not found",e);
		}
	}
}
