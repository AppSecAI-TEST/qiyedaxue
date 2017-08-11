package com.chinamobo.ue.system.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.dto.EmpDto;
import com.chinamobo.ue.system.dto.TomEmpDto;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomEmpOne;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.WeChatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmpServise {
	private static final String STR_FORMAT = "000000";  
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomActivityMapper tomActivityMapper;
	@Autowired
	private TomActivityEmpsRelationMapper tomActivityEmpsRelationMapper;
	@Autowired
	private TomActivityPropertyMapper tomActivityPropertyMapper;
	@Autowired
	private TomCoursesMapper tomCoursesMapper;
	@Autowired
	private TomExamMapper tomExamMapper;
	@Autowired
	private TomExamPaperMapper tomExamPaperMapper;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomUserInfoMapper tomUserInfoMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomCourseLearningRecordMapper tomCourseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper tomCourseSignInRecordsMapper;
	@Autowired
	private UserLogServise userLogServise;
	@Autowired
	private UserInfoServise userInfoServise;
	@Autowired
	private TomDeptMapper deptMapper;
	@Autowired
	private TomOrgMapper orgMapper;
	@Autowired
	private TomUserLogMapper tomUserLogMapper;
	@Autowired
	private ContextInitRedisService contextInitRedisService;
	ObjectMapper mapper = new ObjectMapper();
//	private static Logger logger = LoggerFactory.getLogger(InterfaceJsonToObject.class);

	public PageData<TomEmp> selectByMany() {

		return null;
	}

	public PageData<TomEmp> selectByPage(int pageNum, int pageSize, String deptname, String empcode, String name,
			String cityname, String sex, String code, String statuss,String poststat, String begindate) {
		
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomEmp example = new TomEmp();
		PageData<TomEmp> page = new PageData<TomEmp>();
		// if(null!=sex&&""!=sex){
		// if("男".equals(sex)){
		// sex="0";
		// }else if("女".equals(sex)){
		// sex="1";
		// }
		// }
//		if (name != null) {
//			name = name.replaceAll("/", "//");
//			name = name.replaceAll("%", "/%");
//			name = name.replaceAll("_", "/_");
//		}
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		example.setCode(empcode);
		example.setDeptname(deptname);
		example.setName(name);
		example.setCityname(cityname);
		example.setSex(sex);
		example.setDeptcode(code);
		example.setOrgcode(code);
		example.setPoststat(poststat);
		map1.put("example", example);
		map1.put("statuss", statuss);
		map1.put("begindate", begindate);

		int count = tomEmpMapper.countByExample(map1);
		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("example", example);
		map.put("statuss", statuss);
		map.put("begindate", begindate);
		List<TomEmp> list = tomEmpMapper.selectByMany(map);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;

	}

	public TomEmp selectByCode(String code) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomEmp selectByPrimaryKey = tomEmpMapper.selectByPrimaryKey(code);
		if ("Y".equals(selectByPrimaryKey.getOrgendflag())) {
			selectByPrimaryKey.setOrgendflag("是");
		} else {
			selectByPrimaryKey.setOrgendflag("否");
		}
		if ("Y".equals(selectByPrimaryKey.getPkPost())) {
			selectByPrimaryKey.setPkPost("是");
		} else {
			selectByPrimaryKey.setPkPost("否");
		}
		return selectByPrimaryKey;
	}

	public TomEmp selectByEmail(String email) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomEmpMapper.selectByEmail(email);
	}
	
	public TomUserInfo selectByCodeOnetoOne(String code) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomUserInfoMapper.selectByUserLog(code);
	}

	public PageData<TomActivity> selectByEmpActivity(String code,int pageNum,int pageSize) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomActivity> page = new PageData<TomActivity>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		map1.put("code", code);
		int count1 = tomActivityMapper.CountByEmpActivity(map1);
		if (pageSize == -1) {
			pageSize = count1;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum",  pageSize);//pageNum * 
		map.put("code", code);
		
		List<TomActivity> tomEmp = tomActivityMapper.selectByEmpActivity(map);
		String status = "";
		if (null != tomEmp) {
			List<TomExamScore> tomExamScoreList = tomExamScoreMapper.selectbyCode(code);
				for (int i = 0; i < tomEmp.size(); i++) {
					int count = 0;
					Date date = new Date();
					TomActivity tomActivity = tomEmp.get(i);
					if (null != tomActivity.getApplicationStartTime()) {
						if (date.getTime() < (tomActivity.getApplicationStartTime().getTime())) {
							status = "未报名";
						} else {
							status = "已报名";
							if (date.getTime() < (tomActivity.getActivityStartTime()).getTime()) {
								status = "未进行";
							} else {
								status = "进行中";
							}
						}
					} else if (date.getTime() < (tomActivity.getActivityStartTime()).getTime()) {
						status = "未进行";
					} else {
						status = "进行中";
					}
					List<TomActivityProperty> activityList = tomActivityPropertyMapper
							.selectByActivityId(tomEmp.get(i).getActivityId());
					int examcount = 0;
					int notPassCount=0;
					for (TomActivityProperty tomActivityProperty : activityList) {
						if (null != tomActivityProperty.getExamId()) {
							examcount++;
						}
						if (null != tomActivityProperty.getCourseId()) {						
								Map<Object,Object> map2 = new HashMap<Object,Object>();
								map2.put("code", code);
								map2.put("courseId", tomActivityProperty.getCourseId());
								List<TomCourseLearningRecord> selectLearnRecord = tomCourseLearningRecordMapper.selectLearnRecord(map2);
								int countBySigin = tomCourseSignInRecordsMapper.countBySigin(map2);
								TomCourses tomCourses = tomCoursesMapper
										.selectByPrimaryKey(tomActivityProperty.getCourseId());	
								if (!selectLearnRecord.isEmpty()) {
										count = count + tomCourses.geteCurrency();
									}else if(countBySigin!=0){
										count = count + tomCourses.geteCurrency();
									}else{
										notPassCount++;
									}
						} else {
							for (TomExamScore tomExamScore : tomExamScoreList) {
								if (tomActivityProperty.getExamId().equals(tomExamScore.getExamId())) {
									TomExam tomExam = tomExamMapper.selectByPrimaryKey(tomActivityProperty.getExamId());
									TomExamPaper tomExamPaper = tomExamPaperMapper.selectByExamId(tomExam.getExamId());
									if("Y".equals(tomExamScore.getGradeState())){
										count = count + tomExamPaper.getPassEb();
									}else if("N".equals(tomExamScore.getGradeState())){
										count = count - tomExamPaper.getNotPassEb();
										notPassCount++;
									}else{
										notPassCount++;
									}
								}
							}
						}
					}
					if (examcount == 0) {
						tomEmp.get(i).setFinishStatus("N");
					} else {
						tomEmp.get(i).setFinishStatus("Y");
					}
					if(notPassCount==0){
							status = "已完成";					
					}else{
						if(date.getTime() > (tomActivity.getActivityEndTime()).getTime()){
							status = "未完成";	
						}else{
							status = "进行中";	
						}
					}
					tomEmp.get(i).seteB(count);
					 tomEmp.get(i).setStatus(status);
				}
		}
		page.setDate(tomEmp);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count1);
		return page;
	}

	public PageData<TomEmpDto> selectByEmpExam(String code,int pageNum,int pageSize) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomEmpDto> page = new PageData<TomEmpDto>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		map1.put("code", code);
		int count1 = tomEmpMapper.countByEmpExam(map1);
		if (pageSize == -1) {
			pageSize = count1;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("code", code);
		List<TomEmpDto> tomEmpDtoList = tomEmpMapper.selectByEmpExam(map);
		if (tomEmpDtoList.size() != 0) {
			for (TomEmpDto tomEmpDto : tomEmpDtoList) {
				if ("D".equals(tomEmpDto.getGradeState())) {
					tomEmpDto.setJoin("否");
				} else {
					tomEmpDto.setJoin("是");
				}

			}
		}
		page.setDate(tomEmpDtoList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count1);
		return page;
	}

	public PageData<TomEmpDto> selectByEmpCourses(String code, int pageNum, int pageSize) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomEmpDto> page = new PageData<TomEmpDto>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		map1.put("code", code);
		int count1 = tomEmpMapper.countByEmpCourses(map1);
		if (pageSize == -1) {
			pageSize = count1;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum",  pageSize);//pageNum * 
		map.put("code", code);
		List<TomEmpDto> selectByEmpCourses = tomEmpMapper.selectByEmpCourses(map);
		for(TomEmpDto tomEmpDto:selectByEmpCourses){
			Map<Object,Object> map2 = new HashMap<Object,Object>();
			map2.put("courseId", tomEmpDto.getCourseId());
			map2.put("code", code);
			List<TomCourseLearningRecord> selectLearnRecord = tomCourseLearningRecordMapper.selectLearnRecord(map2);
			int countBySigin = tomCourseSignInRecordsMapper.countBySigin(map2);
			if(!selectLearnRecord.isEmpty()){
				tomEmpDto.setFinishStatus("Y");			
			}else if(countBySigin!=0){
				tomEmpDto.setFinishStatus("Y");
			}else{
				tomEmpDto.setFinishStatus("N");
				tomEmpDto.seteCurrency(0);
			}
			List<TomActivityEmpsRelation> selectByActivitycode = tomActivityEmpsRelationMapper.selectByActivitycode(code);
			for(TomActivityEmpsRelation tomActivityEmpsRelation:selectByActivitycode){
				List<TomActivityProperty> activity = tomActivityPropertyMapper.selectByActivityId(tomActivityEmpsRelation.getActivityId());
				for(TomActivityProperty tomActivityProperty:activity){
					if(null!=tomActivityProperty.getCourseId()){
						if(tomActivityProperty.getCourseId().equals(tomEmpDto.getCourseId())){
							tomEmpDto.setFinishTime(tomActivityProperty.getEndTime());
						}
					}
				}
			}
		}
		page.setDate(selectByEmpCourses);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count1);
		return page;
	}

	@Transactional
	public void insertSelective(TomEmp tom1) {
		// TODO Auto-generated method stub

	}

	@Transactional
	public void updateByPrimaryKeySelective(TomEmp tom1) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * 功能描述：[TODO]
	 *
	 * 创建者：JCX 创建时间: 2016年4月5日 下午6:10:13
	 * 
	 * @param courseId
	 * @return
	 */
	public List<TomEmp> selectByCourseId(int courseId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourses courses= coursesMapper.selectByPrimaryKey(courseId);
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		map.put("createTime", courses.getCreateTime());
		List<TomEmp> emps = tomEmpMapper.selectListByCourseId(map);
		return emps;
	}

	public PageData<TomEmp> selectByAdmin(int pageNum, int pageSize, String deptname, String empcode, String name,
			String cityname, String sex, String code, String statuss) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomEmp example = new TomEmp();
		PageData<TomEmp> page = new PageData<TomEmp>();
		// if
		Map<Object, Object> map1 = new HashMap<Object, Object>();

		// if(null!=sex&&""!=sex){
		// if("男".equals(sex)){
		// sex="0";
		// }else if("女".equals(sex)){
		// sex="1";
		// }
		// }
		example.setCode(empcode);
		example.setDeptname(deptname);
		example.setName(name);
		example.setCityname(cityname);
		example.setSex(sex);
		example.setDeptcode(code);
		example.setOrgcode(code);
		map1.put("example", example);
		map1.put("statuss", statuss);

		int count = tomEmpMapper.countByAdmin(map1);
		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("example", example);
		map.put("statuss", statuss);
		List<TomEmp> list = tomEmpMapper.selectByAdmin(map);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;

	}

	public Object selectByLecturer(int pageNum, int pageSize, String deptname, String empcode, String name,
			String cityname, String sex, String code, String statuss) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomEmp example = new TomEmp();
		PageData<TomEmp> page = new PageData<TomEmp>();
		// if
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		example.setCode(empcode);
		example.setDeptname(deptname);
		example.setName(name);
		example.setCityname(cityname);
		example.setSex(sex);
		example.setDeptcode(code);
		example.setOrgcode(code);
		map1.put("example", example);
		map1.put("statuss", statuss);

		int count = tomEmpMapper.countByLecture(map1);
		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum",  pageSize);//pageNum * 
		map.put("example", example);
		map.put("statuss", statuss);
		List<TomEmp> list = tomEmpMapper.selectByLecture(map);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;
	}

	public Object selectForEmp(int pageNum, int pageSize, String deptname, String empcode, String name, String cityname,
			String sex, String code, String statuss,String country,String begindate) {
		TomEmp example = new TomEmp();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomEmp> page = new PageData<TomEmp>();
		// if(null!=sex&&""!=sex){
		// if("男".equals(sex)){
		// sex="0";
		// }else if("女".equals(sex)){
		// sex="1";
		// }
		// }
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		example.setCode(empcode);
		example.setDeptname(deptname);
		example.setName(name);
		example.setCityname(cityname);
		example.setSex(sex);
		example.setDeptcode(code);
		example.setOrgcode(code);
		map1.put("example", example);
		map1.put("statuss", statuss);
		map1.put("country", country);
		map1.put("begindate", begindate);
		int count = tomEmpMapper.countSelectEmp(map1);
		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum",  pageSize);//pageNum * 
		map.put("example", example);
		map.put("statuss", statuss);
		map.put("country", country);
		map.put("begindate", begindate);
		List<TomEmp> list = tomEmpMapper.selectEmp(map);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;
	}

