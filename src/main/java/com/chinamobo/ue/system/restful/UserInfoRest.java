package com.chinamobo.ue.system.restful;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.service.UserInfoServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/userinfo")
@Scope("request")
@Component
public class UserInfoRest {
	@Autowired
	private UserInfoServise userInfoServise;

	ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 
	 * 功能描述：[更改用户信息]
	 *
	 * 创建者：cjx 创建时间: 2016年3月12日 下午2:10:24
	 * 
	 * @param tomUserInfo
	 * @param request
	 * @return
	 */
	@PUT
	@Path("/updateByCode")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateByCode(@BeanParam TomUserInfo tomUserInfo, @Context HttpServletRequest request) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		userInfoServise.updateByCode(tomUserInfo);
		return "{\"result\":\"error\"}";
	}
}
