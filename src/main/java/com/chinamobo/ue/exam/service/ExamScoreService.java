package com.chinamobo.ue.exam.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.api.exam.RedisData;
import com.chinamobo.ue.api.exam.service.ExamApiService;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.common.servise.Master_Slave;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.exam.dao.TomExamAnswerDetailsMapper;
import com.chinamobo.ue.exam.dao.TomExamEmpAllocationMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamAnswerDetails;
import com.chinamobo.ue.exam.entity.TomExamEmpAllocation;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelEmpRelationMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomLabelEmpRelation;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MapManager;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 考试成绩service
 *
 * 作者: JCX
 * 创建时间: 2016年3月18日 下午5:25:47
 */
@Service
public class ExamScoreService {
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private TomExamEmpAllocationMapper examEmpAllocationMapper;
	@Autowired
	private TomExamAnswerDetailsMapper examAnswerDetailsMapper;
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	@Autowired
	private TomUserInfoMapper userInfoMapper;
	@Autowired
	private TomEbRecordMapper ebRecordMapper;
	@Autowired
	private TomTopicMapper topicMapper;
	@Autowired
	private TomExamQuestionMapper examQuestionMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomLabelEmpRelationMapper tomLabelEmpRelationMapper;
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	String filePath=Config.getProperty("file_path");
	JsonMapper jsonMapper = new JsonMapper();
	RedisData redisData = new RedisData();
	
