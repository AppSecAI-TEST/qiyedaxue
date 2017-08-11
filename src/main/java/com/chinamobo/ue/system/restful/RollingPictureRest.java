package com.chinamobo.ue.system.restful;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.system.entity.TomRollingPicture;
import com.chinamobo.ue.system.service.NewsServise;
import com.chinamobo.ue.system.service.RollingPictureServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/rollingPicture")
@Scope("request")
@Component
public class RollingPictureRest {
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private RollingPictureServise rollingPictureServise;
	@Autowired
	private NewsServise newsServise;
	@Autowired
	private CourseService courseService;
	/**
	 * 
	 * 功能描述：[添加轮播图]
	 *
	 * 创建者：cjx 创建时间: 2016年5月25日 下午2:14:15
	 * 
	 * @param tomRollingPicture
	 * @return
	 */
	@POST
	@Path("/insert")
	@Produces({ MediaType.APPLICATION_JSON })
	public String insert(@BeanParam TomRollingPicture tomRollingPicture) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			rollingPictureServise.insert(tomRollingPicture);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"result\":\"success\"}";

	}

	/**
	 * 
	 * 功能描述：[更改轮播图]
	 *
	 * 创建者：cjx 创建时间: 2016年5月25日 下午2:13:55
	 * 
	 * @param tomRollingPicture
	 * @return
	 */
	@PUT
	@Path("/update")
	@Produces({ MediaType.APPLICATION_JSON })
	public String update(@BeanParam TomRollingPicture tomRollingPicture) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			rollingPictureServise.update(tomRollingPicture);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"result\":\"success\"}";
	}

	/**
	 * 
	 * 功能描述：[更改轮播图置顶]
	 *
	 * 创建者：cjx 创建时间: 2016年5月25日 下午2:43:21
	 * 
	 * @param pictureId
	 * @param deleted
	 * @param isTop
	 * @param isRelease
	 * @return
	 */
	@PUT
	@Path("/updateStatus")
	@Produces({ MediaType.APPLICATION_JSON })
	public String updateStatus(@QueryParam("pictureId") int pictureId, @QueryParam("deleted") String deleted,
			@QueryParam("isTop") String isTop, @QueryParam("isRelease") String isRelease) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			rollingPictureServise.updateStatus(pictureId, deleted, isTop, isRelease);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"result\":\"success\"}";
	}
	/**
	 * 
	 * 功能描述：[伪删除]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月20日 下午8:04:52
	 * @param pictureId
	 * @param deleted
	 * @return
	 */
	@PUT
	@Path("/delete")
	@Produces({ MediaType.APPLICATION_JSON })
	public String delete(@QueryParam("pictureId") int pictureId, @QueryParam("deleted") String deleted
			) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			rollingPictureServise.delete(pictureId, deleted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"result\":\"success\"}";
	}
	/**
	 * 
	 * 功能描述：[发布]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月20日 下午8:16:05
	 * @param pictureId
	 * @param isRelease
	 * @return
	 */
	@PUT
	@Path("/isRelease")
	@Produces({ MediaType.APPLICATION_JSON })
	public String isRelease(@QueryParam("pictureId") int pictureId, @QueryParam("isRelease") String isRelease) {
		String status = "";
		try {
			 status = rollingPictureServise.isRelease(pictureId, isRelease);
				return "{\"result\":\""+status+"\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月16日 下午5:27:31
	 * @param pageNum
	 * @param pageSize
	 * @param resourceName
	 * @return
	 */
	@GET
	@Path("/findpage")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByPage(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("resourceName") String resourceName) {
		try {
			return mapper.writeValueAsString(rollingPictureServise.findPage(pageNum, pageSize, resourceName));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[查看次数详情]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月16日 下午5:35:18
	 * @param pictureId
	 * @return
	 */
	@GET
	@Path("/findByCount")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByCount(@QueryParam("pictureId") int pictureId) {
		try {
			return mapper.writeValueAsString(rollingPictureServise.findByCount(pictureId));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[按标题查看内容]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月16日 下午7:54:45
	 * @param pictureId
	 * @return
	 */
	@GET
	@Path("/findByTitle")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByTitle(@QueryParam("pictureId") int pictureId) {
		try {
			return mapper.writeValueAsString(rollingPictureServise.findByTitle(pictureId));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[查找课程或考试内容]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月22日 下午6:06:41
	 * @param picture_category
	 * @param pageNum
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@GET
	@Path("/findByCategory")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findByCategory(@QueryParam("pictureCategory") String pictureCategory,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize, @QueryParam("name") String name) {
		try {
			if("N".equals(pictureCategory)){
				return mapper.writeValueAsString(newsServise.selectByListForRooling(pageNum, pageSize, name));
			}else{
				TomCourses example=new TomCourses();
				example.setCourseName(name);
				return mapper.writeValueAsString(courseService.selectListByPageForRooling(pageNum, pageSize, example));
			}			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[编辑回选查询]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月23日 上午9:25:37
	 * @param pictureId
	 * @return
	 */
	@GET
	@Path("/findById")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findById(@QueryParam("pictureId") int pictureId) {
		try {
			
				return mapper.writeValueAsString(rollingPictureServise.findById(pictureId));
	
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
}
