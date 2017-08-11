package com.chinamobo.ue.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.common.PageData;
import com.chinamobo.ue.system.dao.TomNewsMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.dao.TomRollingPictureMapper;
import com.chinamobo.ue.system.entity.TomNews;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;

@Service

public class NewsServise {
   @Autowired
   private TomNewsMapper tomnewsmapper;
   @Autowired
   private TomRollingPictureMapper rollingPictureMapper;
   @Autowired
   private TomOrgMapper tomorgmapper;
   
   ShiroUtils user = new ShiroUtils();
   ShiroUser username=ShiroUtils.getCurrentUser();

   /**
    * 功能介绍：[添加资讯]
    * 创建人：LG
    * 创建时间：2016年6月12日 20:21
    * @param tomnews
    */
   @Transactional
   public void insertSelective(TomNews tomnews){
	  Date date=new Date();
	   tomnews.setCreateTime(date);
	   tomnews.setUpdateTime(date);
	   
	   tomnews.setReleaseTime(date);
	   
	   tomnews.setViewCount(0);
	   tomnews.setIsRelease("N");
	   tomnews.setCreater(username.getName());
	   tomnews.setCreatorId(username.getCode());
	   tomnewsmapper.insert(tomnews);
   }
   /**
    * 功能介绍：[编辑资讯]
    * 创建人：LG
    * 创建时间：2016年6月22日 20：36
    * @param tomnews
    */
   @Transactional
   public void updateByPrimaryKeySelective(TomNews tomnews){
	   Date date=new Date();
	   tomnews.setUpdateTime(date);
	   tomnews.setOperator(username.getName());
	   tomnews.setReleaseTime(date);
	   tomnewsmapper.updateByPrimaryKeyWithBLOBs(tomnews);
	   
	   Map<Object,Object> map=new HashMap<Object, Object>();
	   map.put("resourceId",tomnews.getNewsId());
	   map.put("pictureCategory", "N");
	   map.put("resourceName", tomnews.getNewsTitle());
	   rollingPictureMapper.updateResourceName(map);
   }
   
   /**
    * 功能介绍：[根据标题模糊查询(分页)]
    * 创建人：LG
    * 创建时间：2016年6月12日 20：56
    * @param pageNum
    * @param pageSize
    * @param newsTitle
    * @return
    */
   public PageData<TomNews> selectByList(int pageNum, int pageSize,String newsTitle){
//	   if(newsTitle!=null){
//		   newsTitle=newsTitle.replaceAll("/", "//");
//		   newsTitle=newsTitle.replaceAll("%", "/%");
//		   newsTitle=newsTitle.replaceAll("_", "/_");
//	   }
	   DBContextHolder.setDbType(DBContextHolder.SLAVE);
	   List<TomNews> list=new ArrayList<TomNews>();
	   Map<Object, Object> map=new HashMap<Object, Object>();
	   map.put("newsTitle", newsTitle);
	   int count=tomnewsmapper.selectBycount(map);
	   if(pageSize==-1){
			pageSize = count;
		}
		
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum",  pageSize);//pageNum * 
	   list=tomnewsmapper.selectByList(map);
	   List<TomNews> lists=new ArrayList<TomNews>();
	   for(TomNews news :list){
		   TomNews tomnews=new TomNews();
		   tomnews.setNewsId(news.getNewsId());
		   tomnews.setNewsTitle(news.getNewsTitle());
		   tomnews.setReleaseTime(news.getReleaseTime());
		   tomnews.setIsRelease(news.getIsRelease());
		   tomnews.setReleaser(news.getReleaser());
		   tomnews.setAdmins(news.getAdmins());
		   if(news.getIsRelease().equals("Y")){
			   tomnews.setViewCount(news.getViewCount());
		   }else{
			   tomnews.setViewCount(null);//null在页面显示未发布状态
		   }
	      
		   tomnews.setCreater(news.getCreater());
		   tomnews.setOperator(news.getOperator());
		   tomnews.setUpdateTime(news.getUpdateTime());
		   lists.add(tomnews);
	   }
	   PageData<TomNews> page=new PageData<TomNews>();
		page.setCount(count);
		page.setDate(lists);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
	return page;
	   
   }
   /**
    * 功能介绍：[资讯发布和撤回的查询]
    * 创建人：LG
    * 创建时间：2016年6月13日 16：09
    * @param newsId
    */
   public TomNews selectByReleaser(Integer newsId){
	   DBContextHolder.setDbType(DBContextHolder.SLAVE);
	   Map<Object,Object> map=new HashMap<Object,Object>();
	   TomNews tomnews=new TomNews();
	   map.put("newsId", newsId);
	   TomNews i=tomnewsmapper.selectByReleaser(map);
	   if(i!=null){
		   if(i.getIsRelease().equals("Y")){
			   tomnews.setIsRelease("撤回");
		   }else if(i.getIsRelease().equals("N")){
			   tomnews.setIsRelease("发布");
		   }else{
			   return tomnews;
		   }
	   }
	   
	return tomnews;
	   
   }
   /**
    * 功能介绍：[修改资讯发布和撤回]
    * 创建人:LG
    * 创建时间：2016年6月13日 15：40
    * @param newsId
    * @param isRelease
    */
   public void updateByReleaser(int newsId,String isRelease){
	   DBContextHolder.setDbType(DBContextHolder.MASTER);
	  Date date=new Date();
	  TomNews tomnews=new TomNews();
	  if(null!=isRelease){
		  tomnews.setIsRelease(isRelease);
          if(isRelease.equals("Y")){
			  tomnews.setReleaseTime(date);
			  tomnews.setUpdateTime(date);
			  tomnews.setReleaser(username.getName());
			  tomnews.setOperator(username.getName());
			  tomnews.setNewsId(newsId);
		  }else if(isRelease.equals("N")){
			  tomnews.setNewsId(newsId);
			  tomnews.setOperator(username.getName());
			  tomnews.setUpdateTime(date);
		  }
	  }
	  tomnewsmapper.updateByPrimaryKeySelective(tomnews);
   }
   
