//package com.chinamobo.ue.system.webservise;
//
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//
//@Path("/ehr") 
//@Scope("request") 
//@Component
//public class EhrRest {
//	@Autowired
//	private DeptInterfaceServise deptInterfaceServise;
//	@Autowired
//	private OrgInterfaceServise orgInterfaceServise;
//	@Autowired
//	private EmpInterfaceServise empInterfaceServise;
//	@Autowired
//	private EhrAdd ehrAdd;
//	/**
//	 * 
//	 * 功能描述：[EHR接口]
//	 *
//	 * 创建者：cjx 创建时间: 2016年3月12日 下午2:39:45
//	 * 
//	 * @return
//	 * @throws Exception 
//	 */
//	@GET
//	@Path("/insertOrg")
//	@Produces({ MediaType.APPLICATION_JSON })
//	public String insertOrg() throws Exception  {
//
//		orgInterfaceServise.insertList();
//		
//		return "{\"result\":\"error\"}";
//
//	}
//
//@GET
//@Path("/insertDept")
//@Produces({ MediaType.APPLICATION_JSON })
//public String insertDept() throws Exception {
//
//	deptInterfaceServise.insertList();
//	
//	return "{\"result\":\"error\"}";
//
//}
//
//@GET
//@Path("/insertEmp")
//@Produces({ MediaType.APPLICATION_JSON })
//public String insertEmp() throws Exception {
//
//		empInterfaceServise.insertList();
//	
//	return "{\"result\":\"error\"}";
//
//}
//	@GET
//	@Path("/add")
//	@Produces({ MediaType.APPLICATION_JSON })
//	public String insert() throws Exception {
//		ehrAdd.operation();
//		return "{\"result\":\"error\"}";
//
//	}
//}
//
