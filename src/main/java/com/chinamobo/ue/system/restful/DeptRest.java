package com.chinamobo.ue.system.restful;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.BeanParam;
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

import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.service.ContextInitRedisService;
import com.chinamobo.ue.system.service.DeptServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/dept")
@Scope("request")
@Component
public class DeptRest {
	@Autowired
	private DeptServise deptServise;
	ShiroUser user=ShiroUtils.getCurrentUser();
	ObjectMapper mapper = new ObjectMapper();
	
	@GET
	@Path("/selectByPkDept")
	@Produces({ MediaType.APPLICATION_JSON })
	/**
	 * 
	 * 功能描述：[根据公司PKDept查询]
	 *
	 * 创建者：cjx 创建时间: 2016年3月7日 下午6:13:40
	 * 
	 * @param code
	 * @return
	 */
	public String selectByPKDept(@QueryParam("pk_dept") String pk_dept) {

		try {
			return mapper.writeValueAsString(deptServise.selectByPKDept(pk_dept));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[更改部门状态]
	 *
	 * 创建者：cjx 创建时间: 2016年3月8日 上午10:12:43
	 * 
	 * @param pk_dept
	 * @param hrcanceled
	 * @return
	 */
	@PUT
	@Path("/updateByPrimaryKey")
	public String updateByPrimaryKey(@FormParam("pk_dept") String pk_dept, @FormParam("hrcanceled") String hrcanceled) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		deptServise.updateStatus(pk_dept,hrcanceled);
		
		return "{\"result\":\"error\"}";

	}
//	@GET
//	@Path("/insertEmp")
//	@Produces({ MediaType.APPLICATION_JSON })
//	public String insertOrg() throws Exception {
//		deptServise.insertList();
//		return "{\"result\":\"error\"}";
//
//	}
	
	@GET
	@Path("/insertDept")
	@Produces({ MediaType.APPLICATION_JSON })
	public String insertDept(@BeanParam TomDept tomDept)  {
		try{
		deptServise.insertDept(tomDept);
		return "{\"result\":\"success\"}";
		}catch(Exception e){
			e.printStackTrace();
		return "{\"result\":\"error\"}";
		}
	}
	
	//添加部门
	@POST
	@Path("/addDepartment")
	public String addDept(@BeanParam TomDept tomDept)  {
		try{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		tomDept.setCreationtime(str);
		tomDept.setModifiedtime(str);
		deptServise.addDept(tomDept);
		return "{\"result\":\"success\"}";
		}catch(Exception e){
			e.printStackTrace();
		return "{\"result\":\"error\"}";
		}
	}
	
	//查询部门信息
	@GET
	@Path("/selectDepartmentInformation")
	public String selectDept(@QueryParam("pkDept") String pkDept){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(deptServise.selectDepartmentInformation(pkDept));
//		try{
//			deptServise.selectDepartmentInformation(pkDept);
//			return "{\"result\":\"success\"}";
//		}catch(Exception e){
//			e.printStackTrace();
//			return "{\"result\":\"error\"}";
//		}
	}
	
	//同步部门
	@GET
	@Path("/synchronizationDept")
	public String synchronizationDept(){
		try{
			deptServise.synchronizationDept();
			return "{\"result\":\"success\"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
}
