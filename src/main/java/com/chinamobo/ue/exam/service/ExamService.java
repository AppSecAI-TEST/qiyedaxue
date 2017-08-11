package com.chinamobo.ue.exam.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.exam.dao.TomExamEmpAllocationMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamOtherRelationMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomJoinExamRecordMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamEmpAllocation;
import com.chinamobo.ue.exam.entity.TomExamOtherRelation;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomJoinExamRecord;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomLabelClassMapper;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelEmpRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelMapper;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomLabel;
import com.chinamobo.ue.system.entity.TomLabelClass;
import com.chinamobo.ue.system.entity.TomLabelEmpRelation;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.system.service.SendMessageService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.RedisUtils;

@Service
public class ExamService {
	@Autowired
	private SendMessageService sendMessageService;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private TomExamEmpAllocationMapper examEmpAllocationMapper;
	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	@Autowired
	private TomUserInfoMapper userInfoMapper;
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private ExamScoreService examScoreService;
	@Autowired
	private TomEbRecordMapper ebRecordMapper;
	@Autowired
	private TomJoinExamRecordMapper joinExamRecordMapper;
	@Autowired
	private TomOrgMapper tomOrgMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private TomExamOtherRelationMapper tomExamOtherRelationMapper;
	@Autowired
	private TomLabelMapper tomLabelMapper;
	@Autowired
	private TomLabelClassMapper tomLabelClassMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomLabelEmpRelationMapper tomLabelEmpRelationMapper;
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	
	JsonMapper mapper=new JsonMapper();
	
	public TomExam findById(int id){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam exam = examMapper.selectByPrimaryKey(id);
//		System.out.println(exam.getBeginTime());
		//封装补考
		TomRetakingExam example=new TomRetakingExam();
		example.setExamId(id);
		List<TomRetakingExam> retakingExams = retakingExamMapper.selectListByExample(example);
		List<String> rbeginTime=new ArrayList<String>();
		List<String> rendTime=new ArrayList<String>();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(TomRetakingExam retakingExam:retakingExams){
			rbeginTime.add(format.format(retakingExam.getRetakingExamBeginTime()));
			rendTime.add(format.format(retakingExam.getRetakingExamEndTime()));
		}
		exam.setRbeginTime(rbeginTime);
		exam.setRendTime(rendTime);
		//封装考试人员关联
//		List<TomEmp> emps=empMapper.selectByExamId(id);
//		List<String> empIds=new ArrayList<String>();
//		List<String> empNames=new ArrayList<String>();
//		List<String> citynames=new ArrayList<String>();
//		List<String> deptnames=new ArrayList<String>();
//		for(TomEmp emp:emps){
//			empIds.add(emp.getCode());
//			empNames.add(emp.getName());
//			citynames.add(emp.getCityname());
//			deptnames.add(emp.getDeptname());
//		}
//		exam.setEmpIds(empIds);
//		exam.setEmpNames(empNames);
//		exam.setCityname(citynames);
//		exam.setDeptname(deptnames);
		
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		//查询部门下的人员
		Map<Object,Object> mapDeptEmp = new HashMap<Object,Object>();
		mapDeptEmp.put("examId", id);
		mapDeptEmp.put("type", "E");
		List<TomEmp> deptEmps=empMapper.selectByExamIdType(mapDeptEmp);
		if(deptEmps.size()>0){
			for(TomEmp deptEmp : deptEmps){
				Map<String, String> deptEmpMap = new HashMap<String, String>();
				deptEmpMap.put("code", deptEmp.getCode());
				deptEmpMap.put("name", deptEmp.getName());
				deptEmpMap.put("type", "E");
				emps.add(deptEmpMap);
			}
		}
		
		//查询部门关联
		Map<Object,Object> mapDept = new HashMap<Object,Object>();
		mapDept.put("examId", id);
		mapDept.put("type", "D");
		List<TomExamOtherRelation> deptList = tomExamOtherRelationMapper.selectOtherList(mapDept);
		if(deptList.size()>0){
			for(TomExamOtherRelation examOtherRelation : deptList){
				Map<String, String> deptMap = new HashMap<String, String>();
				deptMap.put("code", examOtherRelation.getCode());
				deptMap.put("name", examOtherRelation.getName());
				deptMap.put("type", "D");
				deptMap.put("statuss", "1");
				emps.add(deptMap);
			}
		}
		
		//查询标签下的人员
		Map<Object,Object> mapLabelEmp = new HashMap<Object,Object>();
		mapLabelEmp.put("examId", id);
		mapLabelEmp.put("type", "LE");
		List<TomEmp> labelEmps=empMapper.selectByExamIdType(mapLabelEmp);
		if(labelEmps.size()>0){
			for(TomEmp labelEmp : labelEmps){
				Map<String, String> labelEmpMap = new HashMap<String, String>();
				labelEmpMap.put("code", labelEmp.getCode());
				labelEmpMap.put("name", labelEmp.getName());
				labelEmpMap.put("type", "LE");
				emps.add(labelEmpMap);
			}
		}
		
		//查询标签关联
		Map<Object,Object> mapLabel = new HashMap<Object,Object>();
		mapLabel.put("examId", id);
		mapLabel.put("type", "L");
		List<TomExamOtherRelation> labelList = tomExamOtherRelationMapper.selectOtherList(mapLabel);
		if(labelList.size()>0){
			for(TomExamOtherRelation examOtherRelation : labelList){
				Map<String, String> labelMap = new HashMap<String, String>();
				labelMap.put("code", examOtherRelation.getCode());
				labelMap.put("name", examOtherRelation.getName());
				labelMap.put("type", "L");
				labelMap.put("statuss", "3");
				emps.add(labelMap);
			}
		}
		
		//查询标签分类关联
		Map<Object,Object> mapLabelClass = new HashMap<Object,Object>();
		mapLabelClass.put("examId", id);
		mapLabelClass.put("type", "C");
		List<TomExamOtherRelation> labelClassList = tomExamOtherRelationMapper.selectOtherList(mapLabelClass);
		if(labelClassList.size()>0){
			for(TomExamOtherRelation examOtherRelation : labelClassList){
				Map<String, String> labelClassMap = new HashMap<String, String>();
				labelClassMap.put("code", examOtherRelation.getCode());
				labelClassMap.put("name", examOtherRelation.getName());
				labelClassMap.put("type", "C");
				labelClassMap.put("statuss", "1");
				emps.add(labelClassMap);
			}
		}
		
		exam.setEmps(emps);
		
		return exam;
	}

