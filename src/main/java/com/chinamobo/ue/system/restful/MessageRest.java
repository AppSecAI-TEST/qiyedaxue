package com.chinamobo.ue.system.restful;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.service.MessageServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/message")
@Scope("request")
@Component
public class MessageRest {
	@Autowired
	private MessageServise messageServise;
	ObjectMapper mapper = new ObjectMapper();


	/**
	 * 
	 * 功能描述：[推送消息]
	 *
	 * 创建者：cjx 创建时间: 2016年5月24日 下午2:36:01
	 * 
	 * @param tomMessages
	 * @return
	 */
	@POST
	@Path("/sendMessage")
	@Produces({ MediaType.APPLICATION_JSON })
	public String sendMessage(@BeanParam TomMessages tomMessages) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			messageServise.sendMessage(tomMessages);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
		
	}
	/**
	 * 
	 * 功能描述：[查找所有消息]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月13日 下午1:29:47
	 * @param pageNum
	 * @param pageSize
	 * @param messageTitle
	 * @return
	 */
	@GET
	@Path("/findAll")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findAll(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("messageTitle") String messageTitle){
		try{
			return mapper.writeValueAsString(messageServise.findPage(pageNum, pageSize, messageTitle));
		}catch(Exception e){
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
		
	}
	/**
	 * 
	 * 功能描述：[查看消息]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月16日 下午4:36:58
	 * @param messageId
	 * @return
	 */
	@GET
	@Path("/findOne")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findOne(@QueryParam("messageId") int messageId){
		try{
			return mapper.writeValueAsString(messageServise.findOne(messageId));
		}catch(Exception e){
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
		
	}
	
	@PUT
	@Path("/updateSysMessage")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateSysMessage(@BeanParam TomMessages tomMessages){
		try{
			messageServise.updateSysMessage(tomMessages);
		}catch(Exception e){
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
		
	}
}
