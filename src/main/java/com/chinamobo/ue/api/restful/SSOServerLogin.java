//package com.chinamobo.ue.api.restful;
//
//import java.io.IOException;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.core.Context;
//
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.LockedAccountException;
//import org.apache.shiro.authc.UnknownAccountException;
//import org.apache.shiro.subject.Subject;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import com.chinamobo.ue.ums.shiro.EleFormToken;
//import com.chinamobo.ue.ums.shiro.ShiroUser;
//import com.chinamobo.ue.ums.util.ShiroUtils;
//import com.chinamobo.ue.utils.Config;
//import com.chinamobo.ue.utils.PathUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Path("/sso")
//@Scope("request")
//@Component
//public class SSOServerLogin {
//	
//	ObjectMapper mapper = new ObjectMapper(); 
//	
//	@GET
//	@Path("/login")
//	public void login(@Context HttpServletResponse response, @Context HttpServletRequest request) {
//		
//		String token=request.getParameter("token");
//		String account="abcdefg";
//		String URL=Config.getProperty("URL");
//		if(URL==null || URL==""){
//			URL="http://www.baidu.com";
//		}
//		if(token!=null){
//			try {
//				EleFormToken usertoken = new EleFormToken(account,token);
//				usertoken.setRememberMe(true);
//				Subject currentUser = SecurityUtils.getSubject();
//				currentUser.getSession().setTimeout(86400000);
//				currentUser.login(usertoken);
//				response.sendRedirect("/enterpriseuniversity/services/menu");
//			}catch (LockedAccountException e) {
//				e.printStackTrace();
//				System.out.println("密码锁定或不是管理员");
//				RequestDispatcher dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH+"/403.html");
//				try {
//					dispatcher .forward(request, response);					
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}catch (UnknownAccountException e) {
//				e.printStackTrace();
//				System.out.println("帐号不存在");
//				//token过期
//				try {
//					response.sendRedirect(URL+"/auth/entry?sys=EPXYW");
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else{
//			try {
//				response.sendRedirect(URL+"/auth/entry?sys=EPXYW");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	@GET
//	@Path("/getUser")
//	public String getUser(@Context HttpServletResponse response, @Context HttpServletRequest request) {
//		ShiroUser user=ShiroUtils.getCurrentUser();
//		try {
//			return mapper.writeValueAsString(user);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "{\"result\":\"error\"}"; 
//		}
//	}
//	
//	@GET
//	@Path("/logout")
//	public String logout(@Context HttpServletResponse response, @Context HttpServletRequest request) {
//		try {
//			String URL=Config.getProperty("URL");
//			String httpUrl=URL+"/auth/home";
//			return httpUrl;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "error"; 
//		}
//	}
//}
