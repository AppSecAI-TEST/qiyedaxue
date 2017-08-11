package com.chinamobo.ue.system.restful;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.entity.TomLabelClass;
import com.chinamobo.ue.system.service.LabelClassServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/labelClass")
@Scope("request")
@Component
public class LabelClassRest {
	@Autowired
	private LabelClassServise labelClassServise;
	ObjectMapper mapper = new ObjectMapper();
	ShiroUser user=ShiroUtils.getCurrentUser();
	
	//根据分类id查询
	@GET
	@Path("/selectClass")
	public String selectClass(@QueryParam("classId") Integer classId){
		try{
			//设置从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			String json=mapper.writeValueAsString(labelClassServise.selectClass(classId));
			return json;
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//全查标签分类忽略微信分类
	@GET
	@Path("/selectLabelClass")
	public String selectLabelClass(){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomLabelClass> list = labelClassServise.selectLabelClass();
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	//添加标签分类
	@POST
	@Path("/addLabelClass")
	public String addLabelClass(@BeanParam TomLabelClass labelClass){
		try{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			labelClass.setCreator(user.getName());
			labelClass.setCreatorId(user.getCode());
			labelClass.setOperator(user.getName());
			labelClassServise.addLabelClass(labelClass);
			return "{\"result\":\"success\"}";
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//编辑标签分类
	@POST
	@Path("/editLabelClass")
	public String editLabelClass(@BeanParam TomLabelClass labelClass){
		try{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			labelClass.setOperator(user.getName());
			labelClassServise.editLabelClass(labelClass);
			return "{\"result\":\"success\"}";
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//删除分类
	@DELETE
	@Path("/deleteLabelClass")
	public String deleteLabelClass(@QueryParam("classId") Integer classId){
		try{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			labelClassServise.deleteLabelClass(classId);
			return "{\"result\":\"success\"}";
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
}
