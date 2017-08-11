package com.chinamobo.ue.common.restful;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomTaskPackageMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomTaskPackage;
import com.chinamobo.ue.commodity.dao.TomCommodityMapper;
import com.chinamobo.ue.commodity.entity.TomCommodity;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomQuestionBankMapper;
import com.chinamobo.ue.exam.dao.TomQuestionClassificationMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomQuestionBank;
import com.chinamobo.ue.exam.entity.TomQuestionClassification;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.PathUtil;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 页面跳转
 *
 * 作者: JCX
 * 创建时间: 2016年3月1日 下午4:14:22
 */
@Path("/page") 
@Scope("request") 
@Component
public class PageRest {
	
	@Autowired
	private TomCourseClassifyMapper courseClassfyMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private TomQuestionBankMapper questionBanMapper;
	@Autowired
	private TomQuestionClassificationMapper questionClassificationMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomTaskPackageMapper taskPackageMapper;
	@Autowired
	private TomActivityMapper activityMapper;
	@Autowired
	private TomCommodityMapper commodityMapper;
	
	/**
	 * 
	 * 功能描述：[页面跳转]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月2日 上午11:20:28
	 * @param pageName
	 * @param response
	 * @return
	 */
	@GET
	@Path("/{path}/{pageName}")
	public String toPage(@QueryParam("id") String id,@PathParam("pageName") String pageName,@PathParam("path") String path, @Context HttpServletResponse response, @Context HttpServletRequest request){
//		ShiroUser user=ShiroUtils.getCurrentUser();
//		if(id==null|| user.getId()==0){//非编辑页面及超级管理员
//			return "true";
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		ShiroUser user=ShiroUtils.getCurrentUser();
		String superCode=Config.getProperty("superCode");
		if(id==null|| superCode.equals(user.getCode())){//非编辑页面及超级管理员
			return "true";

		}else{//编辑页面校验管理权限
			if("editCourseClassify".equals(pageName)){
				TomCourseClassify clf=courseClassfyMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(clf.getCreatorId().equals(user.getCode())){
					return "true";
				}else{
					return this.admin(clf.getAdmins(), user.getId());
				}
			}else if("editCourse".equals(pageName)){
				TomCourses course=coursesMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(course.getCreatorId().equals(user.getCode())){
					return "true";
				}else{
					return admin(course.getAdmins(),user.getId());
				}
			}else if("editExamPaper".equals(pageName)){
				TomExamPaper examPaper=examPaperMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(examPaper.getCreatorId().equals(user.getCode())){
					return "true";
				}else {
					return admin(examPaper.getAdmins(),user.getId());
				}
			}else if("editQuestionBank".equals(pageName)|| "superadditionQuestionBank".equals(pageName)){
				TomQuestionBank questionBank=questionBanMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(questionBank.getCreatorId().equals(user.getCode())){
					return "true";
				}else {
					return admin(questionBank.getAdmins(),user.getId());
				}
			}else if("editQuestionBankClassify".equals(pageName)){
				TomQuestionClassification qc=questionClassificationMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(qc.getCreatorId().equals(user.getCode())){
					return "true";
				}else{
					return admin(qc.getAdmins(),user.getId());
				}
			}else if("editExam".equals(pageName)){
				TomExam exam=examMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(exam.getCreatorId().equals(user.getCode())){
					return "true";
				}else {
					return admin(exam.getAdmins(),user.getId());
				}
			}else if("editTaskPackage".equals(pageName)){
				TomTaskPackage taskPackage=taskPackageMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(taskPackage.getCreatorId().equals(user.getCode())){
					return "true";
				}else {
					return admin(taskPackage.getAdmins(),user.getId());
				}
			}else if("editActivity".equals(pageName)){
				TomActivity activity=activityMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(activity.getCreatorId().equals(user.getCode())){
					return "true";
				}else {
					return admin(activity.getAdmins(),user.getId());
				}
			}else if("editCommodity".equals(pageName)){
				TomCommodity commodity=commodityMapper.selectByPrimaryKey(Integer.valueOf(id));
				if(commodity.getCreatorId().equals(user.getCode())){
					return "true";
				}else {
					return admin(commodity.getAdmins(),user.getId());
				}
			}
		}
		return "true";
	}
	/**
	 * 
	 * 功能描述：[是否包含adminId]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月19日 下午2:38:32
	 * @param adminIds
	 * @param userId
	 * @return
	 */
	private String admin(String adminIds,Integer userId){
		if(adminIds!=null){
			String admins[]=adminIds.split(",");
			for(String adminId:admins){
				if(adminId!=null&&!"".equals(adminId)&&Integer.valueOf(adminId)==userId){
					return "true";
				}
			}
		}
		return "false";
	}
	@GET
	@Path("/{pageName}")
	public void toPage2(@PathParam("pageName") String pageName,@PathParam("path") String path, @Context HttpServletResponse response, @Context HttpServletRequest request){
//		System.out.println(PathUtil.VIEW_LOCAL_PATH+"/"+pageName);
		RequestDispatcher dispatcher = request.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH+"/"+pageName);
		try {
			dispatcher .forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
