package com.chinamobo.ue.system.restful;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.service.OrgGroupsServise;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/orggroups")
@Scope("request")
@Component

public class OrgGroupsRest {
	@Autowired
	private OrgGroupsServise orgGroupsServise;


	ObjectMapper mapper = new ObjectMapper();


	/**
	 * 
	 * 功能描述：[组织架构]
	 *
	 * 创建者：cjx 创建时间: 2016年3月3日 下午2:20:27
	 * 
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/tree")
	@Produces({ MediaType.APPLICATION_JSON })

	public String Tree() throws Exception {
		try{
			
		List<Tree> tree = orgGroupsServise.tree();
		return mapper.writeValueAsString(tree);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[角色范围]
	 *
	 * 创建者：cjx 创建时间: 2016年4月7日 上午11:24:56
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/tree2")
	@Produces({ MediaType.APPLICATION_JSON })
	public String Tree2() throws Exception {
		try{
		List<Tree> tree2 = orgGroupsServise.tree2();
		return mapper.writeValueAsString(tree2);
		}catch (Exception e) {
		e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[查询所有部门和公司]
	 *
	 * 创建者：cjx 创建时间: 2016年3月17日 下午2:48:22
	 * 
	 * @return
	 */
	@GET
	@Path("/slecetAll")
	@Produces({ MediaType.APPLICATION_JSON })
	public String slecetAll() {
		try {

			return mapper.writeValueAsString(orgGroupsServise.selectAllDeOrg());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";

	}

	/**
	 * 
	 * 功能描述：[选择人员树]
	 *
	 * 创建者：cjx 创建时间: 2016年4月9日 下午3:22:53
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/selectEmp")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectEmp() throws Exception {
		try{
		List<Tree> selectEmp = orgGroupsServise.selectEmp();		
		return mapper.writeValueAsString(selectEmp);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[选择范围树]
	 *
	 * 创建者：cjx 创建时间: 2016年4月9日 下午3:22:53
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/selectByScope")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByScope() throws Exception {
		try{
		List<Tree> selectByScope = orgGroupsServise.selectByScope();
		return mapper.writeValueAsString(selectByScope);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[选择范围树]
	 *
	 * 创建者：cjx 创建时间: 2016年4月9日 下午3:22:53
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/selectByScope2")
	@Produces({ MediaType.APPLICATION_JSON })
	public String selectByScope2() throws Exception {
		try{
		List<Tree> selectByScope2 = orgGroupsServise.selectByScope2();
		return mapper.writeValueAsString(selectByScope2);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	//根据Code查询集团信息
		@GET
		@Path("/selectOrgGroups")
		@Produces({ MediaType.APPLICATION_JSON })
		public String selectOrgGroups(@QueryParam("code") String code){
			try{
//				JsonMapper jsonMapper = new JsonMapper();
//				return jsonMapper.toJson(orgGroupsServise.selectOrgGroups(code));
				orgGroupsServise.selectOrgGroups(code);
				return "{\"result\":\"success\"}";
			}catch(Exception e){
				e.printStackTrace();
				return "{\"result\":\"error\"}";
			}
		}
		
		//查询集团全表
		@GET
		@Path("/selectGroups")
		@Produces({ MediaType.APPLICATION_JSON })
		public String selectGroups(){
			try{
				orgGroupsServise.selectGroups();
				return "{\"result\":\"success\"}";
			}catch(Exception e){
				e.printStackTrace();
				return "{\"result\":\"error\"}";
			}
		}
}