//	@Transactional
//	public String insertList() throws Exception {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		List<TomEmpOne> members = getPsndoconeJson();
//		int batchCount = 500;// 每批commit的个数
//		int batchLastIndex = batchCount;// 每批最后一个的下标
//		try {
//			for (int index = 0; index < members.size();) {
//				if (batchLastIndex >= members.size()) {
//					List<TomUserLog> list = new ArrayList<TomUserLog>();
//					List<TomUserInfo> list1 = new ArrayList<TomUserInfo>();
//					batchLastIndex = members.size();
//					 tomEmpMapper.insertList(members.subList(index,
//					 batchLastIndex));// 批量插入
//					 //循环添加userlog,userinfo表
//					for (TomEmpOne tomEmpOne : members.subList(index, batchLastIndex)) {
//						TomUserLog tomUserLog = new TomUserLog();
//						TomUserInfo tomUserInfo = new TomUserInfo();
//						 tomUserLog.setCode(tomEmpOne.getCode());
//						 tomUserLog.setEmail(tomEmpOne.getSecretEmail());
//						 tomUserLog.setName(tomEmpOne.getName());
//						 tomUserLog.setPhone(tomEmpOne.getMobile());
//						 tomUserLog.setStatus("Y");
//						tomUserInfo.setCode(tomEmpOne.getCode());
//						tomUserInfo.setAddUpENumber(0);
//						tomUserInfo.setContinuousSignInNumber(0);
//						tomUserInfo.setCourseNumber("0");
//						tomUserInfo.setLearningTime("0");
//						tomUserInfo.seteNumber(0);
//						tomUserInfo.setSumSignInTimes(0);
//						tomUserInfo.setPhoneNumber(tomEmpOne.getMobile());
//						tomUserInfo.setName(tomEmpOne.getName());
//						tomUserInfo.setAnonymity("Y");
//						tomUserInfo.setAddress(tomEmpOne.getCityname());
//						tomUserInfo.setLastSignInTime(sdf.parse("2016-01-01"));
//						 list.add(tomUserLog);
//						list1.add(tomUserInfo);
//					}
//					 tomUserLogMapper.insertList(list);
//					tomUserInfoMapper.insertList(list1);
//					System.out.println("index:" + index + " batchLastIndex:" + batchLastIndex);
//					break;// 数据插入完毕，退出循环
//				} else {
//					 tomEmpMapper.insertList(members.subList(index,
//					 batchLastIndex));
//					List<TomUserLog> list = new ArrayList<TomUserLog>();
//					List<TomUserInfo> list1 = new ArrayList<TomUserInfo>();
//					for (TomEmpOne tomEmpOne : members.subList(index, batchLastIndex)) {
//						TomUserLog tomUserLog = new TomUserLog();
//						TomUserInfo tomUserInfo = new TomUserInfo();
//						 tomUserLog.setCode(tomEmpOne.getCode());
//						 tomUserLog.setEmail(tomEmpOne.getSecretEmail());
//						 tomUserLog.setName(tomEmpOne.getName());
//						 tomUserLog.setPhone(tomEmpOne.getMobile());
//						 tomUserLog.setStatus("Y");
//						tomUserInfo.setCode(tomEmpOne.getCode());
//						tomUserInfo.setAddUpENumber(0);
//						tomUserInfo.setContinuousSignInNumber(0);
//						tomUserInfo.setCourseNumber("0");
//						tomUserInfo.setLearningTime("0");
//						tomUserInfo.seteNumber(0);
//						tomUserInfo.setSumSignInTimes(0);
//						tomUserInfo.setPhoneNumber(tomEmpOne.getMobile());
//						tomUserInfo.setName(tomEmpOne.getName());
//						tomUserInfo.setAnonymity("Y");
//						tomUserInfo.setAddress(tomEmpOne.getCityname());
//						tomUserInfo.setLastSignInTime(sdf.parse("2016-01-01"));
//						 list.add(tomUserLog);
//						list1.add(tomUserInfo);
//					}
//					 tomUserLogMapper.insertList(list);
//					tomUserInfoMapper.insertList(list1);
//					System.out.println("index:" + index + " batchLastIndex:" + batchLastIndex);
//					index = batchLastIndex;// 设置下一批下标
//					batchLastIndex = index + (batchCount - 1);
//					Thread.sleep(2000);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		 //insertCrossEvaluation(list);
//		return null;
//
//	}

	// 处理得到的数据
