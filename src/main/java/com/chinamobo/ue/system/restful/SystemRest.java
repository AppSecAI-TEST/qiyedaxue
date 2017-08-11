package com.chinamobo.ue.system.restful;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.shiro.UeToken;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.PathUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.para.esc.sdk.oauth.utils.OAuthConfigUtil;

import net.sf.json.JSONObject;



@Path("/backend/sys")
@Scope("request")
@Component
public class SystemRest {
	private static Logger logger = LoggerFactory.getLogger(SystemRest.class);
	@Autowired
	private SystemService systemService;
	@Autowired
	private TomUserLogMapper tomUserLogMapper;

	@GET
	@Path("/loginpage")
	public void find(@Context HttpServletResponse response, @Context HttpServletRequest request) {
		RequestDispatcher dispatcher;
		dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH + "/login.html");
		try {
			dispatcher.forward(request, response);
		} catch (Exception e) {

		}
		// return new Viewable("/views/system/login.jsp");
	}

	@POST
	@Path("/login")
	public String login(@FormParam("userName") String userName, @FormParam("password") String password,
			@Context HttpServletResponse response, @Context HttpServletRequest request) {
		System.out.println("begin login......");
		if (userName == null) {
			System.out.println("login return null");
			return "null";
		}
		Subject currentUser = SecurityUtils.getSubject();
		UeToken token = new UeToken(userName, password);
		token.setRememberMe(true);
		try {
			currentUser.login(token);
			currentUser.getSession().setTimeout(3600000);
			System.out.println(userName+":登录成功......");
			return "success";
		} catch (UnknownAccountException e) {// 登录失败
			System.out.println("登录失败,帐号不存在或被禁用......");
			return "incorrectusername";
		} catch (IncorrectCredentialsException e) {
			System.out.println("登录失败,密码错误......");
			return "passworderror";
		} catch (LockedAccountException e) {
			System.out.println("登录失败,没有管理员权限......");
			return "permissiondenied";
		}
	}

	@POST
	@Path("/apilogin")
	@Produces({ MediaType.APPLICATION_JSON })
	public String apilogin(String json, @Context HttpServletResponse response, @Context HttpServletRequest request) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		String userName = jsonObject.getString("userName");
		String passWord = jsonObject.getString("passWord");
		TomUserLog user = systemService.getUserbyCep(userName);
		if (user == null || user.getStatus().equals("N")) {
			logger.error("用户不存在");
			return "{\"status\":\"N\",\"results\":\"用户不存在\",\"errorType\":\"incorrectusername\"}";
		} else if (user.getPassword().equals(passWord)) {
			String token = "";
			if (user.getValidity().before(new Date())) {
				user.setToken(UUID.randomUUID().toString());
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, 30);
				user.setValidity(c.getTime());
				tomUserLogMapper.updateByCode(user);
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			token = user.getToken();
			return "{\"status\":\"Y\",\"token\":\"" + token + "\",\"userId\":\"" + user.getCode() + "\",\"validity\":\""
					+ sf.format(user.getValidity()) + "\"}";
		} else {
			logger.error("密码错误");
			return "{\"status\":\"N\",\"results\":\"密码错误\",\"errorType\":\"passworderror\"}";
		}
	}

	@GET
	@Path("/logout")
	public String logout(@Context HttpServletResponse response)  {
		ShiroUser sUser = ShiroUtils.getCurrentUser();
		if (sUser != null) {
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.logout();
		} 
		OAuthConfigUtil oauthConfig = new OAuthConfigUtil("ecp");
		return oauthConfig.getValue("config.base.url");
	}

	@GET
	@Path("/usertest")
	public String apilogin(@Context HttpServletResponse response, @Context HttpServletRequest request) {
		ShiroUser sUser = ShiroUtils.getCurrentUser();
		if (sUser == null) {
			return "logged user null";
		} else {
			return sUser.getName();
		}

	}
	@GET
	@Path("/getUser")
	public String getUser(@Context HttpServletResponse response, @Context HttpServletRequest request) {
		ShiroUser user=ShiroUtils.getCurrentUser();
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}"; 
		}
	}
	
	@GET
	@Path("/loginByToken")
	public void loginByToken(@Context HttpServletResponse response, @Context HttpServletRequest request){
		String token=request.getParameter("token");
		TomUserLog user = systemService.getUserByToken(token);
		if(user ==null){
			System.out.println("帐号不存在");
			RequestDispatcher dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH+"/404.html");
			try {
				dispatcher .forward(request, response);					
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else {
			Subject currentUser = SecurityUtils.getSubject();
			UeToken ueToken = new UeToken(user.getCode(), user.getPassword());
			ueToken.setRememberMe(true);
			try {
				currentUser.login(ueToken);
				currentUser.getSession().setTimeout(3600000);
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
	}
}
