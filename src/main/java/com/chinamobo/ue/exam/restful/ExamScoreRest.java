package com.chinamobo.ue.exam.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.service.ExamScoreService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 考试成绩管理rest
 *
 * 作者: JCX
 * 创建时间: 2016年3月18日 下午4:27:07
 */
@Path("/examScore") 
@Scope("request") 
@Component
public class ExamScoreRest {

	@Autowired
	private ExamScoreService examScoreService;
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private TomExamMapper examMapper;
	ObjectMapper mapper = new ObjectMapper(); 
	
	/**
	 * 
	 * 功能描述：[获取考试的成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月18日 下午5:25:15
	 * @param pageNum
	 * @param pageSize
	 * @param examId
	 * @return
	 */
	@GET
	@Path("/findPage")
	public String list(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("examId") int examId){
		String json;
		try {
			json = mapper.writeValueAsString(examScoreService.selectByPage(pageNum,pageSize,examId));
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[主观题评分]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月5日 下午4:40:04
	 * @param topicIds
	 * @param scores
	 * @return
	 */
	@POST
	@Path("grade")
	public String grade(@FormParam("examId")Integer examId,@FormParam("code")String code,
			@FormParam("topicId[]")List<Integer> topicIds,@FormParam("rightScore[]")List<Integer> rightScores,
			@FormParam("score[]")List<Integer> scores){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			examScoreService.grade(examId,code,topicIds,scores,rightScores);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	@POST
	@Path("grade1")
	public String test(List<Integer> ids){
		System.out.println(ids);
		return null;
	}
	
	/**
     * 
     * 功能描述：[生成成绩模板并下载]
     *
     * 创建者：JCX
     * 创建时间: 2016年5月9日 下午8:22:43
     * @param questionBankId
     * @return
     */
    @GET
    @Path("/downloadScoreExcel")
	public Response downloadScoreExcel(@QueryParam("examId") int examId){
    	try {
	    	List<TomExamScore> examScores=examScoreMapper.selectExcelListByPage(examId);
	    	TomExam exam=examMapper.selectByPrimaryKey(examId);
	    	String fileName=exam.getExamName()+"成绩模板.xls";
	    	String path=examScoreService.TopicsToExcel(examScores,exam.getExamNumber(), fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			
			String downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.header("Content-Type", "application/text");
	        response.header("Content-Disposition","attachment; filename=\""+downFileName+"\"");
	        
	        return response.build();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return null;
	}
}
