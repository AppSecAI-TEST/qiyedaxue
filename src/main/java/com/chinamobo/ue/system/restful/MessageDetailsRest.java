package com.chinamobo.ue.system.restful;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.system.service.MessageDetailsServise;
import com.chinamobo.ue.ums.DBContextHolder;
//import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Path("/messageDetails")
@Scope("request")
@Component
public class MessageDetailsRest {
	
	@Autowired
	private MessageDetailsServise messageDetailsServise;
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * 功能描述：[查询信息内容]
	 *
	 * 创建者：cjx 创建时间: 2016年12月14日 下午6:01:12
	 * 
	 * @param code
	 * @return
	 */
	@GET
	@Path("/findById")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByCode() {

		try {
//			DBContextHolder.setDbType(DBContextHolder.MASTER);	
			return mapper.writeValueAsString(messageDetailsServise.selectList());
		
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}
	
	/**
	 * 
	 * 功能描述：[更改信息]
	 *
	 * 创建者：cjx 创建时间: 2016年12月22日 下午1:11:36
	 * 
	 * @param roleName
	 * @param roleType
	 * @param status
	 * @param austatus
	 * @param roleScope
	 * @param auid
	 * @param code
	 * @param roleid
	 * @return
	 */
	@PUT
	@Path("/update")
	public String update(String jsonStr,@Context HttpServletResponse response) {
		try{
		DBContextHolder.setDbType(DBContextHolder.MASTER);	
		ShiroUser user=ShiroUtils.getCurrentUser();
		messageDetailsServise.update(jsonStr);
		return "{\"result\":\"success\"}";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}
}
