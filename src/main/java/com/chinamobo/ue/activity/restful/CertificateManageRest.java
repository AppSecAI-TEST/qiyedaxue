package com.chinamobo.ue.activity.restful;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.service.CertificateManageService;

/**
 * 描述 [证书管理]
 * 创建者 LXT
 * 创建时间 2017年6月8日 下午3:42:12
 */
@Path("/certificateManage")
@Scope("request") 
@Component
public class CertificateManageRest {
	
	@Autowired
	private CertificateManageService certificateManageService;
	

	/**
	 * 功能描述 [分页查询]
	 * 创建者 LXT
	 * 创建时间 2017年6月8日 下午3:49:25
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GET
	@Path("/certificateManageList")
	public String certificateManageList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("10") @QueryParam("pageSize") int pageSize,
			@QueryParam("activityName") String activityName){
		
		return certificateManageService.certificateManageList(pageNum, pageSize,activityName);
	}
	
	@GET
	@Path("downExport")
	public void downExport(@QueryParam("activityId") Integer activityId,@Context HttpServletRequest request,@Context HttpServletResponse response){
		try {
			certificateManageService.downExport(activityId,request,response);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	/**
	 * 功能描述 [新增]
	 * 创建者 LXT
	 * 创建时间 2017年6月8日 下午3:49:39
	 * @param jsonStr
	 * @return
	 */
	@POST
	@Path("/addCertificate")
	public String addCertificate(String jsonStr){
		return certificateManageService.insert(jsonStr);
	}
	
	/**
	 * 功能描述 [修改]
	 * 创建者 LXT
	 * 创建时间 2017年6月8日 下午3:49:46
	 * @param jsonStr
	 * @return
	 */
	@POST
	@Path("/updateCertificate")
	public String updateCertificate(String jsonStr){
		return certificateManageService.updateCertificateManage(jsonStr);
	}
	
	/**
	 * 功能描述 [删除]
	 * 创建者 LXT
	 * 创建时间 2017年6月8日 下午3:49:52
	 * @param jsonStr
	 * @return
	 */
	@DELETE
	@Path("/deleteCertificate")
	public String deleteCertificate(Integer activityId){
		return certificateManageService.deleteCertificateManage(activityId);
	}
}
