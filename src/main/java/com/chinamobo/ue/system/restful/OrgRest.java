package com.chinamobo.ue.system.restful;


import javax.ws.rs.BeanParam;
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
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.system.service.OrgServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/org")
@Scope("request")
@Component

public class OrgRest {

	@Autowired
	private OrgServise orgServise;

	ObjectMapper mapper = new ObjectMapper();

	

	/**
	 * 
	 * 功能描述：[查询所有公司]
	 *
	 * 创建者：cjx 创建时间: 2016年3月29日 下午3:42:22
	 * 
	 * @return
	 */
	@GET
	@Path("/findAll")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findAll() {
		try {
//			System.out.println(mapper.writeValueAsString(orgServise.select()));
			return mapper.writeValueAsString(orgServise.select());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

//	@GET
//	@Path("/insert")
//	@Produces({ MediaType.APPLICATION_JSON })
//	public String insert() throws Exception {
//		try {			
//			orgServise.insertList();
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "{\"result\":\"error\"}";
//	}
	
	@POST
	@Path("/insertOrg")
	@Produces({ MediaType.APPLICATION_JSON })
	public String insertOrg(@BeanParam TomOrg tomOrg)  {
		try {	
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			orgServise.insertOrg(tomOrg);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {		
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	@PUT
	@Path("/updateOrg")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateOrg(@BeanParam TomOrg tomOrg)  {
		try {			
			orgServise.updateOrg(tomOrg);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {		
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//查询公司信息
	@GET
	@Path("/queryCompanyInformation")
	public String selectOrg(@QueryParam("pk_org") String pk_org){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(orgServise.selectByTree(pk_org));
	}
}
