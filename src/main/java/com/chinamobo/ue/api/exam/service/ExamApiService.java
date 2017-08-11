
package com.chinamobo.ue.api.exam.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomCertificateManageMapper;
import com.chinamobo.ue.activity.dao.TomCertificateMapper;
import com.chinamobo.ue.activity.dto.TomCertificateDto;
import com.chinamobo.ue.activity.dto.TomCertificateManageDto;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.activity.service.CertificateManageService;
import com.chinamobo.ue.api.activity.service.ActivityApiService;
import com.chinamobo.ue.api.exam.RedisData;
import com.chinamobo.ue.api.exam.dto.AnswerResults;
import com.chinamobo.ue.api.exam.dto.EmpAnswer;
import com.chinamobo.ue.api.exam.dto.ExamInfo;
import com.chinamobo.ue.api.exam.dto.ExamSubmitDto;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.result.Resultc;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.cache.cacher.TomExamCacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.cache.redis.RedisCacheManager;
import com.chinamobo.ue.cache.redis.RedisGetData;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamAnswerDetailsMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomJoinExamRecordMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.dto.MyExam;
import com.chinamobo.ue.exam.dto.RandomTopicDto;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamAnswerDetails;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomJoinExamRecord;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.exam.service.ExamScoreService;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.RedisUtils;
import com.chinamobo.ue.utils.SingleQueue;
import com.chinamobo.ue.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 考试接口service
 *
 * 作者: JCX
 * 创建时间: 2016年3月17日 下午2:37:03
 */
@Service
public class ExamApiService{
	
	@Autowired
	private TomExamMapper examMapper;
	
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	
	@Autowired
	private TomTopicMapper topicMapper;
	
	@Autowired
	private TomTopicOptionMapper topicOptionMapper;
	
	@Autowired
	private TomExamQuestionMapper examQuestionMapper;
	
	@Autowired
	private TomExamAnswerDetailsMapper examAnswerDetailsMapper;
	
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private ExamScoreService examScoreService;
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	
	@Autowired
	private TomUserInfoMapper userInfoMapper;
	@Autowired
	private TomEbRecordMapper ebRecordMapper;
	@Autowired
	private ActivityApiService activityApiService;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;//培训活动配置
	@Autowired
	private TomJoinExamRecordMapper joinExamRecordMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	@Autowired
	private TomCertificateManageMapper tomCertificateManageMapper;
	@Autowired
	private TomCertificateMapper tomCertificateMapper;
	@Autowired
	private TomActivityMapper tomActivityMapper;
	@Autowired
	private RedisCacheManager redisCacheManager;
	@Autowired
	private CertificateManageService certificateManageService;
	
	ObjectMapper mapper=new ObjectMapper();
	JsonMapper jsonMapper = new JsonMapper();
	RedisData redisData = new RedisData();
	
	/**
	 * 
	 * 功能描述：[展示考试列表]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月17日 下午2:37:18
	 * @param request
	 * @return
	 */
	public Result eleExamList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		examScoreService.reSetStatus(request.getParameter("userId"));//扫描所有考试将过期未参加的考试设为不合格
		PageData<TomExam> page=new PageData<TomExam>();
		
		String ids="-1";
		if ("N".equals(RedisUtils.getString("concurrency"))) {
			List<TomExamScore> examScores=examScoreMapper.selectbyCode(request.getParameter("userId"));
			for(TomExamScore examScore:examScores){
				if(examScoreService.getRemainingCount(request.getParameter("userId"), examScore.getExamId())<=0){
					ids=ids+","+examScore.getExamId();
				}
			}
		}
		Map<Object,Object> queryMap=new HashMap<Object, Object>();
		TomExam example=new TomExam();
		String keyword=request.getParameter("keyword");
		String userId = request.getParameter("userId");
		example.setExamName(keyword);
		queryMap.put("example",example);
		queryMap.put("userId",userId);
		queryMap.put("ids", ids);
		if(request.getParameter("firstIndex")==null){
			queryMap.put("startNum",0);
		}else{
			queryMap.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			queryMap.put("endNum",10);
			page.setPageSize(10);
		}else{
			queryMap.put("endNum",Integer.parseInt(request.getParameter("pageSize")));//(int)queryMap.get("startNum")+
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		
		int count=examMapper.countByUser(queryMap);
		List<TomExam> list = null;
		/*Object examsObj = redisCacheManager.getCacheData(new RedisGetData("exam", userId));
		if(examsObj != null){//先从缓存获取;
			list = com.alibaba.fastjson.JSONArray.parseArray(examsObj.toString(), TomExam.class);
		}else{*///没有的话，查询数据库，扔进缓存;
			TomExamCacher examCacher = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
			list = examCacher.initUserExams(queryMap);
		//list=examMapper.selectByUser(queryMap);
		for(TomExam exam:list){
			TomExamPaper examPaper=examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
			TomActivityProperty propertyExample=new TomActivityProperty();
			propertyExample.setExamId(exam.getExamId());
			List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(propertyExample);
			String preStatus = activityApiService.getPreStatus(activityProperties, request.getParameter("userId"));
			exam.setPreStatus(preStatus);
			exam.setExamPaperPicture(examPaper.getExamPaperPicture());
			exam.setTestQuestionCount(examPaper.getTestQuestionCount());
			exam.setFullMark(examPaper.getFullMark());
			exam.setPassMark(examPaper.getPassMark());
			exam.setPassEb(examPaper.getPassEb());
			int remainCount = examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId());
			exam.setRemainingCount(remainCount);
			//判断是否开考
			TomRetakingExam retakingExample=new TomRetakingExam();
			retakingExample.setExamId(exam.getExamId());
			if(exam.getRemainingCount()!=0){
				retakingExample.setSort(exam.getRetakingExamCount()+1-exam.getRemainingCount());
			}else{
				retakingExample.setSort(exam.getRetakingExamCount());
			}
			TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
			exam.setBeginTime(retakingExam.getRetakingExamBeginTime());
			exam.setEndTime(retakingExam.getRetakingExamEndTime());
			exam.setBeginTimeS(String.valueOf(retakingExam.getRetakingExamBeginTime().getTime()));
			exam.setEndTimeS(String.valueOf(retakingExam.getRetakingExamEndTime().getTime()));
			TomExamScore scoreExample=new TomExamScore();
			scoreExample.setExamId(exam.getExamId());
			scoreExample.setCode(request.getParameter("userId"));
			TomExamScore examScore=examScoreMapper.selectOneByExample(scoreExample);
			int examCount=examScoreMapper.countByExample(scoreExample);
			if(examCount==2){
				exam.setExamState("未审阅");
			}else if(examCount==1){
				if (examScore.getGradeState().endsWith("N")) {
					int remainingCount = examScoreService.getRemainingCount(request.getParameter("userId"),exam.getExamId());
					if(remainingCount > 0){
						exam.setExamState("未完成");
					}else{
						exam.setExamState("未通过");
					}
				} else if (examScore.getGradeState().endsWith("Y")) {
					exam.setExamState("已通过");
				} else if (examScore.getGradeState().endsWith("D")) {
					exam.setExamState("待考试");
				}
			}
		}
		page.setCount(count);
		page.setDate(list);
		page.setPageNum((int)queryMap.get("startNum")/(int)queryMap.get("endNum")+1);
		
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson=jsonMapper.toJson(page);
		pageJson=pageJson.replaceAll(":null",":\"\"");
		
