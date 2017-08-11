package com.chinamobo.ue.common.restful;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.utils.PathUtil;

@Path("/menu") 
@Scope("request") 
@Component
public class MenuRest {

	private static Logger logger = LoggerFactory.getLogger(MenuRest.class);
	
	@GET
	public void menu(@Context HttpServletResponse response, @Context HttpServletRequest request,@QueryParam("code")int code) {
		RequestDispatcher dispatcher;
		if(code==403){
			dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH+"/403.html");
		}else{
			dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH+"/index.html");
		}
		
		try {
			dispatcher .forward(request, response);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
	}	
}
