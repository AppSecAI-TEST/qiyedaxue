package com.chinamobo.ue.system.restful;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
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

import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.service.AdminServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/admin")
@Scope("request")
@Component
public class AdminRest {
	@Autowired
	private AdminServise adminServise;
	@Autowired

	ObjectMapper mapper = new ObjectMapper();
	

	/**
	 * 
	 * 功能描述：[新增管理员]
	 *
	 * 创建者：cjx 创建时间: 2016年3月12日 上午11:44:04
	 * 
	 * @param code
	 * @param name
	 * @param creator
	 * @param roleid
	 * @return
	 */
	@POST
	@Path("/insertSelective")
	@Produces({ MediaType.APPLICATION_JSON })
	public String insertSelective(@FormParam("code") String code, @FormParam("name") String name,
			@FormParam("roleId") String roleid) {
		try {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		adminServise.insertSelectiveService(code, name, roleid);
		return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "{\"result\":\"error\"}";

	}

	/**
	 * 
	 * 功能描述：[更改管理员信息]
	 *
	 * 创建者：cjx 创建时间: 2016年3月11日 下午4:31:41
	 * 
	 * @param code
	 * @param name
	 * @param creator
	 * @param roleid
	 * @param adminid
	 * @return
	 */
	@POST
	@Path("/updateByPrimaryKey")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateByPrimaryKey(@FormParam("code") String code, @FormParam("name") String name,
			@FormParam("roleId") String roleid, @FormParam("adminId") Integer adminid) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			adminServise.updateUserInFoMaTion(code,name,roleid,adminid);		
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}

	/**
	 * 
	 * 功能描述：[分页查询管理员]
	 *
	 * 创建者：cjx 创建时间: 2016年3月12日 下午12:46:18
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@GET
	@Path("/findpage")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByPage(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("name") String name) {
		try {
			return mapper.writeValueAsString(adminServise.findPage(pageNum, pageSize, name));
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[选择管理员]
	 *
	 * 创建者：cjx 创建时间: 2016年3月12日 下午12:46:18
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@GET
	@Path("/selectAdmin")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectAdmin(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("name") String name) {
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			return mapper.writeValueAsString(adminServise.selectAdmin(pageNum, pageSize, name));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[根据id删除管理员]
	 *
	 * 创建者：cjx 创建时间: 2016年3月12日 下午3:27:32
	 * 
	 * @param adminId
	 * @return
	 */
	@DELETE
	@Path("/deleteByPrimaryKey")
	@Produces({ MediaType.APPLICATION_JSON })
	public String deleteByPrimaryKey(@QueryParam("adminId") int adminId) {
		try{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		adminServise.deleteByPrimaryKey(adminId);
		return "{\"result\":\"success\"}";
		}catch(Exception e){
			
		}
		return "{\"result\":\"error\"}";

	}

	/**
	 * 
	 * 功能描述：[根据管理员id查询详细信息]
	 *
	 * 创建者：cjx 创建时间: 2016年3月29日 下午1:36:30
	 * 
	 * @param adminId
	 * @return
	 */
	@GET
	@Path("/selectByAdminId")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByAdminId(@QueryParam("adminId") int adminId) {
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			return mapper.writeValueAsString(adminServise.selectByAdminId(adminId));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[更改管理员状态]
	 *
	 * 创建者：cjx 创建时间: 2016年3月29日 下午1:41:10
	 * 
	 * @return
	 */
	@PUT
	@Path("/updateStatus")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateStatus(@QueryParam("adminId") int adminId, @QueryParam("status") String status) {
		try{
		DBContextHolder.setDbType(DBContextHolder.MASTER);	
		TomAdmin tomAdmin = new TomAdmin();
		tomAdmin.setAdminId(adminId);
		tomAdmin.setStatus(status);
		adminServise.updateStatus(tomAdmin);
		return "{\"result\":\"success\"}";
		}catch(Exception e){
			
		}
		return "{\"result\":\"error\"}";

	}
}
