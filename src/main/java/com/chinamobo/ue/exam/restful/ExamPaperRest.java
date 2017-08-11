package com.chinamobo.ue.exam.restful;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomTaskExamPaperRelationMapper;
import com.chinamobo.ue.activity.dao.TomTaskPackageMapper;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.activity.entity.TomTaskExamPaperRelation;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dto.ExamPaperDto;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.service.ExamPaperService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Path("/examPaper")
public class ExamPaperRest {

	private static Logger logger = LoggerFactory.getLogger(ExamPaperRest.class);

	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomTaskExamPaperRelationMapper taskExamPaperRelationMapper;
	
	ObjectMapper mapper = new ObjectMapper(); 

	@GET
	@Path("/view")
	public void view(@Context HttpServletResponse response,@Context HttpServletRequest request){
		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/exam/examPaperForm.html");
		try {
			dispatcher .forward(request, response);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
	}
	/**
	 * 
	 * 功能描述：[编辑查询]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月11日 下午5:04:32
	 * @param examPaperId
	 * @return
	 */
	@GET
	@Path("/findById")
	public String findById(@QueryParam("examPaperId") int examPaperId) {
		JsonMapper jsonMapper = new JsonMapper();
		try {
			return jsonMapper.toJson(examPaperService.findExamQuestion(examPaperId));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	@GET
	@Path("/findOne")
	public String findOne(@QueryParam("examPaperId") int examPaperId) {
		JsonMapper jsonMapper = new JsonMapper();
		try {
			return jsonMapper.toJson(examPaperService.findById(examPaperId));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * 功能描述：[添加试卷]
	 *
	 * 创建者：wjx 创建时间: 2016年3月8日 上午10:34:16
	 * 
	 * @param examPaper
	 * @return
	 */
	@POST
	@Path("/addExamPaper")
	public String addExamPaper(@BeanParam ExamPaperDto dto) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			String status=examPaperService.addExamPaper(dto);
			if(status.equals("N")){
				return "{\"result\":\"wrong\"}";
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[修改试卷]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月10日 上午11:32:12
	 * @param examPaper
	 * @return
	 */
	@POST
	@Path("/updateExamPaper")
	public String updateExamPaper(@BeanParam ExamPaperDto dto) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			Map<Object, Object> map = new HashMap<Object, Object>();
			TomExam exam=new TomExam();
			exam.setExamPaperId(dto.getExamPaperId());
			map.put("example", exam);
			if(examMapper.countByExample(map)>0){
				return "{\"result\":\"protected\"}";
			}else if(taskExamPaperRelationMapper.countByExample(dto.getExamPaperId())>0){
				return "{\"result\":\"inActivity\"}";
			}
			String status=examPaperService.updateExamPaper(dto);
			if(status.equals("N")){
				return "{\"result\":\"wrong\"}";
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
	}
	/**
	 * 
	 * 功能描述：[添加试卷题库关联List]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月10日 上午11:32:27
	 * @param examQuestionList
	 * @param examPaperId
	 * @param examPaperType
	 * @return
	 */
	@POST
	@Path("/addExamQuestionList")
	public String addExamQuestionList(@BeanParam List<TomExamQuestion> examQuestionList,
			@FormParam("examPaperId") int examPaperId, @FormParam("examPaperType") String examPaperType) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try {
			for (TomExamQuestion examQuestion : examQuestionList) {
				examPaperService.addExamQuestion(examQuestion, examPaperId,examPaperType);
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
		
	}
	/**
	 * 
	 * 功能描述：[添加试卷题库关联]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月10日 上午11:35:47
	 * @param examQuestion
	 * @param examPaperId
	 * @param examPaperType
	 * @return
	 */
	@POST
	@Path("/addExamQuestion")
	public String addExamQuestion(@BeanParam TomExamQuestion examQuestion,
			@FormParam("examPaperId") int examPaperId, @FormParam("examPaperType") String examPaperType){
		try{
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			examPaperService.addExamQuestion(examQuestion, examPaperId,examPaperType);
			return "{\"result\":\"success\"}";
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
		
	}
	
	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月12日 下午1:44:13
	 * @param pageNum
	 * @param pageSize
	 * @param examPaperName
	 * @param examPaperType
	 * @return
	 */
	@GET
	@Path("/findPage")
	public String findExamPaperByPage(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("examPaperName") String examPaperName,@QueryParam("examPaperType") String examPaperType){
		
		TomExamPaper example=new TomExamPaper();
		if(examPaperName!=null){
			examPaperName=examPaperName.replaceAll("/", "//");
			examPaperName=examPaperName.replaceAll("%", "/%");
			examPaperName=examPaperName.replaceAll("_", "/_");
		}
		
		example.setExamPaperName(examPaperName);
		example.setExamPaperType(examPaperType);
		PageData<TomExamPaper> page =examPaperService.selectPage(pageNum,pageSize,example);
	
		try {
			
			String json=mapper.writeValueAsString(page);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	@DELETE
	@Path("/delete")
	public String deleteExamPaper (@QueryParam("examPaperId") int examPaperId){
		try{
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			if(taskExamPaperRelationMapper.countByExample(examPaperId)>0){
				return "{\"result\":\"inActivity\"}";
			}
			String res=examPaperService.deleteExamPaper(examPaperId);
			return res;
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[查看试卷]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年5月9日 下午3:13:09
	 * @param examPaperId
	 * @return
	 */
	@GET
	@Path("/examine")
	public String examine(@QueryParam("examPaperId") int examPaperId){
		try{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			return examPaperService.examine(examPaperId);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return "{\"result\":\"error\"}";
	}
}