		return new Result("Y",pageJson,ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[考试详情]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月18日 下午3:15:37
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleExamDetails(HttpServletRequest request,HttpServletResponse response) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String examId = request.getParameter("examId");
		String examRedis = RedisUtils.hget("exam", request.getParameter("examId"));
		TomExam exam = new TomExam();
		if (StringUtil.isNotBlank(examRedis)) {
			JSON.parseObject(examRedis, TomExam.class);
		}else{
			exam=examMapper.selectByPrimaryKey(Integer.valueOf((request.getParameter("examId"))));
		}
		TomExamPaper examPaper = new TomExamPaper();
		//TomExamPaper examPaper=examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
		String examPaperRedis;
		if (null != exam.getExamPaperId()) {
			 examPaperRedis = RedisUtils.hget("tomExamPaper", exam.getExamPaperId().toString());
		}else{
			 examPaperRedis = RedisUtils.hget("tomExamPaper", JSON.parseObject(examRedis, TomExam.class).getExamPaperId().toString());
		}
		
		if (StringUtil.isNotBlank(examPaperRedis)) {
			examPaper = JSON.parseObject(examPaperRedis, TomExamPaper.class);
		}else{
			examPaper = examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
		}
		exam.setTestQuestionCount(examPaper.getTestQuestionCount());
		exam.setFullMark(examPaper.getFullMark());
		if (null != examPaper.getPassMark()) {
			exam.setPassMark(examPaper.getPassMark());
		}
		exam.setExamPaperPicture(examPaper.getExamPaperPicture());
		if (null != examPaper.getPassEb()) {
			exam.setPassEb(examPaper.getPassEb());
		}
		exam.setExamId(JSON.parseObject(examRedis, TomExam.class).getExamId());
		exam.setExamName(JSON.parseObject(examRedis, TomExam.class).getExamName());
		exam.setExamTime(examPaper.getExamTime()*60);
		exam.setOfflineExam(JSON.parseObject(examRedis, TomExam.class).getOfflineExam());
		exam.setRetakingExamCount(JSON.parseObject(examRedis, TomExam.class).getRetakingExamCount());
		exam.setRemainingCount(examScoreService.getRemainingCount(request.getParameter("userId"), Integer.parseInt(request.getParameter("examId"))));
		exam.setShowQualifiedStandard(examPaper.getShowQualifiedStandard());
		//判断是否开考
		TomRetakingExam retakingExample=new TomRetakingExam();
		retakingExample.setExamId(exam.getExamId());
		if(exam.getRemainingCount()!=0){
			retakingExample.setSort(exam.getRetakingExamCount()+1-exam.getRemainingCount());
		}else{
			retakingExample.setSort(exam.getRetakingExamCount());
		}
		//TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
		TomRetakingExam retakingExam = new TomRetakingExam();
		String retakingExamRedis = RedisUtils.hget("retakingExamSort", retakingExample.getExamId() + ":" + retakingExample.getSort());
		if (StringUtil.isNotBlank(retakingExamRedis)) {
			retakingExam = JSON.parseObject(retakingExamRedis, TomRetakingExam.class);
		}else{
			retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
		}
		exam.setBeginTime(retakingExam.getRetakingExamBeginTime());
		exam.setEndTime(retakingExam.getRetakingExamEndTime());
		
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		exam.setBeginTimeS(format.format(exam.getBeginTime()));
		exam.setEndTimeS(format.format(exam.getEndTime()));
		//计算剩余补考次数
//			TomRetakingExam example=new TomRetakingExam();
//			example.setExamId(Integer.parseInt(request.getParameter("examId")));
//			exam.setRemainingCount(0);
//			List<TomRetakingExam> retakingExams=retakingExamMapper.selectListByExample(example);
//			for(TomRetakingExam retakingExam:retakingExams){
//				if(retakingExam.getSort()!=0&&retakingExam.getRetakingExamEndTime().getTime()>new Date().getTime()){
//					exam.setRemainingCount(exam.getRetakingExamCount()-retakingExam.getSort()+1);
//					break;
//				}
//			}
		exam.setEmpIds(null);
		exam.setEmpNames(null);
		String json=mapper.writeValueAsString(exam);
		
//			json=json.replaceAll(String.valueOf(exam.getBeginTime().getTime()),"\""+format.format(exam.getBeginTime())+"\"");
//			json=json.replaceAll(String.valueOf(exam.getEndTime().getTime()),"\""+format.format(exam.getEndTime())+"\"");
		json=json.replaceAll(":null",":\"\"");
		
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}
	
	/**
	 * 
	 * 功能描述：[获取试卷]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月18日 下午3:15:48
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public Result eleExamTopicList(HttpServletRequest request,HttpServletResponse response) throws Exception{
		ExamInfo examInfo=new ExamInfo();
		String examRedis = RedisUtils.hget("exam", request.getParameter("examId"));
		//TomExam exam=examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId")));
		TomExam exam = JSON.parseObject(examRedis, TomExam.class);
		
		//判断是否开考
		exam.setRemainingCount(examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));
		TomRetakingExam retakingExample=new TomRetakingExam();
		retakingExample.setExamId(exam.getExamId());
		if(exam.getRemainingCount()!=0){
			retakingExample.setSort(exam.getRetakingExamCount()+1-exam.getRemainingCount());
		}else{
			return new Result("N","",ErrorCode.API_OVERLOAD, "不在考试时间内。"); 
		}
		//TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
		TomRetakingExam retakingExam = new TomRetakingExam();
		String retakingExamRedis = RedisUtils.hget("retakingExamSort", retakingExample.getExamId() + ":" + retakingExample.getSort());
		retakingExam = JSON.parseObject(retakingExamRedis, TomRetakingExam.class);
		Date nowDate=new Date();
		if(nowDate.getTime()<retakingExam.getRetakingExamBeginTime().getTime()||nowDate.getTime()>retakingExam.getRetakingExamEndTime().getTime()){
			return new Result("N","",ErrorCode.API_OVERLOAD, "不在考试时间内。"); 
		}
		
		//TomExamPaper examPaper=examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
		TomExamPaper examPaper = new TomExamPaper();
		String examPaperRedis = RedisUtils.hget("tomExamPaper", exam.getExamPaperId().toString());
		examPaper = JSON.parseObject(examPaperRedis, TomExamPaper.class);
		examInfo.setExamTime(examPaper.getExamTime()*60);
		examInfo.setTestQuestionCount(examPaper.getTestQuestionCount());
		if(examPaper.getExamPaperType().equals("1")||examPaper.getExamPaperType().equals("3")||examPaper.getExamPaperType().equals("5")){
			//List<TomTopic> topics=topicMapper.selectByExamPaperId(examPaper.getExamPaperId());
			/* start 读取redis缓存试题 */
			ArrayList<TomTopic> topics = new ArrayList<TomTopic>();
			String redisString = RedisUtils.hget("examPaperTopic", examPaper.getExamPaperId().toString());
			JSONArray data = JSONArray.fromObject(redisString);
			/*for(TomTopic topic:topics){
				List<TomTopicOption> topicOptions=topicOptionMapper.selectByTopicIdNoStatus(topic.getTopicId());
				if(topicOptions!=null){
					topic.setTopicOptions(topicOptions);
				}
			}*/
			for (int i = 0; i < data.size(); i++) {
				topics.add(JSON.parseObject(data.get(i).toString(), TomTopic.class));
			}
			/* end 读取redis缓存 */
			examInfo.setTopics(topics);
		}else if(examPaper.getExamPaperType().equals("2")||examPaper.getExamPaperType().equals("4")||examPaper.getExamPaperType().equals("6")){
			//List<TomExamQuestion> examQuestions=sort(examQuestionMapper.selectByExamPaperId(examPaper.getExamPaperId()));
			/* start 读取redis缓存试题 */
			String examQuestion = RedisUtils.hget("randomExamPaper", examPaper.getExamPaperId().toString());
			List<TomTopic> topics=new ArrayList<TomTopic>();
			/*for(TomExamQuestion examQuestion:examQuestions){
				List<TomTopic> topicsPart=randomTopics(examQuestion);
				topics.addAll(topicsPart);
			}*/
			if (StringUtil.isNotBlank(examQuestion)) {
				JSONArray data = JSONArray.fromObject(examQuestion);
				for (int i = 0; i < data.size(); i++) {
					RandomTopicDto dto = JSON.parseObject(data.get(i).toString(), RandomTopicDto.class);
					topics.addAll(randomRedis(dto));
				}
			}else{
				List<TomExamQuestion> examQuestions = sort(examQuestionMapper.selectByExamPaperId(examPaper.getExamPaperId()));
				for (TomExamQuestion tomExamQuestion : examQuestions) {
					List<TomTopic> randomTopics = randomTopics(tomExamQuestion);
					topics.addAll(randomTopics);
				}
				for (TomTopic topic : topics) {
					List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicIdNoStatus(topic.getTopicId());
					if (topicOptions != null) {
						topic.setTopicOptions(topicOptions);
					}
				}
			}
			examInfo.setTopics(topics);
		}
