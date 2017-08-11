package com.chinamobo.ue.system.restful;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.service.UserLogServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/userlog")
@Scope("request")
@Component
public class UserLogRest {

	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private UserLogServise userLogServise;


	/**
	 * 
	 * 功能描述：[更改用户状态]
	 *
	 * 创建者：cjx 创建时间: 2016年3月9日 上午11:08:50
	 * 
	 * @param tomUserLog
	 * @return
	 */
	@PUT
	@Path("/updateByCode")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateByCode(@QueryParam("code") String code, @QueryParam("status") String status) {
		try{
			DBContextHolder.setDbType(DBContextHolder.MASTER);
		userLogServise.updateByCodeTwo(code,status);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
		
	}

	/**
	 * 
	 * 功能描述：[重置密码]
	 *
	 * 创建者：cjx 创建时间: 2016年3月9日 下午2:05:56
	 * 
	 * @param tomUserLog
	 * @param request
	 * @return
	 */
	@PUT
	@Path("/updatePasswordByCode")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updatePasswordByCode(@QueryParam("code") String code) {
		try{
			DBContextHolder.setDbType(DBContextHolder.MASTER);
		userLogServise.updateByCodeTwo(code);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}
}
