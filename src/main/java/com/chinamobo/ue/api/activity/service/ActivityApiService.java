package com.chinamobo.ue.api.activity.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.chinamobo.ue.activity.dao.TomActivityDeptMapper;
import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityOtherRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomCertificateManageMapper;
import com.chinamobo.ue.activity.dto.Myactivity;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityDept;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelationKey;
import com.chinamobo.ue.activity.entity.TomActivityOtherRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.api.activity.dto.ActResults;
import com.chinamobo.ue.api.activity.dto.MyCertificateDto;
import com.chinamobo.ue.api.activity.dto.TaskData;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.result.Resultc;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamEmpAllocationMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamEmpAllocation;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.service.ExamScoreService;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelEmpRelationMapper;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomLabelEmpRelation;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.system.service.SendMessageService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.mysql.fabric.xmlrpc.base.Fault;

import net.sf.json.JSONObject;

/**
 * 版本: [1.0] 功能说明: 培训活动管理
 *
 * 作者: WangLg 创建时间: 2016年3月9日 下午5:19:50
 */
@Service
public class ActivityApiService {// extends BaseService

	private static Logger logger = LoggerFactory.getLogger(ActivityApiService.class);
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private SendMessageService sendMessageService;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomActivityMapper activityMapper;
	// @Autowired
	// private TomActivityFeesMapper activityFeesMapper;//培训活动费用统计
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;// 培训活动配置
	// //private TomActivityExapInfoMapper activityExapInfoMapper;//培训活动补考信息
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;// 培训活动人员
	@Autowired
	private TomActivityDeptMapper activityDeptMapper;// 培训活动推送部门负责人
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomExamEmpAllocationMapper examEmpAllocationMapper;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	@Autowired
	private ExamScoreService examScoreService;
	@Autowired
	private TomActivityOtherRelationMapper tomActivityOtherRelationMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomLabelEmpRelationMapper tomLabelEmpRelationMapper;
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	@Autowired
	private TomCertificateManageMapper tomCertificateManageMapper;

	ObjectMapper mapper = new ObjectMapper();
	
//	ShiroUser user=ShiroUtils.getCurrentUser();

