package com.chinamobo.ue.exam.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.exam.service.AnswerDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 答题详情
 *
 * 作者: JCX
 * 创建时间: 2016年4月9日 上午10:35:35
 */
@Path("/answerDetails") 
@Scope("request") 
@Component
public class AnswerDetailsRest {

	@Autowired
	private AnswerDetailsService answerDetailsService;
	
	ObjectMapper mapper=new ObjectMapper();
	
	/**
	 * 
	 * 功能描述：[查询答题详情]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月21日 下午2:41:58
	 * @param examId
	 * @param code
	 * @return
	 */
	@GET
	@Path("/find")
	public String findAnswerDetails (@QueryParam("examId") int examId,@QueryParam("code") String code,@QueryParam("gradeState") String gradeState){	
		try {
			String json= mapper.writeValueAsString(answerDetailsService.selectAnswerDetails(examId,code,gradeState));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}	
		return "{\"result\":\"error\"}";
	}
}
