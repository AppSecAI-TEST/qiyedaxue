package com.chinamobo.ue.activity.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.chinamobo.ue.activity.dao.TomActivityDeptMapper;
import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityFeesMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityOtherRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomActivityQrMapper;
import com.chinamobo.ue.activity.dao.TomCertificateMapper;
import com.chinamobo.ue.activity.dto.TomActivityDto;
import com.chinamobo.ue.activity.dto.TomActivityFreesDto;
import com.chinamobo.ue.activity.dto.TomActivityPropertyDto;
import com.chinamobo.ue.activity.dto.TomCertificateDto;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityDept;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelationKey;
import com.chinamobo.ue.activity.entity.TomActivityFees;
import com.chinamobo.ue.activity.entity.TomActivityFeesKey;
import com.chinamobo.ue.activity.entity.TomActivityOtherRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.activity.entity.TomActivityQr;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseOtherRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomLecturerMapper;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseOtherRelation;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.exam.dao.TomExamEmpAllocationMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamOtherRelationMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamEmpAllocation;
import com.chinamobo.ue.exam.entity.TomExamOtherRelation;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.service.ExamService;
import com.chinamobo.ue.exception.EleException;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomGrpOrgRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelClassMapper;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelEmpRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelMapper;
import com.chinamobo.ue.system.dao.TomMessageDetailsMapper;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomGrpOrgRelation;
import com.chinamobo.ue.system.entity.TomLabel;
import com.chinamobo.ue.system.entity.TomLabelClass;
import com.chinamobo.ue.system.entity.TomLabelClassRelation;
import com.chinamobo.ue.system.entity.TomLabelEmpRelation;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.system.service.SendMessageService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.QRCodeUtil;
import com.chinamobo.ue.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import jersey.repackaged.com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 版本: [1.0] 功能说明: 培训活动管理
 *
 * 作者: WangLg 创建时间: 2016年3月9日 下午5:19:50
 */
@Service
@SuppressWarnings("all")
public class ActivityService {// extends BaseService{
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private SendMessageService sendMessageService;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomActivityMapper activityMapper;
	@Autowired
	private TomActivityFeesMapper activityFeesMapper;// 培训活动费用统计
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;// 培训活动配置
	// private TomActivityExapInfoMapper activityExapInfoMapper;//培训活动补考信息
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;// 培训活动人员
	@Autowired
	private TomActivityDeptMapper activityDeptMapper;// 培训活动推送部门负责人
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomGrpOrgRelationMapper tomGrpOrgRelationMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomCourseEmpRelationMapper tomCourseEmpRelationMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private TomExamPaperMapper tomExamPaperMapper;
	@Autowired
	private TomExamMapper tomExamMapper;
	@Autowired
	private TomRetakingExamMapper tomRetakingExamMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	@Autowired
	private ExamService examService;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	@Autowired
	private TomExamEmpAllocationMapper examEmpAllocationMapper;
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	@Autowired
	private TomLecturerMapper tomLecturerMapper;
	@Autowired
	private TomActivityQrMapper activityQrMapper;
	@Autowired
	private TomMessageDetailsMapper tomMessageDetailsMapper;
	@Autowired
	private TomOrgMapper tomOrgMapper;
	@Autowired
	private TomLabelMapper tomLabelMapper;
	@Autowired
	private TomActivityOtherRelationMapper tomActivityOtherRelationMapper;
	@Autowired
	private TomLabelClassMapper tomLabelClassMapper;
	@Autowired
	private TomLabelEmpRelationMapper tomLabelEmpRelationMapper;
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	@Autowired
	private TomExamOtherRelationMapper tomExamOtherRelationMapper;
	@Autowired
	private TomCourseOtherRelationMapper tomCourseOtherRelationMapper;
	@Autowired
	private TomCertificateMapper tomCertificateMapper;
	ObjectMapper mapper = new ObjectMapper();
	String filePath1=Config.getProperty("file_path");

	/**
	 * 功能描述：[R-添加培训活动人员]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 上午11:54:21
	 */

	// TODO
	@Transactional
	public void modifyEmpRelation(TomActivityEmpsRelation activityEmpsR, String needApplyType, String flag) {
		activityEmpsR.preInsertData();
		activityEmpsR.preInsert();
		activityEmpsRelationMapper.insertSelective(activityEmpsR);
		if ("add".equals(flag)) {
			activityEmpsR.preInsertData();
			activityEmpsR.preInsert();
			activityEmpsRelationMapper.insertSelective(activityEmpsR);
		} else {
			activityEmpsR.preUpdateData();
			activityEmpsR.preUpdate();
			activityEmpsRelationMapper.updateByPrimaryKeySelective(activityEmpsR);
		}

	}

	/**
	 * 功能描述：[R-培训活动费用统计]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 上午11:54:21
	 */
	@Transactional
	public void modifyActivityFees(TomActivityFees activityFees, String flag) {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		activityFees.preInsertData();
		activityFees.preInsert();
		activityFees.setCreateTime(new Date());
		activityFeesMapper.insertSelective(activityFees);
		if ("add".equals(flag)) {
			activityFees.preInsertData();
			activityFees.preInsert();
			activityFees.setCreateTime(new Date());
			activityFeesMapper.insertSelective(activityFees);
		} else {
			activityFees.preUpdateData();
			activityFees.preUpdate();
			activityFees.setCreateTime(new Date());
			activityFeesMapper.updateByPrimaryKeySelective(activityFees);
		}
	}

	/**
	 * 功能描述：[R-培训活动推送部门负责人]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 上午11:54:21
	 */
	@Transactional
	public void modifyActivityDept(TomActivityDept activityDept, String flag) {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		activityDept.preInsertData();
		activityDept.preInsert();
		activityDeptMapper.insertSelective(activityDept);
		if ("add".equals(flag)) {
			activityDept.preInsertData();
			activityDept.preInsert();
			activityDeptMapper.insertSelective(activityDept);
		} else {
			activityDept.preUpdateData();
			activityDept.preUpdate();
			activityDeptMapper.updateByPrimaryKeySelective(activityDept);
		}
	}

	/**
	 * 功能描述：[R-培训活动配置]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 上午11:54:21
	 */
	@Transactional
	public void modifyActivityProperty(TomActivityProperty activityProperty, String flag) {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		activityProperty.preInsertData();
		activityProperty.preInsert();
		activityPropertyMapper.insertSelective(activityProperty);
		if ("add".equals(flag)) {
			activityProperty.preInsertData();
			activityProperty.preInsert();
			activityPropertyMapper.insertSelective(activityProperty);
		} else {
			activityProperty.preUpdateData();
			activityProperty.preUpdate();
			activityPropertyMapper.updateByPrimaryKeySelective(activityProperty);
		}
	}