   /**
    * 功能介绍：[添加资讯信息时关联组织ID(查询所有ID)]
    * 创建人：LG
    * 创建时间：2016年6月17日 13：41
    * @return 
    */
   public List<TomOrg> SelectAll(){
	   DBContextHolder.setDbType(DBContextHolder.SLAVE);
	   List<TomOrg> list=new ArrayList<TomOrg>();
	   list=tomorgmapper.select();
	   List<TomOrg> lists=new ArrayList<TomOrg>();
	   for(TomOrg org:list){
		   TomOrg tomorg=new TomOrg();
		   tomorg.setCode(org.getCode());
		   tomorg.setPkOrg(org.getPkOrg());
		   tomorg.setName(org.getName());
		   lists.add(tomorg);
	   }
//	   String json=mapper.toJson(lists);
//	   System.out.println(json);
	return lists;
	   
   }
   /**
    * 功能介绍：[编辑前通过ID查询]
    * 创建人：LG
    * 创建时间：2016年6月20日 15：36
    * @param newsId
    */
   public TomNews selectById(Integer newsId){
	   DBContextHolder.setDbType(DBContextHolder.SLAVE);
	   TomNews news=new TomNews();
	   news=tomnewsmapper.selectById(newsId);
	   
		   TomNews tomnews=new TomNews();
		   tomnews.setNewsId(news.getNewsId());
		   tomnews.setNewsTitle(news.getNewsTitle());
		   tomnews.setNewsDetails(news.getNewsDetails());
		   tomnews.setNewsPicture(news.getNewsPicture());
		   tomnews.setReleaseTime(news.getReleaseTime());
		   tomnews.setAdmins(news.getAdmins());
		   tomnews.setPkOrg(news.getPkOrg());
		   tomnews.setIsRelease(news.getIsRelease());
		   tomnews.setViewCount(news.getViewCount());
		   tomnews.setCreater(news.getCreater());
		   tomnews.setOperator(news.getOperator());
		   tomnews.setUpdateTime(news.getUpdateTime());
	   
	return tomnews;
	   
   }
   
   
   public PageData<TomNews> selectByListForRooling(int pageNum, int pageSize,String newsTitle){
//	   if(newsTitle!=null){
//		   newsTitle=newsTitle.replaceAll("/", "//");
//		   newsTitle=newsTitle.replaceAll("%", "/%");
//		   newsTitle=newsTitle.replaceAll("_", "/_");
//	   }
	   DBContextHolder.setDbType(DBContextHolder.SLAVE);
	   List<TomNews> list=new ArrayList<TomNews>();
	   Map<Object, Object> map=new HashMap<Object, Object>();
	   map.put("newsTitle", newsTitle);
	   int count=tomnewsmapper.selectBycountForRooling(map);
	   if(pageSize==-1){
			pageSize = count;
		}
		
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum",  pageSize);//pageNum * 
	   list=tomnewsmapper.selectByListForRoolling(map);
	   List<TomNews> lists=new ArrayList<TomNews>();
	   for(TomNews news :list){
		   TomNews tomnews=new TomNews();
		   tomnews.setNewsId(news.getNewsId());
		   tomnews.setNewsTitle(news.getNewsTitle());
		   tomnews.setReleaseTime(news.getReleaseTime());
		   tomnews.setIsRelease(news.getIsRelease());
		   tomnews.setReleaser(news.getReleaser());
		   tomnews.setAdmins(news.getAdmins());
		   if(news.getIsRelease().equals("Y")){
			   tomnews.setViewCount(news.getViewCount());
		   }else{
			   tomnews.setViewCount(null);//null在页面显示未发布状态
		   }
	      
		   tomnews.setCreater(news.getCreater());
		   tomnews.setOperator(news.getOperator());
		   tomnews.setUpdateTime(news.getUpdateTime());
		   lists.add(tomnews);
	   }
	   PageData<TomNews> page=new PageData<TomNews>();
		page.setCount(count);
		page.setDate(lists);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
	return page;
	   
   }

}