//	public static List<TomEmpOne> getPsndoconeJson() throws Exception {
//
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode rootNode = mapper.readTree(InvokeInterface.getPsndoconeJson());
//		JsonNode arrNode = rootNode.path("psnall");
//
//		List<TomEmpOne> list = Lists.newArrayList();
//		for (JsonNode node : arrNode) {
//			list.add(mapper.readValue(node.traverse(), TomEmpOne.class));
//		}
//
//		Map<String, TomEmpOne> map = Maps.newHashMap();
//		for (TomEmpOne one : list) {
//			if (!map.containsKey(one.getCode())) {
//				map.put(one.getCode(), one);
//			} else {
//				logger.info("存在重复记录人员前code：" + one.getCode() + ",info: " + one);
//				logger.info("存在重复记录人员后code：" + one.getCode() + ",info: " + map.get(one.getCode()));
//			}
//		}
//		list.clear();
//		list.addAll(map.values());
//		return list;
//	}

//	public void insertOne(TomEmpOne tomEmp) {
//	Date date =new Date();
//	SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	String format = simple.format(date);
//	tomEmp.setCreateTime(date);
//	tomEmp.setCreationtime(format);
//	tomEmp.setUpdateTime(date);
//	TomEmp selectCode = tomEmpMapper.selectCode();
//	String[] split = selectCode.getCode().split("U");
//	int parseInt = Integer.parseInt(split[1]);
//	parseInt++;
//	  DecimalFormat df = new DecimalFormat(STR_FORMAT);  
//      String format1 = df.format(parseInt);
//      tomEmp.setCode("U"+format1);
//      tomEmpMapper.insertSelective(tomEmp);
//	}
	
	//添加人员
	@Transactional
	public void insertEmp(TomEmpOne tomEmp) throws ParseException {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		tomEmp.setBegindate(str);
		tomEmp.setCreationtime(str);
		tomEmp.setPoststat("Y");
		tomEmp.setModifiedtime(str);
		if(tomEmp.getPkDept()==null){
			Tree org = orgMapper.selectByTree(tomEmp.getPkOrg());
			tomEmp.setOnedeptcode(org.getCode());
			tomEmp.setOnedeptname(org.getName());
		}else{
			TomDept dept = deptMapper.selectByPrimaryKey2(tomEmp.getPkDept());
			tomEmp.setDeptpsncode(dept.getPsncode());
			tomEmp.setDeptpsnname(dept.getPsnname());
			int pkDpte = dept.getPkDept();
			
			while(true){
				TomDept depts = deptMapper.selectByPrimaryKey2(Integer.toString(pkDpte));
				if(null!=depts.getTopcode()){
					pkDpte = Integer.parseInt(depts.getTopcode());
				}
				if(depts.getTopcode()==null){
					tomEmp.setOnedeptcode(depts.getCode());
					tomEmp.setOnedeptname(depts.getName());
					break;
				}
			}
		}
		tomEmpMapper.insertSelective(tomEmp);
		TomUserLog record = new TomUserLog();
		record.setCode(tomEmp.getCode());
		record.setName(tomEmp.getName());
		record.setPhone(tomEmp.getMobile());
		record.setEmail(tomEmp.getSecretEmail());
		record.setStatus("Y");
		record.setValidity(new Date());
		userLogServise.insertUserLog(record);
		TomUserInfo tomUserInfo = new TomUserInfo();
		tomUserInfo.setCode(tomEmp.getCode());
		tomUserInfo.setName(tomEmp.getName());
		tomUserInfo.setPhoneNumber(tomEmp.getMobile());
		tomUserInfo.setStatus("Y");
		tomUserInfo.setContinuousSignInNumber(0);
		tomUserInfo.setCourseNumber("0");
		tomUserInfo.seteNumber(0);
		tomUserInfo.setAddUpENumber(0);
		tomUserInfo.setSumSignInTimes(0);
		tomUserInfo.setLearningTime("0");
		tomUserInfo.setAnonymity("Y");
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
		tomUserInfo.setLastSignInTime(sdfs.parse("1990-01-01"));
		userInfoServise.insertUserInfo(tomUserInfo);
		contextInitRedisService.init();
	}
	
	//编辑人员
	@Transactional
	public void editEmp(TomEmpOne tomEmpOne){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		tomEmpOne.setModifiedtime(str);
//		tomEmpOne.setDepttopcode(deptMapper.selectByPrimaryKey2(tomEmpOne.getDeptcode()).getTopcode());
//		tomEmpOne.setDepttopname(deptMapper.selectByPrimaryKey2(tomEmpOne.getDeptcode()).getTopname());
		if(tomEmpOne.getDeptcode().equals(tomEmpOne.getOrgcode())){
			Tree org = orgMapper.selectByTree(tomEmpOne.getPkOrg());
			tomEmpOne.setOnedeptcode(org.getCode());
			tomEmpOne.setOnedeptname(org.getName());
		}else{
			TomDept dept = deptMapper.selectByPrimaryKey2(tomEmpOne.getPkDept());
			tomEmpOne.setDeptpsncode(dept.getPsncode());
			tomEmpOne.setDeptpsnname(dept.getPsnname());
			int pkDpte = dept.getPkDept();
			
			while(true){
				TomDept depts = deptMapper.selectByPrimaryKey2(Integer.toString(pkDpte));
				if(null!=depts.getTopcode()){
					pkDpte = Integer.parseInt(depts.getTopcode());
				}
				if(depts.getTopcode()==null){
					tomEmpOne.setOnedeptcode(depts.getCode());
					tomEmpOne.setOnedeptname(depts.getName());
					break;
				}
			}
		}
		tomEmpMapper.updateByPrimaryKeySelective(tomEmpOne);
		TomUserLog record = new TomUserLog();
		record.setCode(tomEmpOne.getCode());
		record.setName(tomEmpOne.getName());
		record.setEmail(tomEmpOne.getSecretEmail());
		record.setPhone(tomEmpOne.getMobile());
		userLogServise.updateUserLog(record);
		TomUserInfo tomUserInfo = new TomUserInfo();
		tomUserInfo.setName(tomEmpOne.getName());
		tomUserInfo.setCode(tomEmpOne.getCode());
		tomUserInfo.setPhoneNumber(tomEmpOne.getMobile());
		userInfoServise.updateUserInfo(tomUserInfo);
		contextInitRedisService.init();
	}
	
	//同步人员