	/**
	 * 
	 * 功能描述：[分页查询考试成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月18日 下午5:26:01
	 * @param pageNum
	 * @param pageSize
	 * @param examId
	 * @return
	 */
	public PageData<TomExamScore> selectByPage(int pageNum, int pageSize, int examId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//扫描单一考试的过期状态
		TomExamEmpAllocation example=new TomExamEmpAllocation();
		example.setExamId(examId);
		List<TomExamEmpAllocation> examEmpAllocations=examEmpAllocationMapper.selectListByExample(example);
		for(TomExamEmpAllocation examEmpAllocation:examEmpAllocations){
			if(examEmpAllocation.getType().equals("D")){
				List<TomEmp> empList = new ArrayList<TomEmp>();
				if(examEmpAllocation.getCode().equals("1")){
					//全查人员
					empList = tomEmpMapper.selectAllDept();
				}else{
					//查询部门下所有人
					empList = tomEmpMapper.selectByDeptCode(examEmpAllocation.getCode());
				}
				if(empList.size()>0){
					for(TomEmp emp : empList){
						reSetStatusByExam(emp.getCode(),examId);
					}
				}
			}else if(examEmpAllocation.getType().equals("L")){
				//查询标签人员
				List<TomLabelEmpRelation> labelEmpList = tomLabelEmpRelationMapper.selectBytagIdList(examEmpAllocation.getCode());
				if(labelEmpList.size()>0){
					for(TomLabelEmpRelation labelEmp : labelEmpList){
						reSetStatusByExam(labelEmp.getCode(),examId);
					}
				}
			}else if(examEmpAllocation.getType().equals("C")){
				//查询标签分类人员
				List<TomLabelEmpRelation> labelClassEmpList = tomLabelClassRelationMapper.selectLabelClassEmpList(Integer.valueOf(examEmpAllocation.getCode()));
				if(labelClassEmpList.size()>0){
					for(TomLabelEmpRelation labelClassEmp : labelClassEmpList){
						reSetStatusByExam(labelClassEmp.getCode(),examId);
					}
				}
			}else if(examEmpAllocation.getType().equals("LE")){
				reSetStatusByExam(examEmpAllocation.getCode(),examId);
			}else if(examEmpAllocation.getType().equals("E")){
				reSetStatusByExam(examEmpAllocation.getCode(),examId);
			}
			//reSetStatusByExam(examEmpAllocation.getCode(),examId);
		}
		PageData<TomExamScore> page=new PageData<TomExamScore>();
		int count=examScoreMapper.countByExamId(examId);
		if(pageSize==-1){
			pageSize=count;
		}
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum",pageSize);//pageNum * 
		map.put("examId", examId);
		List<TomExamScore> list = examScoreMapper.selectListByPage(map);
		for(TomExamScore examScore:list){
			examScore.setGradeState(MapManager.ScoreParam(examScore.getGradeState()));
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 
	 * 功能描述：[主观题评分，刷新成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月5日 下午4:40:22
	 * @param topicIds
	 * @param scores
	 */
	@Transactional
	public void grade(int examId,String code,List<Integer> topicIds, List<Integer> scores,List<Integer> rightScores) {
		int totalPoints=0;
		int rightNumbe=0;
		int wrongNumber=0;
		
		TomExamAnswerDetails record=new TomExamAnswerDetails();
		record.setCode(code);
		record.setExamId(examId);

		TomExamScore scoreExample=new TomExamScore();
		scoreExample.setCode(code);
		scoreExample.setExamId(examId);
		scoreExample.setGradeState("P");
		List<TomExamScore> examples=examScoreMapper.selectListByExample(scoreExample);
		if(examples!=null){
			TomExamScore example=examples.get(0);
		
			TomExamPaper examPaper=examPaperMapper.selectByPrimaryKey(examMapper.selectByPrimaryKey(examId).getExamPaperId());
			for(int i=0;i<scores.size();i++){
				if(scores.get(i)!=null){
					record.setTopicId(topicIds.get(i));
	//				TomExamAnswerDetails examAnswerDetails=examAnswerDetailsMapper.selectOneByExample(record);
					record.setScore(scores.get(i));
					if(rightScores.get(i)==scores.get(i)){
						record.setRightState("Y");
						rightNumbe+=1;
//						example.setRightNumbe(example.getRightNumbe()+1);
					}else{
						record.setRightState("N");
						wrongNumber+=1;
//						example.setWrongNumber(example.getWrongNumber()+1);
					}
					totalPoints+=record.getScore();
//					example.setTotalPoints(example.getTotalPoints()+record.getScore());
					//更新答题详情
					examAnswerDetailsMapper.updateGrade(record);
				}
			}
			//更新成绩
			TomExamScore scoreExample1=new TomExamScore();
			scoreExample1.setExamId(examId);
			scoreExample1.setCode(code);
			TomExamScore lastexamScore=examScoreMapper.selectOneByExample(scoreExample1);
			
			example.setRightNumbe(rightNumbe);
			example.setWrongNumber(wrongNumber);
			example.setTotalPoints(totalPoints);
			example.setAccuracy((double)example.getRightNumbe()/examPaper.getTestQuestionCount());
			
			TomExamScore examScoreExample=new TomExamScore();
			examScoreExample.setCode(code);
			examScoreExample.setExamId(examId);
			examScoreExample.setGradeState("P");
			examScoreMapper.deleteByExample(examScoreExample);
			if(lastexamScore.getTotalPoints()>example.getTotalPoints()){
				lastexamScore.setExamCount(lastexamScore.getExamCount()+1);
				lastexamScore.setCreateTime(example.getCreateTime());
				examScoreMapper.updateSelective(lastexamScore);
				
				TomExamAnswerDetails answerDetails=new TomExamAnswerDetails();
				answerDetails.setExamId(examId);
				answerDetails.setCode(code);
				answerDetails.setStatus("N");
				examAnswerDetailsMapper.deleteByExample(answerDetails);
			}else if(lastexamScore.getTotalPoints()<example.getTotalPoints()){
				
				TomExamAnswerDetails answerDetails=new TomExamAnswerDetails();
				answerDetails.setExamId(examId);
				answerDetails.setCode(code);
				answerDetails.setStatus("Y");
				examAnswerDetailsMapper.deleteByExample(answerDetails);
				
				if(example.getTotalPoints()>=examPaper.getPassMark()){
					example.setGradeState("Y");
				}else{
					example.setGradeState("N");
				}
				examScoreMapper.updateSelective(example);
				
				for(int topicId:topicIds){
					answerDetails.setTopicId(topicId);
					examAnswerDetailsMapper.updateSelective(answerDetails);
				}
				
			}else{
				if(lastexamScore.getExamTotalTime()<example.getExamTotalTime()&&!lastexamScore.getGradeState().equals("D")){
					lastexamScore.setExamCount(lastexamScore.getExamCount()+1);
					lastexamScore.setCreateTime(example.getCreateTime());
					examScoreMapper.updateSelective(lastexamScore);
					
					TomExamAnswerDetails answerDetails=new TomExamAnswerDetails();
					answerDetails.setExamId(examId);
					answerDetails.setCode(code);
					answerDetails.setStatus("N");
					examAnswerDetailsMapper.deleteByExample(answerDetails);
				}else{
					TomExamAnswerDetails answerDetails=new TomExamAnswerDetails();
					answerDetails.setExamId(examId);
					answerDetails.setCode(code);
					answerDetails.setStatus("Y");
					examAnswerDetailsMapper.deleteByExample(answerDetails);
					
					if(example.getTotalPoints()>=examPaper.getPassMark()){
						example.setGradeState("Y");
					}else{
						example.setGradeState("N");
					}
					examScoreMapper.updateSelective(example);
					
					for(int topicId:topicIds){
						answerDetails.setTopicId(topicId);
						examAnswerDetailsMapper.updateSelective(answerDetails);
					}
				}
			}
			
			TomEbRecord ebRecord=new TomEbRecord();
			ebRecord.setCode(code);
			ebRecord.setUpdateTime(new Date());
			TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(code);
			if(example.getGradeState().equals("N")&&getRemainingCount(code,examId)<=1){
				if (userInfo.geteNumber()>=examPaper.getNotPassEb()) {
					userInfo.seteNumber(userInfo.geteNumber()- examPaper.getNotPassEb());
				}else{
					userInfo.seteNumber(0);
				}
				ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
				ebRecord.setRoad("9");
				userInfoMapper.updateByCode(userInfo);
				ebRecordMapper.insertSelective(ebRecord);
			}else if(example.getGradeState().equals("Y")){
//				Map<Object, Object> map1 = new HashMap<Object, Object>();
//				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//				map1.put("userId", code);
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
	}

	/**
	 * 
	 * 功能描述：[导出成绩模板]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月9日 下午8:08:57
	 * @param topics
	 * @param fileName
	 * @return
	 */
	public String TopicsToExcel(List<TomExamScore> examScores,String examNumber,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("考试编号");
		headers.add("员工姓名");
		headers.add("员工ID");
		headers.add("成绩状态（合格：Y，不合格：N）");
		headers.add("总分");
		headers.add("考试用时（分钟）");
		headers.add("正确数");
		headers.add("错误数");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
  
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
  
        for (int i = 0; i < examScores.size(); i++)  
        {  
            row = sheet.createRow((int) i + 1);  
            TomExamScore examScore =  examScores.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(examNumber);  
            row.createCell((short) 1).setCellValue(examScore.getEmpName());  
            row.createCell((short) 2).setCellValue(examScore.getCode()); 
        }  
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try  
        { 
        	
//        	System.out.println(File.separator);
        	
        	File file = new File(path);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
            FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
            wb.write(fout);  
            fout.close();  
           
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        
        return path+"/"+fileName;
	}
	
	/**
	 * 
	 * 功能描述：[将过期考试设置为不合格,过期考试扣除e币]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月10日 上午9:16:50
	 * @param userId
	 */
	@Transactional
	public void reSetStatus(String userId){
		Map<Object,Object> queryMap=new HashMap<Object, Object>();
		queryMap.put("userId",userId);
		queryMap.put("gradeState1","P");
		queryMap.put("gradeState2","D");
		int count=examMapper.countMyExam(queryMap);
		queryMap.put("startNum",0);
		queryMap.put("endNum",count);
		//设置主从库查询
		List<TomExam> list=examMapper.selectMyExam(queryMap);
		for(TomExam exam:list){
			TomExamPaper examPaper=examPaperMapper.selectByExamId(exam.getExamId());
			
			TomExamScore scoreExample=new TomExamScore();
			scoreExample.setExamId(exam.getExamId());
			scoreExample.setCode(userId);
			TomExamScore examScore=examScoreMapper.selectListByExample(scoreExample).get(0);
			if(examScoreMapper.countByExample(scoreExample)==1){
				
				if(exam.getEndTime().getTime()<new Date().getTime()&&examScore.getGradeState().equals("D")&&exam.getOfflineExam().equals("1")){
					examScore.setGradeState("N");
					examScore.setWrongNumber(examPaper.getTestQuestionCount());
					
				}
			}
			
			TomRetakingExam retakingExample=new TomRetakingExam();
			retakingExample.setExamId(exam.getExamId());
			retakingExample.setSort(exam.getRetakingExamCount());
			TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
			Date nowDate=new Date();
			if(examScore.getGradeState().equals("N")&&examScore.getCreateTime().getTime()<=retakingExam.getRetakingExamBeginTime().getTime()&&nowDate.getTime()>retakingExam.getRetakingExamEndTime().getTime()){
				TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(userId);
				
				TomEbRecord ebRecord=new TomEbRecord();
				ebRecord.setCode(userId);
				ebRecord.setUpdateTime(new Date());
				ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
				ebRecord.setRoad("9");
				if (userInfo.geteNumber()>=examPaper.getNotPassEb()) {
					userInfo.seteNumber(userInfo.geteNumber()- examPaper.getNotPassEb());
				}else{
					userInfo.seteNumber(0);
				}
				
				examScore.setCreateTime(retakingExam.getRetakingExamEndTime());
				userInfoMapper.updateByCode(userInfo);
				ebRecordMapper.insertSelective(ebRecord);
				examScoreMapper.updateSelective(examScore);
			}
		}
	}
	
	/**
	 * 
	 * 功能描述：[扫描单一考试的过期状态]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月12日 下午5:34:27
	 * @param userId
	 */
	@Transactional
	public void reSetStatusByExam(String userId,int examId){
		TomExamPaper examPaper=examPaperMapper.selectByExamId(examId);
		TomExam exam=examMapper.selectByPrimaryKey(examId);
		TomExamScore scoreExample=new TomExamScore();
		scoreExample.setExamId(examId);
		scoreExample.setCode(userId);
		TomExamScore examScore=examScoreMapper.selectListByExample(scoreExample).get(0);
		if(examScoreMapper.countByExample(scoreExample)==1){
			if(exam.getEndTime().getTime()<new Date().getTime()&&examScore.getGradeState().equals("D")&&exam.getOfflineExam().equals("1")){
				examScore.setGradeState("N");
				examScore.setWrongNumber(examPaper.getTestQuestionCount());
			}
		}
		TomRetakingExam retakingExample=new TomRetakingExam();
		retakingExample.setExamId(exam.getExamId());
		retakingExample.setSort(exam.getRetakingExamCount());
		TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
		Date nowDate=new Date();
		if(examScore.getGradeState().equals("N")&&examScore.getCreateTime().getTime()<=retakingExam.getRetakingExamBeginTime().getTime()&&nowDate.getTime()>retakingExam.getRetakingExamEndTime().getTime()){
			TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(userId);
			TomEbRecord ebRecord=new TomEbRecord();
			ebRecord.setCode(userId);
			ebRecord.setUpdateTime(new Date());
			ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
			ebRecord.setRoad("9");
			if (userInfo.geteNumber()>=examPaper.getNotPassEb()) {
				userInfo.seteNumber(userInfo.geteNumber()- examPaper.getNotPassEb());
			}else{
				userInfo.seteNumber(0);
			}
			examScore.setCreateTime(retakingExam.getRetakingExamEndTime());
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			userInfoMapper.updateByCode(userInfo);
			ebRecordMapper.insertSelective(ebRecord);
			examScoreMapper.updateSelective(examScore);
		}
	}
	
	/**
	 * 
	 * 功能描述：[计算剩余补考次数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月10日 上午9:17:41
	 * @param userId
	 * @param examId
	 * @return
	 */
	public int getRemainingCount(String userId,int examId){
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam exam=examMapper.selectByPrimaryKey(examId);
		int count=0;
		TomExamScore scoreExample=new TomExamScore();
		scoreExample.setExamId(examId);
		scoreExample.setCode(userId);
		TomExamScore examScore;
		int resultNum=examScoreMapper.countByExample(scoreExample);
		if(resultNum==2){
			scoreExample.setGradeState("P");
			examScore= examScoreMapper.selectListByExample(scoreExample).get(0);
		}else if(resultNum==1){
			examScore=examScoreMapper.selectListByExample(scoreExample).get(0);
		}else{
			System.out.println("用户id："+userId+"  考试id："+examId+"数据错误！");
			return count+1;
		}
		
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("examId",examId);
		map.put("date",examScore.getCreateTime());
		TomRetakingExam retakingExam=retakingExamMapper.selectByTime(map);
		if(retakingExam!=null){
			if(new Date().getTime()<=retakingExam.getRetakingExamEndTime().getTime()){
				if (null != exam.getRetakingExamCount() && null != retakingExam.getSort()) {
					count=exam.getRetakingExamCount()- retakingExam.getSort();
				}
			}else{
				TomRetakingExam example=new TomRetakingExam();
				example.setExamId(examId);
				List<TomRetakingExam> retakingExams=retakingExamMapper.selectListByExample(example);
				for(TomRetakingExam retakingExam1:retakingExams){
					if(retakingExam1.getRetakingExamEndTime().getTime()>=new Date().getTime()){
						count=exam.getRetakingExamCount()-retakingExam1.getSort()+1;
						break;
					}
				}
			}
		}else{
			TomRetakingExam example=new TomRetakingExam();
			example.setExamId(examId);
			List<TomRetakingExam> retakingExams=retakingExamMapper.selectListByExample(example);
			for(TomRetakingExam retakingExam1:retakingExams){
				if(retakingExam1.getRetakingExamEndTime().getTime()>=new Date().getTime()){
					count=exam.getRetakingExamCount()-retakingExam1.getSort()+1;
					break;
				}
			}
		}
		return count;
	}
/**
 * 
 * @Title: canExecute 
 * @Description: 是否可从队列插入数据库 
 * @author Acemon 
 * @date 2017年5月22日 下午1:47:52
 * @return boolean
 */
	public boolean canExecute(String userId, int examId, Date createTime) {
		//设置主从库查询
		//DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam exam = redisData.getExamFromRedis(examId + "");
		TomExamPaper examPaper = redisData.getExamPaperFromRedis(exam.getExamPaperId() + "");
		List<TomExamQuestion> examQuestions = examQuestionMapper.selectByExamPaperId(examPaper.getExamPaperId());

		boolean hasSubjectiveTopic = false;// 考试有无主观题
		for (TomExamQuestion examQuestion : examQuestions) {
			if (examQuestion.getQuestionType().equals("3") || examQuestion.getQuestionType().equals("4")) {
				hasSubjectiveTopic = true;
			}
		}

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("examId", examId);
		map.put("date", createTime);
		TomRetakingExam retakingExam = retakingExamMapper.selectByTime(map);

		// 查询公共条件
		TomExamScore scoreExample = new TomExamScore();
		scoreExample.setCode(userId);
		scoreExample.setExamId(examId);
		scoreExample.setGradeState("P");
		if (hasSubjectiveTopic) {// 有主观题时, 判断有无为P的记录, 有则不可插入, 无则可插入
			List<TomExamScore> examScores = examScoreMapper.selectListByExample(scoreExample);
			return examScores.size() <= 0;
		} else {
			if (retakingExam.getSort() == 0) {// 正考
				// 无主观题时, 判断有无不为D的记录, 有则不可更新, 无则可更新
				List<TomExamScore> examScoresNotD = examScoreMapper.selectListNotD(scoreExample);
				return examScoresNotD.size() <= 0;
			} else {// 补考, 考试成绩创建时间有无在此补考时间之内, 如已有在补考时间内的成绩 ,则不可更新, 反之则可更新
				scoreExample.setGradeState("N");
				List<TomExamScore> examScores = examScoreMapper.selectListByExample(scoreExample);
				// 取出上一次考的成绩,Y或N或D(可能未参加考试), 
				TomExamScore tomExamScore =null;
				if (null != examScores && examScores.size() >0) {
					tomExamScore = examScores.get(0);
				}
				if ( tomExamScore != null && tomExamScore.getCreateTime().getTime() <= retakingExam.getRetakingExamEndTime().getTime()
						&& tomExamScore.getCreateTime().getTime() > retakingExam.getRetakingExamBeginTime().getTime()) {
					// 成绩提交时间 在补考时间之内
					return false;
				} else {
					return true;
				}
			}
		}
	}
	
}
