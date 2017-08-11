package com.chinamobo.ue.system.restful;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.system.service.AuthoritiesTypeServise;
import com.chinamobo.ue.system.service.RoleAuthoritiesServise;
import com.chinamobo.ue.system.service.RoleScopesServise;
import com.chinamobo.ue.system.service.RoleServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/role")
@Scope("request")
@Component
public class RoleRest {
	@Autowired
	private RoleServise roleServise;
	@Autowired
	private RoleScopesServise roleScopesServise;
	@Autowired
	private RoleAuthoritiesServise roleAuthoritiesServise;
	@Autowired
	AuthoritiesTypeServise authoritiesTypeServise;
	ObjectMapper mapper = new ObjectMapper();


	/**
	 * 
	 * 功能描述：[添加角色权限信息]
	 *
	 * 创建者：cjx 创建时间: 2016年3月10日 上午10:44:50
	 * 
	 * @param roleName
	 * @param roleType
	 * @param status
	 * @param austatus
	 * @param roleScope
	 * @param auid
	 * @param code
	 * @return
	 */
	@POST
	@Path("/insert")
	@Produces({ MediaType.APPLICATION_JSON })
	public String insert(@FormParam("roleName") String roleName, @FormParam("roleType") String roleType,
			@FormParam("roleScope") String roleScope, @FormParam("roleAuthority") String auid) {
		try{
			DBContextHolder.setDbType(DBContextHolder.MASTER);
		roleServise.insertRole(roleName,roleType,roleScope,auid);
		return "{\"result\":\"success\"}";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}

	/**
	 * 
	 * 功能描述：[更改角色信息]
	 *
	 * 创建者：cjx 创建时间: 2016年3月12日 下午1:11:36
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
	public String update(@FormParam("roleName") String roleName, @FormParam("roleType") String roleType,
			@FormParam("roleScope") String roleScope, @FormParam("roleAuthority") String auid,
			@FormParam("roleId") Integer roleid) {
		try{
			DBContextHolder.setDbType(DBContextHolder.MASTER);
		roleServise.updateRole(roleName,roleType,roleScope,auid,roleid);
		return "{\"result\":\"success\"}";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}

	/**
	 * 
	 * 功能描述：[更改角色状态]
	 *
	 * 创建者：cjx 创建时间: 2016年3月15日 下午8:50:20
	 * 
	 * @param roleid
	 * @param status
	 * @return
	 */
	@PUT
	@Path("/updateStatus")
	public String updateStatus(@QueryParam("roleid") int roleid, @QueryParam("status") String status) {
		ShiroUser user = ShiroUtils.getCurrentUser();
		TomRole tomRole = new TomRole();
		Date date = new Date();
		tomRole.setOperator(user.getName());
		tomRole.setStatus(status);
		tomRole.setUpdateTime(date);
		tomRole.setRoleId(roleid);
		String str = roleServise.updateStatus(tomRole);

		return "{\"result\":\""+str+"\"}";

	}

	/**
	 * 
	 * 功能描述：[分页查询角色信息]
	 *
	 * 创建者：cjx 创建时间: 2016年3月11日 下午1:30:26
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param role_name
	 * @param authority_id
	 * @param scope
	 * @param request
	 * @return
	 */
	@GET
	@Path("/findpage")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByPage(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("roleName") String roleName,
			@QueryParam("authority_id") Integer authority_id, @QueryParam("scope") String scope,
			@Context HttpServletRequest request) {
		try {

			return mapper.writeValueAsString(roleServise.findPage(pageNum, pageSize, roleName, authority_id, scope));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}

	/**
	 * 
	 * 功能描述：[根据角色code查找角色]
	 *
	 * 创建者：cjx 创建时间: 2016年3月11日 下午2:03:44
	 * 
	 * @param role_id
	 * @return
	 */
	@GET
	@Path("/selectByPrimaryKey")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByPrimaryKey(@QueryParam("role_id") Integer role_id) {
		try {
			return mapper.writeValueAsString(roleServise.selectByPrimaryKey(role_id));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[根据角色code查找权限]
	 *
	 * 创建者：cjx 创建时间: 2016年3月11日 下午2:53:39
	 * 
	 * @param role_id
	 * @return
	 */
	@GET
	@Path("/selectAuthoritiesByRoilId")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectAuthoritiesByRoilId(@QueryParam("role_id") Integer role_id) {

		try {
//			System.out.println(mapper.writeValueAsString(roleAuthoritiesServise.selectAuthoritiesByRoilId(role_id)));
			return mapper.writeValueAsString(authoritiesTypeServise.selectAuthoritiesByRoilId(role_id));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[根据角色code查找范围]
	 *
	 * 创建者：cjx 创建时间: 2016年3月11日 下午2:54:25
	 * 
	 * @param role_id
	 * @return
	 */
	@GET
	@Path("/selectScopeByRoilId")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectScopeByRoilId(@QueryParam("role_id") Integer role_id) {
		try {
//			System.out.println(mapper.writeValueAsString(roleScopesServise.selectScopeByRoilId(role_id)));
			return mapper.writeValueAsString(roleScopesServise.selectScopeByRoilId(role_id));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[根据id删除角色]
	 *
	 * 创建者：cjx 创建时间: 2016年3月12日 下午3:30:15
	 * 
	 * @param roleId
	 * @return
	 */
	@DELETE
	@Path("/deleteByPrimaryKey")
	@Produces({ MediaType.APPLICATION_JSON })
	public String deleteByPrimaryKey(@QueryParam("roleId") int roleId) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String status = roleServise.deleteByPrimaryKey(roleId);
		return "{\"result\":\""+status+"\"}";

	}

	/**
	 * 
	 * 功能描述：[查询所有角色]
	 *
	 * 创建者：cjx 创建时间: 2016年3月15日 上午10:33:54
	 * 
	 * @return
	 */
	@GET
	@Path("/findAll")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findAll() {
		try {
			ShiroUser user = ShiroUtils.getCurrentUser();
			if (0 == user.getRoles().get(0).getRoleId()) {
				return mapper.writeValueAsString(roleServise.findAll());
			} else {
				return mapper.writeValueAsString(roleServise.findRoleByCreator());
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}
	/**
	 * 
	 * 功能描述：[查询所有范围（下拉）]
	 *
	 * 创建者：cjx 创建时间: 2016年5月11日 上午10:33:54
	 * 
	 * @return
	 */
	@GET
	@Path("/findAllScope")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findAllScope() {
		try {
				return mapper.writeValueAsString(roleServise.findAllScope());	
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}
	
	/**
	 * 
	 * 功能描述：[查询角色级别]
	 *
	 * 创建者：cjx 创建时间: 2016年5月11日 上午10:55:54
	 * 
	 * @return
	 */
	@GET
	@Path("/findType")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findType() {
		try {
			ShiroUser user = ShiroUtils.getCurrentUser();
			List<TomRole> roles = user.getRoles();
			int k=0;
			for(TomRole role:roles){
				if("1".equals(role.getRoleType())){
					k++;
				}				
			}
			if(k==0){
				return "2";
			}else{
				return "1";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}
}
