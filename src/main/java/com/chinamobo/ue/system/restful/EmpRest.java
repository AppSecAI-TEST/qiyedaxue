package com.chinamobo.ue.system.restful;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomEmpOne;
import com.chinamobo.ue.system.service.EmpServise;
import com.chinamobo.ue.system.service.GrpOrgRelationServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/emp")
@Scope("request")
@Component
public class EmpRest {
	@Autowired
	private EmpServise empServise;
	@Autowired
	private GrpOrgRelationServise grpOrgRelationServise;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * 功能描述：[分页查询员工]
	 *
	 * 创建者：cjx 创建时间: 2016年3月3日 下午2:20:27
	 * 
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/findpage")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByPage(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("name") String name,
			@QueryParam("deptname") String deptname, @QueryParam("empcode") String empcode,
			@QueryParam("statuss") String statuss, @QueryParam("cityname") String cityname,
			@QueryParam("sex") String sex, @QueryParam("code") String code, @QueryParam("poststat") String poststat,
			 @QueryParam("begindate") String begindate, @Context HttpServletResponse response, @Context HttpServletRequest request) {
		try {
			if (statuss == null) {
				statuss = "1";
			}
			if (statuss.equals("1")) {
				String json = mapper.writeValueAsString(grpOrgRelationServise.selectByCode(pageNum, pageSize, code));
				return json;
			} else {
				
				String json = mapper.writeValueAsString(empServise.selectByPage(pageNum, pageSize, deptname, empcode,
						name, cityname, sex, code, statuss,poststat, begindate));
				return json;
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[查询员工详细信息]
	 *
	 * 创建者：cjx 创建时间: 2016年3月7日 下午6:01:12
	 * 
	 * @param code
	 * @return
	 */
	@GET
	@Path("/findByCode")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByCode(@QueryParam("code") String code) {

		try {
//			System.out.println(mapper.writeValueAsString(empServise.selectByCode(code)));
			return mapper.writeValueAsString(empServise.selectByCode(code));
		
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}

	/**
	 * 
	 * 功能描述：[根据员工Code联合查询用户信息表]
	 *
	 * 创建者：cjx 创建时间: 2016年3月8日 下午1:08:42
	 * 
	 * @param code
	 * @return
	 */
	@GET
	@Path("/selectByCodeOnetoOne")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByCodeOnetoOne(@QueryParam("code") String code) {

		try {
			return mapper.writeValueAsString(empServise.selectByCodeOnetoOne(code));
		} catch (JsonProcessingException e) {

			e.getStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[查询员工表和活动表通过员工CODE]
	 *
	 * 创建者：cjx 创建时间: 2016年3月8日 下午3:33:16
	 * 
	 * @param code
	 * @return
	 */
	@GET
	@Path("/selectByEmpActivity")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByEmpActivity(@QueryParam("code") String code,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize) {
		try {
			return mapper.writeValueAsString(empServise.selectByEmpActivity(code,pageNum,pageSize));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[查询员工表和考试表通过员工CODE]
	 *
	 * 创建者：cjx 创建时间: 2016年3月8日 下午3:33:16
	 * 
	 * @param code
	 * @return
	 */
	@GET
	@Path("/selectByEmpExam")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByEmpExam(@QueryParam("code") String code,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize) {
		try {
			return mapper.writeValueAsString(empServise.selectByEmpExam(code,pageNum,pageSize));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[查询员工表和课程表通过员工CODE]
	 *
	 * 创建者：cjx 创建时间: 2016年3月8日 下午3:33:16
	 * 
	 * @param code
	 * @return
	 */
	@GET
	@Path("/selectByEmpCourses")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByEmpCourses(@QueryParam("code") String code,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize) {
		try {
			return mapper.writeValueAsString(empServise.selectByEmpCourses(code,pageNum,pageSize));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[根据课程id查询]
	 *
	 * 创建者：JCX 创建时间: 2016年4月5日 下午6:10:18
	 * 
	 * @param courseId
	 * @return
	 */
	@GET
	@Path("/selectByCourseId")
	public String findByCourseId(@QueryParam("courseId") int courseId) {
		List<TomEmp> list = empServise.selectByCourseId(courseId);
		try {
			return mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * 功能描述：[分页查询员工(管理员)]
	 *
	 * 创建者：cjx 创建时间: 2016年5月10日 下午2:20:27
	 * 
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/selectByAdmin")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByAdmin(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("name") String name,
			@QueryParam("deptname") String deptname, @QueryParam("empcode") String empcode,
			@QueryParam("statuss") String statuss, @QueryParam("cityname") String cityname,
			@QueryParam("sex") String sex, @QueryParam("code") String code, @Context HttpServletResponse response,
			@Context HttpServletRequest request) {
		try {
			if (statuss == null) {
				statuss = "1";
			}
			if (statuss.equals("1")) {
				String json = mapper.writeValueAsString(grpOrgRelationServise.selectByCode(pageNum, pageSize, code));
				return json;
			} else {
			
				String json = mapper.writeValueAsString(empServise.selectByAdmin(pageNum, pageSize, deptname, empcode,
						name, cityname, sex, code, statuss));
				return json;
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[分页查询员工(讲师)]
	 *
	 * 创建者：cjx 创建时间: 2016年5月10日 下午3:10:27
	 * 
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/selectByLecturer")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByLecturer(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("name") String name,
			@QueryParam("deptname") String deptname, @QueryParam("empcode") String empcode,
			@QueryParam("statuss") String statuss, @QueryParam("cityname") String cityname,
			@QueryParam("sex") String sex, @QueryParam("code") String code, @Context HttpServletResponse response,
			@Context HttpServletRequest request) {
		try {
			if(null!=sex&&""!=sex){
				if("男".equals(sex)){
					sex="0";
				}else if("女".equals(sex)){
					sex="1";
				}
			}
				String json = mapper.writeValueAsString(empServise.selectByLecturer(pageNum, pageSize, deptname, empcode,
						name, cityname, sex, code, statuss));
				return json;
			

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[分页查询员工(选择员工)]
	 *
	 * 创建者：cjx 创建时间: 2016年5月10日 下午3:40:27
	 * 
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/selectForEmp")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectForEmp(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("name") String name,
			@QueryParam("deptname") String deptname, @QueryParam("empcode") String empcode,
			@QueryParam("statuss") String statuss, @QueryParam("cityname") String cityname,
			@QueryParam("sex") String sex, @QueryParam("code") String code,@QueryParam("country") String country, 
			@QueryParam("begindate") String begindate, @Context HttpServletResponse response,@Context HttpServletRequest request) {
		try {
			
				String json = mapper.writeValueAsString(empServise.selectForEmp(pageNum, pageSize, deptname, empcode,
						name, cityname, sex, code, statuss,country, begindate));
				return json;
			

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[批量新增人员]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年8月4日 上午11:47:50
	 * @return
	 * @throws Exception
	 */
//	@GET
//	@Path("/insertEmp")
//	@Produces({ MediaType.APPLICATION_JSON })
//	public String insertOrg() throws Exception {
//		empServise.insertList();
//		return "{\"result\":\"error\"}";
//
//	}
//	/**
//	 * 
//	 * 功能描述：[添加人员]
//	 *
//	 * 创建者：cjx
//	 * 创建时间: 2016年8月4日 上午11:47:37
//	 * @param tomEmp
//	 * @return
//	 * @throws Exception
//	 */
//	@GET
//	@Path("/insertOne")
//	@Produces({ MediaType.APPLICATION_JSON })
//	public String insertOne(@BeanParam TomEmpOne tomEmp) throws Exception {
//		try{
//		empServise.insertOne(tomEmp);
//		return "{\"result\":\"success\"}";
//		}catch(Exception e){
//		e.printStackTrace();
//		return "{\"result\":\"error\"}";
//		}
//	}
	
	//添加员工
	@POST
	@Path("/insertEmp")
	@Produces({ MediaType.APPLICATION_JSON })
	public String insertEmp(@BeanParam TomEmpOne tomEmp){
		try{
			DBContextHolder.setDbType(DBContextHolder.MASTER);
				empServise.insertEmp(tomEmp);
				return "{\"result\":\"success\"}";
			}catch(Exception e){
				e.printStackTrace();
				if(tomEmp.getCode().equals(tomEmpMapper.selectByPrimaryKey(tomEmp.getCode()).getCode())){
					return "{\"result\":\"only\"}";
				}else{
					return "{\"result\":\"error\"}";
				}
			}
	}
	
	//编辑人员
	@POST
	@Path("/editEmp")
	public String editEmp(@BeanParam TomEmpOne tomEmp){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			empServise.editEmp(tomEmp);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//同步人员
	@GET
	@Path("/synchronizationEmp")
	public String synchronizationEmp(@QueryParam("pkDept") String pkDept){
		try{
			empServise.synchronizationEmp(pkDept);
			return "{\"result\":\"success\"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
}
