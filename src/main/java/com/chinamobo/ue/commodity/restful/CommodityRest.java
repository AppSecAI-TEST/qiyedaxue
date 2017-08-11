package com.chinamobo.ue.commodity.restful;

import java.util.Date;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.commodity.entity.TomCommodity;
import com.chinamobo.ue.commodity.service.CommodityService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/commodity") 
@Scope("request") 
@Component
public class CommodityRest {

	ShiroUser user=ShiroUtils.getCurrentUser();
	ObjectMapper mapper = new ObjectMapper(); 
	@Autowired
	private CommodityService  commodityService;
	
	/**
	 * 
	 * 功能描述：[添加商品]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月18日 下午2:13:25
	 * @param commodity
	 * @return
	 */
	@POST
	@Path("add")
	public String addCommodity(@BeanParam TomCommodity commodity){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			commodity.setCreator(user.getName());
			commodity.setCreatorId(user.getCode());
			commodity.setLastOperator(user.getName());
			commodity.setCreateTime(new Date());
			commodity.setUpdateTime(new Date());
			commodityService.addCommodity(commodity);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * 功能描述：[修改商品]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月18日 下午2:29:38
	 * @param commodity
	 * @return
	 */
	@POST
	@Path("edit")
	public String editCommodity(@BeanParam TomCommodity commodity){
		try {

			DBContextHolder.setDbType(DBContextHolder.MASTER);
			commodity.setLastOperator(user.getName());
			commodity.setUpdateTime(new Date());
			commodityService.editCommodity(commodity);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月19日 上午11:18:25
	 * @param pageNum
	 * @param pageSize
	 * @param commodityName
	 * @return
	 */
	@GET
	@Path("/findList")
	public String findList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("commodityName")String commodityName){
		try {
			String json=mapper.writeValueAsString(commodityService.findList(pageNum,pageSize,commodityName));
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * 功能描述：[商品上下架]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月19日 下午12:33:21
	 * @param commodityId
	 * @return
	 */
	@GET
	@Path("/updateStatus")
	public String updateStatus( @QueryParam("commodityId") int commodityId){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			commodityService.updateStatus(commodityId);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * 功能描述：[查询详情]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月6日 下午2:26:44
	 * @param commodityId
	 * @return
	 */
	@GET
	@Path("/findOne")
	public String findOne(@QueryParam("commodityId")int commodityId){
		try {
			String json=mapper.writeValueAsString(commodityService.findOne(commodityId));
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
}