	/**
	 * 功能描述：[添加培训活动]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午9:46:33
	 * 
	 * @param activity
	 */
	@Transactional
	public String addActivity(String jsonStr) throws Exception {
		String BxAct = "";		//必修活动
		String BxActStatus = "";
		String XxAct = "";		//选修活动
		String XxActStatus = "";
		String BxCDown = "";		//必修线下课程
		String BxCDownStatus = "";
		 List<TomMessageDetails> selectList = tomMessageDetailsMapper.selectList();
		 for(TomMessageDetails message:selectList){
			 if(message.getId()==1){
				 BxAct = message.getMessage();
				 BxActStatus = message.getStatus();
			 }
			 if(message.getId()==3){
				 XxAct = message.getMessage();
				 XxActStatus = message.getStatus();
			 }
			 if(message.getId()==5){
				 BxCDown = message.getMessage();
				 BxCDownStatus = message.getStatus();
			 }
		 }
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObj = jsonObject.fromObject(jsonStr);
		TomActivity tomActivity = new TomActivity();
		String protocol = (String) jsonObj.get("protocol");
		String packageId = jsonObj.getString("packageId");
		String number = numberRecordService.getNumber(MapManager.numberType("PXHD"));
		tomActivity.setActivityNumber(number);
		tomActivity.setProtocol(protocol);
		Date date = new Date();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ShiroUser user = ShiroUtils.getCurrentUser();
		if (protocol != null && "Y".equals(protocol)) {
			tomActivity.setProtocolStartTime(format1.parse(jsonObj.getString("protocolStartTime")));
			tomActivity.setProtocolEndTime(format1.parse(jsonObj.getString("protocolEndTime")));
			tomActivity.setTrainFee(jsonObj.getDouble("trainFee"));
		}
		String needApply = jsonObj.getString("needApply");
		tomActivity.setNeedApply(needApply);
		if (needApply != null && "Y".equals(needApply)) {
			tomActivity.setNumberOfParticipants(jsonObj.getInt("numberOfParticipants"));
			tomActivity.setApplicationStartTime(format1.parse(jsonObj.getString("applicationStartTime")));
			tomActivity.setApplicationDeadline(format1.parse(jsonObj.getString("applicationDeadline")));
			if (tomActivity.getApplicationDeadline().before(new Date())) {
				return "protected";
			}
		}
		tomActivity.setActivityName(jsonObj.getString("activityName"));
		tomActivity.setActivityStartTime(format1.parse(jsonObj.getString("activityStartTime")));
		if (tomActivity.getActivityStartTime().before(new Date())) {
			return "protected";
		}
		tomActivity.setActivityEndTime(format1.parse(jsonObj.getString("activityEndTime")));
		tomActivity.setIntroduction(jsonObj.getString("introduction"));
		tomActivity.setActPicture(jsonObj.getString("activityImg"));
		// if(jsonStr.indexOf("\"deptCodes\":")!=-1){
		tomActivity.setDepts(jsonObj.getString("deptCodes"));
		// }
		// if(jsonStr.indexOf("\"employeeGradeStr\":")!=-1){
		tomActivity.setStaffLevels(jsonObj.getString("employeeGradeStr"));
		// }
		// if(jsonStr.indexOf("\"city\":")!=-1){
		tomActivity.setCity(jsonObj.getString("city"));
		// }

		tomActivity.setAdmins(jsonObj.getString("admins"));
		tomActivity.setCreateTime(date);
		tomActivity.setUpdateTime(date);
		tomActivity.setCreator(user.getName());
		tomActivity.setOperator(user.getName());
		tomActivity.setCreatorId(user.getCode());
		//新增证书属性
		if(jsonObj.has("certificateState") ){
			tomActivity.setCertificateState(jsonObj.getString("certificateState"));
		}
		if(jsonObj.has("certificateId")){
			tomActivity.setCertificateId(jsonObj.getInt("certificateId"));
		}
		if(jsonObj.has("receiveState")){
			tomActivity.setReceiveState(jsonObj.getString("receiveState"));
		}
		if(jsonObj.has("receiveAddress")){
			tomActivity.setReceiveAddress(jsonObj.getString("receiveAddress"));
		}
		//设置主库查询
		activityMapper.insertSelective(tomActivity);
		Integer activityId = tomActivity.getActivityId();

		ObjectMapper jsonMapper = new ObjectMapper();
		JSONArray preTaskInfos = jsonObj.getJSONArray("preTaskInfo");
		List<TomActivityProperty> courseForMessage = new ArrayList<TomActivityProperty>();
		List<TomActivityProperty> examForMessage = new ArrayList<TomActivityProperty>();
		for (int i = 0; i < preTaskInfos.size(); i++) {
			TomActivityPropertyDto preTaskDto = jsonMapper.readValue(preTaskInfos.get(i).toString(),
					TomActivityPropertyDto.class);
			TomActivityProperty tomActivityProperty = new TomActivityProperty();
			tomActivityProperty.setActivityId(activityId);
			tomActivityProperty.setSort(preTaskDto.getSort());
			tomActivityProperty.setPackageId(Integer.valueOf(packageId));
			tomActivityProperty.setStartTime(format2.parse(preTaskDto.getStartTime()));
			tomActivityProperty.setEndTime(format2.parse(preTaskDto.getEndTime()));
			tomActivityProperty.setPreTask(preTaskDto.getPretaskId());
			tomActivityProperty.setCreateTime(date);
			tomActivityProperty.setUpdateTime(date);
			tomActivityProperty.setCreator(user.getName());
			tomActivityProperty.setOperator(user.getName());
			if (preTaskDto.getRetakingExamTimes() == null) {
				if (preTaskDto.getLecturerId() != null && !"".equals(preTaskDto.getLecturerId())) {
					tomActivityProperty.setLecturers(preTaskDto.getLecturerId());
					tomActivityProperty.setCourseAddress(preTaskDto.getCourseAddress());
					tomActivityProperty.setCourseTime(Double.valueOf(preTaskDto.getCourseTime()));
					if (preTaskDto.getUnitPrice() != null && !"".equals(preTaskDto.getUnitPrice())) {
						tomActivityProperty.setUnitPrice(Double.valueOf(preTaskDto.getUnitPrice()));
						// TomActivityFees activityFees=new TomActivityFees();
						// activityFees.setActivityId(activityId);
						// activityFees.setFeeName("讲师");
						// activityFees.setFee(Double.valueOf(preTaskDto.getTotalPrice()));
						// activityFees.setRemark(preTaskDto.getLecturerId()+"号讲师进行"+Double.valueOf(preTaskDto.getCourseTime())+"分钟的培训费用。");
						// activityFees.setStatus("Y");
						// activityFees.setCreator(user.getName());
						// activityFees.setCreateTime(date);
						// activityFees.setCreatorId(user.getCode());
						// activityFees.setOperator(user.getName());
						// activityFees.setUpdateTime(date);
						// activityFeesMapper.insertSelective(activityFees);
					}
					tomActivityProperty.setTotalPrice(Double.valueOf(preTaskDto.getTotalPrice()));

				}

				tomActivityProperty.setCourseId(Integer.valueOf(preTaskDto.getTaskId()));
				/*新增活动课程关联二维码信息start*/
				TomCourses course=coursesMapper.selectByPrimaryKey(Integer.valueOf(preTaskDto.getTaskId()));
				if("N".equals(course.getCourseOnline())){
					String filePath1=Config.getProperty("file_path");
					SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
					StringBuffer sb=new StringBuffer();
					sb.append("qrcode:{ \"url\":\"");
					sb.append(Config.getProperty("qrSignUrl"));
					sb.append(course.getCourseId());
					sb.append("&activityId=");
					sb.append(activityId);
					sb.append("\",\"method\":\"get\"}");
					QRCodeUtil.courseEncode(sb.toString(),course.getCourseName()+"签到二维码-"+activityId, filePath1 +"file"+ File.separator + "tdc"+ File.separator+simple.format(new Date()));
					tomActivityProperty.setSignInTwoDimensionCode("file" + "/tdc"+"/"+simple.format(new Date())+"/"+course.getCourseName()+"签到二维码-"+activityId+".jpg");
					StringBuffer sb2=new StringBuffer();
					sb2.append("qrcode:{ \"url\":\"");
					sb2.append(Config.getProperty("qrGradeUrl"));
					sb2.append(course.getCourseId());
					sb2.append("&activityId=");
					sb2.append(activityId);
					sb2.append("\",\"method\":\"get\"}");
					QRCodeUtil.courseEncode(sb2.toString(),course.getCourseName()+"评分二维码-"+activityId, filePath1 +"file"+ File.separator + "tdc"+ File.separator+simple.format(new Date()));
					tomActivityProperty.setGradeTwoDimensionCode("file" + "/tdc"+"/"+simple.format(new Date())+"/"+course.getCourseName()+"评分二维码-"+activityId+".jpg");
				}
				
				courseForMessage.add(tomActivityProperty);
			} else {
				Integer examPaperId = Integer.valueOf(preTaskDto.getTaskId());
				TomExamPaper tomExamPaper = tomExamPaperMapper.selectByPrimaryKey(examPaperId);
				TomExam tomExam = new TomExam();
				tomExam.setExamNumber(numberRecordService.getNumber(MapManager.numberType("KS")));
				tomExam.setExamName(tomExamPaper.getExamPaperName());
				tomExam.setBeginTime(format2.parse(preTaskDto.getStartTime()));
				tomExam.setEndTime(format2.parse(preTaskDto.getEndTime()));
				tomExam.setExamPaperId(examPaperId);
				tomExam.setExamTime(tomExamPaper.getExamTime());
				tomExam.setRetakingExamCount(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
				tomExam.setExamType("A");
				tomExam.setOfflineExam(preTaskDto.getOfflineExam());
				tomExam.setAdmins(tomExamPaper.getAdmins());
				tomExam.setCreateTime(date);
				tomExam.setUpdateTime(date);
				tomExam.setCreator(user.getName());
				tomExam.setLastOperator(user.getName());
				tomExam.setCreatorId(user.getId().toString());
				tomExam.setExamAddress(preTaskDto.getExamAddress());
				if (tomExam.getEndTime().getTime() / 1000
						- tomExam.getBeginTime().getTime() / 1000 < tomExam.getExamTime() * 60) {
					throw new EleException("examTimeOut");
				}
				tomExamMapper.insertSelective(tomExam);
				Integer examId = tomExam.getExamId();
				RedisUtils.hset("exam", tomExam.getExamId().toString(), JSON.toJSONString(tomExam));
				TomRetakingExam tomRetakingExam1 = new TomRetakingExam();
				tomRetakingExam1.setExamId(examId);
				tomRetakingExam1.setSort(0);
				tomRetakingExam1.setRetakingExamBeginTime(tomExam.getBeginTime());
				tomRetakingExam1.setRetakingExamEndTime(tomExam.getEndTime());
				tomRetakingExamMapper.insertSelective(tomRetakingExam1);
				RedisUtils.hset("retakingExamSort",tomRetakingExam1.getExamId() + ":" + tomRetakingExam1.getSort(),JSON.toJSONString(tomRetakingExam1));
				for (int j = 0; j < preTaskDto.getRetakingExamBeginTimeList().size(); j++) {
					TomRetakingExam tomRetakingExam = new TomRetakingExam();
					tomRetakingExam.setExamId(examId);
					tomRetakingExam.setSort(j + 1);
					tomRetakingExam
							.setRetakingExamBeginTime(format2.parse(preTaskDto.getRetakingExamBeginTimeList().get(j)));
					tomRetakingExam
							.setRetakingExamEndTime(format2.parse(preTaskDto.getRetakingExamEndTimeList().get(j)));
					tomRetakingExamMapper.insertSelective(tomRetakingExam);
					RedisUtils.hset("retakingExamSort",tomRetakingExam.getExamId() + ":" + tomRetakingExam.getSort(),JSON.toJSONString(tomRetakingExam));
					List<TomRetakingExam> retakingExams2=retakingExamMapper.selectListByExample(tomRetakingExam);
					RedisUtils.hset("retakingExamsForOneExam", tomRetakingExam.getExamId().toString(),JSON.toJSONString(retakingExams2));
				}
				tomActivityProperty.setExamId(examId);
				tomActivityProperty.setExamName(tomExamPaper.getExamPaperName());
				tomActivityProperty.setExamTime(Double.valueOf(tomExamPaper.getExamTime().toString()));
				tomActivityProperty.setRetakingExamTimes(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
				tomActivityProperty.setOfflineExam(preTaskDto.getOfflineExam());
				examForMessage.add(tomActivityProperty);
			}
			activityPropertyMapper.insertSelective(tomActivityProperty);
		}
		
		//部门下的人员关联
		JSONArray empIdsArray = jsonObj.getJSONArray("empIds");
		if(empIdsArray.size()>0){
			for (int i = 0; i < empIdsArray.size(); i++) {
				TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
				tomActivityEmpsRelation.setActivityId(activityId);
				tomActivityEmpsRelation.setCode(empIdsArray.get(i).toString());
				tomActivityEmpsRelation.setStatus("Y");
				tomActivityEmpsRelation.setType("E");
				if (needApply != null && "N".equals(needApply)) {
					tomActivityEmpsRelation.setApplyStatus("Y");
					tomActivityEmpsRelation.setIsRequired("Y");
					tomActivityEmpsRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
							// 添加考试员工关联
							TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
							TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
							TomExamScore examScore = new TomExamScore();
							examScore.setExamId(activityProperty.getExamId());
							examScore.setCode(empIdsArray.get(i).toString());
							examScore.setEmpName(empMapper.selectByPrimaryKey(empIdsArray.get(i).toString()).getName());
							examScore.setGradeState("D");
							examScore.setType("E");
							examScore.setExamCount(0);
							examScore.setTotalPoints(0);
							examScore.setExamTotalTime(0);
							examScore.setCreateTime(exam.getBeginTime());
							examScore.setRightNumbe(0);
							examScore.setWrongNumber(examPaper.getTestQuestionCount());
							examScore.setAccuracy(0d);
							Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
							mapExamCode.put("examId", activityProperty.getExamId());
							mapExamCode.put("code", empIdsArray.get(i).toString());
							if(examScoreMapper.selectByExamCode(mapExamCode)==null){
								examScoreMapper.insertSelective(examScore);
							}
							RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),JSON.toJSONString(examScore));
							TomExamEmpAllocation examEmpAllocation = new TomExamEmpAllocation();
							examEmpAllocation.setCode(empIdsArray.get(i).toString());
							examEmpAllocation.setExamId(exam.getExamId());
							examEmpAllocation.setCreateTime(date);
							examEmpAllocation.setType("E");
							if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
								examEmpAllocationMapper.insertSelective(examEmpAllocation);
							}
						} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
							TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
							courseEmpRelation.setCode(empIdsArray.get(i).toString());
							courseEmpRelation.setCourseId(activityProperty.getCourseId());
							if (courseEmpRelationMapper.selectByExample(courseEmpRelation).size() == 0) {
								courseEmpRelation.setCreateTime(date);
								courseEmpRelation.setStatus("Y");
								courseEmpRelation.setCreator(user.getName());
								courseEmpRelation.setUpdateTime(date);
								courseEmpRelation.setOperator(user.getName());
								courseEmpRelation.setFinishStatus("N");
								courseEmpRelation.setCreatorId(user.getCode());
								courseEmpRelation.setType("E");
								courseEmpRelationMapper.insertSelective(courseEmpRelation);
							}
						}
					}
				} else {
					tomActivityEmpsRelation.setApplyStatus("N");
					tomActivityEmpsRelation.setIsRequired("N");
					tomActivityEmpsRelation.setApplyType("E");
				}
				tomActivityEmpsRelation.setCreateTime(date);
				tomActivityEmpsRelation.setUpdateTime(date);
				tomActivityEmpsRelation.setCreator(user.getName());
				tomActivityEmpsRelation.setOperator(user.getName());
	
				TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
				tomActivityEmpsRelationKey.setActivityId(activityId);
				tomActivityEmpsRelationKey.setCode(empIdsArray.get(i).toString());
				TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper.selectByPrimaryKey(tomActivityEmpsRelationKey);
				if (activityEmpsRelation == null) {
					activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
				}
			}
		}
		
		//关联部门
		JSONArray deptIdsArray = jsonObj.getJSONArray("deptIds");
		if(deptIdsArray.size()>0){
			for (int i = 0; i < deptIdsArray.size(); i++) {
				TomActivityOtherRelation activityOtherRelation = new TomActivityOtherRelation();
				activityOtherRelation.setActivityId(activityId);
				activityOtherRelation.setCode(deptIdsArray.get(i).toString());
				List<TomEmp> empList = new ArrayList<TomEmp>();
				TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
				TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
				//查询公司/部门名称
				if(deptIdsArray.get(i).toString().equals("1")){
					TomOrg org = tomOrgMapper.selectByPrimaryKey(deptIdsArray.get(i).toString());
					activityOtherRelation.setName(org.getName());
					examOtherRelation.setName(org.getName());
					courseOtherRelation.setName(org.getName());
					//全查人员
					empList = tomEmpMapper.selectAllDept();
				}else{
					TomDept dept = tomDeptMapper.selectByPrimaryKey(deptIdsArray.get(i).toString());
					activityOtherRelation.setName(dept.getName());
					examOtherRelation.setName(dept.getName());
					courseOtherRelation.setName(dept.getName());
					//查询部门下所有人
					empList = tomEmpMapper.selectByDeptCode(deptIdsArray.get(i).toString());
				}
				activityOtherRelation.setStatus("Y");
				activityOtherRelation.setType("D");
				activityOtherRelation.setCreator(user.getName());
				activityOtherRelation.setCreateTime(new Date());
				activityOtherRelation.setOperator(user.getName());
				activityOtherRelation.setUpdateTime(new Date());
				if (needApply != null && "N".equals(needApply)) {
					activityOtherRelation.setApplyStatus("Y");
					activityOtherRelation.setIsRequired("Y");
					activityOtherRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
							//添加考试其它关联
							examOtherRelation.setCode(deptIdsArray.get(i).toString());
							examOtherRelation.setCreateTime(new Date());
							examOtherRelation.setExamId(activityProperty.getExamId());
							examOtherRelation.setType("D");
							tomExamOtherRelationMapper.insertSelective(examOtherRelation);
							// 添加考试员工关联
							TomExamScore examScorDept = new TomExamScore();
							for(TomEmp emp : empList){
								examScorDept.setCode(emp.getCode());
								examScorDept.setExamId(activityProperty.getExamId());
								examScorDept.setEmpName(emp.getName());
								examScorDept.setGradeState("D");
								examScorDept.setExamCount(0);
								examScorDept.setTotalPoints(0);
								examScorDept.setExamTotalTime(0);
								examScorDept.setRightNumbe(0);
								examScorDept.setAccuracy(0d);
								examScorDept.setType("D");
								TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
								examScorDept.setCreateTime(exam.getBeginTime());
								TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
								examScorDept.setWrongNumber(examPaper.getTestQuestionCount());
								Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
								mapExamCode.put("examId", activityProperty.getExamId());
								mapExamCode.put("code", deptIdsArray.get(i).toString());
								if(examScoreMapper.selectByExamCode(mapExamCode)==null){
									examScoreMapper.insertSelective(examScorDept);
								}
								//添加推送考试记录
								RedisUtils.hset("examScore",examScorDept.getExamId() + ":" + examScorDept.getCode()+":"+examScorDept.getGradeState(),JSON.toJSONString(examScorDept));
								TomExamEmpAllocation examEmpAllocationDept = new TomExamEmpAllocation();
								examEmpAllocationDept.setCode(deptIdsArray.get(i).toString());
								examEmpAllocationDept.setExamId(exam.getExamId());
								examEmpAllocationDept.setCreateTime(new Date());
								examEmpAllocationDept.setType("D");
								if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
									examEmpAllocationMapper.insertSelective(examEmpAllocationDept);
								}
							}
						} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
							courseOtherRelation.setCourseId(activityProperty.getCourseId());
							courseOtherRelation.setCode(deptIdsArray.get(i).toString());
							courseOtherRelation.setStatus("Y");
							courseOtherRelation.setCreator(user.getName());
							courseOtherRelation.setCreatorId(user.getCode());
							courseOtherRelation.setCreateTime(new Date());
							courseOtherRelation.setOperator(user.getName());
							courseOtherRelation.setOperatorId(user.getCode());
							courseOtherRelation.setUpdateTime(new Date());
							courseOtherRelation.setType("D");
							Map<Object,Object> map = new HashMap<Object,Object>();
							map.put("courseId", activityProperty.getCourseId());
							map.put("code", deptIdsArray.get(i).toString());
							map.put("type", "D");
							List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
							if(list.size()<1){
								tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
							}
							TomCourseEmpRelation courseEmpRelationDept = new TomCourseEmpRelation();
							for(TomEmp emp : empList){
								courseEmpRelationDept.setCode(emp.getCode());
								courseEmpRelationDept.setCourseId(activityProperty.getCourseId());
								if (courseEmpRelationMapper.selectByExample(courseEmpRelationDept).size() == 0) {
									courseEmpRelationDept.setStatus("Y");
									courseEmpRelationDept.setFinishStatus("N");
									courseEmpRelationDept.setType("D");
									courseEmpRelationDept.setCreatorId(user.getCode());
									courseEmpRelationDept.setCreator(user.getName());
									courseEmpRelationDept.setCreateTime(new Date());
									courseEmpRelationDept.setOperator(user.getName());
									courseEmpRelationDept.setUpdateTime(new Date());
									courseEmpRelationMapper.insertSelective(courseEmpRelationDept);
								}
							}
						}
					}
				}else{
					activityOtherRelation.setApplyStatus("N");
					activityOtherRelation.setIsRequired("N");
					activityOtherRelation.setApplyType("E");
				}
				tomActivityOtherRelationMapper.insertSelective(activityOtherRelation);
			}
		}
		
		//关联标签
		JSONArray labelIdsArray = jsonObj.getJSONArray("labelIds");
		if(labelIdsArray.size()>0){
			for (int i = 0; i < labelIdsArray.size(); i++) {
				TomActivityOtherRelation activityOtherRelation = new TomActivityOtherRelation();
				activityOtherRelation.setActivityId(activityId);
				activityOtherRelation.setCode(labelIdsArray.get(i).toString());
				//查询标签名称
				TomLabel label = tomLabelMapper.selectByPrimaryKey(labelIdsArray.get(i).toString());
				activityOtherRelation.setName(label.getTagName());
				activityOtherRelation.setType("L");
				activityOtherRelation.setStatus("Y");
				activityOtherRelation.setCreator(user.getName());
				activityOtherRelation.setCreateTime(new Date());
				activityOtherRelation.setOperator(user.getName());
				activityOtherRelation.setUpdateTime(new Date());
				if (needApply != null && "N".equals(needApply)) {
					activityOtherRelation.setApplyStatus("Y");
					activityOtherRelation.setIsRequired("Y");
					activityOtherRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						//查询标签下所有人
						List<TomLabelEmpRelation> labelEmpRelationList = tomLabelEmpRelationMapper.selectBytagIdList(labelIdsArray.get(i).toString());
						if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
							//添加考试其它关联
							TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
							examOtherRelation.setCode(labelIdsArray.get(i).toString());
							examOtherRelation.setName(label.getTagName());
							examOtherRelation.setCreateTime(new Date());
							examOtherRelation.setExamId(activityProperty.getExamId());
							examOtherRelation.setType("L");
							tomExamOtherRelationMapper.insertSelective(examOtherRelation);
							// 添加考试员工关联
							TomExamScore examScorLabel = new TomExamScore();
							for(TomLabelEmpRelation labelEmpRelation : labelEmpRelationList){
								examScorLabel.setCode(labelEmpRelation.getCode());
								examScorLabel.setExamId(activityProperty.getExamId());
								examScorLabel.setEmpName(labelEmpRelation.getName());
								examScorLabel.setGradeState("D");
								examScorLabel.setExamCount(0);
								examScorLabel.setTotalPoints(0);
								examScorLabel.setExamTotalTime(0);
								examScorLabel.setRightNumbe(0);
								examScorLabel.setAccuracy(0d);
								examScorLabel.setType("L");
								TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
								examScorLabel.setCreateTime(exam.getBeginTime());
								TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
								examScorLabel.setWrongNumber(examPaper.getTestQuestionCount());
								Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
								mapExamCode.put("examId", activityProperty.getExamId());
								mapExamCode.put("code", labelIdsArray.get(i).toString());
								if(examScoreMapper.selectByExamCode(mapExamCode)==null){
									examScoreMapper.insertSelective(examScorLabel);
								}
								//添加推送考试记录
								RedisUtils.hset("examScore",examScorLabel.getExamId() + ":" + examScorLabel.getCode()+":"+examScorLabel.getGradeState(),JSON.toJSONString(examScorLabel));
								TomExamEmpAllocation examEmpAllocationLabel = new TomExamEmpAllocation();
								examEmpAllocationLabel.setCode(labelIdsArray.get(i).toString());
								examEmpAllocationLabel.setExamId(exam.getExamId());
								examEmpAllocationLabel.setCreateTime(new Date());
								examEmpAllocationLabel.setType("L");
								if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
									examEmpAllocationMapper.insertSelective(examEmpAllocationLabel);
								}
							}
						} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
							TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
							courseOtherRelation.setCourseId(activityProperty.getCourseId());
							courseOtherRelation.setCode(labelIdsArray.get(i).toString());
							courseOtherRelation.setName(label.getTagName());
							courseOtherRelation.setStatus("Y");
							courseOtherRelation.setCreator(user.getName());
							courseOtherRelation.setCreatorId(user.getCode());
							courseOtherRelation.setCreateTime(new Date());
							courseOtherRelation.setOperator(user.getName());
							courseOtherRelation.setOperatorId(user.getCode());
							courseOtherRelation.setUpdateTime(new Date());
							courseOtherRelation.setType("L");
							Map<Object,Object> map = new HashMap<Object,Object>();
							map.put("courseId", activityProperty.getCourseId());
							map.put("code", labelIdsArray.get(i).toString());
							map.put("type", "L");
							List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
							if(list.size()<1){
								tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
							}
							TomCourseEmpRelation courseEmpRelationLabel = new TomCourseEmpRelation();
							for(TomLabelEmpRelation labelEmpRelation : labelEmpRelationList){
								courseEmpRelationLabel.setCode(labelEmpRelation.getCode());
								courseEmpRelationLabel.setCourseId(activityProperty.getCourseId());
								if (courseEmpRelationMapper.selectByExample(courseEmpRelationLabel).size() == 0) {
									courseEmpRelationLabel.setStatus("Y");
									courseEmpRelationLabel.setFinishStatus("N");
									courseEmpRelationLabel.setType("L");
									courseEmpRelationLabel.setCreatorId(user.getCode());
									courseEmpRelationLabel.setCreator(user.getName());
									courseEmpRelationLabel.setCreateTime(new Date());
									courseEmpRelationLabel.setOperator(user.getName());
									courseEmpRelationLabel.setUpdateTime(new Date());
									courseEmpRelationMapper.insertSelective(courseEmpRelationLabel);
								}
							}
						}
					}
				}else{
					activityOtherRelation.setApplyStatus("N");
					activityOtherRelation.setIsRequired("N");
					activityOtherRelation.setApplyType("E");
				}
				tomActivityOtherRelationMapper.insertSelective(activityOtherRelation);
			}
		}
		
		//关联标签人员
		JSONArray labelEmpsArray = jsonObj.getJSONArray("labelEmps");
		if(labelEmpsArray.size()>0){
			for (int i = 0; i < labelEmpsArray.size(); i++) {
				TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
				tomActivityEmpsRelation.setActivityId(activityId);
				tomActivityEmpsRelation.setCode(labelEmpsArray.get(i).toString());
				tomActivityEmpsRelation.setStatus("Y");
				tomActivityEmpsRelation.setType("LE");
				tomActivityEmpsRelation.setCreateTime(date);
				tomActivityEmpsRelation.setUpdateTime(date);
				tomActivityEmpsRelation.setCreator(user.getName());
				tomActivityEmpsRelation.setOperator(user.getName());
				if (needApply != null && "N".equals(needApply)) {
					tomActivityEmpsRelation.setApplyStatus("Y");
					tomActivityEmpsRelation.setIsRequired("Y");
					tomActivityEmpsRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
							// 添加考试员工关联
							TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
							TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
							TomExamScore examScorLabelEmp = new TomExamScore();
							examScorLabelEmp.setExamId(activityProperty.getExamId());
							examScorLabelEmp.setCode(labelEmpsArray.get(i).toString());
							examScorLabelEmp.setEmpName(empMapper.selectByPrimaryKey(labelEmpsArray.get(i).toString()).getName());
							examScorLabelEmp.setGradeState("D");
							examScorLabelEmp.setType("LE");
							examScorLabelEmp.setExamCount(0);
							examScorLabelEmp.setTotalPoints(0);
							examScorLabelEmp.setExamTotalTime(0);
							examScorLabelEmp.setCreateTime(exam.getBeginTime());
							examScorLabelEmp.setRightNumbe(0);
							examScorLabelEmp.setWrongNumber(examPaper.getTestQuestionCount());
							examScorLabelEmp.setAccuracy(0d);
							Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
							mapExamCode.put("examId", activityProperty.getExamId());
							mapExamCode.put("code", labelEmpsArray.get(i).toString());
							if(examScoreMapper.selectByExamCode(mapExamCode)==null){
								examScoreMapper.insertSelective(examScorLabelEmp);
							}
							//添加推送考试记录
							RedisUtils.hset("examScore",examScorLabelEmp.getExamId() + ":" + examScorLabelEmp.getCode()+":"+examScorLabelEmp.getGradeState(),JSON.toJSONString(examScorLabelEmp));
							TomExamEmpAllocation examEmpAllocationLabelEmp = new TomExamEmpAllocation();
							examEmpAllocationLabelEmp.setCode(labelEmpsArray.get(i).toString());
							examEmpAllocationLabelEmp.setExamId(exam.getExamId());
							examEmpAllocationLabelEmp.setCreateTime(new Date());
							examEmpAllocationLabelEmp.setType("LE");
							if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
								examEmpAllocationMapper.insertSelective(examEmpAllocationLabelEmp);
							}
						} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
							TomCourseEmpRelation courseEmpRelationLabelEmp = new TomCourseEmpRelation();
							courseEmpRelationLabelEmp.setCode(labelEmpsArray.get(i).toString());
							courseEmpRelationLabelEmp.setCourseId(activityProperty.getCourseId());
							if (courseEmpRelationMapper.selectByExample(courseEmpRelationLabelEmp).size() == 0) {
								courseEmpRelationLabelEmp.setStatus("Y");
								courseEmpRelationLabelEmp.setFinishStatus("N");
								courseEmpRelationLabelEmp.setType("LE");
								courseEmpRelationLabelEmp.setCreatorId(user.getCode());
								courseEmpRelationLabelEmp.setCreator(user.getName());
								courseEmpRelationLabelEmp.setCreateTime(new Date());
								courseEmpRelationLabelEmp.setOperator(user.getName());
								courseEmpRelationLabelEmp.setUpdateTime(new Date());
								courseEmpRelationMapper.insertSelective(courseEmpRelationLabelEmp);
							}
						}
					}
				} else {
					tomActivityEmpsRelation.setApplyStatus("N");
					tomActivityEmpsRelation.setIsRequired("N");
					tomActivityEmpsRelation.setApplyType("E");
				}
				TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
				tomActivityEmpsRelationKey.setActivityId(activityId);
				tomActivityEmpsRelationKey.setCode(labelEmpsArray.get(i).toString());
				TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper.selectByPrimaryKey(tomActivityEmpsRelationKey);
				if (activityEmpsRelation == null) {
					activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
				}
			}
		}
		
		//关联标签分类
		JSONArray labelClassIdsArray = jsonObj.getJSONArray("labelClassIds");
		if(labelClassIdsArray.size()>0){
			for (int i = 0; i < labelClassIdsArray.size(); i++) {
				TomActivityOtherRelation activityOtherRelation = new TomActivityOtherRelation();
				activityOtherRelation.setActivityId(activityId);
				activityOtherRelation.setCode(labelClassIdsArray.get(i).toString());
				//查询标签分类名称
				TomLabelClass labelClass = tomLabelClassMapper.selectByPrimaryKey(Integer.valueOf(labelClassIdsArray.get(i).toString()));
				activityOtherRelation.setName(labelClass.getClassName());
				activityOtherRelation.setType("C");
				activityOtherRelation.setStatus("Y");
				activityOtherRelation.setCreator(user.getName());
				activityOtherRelation.setCreateTime(new Date());
				activityOtherRelation.setOperator(user.getName());
				activityOtherRelation.setUpdateTime(new Date());
				if (needApply != null && "N".equals(needApply)) {
					activityOtherRelation.setApplyStatus("Y");
					activityOtherRelation.setIsRequired("Y");
					activityOtherRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						//查询标签分类下所标签
						List<TomLabelClassRelation> labelClassRelationList = tomLabelClassRelationMapper.selectByLabelClassRelation(Integer.valueOf(labelClassIdsArray.get(i).toString()));
						for(TomLabelClassRelation labelClassRelation : labelClassRelationList){
							//根据标签ID查询标签人员
							List<TomLabelEmpRelation> labelEmpRelationList = tomLabelEmpRelationMapper.selectBytagIdList(labelClassRelation.getTagId());
							if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
								//添加考试其它关联
								TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
								examOtherRelation.setCode(labelClassIdsArray.get(i).toString());
								examOtherRelation.setName(labelClass.getClassName());
								examOtherRelation.setCreateTime(new Date());
								examOtherRelation.setExamId(activityProperty.getExamId());
								examOtherRelation.setType("L");
								tomExamOtherRelationMapper.insertSelective(examOtherRelation);
								// 添加考试员工关联
								TomExamScore examScorClass = new TomExamScore();
								for(TomLabelEmpRelation labelEmpRelation : labelEmpRelationList){
									examScorClass.setCode(labelEmpRelation.getCode());
									examScorClass.setExamId(activityProperty.getExamId());
									examScorClass.setEmpName(labelEmpRelation.getName());
									examScorClass.setGradeState("D");
									examScorClass.setExamCount(0);
									examScorClass.setTotalPoints(0);
									examScorClass.setExamTotalTime(0);
									examScorClass.setRightNumbe(0);
									examScorClass.setAccuracy(0d);
									examScorClass.setType("C");
									TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
									examScorClass.setCreateTime(exam.getBeginTime());
									TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
									examScorClass.setWrongNumber(examPaper.getTestQuestionCount());
									Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
									mapExamCode.put("examId", activityProperty.getExamId());
									mapExamCode.put("code", labelClassIdsArray.get(i).toString());
									if(examScoreMapper.selectByExamCode(mapExamCode)==null){
										examScoreMapper.insertSelective(examScorClass);
									}
									//添加推送考试记录
									RedisUtils.hset("examScore",examScorClass.getExamId() + ":" + examScorClass.getCode()+":"+examScorClass.getGradeState(),JSON.toJSONString(examScorClass));
									TomExamEmpAllocation examEmpAllocationClass = new TomExamEmpAllocation();
									examEmpAllocationClass.setCode(labelClassIdsArray.get(i).toString());
									examEmpAllocationClass.setExamId(exam.getExamId());
									examEmpAllocationClass.setCreateTime(new Date());
									examEmpAllocationClass.setType("C");
									if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
										examEmpAllocationMapper.insertSelective(examEmpAllocationClass);
									}
								}
							} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
								TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
								courseOtherRelation.setCourseId(activityProperty.getCourseId());
								courseOtherRelation.setCode(labelClassIdsArray.get(i).toString());
								courseOtherRelation.setName(labelClass.getClassName());
								courseOtherRelation.setStatus("Y");
								courseOtherRelation.setCreator(user.getName());
								courseOtherRelation.setCreatorId(user.getCode());
								courseOtherRelation.setCreateTime(new Date());
								courseOtherRelation.setOperator(user.getName());
								courseOtherRelation.setOperatorId(user.getCode());
								courseOtherRelation.setUpdateTime(new Date());
								courseOtherRelation.setType("C");
								Map<Object,Object> map = new HashMap<Object,Object>();
								map.put("courseId", activityProperty.getCourseId());
								map.put("code", labelClassIdsArray.get(i).toString());
								map.put("type", "C");
								List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
								if(list.size()<1){
									tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
								}
								TomCourseEmpRelation courseEmpRelationClass = new TomCourseEmpRelation();
								for(TomLabelEmpRelation labelEmpRelation : labelEmpRelationList){
									courseEmpRelationClass.setCode(labelEmpRelation.getCode());
									courseEmpRelationClass.setCourseId(activityProperty.getCourseId());
									if (courseEmpRelationMapper.selectByExample(courseEmpRelationClass).size() == 0) {
										courseEmpRelationClass.setStatus("Y");
										courseEmpRelationClass.setFinishStatus("N");
										courseEmpRelationClass.setType("C");
										courseEmpRelationClass.setCreatorId(user.getCode());
										courseEmpRelationClass.setCreator(user.getName());
										courseEmpRelationClass.setCreateTime(new Date());
										courseEmpRelationClass.setOperator(user.getName());
										courseEmpRelationClass.setUpdateTime(new Date());
										courseEmpRelationMapper.insertSelective(courseEmpRelationClass);
									}
								}
							}
						}
					}
				}else{
					activityOtherRelation.setApplyStatus("N");
					activityOtherRelation.setIsRequired("N");
					activityOtherRelation.setApplyType("E");
				}
				tomActivityOtherRelationMapper.insertSelective(activityOtherRelation);
			}
		}

		JSONArray deptManagerIdsArray = jsonObj.getJSONArray("deptManagerIds");
		JSONArray codesArray = jsonObj.getJSONArray("codes");
		if (needApply != null && "N".equals(needApply)) {
			for (int i = 0; i < deptManagerIdsArray.size(); i++) {
				TomActivityDept tomActivityDept = new TomActivityDept();
				tomActivityDept.setActivityId(activityId);
				tomActivityDept.setCreateTime(date);
				tomActivityDept.setUpdateTime(date);
				tomActivityDept.setCreator(user.getName());
				tomActivityDept.setOperator(user.getName());
				tomActivityDept.setCode(codesArray.get(i).toString().substring(1));
				tomActivityDept.setHeaderCode(deptManagerIdsArray.get(i).toString());
				tomActivityDept.setStatus("Y");
				tomActivityDept.setPushStatus("N");
				activityDeptMapper.insertSelective(tomActivityDept);
				TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
				tomActivityEmpsRelation.setActivityId(activityId);
				tomActivityEmpsRelation.setCode(deptManagerIdsArray.get(i).toString());
				tomActivityEmpsRelation.setStatus("Y");
				tomActivityEmpsRelation.setApplyStatus("N");
				tomActivityEmpsRelation.setIsRequired("Y");
				tomActivityEmpsRelation.setApplyType("P");
				tomActivityEmpsRelation.setCreateTime(date);
				tomActivityEmpsRelation.setUpdateTime(date);
				tomActivityEmpsRelation.setCreator(user.getName());
				tomActivityEmpsRelation.setOperator(user.getName());
				TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
				tomActivityEmpsRelationKey.setActivityId(activityId);
				tomActivityEmpsRelationKey.setCode(deptManagerIdsArray.get(i).toString());
				TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper
						.selectByPrimaryKey(tomActivityEmpsRelationKey);
				if (activityEmpsRelation == null) {
					activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
				}
			}
		}
		/*TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
		String activityApp="";
		String activityPhone="";
		String activityEmail="";
		String courseApp = "";
		String courseEmail = "";
		String coursePhone = "";
		String examApp ="";
		String examEmail="";
		String examPhone ="";
		if (needApply != null && "N".equals(needApply)) {
			activityApp = "亲爱的蔚来伙伴，"+ jsonObj.getString("activityName") +"已为您开放，"  
					+ "请点击链接查看学习项目详细内容，"
					+",学习项目开放时间："+ format1.format(tomActivity.getActivityStartTime())+"至"+ format1.format(tomActivity.getActivityEndTime())+"。请在此期限内完成此项目的所有学习内容。感谢您的参与！"
					+"Dear NextEVer,"+ jsonObj.getString("activityName") +" learning program is now open for you, please click the link for further information. Program opening time:"
					+"[" +tomActivity.getActivityStartTime()+"-"+tomActivity.getActivityEndTime()+"]"
					+ "Please finish all the courses in time. Thanks for your participation.    "
					+Config.getProperty("h5Index")+"views/task/activity_detail.html?activityId="+activityId ;
			activityPhone =  "亲爱的蔚来伙伴，"+ jsonObj.getString("activityName") +"已为您开放，"  
					+ "请点击链接查看学习项目详细内容，"
					+",学习项目开放时间："+ format1.format(tomActivity.getActivityStartTime())+"至"+ format1.format(tomActivity.getActivityEndTime())+"。请在此期限内完成此项目的所有学习内容。感谢您的参与！"
					+"Dear NextEVer,"+ jsonObj.getString("activityName") +" learning program is now open for you, please click the link for further information . Program opening time:"
					+"[" +tomActivity.getActivityStartTime()+"-"+tomActivity.getActivityEndTime()+"]"
					+ "Please finish all the courses in time. Thanks for your participation.   "+Config.getProperty("h5Index")+"views/task/activity_detail.html?activityId="+activityId;
			activityEmail =  "亲爱的蔚来伙伴，"+ jsonObj.getString("activityName") +"已为您开放，"  
					+ "请点击链接查看学习项目详细内容，"
					+",学习项目开放时间："+ format1.format(tomActivity.getActivityStartTime())+"至"+ format1.format(tomActivity.getActivityEndTime())+"。请在此期限内完成此项目的所有学习内容。感谢您的参与！"
					+"Dear NextEVer,"+ jsonObj.getString("activityName") +" learning program is now open for you, please click the link for further information.Program opening time:"
					+"[" +tomActivity.getActivityStartTime()+"-"+tomActivity.getActivityEndTime()+"]"
					+ "Please finish all the courses in time. Thanks for your participation.   "
					+"<a href=\""+Config.getProperty("pcIndex")+"views/task_center/train_actcon.html?activityId="+activityId+"\">"+Config.getProperty("pcIndex")+"views/task_center/train_actcon.html?activityId="+activityId+"</a>";
		}else if(needApply != null && "Y".equals(needApply)){
			activityApp = "亲爱的蔚来伙伴：" + jsonObj.getString("activityName") 
					+ "已为您开放，诚邀您的参加! 请点击链接查看活动详情，并进行报名。"
					+"席位有限，快点击注册吧！ Dear NextEVer,"+ jsonObj.getString("activityName") +"  course is now open for you. We sincerely invite you to join the course. Please click the link for further detials and registrate the course immediately!      "+Config.getProperty("h5Index")+"views/task/activity_detail.html?activityId="+activityId;
			activityPhone = "亲爱的蔚来伙伴：" + jsonObj.getString("activityName")
					+ "已为您开放，诚邀您的参加!请点击链接查看活动详情，并进行报名。"
					+"席位有限，快点击注册吧！Dear NextEVer,"+ jsonObj.getString("activityName") +"  course is now open for you. We sincerely invite you to join the course. Please click the link for further detials and registrate the course immediately!      "+Config.getProperty("h5Index")+"views/task/activity_detail.html?activityId="+activityId;
			activityEmail = "亲爱的蔚来伙伴：" + jsonObj.getString("activityName") 
					+ "已为您开放，诚邀您的参加! 请点击链接查看活动详情，并进行报名。"
					+"席位有限，快点击注册吧！Dear NextEVer,"+ jsonObj.getString("activityName") +"  course is now open for you. We sincerely invite you to join the course. Please click the link for further detials and registrate the course immediately!      "
							+ "<a href=\""+Config.getProperty("pcIndex")+"views/task_center/train_actcon.html?activityId="+activityId+"\">"+Config.getProperty("pcIndex")+"views/task_center/train_actcon.html?activityId="+activityId+"</a>";
		}
		List<String> list2 = new ArrayList<String>();
		List<String> listForEmail = new ArrayList<String>();
		if (empIdsArray.size() != 0) {
			list2 = JSONArray.toList(empIdsArray);
			if ("Y".equals(message.getSendToApp())) {
				sendMessageService.wxMessage(list2, activityApp);
				if (needApply != null && "N".equals(needApply)) {
					for (TomActivityProperty tomActivityProperty : courseForMessage) {
						TomCourses tomCourse = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
						if ("Y".equals(tomCourse.getCourseOnline())) {
							courseApp = "亲爱的蔚来伙伴：您有一门课程需要完成，课程名称" + "【" + tomCourse.getCourseName() + "】"
									+ "请点击链接前往课程，立即开始学习吧！"
									+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!      "+Config.getProperty("h5Index")+"views/user/my-course.html";
						} else {
							courseApp = "亲爱的蔚来伙伴, 您有一门课程需要完成。课程名称："+ tomCourse.getCourseName()  + "。课程名称："+ tomCourse.getCourseName()
									+"上课时间:"+ format1.format(tomActivityProperty.getStartTime()) +"上课地点："+tomCourse.getCourseAddress()
									+"您可以点击链接前往任务中心查看课程信息。"
									+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Course time: xxx. Location:"+ format1.format(tomActivityProperty.getStartTime()) +".You can click the link to find out more information in the task center.      "+Config.getProperty("h5Index")+"views/user/my-course.html";
						}
						sendMessageService.wxMessage(list2, courseApp);
					}
					for (TomActivityProperty tomActivityProperty : examForMessage) {
						examApp = "亲爱的蔚来伙伴，您有一个考试需要参加，请在【"
								+ format1.format(tomActivityProperty.getStartTime()) + "】至【"
								+ format1.format(tomActivityProperty.getEndTime()) + "】期间完成。请点击链接参加。"
								+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(tomActivityProperty.getStartTime())+"]-["+ format1.format(tomActivityProperty.getEndTime()) +"]. Please click the link to finish the test.      "+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomActivityProperty.getExamId();
								
						sendMessageService.wxMessage(list2, examApp);
					}
				}
			}
			if ("Y".equals(message.getSendToEmail())) {
				for (String code : list2) {
					TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
					if(selectByPrimaryKey.getSecretEmail()!=null && !"".equals(selectByPrimaryKey.getSecretEmail()))
					listForEmail.add(selectByPrimaryKey.getSecretEmail());
				}
				if(listForEmail.size()>0){
					SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", activityEmail);
					sm.sendMessage();
					if (needApply != null && "N".equals(needApply)) {
						for (TomActivityProperty tomActivityProperty : courseForMessage) {
							TomCourses tomCourse = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
							if ("Y".equals(tomCourse.getCourseOnline())) {
								courseEmail = "亲爱的蔚来伙伴：您有一门课程" + "【" + tomCourse.getCourseName() + "】"
										+ "需要完成。请点击链接前往课程，立即开始学习吧！"
										+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!      <a href=\""+Config.getProperty("pcIndex")+"views/task/course_detail.html?courseId="+tomActivityProperty.getCourseId()
										+"&activityId="+tomActivityProperty.getActivityId()+"\">"+Config.getProperty("pcIndex")+"views/task/course_detail.html?courseId="+tomActivityProperty.getCourseId()
										+"&activityId="+tomActivityProperty.getActivityId()+"</a>";
							} else {
								courseEmail = "亲爱的蔚来伙伴, 您有一门课程需要完成。课程名称："+ tomCourse.getCourseName()
								+"开课时间:"+ format1.format(tomActivityProperty.getStartTime()) +"上课地点："+tomCourse.getCourseAddress()
								+"您可以点击链接前往任务中心查看课程信息。"
								+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Course time: "+format1.format(tomActivityProperty.getStartTime())+". Location:"+ tomCourse.getCourseAddress() +".You can click the link to find out more information in the task center.      "+"<a href=\""+Config.getProperty("pcIndex")+"views/course/course_my.html\">"+Config.getProperty("pcIndex")+"views/course/course_my.html"+"</a>";
							}
							SendMail sm1 = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", courseEmail);
							sm1.sendMessage();
						}
						for (TomActivityProperty tomActivityProperty : examForMessage) {
							examEmail = "亲爱的蔚来伙伴：您有一次考试" + "【" + tomActivityProperty.getExamName() + "】" + "需要在【"
									+ format1.format(tomActivityProperty.getStartTime()) + "】至【"
									+ format1.format(tomActivityProperty.getEndTime()) + "】期间完成。"
									+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(tomActivityProperty.getStartTime())+"]-["+ format1.format(tomActivityProperty.getEndTime()) +"]. Please click the link to finish the test.      <a href=\""+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomActivityProperty.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomActivityProperty.getExamId()+"</a>";
							SendMail sm2 = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", examEmail);
							sm2.sendMessage();
						}
					}
				}
			}
			if ("Y".equals(message.getSendToPhone())) {
				for (String code : list2) {
					TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
					for (TomActivityProperty tomActivityProperty : courseForMessage) {
						TomCourses tomCourse = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
						if ("Y".equals(tomCourse.getCourseOnline())) {
							courseApp =  "亲爱的蔚来伙伴：您有一门课程需要完成，课程名称" + "【" + tomCourse.getCourseName() + "】"
									+ "请点击链接前往课程，立即开始学习吧！"
									+"&activityId="+tomActivityProperty.getActivityId()+";"+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Please click the link to start your learning right away!"+Config.getProperty("h5Index")+"views/user/my-course.html";
						} else {
							courseApp = "亲爱的蔚来伙伴, 您有一门课程需要完成。课程名称："+ tomCourse.getCourseName()  + "。课程名称："+ tomCourse.getCourseName()
							+"上课时间:"+ format1.format(tomActivityProperty.getStartTime()) +"上课地点："+tomCourse.getCourseAddress()
							+"您可以点击链接前往任务中心查看课程信息。"
							+"Dear NextEVer, you have a unfinished course that requires your attention. Course name: "+ "【" + tomCourse.getCourseName() + "】"+". Course time: xxx. Location:"+ format1.format(tomActivityProperty.getStartTime()) +".You can click the link to find out more information in the task center.      "+Config.getProperty("h5Index")+"views/user/my-course.html";
						}
						sendMessageService.sendMessage(courseApp, selectByPrimaryKey.getMobile());
					}
					for (TomActivityProperty tomActivityProperty : examForMessage) {
						 examApp = " 【蔚乐学】：您有一次考试" + "【" + tomActivityProperty.getExamName() + "】" + "需要在【"
								+ format1.format(tomActivityProperty.getStartTime()) + "】至【"
								+ format1.format(tomActivityProperty.getEndTime()) + "】期间完成。"
						 		+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(tomActivityProperty.getStartTime())+"]-["+ format1.format(tomActivityProperty.getEndTime()) +"]. Please click the link to finish the test.      "+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomActivityProperty.getExamId();
						sendMessageService.sendMessage(examApp, selectByPrimaryKey.getMobile());
					}
					sendMessageService.sendMessage(activityPhone, selectByPrimaryKey.getMobile());
				}

			}
		} else if (deptManagerIdsArray.size() != 0) {
			list2 = JSONArray.toList(deptManagerIdsArray);
			if ("Y".equals(message.getSendToApp())) {

				sendMessageService.wxMessage(list2, activityApp);
			}
			if ("Y".equals(message.getSendToEmail())) {
				for (String code : list2) {
					TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
					listForEmail.add(selectByPrimaryKey.getSecretEmail());
				}
				SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", activityEmail);
				sm.sendMessage();
			}
			if ("Y".equals(message.getSendToPhone())) {
				for (String code : list2) {
					TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
					listForEmail.add(selectByPrimaryKey.getSecretEmail());
					sendMessageService.sendMessage(activityPhone, selectByPrimaryKey.getMobile());
				}
			}
		}

		TomMessages activityMessage = new TomMessages();
		activityMessage.setMessageTitle(jsonObj.getString("activityName"));
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
			courseApp =   "亲爱的蔚来伙伴：您有一门课程需要完成，课程名称" + "【" + tomCourse.getCourseName() + "】"
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
			examApp = " 【蔚乐学】：您有一次考试" + "【" + tomActivityProperty.getExamName() + "】" + "需要在【"
					+ format1.format(tomActivityProperty.getStartTime()) + "】至【"
					+ format1.format(tomActivityProperty.getEndTime()) + "】期间完成。"
			 		+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format1.format(tomActivityProperty.getStartTime())+"]-["+ format1.format(tomActivityProperty.getEndTime()) +"]. Please click the link to finish the test.      "+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomActivityProperty.getExamId();
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
		}*/
		QRCodeUtil.courseEncode(Config.getProperty("h5Index")+"views/task/activity_detail.html?activityId="+activityId,tomActivity.getActivityName()+"-"+activityId,filePath1+"file"+ File.separator + "tdc"+File.separator+"activity");
		TomActivityQr activeqr = new TomActivityQr();
		activeqr.setQrPath("file" + "/tdc"+"/"+"activity"+"/"+tomActivity.getActivityName()+"-"+activityId+".jpg");
		activeqr.setActivityId(activityId);
		activeqr.setQrName(tomActivity.getActivityName()+"-"+activityId+".jpg");
		activityQrMapper.insert(activeqr);
		return "Y";
	}

	/**
	 * 功能描述：[更新培训活动]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午9:51:37
	 * 
	 * @param activity
	 */
	@Transactional
	public String updateActivity(String jsonStr) throws Exception {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObj = jsonObject.fromObject(jsonStr);
		TomActivity tomActivity = new TomActivity();
		String protocol = (String) jsonObj.get("protocol");
		String packageId = jsonObj.getString("packageId");
		tomActivity.setActivityId(Integer.parseInt(jsonObj.getString("activityId")));
		TomActivity activity = activityMapper.selectByPrimaryKey(tomActivity.getActivityId());
		if ("N".equals(activity.getNeedApply()) && activity.getActivityStartTime().before(new Date())) {
			return "protected";
		} else if ("Y".equals(activity.getNeedApply()) && activity.getApplicationStartTime().before(new Date())) {
			return "protected";
		}

		tomActivity.setProtocol(protocol);
		Date date = new Date();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ShiroUser user = ShiroUtils.getCurrentUser();
		if (protocol != null && "Y".equals(protocol)) {
			tomActivity.setProtocolStartTime(format1.parse(jsonObj.getString("protocolStartTime")));
			tomActivity.setProtocolEndTime(format1.parse(jsonObj.getString("protocolEndTime")));
			tomActivity.setTrainFee(jsonObj.getDouble("trainFee"));
		}
		String needApply = jsonObj.getString("needApply");
		tomActivity.setNeedApply(needApply);
		if (needApply != null && "Y".equals(needApply)) {
			tomActivity.setNumberOfParticipants(jsonObj.getInt("numberOfParticipants"));
			tomActivity.setApplicationStartTime(format1.parse(jsonObj.getString("applicationStartTime")));
			tomActivity.setApplicationDeadline(format1.parse(jsonObj.getString("applicationDeadline")));
		}
		tomActivity.setActivityName(jsonObj.getString("activityName"));
		tomActivity.setActivityStartTime(format1.parse(jsonObj.getString("activityStartTime")));
		tomActivity.setActivityEndTime(format1.parse(jsonObj.getString("activityEndTime")));
		tomActivity.setIntroduction(jsonObj.getString("introduction"));
		tomActivity.setActPicture(jsonObj.getString("activityImg"));
		// if(jsonStr.indexOf("\"deptCodes\":")!=-1){
		tomActivity.setDepts(jsonObj.getString("deptCodes"));
		// }
		// if(jsonStr.indexOf("\"employeeGradeStr\":")!=-1){
		tomActivity.setStaffLevels(jsonObj.getString("employeeGradeStr"));
		// }
		// if(jsonStr.indexOf("\"city\":")!=-1){
		tomActivity.setCity(jsonObj.getString("city"));
		// }
		tomActivity.setAdmins(jsonObj.getString("admins"));
		tomActivity.setUpdateTime(date);
		tomActivity.setOperator(user.getName());
		activityMapper.updateByPrimaryKeySelective(tomActivity);
		Integer activityId = tomActivity.getActivityId();

		// 删除之前的关联数据
		TomActivityProperty example = new TomActivityProperty();
		example.setActivityId(activityId);
		List<TomActivityProperty> tomActivityProperties = activityPropertyMapper.selectByExample(example);
		for (TomActivityProperty activityProperty : tomActivityProperties) {
			if (activityProperty.getExamId() != null) {
				examService.deleteexam(activityProperty.getExamId());
				//删除活动考试其它关联
				tomExamOtherRelationMapper.deleteByExamId(activityProperty.getExamId());
			} else if (activityProperty.getCourseId() != null) {
				TomCourseEmpRelation courseEmpRelationExample = new TomCourseEmpRelation();
				courseEmpRelationExample.setCourseId(activityProperty.getCourseId());
				courseEmpRelationExample.setCreateTime(activityProperty.getCreateTime());
				courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
			}
		}
		activityPropertyMapper.deleteByActivityId(activityId);
		activityEmpsRelationMapper.deleteByActivityId(activityId);
		activityDeptMapper.deleteByActivityId(activityId);
		tomActivityOtherRelationMapper.deleteByActivityId(activityId);
		// TomActivityFees feesExample=new TomActivityFees();
		// feesExample.setActivityId(activityId);
		// activityFeesMapper.deleteByExample(feesExample);

		ObjectMapper jsonMapper = new ObjectMapper();
		JSONArray preTaskInfos = jsonObj.getJSONArray("preTaskInfo");
		for (int i = 0; i < preTaskInfos.size(); i++) {
			TomActivityPropertyDto preTaskDto = jsonMapper.readValue(preTaskInfos.get(i).toString(),
					TomActivityPropertyDto.class);
			TomActivityProperty tomActivityProperty = new TomActivityProperty();
			tomActivityProperty.setActivityId(activityId);
			tomActivityProperty.setSort(preTaskDto.getSort());
			tomActivityProperty.setPackageId(Integer.valueOf(packageId));
			tomActivityProperty.setStartTime(format2.parse(preTaskDto.getStartTime()));
			tomActivityProperty.setEndTime(format2.parse(preTaskDto.getEndTime()));
			tomActivityProperty.setPreTask(preTaskDto.getPretaskId());
			tomActivityProperty.setCreateTime(date);
			tomActivityProperty.setUpdateTime(date);
			tomActivityProperty.setCreator(user.getName());
			tomActivityProperty.setOperator(user.getName());
			if (preTaskDto.getRetakingExamTimes() == null) {
				if (preTaskDto.getLecturerId() != null && !"".equals(preTaskDto.getLecturerId())) {
					tomActivityProperty.setLecturers(preTaskDto.getLecturerId());
					tomActivityProperty.setCourseAddress(preTaskDto.getCourseAddress());
					tomActivityProperty.setCourseTime(Double.valueOf(preTaskDto.getCourseTime()));

					if (preTaskDto.getUnitPrice() != null && !"".equals(preTaskDto.getUnitPrice())) {
						tomActivityProperty.setUnitPrice(Double.valueOf(preTaskDto.getUnitPrice()));
					}

					tomActivityProperty.setTotalPrice(Double.valueOf(preTaskDto.getTotalPrice()));
				}
				tomActivityProperty.setCourseId(Integer.valueOf(preTaskDto.getTaskId()));
				/*新增活动课程关联二维码信息start*/
				TomCourses course=coursesMapper.selectByPrimaryKey(Integer.valueOf(preTaskDto.getTaskId()));
				if("N".equals(course.getCourseOnline())){
					String filePath1=Config.getProperty("file_path");
					SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
					StringBuffer sb=new StringBuffer();
					sb.append("qrcode:{ \"url\":\"");
					sb.append(Config.getProperty("qrSignUrl"));
					sb.append(course.getCourseId());
					sb.append("&activityId=");
					sb.append(activityId);
					sb.append("\",\"method\":\"get\"}");
					QRCodeUtil.courseEncode(sb.toString(),course.getCourseName()+"签到二维码-"+activityId, filePath1 +"file"+ File.separator + "tdc"+ File.separator+simple.format(new Date()));
					tomActivityProperty.setSignInTwoDimensionCode("file" + "/tdc"+"/"+simple.format(new Date())+"/"+course.getCourseName()+"签到二维码-"+activityId+".jpg");
					StringBuffer sb2=new StringBuffer();
					sb2.append("qrcode:{ \"url\":\"");
					sb2.append(Config.getProperty("qrGradeUrl"));
					sb2.append(course.getCourseId());
					sb2.append("&activityId=");
					sb2.append(activityId);
					sb2.append("\",\"method\":\"get\"}");
					QRCodeUtil.courseEncode(sb2.toString(),course.getCourseName()+"评分二维码-"+activityId, filePath1 +"file"+ File.separator + "tdc"+ File.separator+simple.format(new Date()));
					tomActivityProperty.setGradeTwoDimensionCode("file" + "/tdc"+"/"+simple.format(new Date())+"/"+course.getCourseName()+"评分二维码-"+activityId+".jpg");
				}
				
			} else {
				Integer examPaperId = Integer.valueOf(preTaskDto.getTaskId());
				TomExamPaper tomExamPaper = tomExamPaperMapper.selectByPrimaryKey(examPaperId);
				TomExam tomExam = new TomExam();
				tomExam.setExamNumber(numberRecordService.getNumber(MapManager.numberType("KS")));
				tomExam.setExamName(tomExamPaper.getExamPaperName());
				tomExam.setBeginTime(format2.parse(preTaskDto.getStartTime()));
				tomExam.setEndTime(format2.parse(preTaskDto.getEndTime()));
				tomExam.setExamPaperId(examPaperId);
				tomExam.setExamTime(tomExamPaper.getExamTime());
				tomExam.setRetakingExamCount(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
				tomExam.setExamType("A");
				tomExam.setOfflineExam(preTaskDto.getOfflineExam());
				tomExam.setAdmins(tomExamPaper.getAdmins());
				tomExam.setCreateTime(date);
				tomExam.setUpdateTime(date);
				tomExam.setCreator(user.getName());
				tomExam.setLastOperator(user.getName());
				tomExam.setCreatorId(user.getId().toString());
				tomExam.setExamAddress(preTaskDto.getExamAddress());
				tomExamMapper.insertSelective(tomExam);
				RedisUtils.hset("exam", tomExam.getExamId().toString(), JSON.toJSONString(tomExam));
				Integer examId = tomExam.getExamId();

				TomRetakingExam tomRetakingExam1 = new TomRetakingExam();
				tomRetakingExam1.setExamId(examId);
				tomRetakingExam1.setSort(0);
				tomRetakingExam1.setRetakingExamBeginTime(tomExam.getBeginTime());
				tomRetakingExam1.setRetakingExamEndTime(tomExam.getEndTime());
				tomRetakingExamMapper.insertSelective(tomRetakingExam1);
				RedisUtils.hset("retakingExamSort",tomRetakingExam1.getExamId() + ":" + tomRetakingExam1.getSort(),JSON.toJSONString(tomRetakingExam1));
				for (int j = 0; j < preTaskDto.getRetakingExamBeginTimeList().size(); j++) {
					TomRetakingExam tomRetakingExam = new TomRetakingExam();
					tomRetakingExam.setExamId(examId);
					tomRetakingExam.setSort(j + 1);
					tomRetakingExam
							.setRetakingExamBeginTime(format2.parse(preTaskDto.getRetakingExamBeginTimeList().get(j)));
					tomRetakingExam
							.setRetakingExamEndTime(format2.parse(preTaskDto.getRetakingExamEndTimeList().get(j)));
					tomRetakingExamMapper.insertSelective(tomRetakingExam);
					RedisUtils.hset("retakingExamSort",tomRetakingExam.getExamId() + ":" + tomRetakingExam.getSort(),JSON.toJSONString(tomRetakingExam));
					List<TomRetakingExam> retakingExams2=retakingExamMapper.selectListByExample(tomRetakingExam);
					RedisUtils.hset("retakingExamsForOneExam", tomRetakingExam.getExamId().toString(),JSON.toJSONString(retakingExams2));
				}
				tomActivityProperty.setExamId(examId);
				tomActivityProperty.setExamName(tomExamPaper.getExamPaperName());
				tomActivityProperty.setExamTime(Double.valueOf(tomExamPaper.getExamTime().toString()));
				tomActivityProperty.setRetakingExamTimes(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
				tomActivityProperty.setOfflineExam(preTaskDto.getOfflineExam());
			}
			activityPropertyMapper.insertSelective(tomActivityProperty);
		}
		
		JSONArray empIdsArray = jsonObj.getJSONArray("empIds");
		TomExamScore examScore;
		TomExamEmpAllocation examEmpAllocation;
		TomCourseEmpRelation courseEmpRelation;
		for (int i = 0; i < empIdsArray.size(); i++) {
			TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
			tomActivityEmpsRelation.setActivityId(activityId);
			tomActivityEmpsRelation.setCode(empIdsArray.get(i).toString());
			tomActivityEmpsRelation.setStatus("Y");
			tomActivityEmpsRelation.setType("E");
			if (needApply != null && "N".equals(needApply)) {
				tomActivityEmpsRelation.setApplyStatus("Y");
				tomActivityEmpsRelation.setIsRequired("Y");
				tomActivityEmpsRelation.setApplyType("E");

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
						examScore.setCode(empIdsArray.get(i).toString());
						examScore.setEmpName(empMapper.selectByPrimaryKey(empIdsArray.get(i).toString()).getName());
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
						mapExamCode.put("code", empIdsArray.get(i).toString());
						if(examScoreMapper.selectByExamCode(mapExamCode)==null){
							examScoreMapper.insertSelective(examScore);
						}
						examEmpAllocation = new TomExamEmpAllocation();
						examEmpAllocation.setCode(empIdsArray.get(i).toString());
						examEmpAllocation.setExamId(exam.getExamId());
						examEmpAllocation.setCreateTime(date);
						if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
							examEmpAllocationMapper.insertSelective(examEmpAllocation);
							
						}
					} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
						courseEmpRelation = new TomCourseEmpRelation();
						courseEmpRelation.setCode(empIdsArray.get(i).toString());
						courseEmpRelation.setCourseId(activityProperty.getCourseId());
						if (courseEmpRelationMapper.selectByExample(courseEmpRelation).size() == 0) {
							courseEmpRelation.setCreateTime(date);
							courseEmpRelation.setStatus("Y");
							courseEmpRelation.setCreator(user.getName());
							courseEmpRelation.setUpdateTime(date);
							courseEmpRelation.setOperator(user.getName());
							courseEmpRelation.setFinishStatus("N");
							courseEmpRelation.setCreatorId(user.getCode());
							courseEmpRelationMapper.insertSelective(courseEmpRelation);
						}
					}
				}
			} else {
				tomActivityEmpsRelation.setApplyStatus("N");
				tomActivityEmpsRelation.setIsRequired("N");
				tomActivityEmpsRelation.setApplyType("E");
			}
			tomActivityEmpsRelation.setCreateTime(date);
			tomActivityEmpsRelation.setUpdateTime(date);
			tomActivityEmpsRelation.setCreator(user.getName());
			tomActivityEmpsRelation.setOperator(user.getName());
			TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
			tomActivityEmpsRelationKey.setActivityId(activityId);
			tomActivityEmpsRelationKey.setCode(empIdsArray.get(i).toString());
			TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper
					.selectByPrimaryKey(tomActivityEmpsRelationKey);
			if (activityEmpsRelation == null) {
				activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
			}
		}
		
		//关联部门
		JSONArray deptIdsArray = jsonObj.getJSONArray("deptIds");
		if(deptIdsArray.size()>0){
			for (int i = 0; i < deptIdsArray.size(); i++) {
				TomActivityOtherRelation activityOtherRelation = new TomActivityOtherRelation();
				activityOtherRelation.setActivityId(activityId);
				activityOtherRelation.setCode(deptIdsArray.get(i).toString());
				List<TomEmp> empList = new ArrayList<TomEmp>();
				TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
				TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
				//查询公司/部门名称
				if(deptIdsArray.get(i).toString().equals("1")){
					TomOrg org = tomOrgMapper.selectByPrimaryKey(deptIdsArray.get(i).toString());
					activityOtherRelation.setName(org.getName());
					examOtherRelation.setName(org.getName());
					courseOtherRelation.setName(org.getName());
					//全查人员
					empList = tomEmpMapper.selectAllDept();
				}else{
					TomDept dept = tomDeptMapper.selectByPrimaryKey(deptIdsArray.get(i).toString());
					activityOtherRelation.setName(dept.getName());
					examOtherRelation.setName(dept.getName());
					courseOtherRelation.setName(dept.getName());
					//查询部门下所有人
					empList = tomEmpMapper.selectByDeptCode(deptIdsArray.get(i).toString());
				}
				activityOtherRelation.setStatus("Y");
				activityOtherRelation.setType("D");
				activityOtherRelation.setCreator(user.getName());
				activityOtherRelation.setCreateTime(new Date());
				activityOtherRelation.setOperator(user.getName());
				activityOtherRelation.setUpdateTime(new Date());
				if (needApply != null && "N".equals(needApply)) {
					activityOtherRelation.setApplyStatus("Y");
					activityOtherRelation.setIsRequired("Y");
					activityOtherRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
							//添加考试其它关联
							examOtherRelation.setCode(deptIdsArray.get(i).toString());
							examOtherRelation.setCreateTime(new Date());
							examOtherRelation.setExamId(activityProperty.getExamId());
							examOtherRelation.setType("D");
							tomExamOtherRelationMapper.insertSelective(examOtherRelation);
							// 添加考试员工关联
							TomExamScore examScorDept = new TomExamScore();
							for(TomEmp emp : empList){
								examScorDept.setCode(emp.getCode());
								examScorDept.setExamId(activityProperty.getExamId());
								examScorDept.setEmpName(emp.getName());
								examScorDept.setGradeState("D");
								examScorDept.setExamCount(0);
								examScorDept.setTotalPoints(0);
								examScorDept.setExamTotalTime(0);
								examScorDept.setRightNumbe(0);
								examScorDept.setAccuracy(0d);
								examScorDept.setType("D");
								TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
								examScorDept.setCreateTime(exam.getBeginTime());
								TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
								examScorDept.setWrongNumber(examPaper.getTestQuestionCount());
								Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
								mapExamCode.put("examId", activityProperty.getExamId());
								mapExamCode.put("code", deptIdsArray.get(i).toString());
								if(examScoreMapper.selectByExamCode(mapExamCode)==null){
									examScoreMapper.insertSelective(examScorDept);
								}
								//添加推送考试记录
								RedisUtils.hset("examScore",examScorDept.getExamId() + ":" + examScorDept.getCode()+":"+examScorDept.getGradeState(),JSON.toJSONString(examScorDept));
								TomExamEmpAllocation examEmpAllocationDept = new TomExamEmpAllocation();
								examEmpAllocationDept.setCode(deptIdsArray.get(i).toString());
								examEmpAllocationDept.setExamId(exam.getExamId());
								examEmpAllocationDept.setCreateTime(new Date());
								examEmpAllocationDept.setType("D");
								if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
									examEmpAllocationMapper.insertSelective(examEmpAllocationDept);
								}
							}
						} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
							courseOtherRelation.setCourseId(activityProperty.getCourseId());
							courseOtherRelation.setCode(deptIdsArray.get(i).toString());
							courseOtherRelation.setStatus("Y");
							courseOtherRelation.setCreator(user.getName());
							courseOtherRelation.setCreatorId(user.getCode());
							courseOtherRelation.setCreateTime(new Date());
							courseOtherRelation.setOperator(user.getName());
							courseOtherRelation.setOperatorId(user.getCode());
							courseOtherRelation.setUpdateTime(new Date());
							courseOtherRelation.setType("D");
							Map<Object,Object> map = new HashMap<Object,Object>();
							map.put("courseId", activityProperty.getCourseId());
							map.put("code", deptIdsArray.get(i).toString());
							map.put("type", "D");
							List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
							if(list.size()<1){
								tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
							}
							TomCourseEmpRelation courseEmpRelationDept = new TomCourseEmpRelation();
							for(TomEmp emp : empList){
								courseEmpRelationDept.setCode(emp.getCode());
								courseEmpRelationDept.setCourseId(activityProperty.getCourseId());
								if (courseEmpRelationMapper.selectByExample(courseEmpRelationDept).size() == 0) {
									courseEmpRelationDept.setStatus("Y");
									courseEmpRelationDept.setFinishStatus("N");
									courseEmpRelationDept.setType("D");
									courseEmpRelationDept.setCreatorId(user.getCode());
									courseEmpRelationDept.setCreator(user.getName());
									courseEmpRelationDept.setCreateTime(new Date());
									courseEmpRelationDept.setOperator(user.getName());
									courseEmpRelationDept.setUpdateTime(new Date());
									courseEmpRelationMapper.insertSelective(courseEmpRelationDept);
								}
							}
						}
					}
				}else{
					activityOtherRelation.setApplyStatus("N");
					activityOtherRelation.setIsRequired("N");
					activityOtherRelation.setApplyType("E");
				}
				tomActivityOtherRelationMapper.insertSelective(activityOtherRelation);
			}
		}
		
		//关联标签
		JSONArray labelIdsArray = jsonObj.getJSONArray("labelIds");
		if(labelIdsArray.size()>0){
			for (int i = 0; i < labelIdsArray.size(); i++) {
				TomActivityOtherRelation activityOtherRelation = new TomActivityOtherRelation();
				activityOtherRelation.setActivityId(activityId);
				activityOtherRelation.setCode(labelIdsArray.get(i).toString());
				//查询标签名称
				TomLabel label = tomLabelMapper.selectByPrimaryKey(labelIdsArray.get(i).toString());
				activityOtherRelation.setName(label.getTagName());
				activityOtherRelation.setType("L");
				activityOtherRelation.setStatus("Y");
				activityOtherRelation.setCreator(user.getName());
				activityOtherRelation.setCreateTime(new Date());
				activityOtherRelation.setOperator(user.getName());
				activityOtherRelation.setUpdateTime(new Date());
				if (needApply != null && "N".equals(needApply)) {
					activityOtherRelation.setApplyStatus("Y");
					activityOtherRelation.setIsRequired("Y");
					activityOtherRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						//查询标签下所有人
						List<TomLabelEmpRelation> labelEmpRelationList = tomLabelEmpRelationMapper.selectBytagIdList(labelIdsArray.get(i).toString());
						if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
							//添加考试其它关联
							TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
							examOtherRelation.setCode(labelIdsArray.get(i).toString());
							examOtherRelation.setName(label.getTagName());
							examOtherRelation.setCreateTime(new Date());
							examOtherRelation.setExamId(activityProperty.getExamId());
							examOtherRelation.setType("L");
							tomExamOtherRelationMapper.insertSelective(examOtherRelation);
							// 添加考试员工关联
							TomExamScore examScorLabel = new TomExamScore();
							for(TomLabelEmpRelation labelEmpRelation : labelEmpRelationList){
								examScorLabel.setCode(labelEmpRelation.getCode());
								examScorLabel.setExamId(activityProperty.getExamId());
								examScorLabel.setEmpName(labelEmpRelation.getName());
								examScorLabel.setGradeState("D");
								examScorLabel.setExamCount(0);
								examScorLabel.setTotalPoints(0);
								examScorLabel.setExamTotalTime(0);
								examScorLabel.setRightNumbe(0);
								examScorLabel.setAccuracy(0d);
								examScorLabel.setType("L");
								TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
								examScorLabel.setCreateTime(exam.getBeginTime());
								TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
								examScorLabel.setWrongNumber(examPaper.getTestQuestionCount());
								Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
								mapExamCode.put("examId", activityProperty.getExamId());
								mapExamCode.put("code", labelIdsArray.get(i).toString());
								if(examScoreMapper.selectByExamCode(mapExamCode)==null){
									examScoreMapper.insertSelective(examScorLabel);
								}
								//添加推送考试记录
								RedisUtils.hset("examScore",examScorLabel.getExamId() + ":" + examScorLabel.getCode()+":"+examScorLabel.getGradeState(),JSON.toJSONString(examScorLabel));
								TomExamEmpAllocation examEmpAllocationLabel = new TomExamEmpAllocation();
								examEmpAllocationLabel.setCode(labelIdsArray.get(i).toString());
								examEmpAllocationLabel.setExamId(exam.getExamId());
								examEmpAllocationLabel.setCreateTime(new Date());
								examEmpAllocationLabel.setType("L");
								if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
									examEmpAllocationMapper.insertSelective(examEmpAllocationLabel);
								}
							}
						} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
							TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
							courseOtherRelation.setCourseId(activityProperty.getCourseId());
							courseOtherRelation.setCode(labelIdsArray.get(i).toString());
							courseOtherRelation.setName(label.getTagName());
							courseOtherRelation.setStatus("Y");
							courseOtherRelation.setCreator(user.getName());
							courseOtherRelation.setCreatorId(user.getCode());
							courseOtherRelation.setCreateTime(new Date());
							courseOtherRelation.setOperator(user.getName());
							courseOtherRelation.setOperatorId(user.getCode());
							courseOtherRelation.setUpdateTime(new Date());
							courseOtherRelation.setType("L");
							Map<Object,Object> map = new HashMap<Object,Object>();
							map.put("courseId", activityProperty.getCourseId());
							map.put("code", labelIdsArray.get(i).toString());
							map.put("type", "L");
							List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
							if(list.size()<1){
								tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
							}
							TomCourseEmpRelation courseEmpRelationLabel = new TomCourseEmpRelation();
							for(TomLabelEmpRelation labelEmpRelation : labelEmpRelationList){
								courseEmpRelationLabel.setCode(labelEmpRelation.getCode());
								courseEmpRelationLabel.setCourseId(activityProperty.getCourseId());
								if (courseEmpRelationMapper.selectByExample(courseEmpRelationLabel).size() == 0) {
									courseEmpRelationLabel.setStatus("Y");
									courseEmpRelationLabel.setFinishStatus("N");
									courseEmpRelationLabel.setType("L");
									courseEmpRelationLabel.setCreatorId(user.getCode());
									courseEmpRelationLabel.setCreator(user.getName());
									courseEmpRelationLabel.setCreateTime(new Date());
									courseEmpRelationLabel.setOperator(user.getName());
									courseEmpRelationLabel.setUpdateTime(new Date());
									courseEmpRelationMapper.insertSelective(courseEmpRelationLabel);
								}
							}
						}
					}
				}else{
					activityOtherRelation.setApplyStatus("N");
					activityOtherRelation.setIsRequired("N");
					activityOtherRelation.setApplyType("E");
				}
				tomActivityOtherRelationMapper.insertSelective(activityOtherRelation);
			}
		}
		
		//关联标签人员
		JSONArray labelEmpsArray = jsonObj.getJSONArray("labelEmps");
		if(labelEmpsArray.size()>0){
			for (int i = 0; i < labelEmpsArray.size(); i++) {
				TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
				tomActivityEmpsRelation.setActivityId(activityId);
				tomActivityEmpsRelation.setCode(labelEmpsArray.get(i).toString());
				tomActivityEmpsRelation.setStatus("Y");
				tomActivityEmpsRelation.setType("LE");
				tomActivityEmpsRelation.setCreateTime(date);
				tomActivityEmpsRelation.setUpdateTime(date);
				tomActivityEmpsRelation.setCreator(user.getName());
				tomActivityEmpsRelation.setOperator(user.getName());
				if (needApply != null && "N".equals(needApply)) {
					tomActivityEmpsRelation.setApplyStatus("Y");
					tomActivityEmpsRelation.setIsRequired("Y");
					tomActivityEmpsRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
							// 添加考试员工关联
							TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
							TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
							TomExamScore examScorLabelEmp = new TomExamScore();
							examScorLabelEmp.setExamId(activityProperty.getExamId());
							examScorLabelEmp.setCode(labelEmpsArray.get(i).toString());
							examScorLabelEmp.setEmpName(empMapper.selectByPrimaryKey(labelEmpsArray.get(i).toString()).getName());
							examScorLabelEmp.setGradeState("D");
							examScorLabelEmp.setType("LE");
							examScorLabelEmp.setExamCount(0);
							examScorLabelEmp.setTotalPoints(0);
							examScorLabelEmp.setExamTotalTime(0);
							examScorLabelEmp.setCreateTime(exam.getBeginTime());
							examScorLabelEmp.setRightNumbe(0);
							examScorLabelEmp.setWrongNumber(examPaper.getTestQuestionCount());
							examScorLabelEmp.setAccuracy(0d);
							Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
							mapExamCode.put("examId", activityProperty.getExamId());
							mapExamCode.put("code", labelEmpsArray.get(i).toString());
							if(examScoreMapper.selectByExamCode(mapExamCode)==null){
								examScoreMapper.insertSelective(examScorLabelEmp);
							}
							//添加推送考试记录
							RedisUtils.hset("examScore",examScorLabelEmp.getExamId() + ":" + examScorLabelEmp.getCode()+":"+examScorLabelEmp.getGradeState(),JSON.toJSONString(examScorLabelEmp));
							TomExamEmpAllocation examEmpAllocationLabelEmp = new TomExamEmpAllocation();
							examEmpAllocationLabelEmp.setCode(labelEmpsArray.get(i).toString());
							examEmpAllocationLabelEmp.setExamId(exam.getExamId());
							examEmpAllocationLabelEmp.setCreateTime(new Date());
							examEmpAllocationLabelEmp.setType("LE");
							if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
								examEmpAllocationMapper.insertSelective(examEmpAllocationLabelEmp);
							}
						} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
							TomCourseEmpRelation courseEmpRelationLabelEmp = new TomCourseEmpRelation();
							courseEmpRelationLabelEmp.setCode(labelEmpsArray.get(i).toString());
							courseEmpRelationLabelEmp.setCourseId(activityProperty.getCourseId());
							if (courseEmpRelationMapper.selectByExample(courseEmpRelationLabelEmp).size() == 0) {
								courseEmpRelationLabelEmp.setStatus("Y");
								courseEmpRelationLabelEmp.setFinishStatus("N");
								courseEmpRelationLabelEmp.setType("LE");
								courseEmpRelationLabelEmp.setCreatorId(user.getCode());
								courseEmpRelationLabelEmp.setCreator(user.getName());
								courseEmpRelationLabelEmp.setCreateTime(new Date());
								courseEmpRelationLabelEmp.setOperator(user.getName());
								courseEmpRelationLabelEmp.setUpdateTime(new Date());
								courseEmpRelationMapper.insertSelective(courseEmpRelationLabelEmp);
							}
						}
					}
				} else {
					tomActivityEmpsRelation.setApplyStatus("N");
					tomActivityEmpsRelation.setIsRequired("N");
					tomActivityEmpsRelation.setApplyType("E");
				}
				TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
				tomActivityEmpsRelationKey.setActivityId(activityId);
				tomActivityEmpsRelationKey.setCode(labelEmpsArray.get(i).toString());
				TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper.selectByPrimaryKey(tomActivityEmpsRelationKey);
				if (activityEmpsRelation == null) {
					activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
				}
			}
		}
		
		//关联标签分类
		JSONArray labelClassIdsArray = jsonObj.getJSONArray("labelClassIds");
		if(labelClassIdsArray.size()>0){
			for (int i = 0; i < labelClassIdsArray.size(); i++) {
				TomActivityOtherRelation activityOtherRelation = new TomActivityOtherRelation();
				activityOtherRelation.setActivityId(activityId);
				activityOtherRelation.setCode(labelClassIdsArray.get(i).toString());
				//查询标签分类名称
				TomLabelClass labelClass = tomLabelClassMapper.selectByPrimaryKey(Integer.valueOf(labelClassIdsArray.get(i).toString()));
				activityOtherRelation.setName(labelClass.getClassName());
				activityOtherRelation.setType("C");
				activityOtherRelation.setStatus("Y");
				activityOtherRelation.setCreator(user.getName());
				activityOtherRelation.setCreateTime(new Date());
				activityOtherRelation.setOperator(user.getName());
				activityOtherRelation.setUpdateTime(new Date());
				if (needApply != null && "N".equals(needApply)) {
					activityOtherRelation.setApplyStatus("Y");
					activityOtherRelation.setIsRequired("Y");
					activityOtherRelation.setApplyType("E");
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					activityPropertyExample.setActivityId(activityId);
					List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty activityProperty : activityProperties) {
						//查询标签分类下所标签
						List<TomLabelClassRelation> labelClassRelationList = tomLabelClassRelationMapper.selectByLabelClassRelation(Integer.valueOf(labelClassIdsArray.get(i).toString()));
						for(TomLabelClassRelation labelClassRelation : labelClassRelationList){
							//根据标签ID查询标签人员
							List<TomLabelEmpRelation> labelEmpRelationList = tomLabelEmpRelationMapper.selectBytagIdList(labelClassRelation.getTagId());
							if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
								//添加考试其它关联
								TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
								examOtherRelation.setCode(labelClassRelation.getTagId());
								examOtherRelation.setName(labelClass.getClassName());
								examOtherRelation.setCreateTime(new Date());
								examOtherRelation.setExamId(activityProperty.getExamId());
								examOtherRelation.setType("C");
								tomExamOtherRelationMapper.insertSelective(examOtherRelation);
								// 添加考试员工关联
								TomExamScore examScorClass = new TomExamScore();
								for(TomLabelEmpRelation labelEmpRelation : labelEmpRelationList){
									examScorClass.setCode(labelEmpRelation.getCode());
									examScorClass.setExamId(activityProperty.getExamId());
									examScorClass.setEmpName(labelEmpRelation.getName());
									examScorClass.setGradeState("D");
									examScorClass.setExamCount(0);
									examScorClass.setTotalPoints(0);
									examScorClass.setExamTotalTime(0);
									examScorClass.setRightNumbe(0);
									examScorClass.setAccuracy(0d);
									examScorClass.setType("C");
									TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
									examScorClass.setCreateTime(exam.getBeginTime());
									TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
									examScorClass.setWrongNumber(examPaper.getTestQuestionCount());
									Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
									mapExamCode.put("examId", activityProperty.getExamId());
									mapExamCode.put("code", labelClassIdsArray.get(i).toString());
									if(examScoreMapper.selectByExamCode(mapExamCode)==null){
										examScoreMapper.insertSelective(examScorClass);
									}
									//添加推送考试记录
									RedisUtils.hset("examScore",examScorClass.getExamId() + ":" + examScorClass.getCode()+":"+examScorClass.getGradeState(),JSON.toJSONString(examScorClass));
									TomExamEmpAllocation examEmpAllocationClass = new TomExamEmpAllocation();
									examEmpAllocationClass.setCode(labelClassIdsArray.get(i).toString());
									examEmpAllocationClass.setExamId(exam.getExamId());
									examEmpAllocationClass.setCreateTime(new Date());
									examEmpAllocationClass.setType("C");
									if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
										examEmpAllocationMapper.insertSelective(examEmpAllocationClass);
									}
								}
							} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
								TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
								courseOtherRelation.setCourseId(activityProperty.getCourseId());
								courseOtherRelation.setCode(labelClassIdsArray.get(i).toString());
								courseOtherRelation.setName(labelClass.getClassName());
								courseOtherRelation.setStatus("Y");
								courseOtherRelation.setCreator(user.getName());
								courseOtherRelation.setCreatorId(user.getCode());
								courseOtherRelation.setCreateTime(new Date());
								courseOtherRelation.setOperator(user.getName());
								courseOtherRelation.setOperatorId(user.getCode());
								courseOtherRelation.setUpdateTime(new Date());
								courseOtherRelation.setType("C");
								Map<Object,Object> map = new HashMap<Object,Object>();
								map.put("courseId", activityProperty.getCourseId());
								map.put("code", labelClassIdsArray.get(i).toString());
								map.put("type", "C");
								List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
								if(list.size()<1){
									tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
								}
								TomCourseEmpRelation courseEmpRelationClass = new TomCourseEmpRelation();
								for(TomLabelEmpRelation labelEmpRelation : labelEmpRelationList){
									courseEmpRelationClass.setCode(labelEmpRelation.getCode());
									courseEmpRelationClass.setCourseId(activityProperty.getCourseId());
									if (courseEmpRelationMapper.selectByExample(courseEmpRelationClass).size() == 0) {
										courseEmpRelationClass.setStatus("Y");
										courseEmpRelationClass.setFinishStatus("N");
										courseEmpRelationClass.setType("C");
										courseEmpRelationClass.setCreatorId(user.getCode());
										courseEmpRelationClass.setCreator(user.getName());
										courseEmpRelationClass.setCreateTime(new Date());
										courseEmpRelationClass.setOperator(user.getName());
										courseEmpRelationClass.setUpdateTime(new Date());
										courseEmpRelationMapper.insertSelective(courseEmpRelationClass);
									}
								}
							}
						}
					}
				}else{
					activityOtherRelation.setApplyStatus("N");
					activityOtherRelation.setIsRequired("N");
					activityOtherRelation.setApplyType("E");
				}
				tomActivityOtherRelationMapper.insertSelective(activityOtherRelation);
			}
		}
		return "Y";
	}

	/**
	 * 功能描述：[删除培训活动]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午9:51:54
	 * 
	 * @param activity
	 */
	@Transactional
	public String deleteActivity(int activityId) {
		TomActivity activity = activityMapper.selectByPrimaryKey(activityId);
		if ("N".equals(activity.getNeedApply()) && activity.getActivityStartTime().before(new Date())) {
			return "protected";
		} else if ("Y".equals(activity.getNeedApply()) && activity.getApplicationStartTime().before(new Date())) {
			return "protected";
		}

		TomActivityProperty example = new TomActivityProperty();
		example.setActivityId(activityId);
		List<TomActivityProperty> tomActivityProperties = activityPropertyMapper.selectByExample(example);
		for (TomActivityProperty activityProperty : tomActivityProperties) {
			if (activityProperty.getExamId() != null) {
				examService.deleteexam(activityProperty.getExamId());
				//删除活动考试其它关联
				tomExamOtherRelationMapper.deleteByExamId(activityProperty.getExamId());
			} else if (activityProperty.getCourseId() != null) {
				TomCourseEmpRelation courseEmpRelationExample = new TomCourseEmpRelation();
				courseEmpRelationExample.setCourseId(activityProperty.getCourseId());
				courseEmpRelationExample.setCreateTime(activityProperty.getCreateTime());
				courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
			}
		}
		activityMapper.deleteByPrimaryKey(activityId);
		activityPropertyMapper.deleteByActivityId(activityId);
		activityEmpsRelationMapper.deleteByActivityId(activityId);
		activityDeptMapper.deleteByActivityId(activityId);
		TomActivityFees feesExample = new TomActivityFees();
		feesExample.setActivityId(activityId);
		activityFeesMapper.deleteByExample(feesExample);
		tomActivityOtherRelationMapper.deleteByActivityId(activityId);
		return "Y";
	}

	public TomActivityDto copyActDetails(int activityId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TomActivity tomActivity = activityMapper.selectByPrimaryKey(activityId);
		TomActivityDto tomActivityDto = new TomActivityDto();
		if (null == tomActivity.getCity()) {
			tomActivityDto.setCity("");
		} else {
			tomActivityDto.setCity(tomActivity.getCity());
		}
		if (null == tomActivity.getDepts()) {
			tomActivityDto.setDeptCodes("");
		} else {
			tomActivityDto.setDeptCodes(tomActivity.getDepts());
		}
		if (null == tomActivity.getStaffLevels()) {
			tomActivityDto.setEmployeeGradeStr("");
		} else {
			tomActivityDto.setEmployeeGradeStr(tomActivity.getStaffLevels());
		}
		tomActivityDto.setIntroduction(tomActivity.getIntroduction());
		tomActivityDto.setActivityName(tomActivity.getActivityName());
		tomActivityDto.setProtocol(tomActivity.getProtocol());
		if (tomActivity.getProtocol() != null && "Y".equals(tomActivity.getProtocol())) {
			tomActivityDto.setProtocolStartTime(sdf.format(tomActivity.getProtocolStartTime()));
			tomActivityDto.setProtocolEndTime(sdf.format(tomActivity.getProtocolEndTime()));
			tomActivityDto.setTrainFee(tomActivity.getTrainFee().toString());
		}
		if (tomActivity.getNeedApply() != null && "Y".equals(tomActivity.getNeedApply())) {
			tomActivityDto.setNumberOfParticipants(tomActivity.getNumberOfParticipants().toString());
			tomActivityDto.setApplicationStartTime(null);
			tomActivityDto.setApplicationDeadline(null);
		}

		tomActivityDto.setAdmins(tomActivity.getAdmins());
		tomActivityDto.setActivityImg(tomActivity.getActPicture());
		tomActivityDto.setNeedApply(tomActivity.getNeedApply());
		tomActivityDto.setActivityStartTime(null);
		tomActivityDto.setActivityEndTime(null);

		// 培训活动任务包配置信息
		TomActivityProperty example1 = new TomActivityProperty();
		example1.setActivityId(activityId);
		List<TomActivityProperty> tomActivityProperties = activityPropertyMapper.selectByExample(example1);
		tomActivityDto.setPackageId(tomActivityProperties.get(0).getPackageId());
		List<TomActivityPropertyDto> preTaskInfo = new ArrayList<TomActivityPropertyDto>();
		for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
			TomActivityPropertyDto tomActivityPropertyDto = new TomActivityPropertyDto();
			if (tomActivityProperty.getCourseId() != null) {
				TomCourses course = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
				if (course.getCourseOnline().equals("N")) {
					tomActivityPropertyDto.setLecturerId(tomActivityProperty.getLecturers());
					if (tomActivityProperty.getUnitPrice() != null) {
						tomActivityPropertyDto.setUnitPrice(tomActivityProperty.getUnitPrice().toString());
					} else {
						tomActivityPropertyDto.setUnitPrice("");
					}
					tomActivityPropertyDto.setTotalPrice(tomActivityProperty.getTotalPrice().toString());
					double courseTime = tomActivityProperty.getCourseTime();
					tomActivityPropertyDto.setCourseTime((int) courseTime);
					tomActivityPropertyDto.setCourseAddress(tomActivityProperty.getCourseAddress());
				}
				tomActivityPropertyDto.setTaskId(tomActivityProperty.getCourseId().toString());
				tomActivityPropertyDto.setIsTaskType("Y");
				tomActivityPropertyDto.setCourseId(tomActivityProperty.getCourseId());
				tomActivityPropertyDto.setCourseName(course.getCourseName());

			}
			if (tomActivityProperty.getExamId() != null) {
				TomExamPaper examPaper = examPaperMapper.selectByExamId(tomActivityProperty.getExamId());

				tomActivityPropertyDto.setTaskId(tomActivityProperty.getExamId().toString());
				tomActivityPropertyDto.setIsTaskType("N");
				tomActivityPropertyDto.setExamId(tomActivityProperty.getExamId());
				tomActivityPropertyDto.setExamPaperId(examPaper.getExamPaperId());
				tomActivityPropertyDto.setExamName(tomActivityProperty.getExamName());
				double examTime = tomActivityProperty.getExamTime();
				tomActivityPropertyDto.setExamTime((int) examTime);
				tomActivityPropertyDto.setRetakingExamTimes(tomActivityProperty.getRetakingExamTimes().toString());
				tomActivityPropertyDto.setOfflineExam(tomActivityProperty.getOfflineExam());
				TomExam tomExam = tomExamMapper.selectByPrimaryKey(tomActivityProperty.getExamId());
				tomActivityPropertyDto.setExamAddress(tomExam.getExamAddress());
				tomActivityPropertyDto.setOfflineExam(tomActivityProperty.getOfflineExam());
				// 封装补考
				TomRetakingExam example = new TomRetakingExam();
				example.setExamId(tomActivityProperty.getExamId());
				List<TomRetakingExam> retakingExams = retakingExamMapper.selectListByExample(example);
				List<String> rbeginTime = new ArrayList<String>();
				List<String> rendTime = new ArrayList<String>();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (TomRetakingExam retakingExam : retakingExams) {
					rbeginTime.add(format.format(retakingExam.getRetakingExamBeginTime()));
					rendTime.add(format.format(retakingExam.getRetakingExamEndTime()));
				}
				tomActivityPropertyDto.setRetakingExamBeginTimeList(rbeginTime);
				tomActivityPropertyDto.setRetakingExamEndTimeList(rendTime);
			}
			tomActivityPropertyDto.setStartTime(null);
			tomActivityPropertyDto.setEndTime(null);
			tomActivityPropertyDto.setSort(tomActivityProperty.getSort());
			if (tomActivityProperty.getPreTask() != null) {
				tomActivityPropertyDto.setPretaskId(tomActivityProperty.getPreTask());
				String preNames[] = tomActivityProperty.getPreTask().split(",");
				String preName = "";
				for (int i = 0; i < preNames.length; i++) {
					if (i == 0) {
						preName = preName + "任务" + preNames[i];
					} else {
						preName = preName + ",任务" + preNames[i];
					}
				}
				tomActivityPropertyDto.setPreName(preName);
			}
			preTaskInfo.add(tomActivityPropertyDto);
		}
		tomActivityDto.setPreTaskInfo(preTaskInfo);

		// 培训活动人员信息
		List<TomActivityEmpsRelation> tomActivityEmpsRelations = activityEmpsRelationMapper.selectByActivityIds(activityId);
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		if(tomActivityEmpsRelations.size()>0){
			for (TomActivityEmpsRelation activityEmp : tomActivityEmpsRelations) {
				TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(activityEmp.getCode());
				Map<String, String> empMap = new HashMap<String, String>();
				empMap.put("code", activityEmp.getCode());
				empMap.put("name", tomEmp.getName());
				empMap.put("deptcode", tomEmp.getDeptcode());
				empMap.put("deptname", tomEmp.getDeptname());
				empMap.put("cityname", tomEmp.getCityname());
				empMap.put("deptpsncode", tomEmp.getDeptpsncode());
				emps.add(empMap);
			}
		}
		
		//培训活动部门关联信息
		Map<Object, Object> mapDept = new HashMap<Object, Object>();
		mapDept.put("activityId", activityId);
		mapDept.put("type", "D");
		List<TomActivityOtherRelation> activityOtherRelationDept = tomActivityOtherRelationMapper.selectByList(mapDept);
		if(activityOtherRelationDept.size()>0){
			List<String> listDept = new ArrayList<String>();
			for(TomActivityOtherRelation activityOtherRelation : activityOtherRelationDept){
				Map<String, String> deptMap = new HashMap<String, String>();
				deptMap.put("code", activityOtherRelation.getCode());
				deptMap.put("name", activityOtherRelation.getName());
				deptMap.put("type", "D");
				deptMap.put("statuss", "1");
				emps.add(deptMap);
			}
		}
				
		//培训活动标签关联信息
		Map<Object, Object> mapLabel = new HashMap<Object, Object>();
		mapLabel.put("activityId", activityId);
		mapLabel.put("type", "L");
		List<TomActivityOtherRelation> activityOtherRelationLabel = tomActivityOtherRelationMapper.selectByList(mapLabel);
		if(activityOtherRelationLabel.size()>0){
			for(TomActivityOtherRelation activityOtherRelation : activityOtherRelationLabel){
				Map<String, String> labelMap = new HashMap<String, String>();
				labelMap.put("code", activityOtherRelation.getCode());
				labelMap.put("name", activityOtherRelation.getName());
				labelMap.put("type", "L");
				labelMap.put("statuss", "3");
				emps.add(labelMap);
			}
		}
		
		//培训活动标签人员信息
		Map<Object, Object> mapLabelEmp = new HashMap<Object, Object>();
		mapLabelEmp.put("activityId", activityId);
		mapLabelEmp.put("type", "LE");
		List<TomActivityEmpsRelation> tomActivityEmpsRelationLabelEmp = activityEmpsRelationMapper.selectLabelEmp(mapLabelEmp);
		if(tomActivityEmpsRelationLabelEmp.size()>0){
			for(TomActivityEmpsRelation activityEmpsRelationLabelEmp : tomActivityEmpsRelationLabelEmp){
				TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(activityEmpsRelationLabelEmp.getCode());
				Map<String, String> labelEmpMap = new HashMap<String, String>();
				labelEmpMap.put("code", activityEmpsRelationLabelEmp.getCode());
				labelEmpMap.put("name", tomEmp.getName());
				labelEmpMap.put("type", "LE");
				emps.add(labelEmpMap);
			}
		}
				
		//培训活动标签分类关联信息
		Map<Object, Object> mapLabelClass = new HashMap<Object, Object>();
		mapLabelClass.put("activityId", activityId);
		mapLabelClass.put("type", "C");
		List<TomActivityOtherRelation> activityOtherRelationLabelClass = tomActivityOtherRelationMapper.selectByList(mapLabelClass);
		if(activityOtherRelationLabelClass.size()>0){
			for(TomActivityOtherRelation activityOtherRelation : activityOtherRelationLabelClass){
				Map<String, String> labelClassMap = new HashMap<String, String>();
				labelClassMap.put("code", activityOtherRelation.getCode());
				labelClassMap.put("name", activityOtherRelation.getName());
				labelClassMap.put("type", "C");
				labelClassMap.put("statuss", "1");
				emps.add(labelClassMap);
			}
		}
		
		// 培训活动推送部门负责人信息
		List<TomActivityDept> tomActivityDepts = activityDeptMapper.selectByActivityId(activityId);
		List<Map<String, String>> deptManagers = new ArrayList<Map<String, String>>();
		for (TomActivityDept tomActivityDept : tomActivityDepts) {
			TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(tomActivityDept.getHeaderCode());
			Map<String, String> deptMap = new HashMap<String, String>();
			deptMap.put("code", tomActivityDept.getHeaderCode());
			deptMap.put("name", tomEmp.getName());
			deptMap.put("deptcode", tomEmp.getDeptcode());
			deptMap.put("deptname", tomEmp.getDeptname());
			deptMap.put("cityname", tomEmp.getCityname());
			deptMap.put("deptpsncode", tomEmp.getDeptpsncode());
			deptManagers.add(deptMap);
			// 推送范围
			String codes[] = tomActivityDept.getCode().split(",");
			for (String code : codes) {
				Map<String, String> empMap = new HashMap<String, String>();
				TomEmp tomEmp2 = tomEmpMapper.selectByPrimaryKey(code);
				empMap.put("code", code);
				empMap.put("name", tomEmp2.getName());
				empMap.put("deptcode", tomEmp2.getDeptcode());
				empMap.put("deptname", tomEmp2.getDeptname());
				empMap.put("cityname", tomEmp2.getCityname());
				empMap.put("deptpsncode", tomEmp2.getDeptpsncode());
				emps.add(empMap);
			}
		}
		tomActivityDto.setDeptManagers(deptManagers);
		tomActivityDto.setEmps(emps);
		if (tomActivityDepts.size() > 0) {
			tomActivityDto.setIsPrincipal("Y");
		} else {
			tomActivityDto.setIsPrincipal("N");
		}

		return tomActivityDto;

	}

	/**
	 * 功能描述：[查看培训活动明细]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午10:00:03
	 * 
	 * @param example
	 * @return
	 */
	public TomActivityDto findActivityDetails(int activityId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TomActivity tomActivity = activityMapper.selectByPrimaryKey(activityId);
		TomActivityDto tomActivityDto = new TomActivityDto();
		tomActivityDto.setIntroduction(tomActivity.getIntroduction());
		tomActivityDto.setActivityName(tomActivity.getActivityName());
		tomActivityDto.setProtocol(tomActivity.getProtocol());
		if (null == tomActivity.getCity()) {
			tomActivityDto.setCity("");
		} else {
			tomActivityDto.setCity(tomActivity.getCity());
		}
		if (null == tomActivity.getDepts()) {
			tomActivityDto.setDeptCodes("");
		} else {
			tomActivityDto.setDeptCodes(tomActivity.getDepts());
		}
		if (null == tomActivity.getStaffLevels()) {
			tomActivityDto.setEmployeeGradeStr("");
		} else {
			tomActivityDto.setEmployeeGradeStr(tomActivity.getStaffLevels());
		}
		if (tomActivity.getProtocol() != null && "Y".equals(tomActivity.getProtocol())) {
			tomActivityDto.setProtocolStartTime(sdf.format(tomActivity.getProtocolStartTime()));
			tomActivityDto.setProtocolEndTime(sdf.format(tomActivity.getProtocolEndTime()));
			tomActivityDto.setTrainFee(tomActivity.getTrainFee().toString());
		}
		if (tomActivity.getNeedApply() != null && "Y".equals(tomActivity.getNeedApply())) {
			tomActivityDto.setNumberOfParticipants(tomActivity.getNumberOfParticipants().toString());
			tomActivityDto.setApplicationStartTime(sdf.format(tomActivity.getApplicationStartTime()));
			tomActivityDto.setApplicationDeadline(sdf.format(tomActivity.getApplicationDeadline()));
		}
		tomActivityDto.setAdmins(tomActivity.getAdmins());
		tomActivityDto.setActivityImg(tomActivity.getActPicture());
		tomActivityDto.setNeedApply(tomActivity.getNeedApply());
		tomActivityDto.setActivityStartTime(sdf.format(tomActivity.getActivityStartTime()));
		tomActivityDto.setActivityEndTime(sdf.format(tomActivity.getActivityEndTime()));

		//添加证书相关属性
		tomActivityDto.setCertificateState(tomActivity.getCertificateState());
		tomActivityDto.setCertificateId(tomActivity.getCertificateId());
		TomCertificateDto tomCertificateDto=tomCertificateMapper.findOne(tomActivity.getCertificateId());
		tomActivityDto.setCertificateAddress(tomCertificateDto.getAddress());
		tomActivityDto.setReceiveState(tomActivity.getReceiveState());
		tomActivityDto.setReceiveAddress(tomActivity.getReceiveAddress());
				
		// 培训活动任务包配置信息
		TomActivityProperty example1 = new TomActivityProperty();
		example1.setActivityId(activityId);
		List<TomActivityProperty> tomActivityProperties = activityPropertyMapper.selectByExample(example1);
		tomActivityDto.setPackageId(tomActivityProperties.get(0).getPackageId());
		List<TomActivityPropertyDto> preTaskInfo = new ArrayList<TomActivityPropertyDto>();
		for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
			TomActivityPropertyDto tomActivityPropertyDto = new TomActivityPropertyDto();
			if (tomActivityProperty.getCourseId() != null) {
				TomCourses course = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
				if (course.getCourseOnline().equals("N")) {
					tomActivityPropertyDto.setLecturerId(tomActivityProperty.getLecturers());
					if (null != tomActivityProperty.getLecturers() && !"".equals(tomActivityProperty.getLecturers())) {
						TomLecturer lecture=tomLecturerMapper.selectByPrimaryKey(Integer.valueOf(tomActivityProperty.getLecturers()));
					}
					if (tomActivityProperty.getUnitPrice() != null) {
						tomActivityPropertyDto.setUnitPrice(tomActivityProperty.getUnitPrice().toString());
					} else {
						tomActivityPropertyDto.setUnitPrice("");
					}
					tomActivityPropertyDto.setTotalPrice(tomActivityProperty.getTotalPrice().toString());
					double courseTime = tomActivityProperty.getCourseTime();
					tomActivityPropertyDto.setCourseTime((int) courseTime);
					tomActivityPropertyDto.setCourseAddress(tomActivityProperty.getCourseAddress());
				}
				tomActivityPropertyDto.setTaskId(tomActivityProperty.getCourseId().toString());
				tomActivityPropertyDto.setIsTaskType("Y");
				tomActivityPropertyDto.setCourseId(tomActivityProperty.getCourseId());
				tomActivityPropertyDto.setCourseName(course.getCourseName());
				tomActivityPropertyDto.setGradeTwoDimensionCode(tomActivityProperty.getGradeTwoDimensionCode());
				tomActivityPropertyDto.setSignInTwoDimensionCode(tomActivityProperty.getSignInTwoDimensionCode());
			}
			if (tomActivityProperty.getExamId() != null) {
				TomExamPaper examPaper = examPaperMapper.selectByExamId(tomActivityProperty.getExamId());

				tomActivityPropertyDto.setTaskId(tomActivityProperty.getExamId().toString());
				tomActivityPropertyDto.setIsTaskType("N");
				tomActivityPropertyDto.setExamId(tomActivityProperty.getExamId());
				tomActivityPropertyDto.setExamPaperId(examPaper.getExamPaperId());
				tomActivityPropertyDto.setExamName(tomActivityProperty.getExamName());
				double examTime = tomActivityProperty.getExamTime();
				tomActivityPropertyDto.setExamTime((int) examTime);
				tomActivityPropertyDto.setRetakingExamTimes(tomActivityProperty.getRetakingExamTimes().toString());
				tomActivityPropertyDto.setOfflineExam(tomActivityProperty.getOfflineExam());
				TomExam tomExam = tomExamMapper.selectByPrimaryKey(tomActivityProperty.getExamId());
				tomActivityPropertyDto.setExamAddress(tomExam.getExamAddress());
				tomActivityPropertyDto.setOfflineExam(tomActivityProperty.getOfflineExam());
				// 封装补考
				TomRetakingExam example = new TomRetakingExam();
				example.setExamId(tomActivityProperty.getExamId());
				List<TomRetakingExam> retakingExams = retakingExamMapper.selectListByExample(example);
				List<String> rbeginTime = new ArrayList<String>();
				List<String> rendTime = new ArrayList<String>();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (TomRetakingExam retakingExam : retakingExams) {
					rbeginTime.add(format.format(retakingExam.getRetakingExamBeginTime()));
					rendTime.add(format.format(retakingExam.getRetakingExamEndTime()));
				}
				tomActivityPropertyDto.setRetakingExamBeginTimeList(rbeginTime);
				tomActivityPropertyDto.setRetakingExamEndTimeList(rendTime);
			}
			tomActivityPropertyDto.setStartTime(sdf.format(tomActivityProperty.getStartTime()));
			tomActivityPropertyDto.setEndTime(sdf.format(tomActivityProperty.getEndTime()));
			tomActivityPropertyDto.setSort(tomActivityProperty.getSort());
			if (tomActivityProperty.getPreTask() != null) {
				tomActivityPropertyDto.setPretaskId(tomActivityProperty.getPreTask());
				String preNames[] = tomActivityProperty.getPreTask().split(",");
				String preName = "";
				for (int i = 0; i < preNames.length; i++) {
					if (i == 0) {
						preName = preName + "任务" + preNames[i];
					} else {
						preName = preName + ",任务" + preNames[i];
					}
				}
				tomActivityPropertyDto.setPreName(preName);
			}
			preTaskInfo.add(tomActivityPropertyDto);
		}
		tomActivityDto.setPreTaskInfo(preTaskInfo);

		// 培训活动人员信息
		List<TomActivityEmpsRelation> tomActivityEmpsRelations = activityEmpsRelationMapper.selectByActivityIds(activityId);
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		// List<String> empIds=new ArrayList<String>();
		// List<String> empNames=new ArrayList<String>();
		if(tomActivityEmpsRelations.size()>0){
			for (TomActivityEmpsRelation activityEmp : tomActivityEmpsRelations) {
				TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(activityEmp.getCode());
				Map<String, String> empMap = new HashMap<String, String>();
				empMap.put("code", activityEmp.getCode());
				empMap.put("name", tomEmp.getName());
				empMap.put("deptcode", tomEmp.getDeptcode());
				empMap.put("deptname", tomEmp.getDeptname());
				empMap.put("cityname", tomEmp.getCityname());
				empMap.put("deptpsncode", tomEmp.getDeptpsncode());
				emps.add(empMap);
			}
		}
		
		//培训活动部门关联信息
		Map<Object, Object> mapDept = new HashMap<Object, Object>();
		mapDept.put("activityId", activityId);
		mapDept.put("type", "D");
		List<TomActivityOtherRelation> activityOtherRelationDept = tomActivityOtherRelationMapper.selectByList(mapDept);
		if(activityOtherRelationDept.size()>0){
			for(TomActivityOtherRelation activityOtherRelation : activityOtherRelationDept){
				//TomDept tomDept = tomDeptMapper.selectByPrimaryKey(activityOtherRelation.getCode());
				Map<String, String> deptMap = new HashMap<String, String>();
				deptMap.put("code", activityOtherRelation.getCode());
				deptMap.put("name", activityOtherRelation.getName());
				deptMap.put("type", "D");
				deptMap.put("statuss", "1");
				emps.add(deptMap);
			}
		}
		
		//培训活动标签关联信息
		Map<Object, Object> mapLabel = new HashMap<Object, Object>();
		mapLabel.put("activityId", activityId);
		mapLabel.put("type", "L");
		List<TomActivityOtherRelation> activityOtherRelationLabel = tomActivityOtherRelationMapper.selectByList(mapLabel);
		if(activityOtherRelationLabel.size()>0){
			for(TomActivityOtherRelation activityOtherRelation : activityOtherRelationLabel){
				Map<String, String> labelMap = new HashMap<String, String>();
				labelMap.put("code", activityOtherRelation.getCode());
				labelMap.put("name", activityOtherRelation.getName());
				labelMap.put("type", "L");
				labelMap.put("statuss", "3");
				emps.add(labelMap);
			}
		}
		
		//培训活动标签人员信息
		Map<Object, Object> mapLabelEmp = new HashMap<Object, Object>();
		mapLabelEmp.put("activityId", activityId);
		mapLabelEmp.put("type", "LE");
		List<TomActivityEmpsRelation> tomActivityEmpsRelationLabelEmp = activityEmpsRelationMapper.selectLabelEmp(mapLabelEmp);
		if(tomActivityEmpsRelationLabelEmp.size()>0){
			for(TomActivityEmpsRelation activityEmpsRelationLabelEmp : tomActivityEmpsRelationLabelEmp){
				TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(activityEmpsRelationLabelEmp.getCode());
				Map<String, String> labelEmpMap = new HashMap<String, String>();
				labelEmpMap.put("code", activityEmpsRelationLabelEmp.getCode());
				labelEmpMap.put("name", tomEmp.getName());
				labelEmpMap.put("type", "LE");
				emps.add(labelEmpMap);
			}
		}
		
		//培训活动标签分类关联信息
		Map<Object, Object> mapLabelClass = new HashMap<Object, Object>();
		mapLabelClass.put("activityId", activityId);
		mapLabelClass.put("type", "C");
		List<TomActivityOtherRelation> activityOtherRelationLabelClass = tomActivityOtherRelationMapper.selectByList(mapLabelClass);
		if(activityOtherRelationLabelClass.size()>0){
			for(TomActivityOtherRelation activityOtherRelation : activityOtherRelationLabelClass){
				Map<String, String> labelClassMap = new HashMap<String, String>();
				labelClassMap.put("code", activityOtherRelation.getCode());
				labelClassMap.put("name", activityOtherRelation.getName());
				labelClassMap.put("type", "C");
				labelClassMap.put("statuss", "1");
				emps.add(labelClassMap);
			}
		}

		// 培训活动推送部门负责人信息
		List<TomActivityDept> tomActivityDepts = activityDeptMapper.selectByActivityId(activityId);
		List<Map<String, String>> deptManagers = new ArrayList<Map<String, String>>();
		for (TomActivityDept tomActivityDept : tomActivityDepts) {
			TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(tomActivityDept.getHeaderCode());
			Map<String, String> deptMap = new HashMap<String, String>();
			deptMap.put("code", tomActivityDept.getHeaderCode());
			deptMap.put("name", tomEmp.getName());
			deptMap.put("deptcode", tomEmp.getDeptcode());
			deptMap.put("deptname", tomEmp.getDeptname());
			deptMap.put("cityname", tomEmp.getCityname());
			deptMap.put("deptpsncode", tomEmp.getDeptpsncode());
			deptManagers.add(deptMap);
			// 推送范围
			String codes[] = tomActivityDept.getCode().split(",");
			for (String code : codes) {
				Map<String, String> empMap = new HashMap<String, String>();
				TomEmp tomEmp2 = tomEmpMapper.selectByPrimaryKey(code);
				empMap.put("code", code);
				empMap.put("name", tomEmp2.getName());
				empMap.put("deptcode", tomEmp2.getDeptcode());
				empMap.put("deptname", tomEmp2.getDeptname());
				empMap.put("cityname", tomEmp2.getCityname());
				empMap.put("deptpsncode", tomEmp2.getDeptpsncode());
				emps.add(empMap);
			}
		}
		tomActivityDto.setDeptManagers(deptManagers);
		tomActivityDto.setEmps(emps);

		if (tomActivityDepts.size() > 0) {
			tomActivityDto.setIsPrincipal("Y");
		} else {
			tomActivityDto.setIsPrincipal("N");
		}

		
		return tomActivityDto;

	}

	/**
	 * 功能描述：[查询培训活动列表]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午10:00:07
	 * 
	 * @param example
	 * @return
	 */
	public PageData<TomActivity> selectListByParam(int pageNum, int pageSize, String activityName) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomActivity> page = new PageData<TomActivity>();
		Map<Object, Object> map = Maps.newHashMap();

		/*if (activityName != null) {
			activityName = activityName.replaceAll("/", "//");
			activityName = activityName.replaceAll("%", "/%");
			activityName = activityName.replaceAll("_", "/_");

		}*/
		map.put("activityName", activityName);
		int count = activityMapper.countByList(map);
		if (pageSize == -1) {
			pageSize = count;
		}
		List<TomActivity> list = Lists.newArrayList();
		
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		list = activityMapper.selectListByParam(map);
		//List<TomActivityQr>  qrlist = new ArrayList<TomActivityQr>();
		TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
		for (TomActivity activity : list) {
			activityEmpsRelation.setActivityId(activity.getActivityId());
			activityEmpsRelation.setApplyStatus("Y");
			activity.setNum(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
			TomActivityQr qr =new TomActivityQr();
			qr = activityQrMapper.selectByPrActivityId(activity.getActivityId());
			if(qr!=null){
				activity.setImgpath(activityQrMapper.selectByPrActivityId(activity.getActivityId()).getQrPath());
			}
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;

	}

	/**
	 * 功能描述：[费用统计]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午11:00:36
	 * 
	 * @param example
	 * @return
	 */
	public List<TomActivityFees> findActivityCostList(int activityId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomActivityFeesKey activityFeesKey = new TomActivityFeesKey();
		activityFeesKey.setActivityId(activityId);
		List<TomActivityFees> feesList = activityFeesMapper.selectByPrimaryParam(activityFeesKey);
		return feesList;
	}

	/**
	 * 功能描述：[添加项目花费记录]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 下午2:35:29
	 * 
	 * @param dto
	 * @throws Exception
	 */
	@Transactional
	public void addActivityCost(String jsonStr) {
		ObjectMapper jsonMapper = new ObjectMapper();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray jsonArray = jsonObject.getJSONArray("listFrees");
		Date date = new Date();
		ShiroUser user = ShiroUtils.getCurrentUser();
		for (int i = 0; i < jsonArray.size(); i++) {
			try {
				TomActivityFreesDto freesDto = jsonMapper.readValue(jsonArray.get(i).toString(),
						TomActivityFreesDto.class);
				if (freesDto.getFeeName() == null || "".equals(freesDto.getFeeName()) || freesDto.getFee() == null
						|| "".equals(freesDto.getFee())) {
					continue;
				}
				TomActivityFees frees = new TomActivityFees();
				frees.setActivityId(freesDto.getActivityId());
				frees.setCreateTime(date);
				frees.setCreator(user.getName());
				frees.setFee(freesDto.getFee());
				frees.setRemark(freesDto.getRemark());
				frees.setFeeName(freesDto.getFeeName());
				frees.setStatus("Y");
				frees.setOperator(user.getName());
				frees.setCreatorId(user.getCode());
				frees.setUpdateTime(date);
				activityFeesMapper.insertSelective(frees);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能描述：[删除项目花费记录]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 下午2:35:29
	 * 
	 * @param dto
	 * @throws Exception
	 */
	@Transactional
	public void deleteActivityCost(TomActivityFeesKey dto) {
		//设置主从库查询
	
		TomActivityFees tomActivityFees = activityFeesMapper.selectByPrimaryKey(dto);
		tomActivityFees.setStatus("N");
		//设置主从库查询
		
		activityFeesMapper.updateByPrimaryKeySelective(tomActivityFees);
	}

	public PageData<TomGrpOrgRelation> selectByCode(int pageNum, int pageSize, String grpCode) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomGrpOrgRelation example = new TomGrpOrgRelation();
		PageData<TomGrpOrgRelation> page = new PageData<TomGrpOrgRelation>();
		example.setGrpCode(grpCode);
		int count = tomGrpOrgRelationMapper.countByExample(example);

		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("example", example);
		List<TomGrpOrgRelation> list = tomGrpOrgRelationMapper.selectByCode(map);

		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		for (TomGrpOrgRelation tomGrpOrgRelation : list) {

		}

		return page;

	}

	public PageData<TomEmp> selectByPage(int pageNum, int pageSize, String empcode, String name, String cityname,
			String sex, String code, String statuss, String taskState, String packageId,String country,String begindate) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		// 选择了任务包查询
		PageData<TomEmp> page = new PageData<TomEmp>();
		List<TomEmp> emps = new ArrayList<TomEmp>();
		String ids = "'-1'";
		if (packageId != null && !"".equals(packageId)) {
			List<TomActivityProperty> tomActivityProperties = activityPropertyMapper
					.selectByTaskPackageId(Integer.valueOf(packageId));
			if (tomActivityProperties.size() > 0) {
				Map<String, Integer> finishedMap = new HashMap<String, Integer>();
				for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
					if (tomActivityProperty.getExamId() != null) {
						List<TomExamScore> tomExamScores = tomExamScoreMapper
								.selectByExamId(tomActivityProperty.getExamId());
						for (TomExamScore tomExamScore : tomExamScores) {
							if (tomExamScore.getGradeState().equals("Y")) {
								if (finishedMap.get(tomExamScore.getCode()) == null) {
									finishedMap.put(tomExamScore.getCode(), 0);
								}
								finishedMap.put(tomExamScore.getCode(), finishedMap.get(tomExamScore.getCode()) + 1);
							}
						}
					} else if (tomActivityProperty.getCourseId() != null) {
						TomCourses courses = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
						if (courses.getCourseOnline().equals("Y")) {
							Map<Object, Object> map = new HashMap<Object, Object>();
							map.put("courseId", courses.getCourseId());
							map.put("learningDate", tomActivityProperty.getEndTime());
							List<TomCourseLearningRecord> courseLearningRecords = courseLearningRecordMapper
									.selectLearnRecord(map);
							for (TomCourseLearningRecord courseLearningRecord : courseLearningRecords) {
								if (finishedMap.get(courseLearningRecord.getCode()) == null) {
									finishedMap.put(courseLearningRecord.getCode(), 0);
								}
								finishedMap.put(courseLearningRecord.getCode(),
										finishedMap.get(courseLearningRecord.getCode()) + 1);

							}
						} else {
							TomCourseSignInRecords example = new TomCourseSignInRecords();
							example.setCourseId(courses.getCourseId());
							example.setCreateDate(tomActivityProperty.getEndTime());
							List<TomCourseSignInRecords> courseSignInRecords = courseSignInRecordsMapper
									.selectByExample(example);
							for (TomCourseSignInRecords signInRecords : courseSignInRecords) {
								if (finishedMap.get(signInRecords.getCode()) == null) {
									finishedMap.put(signInRecords.getCode(), 0);
								}
								finishedMap.put(signInRecords.getCode(), finishedMap.get(signInRecords.getCode()) + 1);
							}
						}
					}
				}
				for (Map.Entry<String, Integer> entry : finishedMap.entrySet()) {
					if (entry.getValue() == tomActivityProperties.size()) {
						ids = ids + ",'" + entry.getKey() + "'";
					}
				}
			}
		}

		TomEmp example = new TomEmp();
		Map<Object, Object> map = new HashMap<Object, Object>();
		example.setCode(empcode);
		example.setName(name);
		example.setSex(sex);
		example.setCityname(cityname);
		map.put("example", example);
		map.put("taskState", taskState);
		map.put("ids", ids);
		map.put("statuss", statuss);
		map.put("country", country);
		map.put("begindate", begindate);
		example.setDeptcode(code);
		example.setOrgcode(code);
		int count = tomEmpMapper.countSelectEmp(map);
		if (pageSize == -1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		emps = tomEmpMapper.selectEmp(map);
		page.setDate(emps);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;

	}

	/**
	 * 
	 * 功能描述：[导入活动人员]
	 *
	 * 创建者：JCX 创建时间: 2016年7月5日 下午5:16:18
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public String importEmps(String filePath) throws Exception {
		String path = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if ("xls".equals(path)) {
			String s = readXls(filePath);
			return s;
		} else if ("xlsx".equals(path)) {
			String s = readXlsx(filePath);
			return s;
		}

		return "模板文件类型不正确。";
	}

	/**
	 * 
	 * 功能描述：[读取xls]
	 *
	 * 创建者：JCX 创建时间: 2016年7月5日 下午5:31:09
	 * 
	 * @param filePath
	 * @param examId
	 * @return
	 * @throws IOException
	 */
	@Transactional
	private String readXls(String filePath) throws IOException {
		//设置从库查询
		
		InputStream is = new FileInputStream(filePath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);

		Set<String> codes = new HashSet<String>();
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		List<String> errorCodes = new ArrayList<String>();
		Map<Object, Object> result = new HashMap<Object, Object>();

		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null && hssfRow.getCell(0) != null && !"".equals(hssfRow.getCell(0))) {

					HSSFCell code = hssfRow.getCell(0);
					codes.add(getValue(code));
				}
			}
		}

		
		for (String code : codes) {
			TomEmp emp = empMapper.selectByPrimaryKey(code);
			if (emp != null) {
				Map<String, String> empMap = new HashMap<String, String>();
				empMap.put("code", emp.getCode());
				empMap.put("name", emp.getName());
				empMap.put("deptcode", emp.getDeptcode());
				empMap.put("deptname", emp.getDeptname());
				empMap.put("cityname", emp.getCityname());
				empMap.put("deptpsncode", emp.getDeptpsncode());
				emps.add(empMap);
			} else {
				errorCodes.add(code);
			}
		}

		result.put("emps", emps);
		result.put("errorCodes", errorCodes);
		return mapper.writeValueAsString(result);
	}

	/**
	 * 
	 * 功能描述：[读取xlsx]
	 *
	 * 创建者：JCX 创建时间: 2016年7月5日 下午5:41:10
	 * 
	 * @param filePath
	 * @param examId
	 * @return
	 * @throws IOException
	 */
	@Transactional
	private String readXlsx(String filePath) throws IOException {
		//设置从库查询
		Set<String> codes = new HashSet<String>();
		List<String> errorCodes = new ArrayList<String>();
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		Map<Object, Object> result = new HashMap<Object, Object>();

		InputStream is = new FileInputStream(filePath);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null && xssfRow.getCell(0) != null && !"".equals(xssfRow.getCell(0))) {
					XSSFCell code = xssfRow.getCell(0);
					codes.add(getValue(code));
				}
			}
		}

		for (String code:codes) {
			TomEmp emp = empMapper.selectByPrimaryKey(code);
			if (emp != null && emp.getPoststat().equals("Y")) {
				Map<String, String> empMap = new HashMap<String, String>();
				empMap.put("code", emp.getCode());
				empMap.put("name", emp.getName());
				empMap.put("deptcode", emp.getDeptcode());
				empMap.put("deptname", emp.getDeptname());
				empMap.put("cityname", emp.getCityname());
				empMap.put("deptpsncode", emp.getDeptpsncode());
				emps.add(empMap);
			} else {
				errorCodes.add(code);
			}
		}

		result.put("emps", emps);
		result.put("errorCodes", errorCodes);
		return mapper.writeValueAsString(result);
	}

	/**
	 * 
	 * 功能描述：[类型处理]
	 *
	 * 创建者：wjx 创建时间: 2016年3月8日 下午4:20:16
	 * 
	 * @param xssfRow
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	/**
	 * 
	 * 功能描述：[类型处理]
	 *
	 * 创建者：wjx 创建时间: 2016年3月8日 下午4:20:32
	 * 
	 * @param hssfCell
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
