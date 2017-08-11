package com.chinamobo.ue.api.global.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.dao.TomActivityDeptMapper;
import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityDept;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.api.activity.dto.ActResults;
import com.chinamobo.ue.api.activity.service.ActivityApiService;
import com.chinamobo.ue.api.global.dto.SerchResults;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomGradeRecordsMapper;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomGradeRecords;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.dto.MyExam;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.service.ExamScoreService;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.dto.AdminDto;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApiGlobalService {
	@Autowired
	private TomEbRecordMapper tomEbRecordMapper;
	@Autowired
	private TomGradeRecordsMapper gradeRecordsMapper;
	@Autowired
	private ActivityApiService activityApiService;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;// 培训活动配置
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomActivityMapper activityMapper;
	@Autowired
	private TomCourseSignInRecordsMapper tomCourseSignInRecordsMapper;
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	@Autowired
	private SystemService systemService;
	@Autowired
	private TomActivityDeptMapper activityDeptMapper;
	@Autowired
	private TomActivityEmpsRelationMapper tomActivityEmpsRelationMapper;
	@Autowired
	private TomActivityPropertyMapper tomActivityPropertyMapper;
	@Autowired
	private ExamScoreService examScoreService;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	@Autowired
	private TomUserInfoMapper tomUserInfoMapper;
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;// 培训活动人员
	@Autowired
	private TomEbRecordMapper ebRecordMapper ;
	@Autowired TomAdminMapper tomAdminMapper;
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * 版本: [4.1全局搜索] 功能说明: 全局搜索接口实现
	 *
	 * 作者: CJX 创建时间: 2016年4月21日 上午10：58
	 */

	public Result eleGlobalSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			reSetStatus(request.getParameter("userId"));// 扫描所有考试将过期未参加的考试设为不合格
			// List<PageData> dataList = new ArrayList<PageData>();
			SerchResults dataList = new SerchResults();
			String pageJson = null;
			PageData<TomCourses> courseList = new PageData<TomCourses>();
			PageData<TomExam> examList = new PageData<TomExam>();
			PageData<ActResults> activityList = new PageData<ActResults>();
			Map<Object, Object> map = new HashMap<Object, Object>();
			Map<Object, Object> map1 = new HashMap<Object, Object>();
			Map<Object, Object> map2 = new HashMap<Object, Object>();
			TomCourses example = new TomCourses();
			TomExam example1 = new TomExam();
			TomActivity example2 = new TomActivity();
			String keyword = request.getParameter("keyword");
//			if (keyword != null) {
//				keyword = keyword.replaceAll("/", "//");
//				keyword = keyword.replaceAll("%", "/%");
//				keyword = keyword.replaceAll("_", "/_");
//			}
			String userId = request.getParameter("userId");
			if ("P".equals(request.getParameter("identifying"))) {
				map1.put("gradeState2", "D");
			} else {
				map1.put("gradeState2", request.getParameter("identifying"));
			}
			map1.put("gradeState1", request.getParameter("identifying"));
			if (request.getParameter("firstIndex") == null) {
				map.put("startNum", 0);
				map1.put("startNum", 0);
				map2.put("startNum", 0);
			} else {
				map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
				map1.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
				map2.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
			}
			if (request.getParameter("pageSize") == null) {
				map.put("endNum", 10);
				map1.put("endNum", 10);
				map2.put("endNum", 10);
				courseList.setPageSize(10);
				examList.setPageSize(10);
				activityList.setPageSize(10);
			} else {
				map.put("endNum",  Integer.parseInt(request.getParameter("pageSize")));//Integer.parseInt(request.getParameter("firstIndex"))+
				
				map1.put("endNum", Integer.parseInt(request.getParameter("pageSize")));//Integer.parseInt(request.getParameter("firstIndex"))+ 
				
				
				
				map2.put("endNum", Integer.parseInt(request.getParameter("pageSize")));//Integer.parseInt(request.getParameter("firstIndex"))+ 
				courseList.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
				examList.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
				activityList.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
			}
			if (null == request.getParameter("classify") || "".equals(request.getParameter("classify"))) {
				example.setCode(userId);
				example.setCourseName(keyword);
				example.setCourseOnline("Y");
				map.put("example", example);
				int count = coursesMapper.countByExampleApi(example);
				List<TomCourses> list = coursesMapper.selectListByPageApi(map);
				for (TomCourses tomCourses : list) {
					Map<Object, Object> mapLearning = new HashMap<Object, Object>();
					mapLearning.put("code", userId);
					mapLearning.put("courseId", tomCourses.getCourseId());
					if ("Y".equals(tomCourses.getCourseOnline())) {
						List<TomCourseLearningRecord> selectLearnRecord = courseLearningRecordMapper
								.selectLearnRecord(mapLearning);
						if (selectLearnRecord.isEmpty()) {
							TomActivityProperty activityProperty = new TomActivityProperty();
							activityProperty.setCourseId(tomCourses.getCourseId());
							List<TomActivityProperty> proList = activityPropertyMapper
									.selectByExample(activityProperty);
							String preStatus = activityApiService.getPreStatus(proList, userId);
							tomCourses.setPreStatus(preStatus);
						} else {
							TomActivityProperty tomActivityProperty = new TomActivityProperty();
							tomActivityProperty.setCourseId(tomCourses.getCourseId());
							List<TomActivityProperty> activityProperties = activityPropertyMapper
									.selectByExample(tomActivityProperty);
							if (activityProperties.size() != 0) {
								tomCourses.setActivityId(activityProperties.get(0).getActivityId());
							}
						}
					} else {
						int countBySigin = courseSignInRecordsMapper.countBySigin(mapLearning);
						if (0 == countBySigin) {
							TomActivityProperty activityProperty = new TomActivityProperty();
							activityProperty.setCourseId(tomCourses.getCourseId());
							List<TomActivityProperty> proList = activityPropertyMapper
									.selectByExample(activityProperty);
							String preStatus = activityApiService.getPreStatus(proList, userId);
							tomCourses.setPreStatus(preStatus);
						} else {
							TomActivityProperty tomActivityProperty = new TomActivityProperty();
							tomActivityProperty.setCourseId(tomCourses.getCourseId());
							List<TomActivityProperty> activityProperties = activityPropertyMapper
									.selectByExample(tomActivityProperty);
							if (activityProperties.size() != 0) {
								tomCourses.setActivityId(activityProperties.get(0).getActivityId());
							}
						}
					}
					TomActivityProperty activityProperty = activityPropertyMapper.selectByNewDate(mapLearning);
					if (null != activityProperty) {
						DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						tomCourses.setBeginTime(format.format(activityProperty.getStartTime()));
						tomCourses.setEndTime(format.format(activityProperty.getEndTime()));
					} else {
						tomCourses.setBeginTime("");
						tomCourses.setEndTime("");
					}
					TomGradeRecords gradeExample = new TomGradeRecords();
					gradeExample.setCode(userId);
					gradeExample.setCourseId(tomCourses.getCourseId());
					gradeExample.setGradeType("C");
					List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(gradeExample);
					gradeExample.setGradeType("L");
					List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(gradeExample);
					if (gradeRecords1.size() > 0 && gradeRecords2.size() > 0) {
						tomCourses.setIsGrade("Y");
					} else {
						tomCourses.setIsGrade("N");
					}

					if ("Y".equals(tomCourses.getCourseOnline())) {
						TomCourseLearningRecord record = new TomCourseLearningRecord();
						record.setCode(userId);
						record.setCourseId(tomCourses.getCourseId());
						int learningNum = courseLearningRecordMapper.countByExample(record);
						if (learningNum > 0) {
							tomCourses.setCourseStatus("2");
						} else {
							tomCourses.setCourseStatus("0");
						}
					} else {
						TomCourseSignInRecords signInExample = new TomCourseSignInRecords();
						signInExample.setCode(userId);
						signInExample.setCourseId(tomCourses.getCourseId());
						int signInNum = courseSignInRecordsMapper.countByExample(signInExample);
						if (signInNum > 0) {
							tomCourses.setCourseStatus("3");
						} else {
							tomCourses.setCourseStatus("1");
						}
					}
				}
				courseList.setDate(list);
				courseList.setCount(count);
				courseList.setPageNum((int) map.get("startNum") / (int) map.get("endNum") + 1);

				example1.setExamName(keyword);
				map1.put("example", example1);
				map1.put("userId", request.getParameter("userId"));

				int count1 = examMapper.countByUserGlobel(map1);
				List<TomExam> list1 = examMapper.selectByUserGlobel(map1);
				for (TomExam exam : list1) {
					TomExamPaper examPaper = examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());

					TomExamScore scoreExample = new TomExamScore();
					scoreExample.setExamId(exam.getExamId());
					scoreExample.setCode(request.getParameter("userId"));
					TomExamScore examScore;
					int reslutNum = examScoreMapper.countByExample(scoreExample);
					if (reslutNum == 2) {
						scoreExample.setGradeState("P");
						examScore = examScoreMapper.selectListByExample(scoreExample).get(0);
					} else if (reslutNum == 1) {
						examScore = examScoreMapper.selectListByExample(scoreExample).get(0);
					} else {
						return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "系统繁忙(userId:"
								+ request.getParameter("userId") + "   examId:" + exam.getExamId() + ")！");
					}

					TomActivityProperty propertyExample = new TomActivityProperty();
					propertyExample.setExamId(exam.getExamId());
					List<TomActivityProperty> activityProperties = activityPropertyMapper
							.selectByExample(propertyExample);
					exam.setPreStatus(
							activityApiService.getPreStatus(activityProperties, request.getParameter("userId")));
					exam.setExamPaperPicture(examPaper.getExamPaperPicture());
					exam.setTestQuestionCount(examPaper.getTestQuestionCount());
					exam.setFullMark(examPaper.getFullMark());
					exam.setPassMark(examPaper.getPassMark());
					exam.setPassEb(examPaper.getPassEb());
					exam.setRemainingCount(
							examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));

					// 判断是否开考
					TomRetakingExam retakingExample = new TomRetakingExam();
					retakingExample.setExamId(exam.getExamId());
					if (exam.getRemainingCount() != 0) {
						retakingExample.setSort(exam.getRetakingExamCount() + 1 - exam.getRemainingCount());
					} else {
						retakingExample.setSort(exam.getRetakingExamCount());
					}
					TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);

					exam.setBeginTime(retakingExam.getRetakingExamBeginTime());
					exam.setEndTime(retakingExam.getRetakingExamEndTime());
					if (examScore != null) {
						// 显示分数或合格标准
						if (examPaper.getShowScore().equals("Y") && !examScore.getGradeState().equals("P")) {
							exam.setTotalPoints(String.valueOf(examScore.getTotalPoints()));
						} else {
							exam.setTotalPoints(examScore.getGradeState());
						}
					}
					// 设置状态
					if (examScore != null && examScore.getGradeState().endsWith("N")) {
						exam.setExamState("不合格");
					} else if (examScore.getGradeState().endsWith("P")) {
						exam.setExamState("未审阅");
					} else if (examScore != null && examScore.getGradeState().endsWith("Y")) {
						exam.setExamState("合格");
					} else if (examScore.getGradeState().endsWith("D")) {
						if (exam.getOfflineExam().equals("2") && exam.getEndTime().getTime() > new Date().getTime()) {
							exam.setExamState("未参加");
						} else if (exam.getOfflineExam().equals("1")
								&& exam.getEndTime().getTime() > new Date().getTime()) {
							exam.setExamState("待考试");
						}
					}
				}
				examList.setCount(count1);
				examList.setDate(list1);
				examList.setPageNum((int) map1.get("startNum") / (int) map1.get("endNum") + 1);

				MyExam myExam = new MyExam();
				myExam.setExamList(examList);

				example2.setActivityName(keyword);
				map2.put("example", example2);
				map2.put("code", userId);
				int count2 = activityMapper.countByListGlobel(map2);
				List<ActResults> list2 = activityMapper.selectByUserListGlobel(map2);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date nowDate = new Date();
				TomActivityProperty activityPropertyExample = new TomActivityProperty();
				TomExamScore scoreExample = new TomExamScore();
				for (ActResults actResults : list2) {
					actResults.setActivityEndTimeS(format.format(actResults.getActivityEndTime()));
					actResults.setActivityStartTimeS(format.format(actResults.getActivityStartTime()));
					boolean b = false;
					activityPropertyExample.setActivityId(actResults.getActivityId());
					List<TomActivityProperty> tomActivityProperties = activityPropertyMapper
							.selectByExample(activityPropertyExample);
					if (tomActivityProperties.size() > 0) {
						Map<Integer, Integer> finishedMap = new HashMap<Integer, Integer>();
						for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
							if (tomActivityProperty.getExamId() != null) {
								scoreExample.setCode(userId);
								scoreExample.setExamId(tomActivityProperty.getExamId());
								TomExamScore tomExamScore = tomExamScoreMapper.selectOneByExample(scoreExample);
								if (null != tomExamScore) {
									if (tomExamScore.getGradeState().equals("Y")) {
										if (finishedMap.get(actResults.getActivityId()) == null) {
											finishedMap.put(actResults.getActivityId(), 1);
										} else {
											finishedMap.put(actResults.getActivityId(),
													finishedMap.get(actResults.getActivityId()) + 1);
										}
									}
								}
							} else if (tomActivityProperty.getCourseId() != null) {
								TomCourses courses = coursesMapper
										.selectByPrimaryKey(tomActivityProperty.getCourseId());
								if (courses.getCourseOnline().equals("Y")) {
									TomCourseLearningRecord courseLearningRecordsExample = new TomCourseLearningRecord();
									courseLearningRecordsExample.setCode(userId);
									courseLearningRecordsExample.setCourseId(courses.getCourseId());
									courseLearningRecordsExample.setLearningDate(tomActivityProperty.getEndTime());
									if (courseLearningRecordMapper.countByExample(courseLearningRecordsExample) > 0) {
										if (finishedMap.get(actResults.getActivityId()) == null) {
											finishedMap.put(actResults.getActivityId(), 1);
										} else {
											finishedMap.put(actResults.getActivityId(),
													finishedMap.get(actResults.getActivityId()) + 1);
										}
									}
								} else {
									TomCourseSignInRecords courseSingin = new TomCourseSignInRecords();
									courseSingin.setCourseId(courses.getCourseId());
									courseSingin.setCode(userId);
									courseSingin.setCreateDate(tomActivityProperty.getEndTime());
									if (courseSignInRecordsMapper.countByExample(courseSingin) > 0) {
										if (finishedMap.get(actResults.getActivityId()) == null) {
											finishedMap.put(actResults.getActivityId(), 1);
										} else {
											finishedMap.put(actResults.getActivityId(),
													finishedMap.get(actResults.getActivityId()) + 1);
										}
									}
								}
							}
						}
						for (Map.Entry<Integer, Integer> entry : finishedMap.entrySet()) {
							if (entry.getValue() == tomActivityProperties.size()) {
								b = true;
							}
						}
					}
					TomActivityEmpsRelation key = new TomActivityEmpsRelation();
					key.setActivityId(actResults.getActivityId());
					key.setCode(userId);
					TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper.selectByPrimaryKey(key);
					TomActivityDept activityDeptExample = new TomActivityDept();
					activityDeptExample.setActivityId(actResults.getActivityId());

					activityDeptExample.setHeaderCode(userId);
					TomActivityDept activityDept = activityDeptMapper.selectByExample(activityDeptExample);
					if (true == b) {
						if (activityDept != null && activityEmpsRelation != null) {
							actResults.setApplyStatus("11");
						} else if (activityDept != null && activityEmpsRelation == null) {
							actResults.setApplyStatus("10");
						} else if (activityDept == null && activityEmpsRelation != null) {
							actResults.setApplyStatus("6");
						}

					} else if (activityDept != null && activityEmpsRelation == null) {
						actResults.setApplyStatus("10");
					} else if (false == b && actResults.getActivityStartTime().before(nowDate)) {
						if (activityEmpsRelation.getApplyType().equals("P")) {
							actResults.setApplyStatus("9");
						} else {
							actResults.setApplyStatus("7");
						}
					} else {
						if (actResults.getNeedApply().equals("Y")) {
							if (actResults.getApplicationStartTime().after(nowDate)) {
								actResults.setApplyStatus("1");
							} else if (actResults.getApplicationStartTime().before(nowDate)
									&& actResults.getApplicationDeadline().after(nowDate)) {
								if (activityEmpsRelation.getApplyStatus().equals("N")) {
									actResults.setApplyStatus("2");
								} else {
									actResults.setApplyStatus("3");
								}
							} else if (activityEmpsRelation.getApplyStatus().equals("Y")
									&& actResults.getApplicationDeadline().before(nowDate)
									&& actResults.getActivityStartTime().after(nowDate)) {
								actResults.setApplyStatus("4");
							} else if (activityEmpsRelation.getApplyStatus().equals("Y")
									&& actResults.getActivityStartTime().before(nowDate)
									&& actResults.getActivityEndTime().after(nowDate)) {
								actResults.setApplyStatus("5");
							}
						} else {
							if (actResults.getActivityStartTime().after(nowDate)) {
								if (activityEmpsRelation.getApplyType().equals("P")) {
									actResults.setApplyStatus("8");
								} else {
									actResults.setApplyStatus("4");
								}

							} else if (actResults.getActivityStartTime().before(nowDate)
									&& actResults.getActivityEndTime().after(nowDate)) {
								actResults.setApplyStatus("5");
							}
						}
					}
				}
				activityList.setDate(list2);
				activityList.setCount(count2);
				activityList.setPageNum((int) map2.get("startNum") / (int) map2.get("endNum") + 1);
				dataList.setExamList(examList);
				dataList.setCoursrList(courseList);
				dataList.setActivityList(activityList);
				pageJson = mapper.writeValueAsString(dataList);
				for (TomExam exam : list1) {
					pageJson = pageJson.replaceAll(String.valueOf(exam.getBeginTime().getTime()),
							"\"" + format.format(exam.getBeginTime()) + "\"");
					pageJson = pageJson.replaceAll(String.valueOf(exam.getEndTime().getTime()),
							"\"" + format.format(exam.getEndTime()) + "\"");
				}
				pageJson = pageJson.replaceAll(":null", ":\"\"");
				return new Result("Y", pageJson, "0", "");
			} else {
				List<TomExam> list1 = null;
				MyExam myExam = new MyExam();
				int classify = Integer.parseInt(request.getParameter("classify"));

				if (0 == classify) {
					example.setCode(userId);
					example.setCourseName(keyword);
					example.setCourseOnline("Y");
					map.put("example", example);
					int count = coursesMapper.countByExampleApi(example);
					List<TomCourses> list = coursesMapper.selectListByPageApi(map);
					for (TomCourses tomCourses : list) {
						Map<Object, Object> mapLearning = new HashMap<Object, Object>();
						mapLearning.put("code", userId);
						mapLearning.put("courseId", tomCourses.getCourseId());
						if ("Y".equals(tomCourses.getCourseOnline())) {
							List<TomCourseLearningRecord> selectLearnRecord = courseLearningRecordMapper
									.selectLearnRecord(mapLearning);
							if (selectLearnRecord.isEmpty()) {
								TomActivityProperty activityProperty = new TomActivityProperty();
								activityProperty.setCourseId(tomCourses.getCourseId());
								List<TomActivityProperty> proList = activityPropertyMapper
										.selectByExample(activityProperty);
								String preStatus = activityApiService.getPreStatus(proList, userId);
								tomCourses.setPreStatus(preStatus);
							} else {
								TomActivityProperty tomActivityProperty = new TomActivityProperty();
								tomActivityProperty.setCourseId(tomCourses.getCourseId());
								List<TomActivityProperty> activityProperties = activityPropertyMapper
										.selectByExample(tomActivityProperty);
								if (activityProperties.size() != 0) {
									tomCourses.setActivityId(activityProperties.get(0).getActivityId());
								}
							}
						} else {
							int countBySigin = courseSignInRecordsMapper.countBySigin(mapLearning);
							if (0 == countBySigin) {
								TomActivityProperty activityProperty = new TomActivityProperty();
								activityProperty.setCourseId(tomCourses.getCourseId());
								List<TomActivityProperty> proList = activityPropertyMapper
										.selectByExample(activityProperty);
								String preStatus = activityApiService.getPreStatus(proList, userId);
								tomCourses.setPreStatus(preStatus);
							} else {
								TomActivityProperty tomActivityProperty = new TomActivityProperty();
								tomActivityProperty.setCourseId(tomCourses.getCourseId());
								List<TomActivityProperty> activityProperties = activityPropertyMapper
										.selectByExample(tomActivityProperty);
								if (activityProperties.size() != 0) {
									tomCourses.setActivityId(activityProperties.get(0).getActivityId());
								}
							}
						}
						TomActivityProperty activityProperty = activityPropertyMapper.selectByNewDate(mapLearning);
						if (null != activityProperty) {
							DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							tomCourses.setBeginTime(format.format(activityProperty.getStartTime()));
							tomCourses.setEndTime(format.format(activityProperty.getEndTime()));
						} else {
							tomCourses.setBeginTime("");
							tomCourses.setEndTime("");
						}
						TomGradeRecords gradeExample = new TomGradeRecords();
						gradeExample.setCode(userId);
						gradeExample.setCourseId(tomCourses.getCourseId());
						gradeExample.setGradeType("C");
						List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(gradeExample);
						gradeExample.setGradeType("L");
						List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(gradeExample);
						if (gradeRecords1.size() > 0 && gradeRecords2.size() > 0) {
							tomCourses.setIsGrade("Y");
						} else {
							tomCourses.setIsGrade("N");
						}

						if ("Y".equals(tomCourses.getCourseOnline())) {
							TomCourseLearningRecord record = new TomCourseLearningRecord();
							record.setCode(userId);
							record.setCourseId(tomCourses.getCourseId());
							int learningNum = courseLearningRecordMapper.countByExample(record);
							if (learningNum > 0) {
								tomCourses.setCourseStatus("2");
							} else {
								tomCourses.setCourseStatus("0");
							}
						} else {
							TomCourseSignInRecords signInExample = new TomCourseSignInRecords();
							signInExample.setCode(userId);
							signInExample.setCourseId(tomCourses.getCourseId());
							int signInNum = courseSignInRecordsMapper.countByExample(signInExample);
							if (signInNum > 0) {
								tomCourses.setCourseStatus("3");
							} else {
								tomCourses.setCourseStatus("1");
							}
						}
					}
					courseList.setDate(list);
					courseList.setCount(count);
					courseList.setPageNum((int) map.get("startNum") / (int) map.get("endNum") + 1);
					examList.setCount(0);
					activityList.setCount(0);

				} else if (1 == classify) {

					example1.setExamName(keyword);
					map1.put("example", example1);
					map1.put("userId", request.getParameter("userId"));

					int count1 = examMapper.countByUserGlobel(map1);
					list1 = examMapper.selectByUserGlobel(map1);
					for (TomExam exam : list1) {
						TomExamPaper examPaper = examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());

						TomExamScore scoreExample = new TomExamScore();
						scoreExample.setExamId(exam.getExamId());
						scoreExample.setCode(request.getParameter("userId"));
						TomExamScore examScore;
						int reslutNum = examScoreMapper.countByExample(scoreExample);
						if (reslutNum == 2) {
							scoreExample.setGradeState("P");
							examScore = examScoreMapper.selectListByExample(scoreExample).get(0);
						} else if (reslutNum == 1) {
							examScore = examScoreMapper.selectListByExample(scoreExample).get(0);
						} else {
							return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "系统繁忙(userId:"
									+ request.getParameter("userId") + "   examId:" + exam.getExamId() + ")！");
						}

						TomActivityProperty propertyExample = new TomActivityProperty();
						propertyExample.setExamId(exam.getExamId());
						List<TomActivityProperty> activityProperties = activityPropertyMapper
								.selectByExample(propertyExample);
						exam.setPreStatus(
								activityApiService.getPreStatus(activityProperties, request.getParameter("userId")));
						exam.setExamPaperPicture(examPaper.getExamPaperPicture());
						exam.setTestQuestionCount(examPaper.getTestQuestionCount());
						exam.setFullMark(examPaper.getFullMark());
						exam.setPassMark(examPaper.getPassMark());
						exam.setPassEb(examPaper.getPassEb());
						exam.setRemainingCount(
								examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));

						// 判断是否开考
						TomRetakingExam retakingExample = new TomRetakingExam();
						retakingExample.setExamId(exam.getExamId());
						if (exam.getRemainingCount() != 0) {
							retakingExample.setSort(exam.getRetakingExamCount() + 1 - exam.getRemainingCount());
						} else {
							retakingExample.setSort(exam.getRetakingExamCount());
						}
						TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);

						exam.setBeginTime(retakingExam.getRetakingExamBeginTime());
						exam.setEndTime(retakingExam.getRetakingExamEndTime());
						if (examScore != null) {
							// 显示分数或合格标准
							if (examPaper.getShowScore().equals("Y") && !examScore.getGradeState().equals("P")) {
								exam.setTotalPoints(String.valueOf(examScore.getTotalPoints()));
							} else {
								exam.setTotalPoints(examScore.getGradeState());
							}
						}
						// 设置状态
						if (examScore != null && examScore.getGradeState().endsWith("N")) {
							exam.setExamState("不合格");
						} else if (examScore.getGradeState().endsWith("P")) {
							exam.setExamState("未审阅");
						} else if (examScore != null && examScore.getGradeState().endsWith("Y")) {
							exam.setExamState("合格");
						} else if (examScore.getGradeState().endsWith("D")) {
							if (exam.getOfflineExam().equals("2")
									&& exam.getEndTime().getTime() > new Date().getTime()) {
								exam.setExamState("未参加");
							} else if (exam.getOfflineExam().equals("1")
									&& exam.getEndTime().getTime() > new Date().getTime()) {
								exam.setExamState("待考试");
							}
						}
					}
					examList.setCount(count1);
					examList.setDate(list1);
					examList.setPageNum((int) map1.get("startNum") / (int) map1.get("endNum") + 1);
					myExam.setExamList(examList);
					courseList.setCount(0);
					activityList.setCount(0);
				} else if (2 == classify) {

					example2.setActivityName(keyword);
					map2.put("example", example2);
					map2.put("code", userId);
					int count2 = activityMapper.countByListGlobel(map2);
					List<ActResults> list2 = activityMapper.selectByUserListGlobel(map2);
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date nowDate = new Date();
					TomActivityProperty activityPropertyExample = new TomActivityProperty();
					TomExamScore scoreExample = new TomExamScore();
					for (ActResults actResults : list2) {
						actResults.setActivityEndTimeS(format.format(actResults.getActivityEndTime()));
						actResults.setActivityStartTimeS(format.format(actResults.getActivityStartTime()));
						boolean b = false;
						activityPropertyExample.setActivityId(actResults.getActivityId());
						List<TomActivityProperty> tomActivityProperties = activityPropertyMapper
								.selectByExample(activityPropertyExample);
						if (tomActivityProperties.size() > 0) {
							Map<Integer, Integer> finishedMap = new HashMap<Integer, Integer>();
							for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
								if (tomActivityProperty.getExamId() != null) {
									scoreExample.setCode(userId);
									scoreExample.setExamId(tomActivityProperty.getExamId());
									TomExamScore tomExamScore = tomExamScoreMapper.selectOneByExample(scoreExample);
									if (null != tomExamScore) {
										if (tomExamScore.getGradeState().equals("Y")) {
											if (finishedMap.get(actResults.getActivityId()) == null) {
												finishedMap.put(actResults.getActivityId(), 1);
											} else {
												finishedMap.put(actResults.getActivityId(),
														finishedMap.get(actResults.getActivityId()) + 1);
											}
										}
									}
								} else if (tomActivityProperty.getCourseId() != null) {
									TomCourses courses = coursesMapper
											.selectByPrimaryKey(tomActivityProperty.getCourseId());
									if (courses.getCourseOnline().equals("Y")) {
										TomCourseLearningRecord courseLearningRecordsExample = new TomCourseLearningRecord();
										courseLearningRecordsExample.setCode(userId);
										courseLearningRecordsExample.setCourseId(courses.getCourseId());
										courseLearningRecordsExample.setLearningDate(tomActivityProperty.getEndTime());
										if (courseLearningRecordMapper
												.countByExample(courseLearningRecordsExample) > 0) {
											if (finishedMap.get(actResults.getActivityId()) == null) {
												finishedMap.put(actResults.getActivityId(), 1);
											} else {
												finishedMap.put(actResults.getActivityId(),
														finishedMap.get(actResults.getActivityId()) + 1);
											}
										}
									} else {
										TomCourseSignInRecords courseSingin = new TomCourseSignInRecords();
										courseSingin.setCourseId(courses.getCourseId());
										courseSingin.setCode(userId);
										courseSingin.setCreateDate(tomActivityProperty.getEndTime());
										if (courseSignInRecordsMapper.countByExample(courseSingin) > 0) {
											if (finishedMap.get(actResults.getActivityId()) == null) {
												finishedMap.put(actResults.getActivityId(), 1);
											} else {
												finishedMap.put(actResults.getActivityId(),
														finishedMap.get(actResults.getActivityId()) + 1);
											}
										}
									}
								}
							}
							for (Map.Entry<Integer, Integer> entry : finishedMap.entrySet()) {
								if (entry.getValue() == tomActivityProperties.size()) {
									b = true;
								}
							}
						}
						TomActivityEmpsRelation key = new TomActivityEmpsRelation();
						key.setActivityId(actResults.getActivityId());
						key.setCode(userId);
						TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper
								.selectByPrimaryKey(key);
						TomActivityDept activityDeptExample = new TomActivityDept();
						activityDeptExample.setActivityId(actResults.getActivityId());

						activityDeptExample.setHeaderCode(userId);
						TomActivityDept activityDept = activityDeptMapper.selectByExample(activityDeptExample);
						if (true == b) {
							if (activityDept != null && activityEmpsRelation != null) {
								actResults.setApplyStatus("11");
							} else if (activityDept == null && activityEmpsRelation != null) {
								actResults.setApplyStatus("6");
							}

						} else if (activityDept != null) {
							if (activityDept.getPushStatus().equals("Y")) {
								actResults.setApplyStatus("10");
								if (actResults.getActivityEndTime().before(nowDate)) {
									actResults.setApplyStatus("7");
								}
							} else {
								if (actResults.getActivityStartTime().before(nowDate)) {
									actResults.setApplyStatus("9");
								} else {
									actResults.setApplyStatus("8");
								}
							}
						} else if (actResults.getActivityEndTime().before(nowDate)) {
							actResults.setApplyStatus("7");
						} else {
							if (actResults.getNeedApply().equals("Y")) {
								if (actResults.getApplicationStartTime().after(nowDate)) {
									actResults.setApplyStatus("1");
								} else if (actResults.getApplicationStartTime().before(nowDate)
										&& actResults.getApplicationDeadline().after(nowDate)) {
									if (activityEmpsRelation.getApplyStatus().equals("N")) {
										actResults.setApplyStatus("2");
									} else {
										actResults.setApplyStatus("3");
									}
								} else if (activityEmpsRelation.getApplyStatus().equals("Y")
										&& actResults.getApplicationDeadline().before(nowDate)
										&& actResults.getActivityStartTime().after(nowDate)) {
									actResults.setApplyStatus("4");
								} else if (activityEmpsRelation.getApplyStatus().equals("Y")
										&& actResults.getActivityStartTime().before(nowDate)
										&& actResults.getActivityEndTime().after(nowDate)) {
									actResults.setApplyStatus("5");
								}
							} else {
								if (actResults.getActivityStartTime().after(nowDate)) {
									if (activityEmpsRelation.getApplyType().equals("P")) {
										actResults.setApplyStatus("8");
									} else {
										actResults.setApplyStatus("4");
									}

								} else if (actResults.getActivityStartTime().before(nowDate)
										&& actResults.getActivityEndTime().after(nowDate)) {
									actResults.setApplyStatus("5");
								}
							}
						}
					}
					activityList.setDate(list2);
					activityList.setCount(count2);
					activityList.setPageNum((int) map2.get("startNum") / (int) map2.get("endNum") + 1);
					examList.setCount(0);
					courseList.setCount(0);
				}
				dataList.setExamList(examList);
				dataList.setCoursrList(courseList);
				dataList.setActivityList(activityList);
				pageJson = mapper.writeValueAsString(dataList);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				if (null != list1) {
					for (TomExam exam : list1) {
						pageJson = pageJson.replaceAll(String.valueOf(exam.getBeginTime().getTime()),
								"\"" + format.format(exam.getBeginTime()) + "\"");
						pageJson = pageJson.replaceAll(String.valueOf(exam.getEndTime().getTime()),
								"\"" + format.format(exam.getEndTime()) + "\"");
					}
				}
				pageJson = pageJson.replaceAll(":null", ":\"\"");
				return new Result("Y", pageJson, "0", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 功能描述：[3.1.扫一扫签到]
	 *
	 * 创建者：cjx 创建时间: 2016年4月25日 下午8:13:41
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public Result eleScanSignIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		TomCourseSignInRecords tomCourseSignInRecords = new TomCourseSignInRecords();
		Date date = new Date();
		Map<Object, Object> map = new HashMap<Object, Object>();
		tomCourseSignInRecords.setCode(request.getParameter("userId"));

		tomCourseSignInRecords.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		tomCourseSignInRecords.setCreateDate(date);
		int courseId = Integer.parseInt(request.getParameter("courseId"));
		TomCourses course = coursesMapper.selectByPrimaryKey(courseId);
		if (null != course && "N".equals(course.getCourseOnline())) {
			int count = tomCourseSignInRecordsMapper.countByExample(tomCourseSignInRecords);

			if (0 == count) {
				int countCourse = 0;
				List<TomActivityEmpsRelation> list = tomActivityEmpsRelationMapper
						.selectByActivitycode(request.getParameter("userId"));
				for (TomActivityEmpsRelation tomActivityEmpsRelation : list) {
					Date endTime = activityMapper.selectByPrimaryKey(tomActivityEmpsRelation.getActivityId())
							.getActivityEndTime();
					List<TomActivityProperty> list2 = tomActivityPropertyMapper
							.selectByActivityId(tomActivityEmpsRelation.getActivityId());
					for (TomActivityProperty tomActivityProperty : list2) {
						if (null != tomActivityProperty.getCourseId()) {

							if (tomActivityProperty.getCourseId().equals(
									Integer.parseInt(request.getParameter("courseId"))) && date.before(endTime)) {
								countCourse++;
							}
						}
					}
				}
				if (countCourse != 0) {
					tomCourseSignInRecordsMapper.insert(tomCourseSignInRecords);
					TomCourses selectByPrimaryKey = coursesMapper
							.selectByPrimaryKey(Integer.parseInt(request.getParameter("courseId")));
					TomUserInfo userInfo = tomUserInfoMapper.selectByPrimaryKey(request.getParameter("userId"));
					Integer eNumber = userInfo.geteNumber();
					String courseNumber = userInfo.getCourseNumber();
					Map<Object, Object> map1 = new HashMap<Object, Object>();
					SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
					map1.put("userId", request.getParameter("userId"));
					map1.put("road", "2");
					map1.put("date", simple.format(new Date()));
					List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map1);
					int ebCount=0;
					for(TomEbRecord er:ebRList){
						ebCount+=er.getExchangeNumber();
					}
					int addEb=100-ebCount;
					if(addEb>=selectByPrimaryKey.geteCurrency()){
					userInfo.seteNumber(eNumber+selectByPrimaryKey.geteCurrency());
					userInfo.setCourseNumber(String.valueOf(Integer.parseInt(courseNumber)+1));
					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+ selectByPrimaryKey.geteCurrency());
					tomUserInfoMapper.updateByCode(userInfo);
					TomEbRecord ebRecord = new TomEbRecord();
					ebRecord.setExchangeNumber(selectByPrimaryKey.geteCurrency());
					ebRecord.setCode(request.getParameter("userId"));
					ebRecord.setRoad("2");
					ebRecord.setUpdateTime(new Date());
					tomEbRecordMapper.insertSelective(ebRecord);
					}else{
						userInfo.seteNumber(eNumber+addEb);
						userInfo.setCourseNumber(String.valueOf(Integer.parseInt(courseNumber)+1));
						userInfo.setAddUpENumber(userInfo.getAddUpENumber()+ addEb);
						tomUserInfoMapper.updateByCode(userInfo);
						TomEbRecord ebRecord = new TomEbRecord();
						ebRecord.setExchangeNumber(addEb);
						ebRecord.setCode(request.getParameter("userId"));
						ebRecord.setRoad("2");
						ebRecord.setUpdateTime(new Date());
						tomEbRecordMapper.insertSelective(ebRecord);
					}
					map.put("status1", "Y");
					map.put("status2", selectByPrimaryKey);
					String pageJson = mapper.writeValueAsString(map);
					return new Result("Y", pageJson, "0", "");
				} else {
					map.put("status1", "P");
					map.put("status2", "亲，没有给您分配这门课程或活动时间已过！");
					String pageJson = mapper.writeValueAsString(map);
					return new Result("P", pageJson, "0", "");
				}

			} else {
				map.put("status1", "R");
				map.put("status2", "亲，您已经签过了，请勿重复签到！");
				String pageJson = mapper.writeValueAsString(map);
				return new Result("R", pageJson, "0", "");
			}
		} else {
			map.put("status1", "W");
			map.put("status2", "亲，这门课程不是线下课程，或不存在这门课程，请联系负责人！");
			String pageJson = mapper.writeValueAsString(map);
			return new Result("R", pageJson, "0", "");
		}
	}

	public void reSetStatus(String userId) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		Map<Object, Object> queryMap = new HashMap<Object, Object>();
		queryMap.put("userId", userId);
		queryMap.put("gradeState1", "P");
		queryMap.put("gradeState2", "D");
		int count = examMapper.countMyExam(queryMap);
		queryMap.put("startNum", 0);
		queryMap.put("endNum", count);

		List<TomExam> list = examMapper.selectMyExam(queryMap);
		for (TomExam exam : list) {
			TomExamScore scoreExample = new TomExamScore();
			scoreExample.setExamId(exam.getExamId());
			scoreExample.setCode(userId);
			TomExamScore examScore;
			if (examScoreMapper.countByExample(scoreExample) == 2) {
				scoreExample.setGradeState("P");
				List<TomExamScore> examScores = examScoreMapper.selectListByExample(scoreExample);
				if (examScores.size() > 0) {
					examScore = examScores.get(0);
				} else {
					System.out.println("code:" + userId + "   examId:" + exam.getExamId() + "数据异常");
					continue;
				}
			} else {
				examScore = examScoreMapper.selectListByExample(scoreExample).get(0);
			}

			if (exam.getEndTime().getTime() < new Date().getTime() && examScore.getGradeState().equals("D")) {
				examScore.setGradeState("N");
				examScoreMapper.updateSelective(examScore);
			}
		}
	}

	/**
	 * 
	 * 功能描述：[通过token查询用户id]
	 *
	 * 创建者：wjx 创建时间: 2016年5月13日 上午10:54:32
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result queryUserByToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String token = request.getParameter("token");
		TomUserLog user = systemService.getUserByToken(token);
		TomAdmin admin=tomAdminMapper.getAdminByCode(user.getCode());
		AdminDto dto=new AdminDto();
		PropertyUtils.copyProperties(dto,user);
		if(admin != null ){
			dto.setAdminStatus(admin.getStatus());
		}else {
			dto.setAdminStatus("N");
		}
		String s;
		try {
		
			s = mapper.writeValueAsString(dto);

			if (user != null) {
				return new Result("Y", s, "0", "");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Result("N", "", "0", "");
	}

	/**
	 * 
	 * 功能描述：[判断课程是否有效]
	 *
	 * 创建者：JCX 创建时间: 2016年6月15日 下午5:45:07
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Result eleCourseVerification(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<String, String> map = new HashMap<String, String>();

		TomCourseSignInRecords example = new TomCourseSignInRecords();
		example.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		example.setCode(request.getParameter("userId"));
		if (tomCourseSignInRecordsMapper.countByExample(example) > 0) {
			map.put("flag", "Y");
		} else {
			map.put("flag", "N");
		}

		String json = mapper.writeValueAsString(map);
		return new Result("Y", json, "0", "");
	}
}
