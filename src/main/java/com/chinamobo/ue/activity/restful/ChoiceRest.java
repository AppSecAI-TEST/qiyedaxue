package com.chinamobo.ue.activity.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.service.ChoiceRestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 版本: [1.0]
 * 功能说明: 所属部门\指定学员\推送部门负责人\选择管理员\选择讲师\任务包
 *
 * 作者: WangLg
 * 创建时间: 2016年3月15日 下午2:10:43
 */

@Path("/belong") 
@Scope("request") 
@Component
public class ChoiceRest {

	@Autowired
	private ChoiceRestService choiceRestService;
	
	/**
	 * 功能描述：[选择讲师]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午3:13:11
	 * @return
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/findLecturerApi")
	@Produces({MediaType.APPLICATION_JSON})
	public String findTaskList () throws JsonProcessingException{
			ObjectMapper mapper = new ObjectMapper();
			String json;
			json= mapper.writeValueAsString(choiceRestService.selectAllName());
			return json;
	}
	
	
	/**
	 * 功能描述：[选择任务包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午3:13:19
	 * @return
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/findTaskApi")
	@Produces({MediaType.APPLICATION_JSON})
	public String selectAllTask () throws JsonProcessingException{
			ObjectMapper mapper = new ObjectMapper();
			String json;
			json= mapper.writeValueAsString(choiceRestService.selectAllTask());
			return json;
	}
	
	
	/**TomDept.java
	 * 功能描述：[选择部门]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午3:13:19
	 * @return
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/findDeptApi")
	@Produces({MediaType.APPLICATION_JSON})
	public String selectAllDept() throws JsonProcessingException{
			ObjectMapper mapper = new ObjectMapper();
			String json;
			json= mapper.writeValueAsString(choiceRestService.selectAllTask());
			return json;
	}
	
	/**
	 * 功能描述：[指定学员\推送部门负责人]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午3:49:06
	 * @return
	 * @throws JsonProcessingException
	 */
	@GET
	@Path("/choiceEmpApi")
	@Produces({MediaType.APPLICATION_JSON})
	public String choiceEmp() throws JsonProcessingException{
			ObjectMapper mapper = new ObjectMapper();
			String json;
			json= mapper.writeValueAsString("");
			return json;
	}
	
	
	@GET
	@Path("/findAdminApi")
	@Produces({MediaType.APPLICATION_JSON})
	public String findAdmin() throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
			String json;
			json= mapper.writeValueAsString(choiceRestService.findAdmin());
			return json;
	}
	
	/**
	 * 
	 * 功能描述：[查询课程信息]
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:13:06
	 */
	@GET
	@Path("/findCoursesApi")
	@Produces({MediaType.APPLICATION_JSON})
	public String findCourses() throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
			String json;
			json= mapper.writeValueAsString(choiceRestService.findCourses());
			return json;
	}
	
	/**
	 * 
	 * 功能描述：[查询考卷信息]
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午4:13:06
	 */
	@GET
	@Path("/findTaskExamApi")
	@Produces({MediaType.APPLICATION_JSON})
	public String findTaskExam() throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
			String json;
			json= mapper.writeValueAsString(choiceRestService.findExampaper());
			return json;
	}
//	/**
//	 * 功能描述：[选择员工层级]
//	 *
//	 * 创建者：WangLg
//	 * 创建时间: 2016年3月15日 下午3:13:19
//	 * @return
//	 * @throws JsonProcessingException
//	 */
//	
//	@GET
//	@Path("/findTask")
//	public String selectAllEmpGrade() throws JsonProcessingException{
//			String json;
//			json= mapper.writeValueAsString(choiceRestService.selectAllTask());
//			return json;
//	}
	
	
}