	/**
	 * 功能描述：[考试查询]
	 *
	 * 创建者：xjw
	 * 创建时间: 2016年3月4日 下午2:48:34
	 * @param pageNum
	 * @param pageSize
	 * @param examname
	 * @return
	 */
	public PageData<TomExam> findList(int pageNum,int pageSize,String examname){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam example=new TomExam();
		
//		if(examname!=null){
//			examname=examname.replaceAll("/", "//");
//			examname=examname.replaceAll("%", "/%");
//			examname=examname.replaceAll("_", "/_");
//			
//		}
		example.setExamName(examname);
		PageData<TomExam> page=new PageData<TomExam>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("example", example);
		int count =examMapper.countByExample(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		
		List<TomExam> list=examMapper.findList(map);
		for(TomExam exam:list){
			TomExamPaper examPaper =examPaperService.findById(exam.getExamPaperId());
			exam.setTestQuestionCount(examPaper.getTestQuestionCount());
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 功能描述：[删除考试]
	 *
	 * 创建者：xjw
	 * 创建时间: 2016年3月4日 下午2:27:31
	 * @param id
	 */
	@Transactional
	public void deleteexam(int id){
		//设置主从库查询
		examMapper.deleteByPrimaryKey(id);
		//删除初始默认成绩
		TomExamScore scoreExample=new TomExamScore();
		scoreExample.setExamId(id);
		examScoreMapper.deleteByExample(scoreExample);
		//删除员工关联
		TomExamEmpAllocation example=new TomExamEmpAllocation();
		example.setExamId(id);
		examEmpAllocationMapper.deleteByExample(example);
		//删除补考
		retakingExamMapper.deleteByExamId(id);
		//删除其它关联
		tomExamOtherRelationMapper.deleteByExamId(id);
	}
	
	/**
	 * 功能描述：[增加考试]
	 *
	 * 创建者：xjw
	 * 创建时间: 2016年3月4日 下午5:41:04
	 * @param tomExam
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	@Transactional
	public void addexam(TomExam tomExam) throws UnsupportedEncodingException, Exception{
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			tomExam.setBeginTime(format.parse(tomExam.getBeginTimeS()));
			tomExam.setEndTime(format.parse(tomExam.getEndTimeS()));
			tomExam.setExamType("C");
			String examNumber= numberRecordService.getNumber(MapManager.numberType("KS"));
			tomExam.setExamNumber(examNumber);
			tomExam.setCreateTime(new Date());
			tomExam.setUpdateTime(new Date());
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			examMapper.insertSelective(tomExam);
			RedisUtils.hset("exam", tomExam.getExamId().toString(), mapper.toJson(tomExam));//新增考试到缓存
			//添加补考
			TomRetakingExam retakingExam;
			TomRetakingExam exam=new TomRetakingExam();
			exam.setExamId(tomExam.getExamId());
			exam.setRetakingExamBeginTime(tomExam.getBeginTime());
			exam.setRetakingExamEndTime(tomExam.getEndTime());
			exam.setSort(0);
			retakingExamMapper.insertSelective(exam);
			RedisUtils.hset("retakingExamSort", exam.getExamId() + ":" + exam.getSort(), mapper.toJson(exam));//新增补考到缓存
			for(int i=0;i<tomExam.getRbeginTime().size();i++){
				retakingExam=new TomRetakingExam();
				retakingExam.setExamId(tomExam.getExamId());
				retakingExam.setRetakingExamBeginTime(format.parse(tomExam.getRbeginTime().get(i)));
				retakingExam.setRetakingExamEndTime(format.parse(tomExam.getRendTime().get(i)));
				retakingExam.setSort(i+1);
				retakingExamMapper.insertSelective(retakingExam);
				RedisUtils.hset("retakingExamSort", retakingExam.getExamId() + ":" + retakingExam.getSort(),mapper.toJson(retakingExam));//新增补考到缓存
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.SLAVE);
				List<TomRetakingExam> retakingExams = retakingExamMapper.selectListByExample(retakingExam);
				RedisUtils.hset("retakingExamsForOneExam", retakingExam.getExamId().toString(), mapper.toJson(retakingExams));
			}
			
			//添加考试员工关联
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			TomExamPaper examPaper=examPaperMapper.selectByExamId(exam.getExamId());
			//关联部门下的人员
			if(tomExam.getEmpIds().size()>0){
				for(String code:tomExam.getEmpIds()){
					TomExamScore examScore=new TomExamScore();
					examScore.setExamId(tomExam.getExamId());
					examScore.setCode(code);
					examScore.setEmpName(empMapper.selectByPrimaryKey(code).getName());
					examScore.setGradeState("D");
					examScore.setExamCount(0);
					examScore.setTotalPoints(0);
					examScore.setExamTotalTime(0);
					examScore.setCreateTime(tomExam.getBeginTime());
					examScore.setRightNumbe(0);
					examScore.setWrongNumber(examPaper.getTestQuestionCount());
					examScore.setAccuracy(0d);
					examScore.setType("E");
					//设置主从库查询
					DBContextHolder.setDbType(DBContextHolder.MASTER);
					Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
					mapExamCode.put("examId", tomExam.getExamId());
					mapExamCode.put("code", code);
					if(examScoreMapper.selectByExamCode(mapExamCode)==null){
						examScoreMapper.insertSelective(examScore);
					}
					RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
					TomExamEmpAllocation examEmpAllocation=new TomExamEmpAllocation();
					examEmpAllocation.setCode(code);
					examEmpAllocation.setExamId(tomExam.getExamId());
					examEmpAllocation.setCreateTime(new Date());
					examEmpAllocation.setType("E");
					if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
						examEmpAllocationMapper.insertSelective(examEmpAllocation);
					}
				}
			}
			
			//关联部门
			List<String> deptIds = tomExam.getDeptIds();
			if(deptIds.size()>0){
				for(String deptId : deptIds){
					TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
					List<TomEmp> empList = new ArrayList<TomEmp>();
					//添加考试其它关联表
					if(deptId.equals("1")){
						//查询公司信息
						TomOrg org = tomOrgMapper.selectByPrimaryKey(deptId);
						examOtherRelation.setName(org.getName());
						//全查人员
						empList = tomEmpMapper.selectAllDept();
					}else{
						//查询部门信息
						TomDept dept = tomDeptMapper.selectByPrimaryKey(deptId);
						examOtherRelation.setName(dept.getName());
						//查询部门下所有人
						empList = tomEmpMapper.selectByDeptCode(deptId);
					}
					examOtherRelation.setCode(deptId);
					examOtherRelation.setCreateTime(new Date());
					examOtherRelation.setExamId(tomExam.getExamId());
					examOtherRelation.setType("D");
					tomExamOtherRelationMapper.insertSelective(examOtherRelation);
					//关联考试成绩表
					if(empList.size()>0){
						for(TomEmp emp : empList){
							TomExamScore examScore=new TomExamScore();
							examScore.setExamId(tomExam.getExamId());
							examScore.setCode(emp.getCode());
							examScore.setEmpName(emp.getName());
							examScore.setGradeState("D");
							examScore.setExamCount(0);
							examScore.setTotalPoints(0);
							examScore.setExamTotalTime(0);
							examScore.setCreateTime(tomExam.getBeginTime());
							examScore.setRightNumbe(0);
							examScore.setWrongNumber(examPaper.getTestQuestionCount());
							examScore.setAccuracy(0d);
							examScore.setType("E");
							//设置主从库查询
							DBContextHolder.setDbType(DBContextHolder.MASTER);
							Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
							mapExamCode.put("examId", tomExam.getExamId());
							mapExamCode.put("code", emp.getCode());
							if(examScoreMapper.selectByExamCode(mapExamCode)==null){
								examScoreMapper.insertSelective(examScore);
							}
							RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
						}
					}
//					//关联考试人员分配表
//					TomExamEmpAllocation examEmpAllocationDept = new TomExamEmpAllocation();
//					examEmpAllocationDept.setCode(deptId);
//					examEmpAllocationDept.setExamId(exam.getExamId());
//					examEmpAllocationDept.setCreateTime(new Date());
//					examEmpAllocationDept.setType("D");
//					Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
//					mapExamCode.put("examId", exam.getExamId());
//					mapExamCode.put("code", deptId);
//					if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
//						examEmpAllocationMapper.insertSelective(examEmpAllocationDept);
//					}
				}
			}
			
			//关联标签下的人员
			List<String> labelEmps = tomExam.getLabelEmps();
			if(labelEmps.size()>0){
				for(String labelEmp : labelEmps){
					TomExamScore examScore=new TomExamScore();
					examScore.setExamId(tomExam.getExamId());
					examScore.setCode(labelEmp);
					examScore.setEmpName(empMapper.selectByPrimaryKey(labelEmp).getName());
					examScore.setGradeState("D");
					examScore.setExamCount(0);
					examScore.setTotalPoints(0);
					examScore.setExamTotalTime(0);
					examScore.setCreateTime(tomExam.getBeginTime());
					examScore.setRightNumbe(0);
					examScore.setWrongNumber(examPaper.getTestQuestionCount());
					examScore.setAccuracy(0d);
					examScore.setType("LE");
					Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
					mapExamCode.put("examId", exam.getExamId());
					mapExamCode.put("code", labelEmp);
					if(examScoreMapper.selectByExamCode(mapExamCode)==null){
						examScoreMapper.insertSelective(examScore);
					}
					RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
					TomExamEmpAllocation examEmpAllocation=new TomExamEmpAllocation();
					examEmpAllocation.setCode(labelEmp);
					examEmpAllocation.setExamId(tomExam.getExamId());
					examEmpAllocation.setCreateTime(new Date());
					examEmpAllocation.setType("LE");
					if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
						examEmpAllocationMapper.insertSelective(examEmpAllocation);
					}
				}
			}
			
			//关联标签
			List<String> labelIds = tomExam.getLabelIds();
			if(labelIds.size()>0){
				for(String labelId : labelIds){
					TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
					//查询标签信息
					TomLabel label = tomLabelMapper.selectByPrimaryKey(labelId);
					examOtherRelation.setCode(labelId);
					examOtherRelation.setName(label.getTagName());
					examOtherRelation.setCreateTime(new Date());
					examOtherRelation.setExamId(tomExam.getExamId());
					examOtherRelation.setType("L");
					tomExamOtherRelationMapper.insertSelective(examOtherRelation);
					//关联考试成绩表
					//查询标签下的人员
					List<TomLabelEmpRelation> labelEmpList = tomLabelEmpRelationMapper.selectBytagIdList(labelId);
					if(labelEmpList.size()>0){
						for(TomLabelEmpRelation labelEmpRelation : labelEmpList){
							TomExamScore examScore=new TomExamScore();
							examScore.setExamId(tomExam.getExamId());
							examScore.setCode(labelEmpRelation.getCode());
							examScore.setEmpName(labelEmpRelation.getName());
							examScore.setGradeState("D");
							examScore.setExamCount(0);
							examScore.setTotalPoints(0);
							examScore.setExamTotalTime(0);
							examScore.setCreateTime(tomExam.getBeginTime());
							examScore.setRightNumbe(0);
							examScore.setWrongNumber(examPaper.getTestQuestionCount());
							examScore.setAccuracy(0d);
							examScore.setType("LE");
							//设置主从库查询
							DBContextHolder.setDbType(DBContextHolder.MASTER);
							Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
							mapExamCode.put("examId", tomExam.getExamId());
							mapExamCode.put("code", labelEmpRelation.getCode());
							if(examScoreMapper.selectByExamCode(mapExamCode)==null){
								examScoreMapper.insertSelective(examScore);
							}
							RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
						}
					}
//					//关联考试人员分配表
//					TomExamEmpAllocation examEmpAllocationDept = new TomExamEmpAllocation();
//					examEmpAllocationDept.setCode(labelId);
//					examEmpAllocationDept.setExamId(exam.getExamId());
//					examEmpAllocationDept.setCreateTime(new Date());
//					examEmpAllocationDept.setType("L");
//					Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
//					mapExamCode.put("examId", exam.getExamId());
//					mapExamCode.put("code", labelId);
//					if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
//						examEmpAllocationMapper.insertSelective(examEmpAllocationDept);
//					}
				}
			}
			
			//关联标签分类
			List<String> labelClassIds = tomExam.getLabelClassIds();
			if(labelClassIds.size()>0){
				for(String labelClassId : labelClassIds){
					TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
					//查询标签分类信息
					TomLabelClass labelClass = tomLabelClassMapper.selectByPrimaryKey(Integer.valueOf(labelClassId));
					examOtherRelation.setCode(labelClassId);
					examOtherRelation.setName(labelClass.getClassName());
					examOtherRelation.setCreateTime(new Date());
					examOtherRelation.setExamId(tomExam.getExamId());
					examOtherRelation.setType("C");
					tomExamOtherRelationMapper.insertSelective(examOtherRelation);
					//关联考试成绩表
					//查询标签分类下的人员
					List<TomLabelEmpRelation> labelClassEmpList = tomLabelClassRelationMapper.selectLabelClassEmpList(Integer.valueOf(labelClassId));
					if(labelClassEmpList.size()>0){
						for(TomLabelEmpRelation labelEmpRelation : labelClassEmpList){
							TomExamScore examScore=new TomExamScore();
							examScore.setExamId(tomExam.getExamId());
							examScore.setCode(labelEmpRelation.getCode());
							examScore.setEmpName(labelEmpRelation.getName());
							examScore.setGradeState("D");
							examScore.setExamCount(0);
							examScore.setTotalPoints(0);
							examScore.setExamTotalTime(0);
							examScore.setCreateTime(tomExam.getBeginTime());
							examScore.setRightNumbe(0);
							examScore.setWrongNumber(examPaper.getTestQuestionCount());
							examScore.setAccuracy(0d);
							examScore.setType("LE");
							//设置主从库查询
							DBContextHolder.setDbType(DBContextHolder.MASTER);
							Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
							mapExamCode.put("examId", tomExam.getExamId());
							mapExamCode.put("code", labelEmpRelation.getCode());
							if(examScoreMapper.selectByExamCode(mapExamCode)==null){
								examScoreMapper.insertSelective(examScore);
							}
							RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
						}
					}
//					//关联考试人员分配表
//					TomExamEmpAllocation examEmpAllocationDept = new TomExamEmpAllocation();
//					examEmpAllocationDept.setCode(labelClassId);
//					examEmpAllocationDept.setExamId(exam.getExamId());
//					examEmpAllocationDept.setCreateTime(new Date());
//					examEmpAllocationDept.setType("C");
//					Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
//					mapExamCode.put("examId", exam.getExamId());
//					mapExamCode.put("code", labelClassId);
//					if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
//						examEmpAllocationMapper.insertSelective(examEmpAllocationDept);
//					}
				}
			}
			
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
			List<String> listForEmail = new ArrayList<String>();
			String examApp = "亲爱的蔚来伙伴：您有一次考试" + "【" + tomExam.getExamName() + "】" + "需要在【"
					+ format.parse(tomExam.getBeginTimeS()) + "】至【" + format.parse(tomExam.getEndTimeS())
					+ "】期间完成。请点击链接参加。"
					+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format.parse(tomExam.getBeginTimeS())+"]-["+ format.parse(tomExam.getEndTimeS()) +"]. Please click the link to finish the test.      "+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomExam.getExamId();
			String examEmail = "  【蔚乐学】：您有一次考试" + "【" + tomExam.getExamName() + "】" + "需要在【"
					+format.parse(tomExam.getBeginTimeS()) + "】至【" +format.parse(tomExam.getEndTimeS())
					+ "】期间完成。请点击链接参加。"
					+"Dear NextEVer, you have a test that requires your participation. Please finish the test during["+format.parse(tomExam.getBeginTimeS())+"]-["+ format.parse(tomExam.getEndTimeS()) +"]. Please click the link to finish the test.      <a href=\""+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomExam.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomExam.getExamId()+"</a>";
			if ("Y".equals(message.getSendToApp())) {
				sendMessageService.wxMessage(tomExam.getEmpIds(), examApp);
			}
			if ("Y".equals(message.getSendToEmail())) {
				for(String code:tomExam.getEmpIds()){
					TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
					if(selectByPrimaryKey.getSecretEmail()!=null)
					listForEmail.add(selectByPrimaryKey.getSecretEmail());
				}
				if(listForEmail.size()>0){
					SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", examEmail);
					sm.sendMessage();
				}
			}
			if("Y".equals(message.getSendToPhone())){
				for(String code:tomExam.getEmpIds()){
					TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
					sendMessageService.sendMessage(examApp, selectByPrimaryKey.getMobile());
				}
			}
			TomMessages activityMessage = new TomMessages();
			activityMessage.setMessageTitle( tomExam.getExamName());
			activityMessage.setMessageDetails(examApp);
			activityMessage.setEmpIds(tomExam.getEmpIds());
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
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			tomMessagesMapper.insertSelective(activityMessage);
			for (String code : tomExam.getEmpIds()) {
				TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
				tomMessagesEmployees.setCreateTime(date);
				tomMessagesEmployees.setEmpCode(code);
				tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
				tomMessagesEmployees.setIsView("N");
				tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * 功能描述：[编辑考试]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月13日 下午1:08:50
	 * @param tomExam
	 * @throws Exception
	 */
	@Transactional
	public void updateexam(TomExam tomExam) throws Exception{
		//设置主从库查询
		DateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		tomExam.setUpdateTime(new Date());
		tomExam.setBeginTime(format1.parse(tomExam.getBeginTimeS()));
		tomExam.setEndTime(format1.parse(tomExam.getEndTimeS()));
		examMapper.updateByPrimaryKeySelective(tomExam);
		RedisUtils.hset("exam", tomExam.getExamId().toString(), mapper.toJson(tomExam));
		//删除旧补考
		retakingExamMapper.deleteByExamId(tomExam.getExamId());
		//插入新补考
		TomRetakingExam retakingExam;
		
		if(tomExam.getRetakingExamCount()>0){
			TomRetakingExam exam=new TomRetakingExam();
			exam.setExamId(tomExam.getExamId());
			exam.setRetakingExamBeginTime(tomExam.getBeginTime());
			exam.setRetakingExamEndTime(tomExam.getEndTime());
			exam.setSort(0);
			retakingExamMapper.insertSelective(exam);
			RedisUtils.hset("retakingExamSort",exam.getExamId() + ":" + exam.getSort(),mapper.toJson(exam));
			List<TomRetakingExam> retakingExams=retakingExamMapper.selectListByExample(exam);
			RedisUtils.hset("retakingExamsForOneExam", exam.getExamId().toString(), mapper.toJson(retakingExams));
			for(int i=0;i<tomExam.getRetakingExamCount();i++){
				retakingExam=new TomRetakingExam();
				retakingExam.setExamId(tomExam.getExamId());
				retakingExam.setRetakingExamBeginTime(format2.parse(tomExam.getRbeginTime().get(i)));
				retakingExam.setRetakingExamEndTime(format2.parse(tomExam.getRendTime().get(i)));
				retakingExam.setSort(i+1);
				retakingExamMapper.insertSelective(retakingExam);
				RedisUtils.hset("retakingExamSort",retakingExam.getExamId() + ":" + retakingExam.getSort(),mapper.toJson(retakingExam));
				List<TomRetakingExam> retakingExams2=retakingExamMapper.selectListByExample(retakingExam);
				RedisUtils.hset("retakingExamsForOneExam", retakingExam.getExamId().toString(), mapper.toJson(retakingExams2));
			}
		}else{
			retakingExam=new TomRetakingExam();
			retakingExam.setExamId(tomExam.getExamId());
			retakingExam.setRetakingExamBeginTime(tomExam.getBeginTime());
			retakingExam.setRetakingExamEndTime(tomExam.getEndTime());
			retakingExam.setSort(0);
			retakingExamMapper.insertSelective(retakingExam);
			RedisUtils.hset("retakingExamSort",retakingExam.getExamId() + ":" + retakingExam.getSort(),mapper.toJson(retakingExam));
			List<TomRetakingExam> retakingExams=retakingExamMapper.selectListByExample(retakingExam);
			RedisUtils.hset("retakingExamsForOneExam", retakingExam.getExamId().toString(), mapper.toJson(retakingExams));
		}
		
		//删除旧员工关联
		TomExamEmpAllocation example=new TomExamEmpAllocation();
		example.setExamId(tomExam.getExamId());
		examEmpAllocationMapper.deleteByExample(example);
		//删除旧默认成绩
		TomExamScore examScoreExample=new TomExamScore();
		examScoreExample.setExamId(tomExam.getExamId());
		examScoreExample.setGradeState("D");
		examScoreMapper.deleteByExample(examScoreExample);
		//删除其它关联
		tomExamOtherRelationMapper.deleteByExamId(tomExam.getExamId());
		//添加新考试员工关联
//		TomExamScore examScoreExample2=new TomExamScore();
//		examScoreExample2.setExamId(tomExam.getExamId());
//		List<TomExamScore> selectListByExample = examScoreMapper.selectListByExample(examScoreExample2);
//		for(String code:tomExam.getEmpIds()){
//				int i =0;
//			for(TomExamScore score :selectListByExample){
//				if(code.equals(score.getCode())){
//						i++;
//				}
//			}
//			if(i==0){
//				TomExamScore examScore=new TomExamScore();
//				examScore.setExamId(tomExam.getExamId());
//				examScore.setCode(code);
//				examScore.setEmpName(empMapper.selectByPrimaryKey(code).getName());
//				examScore.setGradeState("D");
//				examScore.setExamCount(0);
//				examScore.setTotalPoints(0);
//				examScore.setExamTotalTime(0);
//				examScore.setCreateTime(tomExam.getBeginTime());
//				examScore.setRightNumbe(0);
//				examScore.setWrongNumber(0);
//				examScore.setAccuracy(0d);
//				examScoreMapper.insertSelective(examScore);
//				RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
//			}
//			TomExamEmpAllocation examEmpAllocation=new TomExamEmpAllocation();
//			examEmpAllocation.setCode(code);
//			examEmpAllocation.setExamId(tomExam.getExamId());
//			examEmpAllocation.setCreateTime(new Date());
//			examEmpAllocationMapper.insertSelective(examEmpAllocation);
//		}
		
		//添加考试员工关联
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExamPaper examPaper=examPaperMapper.selectByExamId(tomExam.getExamId());
		//关联部门下的人员
		if(tomExam.getEmpIds().size()>0){
			for(String code:tomExam.getEmpIds()){
				TomExamScore examScore=new TomExamScore();
				examScore.setExamId(tomExam.getExamId());
				examScore.setCode(code);
				examScore.setEmpName(empMapper.selectByPrimaryKey(code).getName());
				examScore.setGradeState("D");
				examScore.setExamCount(0);
				examScore.setTotalPoints(0);
				examScore.setExamTotalTime(0);
				examScore.setCreateTime(tomExam.getBeginTime());
				examScore.setRightNumbe(0);
				examScore.setWrongNumber(examPaper.getTestQuestionCount());
				examScore.setAccuracy(0d);
				examScore.setType("E");
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
				mapExamCode.put("examId", tomExam.getExamId());
				mapExamCode.put("code", code);
				if(examScoreMapper.selectByExamCode(mapExamCode)==null){
					examScoreMapper.insertSelective(examScore);
				}
				RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
				TomExamEmpAllocation examEmpAllocation=new TomExamEmpAllocation();
				examEmpAllocation.setCode(code);
				examEmpAllocation.setExamId(tomExam.getExamId());
				examEmpAllocation.setCreateTime(new Date());
				examEmpAllocation.setType("E");
				if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
					examEmpAllocationMapper.insertSelective(examEmpAllocation);
				}
			}
		}
		
		//关联部门
		List<String> deptIds = tomExam.getDeptIds();
		if(deptIds.size()>0){
			for(String deptId : deptIds){
				TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
				List<TomEmp> empList = new ArrayList<TomEmp>();
				//添加考试其它关联表
				if(deptId.equals("1")){
					//查询公司信息
					TomOrg org = tomOrgMapper.selectByPrimaryKey(deptId);
					examOtherRelation.setName(org.getName());
					//全查人员
					empList = tomEmpMapper.selectAllDept();
				}else{
					//查询部门信息
					TomDept dept = tomDeptMapper.selectByPrimaryKey(deptId);
					examOtherRelation.setName(dept.getName());
					//查询部门下所有人
					empList = tomEmpMapper.selectByDeptCode(deptId);
				}
				examOtherRelation.setCode(deptId);
				examOtherRelation.setCreateTime(new Date());
				examOtherRelation.setExamId(tomExam.getExamId());
				examOtherRelation.setType("D");
				tomExamOtherRelationMapper.insertSelective(examOtherRelation);
				//关联考试成绩表
				if(empList.size()>0){
					for(TomEmp emp : empList){
						TomExamScore examScore=new TomExamScore();
						examScore.setExamId(tomExam.getExamId());
						examScore.setCode(emp.getCode());
						examScore.setEmpName(emp.getName());
						examScore.setGradeState("D");
						examScore.setExamCount(0);
						examScore.setTotalPoints(0);
						examScore.setExamTotalTime(0);
						examScore.setCreateTime(tomExam.getBeginTime());
						examScore.setRightNumbe(0);
						examScore.setWrongNumber(examPaper.getTestQuestionCount());
						examScore.setAccuracy(0d);
						examScore.setType("E");
						//设置主从库查询
						DBContextHolder.setDbType(DBContextHolder.MASTER);
						Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
						mapExamCode.put("examId", tomExam.getExamId());
						mapExamCode.put("code", emp.getCode());
						if(examScoreMapper.selectByExamCode(mapExamCode)==null){
							examScoreMapper.insertSelective(examScore);
						}
						RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
					}
				}
//				//关联考试人员分配表
//				TomExamEmpAllocation examEmpAllocationDept = new TomExamEmpAllocation();
//				examEmpAllocationDept.setCode(deptId);
//				examEmpAllocationDept.setExamId(tomExam.getExamId());
//				examEmpAllocationDept.setCreateTime(new Date());
//				examEmpAllocationDept.setType("D");
//				Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
//				mapExamCode.put("examId", tomExam.getExamId());
//				mapExamCode.put("code", deptId);
//				if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
//					examEmpAllocationMapper.insertSelective(examEmpAllocationDept);
//				}
			}
		}
		
		//关联标签下的人员
		List<String> labelEmps = tomExam.getLabelEmps();
		if(labelEmps.size()>0){
			for(String labelEmp : labelEmps){
				TomExamScore examScore=new TomExamScore();
				examScore.setExamId(tomExam.getExamId());
				examScore.setCode(labelEmp);
				examScore.setEmpName(empMapper.selectByPrimaryKey(labelEmp).getName());
				examScore.setGradeState("D");
				examScore.setExamCount(0);
				examScore.setTotalPoints(0);
				examScore.setExamTotalTime(0);
				examScore.setCreateTime(tomExam.getBeginTime());
				examScore.setRightNumbe(0);
				examScore.setWrongNumber(examPaper.getTestQuestionCount());
				examScore.setAccuracy(0d);
				examScore.setType("LE");
				Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
				mapExamCode.put("examId", tomExam.getExamId());
				mapExamCode.put("code", labelEmp);
				if(examScoreMapper.selectByExamCode(mapExamCode)==null){
					examScoreMapper.insertSelective(examScore);
				}
				RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
				TomExamEmpAllocation examEmpAllocation=new TomExamEmpAllocation();
				examEmpAllocation.setCode(labelEmp);
				examEmpAllocation.setExamId(tomExam.getExamId());
				examEmpAllocation.setCreateTime(new Date());
				examEmpAllocation.setType("LE");
				if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
					examEmpAllocationMapper.insertSelective(examEmpAllocation);
				}
			}
		}
		
		//关联标签
		List<String> labelIds = tomExam.getLabelIds();
		if(labelIds.size()>0){
			for(String labelId : labelIds){
				TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
				//查询标签信息
				TomLabel label = tomLabelMapper.selectByPrimaryKey(labelId);
				examOtherRelation.setCode(labelId);
				examOtherRelation.setName(label.getTagName());
				examOtherRelation.setCreateTime(new Date());
				examOtherRelation.setExamId(tomExam.getExamId());
				examOtherRelation.setType("L");
				tomExamOtherRelationMapper.insertSelective(examOtherRelation);
				//关联考试成绩表
				//查询标签下的人员
				List<TomLabelEmpRelation> labelEmpList = tomLabelEmpRelationMapper.selectBytagIdList(labelId);
				if(labelEmpList.size()>0){
					for(TomLabelEmpRelation labelEmpRelation : labelEmpList){
						TomExamScore examScore=new TomExamScore();
						examScore.setExamId(tomExam.getExamId());
						examScore.setCode(labelEmpRelation.getCode());
						examScore.setEmpName(labelEmpRelation.getName());
						examScore.setGradeState("D");
						examScore.setExamCount(0);
						examScore.setTotalPoints(0);
						examScore.setExamTotalTime(0);
						examScore.setCreateTime(tomExam.getBeginTime());
						examScore.setRightNumbe(0);
						examScore.setWrongNumber(examPaper.getTestQuestionCount());
						examScore.setAccuracy(0d);
						examScore.setType("LE");
						//设置主从库查询
						DBContextHolder.setDbType(DBContextHolder.MASTER);
						Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
						mapExamCode.put("examId", tomExam.getExamId());
						mapExamCode.put("code", labelEmpRelation.getCode());
						if(examScoreMapper.selectByExamCode(mapExamCode)==null){
							examScoreMapper.insertSelective(examScore);
						}
						RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
					}
				}
//				//关联考试人员分配表
//				TomExamEmpAllocation examEmpAllocationDept = new TomExamEmpAllocation();
//				examEmpAllocationDept.setCode(labelId);
//				examEmpAllocationDept.setExamId(tomExam.getExamId());
//				examEmpAllocationDept.setCreateTime(new Date());
//				examEmpAllocationDept.setType("L");
//				Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
//				mapExamCode.put("examId", tomExam.getExamId());
//				mapExamCode.put("code", labelId);
//				if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
//					examEmpAllocationMapper.insertSelective(examEmpAllocationDept);
//				}
			}
		}
		
		//关联标签分类
		List<String> labelClassIds = tomExam.getLabelClassIds();
		if(labelClassIds.size()>0){
			for(String labelClassId : labelClassIds){
				TomExamOtherRelation examOtherRelation = new TomExamOtherRelation();
				//查询标签分类信息
				TomLabelClass labelClass = tomLabelClassMapper.selectByPrimaryKey(Integer.valueOf(labelClassId));
				examOtherRelation.setCode(labelClassId);
				examOtherRelation.setName(labelClass.getClassName());
				examOtherRelation.setCreateTime(new Date());
				examOtherRelation.setExamId(tomExam.getExamId());
				examOtherRelation.setType("C");
				tomExamOtherRelationMapper.insertSelective(examOtherRelation);
				//关联考试成绩表
				//查询标签分类下的人员
				List<TomLabelEmpRelation> labelClassEmpList = tomLabelClassRelationMapper.selectLabelClassEmpList(Integer.valueOf(labelClassId));
				if(labelClassEmpList.size()>0){
					for(TomLabelEmpRelation labelEmpRelation : labelClassEmpList){
						TomExamScore examScore=new TomExamScore();
						examScore.setExamId(tomExam.getExamId());
						examScore.setCode(labelEmpRelation.getCode());
						examScore.setEmpName(labelEmpRelation.getName());
						examScore.setGradeState("D");
						examScore.setExamCount(0);
						examScore.setTotalPoints(0);
						examScore.setExamTotalTime(0);
						examScore.setCreateTime(tomExam.getBeginTime());
						examScore.setRightNumbe(0);
						examScore.setWrongNumber(examPaper.getTestQuestionCount());
						examScore.setAccuracy(0d);
						examScore.setType("LE");
						//设置主从库查询
						DBContextHolder.setDbType(DBContextHolder.MASTER);
						Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
						mapExamCode.put("examId", tomExam.getExamId());
						mapExamCode.put("code", labelEmpRelation.getCode());
						if(examScoreMapper.selectByExamCode(mapExamCode)==null){
							examScoreMapper.insertSelective(examScore);
						}
						RedisUtils.hset("examScore",examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore));
					}
				}
//				//关联考试人员分配表
//				TomExamEmpAllocation examEmpAllocationDept = new TomExamEmpAllocation();
//				examEmpAllocationDept.setCode(labelClassId);
//				examEmpAllocationDept.setExamId(tomExam.getExamId());
//				examEmpAllocationDept.setCreateTime(new Date());
//				examEmpAllocationDept.setType("C");
//				Map<Object,Object> mapExamCode = new HashMap<Object,Object>();
//				mapExamCode.put("examId", tomExam.getExamId());
//				mapExamCode.put("code", labelClassId);
//				if(examEmpAllocationMapper.selectByCodeExamId(mapExamCode)==null){
//					examEmpAllocationMapper.insertSelective(examEmpAllocationDept);
//				}
			}
		}
	}

