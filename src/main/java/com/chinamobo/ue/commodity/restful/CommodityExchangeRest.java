package com.chinamobo.ue.commodity.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.commodity.entity.TomCommodityExchange;
import com.chinamobo.ue.commodity.service.CommodityExchangeService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/commodityExchange") 
@Scope("request") 
@Component
public class CommodityExchangeRest {
	@Autowired
	private CommodityExchangeService commodityExchangeService;
	ObjectMapper mapper = new ObjectMapper(); 
	
	/**
	 * 
	 * 功能描述：[商品兑换分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月18日
	 * @param pageNum
	 * @param pageSize
	 * @param commodityExchange
	 * @return
	 */
	@GET
	@Path("/commodityExchangeList")
	public String commodityExchangeList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("empCode") String empCode,
			@QueryParam("postMethod") String postMethod,@QueryParam("exchangeState") String exchangeState){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(commodityExchangeService.queryCommodityExchange(pageNum, pageSize, empCode,postMethod,exchangeState));
	}
	
	/**
	 * 
	 * 功能描述：[修改兑换状态]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月20日
	 * @param commodityExchange
	 * @return
	 */
	@POST
	@Path("/modifyCommodityExchangeState")
	public String modifyCommodityExchangeState(@BeanParam TomCommodityExchange commodityExchange){
		try{

			DBContextHolder.setDbType(DBContextHolder.MASTER);
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			commodityExchange.setExchangeState("Y");
			commodityExchange.setExchangeTime(format.parse(commodityExchange.getExchangeTimeS()));
			commodityExchangeService.modifyCommodityExchangeState(commodityExchange);
			return "{\"result\":\"success\"}";
		}catch(Exception e){
			return "{\"result\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * 功能描述：[兑换记录导出]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月2日
	 * @return
	 */
	@GET
    @Path("/downloadExchangeExcel")
	public Response downloadExchangeExcel(){
    	try {
    		TomCommodityExchange example =new TomCommodityExchange();
    		example.setExchangeState("Y");
    		List<TomCommodityExchange> commodityExchanges=commodityExchangeService.selectByExample(example);
    		String fileName="兑换历史.xls";
	    	String path=commodityExchangeService.TopicsToExcel(commodityExchanges, fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			
			String downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.header("Content-Type", "application/text");
	        response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
	        return response.build();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	
	@GET
	@Path("/selectByExchange")
	public String selectByExchange(@QueryParam("commodityId") int commodityId,@QueryParam("code") String code,@QueryParam("exchangeTime") String exchangeTime){
		try{
			String json=mapper.writeValueAsString(commodityExchangeService.selectByExchange(commodityId,code,exchangeTime));
			return json;
		}catch(Exception e){
			e.printStackTrace();
			return "{\"result\":\"error\"}"; 
		}
	}
}
