package com.chinamobo.ue.exam.restful;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.service.RetakingExamService;
import com.chinamobo.ue.utils.JsonMapper;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 补考信息rest
 *
 * 作者: JCX
 * 创建时间: 2016年4月1日 下午1:27:23
 */
@Path("/retakingExam") 
@Scope("request") 
@Component
public class RetakingExamRest {

	@Autowired
	private RetakingExamService retakingExamService;
	
	/**
	 * 
	 * 功能描述：[根据考试id查询补考]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月1日 下午1:35:45
	 * @param examId
	 * @return
	 */
	@GET
	@Path("/findList")
	public String findList(@QueryParam("examId") int examId){
		try {
			JsonMapper jsonMapper = new JsonMapper();
			TomRetakingExam example=new TomRetakingExam();
			example.setExamId(examId);
			List<TomRetakingExam> list=retakingExamService.selectListByExample(example);
			return jsonMapper.toJson(list);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
}