	/**
	 * 功能描述：[查询培训活动列表] ELE_TRAIN_ACTIVITIES_DETAILS 创建者：WangLg 创建时间: 2016年3月10日
	 * 上午10:00:03
	 * 
	 * @param example
	 * @return
	 */
	public Result eleTrainActivitiesList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> queryMap = Maps.newHashMap();
		PageData<ActResults> page = new PageData<ActResults>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String code = request.getParameter("userId");
		logger.info("eleTrainActivitiesList-------" + code);
		if (null == code || "".equals(code)) {
			return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
		}
		queryMap.put("code", code);
		if (request.getParameter("firstIndex") == null) {
			queryMap.put("startNum", 0);
		} else {
			queryMap.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			queryMap.put("endNum", 10);
			page.setPageSize(10);
		} else {
			queryMap.put("endNum",Integer.parseInt(request.getParameter("pageSize")));// Integer.parseInt(request.getParameter("firstIndex"))+ 
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		int count = activityMapper.countByUserList(queryMap);
		List<ActResults> queryList = activityMapper.selectByUserList(queryMap);

		// 判断活动状态
		Date nowDate = new Date();
		for (ActResults actResults : queryList) {
			if (actResults.getNeedApply().equals("Y")) {//选修活动
				if (actResults.getApplicationStartTime().after(nowDate)) {
					actResults.setApplyStatus("1");//报名时间未开始
				} else if (actResults.getApplicationStartTime().before(nowDate)
						&& actResults.getApplicationDeadline().after(nowDate)) {
					if (actResults.getApplyStatus().equals("N")) {
						actResults.setApplyStatus("2");//报名中，未报名
					} else {
						actResults.setApplyStatus("3");//报名中，已报名
					}
				} else if (actResults.getApplyStatus().equals("Y")
						&& actResults.getApplicationDeadline().before(nowDate)
						&& actResults.getActivityStartTime().after(nowDate)) {
					actResults.setApplyStatus("4");//报名时间结束，活动未开始，已报名
				} else if (actResults.getApplyStatus().equals("Y")
						&& actResults.getActivityStartTime().before(nowDate)) {
					actResults.setApplyStatus("5");//活动已开始，已报名
				}
				actResults.setApplicationStartTimeS(format.format(actResults.getApplicationStartTime()));
				actResults.setApplicationDeadlineS(format.format(actResults.getApplicationDeadline()));
			} else {//必修活动
				TomActivityDept example = new TomActivityDept();
				example.setActivityId(actResults.getActivityId());
				example.setHeaderCode(code);
				TomActivityDept activityDept = activityDeptMapper.selectByExample(example);
				if (activityDept != null && activityDept.getPushStatus().equals("N")
						&& actResults.getActivityStartTime().after(nowDate)) {
					actResults.setApplyStatus("6");//活动未推送，活动未开始
				} else {
					if (actResults.getActivityStartTime().after(nowDate)) {
						actResults.setApplyStatus("4");
					} else if (actResults.getActivityStartTime().before(nowDate)) {
						actResults.setApplyStatus("5");
					}
				}

			}

			actResults.setActivityStartTimeS(format.format(actResults.getActivityStartTime()));
			actResults.setActivityEndTimeS(format.format(actResults.getActivityEndTime()));
		}

		page.setCount(count);// TODO
		page.setDate(queryList);
		page.setPageNum((int) queryMap.get("startNum") / (int) queryMap.get("endNum") + 1);
		String json = mapper.writeValueAsString(page);
		json = json.replaceAll(":null", ":\"\"");
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 功能描述：[学员活动详情] ELE_TRAIN_ACTIVITIES_LIST
	 * 
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午10:00:07
	 * 
	 * @param example
	 * @return
	 */
	public Result eleTrainActivitiesDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> queryMap = Maps.newHashMap();
		ActResults actresult = new ActResults();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date nowDate = new Date();
		boolean confer = false;
		String code = request.getParameter("userId");
		examScoreService.reSetStatus(code);
		if (null == code || "".equals(code)) {
			return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
		}
		String activityId = request.getParameter("activityId");
		if (null == activityId || "".equals(activityId)) {
			return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
		}
		queryMap.put("activityId", Integer.parseInt(activityId));
		queryMap.put("code", code);
		actresult = activityMapper.selectByUserDetail(queryMap);
//		if(actresult == null){
//			return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "您没有安排该活动！You do not have to arrange the activity!");
//		}
		if(actresult == null){
			//查询活动是否与其它关联
			Map<Object,Object> mapOther = new HashMap<Object,Object>();
			mapOther.put("activityId", activityId);
			List<TomActivityOtherRelation> activityOtherRelationList = tomActivityOtherRelationMapper.selectByList(mapOther);
			if(activityOtherRelationList.size()>0){
				int i = 0;
				int j = 0;
				int r = 0;
				//查询人员信息
				TomEmp emp = tomEmpMapper.selectByPrimaryKey(code);
				for(TomActivityOtherRelation activityOtherRelation : activityOtherRelationList){
					if(activityOtherRelation.getType().equals("D")){
						if(!activityOtherRelation.getCode().equals("1")){
							if(emp.getDeptcode().equals(activityOtherRelation.getCode())){
								i+=1;
							}
						}
					}else if(activityOtherRelation.getType().equals("L")){
						Map<Object,Object> mapTagIdCode = new HashMap<Object,Object>();
						mapTagIdCode.put("tagId", activityOtherRelation.getCode());
						mapTagIdCode.put("code", code);
						//根据标签ID与人员CODE查询标签人员关联表
						TomLabelEmpRelation labelEmpRelation = tomLabelEmpRelationMapper.selectByTagIdCode(mapTagIdCode);
						if(labelEmpRelation!=null){
							j+=1;
						}
					}else if(activityOtherRelation.getType().equals("C")){
						List<TomLabelEmpRelation> selectLabelClassEmpList = tomLabelClassRelationMapper.selectLabelClassEmpList(Integer.valueOf(activityOtherRelation.getCode()));
						if(selectLabelClassEmpList.size()>0){
							for(TomLabelEmpRelation labelEmpRelation : selectLabelClassEmpList){
								if(code.equals(labelEmpRelation.getCode())){
									r+=1;
								}
							}
						}
					}
				}
				if(actresult == null&&i==0&&j==0&&r==0){
					return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "您没有安排该活动！You do not have to arrange the activity!");
				}
			}else{
				return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "您没有安排该活动！You do not have to arrange the activity!");
			}
		}
		actresult.setActivityStartTimeS(format.format(actresult.getActivityStartTime()));
		actresult.setActivityEndTimeS(format.format(actresult.getActivityEndTime()));
		if (actresult.getNeedApply().equals("Y")) {
			actresult.setApplicationStartTimeS(format.format(actresult.getApplicationStartTime()));
			actresult.setApplicationDeadlineS(format.format(actresult.getApplicationDeadline()));
		}
		TomActivityEmpsRelation empsExample = new TomActivityEmpsRelation();
		empsExample.setActivityId(Integer.parseInt(activityId));
		empsExample.setApplyStatus("Y");
		if (actresult.getNeedApply().equals("Y")) {
			if (actresult.getApplyStatus().equals("N") && activityEmpsRelationMapper.countByExample(empsExample) < actresult.getNumberOfParticipants()) {
				actresult.setAllow("Y");
			} else if (actresult.getApplyStatus().equals("Y")) {
				actresult.setAllow("Y");
			}
		} else {
			actresult.setAllow("N");
		}

		TomActivityProperty example = new TomActivityProperty();
		example.setActivityId(Integer.parseInt(activityId));
		List<TomActivityProperty> activityPropertys = activityPropertyMapper.selectByExample(example);
		List<TaskData> taskDatas = new ArrayList<TaskData>();
		String underwaySorts = "";
		for (TomActivityProperty activityProperty : activityPropertys) {
			TaskData taskData = new TaskData();
			taskData.setSort(activityProperty.getSort());
			if (activityProperty.getCourseId() == null || "".equals(activityProperty.getCourseId())) {
				// 考试
				TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
				TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
				TomRetakingExam retakingExamExample = new TomRetakingExam();
				retakingExamExample.setExamId(activityProperty.getExamId());
				List<TomRetakingExam> retakingExams = retakingExamMapper.selectListByExample(retakingExamExample);

				TomExamScore scoreExample = new TomExamScore();
				scoreExample.setExamId(activityProperty.getExamId());
				scoreExample.setCode(code);

				taskData.setId(activityProperty.getExamId());
				taskData.setName(activityProperty.getExamName());
				taskData.setCourseExamImg(examPaper.getExamPaperPicture());
				taskData.setAddress(exam.getExamAddress());
				taskData.setStartTime(retakingExams.get(0).getRetakingExamBeginTime());
				taskData.setEndTime(retakingExams.get(retakingExams.size() - 1).getRetakingExamEndTime());
				taskData.setStartTimeS(format.format(taskData.getStartTime()));
				taskData.setEndTimeS(format.format(taskData.getEndTime()));
				taskData.setOnline(activityProperty.getOfflineExam());
				taskData.setType("exam");

				if (actresult.getApplyStatus().equals("Y")) {
					TomExamScore examScore = examScoreMapper.selectOneLast(scoreExample);
					if (taskData.getStartTime().after(new Date())) {
						taskData.setTaskProgress("W");
					} else if (examScore.getGradeState().equals("Y")) {
						taskData.setTaskProgress("Y");
					} else if (examScore.getGradeState().equals("P")) {
						taskData.setTaskProgress("P");
					} else if (examScore.getGradeState().equals("N")
							&& examScoreService.getRemainingCount(code, activityProperty.getExamId()) <= 0) {
						taskData.setTaskProgress("N");
					} else if(examScore.getGradeState().equals("D")&&taskData.getEndTime().before(new Date())&&taskData.getOnline().equals(2)){
						taskData.setTaskProgress("P");
					} else {
						taskData.setTaskProgress("U");
					}
				} else {

				}
			} else {
				TomCourses course = coursesMapper.selectByPrimaryKey(activityProperty.getCourseId());
				taskData.setId(activityProperty.getCourseId());
				taskData.setName(course.getCourseName());
				taskData.setCourseExamImg(course.getCourseImg());
				taskData.setStartTime(activityProperty.getStartTime());
				taskData.setEndTime(activityProperty.getEndTime());
				taskData.setStartTimeS(format.format(taskData.getStartTime()));
				taskData.setEndTimeS(format.format(taskData.getEndTime()));
				taskData.setOnline(course.getCourseOnline());
				taskData.setType("course");

				TomCourseLearningRecord example1 = new TomCourseLearningRecord();
				example1.setCode(code);
				example1.setCourseId(activityProperty.getCourseId());
				example1.setLearningDate(taskData.getEndTime());
				TomCourseSignInRecords example2 = new TomCourseSignInRecords();
				example2.setCode(code);
				example2.setCourseId(activityProperty.getCourseId());
				example2.setCreateDate(taskData.getEndTime());

				if(taskData.getOnline().equals("Y")&&courseLearningRecordMapper.countByExample(example1)>=1){

					taskData.setTaskProgress("Y");

				}else if(taskData.getOnline().equals("N")&&courseSignInRecordsMapper.countByExample(example2)>=1){

					taskData.setTaskProgress("Y");
				} else {
					taskData.setTaskProgress("N");
				}
			}

			boolean flag = true;
			String preTask;
			if (activityProperty.getPreTask() != null && !"".equals(activityProperty.getPreTask())) {
				String preTasks[] = activityProperty.getPreTask().split(",");
				preTask = "";
				for (String s : preTasks) {
					if (preTask.equals("")) {
						preTask = preTask + "任务" + s;
					} else {
						preTask = preTask + ",任务" + s;
					}

					if (taskDatas.get(Integer.parseInt(s) - 1).getTaskProgress().equals("N")) {
						flag = false;
					}
				}
				taskData.setPreTask(preTask);
			}
			if (flag && actresult.getApplyStatus().equals("Y") && taskData.getStartTime().before(nowDate)
					&& taskData.getEndTime().after(nowDate)) {
				taskData.setTaskStatus("Y");
				if (taskData.getTaskProgress().equals("N")) {
					underwaySorts += "," + taskData.getSort();
				}
			} else if (flag && (taskData.getOnline().equals("N") || taskData.getOnline().equals("2"))
					&& taskData.getEndTime().after(nowDate)) {
				taskData.setTaskStatus("Y");
				if (taskData.getTaskProgress().equals("N")) {
					underwaySorts += "," + taskData.getSort();
				}
			} else if ("Y".equals(taskData.getTaskProgress())) {
				taskData.setTaskStatus("Y");
			} else {
				taskData.setTaskStatus("N");
			}

			taskDatas.add(taskData);
		}
		if (!"".equals(underwaySorts)) {
			actresult.setUnderwaySort(underwaySorts.substring(1));
		}
		actresult.setTaskData(taskDatas);
		String json = mapper.writeValueAsString(actresult);
		json = json.replaceAll(":null", ":\"\"");
		//判断证书状态
		TomActivity activity = activityMapper.selectByPrimaryKey(Integer.parseInt(activityId));
		if ("Y".equals(activity.getCertificateState())) {
			Map<Object,Object> map = new HashMap<>();
			map.put("code", code);
			map.put("activityId", Integer.parseInt(activityId));
			int tomCertificate = tomCertificateManageMapper.countByCodeAct(map);
			if (tomCertificate > 0) {
				confer = true;
			}
		}
		return new Resultc("Y", json, ErrorCode.SUCCESS_CODE, "",confer,"");
	}

	/**
	 * 功能描述：[培训活动个人报名] ELE_TRAIN_ACTIVITIES_SIGNUP 创建者：Lg 创建时间: 2016年4月28日
	 * 
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Result eleTrainActivitiesSignup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonMapper jsonMapper = new JsonMapper();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date();
		String code = request.getParameter("userId");
		String activityId = request.getParameter("activityId");
		Map<Object, Object> queryMap = Maps.newHashMap();
		queryMap.put("code", code);
		queryMap.put("activityId", activityId);
		if (null == code || "".equals(code) || null == activityId || "".equals(activityId)) {
			return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
		}
		TomActivity activity = activityMapper.selectByPrimaryKey(Integer.parseInt(activityId));
		TomActivityEmpsRelation empsExample = new TomActivityEmpsRelation();
		empsExample.setActivityId(Integer.parseInt(activityId));
		empsExample.setApplyStatus("Y");
		TomActivityEmpsRelation example = new TomActivityEmpsRelation();
		example.setActivityId(Integer.parseInt(activityId));
		example.setCode(code);
		TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper.selectByPrimaryKey(example);
		if(activityEmpsRelation!=null){
			if (activity.getNeedApply().equals("Y")) {
				if (activity.getApplicationStartTime().after(nowDate) || activity.getApplicationDeadline().before(nowDate)) {
					return new Result("N", "", ErrorCode.API_OVERLOAD, "不在报名时间内。");
				} else if (activityEmpsRelation.getApplyStatus().equals("N") && activityEmpsRelationMapper.countByExample(empsExample) >= activity.getNumberOfParticipants()) {
					return new Result("N", "", ErrorCode.API_OVERLOAD, "人数已满。");
				}
			} else {
				return new Result("N", "", ErrorCode.API_OVERLOAD, "无需报名。");
			}
			if (activityEmpsRelation.getApplyStatus().equals("Y")) {
				activityEmpsRelation.setApplyStatus("N");
			} else {
				activityEmpsRelation.setApplyStatus("Y");
			}
			activityEmpsRelation.setUpdateTime(new Date());
			activityEmpsRelationMapper.updateByPrimaryKeySelective(activityEmpsRelation);
		}else{
			if (activity.getNeedApply().equals("Y")) {
				if (activity.getApplicationStartTime().after(nowDate) || activity.getApplicationDeadline().before(nowDate)) {
					return new Result("N", "", ErrorCode.API_OVERLOAD, "不在报名时间内。");
				} else if (activityEmpsRelation.getApplyStatus().equals("N") && activityEmpsRelationMapper.countByExample(empsExample) >= activity.getNumberOfParticipants()) {
					return new Result("N", "", ErrorCode.API_OVERLOAD, "人数已满。");
				}
			} else {
				return new Result("N", "", ErrorCode.API_OVERLOAD, "无需报名。");
			}
			activityEmpsRelation.setActivityId(Integer.valueOf(activityId));
			activityEmpsRelation.setCode(code);
			activityEmpsRelation.setStatus("Y");
			activityEmpsRelation.setApplyStatus("Y");
			activityEmpsRelation.setApplyType("E");
			if(activity.getNeedApply().equals("Y")){
				activityEmpsRelation.setIsRequired("N");
			}else{
				activityEmpsRelation.setIsRequired("Y");
			}
			activityEmpsRelation.setCreateTime(new Date());
			activityEmpsRelation.setUpdateTime(new Date());
			activityEmpsRelationMapper.insertSelective(activityEmpsRelation);
		}
		
		// 更改人员关联
		TomActivityProperty activityPropertyExample = new TomActivityProperty();
		activityPropertyExample.setActivityId(Integer.parseInt(activityId));
		List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
		TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
		for (TomActivityProperty activityProperty : activityProperties) {
			if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
				if (activityEmpsRelation.getApplyStatus().equals("Y")) {
					// 添加考试员工关联
					TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
					TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
					TomExamScore examScore = new TomExamScore();
					examScore.setExamId(activityProperty.getExamId());
					examScore.setCode(code);
					examScore.setEmpName(empMapper.selectByPrimaryKey(code).getName());
					examScore.setGradeState("D");
					examScore.setExamCount(0);
					examScore.setTotalPoints(0);
					examScore.setExamTotalTime(0);
					examScore.setCreateTime(exam.getBeginTime());
					examScore.setRightNumbe(0);
					examScore.setWrongNumber(examPaper.getTestQuestionCount());
					examScore.setAccuracy(0d);
					Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
					mapExamCode.put("examId", activityProperty.getExamId());
					mapExamCode.put("code", code);
					if(examScoreMapper.selectByExamCode(mapExamCode)==null){
						examScoreMapper.insertSelective(examScore);
					}
					TomExamEmpAllocation examEmpAllocation = new TomExamEmpAllocation();
					examEmpAllocation.setCode(code);
					examEmpAllocation.setExamId(exam.getExamId());
					examEmpAllocation.setCreateTime(new Date());
					if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
						examEmpAllocationMapper.insertSelective(examEmpAllocation);
					}
					if("Y".equals(message.getSendToApp())){
						String examApp =" 亲爱的蔚来伙伴：您有一次考试" + "【" + activityProperty.getExamName() + "】" + "需要在【"
								+ format1.format(activityProperty.getStartTime()) + "】至【"
								+ format1.format(activityProperty.getEndTime()) + "】期间完成。"
						 		+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(activityProperty.getStartTime())+"]-["+ format1.format(activityProperty.getEndTime()) +"]. Please click the link to finish the test.         "+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+activityProperty.getExamId();
						List<String> listuser=new ArrayList<String>();
						listuser.add(code);
						sendMessageService.wxMessage(listuser, examApp);
					}
					if("Y".equals(message.getSendToEmail())){
						TomEmp emp=empMapper.selectByCodeOnetoOne(code);
						if(emp.getSecretEmail()!=null){
							List<String> listuser = new ArrayList<String>();
							listuser.add(emp.getSecretEmail());
							String examEmail =  "亲爱的蔚来伙伴：您有一次考试" + "【" + activityProperty.getExamName() + "】" + "需要在【"
									+ format1.format(activityProperty.getStartTime()) + "】至【"
									+ format1.format(activityProperty.getEndTime()) + "】期间完成。"
									+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(activityProperty.getStartTime())+"]-["+ format1.format(activityProperty.getEndTime()) +"]. Please click the link to finish the test.        <a href=\""+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+activityProperty.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+activityProperty.getExamId()+"</a>";
							SendMail sm2 = SendMailUtil.getMail(listuser, "【蔚乐学】任务通知", new Date(), "蔚乐学", examEmail);
							sm2.sendMessage();
						}
					}
				} else {
					TomExamScore scoreExample = new TomExamScore();
					scoreExample.setExamId(activityProperty.getExamId());
					scoreExample.setCode(code);
					examScoreMapper.deleteByExample(scoreExample);
					TomExamEmpAllocation empAllocationExample = new TomExamEmpAllocation();
					empAllocationExample.setExamId(activityProperty.getExamId());
					empAllocationExample.setCode(code);
					examEmpAllocationMapper.deleteByExample(empAllocationExample);
				}
			} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
				if (activityEmpsRelation.getApplyStatus().equals("Y")) {
					TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
					courseEmpRelation.setCode(code);
					courseEmpRelation.setCourseId(activityProperty.getCourseId());
					List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper.selectByExample(courseEmpRelation);
					if (courseEmpRelations.size() == 0) {
						courseEmpRelation.setCreateTime(activityProperty.getCreateTime());
						courseEmpRelation.setStatus("Y");
						courseEmpRelation.setUpdateTime(activityProperty.getCreateTime());
						courseEmpRelation.setFinishStatus("N");
						courseEmpRelation.setCreatorId(code);
						courseEmpRelationMapper.insertSelective(courseEmpRelation);
					} else {
						courseEmpRelation = courseEmpRelations.get(0);
						courseEmpRelation.setStatus("Y");
						courseEmpRelation.setCreatorId(code);
						courseEmpRelation.setCreateTime(activityProperty.getCreateTime());
						courseEmpRelation.setUpdateTime(activityProperty.getCreateTime());
						courseEmpRelationMapper.update(courseEmpRelation);
					}
					TomCourses tomCourse=coursesMapper.selectByPrimaryKey(activityProperty.getCourseId());
					if("Y".equals(message.getSendToApp())){
						List<String> listuser=new ArrayList<String>();
						listuser.add(code);
						String courseApp ="亲爱的蔚来伙伴：您已成功报名《"+ tomCourse.getCourseName() +"》课程。课程名称" + "【" + tomCourse.getCourseName() + "】"
								+ "您所报名的课程可以在任务中心找到，点击链接即可看到课程信息。"
								+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!     "+Config.getProperty("h5Index")+"views/user/my-course.html";
						sendMessageService.wxMessage(listuser, courseApp);
					}
					if("Y".equals(message.getSendToEmail())){
						TomEmp emp=empMapper.selectByCodeOnetoOne(code);
						if(emp.getSecretEmail()!=null){
							List<String> listuser = new ArrayList<String>();
							listuser.add(emp.getSecretEmail());
							String courseEmail="";
							if ("Y".equals(tomCourse.getCourseOnline())) {
							 courseEmail ="亲爱的蔚来伙伴：您有一门课程需要完成，课程名称" + "【" + tomCourse.getCourseName() + "】"
										+ "请点击链接前往课程，立即开始学习吧！"
										+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!      "+"<a href=\""+Config.getProperty("pcIndex")+"views/course/course_my.html\">"+Config.getProperty("h5Index")+"views/user/my-course.html"+"</a>";
							}else{
								courseEmail ="亲爱的蔚来伙伴: 您已成功报名《"+ tomCourse.getCourseName() +"》课程。课程名称："+ tomCourse.getCourseName()
								+"开课时间:"+ format1.format(activityProperty.getStartTime()) +"上课地点："+tomCourse.getCourseAddress()
								+"您所报名的课程可以在任务中心找到，点击链接即可看到课程信息。"
								+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Course time: "+format1.format(activityProperty.getStartTime())+". Location:"+ tomCourse.getCourseAddress() +".You can click the link to find out more information in the task center.      "+"<a href=\""+Config.getProperty("pcIndex")+"views/course/course_my.html\">"+Config.getProperty("pcIndex")+"views/course/course_my.html"+"</a>";
							}
							SendMail sm1 = SendMailUtil.getMail(listuser, "【蔚乐学】任务通知", new Date(), "蔚乐学", courseEmail);
							sm1.sendMessage();
						}
					}
				} else {
					TomCourseEmpRelation courseEmpRelationExample = new TomCourseEmpRelation();
					courseEmpRelationExample.setCourseId(activityProperty.getCourseId());
					courseEmpRelationExample.setCode(code);
					courseEmpRelationExample.setCreateTime(activityProperty.getCreateTime());
					courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
				}
			}
		}
		String json = jsonMapper.toJson(activityEmpsRelation.getApplyStatus());
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[培训活动负责人推送列表]
	 *
	 * 创建者：JCX 创建时间: 2016年5月29日 下午3:31:06
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleTrainActivitiesEmpList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		JsonMapper jsonMapper = new JsonMapper();
		String code = request.getParameter("userId");
		String activityId = request.getParameter("activityId");
		// String code = "4";
		// String activityId = "12";
		if (null == code || "".equals(code) || null == activityId || "".equals(activityId)) {
			return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
		}
		TomActivityDept activityDeptExample = new TomActivityDept();
		activityDeptExample.setActivityId(Integer.parseInt(activityId));
		activityDeptExample.setHeaderCode(code);
		TomActivityDept activityDept = activityDeptMapper.selectByExample(activityDeptExample);

		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, String>> empList = new ArrayList<Map<String, String>>();
		String codes[] = activityDept.getCode().split(",");
		for (String empId : codes) {
			Map<String, String> empMap = new HashMap<String, String>();
			TomEmp emp = empMapper.selectByPrimaryKey(empId);
			empMap.put("userId", empId);
			empMap.put("userName", emp.getName());
			empList.add(empMap);
		}
		data.put("activityId", activityId);
		data.put("data", empList);
		String json = jsonMapper.toJson(data);
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 功能描述：[培训活动负责人报名] ELE_TRAIN_ACTIVITIES_LEAD_SIGNUP 创建者：Lg 创建时间: 2016年3月22日
	 * 下午3:40:01
	 * 
	 * @param json
	 * @return
	 */
	@Transactional
	public Result eleTrainActivitiesLeadSignup(String json) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(json);
		String userId = jsonObject.getString("userId");
		String ids = jsonObject.getString("ids");
		int activityId = Integer.parseInt(jsonObject.getString("activityId"));
		TomActivityDept activityDept = new TomActivityDept();
		activityDept.setActivityId(activityId);
		activityDept.setHeaderCode(userId);
		activityDept.setPushStatus("Y");
		activityDeptMapper.updateByPrimaryKeySelective(activityDept);
		TomActivityEmpsRelationKey example = new TomActivityEmpsRelationKey();
		example.setActivityId(activityId);
		example.setCode(userId);
		activityEmpsRelationMapper.deleteByPrimaryKey(example);
		List<TomActivityProperty> courseForMessage = new ArrayList<TomActivityProperty>();
		List<TomActivityProperty> examForMessage = new ArrayList<TomActivityProperty>();
		TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
		Date date = new Date();
		TomExamScore examScore;
		TomExamEmpAllocation examEmpAllocation;
		TomCourseEmpRelation courseEmpRelation;
		if (null != ids) {
			String str[] = ids.split(",");
			for (int i = 0; i < str.length; i++) {
				// 封装人员关联
				tomActivityEmpsRelation.setActivityId(activityId);
				tomActivityEmpsRelation.setApplyStatus("Y");
				tomActivityEmpsRelation.setApplyType("D");
				tomActivityEmpsRelation.setCode(str[i]);
				tomActivityEmpsRelation.setCreateTime(date);
				tomActivityEmpsRelation.setCreator(userId);
				tomActivityEmpsRelation.setIsRequired("Y");
				tomActivityEmpsRelation.setOperator(userId);
				tomActivityEmpsRelation.setStatus("Y");
				tomActivityEmpsRelation.setUpdateTime(date);
				activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);

				TomActivityProperty activityPropertyExample = new TomActivityProperty();
				activityPropertyExample.setActivityId(activityId);
				List<TomActivityProperty> activityProperties = activityPropertyMapper
						.selectByExample(activityPropertyExample);
				for (TomActivityProperty activityProperty : activityProperties) {
					if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
						// 添加考试员工关联
						TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
						TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
						examScore = new TomExamScore();
						examScore.setExamId(activityProperty.getExamId());
						examScore.setCode(str[i]);
						examScore.setEmpName(empMapper.selectByPrimaryKey(str[i]).getName());
						examScore.setGradeState("D");
						examScore.setExamCount(0);
						examScore.setTotalPoints(0);
						examScore.setExamTotalTime(0);
						examScore.setCreateTime(exam.getBeginTime());
						examScore.setRightNumbe(0);
						examScore.setWrongNumber(examPaper.getTestQuestionCount());
						examScore.setAccuracy(0d);
						examScoreMapper.insertSelective(examScore);
						examEmpAllocation = new TomExamEmpAllocation();
						examEmpAllocation.setCode(str[i]);
						examEmpAllocation.setExamId(exam.getExamId());
						examEmpAllocation.setCreateTime(new Date());
						examEmpAllocationMapper.insertSelective(examEmpAllocation);
						courseForMessage.add(activityProperty);
					} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
						courseEmpRelation = new TomCourseEmpRelation();
						courseEmpRelation.setCode(str[i]);
						courseEmpRelation.setCourseId(activityProperty.getCourseId());
						List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper
								.selectByExample(courseEmpRelation);
						if (courseEmpRelations.size() == 0) {
							courseEmpRelation.setCreateTime(activityProperty.getCreateTime());
							courseEmpRelation.setStatus("Y");
							courseEmpRelation.setUpdateTime(activityProperty.getCreateTime());
							courseEmpRelation.setFinishStatus("N");
							courseEmpRelation.setCreatorId(userId);
							courseEmpRelationMapper.insertSelective(courseEmpRelation);
						} else {
							courseEmpRelation = courseEmpRelations.get(0);
							courseEmpRelation.setCreateTime(activityProperty.getCreateTime());
							courseEmpRelation.setStatus("Y");
							courseEmpRelation.setCreatorId(userId);
							courseEmpRelation.setUpdateTime(activityProperty.getCreateTime());
							courseEmpRelationMapper.update(courseEmpRelation);
						}
						examForMessage.add(activityProperty);
					}
				}
			}
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TomActivity activity = activityMapper.selectByPrimaryKey(activityId);
		TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
		String activityApp =  "亲爱的蔚来伙伴：" +  activity.getActivityName()  
		+ "已为您开放，诚邀您的参加! 请点击链接查看活动详情，并进行报名。"
		+"席位有限，快点击注册吧！Dear NextEVer,"+ activity.getActivityName() +"  course is now open for you. We sincerely invite you to join the course. Please click the link for further detials and registrate the course immediately!                "+Config.getProperty("h5Index")+"views/task/activity_detail.html?activityId="+activityId;
		String activityPhone = "亲爱的蔚来伙伴：" + activity.getActivityName() 
		+ "已为您开放，诚邀您的参加! 请点击链接查看活动详情，并进行报名。"
		+"席位有限，快点击注册吧！Dear NextEVer,"+  activity.getActivityName()  +"  course is now open for you. We sincerely invite you to join the course. Please click the link for further detials and registrate the course immediately!               "+Config.getProperty("h5Index")+"views/task/activity_detail.html?activityId="+activityId;
		String activityEmail ="亲爱的蔚来伙伴：" + activity.getActivityName() 
		+ "已为您开放，诚邀您的参加! 请点击链接查看活动详情，并进行报名。"
		+"席位有限，快点击注册吧！Dear NextEVer,"+ activity.getActivityName() +"  course is now open for you. We sincerely invite you to join the course. Please click the link for further detials and registrate the course immediately!                   <a href=\""+Config.getProperty("pcIndex")+"views/task_center/train_actcon.html?activityId="+activityId+"\">"+Config.getProperty("pcIndex")+"views/task_center/train_actcon.html?activityId="+activityId+"</a>";
		String courseApp = "";
		String courseEmail = "";
		String coursePhone = "";

		// String examPhone = " 【蔚乐学】：您有一次培训活动" + "【" +
		// activity.getActivityName()+ "】"
		// + "需要完成。";

		List<String> list2 = new ArrayList<String>();
		List<String> listForEmail = new ArrayList<String>();
		if (null != ids) {
			String str[] = ids.split(",");
			for (int i = 0; i < str.length; i++) {
				list2.add(str[i]);
			}
		}
		if ("Y".equals(message.getSendToApp())) {
			for (TomActivityProperty tomActivityProperty : courseForMessage) {
				TomCourses tomCourse = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
				if ("Y".equals(tomCourse.getCourseOnline())) {
					courseApp =  "亲爱的蔚来伙伴：您有一门课程需要完成，课程名称" + "【" + tomCourse.getCourseName() + "】"
							+ "请点击链接前往课程，立即开始学习吧！"
							+"&activityId="+tomActivityProperty.getActivityId()+";"+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!            "+Config.getProperty("h5Index")+"views/user/my-course.html";
				} else {
					courseApp =  "亲爱的蔚来伙伴,您已成功报名《"+ tomCourse.getCourseName() +"》课程。课程名称："+ tomCourse.getCourseName()  + "。课程名称："+ tomCourse.getCourseName()
					+"上课时间:"+ format1.format(tomActivityProperty.getStartTime()) +"上课地点："+tomCourse.getCourseAddress()
					+"您所报名的课程可以在任务中心找到，点击链接即可看到课程信息。"
					+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Course time: xxx. Location:"+ format1.format(tomActivityProperty.getStartTime()) +".You can click the link to find out more information in the task center.            "+Config.getProperty("h5Index")+"views/user/my-course.html";
				}
				sendMessageService.wxMessage(list2, courseApp);
			}
			for (TomActivityProperty tomActivityProperty : examForMessage) {
				String examApp = "亲爱的蔚来伙伴：您有一次考试" + "【" + tomActivityProperty.getExamName() + "】" + "需要在【"
						+format1.format(tomActivityProperty.getStartTime()) + "】至【" + format1.format( tomActivityProperty.getEndTime())
						+ "】期间完成。请点击链接参加。"
						+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(tomActivityProperty.getStartTime()) +"]-["+ format1.format( tomActivityProperty.getEndTime()) +"]. Please click the link to finish the test.              "+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomActivityProperty.getExamId();
				sendMessageService.itsm(list2, examApp);
			}
			sendMessageService.wxMessage(list2, activityApp);
		}
		if ("Y".equals(message.getSendToEmail())) {
			for (String code : list2) {
				TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
				if(selectByPrimaryKey.getSecretEmail()!=null)
				listForEmail.add(selectByPrimaryKey.getSecretEmail());
			}
			for (TomActivityProperty tomActivityProperty : courseForMessage) {
				TomCourses tomCourse = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
				if ("Y".equals(tomCourse.getCourseOnline())) {
					courseEmail =  "亲爱的蔚来伙伴：您有一门课程" + "【" + tomCourse.getCourseName() + "】"
							+ "需要完成。请点击链接前往课程，立即开始学习吧！"
							+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!      "+Config.getProperty("pIndex")+"views/task/course_detail.html?courseId="+tomActivityProperty.getCourseId()
							+"&activityId="+tomActivityProperty.getActivityId()+"\">"+Config.getProperty("pIndex")+"views/task/course_detail.html?courseId="+tomActivityProperty.getCourseId()
							+"&activityId="+tomActivityProperty.getActivityId()+"</a>";
				} else {
					courseEmail = "亲爱的蔚来伙伴, 您已成功报名《"+ tomCourse.getCourseName() +"》课程。课程名称："+ tomCourse.getCourseName()
					+"开课时间:"+ format1.format(tomActivityProperty.getStartTime()) +"上课地点："+tomCourse.getCourseAddress()
					+"您所报名的课程可以在任务中心找到，点击链接即可看到课程信息。"
					+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Course time: "+format1.format(tomActivityProperty.getStartTime())+". Location:"+ tomCourse.getCourseAddress() +".You can click the link to find out more information in the task center.      "+"<a href=\""+Config.getProperty("pcIndex")+"views/course/course_my.html\">"+Config.getProperty("pcIndex")+"views/course/course_my.html"+"</a>";
				}
				SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", courseEmail);
				sm.sendMessage();
			}
			for (TomActivityProperty tomActivityProperty : examForMessage) {
				String examEmail = "亲爱的蔚来伙伴：您有一次考试" + "【" + tomActivityProperty.getExamName() + "】" + "需要在【"
						+ format1.format(tomActivityProperty.getStartTime()) + "】至【"
						+ format1.format(tomActivityProperty.getEndTime()) + "】期间完成。请点击链接参加。"
						+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(tomActivityProperty.getStartTime())+"]-["+ format1.format(tomActivityProperty.getEndTime()) +"]. Please click the link to finish the test.      <a href"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomActivityProperty.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomActivityProperty.getExamId()+"</a>";
				SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "ele", examEmail);
				sm.sendMessage();
			}
			SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", activityEmail);
			sm.sendMessage();
		}
		if ("Y".equals(message.getSendToPhone())) {
			for (String code : list2) {
				TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
				for (TomActivityProperty tomActivityProperty : courseForMessage) {
					TomCourses tomCourse = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
					if ("Y".equals(tomCourse.getCourseOnline())) {
						courseApp =  "亲爱的蔚来伙伴：您有一门课程需要完成，课程名称" + "【" + tomCourse.getCourseName() + "】"
								+ "请点击链接前往课程，立即开始学习吧！"
								+"&activityId="+tomActivityProperty.getActivityId()+";"+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!      "+Config.getProperty("h5Index")+"views/user/my-course.html";
					} else {
						courseApp = "亲爱的蔚来伙伴, 您已成功报名《"+ tomCourse.getCourseName() +"》课程。课程名称："+ tomCourse.getCourseName()  + "。课程名称："+ tomCourse.getCourseName()
						+"上课时间:"+ format1.format(tomActivityProperty.getStartTime()) +"上课地点："+tomCourse.getCourseAddress()
						+"您所报名的课程可以在任务中心找到，点击链接即可看到课程信息。"
						+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Course time: xxx. Location:"+ format1.format(tomActivityProperty.getStartTime()) +".You can click the link to find out more information in the task center."+Config.getProperty("h5Index")+"views/user/my-course.html";
					}
					sendMessageService.sendMessage(courseApp, selectByPrimaryKey.getMobile());
				}
				for (TomActivityProperty tomActivityProperty : examForMessage) {
					String examApp =  "亲爱的蔚来伙伴：您有一次考试" + "【" + tomActivityProperty.getExamName() + "】" + "需要在【"
							+format1.format(tomActivityProperty.getStartTime()) + "】至【" + format1.format( tomActivityProperty.getEndTime())
							+ "】期间完成。请点击链接参加。"
							+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(tomActivityProperty.getStartTime()) +"]-["+ format1.format( tomActivityProperty.getEndTime()) +"]. Please click the link to finish the test."+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomActivityProperty.getExamId();
					sendMessageService.sendMessage(examApp, selectByPrimaryKey.getMobile());
				}
				sendMessageService.sendMessage(activityPhone, selectByPrimaryKey.getMobile());
			}

		}
		TomMessages activityMessage = new TomMessages();
		activityMessage.setMessageTitle( activity.getActivityName());
		activityMessage.setMessageDetails(activityApp);
		activityMessage.setEmpIds(list2);
		activityMessage.setMessageType("0");
		activityMessage.setCreateTime(date);
		activityMessage.setViewCount(0);
		if ("Y".equals(message.getSendToApp())) {
			activityMessage.setSendToApp("Y");
		} else {
			activityMessage.setSendToApp("N");
		}
		if ("Y".equals(message.getSendToEmail())) {
			activityMessage.setSendToEmail("Y");
		} else {
			activityMessage.setSendToEmail("N");
		}
		if ("Y".equals(message.getSendToPhone())) {
			activityMessage.setSendToPhone("Y");
		} else {
			activityMessage.setSendToPhone("N");
		}
		tomMessagesMapper.insertSelective(activityMessage);
		for (String code : list2) {
			TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
			tomMessagesEmployees.setCreateTime(date);
			tomMessagesEmployees.setEmpCode(code);
			tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
			tomMessagesEmployees.setIsView("N");
			tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
		}
		for (TomActivityProperty tomActivityProperty : courseForMessage) {
			TomCourses tomCourse = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
			activityMessage.setMessageTitle(tomCourse.getCourseName());
			courseApp =  "亲爱的蔚来伙伴：您有一门课程需要完成，课程名称" + "【" + tomCourse.getCourseName() + "】"
					+ "请点击链接前往课程，立即开始学习吧！"
					+"&activityId="+tomActivityProperty.getActivityId()+";"+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!      "+Config.getProperty("h5Index")+"views/user/my-course.html";
			activityMessage.setMessageDetails(courseApp);
			tomMessagesMapper.insertSelective(activityMessage);
			for (String code : list2) {
				TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
				tomMessagesEmployees.setCreateTime(date);
				tomMessagesEmployees.setEmpCode(code);
				tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
				tomMessagesEmployees.setIsView("N");
				tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
			}
		}
		for (TomActivityProperty tomActivityProperty : examForMessage) {
			activityMessage.setMessageTitle(tomActivityProperty.getExamName());
			String examApp =  "亲爱的蔚来伙伴：您有一次考试" + "【" + tomActivityProperty.getExamName() + "】" + "需要在【"
					+format1.format(tomActivityProperty.getStartTime()) + "】至【" + format1.format( tomActivityProperty.getEndTime())
					+ "】期间完成。请点击链接参加。"
					+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(tomActivityProperty.getStartTime()) +"]-["+ format1.format( tomActivityProperty.getEndTime()) +"]. Please click the link to finish the test.      "+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomActivityProperty.getExamId();
			activityMessage.setMessageDetails(examApp);
			tomMessagesMapper.insertSelective(activityMessage);
			for (String code : list2) {
				TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
				tomMessagesEmployees.setCreateTime(date);
				tomMessagesEmployees.setEmpCode(code);
				tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
				tomMessagesEmployees.setIsView("N");
				tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
			}
		}

		return new Result("Y", "", ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[负责人活动报名结果(分配成功)] ELE_TRAIN_ACTIVITIES_RESULT 创建者：Lg 创建时间:
	 * 2016年4月18日 下午1：30
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleTrainActivitiesResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		JsonMapper jsonMapper = new JsonMapper();
		String code = request.getParameter("userId");
		String activityId = request.getParameter("activityId");
		// String code = "4";
		// String activityId = "12";
		if (null == code || "".equals(code) || null == activityId || "".equals(activityId)) {
			return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
		}
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, String>> empList = new ArrayList<Map<String, String>>();

		TomActivityEmpsRelation empsRelationExample = new TomActivityEmpsRelation();
		empsRelationExample.setActivityId(Integer.parseInt(activityId));
		empsRelationExample.setApplyType("D");
		empsRelationExample.setApplyStatus("Y");
		empsRelationExample.setCreator(code);
		List<TomActivityEmpsRelation> activityEmpsRelations = activityEmpsRelationMapper
				.selectListByExample(empsRelationExample);
		for (TomActivityEmpsRelation activityEmpsRelation : activityEmpsRelations) {
			Map<String, String> empMap = new HashMap<String, String>();
			TomEmp emp = empMapper.selectByPrimaryKey(activityEmpsRelation.getCode());
			empMap.put("userId", emp.getCode());
			empMap.put("userName", emp.getName());
			empList.add(empMap);
		}
		data.put("activityId", activityId);
		data.put("data", empList);
		String json = jsonMapper.toJson(data);
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 功能描述：[查看我的活动时调用接口]
	 *
	 * 创建者：lg 建时间: 2016年3月23日 下午5:49:01
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public Result eleMyActivity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> queryMap = Maps.newHashMap();
		PageData<ActResults> page = new PageData<ActResults>();
		String idfy = request.getParameter("identifying");
		String code = request.getParameter("userId");
		examScoreService.reSetStatus(code);//扫描所有考试将过期未参加的考试设为不合格
		
		queryMap.put("identifying", idfy);
		queryMap.put("code", code);
		int count0 = 0;
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;

		// 分页
		int startNum = 0;
		if (request.getParameter("firstIndex") != null) {
			startNum = Integer.parseInt(request.getParameter("firstIndex"));
			queryMap.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		} else {
			queryMap.put("startNum", 0);
		}
		if (request.getParameter("pageSize") == null) {
			queryMap.put("endNum", 10);
			page.setPageSize(10);
		} else {
			queryMap.put("endNum",Integer.parseInt(request.getParameter("pageSize")));//startNum +
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}

		List<TomActivity> activities = activityMapper.selectAllByEmp(code);
		TomActivityProperty activityPropertyExample = new TomActivityProperty();
		TomExamScore scoreExample = new TomExamScore();
		//已通过的活动ID
		String passIds = "-1";
		for (TomActivity activity : activities) {
			activityPropertyExample.setActivityId(activity.getActivityId());
			List<TomActivityProperty> tomActivityProperties = activityPropertyMapper
					.selectByExample(activityPropertyExample);
			if (tomActivityProperties.size() > 0) {
				Map<Integer, Integer> finishedMap = new HashMap<Integer, Integer>();
				for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
					if (tomActivityProperty.getExamId() != null) {
						scoreExample.setCode(code);
						scoreExample.setExamId(tomActivityProperty.getExamId());
						TomExamScore tomExamScore = tomExamScoreMapper.selectOneByExample(scoreExample);
						if (tomExamScore.getGradeState().equals("Y")) {
							if (finishedMap.get(activity.getActivityId()) == null) {
								finishedMap.put(activity.getActivityId(), 1);
							} else {
								finishedMap.put(activity.getActivityId(),
										finishedMap.get(activity.getActivityId()) + 1);
							}
						}
					} else if (tomActivityProperty.getCourseId() != null) {
						TomCourses courses = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
						if (courses.getCourseOnline().equals("Y")) {
							TomCourseLearningRecord courseLearningRecordsExample = new TomCourseLearningRecord();
							courseLearningRecordsExample.setCode(code);
							courseLearningRecordsExample.setCourseId(courses.getCourseId());
							courseLearningRecordsExample.setLearningDate(tomActivityProperty.getEndTime());
							if (courseLearningRecordMapper.countByExample(courseLearningRecordsExample) > 0) {
								if (finishedMap.get(activity.getActivityId()) == null) {
									finishedMap.put(activity.getActivityId(), 1);
								} else {
									finishedMap.put(activity.getActivityId(),
									finishedMap.get(activity.getActivityId()) + 1);
								}
							}
						} else {
							TomCourseSignInRecords example = new TomCourseSignInRecords();
							example.setCourseId(courses.getCourseId());
							example.setCode(code);
							example.setCreateDate(tomActivityProperty.getEndTime());
							if (courseSignInRecordsMapper.countByExample(example) > 0) {
								if (finishedMap.get(activity.getActivityId()) == null) {
									finishedMap.put(activity.getActivityId(), 1);
								} else {
									finishedMap.put(activity.getActivityId(),
											finishedMap.get(activity.getActivityId()) + 1);
								}
							}
						}
					}
				}
				for (Map.Entry<Integer, Integer> entry : finishedMap.entrySet()) {
					if (entry.getValue() == tomActivityProperties.size()) {
						passIds = passIds + "," + entry.getKey();
					}
				}
			}
		}
		//未通过的活动ID
		String failIds = "-1";
		for (TomActivity activity : activities) {
			activityPropertyExample.setActivityId(activity.getActivityId());
			List<TomActivityProperty> tomActivityProperties = activityPropertyMapper
					.selectByExample(activityPropertyExample);
			if (tomActivityProperties.size() > 0) {
				Map<Integer, Integer> finishedMap = new HashMap<Integer, Integer>();
				for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
					if (tomActivityProperty.getExamId() != null) {
						scoreExample.setCode(code);
						scoreExample.setExamId(tomActivityProperty.getExamId());
						TomExamScore tomExamScore = tomExamScoreMapper.selectOneByExample(scoreExample);
						if (tomExamScore.getGradeState().equals("N")&&examScoreService.getRemainingCount(code, tomActivityProperty.getExamId())<=0) {
							if (finishedMap.get(activity.getActivityId()) == null) {
								finishedMap.put(activity.getActivityId(), 1);
							} else {
								finishedMap.put(activity.getActivityId(),
								finishedMap.get(activity.getActivityId()) + 1);
							}
						}
					} else if (tomActivityProperty.getCourseId() != null) {
						TomCourses courses = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
						if (courses.getCourseOnline().equals("Y")) {
							TomCourseLearningRecord courseLearningRecordsExample = new TomCourseLearningRecord();
							courseLearningRecordsExample.setCode(code);
							courseLearningRecordsExample.setCourseId(courses.getCourseId());
							courseLearningRecordsExample.setLearningDate(tomActivityProperty.getEndTime());
							if (courseLearningRecordMapper.countByExample(courseLearningRecordsExample) == 0&&tomActivityProperty.getEndTime().before(new Date())) {
								if (finishedMap.get(activity.getActivityId()) == null) {
									finishedMap.put(activity.getActivityId(), 1);
								} else {
									finishedMap.put(activity.getActivityId(),
									finishedMap.get(activity.getActivityId()) + 1);
								}
							}
						} else {
							TomCourseSignInRecords example = new TomCourseSignInRecords();
							example.setCourseId(courses.getCourseId());
							example.setCode(code);
							example.setCreateDate(tomActivityProperty.getEndTime());
							if (courseSignInRecordsMapper.countByExample(example) == 0&&tomActivityProperty.getEndTime().before(new Date())) {
								if (finishedMap.get(activity.getActivityId()) == null) {
									finishedMap.put(activity.getActivityId(), 1);
								} else {
									finishedMap.put(activity.getActivityId(),
									finishedMap.get(activity.getActivityId()) + 1);
								}
							}
						}
					}
				}
				for (Map.Entry<Integer, Integer> entry : finishedMap.entrySet()) {
					if (entry.getValue()>0) {
						failIds = failIds + "," + entry.getKey();
					}
				}
			}
		}
		
		queryMap.put("applyStatus", "Y");
		queryMap.put("code", code);
		count0 = activityMapper.countNotStart(queryMap);
		count1 = activityMapper.countUnderway(queryMap);
		queryMap.put("ids", passIds);
		count2 = activityMapper.countFinished(queryMap);
		queryMap.put("ids", failIds);
		count3 = activityMapper.countNotPass(queryMap);
		List<ActResults> list = new ArrayList<ActResults>();
		List<TomActivity> activities2;
		int count = 0;
		if ("0".equals(idfy)) {
			count = count0;
			activities2 = activityMapper.selectNotStart(queryMap);
		} else if ("1".equals(idfy)) {
			count = count1;
			activities2 = activityMapper.selectUnderway(queryMap);
		} else if ("2".equals(idfy)) {
			count = count2;
			queryMap.put("ids", passIds);
			activities2 = activityMapper.selectFinished(queryMap);
		} else {
			count = count3;
			queryMap.put("ids", failIds);
			activities2 = activityMapper.selectNotPass(queryMap);
		}

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date nowDate = new Date();
		for (TomActivity activity : activities2) {
			TomActivityEmpsRelation key = new TomActivityEmpsRelation();
			key.setActivityId(activity.getActivityId());
			key.setCode(code);
			TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper.selectByPrimaryKey(key);

			ActResults ac = new ActResults();
			ac.setActivityId(activity.getActivityId());
			ac.setActivityName(activity.getActivityName());
			ac.setActivityStartTime(activity.getActivityStartTime());
			ac.setActivityEndTime(activity.getActivityEndTime());
			ac.setActivityStartTimeS(format.format(activity.getActivityStartTime()));
			ac.setActivityEndTimeS(format.format(activity.getActivityEndTime()));
			ac.setActPicture(activity.getActPicture());
			ac.setNeedApply(activity.getNeedApply());
			ac.setCertificateState(activity.getCertificateState());
			ac.setReceiveState(activity.getReceiveState());
			ac.setReceiveAddress(activity.getReceiveAddress());
			ac.setCertificateValidState(activity.getCertificateValidState());
			ac.setCertificateId(activity.getCertificateId());
			if ("2".equals(idfy)) {
				TomActivityDept activityDeptExample = new TomActivityDept();
				activityDeptExample.setActivityId(activity.getActivityId());

				activityDeptExample.setHeaderCode(code);
				TomActivityDept activityDept = activityDeptMapper.selectByExample(activityDeptExample);
				if (activityDept != null && activityEmpsRelation != null) {
					String finishIds[] = passIds.split(",");
					for (String finishId : finishIds) {
						ac.setApplyStatus("10");
						if (finishId.equals(String.valueOf(activity.getActivityId()))) {
							ac.setApplyStatus("11");
							break;
						}
					}
				} else if (activityDept != null && activityEmpsRelation == null) {
					ac.setApplyStatus("10");
				} else if (activityDept == null && activityEmpsRelation != null) {
					ac.setApplyStatus("6");
				}

			} else if ("3".equals(idfy)) {
				if (activityEmpsRelation.getApplyType().equals("P")) {
					ac.setApplyStatus("9");
				} else {
					ac.setApplyStatus("7");
				}
			} else {
				if (activity.getNeedApply().equals("Y")) {
					if (activity.getApplicationStartTime().after(nowDate)) {
						ac.setApplyStatus("1");
					} else if (activity.getApplicationStartTime().before(nowDate)
							&& activity.getApplicationDeadline().after(nowDate)) {
						if (activityEmpsRelation.getApplyStatus().equals("N")) {
							ac.setApplyStatus("2");
						} else {
							ac.setApplyStatus("3");
						}
					} else if (activityEmpsRelation.getApplyStatus().equals("Y")
							&& activity.getApplicationDeadline().before(nowDate)
							&& activity.getActivityStartTime().after(nowDate)) {
						ac.setApplyStatus("4");
					} else if (activityEmpsRelation.getApplyStatus().equals("Y")
							&& activity.getActivityStartTime().before(nowDate)
							&& activity.getActivityEndTime().after(nowDate)) {
						ac.setApplyStatus("5");
					}
				} else {
					if (activity.getActivityStartTime().after(nowDate)) {
						if (activityEmpsRelation.getApplyType().equals("P")) {
							ac.setApplyStatus("8");
						} else {
							ac.setApplyStatus("4");
						}

					} else if (activity.getActivityStartTime().before(nowDate)
							&& activity.getActivityEndTime().after(nowDate)) {
						ac.setApplyStatus("5");
					}
				}
			}
			list.add(ac);
		}
		page.setCount(count);// TODO
		page.setDate(list);
		page.setPageNum((int) queryMap.get("startNum") / (int) queryMap.get("endNum") + 1);

		Myactivity as = new Myactivity();
		as.setCount0(count0);
		as.setCount1(count1);
		as.setCount2(count2);
		as.setCount3(count3);
		as.setActivityList(page);
		String json = mapper.writeValueAsString(as);
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");

	}
	
