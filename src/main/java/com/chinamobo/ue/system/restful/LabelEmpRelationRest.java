package com.chinamobo.ue.system.restful;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.service.LabelEmpRelationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/labelEmp")
@Scope("request")
@Component
public class LabelEmpRelationRest {
	@Autowired
	private LabelEmpRelationService labelEmpRelationService;
	ObjectMapper mapper = new ObjectMapper();
	
	//分页查询标签人员
	@GET
	@Path("/selectLabelEmpList")
	public String selectLabelEmpList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize, @QueryParam("empcode") String code,
			@QueryParam("name") String name, @QueryParam("sex") String sex, @QueryParam("begindate") String begindate,
			@QueryParam("cityname") String cityname, @QueryParam("statuss") String statuss, @QueryParam("id") String id){
		try{
			String json = mapper.writeValueAsString(labelEmpRelationService.selectLabelEmpList(pageNum, pageSize, code, name, sex, begindate, cityname, statuss, id));
			return json;
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
}
