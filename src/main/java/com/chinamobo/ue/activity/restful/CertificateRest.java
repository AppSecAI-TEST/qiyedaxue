package com.chinamobo.ue.activity.restful;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.service.CertificateService;

/**
 * 描述 [证书模版控制器]
 * 创建者 LXT
 * 创建时间 2017年6月6日 下午3:41:11
 */
@Path("/certificate") 
@Scope("request") 
@Component
public class CertificateRest {
	
	@Autowired
	private CertificateService certificateService;
	/**
	 * 功能描述 [查询证书模版 分页]
	 * 创建者 LXT
	 * 创建时间 2017年6月6日 下午3:47:27
	 * @return
	 */
	@GET
	@Path("/certificateList")
	public String certificateList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize){
		return certificateService.certificateList(pageNum, pageSize);
	}
	
	/**
	 * 功能描述 [添加证书模版]
	 * 创建者 LXT
	 * 创建时间 2017年6月6日 下午3:50:37
	 * @param jsonStr
	 * @return
	 */
	@POST
	@Path("/addCertificate")
	public String addCertificate(String jsonStr){
		return certificateService.insert(jsonStr);
	}
	
	/**
	 * 功能描述 [修改证书模版]
	 * 创建者 LXT
	 * 创建时间 2017年6月6日 下午3:51:41
	 * @param jsonStr
	 * @return
	 */
	@POST
	@Path("/updateCertificate")
	public String updateCertificate(String jsonStr){
		return certificateService.update(jsonStr);
	}
	
	/**
	 * 功能描述 [删除证书模版]
	 * 创建者 LXT
	 * 创建时间 2017年6月6日 下午3:53:04
	 * @param jsonStr
	 * @return
	 */
	@POST
	@Path("/deleteCertificate")
	public String deleteCertificate(Integer id){
		return certificateService.delete(id);
	}
}
