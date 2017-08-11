package com.chinamobo.ue.system.restful;

 

import java.util.HashMap;
import java.util.Map;

 
import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.dao.TomRollingPictureMapper;
import com.chinamobo.ue.system.entity.TomNews;
import com.chinamobo.ue.system.service.NewsServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;

@Path("/news")
@Scope("request")
@Component
public class NewsRest {
   
	@Autowired
	private NewsServise newsservise;
	@Autowired
	private TomRollingPictureMapper rollingPictureMapper;
	
	JsonMapper mapper=new JsonMapper();
	
	/**
	    * 功能介绍：[根据标题模糊查询(分页)]
	    * 创建人：LG
	    * 创建时间：2016年6月12日 10：34
	    * @param pageNum
	    * @param pageSize
	    * @param newsTitle
	    * @return
	    */
	@GET
	@Path("/selectByList")
	public String selectByList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("newsTitle") String newsTitle){
		
				return mapper.toJson(newsservise.selectByList(pageNum, pageSize, newsTitle));
		
	}
	
	 /**
	    * 功能介绍：[修改资讯发布和撤回]
	    * 创建人:LG
	    * 创建时间：2016年6月14日 10：47
	    * @param newsId
	    * @param isRelease
	    */
	@PUT
	@Path("/updateByReleaser")
	public String updateByReleaser(@QueryParam("newsId") int newsId,@QueryParam("isRelease") String isRelease){
		try{
			Map<Object,Object> map=new HashMap<Object, Object>();
			map.put("resourceId", newsId);
			map.put("pictureCategory", "N");
			map.put("isRelease", "Y");
			int count=rollingPictureMapper.countByPage(map);
			if(count!=0){
				return "{\"result\":\"protected\"}";
			}
			newsservise.updateByReleaser(newsId, isRelease);
			return "{\"result\":\"success\"}";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	    * 功能介绍：[添加资讯]
	    * 创建人：LG
	    * 创建时间：2016年6月14日 13:58
	    * @param tomnews
	    */
    @POST
    @Path("/insertSelective")
    public String insertSelective(@BeanParam TomNews tomnews){
    	try{
//    		System.out.println(tomnews);
    		DBContextHolder.setDbType(DBContextHolder.MASTER);
    		newsservise.insertSelective(tomnews);
    		return "{\"result\":\"success\"}";
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return "{\"result\":\"error\"}";
    	
    }
    
    /**
     * 功能介绍：[添加资讯信息时关联组织ID(查询所有ID)]
     * 创建人：LG
     * 创建时间：2016年6月17日 13：41
     * @return 
     */
    @GET
    @Path("/selectAll")
    public String selectAll(){
    	try{
    		DBContextHolder.setDbType(DBContextHolder.SLAVE);
    		newsservise.SelectAll();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return mapper.toJson(newsservise.SelectAll());
    }
    /**
     * 功能介绍：[编辑资讯]
     * 创建人：LG
     * 创建时间：2016年6月22日 20：36
     * @param tomnews
     */
    @POST
    @Path("/updateByPrimaryKeySelective")
    public String updateByPrimaryKeySelective(@BeanParam TomNews  tomnews){
    	try{
    		DBContextHolder.setDbType(DBContextHolder.MASTER);
    		newsservise.updateByPrimaryKeySelective(tomnews);
    		return "{\"result\":\"success\"}";
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return "{\"result\":\"error\"}";
    }
    /**
     * 功能介绍：[根据ID查询信息]
     * 创建人：LG
     * 创建时间：2016年6月20日 15：24
     */
    @GET
    @Path("/selectById")
    public TomNews selectById(@QueryParam("newsId") int newsId){
    	try{
    		DBContextHolder.setDbType(DBContextHolder.SLAVE);
    		newsservise.selectById(newsId);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return newsservise.selectById(newsId);
    	
    }
    
}