//			TomExamAnswerDetails answerDetails;
//			for(TomTopic topic:examInfo.getTopics()){
//				answerDetails=new TomExamAnswerDetails();
//				answerDetails.setCode(request.getParameter("userId"));
//				answerDetails.setExamId(Integer.parseInt(request.getParameter("examId")));
//				answerDetails.setTopicId(topic.getTopicId());
//				answerDetails.setTopic(topic.getTopicName());
//				answerDetails.setScore(0);
//				answerDetails.setCreateTime(new Date());
//				answerDetails.setStatus("Y");
//				answerDetails.setRightState("N");
//				examAnswerDetailsMapper.insertSelective(answerDetails);
//			}
		String json=mapper.writeValueAsString(examInfo);
		json=json.replaceAll(":null",":\"\"");
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}
	
	public List<TomTopic> randomRedis(RandomTopicDto dto) {
		List<TomTopic> list = new ArrayList<TomTopic>();
		List<TomTopic> topics = dto.getTopicList();
		for (int i = 0; i < dto.getCount(); i++) {
			int index = (int) (Math.random() * topics.size());
			list.add(topics.get(index));
			topics.remove(index);
		}
		return list;
	}
	
	/**
	 * 
	 * 功能描述：[提交考卷系统并对非主观题打分]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月22日 下午1:49:17
	 * @param json
	 * @return
	 */
	@Transactional
	public Result eleExamSubmit(String json) throws Exception{
		
		JSONObject jsonObject=JSONObject.fromObject(json);
		AnswerResults answerResults=new AnswerResults();
		answerResults.setExamId(jsonObject.getInt("examId"));
		answerResults.setUserId(jsonObject.getString("userId"));
		answerResults.setExamTotalTime(jsonObject.getInt("examTotalTime"));
		TomExamScore examScore=new TomExamScore();
		//判断是否开考
		//TomExam exam=examMapper.selectByPrimaryKey(jsonObject.getInt("examId"));
		//从缓存中读取考试数据
		TomExam exam = redisData.getExamFromRedis(jsonObject.getInt("examId") + "");
		int remainingCount=examScoreService.getRemainingCount(jsonObject.getString("userId"),jsonObject.getInt("examId"));
		TomRetakingExam retakingExample=new TomRetakingExam();
		retakingExample.setExamId(jsonObject.getInt("examId"));
		if(remainingCount!=0){
			retakingExample.setSort(exam.getRetakingExamCount()+1-remainingCount);
		}else{
			//return new Result("N","",ErrorCode.API_OVERLOAD, "不在考试时间内。"); 
			retakingExample.setSort(exam.getRetakingExamCount());
		}
		//TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
		//从缓存中查询补考信息
		TomRetakingExam retakingExam = redisData.getRetakingExamFromRedis(retakingExample.getExamId() + "", retakingExample.getSort() + "");
		Date nowDate=new Date();
		if(nowDate.getTime()<retakingExam.getRetakingExamBeginTime().getTime()||nowDate.getTime()>retakingExam.getRetakingExamEndTime().getTime()){
			return new Result("N","",ErrorCode.API_OVERLOAD, "不在考试时间内。"); 
		}
		//返回补考时间begin
		String retakingExamRedis = RedisUtils.hget("retakingExamSort", retakingExample.getExamId() + ":" + (retakingExample.getSort()+1));
		if (StringUtil.isNotBlank(retakingExamRedis)) {
			retakingExam = jsonMapper.fromJson(retakingExamRedis, TomRetakingExam.class);
		}else{
			retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
		}
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		examScore.setRetakingExamBeginTime(format.format(retakingExam.getRetakingExamBeginTime()));
		//返回补考时间end
		//AnswerResults answerResults= mapper.readValue(json, AnswerResults.class);
		//TomExamPaper examPaper =examPaperMapper.selectByPrimaryKey(examMapper.selectByPrimaryKey(answerResults.getExamId()).getExamPaperId());
		//从缓存中查询试卷
		TomExam hg = redisData.getExamFromRedis(answerResults.getExamId().toString());
		TomExamPaper examPaper = redisData.getExamPaperFromRedis( hg.getExamPaperId().toString());
		TomExamScore scoreExample=new TomExamScore();
		scoreExample.setExamId(answerResults.getExamId());
		scoreExample.setCode(answerResults.getUserId());
		//TomExamScore LastexamScore=examScoreMapper.selectOneByExample(scoreExample);
		//从缓存中查询已经考过的考试（不是为审阅的）
		TomExamScore lastExamScore = redisData.getExamScoreNotPFromRedis(scoreExample.getExamId() + "", scoreExample.getCode());
		List<TomExamAnswerDetails> list=new ArrayList<TomExamAnswerDetails>();
		List<EmpAnswer> empAnswers=new ArrayList<EmpAnswer>();
		EmpAnswer empAnswer1;
		for(int i=0;i<jsonObject.getJSONArray("examResult").size();i++){
			empAnswer1= mapper.readValue(jsonObject.getJSONArray("examResult").get(i).toString(), EmpAnswer.class); 
			empAnswers.add(empAnswer1);
		}
		answerResults.setEmpAnswers(empAnswers);

		examScore.setExamId(answerResults.getExamId());
		examScore.setCode(answerResults.getUserId());
		examScore.setExamTotalTime(answerResults.getExamTotalTime());
		//从缓存中获取用户名
		//examScore.setEmpName(LastexamScore.getEmpName());
		TomUserLog userLog = redisData.getUserLogFromRedis(answerResults.getUserId());
		examScore.setEmpName(userLog.getName());
		int totalPoints=0;
		int rightNumbe=0;
		int wrongNumber=0;
		Boolean flag=false;
		
		for(EmpAnswer empAnswer:answerResults.getEmpAnswers()){
			TomExamAnswerDetails examAnswerDetails=new TomExamAnswerDetails();
			examAnswerDetails.setExamId(answerResults.getExamId());//封装考试id
			examAnswerDetails.setCode(answerResults.getUserId());//封装员工id
			examAnswerDetails.setCreateTime(new Date());//封装创建时间
			
			//TomTopic topic=topicMapper.selectByPrimaryKey(empAnswer.getTopicId());
			//从缓存中查询题目
			TomTopic topic = redisData.getTopicFromRedis(empAnswer.getTopicId().toString());
			if(topic.getQuestionType().equals("1")||topic.getQuestionType().equals("5")||topic.getQuestionType().equals("2")){
				if("0".equals(empAnswer.getOptionId())){
					empAnswer.setOptionId("");
				}
				String answers[]=empAnswer.getOptionId().split(",");
				//List<TomTopicOption> rightOptions = topicOptionMapper.selectRightOption(topic.getTopicId());
				//从缓存中查询正确选项
				List<TomTopicOption> rightOptions = redisData.getRightOptionsFromRedis(topic.getTopicId().toString());
				boolean isRight=true;
				if (empAnswer.getOptionId()!=null&&!"".equals(empAnswer.getOptionId())&&rightOptions.size()==answers.length) {
					for (String s : answers) {
						//TomTopicOption topicOption = topicOptionMapper.selectByPrimaryKey(Integer.parseInt(s));
						//从缓存中查询题目选项
						TomTopicOption topicOption = redisData.getOneOptionFromRedis(topic.getTopicId().toString(), s);
						if (topicOption != null&& topicOption.getRight().equals("N")) {
							isRight = false;
						}
					}
				}else{
					isRight=false;
				}
				/*
				TomExamQuestion example=new TomExamQuestion();
				example.setExamPaperId(examPaper.getExamPaperId());
				example.setQuestionType(topic.getQuestionType());
				TomExamQuestion examQuestion=examQuestionMapper.selectByExample(example);*/
				//从缓存中获取试卷题库关联信息
				TomExamQuestion examQuestion = redisData.getExamQuestionFromRedis(examPaper.getExamPaperId().toString(), topic.getQuestionBankId().toString(), topic.getQuestionType());
				examAnswerDetails.setEmpAnswer(empAnswer.getOptionId());//封装非主观题学员答案
				examAnswerDetails.setSubjectiveItemAnswer("");//封装主观题学员答案
				
				if(isRight){
					examAnswerDetails.setScore(examQuestion.getScore());//封装得分
					examAnswerDetails.setRightState("Y");//封装正确状态
					totalPoints+=examQuestion.getScore();
					rightNumbe+=1;
				}else{
					examAnswerDetails.setScore(0);//封装得分
					examAnswerDetails.setRightState("N");//封装正确状态
					wrongNumber+=1;
				}
			}else{
				flag=true;
				examAnswerDetails.setEmpAnswer("");//封装非主观题学员答案
				examAnswerDetails.setSubjectiveItemAnswer(empAnswer.getOptionId());//封装主观题学员答案
				examAnswerDetails.setScore(0);
			}
			examAnswerDetails.setTopicId(empAnswer.getTopicId());//封装题目id
			examAnswerDetails.setTopic(topic.getTopicName());//封装题目名称
			list.add(examAnswerDetails);
//				examAnswerDetailsMapper.insertSelective(examAnswerDetails);
		}

		examScore.setRightNumbe(rightNumbe);
		examScore.setWrongNumber(wrongNumber);
		examScore.setAccuracy((double)rightNumbe/(double)examPaper.getTestQuestionCount());
		examScore.setTotalPoints(totalPoints);
		examScore.setCreateTime(new Date());
		examScore.setShowScore(examPaper.getShowScore());
		examScore.setRetakingExamCount(String.valueOf(examScoreService.getRemainingCount(scoreExample.getCode(), scoreExample.getExamId())-1));
		
		ExamSubmitDto examSubmitDto = new ExamSubmitDto();
		examSubmitDto.setFlag(flag);
		examSubmitDto.setExistsLastExam(lastExamScore != null);
		if(flag){
			examScore.setGradeState("P");
			examScore.setExamCount(lastExamScore.getExamCount()+1);
			
			examSubmitDto.setTomExamScore(examScore);
			examSubmitDto.setAnswerDetailList(list);
			/*examScoreMapper.insertSelective(examScore);//插入考试成绩
			for(TomExamAnswerDetails examAnswerDetails:list){
				examAnswerDetails.setStatus("N");
				examAnswerDetailsMapper.insertSelective(examAnswerDetails);
			}*/
		}else{
			if(examScore.getTotalPoints()>=examPaper.getPassMark()){
				examScore.setGradeState("Y");
			}else{
				examScore.setGradeState("N");
			}
			if (lastExamScore != null) {
			//当前考试与上一次考试得分比较
			if(lastExamScore.getTotalPoints()>examScore.getTotalPoints()){// 当前考试得分低
				lastExamScore.setExamCount(lastExamScore.getExamCount()+1);
				lastExamScore.setCreateTime(examScore.getCreateTime());
				
				//examScoreMapper.updateSelective(lastExamScore);
				examSubmitDto.setTomExamScore(lastExamScore);
				examSubmitDto.setPointCompareResult(1);
			}else if(lastExamScore.getTotalPoints()<examScore.getTotalPoints()){
				examScore.setExamCount(lastExamScore.getExamCount() + 1);
				examSubmitDto.setPointCompareResult(-1);
				examSubmitDto.setAnswerDetailList(list);
				examSubmitDto.setTomExamScore(examScore);
				TomExamAnswerDetails answerDetails=new TomExamAnswerDetails();
				answerDetails.setExamId(answerResults.getExamId());
				answerDetails.setCode(answerResults.getUserId());
				examSubmitDto.setDeletingAnswerDetails(answerDetails);
				examSubmitDto.setAnswerDetailList(list);
				/*examAnswerDetailsMapper.deleteByExample(answerDetails);
				for(TomExamAnswerDetails examAnswerDetails:list){
					examAnswerDetails.setStatus("Y");
					examAnswerDetailsMapper.insertSelective(examAnswerDetails);
				}
				examScore.setExamCount(lastExamScore.getExamCount()+1);
				examScoreMapper.updateSelective(examScore);*/
			}else{
				examSubmitDto.setPointCompareResult(0);
				if(lastExamScore.getExamTotalTime()<examScore.getExamTotalTime()&&!lastExamScore.getGradeState().equals("D")){
					examSubmitDto.setTimeCompareResult(1);
					lastExamScore.setExamCount(lastExamScore.getExamCount()+1);
					lastExamScore.setCreateTime(examScore.getCreateTime());
					examSubmitDto.setTomExamScore(lastExamScore);
					//examScoreMapper.updateSelective(lastExamScore);
				}else{
					examScore.setExamCount(lastExamScore.getExamCount() + 1);
					examSubmitDto.setTimeCompareResult(-1);
					TomExamAnswerDetails answerDetails=new TomExamAnswerDetails();
					answerDetails.setExamId(answerResults.getExamId());
					answerDetails.setCode(answerResults.getUserId());
					examSubmitDto.setDeletingAnswerDetails(answerDetails);
					examSubmitDto.setAnswerDetailList(list);
					examSubmitDto.setTomExamScore(examScore);
					/*examAnswerDetailsMapper.deleteByExample(answerDetails);
					for(TomExamAnswerDetails examAnswerDetails:list){
						examAnswerDetails.setStatus("Y");
						examAnswerDetailsMapper.insertSelective(examAnswerDetails);
					}
					examScore.setExamCount(lastExamScore.getExamCount()+1);
					examScoreMapper.updateSelective(examScore);*/
				}
			}
			}else{
				examScore.setExamCount(1);
				examSubmitDto.setAnswerDetailList(list);
				examSubmitDto.setTomExamScore(examScore);
			}
//				TomExamScore scoreExample1=new TomExamScore();
//				scoreExample1.setCode(answerResults.getUserId());
//				scoreExample1.setExamId(answerResults.getExamId());
//				TomExamScore examScore2=examScoreMapper.selectOneByExample(scoreExample1);
			//TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(answerResults.getUserId());
			//根据考试答题结果查询用户
			TomUserInfo userInfo = redisData.getUserInfoFromRedis(answerResults.getUserId());
			TomEbRecord ebRecord=new TomEbRecord();
			ebRecord.setCode(answerResults.getUserId());
			ebRecord.setUpdateTime(new Date());
			if(examScore.getGradeState().equals("N")&&examScoreService.getRemainingCount(answerResults.getUserId(), answerResults.getExamId())<=1){
				// 不及格且剩余补考次数小于等于1时(为1时, 是当前考试), 扣除e币
				examSubmitDto.setScoreResult(0);

				if (null != examPaper.getNotPassEb()) {// 后台设置的扣除的e币不为空时
					examSubmitDto.setReduceECoin(true);
					if (userInfo.geteNumber() >= examPaper.getNotPassEb()) {// 用户当前e币数量
																			// 大于
																			// 应扣除e币时,
																			// 直接扣除
						userInfo.seteNumber(userInfo.geteNumber() - examPaper.getNotPassEb());
					} else {// 用户当前e币数量 小于或等于 应扣除e币时, 直接将e币数量置0
						userInfo.seteNumber(0);
					}
					ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
					ebRecord.setRoad("9");

					examSubmitDto.setUserInfo(userInfo);
					examSubmitDto.setEbRecord(ebRecord);
					// userInfoMapper.updateByCode(userInfo);// 更新用户信息
					// ebRecordMapper.insertSelective(ebRecord);// 插入e币记录
				}
			}else if(examScore.getGradeState().equals("Y")){
//				Map<Object, Object> map1 = new HashMap<Object, Object>();
//				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//				map1.put("userId", jsonObject.getString("userId"));
//				map1.put("road", "3");
//				map1.put("date", simple.format(new Date()));
//				List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map1);
//				int ebCount=0;
//				for(TomEbRecord er:ebRList){
//					ebCount+=er.getExchangeNumber();
//				}
//				if(ebCount+examPaper.getPassEb()<=100){
				if (null != examPaper.getPassEb()) {// 后台设置了考试及格奖励e币时
					examSubmitDto.setReduceECoin(true);

					userInfo.seteNumber(userInfo.geteNumber() + examPaper.getPassEb());
					userInfo.setAddUpENumber(userInfo.getAddUpENumber() + examPaper.getPassEb());
					ebRecord.setExchangeNumber(examPaper.getPassEb());
					ebRecord.setRoad("3");

					examSubmitDto.setUserInfo(userInfo);
					examSubmitDto.setEbRecord(ebRecord);
					// userInfoMapper.updateByCode(userInfo);// 更新用户信息
					// ebRecordMapper.insertSelective(ebRecord);// 插入e币记录
				}
//				}else{
//					userInfo.seteNumber(userInfo.geteNumber()+100-ebCount);
//					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+100-ebCount);
//					ebRecord.setExchangeNumber(100-ebCount);
//					ebRecord.setRoad("3");
//					userInfoMapper.updateByCode(userInfo);
//					ebRecordMapper.insertSelective(ebRecord);
//				}
			}else {
				examSubmitDto.setScoreResult(2);
			}
		}
		if(flag){
			examPaper.setImmediatelyShow("N");
		}
		
		//插入考试记录
		TomJoinExamRecord joinExamRecord=new TomJoinExamRecord();
		joinExamRecord.setCode(jsonObject.getString("userId"));
		joinExamRecord.setExamid(jsonObject.getInt("examId"));
		joinExamRecord.setCreateTime(new Date());
		//joinExamRecordMapper.insertSelective(joinExamRecord);
		examSubmitDto.setTomJoinExamRecord(joinExamRecord);
		// joinExamRecordMapper.insertSelective(joinExamRecord);
		// 将数据库操作对象放到静态数组中
		ArrayBlockingQueue<ExamSubmitDto> queue = SingleQueue.getInstance();
		queue.put(examSubmitDto);

		// 先判断是否马上显示考试成绩
		String resultJson = examPaper.getImmediatelyShow();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", resultJson);
		if ("Y".equals(resultJson)) {// 马上显示时, 则将考试成绩返回, 否则返回N
			resultMap.put("data", examScore);
		}
		//return new Result("Y",mapper.writeValueAsString(examPaper.getImmediatelyShow()),ErrorCode.SUCCESS_CODE, "");
		return new Result("Y",JSON.toJSONString(resultMap),ErrorCode.SUCCESS_CODE, "");
	}
	
	/**
	 * 
	 * @Title: executeDataQueue 
	 * @Description: 从队列中取数据, 执行数据库操作, 更新/删除/插入 
	 * @author Acemon 
	 * @date 2017年5月22日 下午1:46:50
	 * @return void
	 */
	@Transactional
	public void executeDataQueue(ExamSubmitDto examSubmitDto) throws InterruptedException {
		boolean confer = false;
		String certificateJson = "";
		//先判断有无考试次数, 以去除重复提交的无效数据
		String userId = examSubmitDto.getTomExamScore().getCode();
		Integer examId = examSubmitDto.getTomExamScore().getExamId();
		Date createTime = examSubmitDto.getTomExamScore().getCreateTime();
		boolean canExecute = examScoreService.canExecute(userId, examId, createTime);
		//设置主从库查询
		if (canExecute) {
			if (examSubmitDto.isFlag()) {// 未审阅
				examScoreMapper.insertSelective(examSubmitDto.getTomExamScore());// 插入考试成绩
				for (TomExamAnswerDetails examAnswerDetails : examSubmitDto.getAnswerDetailList()) {
					examAnswerDetails.setStatus("N");// 批阅状态设置为N未批完
					examAnswerDetailsMapper.insertSelective(examAnswerDetails);// 插入考试答题详情
				}
			} else {// 已审阅
				if (examSubmitDto.isExistsLastExam()) {// 存在上一次考试
					if (examSubmitDto.getPointCompareResult() == 1) {// 当前考试得分低
						examScoreMapper.updateSelective(examSubmitDto.getTomExamScore());
					} else if (examSubmitDto.getPointCompareResult() == -1) {// 当前考试得分高
						examAnswerDetailsMapper.deleteByExample(examSubmitDto.getDeletingAnswerDetails());// 删除上一次考试答题详情
						for (TomExamAnswerDetails examAnswerDetails : examSubmitDto.getAnswerDetailList()) {
							examAnswerDetails.setStatus("Y");
							examAnswerDetailsMapper.insertSelective(examAnswerDetails);// 插入本次考试答题详情
						}
						examScoreMapper.updateSelective(examSubmitDto.getTomExamScore());
					} else {
						if (examSubmitDto.getTimeCompareResult() == 1) {// 当前考试用时较长
							examScoreMapper.updateSelective(examSubmitDto.getTomExamScore());
						} else {
							examAnswerDetailsMapper.deleteByExample(examSubmitDto.getDeletingAnswerDetails());// 删除考试答题详情
							for (TomExamAnswerDetails examAnswerDetails : examSubmitDto.getAnswerDetailList()) {
								examAnswerDetails.setStatus("Y");
								examAnswerDetailsMapper.insertSelective(examAnswerDetails);// 插入考试答题详情
							}
							examScoreMapper.updateSelective(examSubmitDto.getTomExamScore());
						}
					}
				} else {// 不存在上一次考试
					examScoreMapper.updateSelective(examSubmitDto.getTomExamScore());
				}
				if (null != examSubmitDto.getScoreResult()) {
					if (examSubmitDto.getScoreResult() == 0 || examSubmitDto.getScoreResult() == 1) {// 不及格或及格
						if (examSubmitDto.isReduceECoin()) {// 后台设置的扣除的e币不为空
							userInfoMapper.updateByCode(examSubmitDto.getUserInfo());// 更新用户信息
							RedisUtils.hset("userInfo", examSubmitDto.getUserInfo().getCode(),
									JSON.toJSONString(examSubmitDto.getUserInfo()));

							ebRecordMapper.insertSelective(examSubmitDto.getEbRecord());// 插入e币记录
						}
					}
				}
				
			}

			joinExamRecordMapper.insertSelective(examSubmitDto.getTomJoinExamRecord());

			RedisUtils.hset("examScore",
					examSubmitDto.getTomExamScore().getExamId() + ":" + examSubmitDto.getTomExamScore().getCode() + ":"
							+ examSubmitDto.getTomExamScore().getGradeState(),
					JSON.toJSONString(examSubmitDto.getTomExamScore()));
			//扫描当前活动下任务完成进度begin
			Map<Object, Object> map = new HashMap<>();
			TomActivityProperty activityPropertyExample = new TomActivityProperty();
			TomExamScore scoreExamples = new TomExamScore();
			map.put("examId", examId);
			map.put("code", userId);
			TomExamScore examScore = examScoreMapper.selectByExamCode(map);
			TomExam exam = examMapper.selectByPrimaryKey(examId);
			if ("A".equals(exam.getExamType()) && "Y".equals(examScore.getGradeState())) {
				TomActivityProperty activityProperty = activityPropertyMapper.selectByExamId(exam.getExamId());
				activityPropertyExample.setActivityId(activityProperty.getActivityId());
				TomActivity tomActivity = tomActivityMapper.selectByPrimaryKey(activityProperty.getActivityId());
				if ("Y".equals(tomActivity.getCertificateState())) {//判断当前活动是不是有证书
					List<TomActivityProperty> example = activityPropertyMapper.selectByExample(activityPropertyExample);
					for (TomActivityProperty tomActivityProperty : example) {
						if (null != tomActivityProperty.getExamId()) {//考试
							scoreExamples.setCode(userId);
							scoreExamples.setExamId(tomActivityProperty.getExamId());
							TomExamScore tomExamScores = examScoreMapper.selectOneByExample(scoreExamples);
							if (tomExamScores.getGradeState().equals("Y")) {
								confer = true;
							}else{
								confer = false;
								break;
							}
						}
						if (null != tomActivityProperty.getCourseId()) {
							TomCourses tomCourses = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
							if (tomCourses.getCourseOnline().equals("Y")) {//线上课程
								TomCourseLearningRecord courseLearningRecordsExample = new TomCourseLearningRecord();
								courseLearningRecordsExample.setCode(userId);
								courseLearningRecordsExample.setCourseId(tomCourses.getCourseId());
								courseLearningRecordsExample.setLearningDate(tomActivityProperty.getEndTime());
								if (courseLearningRecordMapper.countByExample(courseLearningRecordsExample) > 0) {
									confer = true;
								}else{
									confer = false;
									break;
								}
							}else{//线下课程
								TomCourseSignInRecords signInRecordsExample = new TomCourseSignInRecords();
								signInRecordsExample.setCode(userId);
								signInRecordsExample.setCourseId(tomCourses.getCourseId());
								signInRecordsExample.setCreateDate(tomActivityProperty.getEndTime());
								if (courseSignInRecordsMapper.countByExample(signInRecordsExample) > 0) {
									confer = true;
								}else{
									confer = false;
									break;
								}
							}
						}
					}
					if (confer == true) {//完成活动下所有任务，发证书
						JSONObject jsonObject2 = new JSONObject();
						jsonObject2.put("code", userId);
						jsonObject2.put("activityId", activityProperty.getActivityId());
						TomActivity activity = tomActivityMapper.selectByPrimaryKey(activityProperty.getActivityId());
						jsonObject2.put("certificateId", activity.getCertificateId());
						Map<Object,Object> map1 = (Map<Object, Object>) jsonObject2.toBean(jsonObject2, Map.class);
						if (tomCertificateManageMapper.countByCodeAct(map1) < 1) {
							certificateManageService.insert(jsonObject2.toString());
						}
						TomCertificateDto tomCertificateDto = tomCertificateMapper.selectByAct(activityProperty.getActivityId());
						try {
							certificateJson = mapper.writeValueAsString(tomCertificateDto);
							certificateJson = certificateJson.replaceAll(":null",":\"\"");
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						/*if (null != tomCertificateDto) {
							TomCertificateManageDto tomCertificateManageDto= new TomCertificateManageDto();
							tomCertificateManageDto.setActivityId(activityProperty.getActivityId());
							tomCertificateManageDto.setCertificateAddress(tomCertificateDto.getAddress());
							tomCertificateManageDto.setAfourCertificateAddress(tomCertificateDto.getAfourAddress());
							tomCertificateManageDto.setCertificateId(tomCertificateDto.getId());
							tomCertificateManageDto.setCode(userId);
							tomCertificateManageDto.setCreateTime(new Date());
							if (tomCertificateManageMapper.countByCodeAct(map) < 1) {
								DBContextHolder.setDbType(DBContextHolder.MASTER);
								tomCertificateManageMapper.insert(tomCertificateManageDto);
							}
							try {
								certificateJson = mapper.writeValueAsString(tomCertificateDto);
								certificateJson = certificateJson.replaceAll(":null",":\"\"");
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
						}*/
					}
				}
			}
			//扫描当前活动下任务完成进度end
		}else {
			System.out.println("重复提交, userId:" + examSubmitDto.getTomExamScore().getCode() + ", examId:"+examSubmitDto.getTomExamScore().getExamId());
		}
	}
	
	/**
	 * 
	 * 功能描述：[考试结果]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月31日 下午4:06:58
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleExamResult(HttpServletRequest request,HttpServletResponse response) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		boolean confer = false;
		String certificateJson = "";
		TomExamScore scoreExample = new TomExamScore();
		scoreExample.setExamId(Integer.parseInt(request.getParameter("examId")));
		scoreExample.setCode(request.getParameter("userId"));
		//TomExamScore tomExamScore = examScoreMapper.selectOneByExample(scoreExample);
		//TomExamPaper examPaper=examPaperMapper.selectByPrimaryKey(examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId"))).getExamPaperId());
		TomExamScore tomExamScore = new TomExamScore();
		String examScoreRedis = RedisUtils.hget("showExamScore", request.getParameter("examId") + ":" + request.getParameter("userId"));
		if (StringUtil.isNotBlank(examScoreRedis)) {
			tomExamScore = jsonMapper.fromJson(examScoreRedis, TomExamScore.class);
		}else{
			tomExamScore = examScoreMapper.selectOneByExample(scoreExample);
		}
		TomExam exam = new TomExam();
		String examRedis = RedisUtils.hget("exam", request.getParameter("examId"));
		if (StringUtil.isNotBlank(examRedis)) {
			exam = jsonMapper.fromJson(examRedis, TomExam.class);
		}else{
			exam = examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId")));
		}
		TomExamPaper examPaper = new TomExamPaper();
		String examPaperRedis = RedisUtils.hget("tomExamPaper", exam.getExamPaperId().toString());
		if (StringUtil.isNotBlank(examPaperRedis)) {
			examPaper = jsonMapper.fromJson(examPaperRedis, TomExamPaper.class);
		}else{
			examPaperMapper.deleteByPrimaryKey(exam.getExamPaperId());
		}
		tomExamScore.setImmediatelyShow(examPaper.getImmediatelyShow());
		tomExamScore.setShowQualifiedStandard(examPaper.getShowQualifiedStandard());
		if (null != examPaper.getPassMark()) {
			tomExamScore.setPassMark(examPaper.getPassMark());
		}
		tomExamScore.setRetakingExamCount(String.valueOf(examScoreService.getRemainingCount(scoreExample.getCode(), scoreExample.getExamId())));
		tomExamScore.setShowScore(examPaper.getShowScore());
		
		//判断是否开考
		//TomExam exam=examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId")));
		exam.setRemainingCount(examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));
		TomRetakingExam retakingExample=new TomRetakingExam();
		retakingExample.setExamId(exam.getExamId());
		if(exam.getRemainingCount()!=0){
			retakingExample.setSort(exam.getRetakingExamCount()+1-exam.getRemainingCount());
		}else{
			retakingExample.setSort(exam.getRetakingExamCount());
		}
		//TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
		TomRetakingExam retakingExam = new TomRetakingExam();
		String retakingExamRedis = RedisUtils.hget("retakingExamSort", retakingExample.getExamId() + ":" + retakingExample.getSort());
		if (StringUtil.isNotBlank(retakingExamRedis)) {
			retakingExam = jsonMapper.fromJson(retakingExamRedis, TomRetakingExam.class);
		}else{
			retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
		}
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		tomExamScore.setRetakingExamBeginTime(format.format(retakingExam.getRetakingExamBeginTime()));
		
		TomExamAnswerDetails examAnswerDetails1=new TomExamAnswerDetails();
		examAnswerDetails1.setCode(request.getParameter("userId"));
		examAnswerDetails1.setExamId(Integer.parseInt(request.getParameter("examId")));
		List<TomExamAnswerDetails> list=examAnswerDetailsMapper.selectByExample(examAnswerDetails1);
		
		List<TomTopic> topics=new ArrayList<TomTopic>();
		for(TomExamAnswerDetails examAnswerDetails:list){
			if ("N".equals(examAnswerDetails.getRightState())) {
				TomTopic topic = topicMapper
						.selectByPrimaryKey(examAnswerDetails.getTopicId());
				List<TomTopicOption> topicOptions = topicOptionMapper
						.selectByTopicId(topic.getTopicId());
				topic.setTopicOptions(topicOptions);
				if (topic.getQuestionType().equals("3")
						|| topic.getQuestionType().equals("4")) {
					topic.setAnswer(examAnswerDetails
							.getSubjectiveItemAnswer());
				} else {
					topic.setAnswer(examAnswerDetails.getEmpAnswer());
				}
				topics.add(topic);
			}
		}
		tomExamScore.setTopics(topics);
		String json=mapper.writeValueAsString(tomExamScore);
		json=json.replaceAll(":null",":\"\"");
		//返回证书结果
		if ("A".equals(exam.getExamType()) && "Y".equals(tomExamScore.getGradeState())) {
			TomActivityProperty activityProperty = activityPropertyMapper.selectByExamId(exam.getExamId());
			TomCertificateDto tomCertificateDto = tomCertificateMapper.selectByAct(activityProperty.getActivityId());
			Map<Object,Object> map = new HashMap<>();
			map.put("code", request.getParameter("userId"));
			map.put("activityId", activityProperty.getActivityId());
			int tomCertificate = tomCertificateManageMapper.countByCodeAct(map);
			if (tomCertificate > 0) {
				confer = true;
				certificateJson = mapper.writeValueAsString(tomCertificateDto);
				certificateJson = certificateJson.replaceAll(":null",":\"\"");
			}
		}
		return new Resultc("Y", json, ErrorCode.SUCCESS_CODE,"", confer,certificateJson);
		//return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[展示我的考试]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月25日 上午9:19:44
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleMyExam(HttpServletRequest request,HttpServletResponse response) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		examScoreService.reSetStatus(request.getParameter("userId"));//扫描所有考试将过期未参加的考试设为不合格
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		PageData<TomExam> page=new PageData<TomExam>();
		Map<Object,Object> queryMap=new HashMap<Object, Object>();
		queryMap.put("userId",request.getParameter("userId"));
		if(request.getParameter("identifying").equals("P")){
			queryMap.put("gradeState2","D");
		}else{
			queryMap.put("gradeState2",request.getParameter("identifying"));
		}
		queryMap.put("gradeState1",request.getParameter("identifying"));
		
		if(request.getParameter("firstIndex")==null){
			queryMap.put("startNum",0);
		}else{
			queryMap.put("startNum",Integer.parseInt(request.getParameter("firstIndex")));
		}
		if(request.getParameter("pageSize")==null){
			queryMap.put("endNum",10);
			page.setPageSize(10);
		}else{
			queryMap.put("endNum",Integer.parseInt(request.getParameter("pageSize")));//(int)queryMap.get("startNum")+
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		
		int count=examMapper.countMyExam(queryMap);
		List<TomExam> list=examMapper.selectMyExam(queryMap);
		
		queryMap.put("gradeState1","Y");
		queryMap.put("gradeState2","Y");
		int examCount0=examMapper.countMyExam(queryMap);
		queryMap.put("gradeState1","N");
		queryMap.put("gradeState2","N");
		int examCount1=examMapper.countMyExam(queryMap);
		queryMap.put("gradeState1","P");
		queryMap.put("gradeState2","D");
		int examCount2=examMapper.countMyExam(queryMap);
		for(TomExam exam:list){
			TomExamPaper examPaper=examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
			
			TomExamScore scoreExample=new TomExamScore();
			scoreExample.setExamId(exam.getExamId());
			scoreExample.setCode(request.getParameter("userId"));
			TomExamScore examScore;
			int reslutNum=examScoreMapper.countByExample(scoreExample);
			if(reslutNum==2){
				scoreExample.setGradeState("P");
				examScore= examScoreMapper.selectListByExample(scoreExample).get(0);
			}else if(reslutNum==1){
				examScore=examScoreMapper.selectListByExample(scoreExample).get(0);
			}else{
				return new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),"系统繁忙(userId:"+request.getParameter("userId")+"   examId:"+exam.getExamId()+")！");
			}
			
			TomActivityProperty propertyExample=new TomActivityProperty();
			propertyExample.setExamId(exam.getExamId());
			List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(propertyExample);
			exam.setPreStatus(activityApiService.getPreStatus(activityProperties, request.getParameter("userId")));
			exam.setExamPaperPicture(examPaper.getExamPaperPicture());
			exam.setTestQuestionCount(examPaper.getTestQuestionCount());
			exam.setFullMark(examPaper.getFullMark());
			exam.setPassMark(examPaper.getPassMark());
			exam.setPassEb(examPaper.getPassEb());
			exam.setRemainingCount(examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));

			//判断是否开考
			TomRetakingExam retakingExample=new TomRetakingExam();
			retakingExample.setExamId(exam.getExamId());
			if(exam.getRemainingCount()!=0){
				retakingExample.setSort(exam.getRetakingExamCount()+1-exam.getRemainingCount());
			}else{
				retakingExample.setSort(exam.getRetakingExamCount());
			}
			TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
			
			exam.setBeginTime(retakingExam.getRetakingExamBeginTime());
			exam.setEndTime(retakingExam.getRetakingExamEndTime());
			exam.setBeginTimeS(format.format(retakingExam.getRetakingExamBeginTime()));
			exam.setEndTimeS(format.format(retakingExam.getRetakingExamEndTime()));
			if (examScore!=null) {
				//显示分数或合格标准
				if (examPaper.getShowScore().equals("Y")&& !examScore.getGradeState().equals("P")) {
					exam.setTotalPoints(String.valueOf(examScore.getTotalPoints()));
				} else {
					exam.setTotalPoints(examScore.getGradeState());
				}
			}
			//设置状态
			if(examScore!=null&&examScore.getGradeState().endsWith("N")){
				exam.setExamState("不合格");
			}else if(examScore.getGradeState().endsWith("P")){
				exam.setExamState("未审阅");
			}else if(examScore!=null&&examScore.getGradeState().endsWith("Y")){
				exam.setExamState("合格");
			}else if(examScore.getGradeState().endsWith("D")){
				if(exam.getOfflineExam().equals("2")&&exam.getEndTime().getTime()>new Date().getTime()){
					exam.setExamState("未参加");
				}else if(exam.getOfflineExam().equals("2")&&exam.getEndTime().getTime()<=new Date().getTime()){
					exam.setExamState("未审阅");
				}else if(exam.getOfflineExam().equals("1")&&exam.getEndTime().getTime()>new Date().getTime()){
					exam.setExamState("待考试");
				}
			}
		}
		page.setCount(count);
		page.setDate(list);
		page.setPageNum((int)queryMap.get("startNum")/(int)queryMap.get("endNum")+1);
		
		MyExam myExam=new MyExam();
		myExam.setExamCount0(examCount0);
		myExam.setExamCount1(examCount1);
		myExam.setExamCount2(examCount2);
		myExam.setExamList(page);
		
		String pageJson=mapper.writeValueAsString(myExam);
		
		pageJson=pageJson.replaceAll(":null",":\"\"");
		return new Result("Y",pageJson,ErrorCode.SUCCESS_CODE, "");
	}
	
	/**
	 * 
	 * 功能描述：[排序]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月21日 下午5:28:15
	 * @param list
	 * @return
	 */
	public List<TomExamQuestion> sort(List<TomExamQuestion> list) throws Exception{
		TomExamQuestion temp; // 记录临时中间值   
	    int size = list.size(); // 大小   
	    for (int i = 0; i < size - 1; i++) {   
	        for (int j = i + 1; j < size; j++) {   
	            if(Integer.parseInt(list.get(i).getSort())> Integer.parseInt(list.get(j).getSort())){ 
	                temp = list.get(i);   
	                list.set(i, list.get(j));   
	                list.set(j,temp);   
	            }   
	        }   
	    } 
	    return list;
	}
	
	/**
	 * 
	 * 功能描述：[随机抽取题目]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月21日 下午6:08:05
	 * @param examQuestion
	 * @return
	 */
	public List<TomTopic> randomTopics(TomExamQuestion examQuestion){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomTopic> topics=topicMapper.selectByqbIdAndType(examQuestion.getQuestionBankId(), examQuestion.getQuestionType());
		List<TomTopic> list=new ArrayList<TomTopic>();
		for(int i=0;i<examQuestion.getCount();i++){
			int index=(int)(Math.random()*topics.size());
			list.add(topics.get(index));
			topics.remove(index);
		}
		return list;
	}
	
//	/**
//	 * 
//	 * 功能描述：[将过期考试设置为不合格,过期考试扣除e币]
//	 *
//	 * 创建者：JCX
//	 * 创建时间: 2016年5月10日 上午9:16:50
//	 * @param userId
//	 */
//	@Transactional
//	public void reSetStatus(String userId){
//		Map<Object,Object> queryMap=new HashMap<Object, Object>();
//		queryMap.put("userId",userId);
//		queryMap.put("gradeState1","P");
//		queryMap.put("gradeState2","D");
//		int count=examMapper.countMyExam(queryMap);
//		queryMap.put("startNum",0);
//		queryMap.put("endNum",count);
//		
//		List<TomExam> list=examMapper.selectMyExam(queryMap);
//		for(TomExam exam:list){
//			TomExamPaper examPaper=examPaperMapper.selectByExamId(exam.getExamId());
//			
//			TomExamScore scoreExample=new TomExamScore();
//			scoreExample.setExamId(exam.getExamId());
//			scoreExample.setCode(userId);
//			TomExamScore examScore=examScoreMapper.selectListByExample(scoreExample).get(0);
//			if(examScoreMapper.countByExample(scoreExample)==1){
//				
//				if(exam.getEndTime().getTime()<new Date().getTime()&&examScore.getGradeState().equals("D")){
//					examScore.setGradeState("N");
//					examScore.setWrongNumber(examPaper.getTestQuestionCount());
//					examScoreMapper.updateSelective(examScore);
//				}
//			}
//			
//			TomRetakingExam retakingExample=new TomRetakingExam();
//			retakingExample.setExamId(exam.getExamId());
//			retakingExample.setSort(exam.getRetakingExamCount());
//			TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
//			Date nowDate=new Date();
//			if(examScore.getGradeState().equals("N")&&examScore.getCreateTime().getTime()<=retakingExam.getRetakingExamBeginTime().getTime()&&nowDate.getTime()>retakingExam.getRetakingExamEndTime().getTime()){
//				TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(userId);
//				
//				TomEbRecord ebRecord=new TomEbRecord();
//				ebRecord.setCode(userId);
//				ebRecord.setUpdateTime(new Date());
//				ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
//				ebRecord.setRoad("9");
//				userInfo.seteNumber(userInfo.geteNumber()-examPaper.getNotPassEb());
//				
//				userInfoMapper.updateByCode(userInfo);
//				ebRecordMapper.insertSelective(ebRecord);
//			}
//		}
//	}
	
//	/**
//	 * 
//	 * 功能描述：[计算剩余补考次数]
//	 *
//	 * 创建者：JCX
//	 * 创建时间: 2016年5月10日 上午9:17:41
//	 * @param userId
//	 * @param examId
//	 * @return
//	 */
//	public int getRemainingCount(String userId,int examId){
//		TomExam exam=examMapper.selectByPrimaryKey(examId);
//		int count=0;
//		TomExamScore scoreExample=new TomExamScore();
//		scoreExample.setExamId(examId);
//		scoreExample.setCode(userId);
//		TomExamScore examScore;
//		int resultNum=examScoreMapper.countByExample(scoreExample);
//		if(resultNum==2){
//			scoreExample.setGradeState("P");
//			examScore= examScoreMapper.selectListByExample(scoreExample).get(0);
//		}else if(resultNum==1){
//			examScore=examScoreMapper.selectListByExample(scoreExample).get(0);
//		}else{
//			System.out.println("用户id："+userId+"  考试id："+examId+"数据错误！");
//			return count+1;
//		}
//		
//		Map<Object,Object> map=new HashMap<Object, Object>();
//		map.put("examId",examId);
//		map.put("date",examScore.getCreateTime());
//		TomRetakingExam retakingExam=retakingExamMapper.selectByTime(map);
//		if(retakingExam!=null){
//			if(new Date().getTime()<=retakingExam.getRetakingExamEndTime().getTime()){
//				count=exam.getRetakingExamCount()- retakingExam.getSort();
//			}else{
//				TomRetakingExam example=new TomRetakingExam();
//				example.setExamId(examId);
//				List<TomRetakingExam> retakingExams=retakingExamMapper.selectListByExample(example);
//				for(TomRetakingExam retakingExam1:retakingExams){
//					if(retakingExam1.getRetakingExamEndTime().getTime()>=new Date().getTime()){
//						count=exam.getRetakingExamCount()-retakingExam1.getSort()+1;
//						break;
//					}
//				}
//			}
//		}else{
//			TomRetakingExam example=new TomRetakingExam();
//			example.setExamId(examId);
//			List<TomRetakingExam> retakingExams=retakingExamMapper.selectListByExample(example);
//			for(TomRetakingExam retakingExam1:retakingExams){
//				if(retakingExam1.getRetakingExamEndTime().getTime()>=new Date().getTime()){
//					count=exam.getRetakingExamCount()-retakingExam1.getSort()+1;
//					break;
//				}
//			}
//		}
//		return count;
//	}
}