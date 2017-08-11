package com.chinamobo.ue.system.restful;



import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.system.service.AuthoritiesServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/authoritiesRest")
@Scope("request")
@Component
public class AuthoritiesRest {
	@Autowired
	private AuthoritiesServise authoritiesServise;

	ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * 功能描述：[查询所有权限]
	 *
	 * 创建者：cjx 创建时间: 2016年3月11日 下午2:11:13
	 * 
	 * @return
	 */
	@GET
	@Path("/selectByRole")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectAll() {
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			ShiroUser user = ShiroUtils.getCurrentUser();
			 List<TomRole> role =	user.getRoles();
			String json = mapper.writeValueAsString(authoritiesServise.selectAll(role));	
			return json;
		} catch (JsonProcessingException e) {
			e.getStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
}