	/**
	 * 
	 * 功能描述：[导入成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月25日 上午9:49:13
	 * @param examId
	 * @param string
	 */
	public String importResults(int examId, String filePath) throws IOException {
		String path=filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		if("xls".equals(path))
			return readXls(filePath,examId);
		else if("xlsx".equals(path))
			return readXlsx(filePath, examId);
		return "模板文件类型不正确。";
	}

	/**
	 * 
	 * 功能描述：[读取xlsx]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月25日 上午9:56:56
	 * @param filePath
	 * @param examId
	 */
	@Transactional
	private String readXlsx(String filePath, int examId) throws IOException {
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam exam=examMapper.selectByPrimaryKey(examId);
		TomExamPaper examPaper=examPaperMapper.selectByExamId(examId);
		List<TomExamScore> examScores=new ArrayList<TomExamScore>();
		
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
				if (xssfRow!=null&&xssfRow.getCell(0) != null&&!"".equals(xssfRow.getCell(0))) {
					TomExamScore examScore = new TomExamScore();
					
					XSSFCell examNumber = xssfRow.getCell(0);
					if(!exam.getExamNumber().equals(getValue(examNumber))){
						return "并非此考试的成绩！";
					}
					if(exam.getBeginTime().after(new Date())){
						return "考试并未开始";
					}
					
					XSSFCell empName = xssfRow.getCell(1);
					XSSFCell code = xssfRow.getCell(2);
					XSSFCell gradeState = xssfRow.getCell(3);
					XSSFCell totalPoints = xssfRow.getCell(4);
					XSSFCell examTotalTime = xssfRow.getCell(5);
					XSSFCell rightNumbe = xssfRow.getCell(6);
					XSSFCell wrongNumber = xssfRow.getCell(7);
					examScore.setCode(getValue(code));
					examScore.setExamId(examId);
					examScore.setEmpName(getValue(empName));
					examScore.setGradeState(getValue(gradeState));
					examScore.setTotalPoints((int)Double.parseDouble(getValue(totalPoints)));
					examScore.setExamTotalTime((int)Double.parseDouble(getValue(examTotalTime))*60);
					examScore.setRightNumbe((int)Double.parseDouble(getValue(rightNumbe)));
					examScore.setWrongNumber((int)Double.parseDouble(getValue(wrongNumber)));
					examScore.setAccuracy(Double.parseDouble(getValue(rightNumbe))/(Double.parseDouble(getValue(rightNumbe))+Double.parseDouble(getValue(wrongNumber))));
					
					if(examScore.getRightNumbe()+examScore.getWrongNumber()!=examPaper.getTestQuestionCount()){
						return examScore.getEmpName()+"("+examScore.getCode()+")的题目总数不正确!";
					}else if(examScore.getTotalPoints()>examPaper.getFullMark()||examScore.getTotalPoints()<0){
						return examScore.getEmpName()+"("+examScore.getCode()+")的分数填写错误!";
					}else if(examScore.getExamTotalTime()>examPaper.getExamTime()*60||examScore.getExamTotalTime()<0){
						return examScore.getEmpName()+"("+examScore.getCode()+")的考试时长错误!";
					}else if(!"N".equals(examScore.getGradeState())&&!"Y".equals(examScore.getGradeState())){
						return examScore.getEmpName()+"("+examScore.getCode()+")的成绩状态错误!";
					}
					
					TomExamScore scoreExample=new TomExamScore();
					scoreExample.setCode(examScore.getCode());
					scoreExample.setExamId(examId);
					
					List<TomExamScore> selectListByExample = examScoreMapper.selectListByExample(scoreExample);
					if(selectListByExample.size()<=0){
						return examScore.getEmpName()+"("+examScore.getCode()+")不属于本次考试！";
					}else if(selectListByExample.size()==1){
						//判断导入次数
						int remainingCount=examScoreService.getRemainingCount(getValue(code),examId);
						if(remainingCount!=0&&exam.getRetakingExamCount()+1-selectListByExample.get(0).getExamCount()>=remainingCount){
							examScore.setExamCount(selectListByExample.get(0).getExamCount()+1);
							
							TomRetakingExam retakingExample=new TomRetakingExam();
							retakingExample.setExamId(examId);
							retakingExample.setSort(exam.getRetakingExamCount()+1-remainingCount);
							TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
							
							if(retakingExam.getRetakingExamBeginTime().before(new Date())){
								if(!selectListByExample.get(0).getGradeState().equals("Y")){
									examScore.setCreateTime(retakingExam.getRetakingExamEndTime());
									examScores.add(examScore);
								}
							}else{
								return "已经完成导入，不能重复导入。";
							}
							
						}else{
							return "已经完成导入，不能重复导入。";
						}
					}else{
						return examScore.getCode()+"系统数据异常。";
					}
				}
			}
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		for(TomExamScore examScore:examScores){
			examScoreMapper.updateSelective(examScore);
			
			//插入考试记录
			TomJoinExamRecord joinExamRecord=new TomJoinExamRecord();
			joinExamRecord.setCode(examScore.getCode());
			joinExamRecord.setExamid(examId);
			joinExamRecord.setCreateTime(examScore.getCreateTime());
			joinExamRecordMapper.insertSelective(joinExamRecord);
			
			TomEbRecord ebRecord=new TomEbRecord();
			ebRecord.setCode(examScore.getCode());
			ebRecord.setUpdateTime(new Date());
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(examScore.getCode());
			if(examScore.getGradeState().equals("N")&&examScoreService.getRemainingCount(examScore.getCode(),examId)<=0){
				if (userInfo.geteNumber()>=examPaper.getNotPassEb()) {
					userInfo.seteNumber(userInfo.geteNumber()- examPaper.getNotPassEb());
				}else{
					userInfo.seteNumber(0);
				}
				ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
				ebRecord.setRoad("9");
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				userInfoMapper.updateByCode(userInfo);
				ebRecordMapper.insertSelective(ebRecord);
			}else if(examScore.getGradeState().equals("Y")){
//				Map<Object, Object> map1 = new HashMap<Object, Object>();
//				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//				map1.put("userId", examScore.getCode());
//				map1.put("road", "3");
//				map1.put("date", simple.format(new Date()));
//				List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map1);
//				int ebCount=0;
//				for(TomEbRecord er:ebRList){
//					ebCount+=er.getExchangeNumber();
//				}
//				if(ebCount+examPaper.getPassEb()<=100){
					userInfo.seteNumber(userInfo.geteNumber()+examPaper.getPassEb());
					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+examPaper.getPassEb());
					ebRecord.setExchangeNumber(examPaper.getPassEb());
					ebRecord.setRoad("3");
					//设置主从库查询
					DBContextHolder.setDbType(DBContextHolder.MASTER);
					userInfoMapper.updateByCode(userInfo);
					ebRecordMapper.insertSelective(ebRecord);
//				}else{
//					userInfo.seteNumber(userInfo.geteNumber()+100-ebCount);
//					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+100-ebCount);
//					ebRecord.setExchangeNumber(100-ebCount);
//					ebRecord.setRoad("3");
//					userInfoMapper.updateByCode(userInfo);
//					ebRecordMapper.insertSelective(ebRecord);
//				}
			}
		}
		return "导入成功！";
	}

	/**
	 * 
	 * 功能描述：[读取xls]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月25日 上午9:57:08
	 * @param filePath
	 * @param examId
	 */
	@Transactional
	private String readXls(String filePath, int examId) throws IOException {
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam exam=examMapper.selectByPrimaryKey(examId);
		TomExamPaper examPaper=examPaperMapper.selectByExamId(examId);
		List<TomExamScore> examScores=new ArrayList<TomExamScore>();
		
		InputStream is = new FileInputStream(filePath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow!=null&&hssfRow.getCell(0) != null&&!"".equals(hssfRow.getCell(0))) {
					TomExamScore examScore = new TomExamScore();
					
					HSSFCell examNumber = hssfRow.getCell(0);
					if(!exam.getExamNumber().equals(getValue(examNumber))){
						return "并非此考试的成绩!";
					}
					if(exam.getBeginTime().after(new Date())){
						return "考试并未开始";
					}
					
					HSSFCell empName = hssfRow.getCell(1);
					HSSFCell code = hssfRow.getCell(2);
					HSSFCell gradeState = hssfRow.getCell(3);
					HSSFCell totalPoints = hssfRow.getCell(4);
					HSSFCell examTotalTime = hssfRow.getCell(5);
					HSSFCell rightNumbe = hssfRow.getCell(6);
					HSSFCell wrongNumber = hssfRow.getCell(7);
					examScore.setCode(String.valueOf(getValue(code)));
					examScore.setExamId(examId);
					examScore.setEmpName(getValue(empName));
					examScore.setGradeState(getValue(gradeState));
					examScore.setTotalPoints((int)Double.parseDouble(getValue(totalPoints)));
					examScore.setExamTotalTime((int)Double.parseDouble(getValue(examTotalTime))*60);
					examScore.setRightNumbe((int)Double.parseDouble(getValue(rightNumbe)));
					examScore.setWrongNumber((int)Double.parseDouble(getValue(wrongNumber)));
					examScore.setAccuracy(Double.parseDouble(getValue(rightNumbe))/(Double.parseDouble(getValue(rightNumbe))+Double.parseDouble(getValue(wrongNumber))));
					
					if(examScore.getRightNumbe()+examScore.getWrongNumber()!=examPaper.getTestQuestionCount()){
						return examScore.getEmpName()+"("+examScore.getCode()+")的题目总数不正确!";
					}else if(examScore.getTotalPoints()>examPaper.getFullMark()||examScore.getTotalPoints()<0){
						return examScore.getEmpName()+"("+examScore.getCode()+")的分数填写错误!";
					}else if(examScore.getExamTotalTime()>examPaper.getExamTime()*60||examScore.getExamTotalTime()<0){
						return examScore.getEmpName()+"("+examScore.getCode()+")的考试时长错误!";
					}else if(!"N".equals(examScore.getGradeState())&&!"Y".equals(examScore.getGradeState())){
						return examScore.getEmpName()+"("+examScore.getCode()+")的成绩状态错误!";
					}
					
					TomExamScore scoreExample=new TomExamScore();
					scoreExample.setCode(examScore.getCode());
					scoreExample.setExamId(examId);
					
					//查询员工的现有成绩
					List<TomExamScore> selectListByExample = examScoreMapper.selectListByExample(scoreExample);
					if(selectListByExample.size()<=0){
						//没有则不属于本次考试
						return examScore.getEmpName()+"("+examScore.getCode()+")不属于本次考试！";
					}else if(selectListByExample.size()==1){
						//剩余补考次数
						int remainingCount=examScoreService.getRemainingCount(getValue(code),examId);
						//在还有剩余补考次数的情况下
						if(remainingCount!=0&&exam.getRetakingExamCount()+1-selectListByExample.get(0).getExamCount()>=remainingCount){
							examScore.setExamCount(selectListByExample.get(0).getExamCount()+1);
							
							TomRetakingExam retakingExample=new TomRetakingExam();
							retakingExample.setExamId(examId);
							//sort为下次导入对应的考试
							retakingExample.setSort(exam.getRetakingExamCount()+1-remainingCount);
							TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
							//并未到下次导入时间
							if(retakingExam.getRetakingExamBeginTime().before(new Date())){
								//对于已经通过的成绩不做处理
								if(!selectListByExample.get(0).getGradeState().equals("Y")){
									//将创建时间设置为本次考试的结束时间。（判断一次成绩属于哪次补考是通过此createTime大于开始时间小于等于结束时间）
									examScore.setCreateTime(retakingExam.getRetakingExamEndTime());
									//需要更新的成绩放入list
									examScores.add(examScore);
								}
							}else{
								//throw new EleException("已经完成导入，不能重复导入。");
								return "已经完成导入，不能重复导入。";
							}
						}else{
//							throw new EleException("已经完成导入，不能重复导入。");
							return "已经完成导入，不能重复导入。";
						}
					}else{
						//线下考试成绩出现多条异常
						return examScore.getCode()+"系统数据异常。";
					}
				}
			}
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		for(TomExamScore examScore:examScores){
			examScoreMapper.updateSelective(examScore);
			
			//插入考试记录
			TomJoinExamRecord joinExamRecord=new TomJoinExamRecord();
			joinExamRecord.setCode(examScore.getCode());
			joinExamRecord.setExamid(examId);
			joinExamRecord.setCreateTime(examScore.getCreateTime());
			joinExamRecordMapper.insertSelective(joinExamRecord);
			
			TomEbRecord ebRecord=new TomEbRecord();
			ebRecord.setCode(examScore.getCode());
			ebRecord.setUpdateTime(new Date());
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(examScore.getCode());
			if(examScore.getGradeState().equals("N")&&examScoreService.getRemainingCount(examScore.getCode(),examId)<=0){
				if (userInfo.geteNumber()>=examPaper.getNotPassEb()) {
					userInfo.seteNumber(userInfo.geteNumber()- examPaper.getNotPassEb());
				}else{
					userInfo.seteNumber(0);
				}
				ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
				ebRecord.setRoad("9");
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				userInfoMapper.updateByCode(userInfo);
				ebRecordMapper.insertSelective(ebRecord);
			}else if(examScore.getGradeState().equals("Y")){
//				Map<Object, Object> map1 = new HashMap<Object, Object>();
//				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//				map1.put("userId", examScore.getCode());
//				map1.put("road", "3");
//				map1.put("date", simple.format(new Date()));
//				List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map1);
//				int ebCount=0;
//				for(TomEbRecord er:ebRList){
//					ebCount+=er.getExchangeNumber();
//				}
//				if(ebCount+examPaper.getPassEb()<=100){
					userInfo.seteNumber(userInfo.geteNumber()+examPaper.getPassEb());
					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+examPaper.getPassEb());
					ebRecord.setExchangeNumber(examPaper.getPassEb());
					ebRecord.setRoad("3");
					//设置主从库查询
					DBContextHolder.setDbType(DBContextHolder.MASTER);
					userInfoMapper.updateByCode(userInfo);
					ebRecordMapper.insertSelective(ebRecord);
//				}else{
//					userInfo.seteNumber(userInfo.geteNumber()+100-ebCount);
//					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+100-ebCount);
//					ebRecord.setExchangeNumber(100-ebCount);
//					ebRecord.setRoad("3");
//					userInfoMapper.updateByCode(userInfo);
//					ebRecordMapper.insertSelective(ebRecord);
//				}
			}
		}
		return "导入成功！";
	}
	
	/**
	 * 
	 * 功能描述：[类型处理]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:20:16
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
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:20:32
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
