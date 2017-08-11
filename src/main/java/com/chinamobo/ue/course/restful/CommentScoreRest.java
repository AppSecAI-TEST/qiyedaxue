package com.chinamobo.ue.course.restful;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.course.entity.TomGradeDimension;
import com.chinamobo.ue.course.service.GradeDimensionService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 评论管理
 *
 * 作者: WangLg
 * 创建时间: 2016年3月3日 上午10:45:34
 */
@Path("/commentScore") 
@Scope("request") 
@Component
public class CommentScoreRest {

	@Autowired
	private GradeDimensionService gradeDimensionService;//评分服务
	
	ShiroUser user=ShiroUtils.getCurrentUser();
	
	@PUT 
	@Path("/delete")
	public String deleteGradeDimenCom(@QueryParam("gradeDimensionId") int gradeDimensionId,@Context HttpServletResponse servletResponse){
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try {
			gradeDimensionService.deleteGradeDimension(gradeDimensionId);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	/**
	 * 功能描述：[修改讲师评分/课程评分]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月2日 下午1:30:50
	 * @param gradeDimensionType
	 * @param gradeDimensionName
	 * @param gradeDimensionNumber
	 * @param request
	 * @return
	 */
	@POST 
	@Path("/update")
	public String updateGradeDimenCom(@BeanParam TomGradeDimension gradeDimension,@Context HttpServletResponse servletResponse){
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try {
			gradeDimension.setOperator(user.getName());
			gradeDimension.setOperatorId(user.getCode());
			gradeDimensionService.updateGradeDimension(gradeDimension);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	
	/**
	 * 功能描述：[添加讲师评分/课程评分]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月2日 上午9:45:28
	 * @param gradeDimensionType
	 * @param gradeDimensionName
	 * @param request
	 * @return
	 */
	@POST	
	@Path("/add") 
	public String saveGradeDimenCom(@BeanParam TomGradeDimension gradeDimension,@Context HttpServletResponse servletResponse){
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try {
			gradeDimension.setCreator(user.getName());
			gradeDimension.setCreatorId(user.getCode());
			gradeDimension.setOperator(user.getName());
			gradeDimensionService.saveGradeDimension(gradeDimension);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	
	/**
	 * 功能描述：[查询讲师/课程维度]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月2日 上午11:49:32
	 * @param gradeDimensionType
	 * @param gradeDimensionName
	 * @param request
	 * @return
	 * @throws JsonProcessingException 
	 */
	@GET
	@Path("/findList")
	@Produces({MediaType.APPLICATION_JSON})
	public String findGradeDimenComList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("10") @QueryParam("pageSize") int pageSize,
			@QueryParam("gradeDimensionName") String gradeDimensionName,@QueryParam("gradeDimensionType") String gradeDimensionType,
			@Context HttpServletRequest request) throws JsonProcessingException{
		JsonMapper jsonMapper = new JsonMapper();
		Map<Object, Object> queryMap = Maps.newHashMap();
		
		if(gradeDimensionName!=null){
			gradeDimensionName=gradeDimensionName.replaceAll("/", "//");
			gradeDimensionName=gradeDimensionName.replaceAll("%", "/%");
			gradeDimensionName=gradeDimensionName.replaceAll("_", "/_");
			
		}
		
		if("'C'".equals(gradeDimensionType)){
			gradeDimensionType ="C";
		}else if("'L'".equals(gradeDimensionType)){
			gradeDimensionType ="L";
		}
		queryMap.put("gradeDimensionName", gradeDimensionName);
		queryMap.put("gradeDimensionType", gradeDimensionType);
		String json = jsonMapper.toJson(gradeDimensionService.findGradeDimenComList(pageNum, pageSize, queryMap));
		return json;
	}
	
	@GET
	@Path("/findDetails")
	@Produces({MediaType.APPLICATION_JSON})
	public String findDetails(@QueryParam(value = "gradeDimensionId") int gradeDimensionId,@Context HttpServletRequest request,@Context HttpServletResponse servletResponse) throws JsonProcessingException{
			JsonMapper jsonMapper = new JsonMapper();
			Map<Object,Object> map = Maps.newHashMap();
			map.put("gradeDimensionId", gradeDimensionId);
			String json;		
				json= jsonMapper.toJson(gradeDimensionService.findDetails(map));
		return json;
	}
	
	@GET
	@Path("/findModel")
	@Produces({MediaType.APPLICATION_JSON})
	public String findGradeDimenComModel(@QueryParam("gradeDimensionType") String gradeDimensionType,
			@Context HttpServletRequest request,@Context HttpServletResponse servletResponse) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		JsonMapper jsonMapper = new JsonMapper();
		Map<Object, Object> queryMap = Maps.newHashMap();
		queryMap.put("gradeDimensionType", gradeDimensionType);
		String json = "";
		 json = jsonMapper.toJson(gradeDimensionService.findGradeDimenModel(queryMap));
		return json;
	}
}
