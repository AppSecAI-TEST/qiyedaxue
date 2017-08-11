package com.chinamobo.ue.system.restful;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.entity.TomLabel;
import com.chinamobo.ue.system.service.LabelServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/label")
@Scope("request")
@Component
public class LabelRest {
	@Autowired
	private LabelServise labelServise;
	ObjectMapper mapper = new ObjectMapper();
	ShiroUser user=ShiroUtils.getCurrentUser();
	
	//根据标签id查询
	@GET
	@Path("/selectLabel")
	public String selectLabel(@QueryParam("tagId") String tagId){
		try{
			//设置从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			String json=mapper.writeValueAsString(labelServise.selectLabel(tagId));
			return json;
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//同步标签
	@GET
	@Path("/syncTag")
	public String syncTag(){
		try{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			labelServise.syncTag();
			return "{\"result\":\"success\"}";
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//添加标签
	@POST
	@Path("/addLabel")
	public String addLabel(@BeanParam TomLabel label){
		try{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			label.setCreator(user.getName());
			label.setCreatorId(user.getCode());
			label.setOperator(user.getName());
			labelServise.addLabel(label);
			return "{\"result\":\"success\"}";
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//修改标签
	@POST
	@Path("/updateLabel")
	public String updateLabel(@BeanParam TomLabel label){
		try{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			label.setOperator(user.getName());
			labelServise.updateLabel(label);
			return "{\"result\":\"success\"}";
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//删除标签
	@DELETE
	@Path("/deleteLabel")
	public String deleteLabel(@QueryParam("tagId") String tagId){
		try{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			labelServise.deleteLabel(tagId);
			return "{\"result\":\"success\"}";
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
}