//	public void synchronizationEmp(String pkDept) throws ParseException{
//		String access_token = WeChatUtil.getToken();
//		List<EmpDto>list = WeChatUtil.getEmp(access_token, pkDept);
//		for(EmpDto emp : list){
//			TomEmpOne tomEmp = new TomEmpOne();
//			tomEmp.setCode(emp.getUserid());
//			tomEmp.setName(emp.getName());
//			tomEmp.setMobile(emp.getMobile());
//			tomEmp.setSecretEmail(emp.getEmail());
//			if(emp.getAvatar().equals("0")){
//				tomEmp.setSex("未定义");
//			}else if(emp.getAvatar().equals("1")){
//				tomEmp.setSex("男");
//			}else{
//				tomEmp.setSex("女");
//			}
//			TomUserLog record = new TomUserLog();
//			record.setCode(emp.getUserid());
//			record.setName(emp.getName());
//			record.setPhone(emp.getMobile());
//			record.setEmail(emp.getEmail());
//			TomUserInfo tomUserInfo = new TomUserInfo();
//			tomUserInfo.setCode(emp.getUserid());
//			tomUserInfo.setName(emp.getName());
//			tomUserInfo.setCode(emp.getUserid());
//			tomUserInfo.setPhoneNumber(emp.getMobile());
//			tomUserInfo.setHeadImg(emp.getAvatar());
//			if(emp.getAvatar().equals("0")){
//				tomUserInfo.setSex("未定义");
//			}else if(emp.getAvatar().equals("1")){
//				tomUserInfo.setSex("男");
//			}else{
//				tomUserInfo.setSex("女");
//			}
//			if(tomEmpMapper.selectByPrimaryKey(emp.getUserid())!=null){
//				tomEmpMapper.updateByPrimaryKeySelective(tomEmp);
//				userLogServise.updateUserLog(record);
//				userInfoServise.updateUserInfo(tomUserInfo);
//			}else{
//				Date date = new Date();
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String str = sdf.format(date);
//				tomEmp.setBegindate(str);
//				tomEmp.setCreationtime(str);
//				tomEmp.setPoststat("Y");
//				tomEmp.setModifiedtime(str);
//				if(tomEmp.getPkDept()==null){
//					Tree org = orgMapper.selectByTree(tomEmp.getPkOrg());
//					tomEmp.setOnedeptcode(org.getCode());
//					tomEmp.setOnedeptname(org.getName());
//				}else{
//					TomDept dept = deptMapper.selectByPrimaryKey2(tomEmp.getPkDept());
//					tomEmp.setDeptpsncode(dept.getPsncode());
//					tomEmp.setDeptpsnname(dept.getPsnname());
//					String pkDpte = dept.getPkDept();
//					while(true){
//						TomDept depts = deptMapper.selectByPrimaryKey2(pkDpte);
//						pkDpte = depts.getTopcode();
//						if(depts.getTopcode()==null){
//							tomEmp.setOnedeptcode(depts.getCode());
//							tomEmp.setOnedeptname(depts.getName());
//							break;
//						}
//					}
//				}
//				tomEmpMapper.insertSelective(tomEmp);
//				record.setStatus("Y");
//				record.setValidity(new Date());
//				userLogServise.insertUserLog(record);
//				tomUserInfo.setPhoneNumber(tomEmp.getMobile());
//				tomUserInfo.setStatus("Y");
//				tomUserInfo.setContinuousSignInNumber(0);
//				tomUserInfo.setCourseNumber("0");
//				tomUserInfo.seteNumber(0);
//				tomUserInfo.setAddUpENumber(0);
//				tomUserInfo.setSumSignInTimes(0);
//				tomUserInfo.setLearningTime("0");
//				tomUserInfo.setAnonymity("Y");
//				SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
//				tomUserInfo.setLastSignInTime(sdfs.parse("1990-01-01"));
//				userInfoServise.insertUserInfo(tomUserInfo);
//			}
//		}
//	}
	
	public void synchronizationEmp(String pkDept) throws ParseException{
		String access_token = WeChatUtil.getToken();
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		List<EmpDto>list = WeChatUtil.getEmp(access_token, pkDept);
		List<TomEmp> oldList=new ArrayList<TomEmp>();
		if("1".equals(pkDept)){
			oldList=tomEmpMapper.selectAllDept();
		}else {
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("deptcode", pkDept);
			oldList=tomEmpMapper.selectByWxDept(map);
		}
		for(EmpDto emp : list){
			for(int i=0;i<oldList.size();i++){
				if(emp.getUserid().equals(oldList.get(i).getCode())){
					oldList.remove(i);
					break;
				}
			}
			TomEmpOne tomEmp = new TomEmpOne();
			tomEmp.setCode(emp.getUserid());
			tomEmp.setName(emp.getName());
			tomEmp.setMobile(emp.getMobile());
			tomEmp.setSecretEmail(emp.getEmail());
			if(emp.getGender().equals("0")){
				tomEmp.setSex("未定义");
			}else if(emp.getGender().equals("1")){
				tomEmp.setSex("男");
			}else if(emp.getGender().equals("2")){
				tomEmp.setSex("女");
			}
			TomUserLog record = new TomUserLog();
			record.setCode(emp.getUserid());
			record.setName(emp.getName());
			record.setPhone(emp.getMobile());
			record.setEmail(emp.getEmail());
			TomUserInfo tomUserInfo = new TomUserInfo();
			tomUserInfo.setCode(emp.getUserid());
			tomUserInfo.setName(emp.getName());
			tomUserInfo.setCode(emp.getUserid());
			tomUserInfo.setPhoneNumber(emp.getMobile());
			if(emp.getGender().equals("0")){
				tomUserInfo.setSex("未定义");
			}else if(emp.getGender().equals("1")){
				tomUserInfo.setSex("男");
			}else if(emp.getGender().equals("2")){
				tomUserInfo.setSex("女");
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String str = sdf.format(date);
			tomEmp.setBegindate(str);
			tomEmp.setCreationtime(str);
			tomEmp.setPoststat("Y");
			tomEmp.setModifiedtime(str);
			tomEmp.setPkDept(emp.getDepartment()[0]);
			tomEmp.setDeptcode(emp.getDepartment()[0]);
			if(orgMapper.selectByTree(emp.getDepartment()[0])!=null){
				Tree org = orgMapper.selectByTree(emp.getDepartment()[0]);
				tomEmp.setOnedeptcode(org.getCode());
				tomEmp.setOnedeptname(org.getName());
			}else{
				TomDept dept = deptMapper.selectByPrimaryKey2(emp.getDepartment()[0]);
				tomEmp.setDeptname(dept.getName());
				tomEmp.setDepttopcode(dept.getTopcode());
				tomEmp.setDepttopname(dept.getTopname());
				tomEmp.setPkOrg(dept.getPkOrg());
				tomEmp.setOrgcode(dept.getOrgcode());
				tomEmp.setOrgname(dept.getOrgname());
				tomEmp.setDeptpsncode(dept.getPsncode());
				tomEmp.setDeptpsnname(dept.getPsnname());
				int pkDptes = dept.getPkDept();
				while(true){
					TomDept depts = deptMapper.selectByPrimaryKey2(Integer.toString(pkDptes));
					if(depts.getTopcode()==null){
						tomEmp.setOnedeptcode(depts.getCode());
						tomEmp.setOnedeptname(depts.getName());
						break;
					}
					pkDptes = Integer.parseInt(depts.getTopcode());
				}
			}
			if(tomEmpMapper.selectByPrimaryKey(emp.getUserid())!=null){
//				TomEmpOne tomEmpOne = tomEmpMapper.selectByPrimaryKeys(emp.getUserid());
//				tomEmpOne.setName(emp.getName());
//				tomEmpOne.setMobile(emp.getMobile());
//				tomEmpOne.setSecretEmail(emp.getEmail());
//				tomEmpOne.setSex(tomEmp.getSex());
//				Date date1 = new Date();
//				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//				String str1 = sdf1.format(date1);
//				tomEmpOne.setModifiedtime(str1);
				tomEmpMapper.updateByPrimaryKeySelective(tomEmp);
				userLogServise.updateUserLog(record);
				userInfoServise.updateUserInfo(tomUserInfo);
			}else{
				tomEmp.setCreateTime(new Date());
				tomEmp.setUpdateTime(new Date());
				tomEmpMapper.insertSelective(tomEmp);
				record.setStatus("Y");
				record.setValidity(new Date());
				userLogServise.insertUserLog(record);
				tomUserInfo.setPhoneNumber(tomEmp.getMobile());
				tomUserInfo.setStatus("Y");
				tomUserInfo.setContinuousSignInNumber(0);
				tomUserInfo.setCourseNumber("0");
				tomUserInfo.seteNumber(0);
				tomUserInfo.setAddUpENumber(0);
				tomUserInfo.setSumSignInTimes(0);
				tomUserInfo.setLearningTime("0");
				tomUserInfo.setAnonymity("Y");
				SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
				tomUserInfo.setLastSignInTime(sdfs.parse("1990-01-01"));
				userInfoServise.insertUserInfo(tomUserInfo);
			}
		}
		if(oldList.size()>0){
			for (TomEmp tomEmp : oldList) {
				TomEmpOne empOne=new TomEmpOne();
				empOne.setCode(tomEmp.getCode());
				empOne.setPoststat("N");
				empOne.setUpdateTime(new Date());
				tomEmpMapper.updateByPrimaryKeySelective(empOne);
				TomUserLog tomUserLog = tomUserLogMapper.getUserByCode(tomEmp.getCode());
				TomUserInfo tomUserInfo = tomUserInfoMapper.selectByPrimaryKey(tomEmp.getCode());
				tomUserInfo.setStatus("N");
				tomUserLog.setStatus("N");
				tomUserLogMapper.updateByCode(tomUserLog);
				tomUserInfoMapper.updateByCode(tomUserInfo);
			}
		}
		contextInitRedisService.init();
	}
}
