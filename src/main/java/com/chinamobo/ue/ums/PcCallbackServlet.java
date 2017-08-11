package com.chinamobo.ue.ums;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.PathUtil;
import com.para.esc.sdk.oauth.OAuthService;
import com.para.esc.sdk.oauth.client.model.UserInfo;
import com.para.esc.sdk.oauth.exceptions.OAuthApiException;
import com.para.esc.sdk.oauth.model.Token;
import com.para.esc.sdk.oauth.utils.OAuthConfigUtil;

public class PcCallbackServlet extends HttpServlet {
	
	private SystemService systemService;
	private ApplicationContext applicationContext;  
	  
    public PcCallbackServlet() {  
        super();  
    }  
  
    public void init(ServletConfig config) throws ServletException {  
        super.init(config);  
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());  
        systemService = (SystemService) applicationContext.getBean("systemService");  
    }  

	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

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
		issuer = "ecp";

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
				TomUserLog userlog=  systemService.getUserbyCode(user.getId());
				if (userlog !=null){
					String token="";
					if(userlog.getToken()!=null && userlog.getValidity().after(new Date())){
						token=userlog.getToken();
					}else {
						token=UUID.randomUUID().toString();
						userlog.setToken(token);
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DAY_OF_MONTH, 30);
						userlog.setValidity(c.getTime());
						systemService.updateByCode(userlog);
					}
					response.sendRedirect(Config.getProperty("pcIndex")+"views/index/index.html?token="+token);
				}else {
					System.out.println("帐号不存在");
					RequestDispatcher dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH+"/404.html");
					try {
						dispatcher .forward(request, response);					
					} catch (Exception e1) {
						e1.printStackTrace();
					}
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
