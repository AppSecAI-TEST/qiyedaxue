package com.chinamobo.ue.system.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.service.LabelClassRelationServise;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/labelClassRelation")
@Scope("request")
@Component
public class LabelClassRelationRest {
	@Autowired
	private LabelClassRelationServise labelClassRelationServise;
	ObjectMapper mapper = new ObjectMapper();
	
	//获取标签分类树形菜单
	@GET
	@Path("/tree")
	public String getTree(){
		Tree tree=labelClassRelationServise.getClassifyTree();
		String json;
		try {
			json = mapper.writeValueAsString(tree);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
}
