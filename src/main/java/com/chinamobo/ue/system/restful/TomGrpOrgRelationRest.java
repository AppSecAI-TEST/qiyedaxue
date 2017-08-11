package com.chinamobo.ue.system.restful;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.service.GrpOrgRelationServise;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/grpOrgRelation")
@Scope("request")
@Component
public class TomGrpOrgRelationRest {

	@Autowired
	private GrpOrgRelationServise grpOrgRelationServise;
	/**
	 * 
	 * 功能描述：[根据集团分页查询公司]
	 *
	 * 创建者：cjx 创建时间: 2016年3月4日 下午4:09:56
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param grpCode
	 * @param response
	 * @return
	 */
	ObjectMapper mapper = new ObjectMapper();

	@GET
	@Path("/findPage")
	public String findLecturerByPage(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("grpCode") String grpCode,
			@Context HttpServletResponse response) {

		try {
			String json = mapper.writeValueAsString(grpOrgRelationServise.selectByCode(pageNum, pageSize, grpCode));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
}
