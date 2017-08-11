package com.chinamobo.ue.api;

import java.net.URLDecoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.api.weChat.WeChatApiService;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.PathUtil;
import com.chinamobo.ue.utils.StringUtil;
@Component
@Scope("request")
@Path("weChat")
public class ApiWeChatRest {
	
	private static Logger logger = LoggerFactory.getLogger(ApiWeChatRest.class);
	
	@Autowired
	private WeChatApiService wechatapiservice;
//	private static final String appid=Config.getProperty("appId");
//	private static final String secret=Config.getProperty("appsecret");
//	
	/**
	 * 功能描述  根据code和token获取userid
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@Path("/getaccess_token")
	public void weChar(@Context HttpServletRequest request,@Context HttpServletResponse response){
		String code = request.getParameter("code");
		String backUrl= URLDecoder.decode(request.getParameter("backurl"));
		try{
			TomUserLog user = wechatapiservice.getuserinfo(request,response);
			if(user==null){
				System.out.println("帐号不存在");
				response.sendRedirect(Config.getProperty("h5Index")+"/views/login/error.html");
			}else{
				if(StringUtil.isNotBlank(backUrl)){
					if(backUrl.indexOf("?token")!=-1 || backUrl.indexOf("&token")!=-1){
						backUrl=backUrl.split("token")[0].substring(0, backUrl.split("token")[0].length()-1);
					}
					if(backUrl.indexOf("?")!=-1){
						backUrl=backUrl+"&token="+user.getToken()+"&userId="+user.getCode();
					}else {
						backUrl=backUrl+"?token="+user.getToken()+"&userId="+user.getCode();
					}
//					System.out.println(backUrl);
					response.sendRedirect(backUrl);
				}else {
					response.sendRedirect(Config.getProperty("h5Index")+"views/index/index.html#/footer/home?token="+user.getToken()+"&userId="+user.getCode());
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
	}
	
	
}