//	public Result eleMyActivity(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		Map<Object, Object> queryMap = Maps.newHashMap();
//		PageData<ActResults> page = new PageData<ActResults>();
//		String idfy = request.getParameter("identifying");
//		String code = request.getParameter("userId");
//		queryMap.put("identifying", idfy);
//		queryMap.put("code", code);
//		int count0 = 0;
//		int count1 = 0;
//		int count2 = 0;
//		int count3 = 0;
//
//		// 分页
//		int startNum = 0;
//		if (request.getParameter("firstIndex") != null) {
//			startNum = Integer.parseInt(request.getParameter("firstIndex"));
//			queryMap.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
//		} else {
//			queryMap.put("startNum", 0);
//		}
//		if (request.getParameter("pageSize") == null) {
//			queryMap.put("endNum", 10);
//			page.setPageSize(10);
//		} else {
//			queryMap.put("endNum", startNum + Integer.parseInt(request.getParameter("pageSize")));
//			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
//		}
//
//		List<TomActivity> activities = activityMapper.selectAllByEmp(code);
//		TomActivityProperty activityPropertyExample = new TomActivityProperty();
//		TomExamScore scoreExample = new TomExamScore();
//		String ids = "-1";
//		for (TomActivity activity : activities) {
//			activityPropertyExample.setActivityId(activity.getActivityId());
//			List<TomActivityProperty> tomActivityProperties = activityPropertyMapper
//					.selectByExample(activityPropertyExample);
//			if (tomActivityProperties.size() > 0) {
//				Map<Integer, Integer> finishedMap = new HashMap<Integer, Integer>();
//				for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
//					if (tomActivityProperty.getExamId() != null) {
//						scoreExample.setCode(code);
//						scoreExample.setExamId(tomActivityProperty.getExamId());
//						TomExamScore tomExamScore = tomExamScoreMapper.selectOneByExample(scoreExample);
//						if (tomExamScore.getGradeState().equals("Y")) {
//							if (finishedMap.get(activity.getActivityId()) == null) {
//								finishedMap.put(activity.getActivityId(), 1);
//							} else {
//								finishedMap.put(activity.getActivityId(),
//										finishedMap.get(activity.getActivityId()) + 1);
//							}
//						}
//					} else if (tomActivityProperty.getCourseId() != null) {
//						TomCourses courses = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
//						if (courses.getCourseOnline().equals("Y")) {
//							TomCourseLearningRecord courseLearningRecordsExample = new TomCourseLearningRecord();
//							courseLearningRecordsExample.setCode(code);
//							courseLearningRecordsExample.setCourseId(courses.getCourseId());
//							courseLearningRecordsExample.setLearningDate(tomActivityProperty.getEndTime());
//							if (courseLearningRecordMapper.countByExample(courseLearningRecordsExample) > 0) {
//								if (finishedMap.get(activity.getActivityId()) == null) {
//									finishedMap.put(activity.getActivityId(), 1);
//								} else {
//									finishedMap.put(activity.getActivityId(),
//											finishedMap.get(activity.getActivityId()) + 1);
//								}
//							}
//						} else {
//							TomCourseSignInRecords example = new TomCourseSignInRecords();
//							example.setCourseId(courses.getCourseId());
//							example.setCode(code);
//							example.setCreateDate(tomActivityProperty.getEndTime());
//							if (courseSignInRecordsMapper.countByExample(example) > 0) {
//								if (finishedMap.get(activity.getActivityId()) == null) {
//									finishedMap.put(activity.getActivityId(), 1);
//								} else {
//									finishedMap.put(activity.getActivityId(),
//											finishedMap.get(activity.getActivityId()) + 1);
//								}
//							}
//						}
//					}
//				}
//				for (Map.Entry<Integer, Integer> entry : finishedMap.entrySet()) {
//					if (entry.getValue() == tomActivityProperties.size()) {
//						ids = ids + "," + entry.getKey();
//					}
//				}
//			}
//		}
//		queryMap.put("applyStatus", "Y");
//		queryMap.put("code", code);
//		count0 = activityMapper.countNotStart(queryMap);
//		count1 = activityMapper.countUnderway(queryMap);
//		queryMap.put("ids", ids);
//		count2 = activityMapper.countFinished(queryMap);
//		count3 = activityMapper.countNotPass(queryMap);
//		List<ActResults> list = new ArrayList<ActResults>();
//		List<TomActivity> activities2;
//		int count = 0;
//		if ("0".equals(idfy)) {
//			count = count0;
//			activities2 = activityMapper.selectNotStart(queryMap);
//		} else if ("1".equals(idfy)) {
//			count = count1;
//			activities2 = activityMapper.selectUnderway(queryMap);
//		} else if ("2".equals(idfy)) {
//			count = count2;
//			activities2 = activityMapper.selectFinished(queryMap);
//		} else {
//			count = count3;
//			activities2 = activityMapper.selectNotPass(queryMap);
//		}
//
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		Date nowDate = new Date();
//		for (TomActivity activity : activities2) {
//			TomActivityEmpsRelation key = new TomActivityEmpsRelation();
//			key.setActivityId(activity.getActivityId());
//			key.setCode(code);
//			TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper.selectByPrimaryKey(key);
//
//			ActResults ac = new ActResults();
//			ac.setActivityId(activity.getActivityId());
//			ac.setActivityName(activity.getActivityName());
//			ac.setActivityStartTime(activity.getActivityStartTime());
//			ac.setActivityEndTime(activity.getActivityEndTime());
//			ac.setActivityStartTimeS(format.format(activity.getActivityStartTime()));
//			ac.setActivityEndTimeS(format.format(activity.getActivityEndTime()));
//			ac.setActPicture(activity.getActPicture());
//			ac.setNeedApply(activity.getNeedApply());
//			if ("2".equals(idfy)) {
//				TomActivityDept activityDeptExample = new TomActivityDept();
//				activityDeptExample.setActivityId(activity.getActivityId());
//
//				activityDeptExample.setHeaderCode(code);
//				TomActivityDept activityDept = activityDeptMapper.selectByExample(activityDeptExample);
//				if (activityDept != null && activityEmpsRelation != null) {
//					String finishIds[] = ids.split(",");
//					for (String finishId : finishIds) {
//						ac.setApplyStatus("10");
//						if (finishId.equals(String.valueOf(activity.getActivityId()))) {
//							ac.setApplyStatus("11");
//							break;
//						}
//					}
//				} else if (activityDept != null && activityEmpsRelation == null) {
//					ac.setApplyStatus("10");
//				} else if (activityDept == null && activityEmpsRelation != null) {
//					ac.setApplyStatus("6");
//				}
//
//			} else if ("3".equals(idfy)) {
//				if (activityEmpsRelation.getApplyType().equals("P")) {
//					ac.setApplyStatus("9");
//				} else {
//					ac.setApplyStatus("7");
//				}
//			} else {
//				if (activity.getNeedApply().equals("Y")) {
//					if (activity.getApplicationStartTime().after(nowDate)) {
//						ac.setApplyStatus("1");
//					} else if (activity.getApplicationStartTime().before(nowDate)
//							&& activity.getApplicationDeadline().after(nowDate)) {
//						if (activityEmpsRelation.getApplyStatus().equals("N")) {
//							ac.setApplyStatus("2");
//						} else {
//							ac.setApplyStatus("3");
//						}
//					} else if (activityEmpsRelation.getApplyStatus().equals("Y")
//							&& activity.getApplicationDeadline().before(nowDate)
//							&& activity.getActivityStartTime().after(nowDate)) {
//						ac.setApplyStatus("4");
//					} else if (activityEmpsRelation.getApplyStatus().equals("Y")
//							&& activity.getActivityStartTime().before(nowDate)
//							&& activity.getActivityEndTime().after(nowDate)) {
//						ac.setApplyStatus("5");
//					}
//				} else {
//					if (activity.getActivityStartTime().after(nowDate)) {
//						if (activityEmpsRelation.getApplyType().equals("P")) {
//							ac.setApplyStatus("8");
//						} else {
//							ac.setApplyStatus("4");
//						}
//
//					} else if (activity.getActivityStartTime().before(nowDate)
//							&& activity.getActivityEndTime().after(nowDate)) {
//						ac.setApplyStatus("5");
//					}
//				}
//			}
//			list.add(ac);
//		}
//		page.setCount(count);// TODO
//		page.setDate(list);
//		page.setPageNum((int) queryMap.get("startNum") / (int) queryMap.get("endNum") + 1);
//
//		Myactivity as = new Myactivity();
//		as.setCount0(count0);
//		as.setCount1(count1);
//		as.setCount2(count2);
//		as.setCount3(count3);
//		as.setActivityList(page);
//		String json = mapper.writeValueAsString(as);
//		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
//
//	}

	/**
	 * 
	 * 功能描述：[判断是否可进行]
	 *
	 * 创建者：JCX 创建时间: 2016年6月3日 下午3:39:34
	 * 
	 * @param activityProperties
	 * @param code
	 * @return
	 */
	public String getPreStatus(List<TomActivityProperty> activityProperties, String code) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		for (TomActivityProperty activityProperty : activityProperties) {
			if (activityProperty.getPreTask() == null || "".equals(activityProperty.getPreTask())) {
				break;
			}

			String sorts[] = activityProperty.getPreTask().split(",");
			for (String sort : sorts) {
				TomActivityProperty activityPropertyExample = new TomActivityProperty();
				activityPropertyExample.setSort(Integer.parseInt(sort));
				activityPropertyExample.setActivityId(activityProperty.getActivityId());
				TomActivityProperty preActivityProperty = activityPropertyMapper
						.selectByExample(activityPropertyExample).get(0);

				if (preActivityProperty.getCourseId() == null || "".equals(preActivityProperty.getCourseId())) {

					TomExamScore scoreExample = new TomExamScore();
					scoreExample.setExamId(preActivityProperty.getExamId());
					scoreExample.setCode(code);
					TomExamScore examScore = examScoreMapper.selectOneByExample(scoreExample);

					if (!examScore.getGradeState().equals("Y")) {
						return "N";
					}

				} else {
					TomCourses course = coursesMapper.selectByPrimaryKey(preActivityProperty.getCourseId());

					TomCourseLearningRecord example1 = new TomCourseLearningRecord();
					example1.setCode(code);
					example1.setCourseId(preActivityProperty.getCourseId());
					example1.setLearningDate(preActivityProperty.getEndTime());
					TomCourseSignInRecords example2 = new TomCourseSignInRecords();
					example2.setCode(code);
					example2.setCourseId(preActivityProperty.getCourseId());
					example2.setCreateDate(preActivityProperty.getEndTime());
					if (course.getCourseOnline().equals("Y")
							&& courseLearningRecordMapper.countByExample(example1) == 0) {
						return "N";
					} else if (course.getCourseOnline().equals("N")
							&& courseSignInRecordsMapper.countByExample(example2) == 0) {
						return "N";
					}
				}
			}
		}

		return "Y";
	}
	/**
	 * 
	 * @Title: myCertificate 
	 * @Description: 我的证书 
	 * @author Acemon 
	 * @date 2017年6月15日 下午3:40:29
	 * @return Result
	 */
	public Result myCertificate(HttpServletRequest request, HttpServletResponse response){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object,Object> map = new HashMap<>();
		PageData<MyCertificateDto> page = new PageData<MyCertificateDto>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String code = request.getParameter("userId");
		String json = "";
		if (null == code || "".equals(code)) {
			return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
		}
		map.put("code", code);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		}else{
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		}else{
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		int count = activityMapper.countMyCertificate(map);
		List<MyCertificateDto> list = activityMapper.selectMyCertificate(map);
		for (MyCertificateDto myCertificateDto : list) {
			myCertificateDto.setCreateTimeS(format.format(myCertificateDto.getCreateTime()));
		}
		page.setCount(count);
		page.setDate(list);
		page.setPageNum((int) map.get("startNum") / (int) map.get("endNum") + 1);
		try {
			json = mapper.writeValueAsString(page);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		json.replaceAll(":null", ":\"\"");
		return new Result("Y",json,ErrorCode.SUCCESS_CODE,"");
		
	}
	
	
}
