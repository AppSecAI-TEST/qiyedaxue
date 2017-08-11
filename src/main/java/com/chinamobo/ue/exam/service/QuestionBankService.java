package com.chinamobo.ue.exam.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.common.restful.FileRest;
import com.chinamobo.ue.course.dto.CourseJsonDto;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.dao.TomQuestionBankMapper;
import com.chinamobo.ue.exam.dao.TomQuestionClassificationMapper;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.dto.OptionDto;
import com.chinamobo.ue.exam.dto.QuestionBankDto;
import com.chinamobo.ue.exam.dto.QuestionDto;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomQuestionBank;
import com.chinamobo.ue.exam.entity.TomQuestionClassification;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.exception.EleException;
import com.chinamobo.ue.system.dao.TomNumberRecordMapper;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.RedisUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class QuestionBankService {

	@Autowired
	private TomQuestionBankMapper bankMapper;
	@Autowired
	private TomQuestionClassificationMapper classificationMapper;
	@Autowired
	private TomNumberRecordMapper recordMapper;
	@Autowired
	private TomTopicMapper tomTopicMapper;
	@Autowired
	private TomTopicOptionMapper tomTopicOptionMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomExamQuestionMapper examQuestionMapper;
	
	JsonMapper mapper = new JsonMapper();

	String filePath=Config.getProperty("file_path");
	 
	public TomQuestionBank findOne(int id) {
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomQuestionBank b = bankMapper.selectByPrimaryKey(id);
		return b;
	}

	/**
	 * 
	 * 功能描述：[添加题库分类]
	 *
	 * 创建者：wjx 创建时间: 2016年2月25日 下午5:21:42
	 * 
	 * @param classification
	 */
	@Transactional
	public void addClassification(TomQuestionClassification classification) {	
		classification.setQuestionClassificationNumber(numberRecordService.getNumber(MapManager.numberType("TKFL")));
		if(classification.getParentClassificationId()==null){
			classification.setParentClassificationId(0);
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		classificationMapper.insertSelective(classification);
	}

	/**
	 * 
	 * 功能描述：[分类修改查询]
	 *
	 * 创建者：wjx 创建时间: 2016年2月26日 上午10:43:25
	 * 
	 * @param id
	 * @return
	 */
	public TomQuestionClassification queryClassification(int id) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomQuestionClassification questionClassification = classificationMapper.selectByPrimaryKey(id);
		if(questionClassification.getParentClassificationId()!=0){
			questionClassification.setParentClassificationName(classificationMapper.selectByPrimaryKey(questionClassification.getParentClassificationId()).getQuestionClassificationName());
		}else{
			questionClassification.setParentClassificationName("-");
		}
		
		return questionClassification;
	}

	/**
	 * 
	 * 功能描述：[题库分类修改]
	 *
	 * 创建者：wjx 创建时间: 2016年2月26日 上午10:46:19
	 * 
	 * @param classification
	 */
	@Transactional
	public void updateClassfication(TomQuestionClassification classification) {
		classificationMapper.updateByPrimaryKeySelective(classification);
	}

//	/**
//	 * 
//	 * 功能描述：[题库分类删除]
//	 *
//	 * 创建者：wjx 创建时间: 2016年3月2日 下午5:01:47
//	 * 
//	 * @param questionClassificationId
//	 */
//	public void deleteClassification(int questionClassificationId) {
//		classificationMapper.deleteByPrimaryKey(questionClassificationId);
//	}
	
//	/**
//	 * 
//	 * 功能描述：[批量删除题库分类]
//	 *
//	 * 创建者：wjx 创建时间: 2016年3月3日 上午11:17:52
//	 * 
//	 * @param id
//	 */
//	@Transactional
//	public void deleteClassifications(String id) {
//		String[] questionBanks = id.split(",");
//		int[] intTemp = new int[questionBanks.length];
//		for(int i = 1;i<=questionBanks.length;i++){
//			intTemp[i]=Integer.parseInt(questionBanks[i]);
//			int arc = intTemp[i];
//			classificationMapper.deleteByPrimaryKey(arc);
//		}
//	}
	
	
	/**
	 * 
	 * 功能描述：[查询题库-byId]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午10:43:52
	 * 
	 * @param questionBankId
	 * @return
	 */
	public TomQuestionBank findQuestionBank(int questionBankId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomQuestionBank questionBank = bankMapper.selectByPrimaryKey(questionBankId);
		TomQuestionClassification qc=classificationMapper.selectByPrimaryKey(questionBank.getQuestionClassificationId());
		questionBank.setQuestionClassificationName(qc.getQuestionClassificationName());
		//封装题目
		TomTopic topicExample=new TomTopic();
		topicExample.setQuestionBankId(questionBankId);
		List<TomTopic> topics = new ArrayList<TomTopic>();
		topics=	tomTopicMapper.selectByExample(topicExample);
		for(TomTopic topic:topics){
			topic.setTopicOptions(tomTopicOptionMapper.selectByTopicId(topic.getTopicId()));
		}
		questionBank.setTopics(topics);
		return questionBank;
	}
	
	/**
	 * 
	 * 功能描述：[题库分页查询]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月12日 下午5:35:08
	 * @param pageNum
	 * @param pageSize
	 * @param bankName
	 * @return
	 */
//	public PageData<TomQuestionBank> questionBankList(int pageNum, int pageSize,String bankName) {
//		PageData<TomQuestionBank> page=new PageData<TomQuestionBank>();
//		List<TomQuestionBank> list=new ArrayList<TomQuestionBank>();
//		TomQuestionBank questionBank=new TomQuestionBank();
//		questionBank.setQuestionBankName(bankName);
//		int count = bankMapper.count(questionBank);
//		if(pageSize==-1){
//			list=bankMapper.queryList(questionBank);
//			pageSize=count;
//		}else {
//			Map<Object, Object> map = new HashMap<Object, Object>();
//			map.put("startNum", (pageNum - 1) * pageSize);
//			map.put("endNum", pageNum * pageSize);
//			map.put("questionBankName", bankName);
//			list=bankMapper.selectPage(map);
//		}
//		for(TomQuestionBank questionBank2:list){
//			questionBank2.setQuestionClassificationName(classificationMapper.selectByPrimaryKey(questionBank2.getQuestionClassificationId()).getQuestionClassificationName());
//		}
//		page.setDate(list);
//		page.setPageNum(pageNum);
//		page.setPageSize(pageSize);
//		page.setCount(count);
//		return page;
//	}

	public PageData<QuestionBankDto> queryQuestionBank(int pageNum, int pageSize,String bankName) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
//		if(bankName!=null){
//			bankName=bankName.replaceAll("/", "//");
//			bankName=bankName.replaceAll("%", "/%");
//			bankName=bankName.replaceAll("_", "/_");
//			
//		}
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<QuestionBankDto> page=new PageData<QuestionBankDto>();
		List<TomQuestionBank> list=new ArrayList<TomQuestionBank>();
		List<QuestionBankDto> returnList=new ArrayList<QuestionBankDto>();
		TomQuestionBank questionBank=new TomQuestionBank();
		questionBank.setQuestionBankName(bankName);
		int count = bankMapper.count(questionBank);
		if(pageSize==-1){
			list=bankMapper.queryList(questionBank);
			pageSize=count;
		}else {
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("startNum", (pageNum - 1) * pageSize);
			map.put("endNum",  pageSize);//pageNum * 
			map.put("questionBankName", bankName);
			list=bankMapper.selectPage(map);
		}
		for(TomQuestionBank quetionBank:list){
			QuestionBankDto dto= new QuestionBankDto();
			dto.setQuestionBankId(quetionBank.getQuestionBankId());
			dto.setQuestionBankName(quetionBank.getQuestionBankName());
			dto.setQuestionBankNumber(quetionBank.getQuestionBankNumber());
			dto.setCreateTime(quetionBank.getCreateTime());
			dto.setQuestionClassificationId(quetionBank.getQuestionClassificationId());
			dto.setCreator(quetionBank.getCreator());
			dto.setLastOperator(quetionBank.getLastOperator());
			dto.setUpdateTime(quetionBank.getUpdateTime());
			Integer singleCount=0;//单选数量
			Integer mcqCount=0;//多选数量
			Integer gapCount=0;//填空题数量
			Integer essayCount=0;//问答题数量
			Integer tOrFCount=0;//判断题数量
			List<TomTopic> topicList=tomTopicMapper.selectByBankId(quetionBank.getQuestionBankId());
			for(TomTopic topic:topicList){
				if("1".equals(topic.getQuestionType())){
					singleCount++;
				}else if("2".equals(topic.getQuestionType())){
					mcqCount++;
				}else if("3".equals(topic.getQuestionType())){
					gapCount++;
				}else if("4".equals(topic.getQuestionType())){
					essayCount++;
				}else if("5".equals(topic.getQuestionType())){
					tOrFCount++;
				}
			}
			dto.setEssayCount(essayCount);
			dto.setGapCount(gapCount);
			dto.setMcqCount(mcqCount);
			dto.setSingleCount(singleCount);
			dto.settOrFCount(tOrFCount);
			returnList.add(dto);
		}
		for(QuestionBankDto questionBankDto:returnList){
			
			questionBankDto.setQuestionClassificationName(classificationMapper.selectByPrimaryKey(questionBankDto.getQuestionClassificationId()).getQuestionClassificationName());
		}
		page.setDate(returnList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
		
	/**
	 * 
	 * 功能描述：[添加题库]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午11:10:37
	 * 
	 * @param bank
	 * @throws Exception 
	 */
	@Transactional
	public void addQuestionBank(TomQuestionBank bank) throws Exception {
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		bank.setQuestionBankNumber(numberRecordService.getNumber(MapManager.numberType("TK")));
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		bankMapper.insertSelective(bank);
		excle(filePath+bank.getFilePath(), bank.getQuestionBankId());
		
	}
	@Transactional
	public void automatAddQuestionBank(String jsonStr) throws Exception{
		ShiroUser user=ShiroUtils.getCurrentUser();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		TomQuestionBank tomQuestionBank = JSON.parseObject(jsonStr, TomQuestionBank.class);//题库对象
//		TomQuestionBank tomQuestionBank = mapper.fromJson(jsonStr, TomQuestionBank.class);
		tomQuestionBank.setCreateTime(new Date());
		tomQuestionBank.setUpdateTime(new Date());
		tomQuestionBank.setCreator(user.getName());
		tomQuestionBank.setCreatorId(user.getCode());
		tomQuestionBank.setLastOperator(user.getName());
		tomQuestionBank.setLastOperatorId(user.getCode());
		tomQuestionBank.setQuestionBankNumber(numberRecordService.getNumber(MapManager.numberType("TK")));
		String questionBankName = jsonObject.getString("questionBankName");
		Integer questionClassificationId =Integer.valueOf(jsonObject.getString("questionClassificationId"));
		String admins = jsonObject.getString("admins");
		String filePath1=jsonObject.getString("filePath");
		tomQuestionBank.setQuestionBankName(questionBankName);
		tomQuestionBank.setQuestionClassificationId(questionClassificationId);
		tomQuestionBank.setFilePath(filePath1);
		tomQuestionBank.setAdmins(admins);
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		bankMapper.insertSelective(tomQuestionBank);
		
			List<TomTopic >handQuestions =tomQuestionBank.getHandQuestions();
			List<TomTopic> topicListForRedis = new ArrayList<TomTopic>();//缓存对象
			for(TomTopic handQuestionsList : handQuestions ){
				handQuestionsList.setTopicName(handQuestionsList.getQuestionName());
				handQuestionsList.setCreateTime(new Date());
				handQuestionsList.setQuestionBankId(tomQuestionBank.getQuestionBankId());
				if(!handQuestionsList.getQuestionImgUrl().equals("")){
					handQuestionsList.setQuestionFileUrl(handQuestionsList.getQuestionImgUrl());
				}
				handQuestionsList.setQuestionFileUrl(handQuestionsList.getQuestionFileUrl());
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				tomTopicMapper.insert(handQuestionsList);
				topicListForRedis.add(handQuestionsList);
				List<TomTopicOption>questionOptionsLists =handQuestionsList.getQuestionOptions();
				for(TomTopicOption questionOption:questionOptionsLists){
					if(handQuestionsList.getQuestionType().equals("4") || handQuestionsList.getQuestionType().equals("3")){
						questionOption.setOptionName(handQuestionsList.getQuestionAnswer());
						questionOption.setCreateTime(new Date());
						questionOption.setRight("Y");
					}else{
						questionOption.setCreateTime(new Date());
						questionOption.setOptionName(questionOption.getOptionKeyAndName());
//					questionOption.setOptionKeyAndName(questionOption.getOptionKeyAndName());
						String[] answers = handQuestionsList.getQuestionAnswer().split(",");
						questionOption.setRight("N");
						for(String answer:answers){
							if(questionOption.getOptionKeyAndName().substring(0, 1).equals(answer)){
								questionOption.setRight("Y");
							}
						}
					}
					questionOption.setOptionFileUrl(questionOption.getOptionFileUrl());
					questionOption.setTopicId(handQuestionsList.getTopicId());	
					//设置主从库查询
					DBContextHolder.setDbType(DBContextHolder.MASTER);
					tomTopicOptionMapper.insert(questionOption);
				}
			}
			tomQuestionBank.setHandQuestions(handQuestions);	
			addTopicAndOptionsToRedis(topicListForRedis);//加入缓存
	}
	/*
	 * 
	 * 追加题库
	 */
	@Transactional
	public void  automatSuperAddQuestionBank(String jsonStr){
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Integer questionBankId =Integer.valueOf(jsonObject.getString("questionBankId"));
		JsonMapper objectMapper = new JsonMapper();
		TomQuestionBank tomQuestionBank = objectMapper.fromJson(jsonStr,TomQuestionBank.class);//题库对象
		List<TomTopic >handQuestions =tomQuestionBank.getHandQuestions();
		//JSONArray tomQuestionBankinfos = jsonObject.getJSONArray("handQuestions");
//		List<TomTopic>handQuestionsLists = JSONArray.toList(tomQuestionBankinfos, TomTopic.class);
		// List<TomTopic> handQuestionsLists = JSONArray.toList(tomQuestionBankinfos, new TomTopic(), new JsonConfig());
		List<TomTopic> topicListForRedis = new ArrayList<TomTopic>();//缓存对象
		for(TomTopic handQuestionsList : handQuestions ){
			handQuestionsList.setTopicName(handQuestionsList.getQuestionName());
			handQuestionsList.setCreateTime(new Date());
			handQuestionsList.setQuestionBankId(questionBankId);
			if(!handQuestionsList.getQuestionImgUrl().equals("")){
				handQuestionsList.setQuestionFileUrl(handQuestionsList.getQuestionImgUrl());
			}
			handQuestionsList.setQuestionFileUrl(handQuestionsList.getQuestionFileUrl());
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			tomTopicMapper.insert(handQuestionsList);
			topicListForRedis.add(handQuestionsList);
			List<TomTopicOption>questionOptionsLists =handQuestionsList.getQuestionOptions();
			for(TomTopicOption questionOption:questionOptionsLists){
				if(handQuestionsList.getQuestionType().equals("4") || handQuestionsList.getQuestionType().equals("3")){
					questionOption.setOptionName(handQuestionsList.getQuestionAnswer());
					questionOption.setCreateTime(new Date());
					questionOption.setRight("Y");
				}else{
					questionOption.setCreateTime(new Date());
					questionOption.setOptionName(questionOption.getOptionKeyAndName());
//					questionOption.setOptionKeyAndName(questionOption.getOptionKeyAndName());
					String[] answers = handQuestionsList.getQuestionAnswer().split(",");
					questionOption.setRight("N");
					for(String answer:answers){
						if(questionOption.getOptionKeyAndName().substring(0, 1).equals(answer)){
							questionOption.setRight("Y");
						}
					}
				}
				questionOption.setOptionFileUrl(questionOption.getOptionFileUrl());
				questionOption.setTopicId(handQuestionsList.getTopicId());	
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				tomTopicOptionMapper.insert(questionOption);
			}
		}
		addTopicAndOptionsToRedis(topicListForRedis);//加入缓存
	}
	
	
	/**
	 * 
	 * 功能描述：[修改题库]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午11:17:17
	 * 
	 * @param bank
	 * @throws Exception 
	 */
	@Transactional
	public String updateQuestionBank(TomQuestionBank bank) throws Exception {
		
		TomExamQuestion example=new TomExamQuestion();
		example.setQuestionBankId(bank.getQuestionBankId());
		if(examQuestionMapper.selectListByExample(example).size()==0){
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			bankMapper.updateByPrimaryKeySelective(bank);
			TomTopicOption topicOption;
			for(int topicId:bank.getDeleteTopicIds()){
				tomTopicMapper.deleteByPrimaryKey(topicId);
				topicOption=new TomTopicOption();
				topicOption.setTopicId(topicId);
				tomTopicOptionMapper.deleteByExample(topicOption);
			}
		}else{
			return "N";
		}
		return "";
	}

	/**
	 * 
	 * 功能描述：[删除题库]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午11:17:52
	 * 
	 * @param id
	 */
	public String deleteQuestionBank(int id) {
		TomExamQuestion example=new TomExamQuestion();
		example.setQuestionBankId(id);
		if(examQuestionMapper.selectListByExample(example).size()==0){
			TomTopic topicExample=new TomTopic();
			topicExample.setQuestionBankId(id);
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			List<TomTopic> tomTopics = tomTopicMapper.selectByExample(topicExample);
			
			TomTopicOption topicOption;
			for(TomTopic topic:tomTopics){
				topicOption=new TomTopicOption();
				topicOption.setTopicId(topic.getTopicId());
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				tomTopicOptionMapper.deleteByExample(topicOption);
			}
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			tomTopicMapper.deleteByQuestionBankId(id);
			bankMapper.deleteByPrimaryKey(id);
		}else{
			return "N";
		}
		return "";
	}
	
	/**
	 * 
	 * 功能描述：[批量删除题库]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午11:17:52
	 * 
	 * @param id
	 */
	@Transactional
	public String deleteQuestionBanks(String id) {
		List<Integer> ids=new ArrayList<Integer>();
		String[] questionBanks = id.split(",");
		TomExamQuestion example;
		for(int i = 1;i<questionBanks.length;i++){		
			int arc =Integer.parseInt(questionBanks[i]);
			example=new TomExamQuestion();
			example.setQuestionBankId(arc);
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			if(examQuestionMapper.selectListByExample(example).size()==0){
				ids.add(arc);
			}else{
				return "N";
			}
		}
		
		for(int i:ids){	
			TomTopic topicExample=new TomTopic();
			topicExample.setQuestionBankId(i);
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<TomTopic> tomTopics = tomTopicMapper.selectByExample(topicExample);
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			TomTopicOption topicOption;
			for(TomTopic topic:tomTopics){
				topicOption=new TomTopicOption();
				topicOption.setTopicId(topic.getTopicId());
				tomTopicOptionMapper.deleteByExample(topicOption);
			}
			tomTopicMapper.deleteByQuestionBankId(i);
			bankMapper.deleteByPrimaryKey(i);
			
		}
		return "";
	}
	
	/**
	 * 
	 * 功能描述：[题库文件解析入口]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:19:31
	 * @param filePath
	 * @param questionBankId
	 * @throws Exception
	 */
	@Transactional
	public void excle(String filePath, int questionBankId) throws Exception {
		String path=filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		if("xls".equals(path)){
			readXls(filePath,questionBankId);
			
		}else if("xlsx".equals(path)){
			readXlsx(filePath, questionBankId);
		}
	}
	


	/**
	 * 
	 * 功能描述：[xlsx文件解析]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:19:43
	 * @param path
	 * @param questionBankId
	 * @throws IOException
	 */
	@Transactional
	public void readXlsx(String path, int questionBankId) throws Exception {

		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		List<TomTopic> topicListForRedis = new ArrayList<TomTopic>();// 需要写入缓存的题目
		List<TomTopicOption> tomTopicOptions=new ArrayList<TomTopicOption>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow!=null&&getValue(xssfRow.getCell(0))!= null&&!"".equals(getValue(xssfRow.getCell(0)))) {
					TomTopic topic = new TomTopic();
					XSSFCell type = xssfRow.getCell(0);
					XSSFCell name = xssfRow.getCell(1);
					XSSFCell rightCell = xssfRow.getCell(2);
					topic.setCreateTime(new Date());
					topic.setQuestionBankId(questionBankId);
					if(MapManager.TopicTypeParam(getValue(type))==null){
//							for(int id:topicIds){
//								tomTopicMapper.deleteByPrimaryKey(id);
//							}
//							return "题目类型超出范围";
						throw new EleException("questionTypeError");
					}
					topic.setQuestionType(MapManager.TopicTypeParam(getValue(type)));
					topic.setTopicName(getValue(name));
					//设置主从库查询
					DBContextHolder.setDbType(DBContextHolder.MASTER);
					tomTopicMapper.insert(topic);
					topicListForRedis.add(topic);
					int topicId = topic.getTopicId();
					
					boolean flag = false;
					if (topic.getQuestionType().equals("4")) {
						TomTopicOption tomTopicOption = new TomTopicOption();
						tomTopicOption.setCreateTime(new Date());
						if(rightCell==null||getValue(rightCell)==null||"".equals(getValue(rightCell))){
							tomTopicOption.setOptionName("略");
						}else{
							tomTopicOption.setOptionName(getValue(rightCell));
						}
						
						tomTopicOption.setTopicId(topicId);
						tomTopicOption.setRight("Y");
						tomTopicOptions.add(tomTopicOption);
						flag=true;
						
					}else{
						
						String rights[] = getValue(rightCell).split(",");
						List<String> rigthOpts = new ArrayList<String>();
						for (String s : rights) {
							rigthOpts.add(s);
						}
						int j = 3;
						
						while (true) {
							XSSFCell option = xssfRow.getCell(j);
							if (j > 28 || option == null|| "".equals(getValue(option))) {
								break;
							}

							TomTopicOption tomTopicOption = new TomTopicOption();
							tomTopicOption.setCreateTime(new Date());
							tomTopicOption.setOptionName((char) (62 + j)
									+ " " + getValue(option));
							tomTopicOption.setTopicId(topicId);
							tomTopicOption.setRight("N");

							for (String right : rigthOpts) {
								if (right.equals((char) (62 + j) + "")) {
									tomTopicOption.setRight("Y");
									flag = true;
								}
							}
							tomTopicOptions.add(tomTopicOption);
							j++;
						}
					}
					if(!flag){
//							for(int id:topicIds){
//								tomTopicMapper.deleteByPrimaryKey(id);
//							}
//							return "请为所有题目填写正确答案";
						throw new EleException("rightOptionError");
					}
				}
			}
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		for(TomTopicOption tomTopicOption:tomTopicOptions){
			tomTopicOptionMapper.insert(tomTopicOption);
		}
//		缓存操作
		addTopicAndOptionsToRedis(topicListForRedis);
	}
	/**
	 * 
	 * 功能描述：[xls文件解析]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:20:06
	 * @param path
	 * @param questionBankId
	 * @throws Exception 
	 * @throws IOException
	 */
	@Transactional
	public void readXls(String path, int questionBankId) throws Exception {
		InputStream is = new FileInputStream(path);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		List<TomTopic> topicListForRedis = new ArrayList<TomTopic>();//缓存对象
		List<TomTopicOption> tomTopicOptions=new ArrayList<TomTopicOption>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow!=null&&getValue(hssfRow.getCell(0)) != null&&!"".equals(getValue(hssfRow.getCell(0)))) {
					HSSFCell type = hssfRow.getCell(0);
					HSSFCell name = hssfRow.getCell(1);
					HSSFCell rightCell = hssfRow.getCell(2);
					TomTopic topic = new TomTopic();
					topic.setCreateTime(new Date());
					topic.setQuestionBankId(questionBankId);
					if(MapManager.TopicTypeParam(getValue(type))==null){
						throw new EleException("questionTypeError");
					}
					topic.setQuestionType(MapManager.TopicTypeParam(getValue(type)));
					
					topic.setTopicName(getValue(name));
					//设置主从库查询
					DBContextHolder.setDbType(DBContextHolder.MASTER);
					tomTopicMapper.insert(topic);
					topicListForRedis.add(topic);
					int topicId = topic.getTopicId();
					
					boolean flag=false;
					if (topic.getQuestionType().equals("4")) {
						TomTopicOption tomTopicOption = new TomTopicOption();
						tomTopicOption.setCreateTime(new Date());
						if(rightCell==null||getValue(rightCell)==null||"".equals(getValue(rightCell))){
							tomTopicOption.setOptionName("略");
						}else{
							tomTopicOption.setOptionName(getValue(rightCell));
						}
						tomTopicOption.setTopicId(topicId);
						tomTopicOption.setRight("Y");
						tomTopicOptions.add(tomTopicOption);
						flag=true;
						
					}else{
						String rights[]=getValue(rightCell).split(",");
						List<String> rigthOpts=new ArrayList<String>();
						for(String s:rights){
							rigthOpts.add(s);
						}
//					String rights[] = String.valueOf((int)rightCell.getNumericCellValue()).split(",");
						int j = 3;
						
						while (true) {
							HSSFCell option = hssfRow.getCell(j);
							if (j>28 || option==null||"".equals(getValue(option))){
								break;
							}
							TomTopicOption tomTopicOption = new TomTopicOption();
							tomTopicOption.setCreateTime(new Date());
							tomTopicOption.setOptionName((char)(62+j)+" "+getValue(option));
							tomTopicOption.setTopicId(topicId);
							tomTopicOption.setRight("N");
							
							for (String right : rigthOpts) {
								if(right.equals((char) (62 + j) + "")){
									tomTopicOption.setRight("Y");
									flag=true;
								}
							}
							
							tomTopicOptions.add(tomTopicOption);
							j++;
						}
					}
					if(!flag){
						
						throw new EleException("rightOptionError");
					}
				}
			}
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		for(TomTopicOption tomTopicOption:tomTopicOptions){
			tomTopicOptionMapper.insert(tomTopicOption);
		}
		addTopicAndOptionsToRedis(topicListForRedis);//加入缓存
	}
	
	
/**
 * 
 * @Title: addTopicAndOptionsToRedis 
 * @Description: 将题库的题目和选项加入缓存
 * @author Acemon 
 * @date 2017年5月18日 下午4:18:34
 * @return void
 */
	private void addTopicAndOptionsToRedis(List<TomTopic> topicListForRedis) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		for (TomTopic topic : topicListForRedis) {
			// 将题目加入缓存
			putTopicDataToRedis(topic);
			// 将题目的选项
			List<TomTopicOption> optionlist = tomTopicOptionMapper.selectByTopicId(topic.getTopicId());
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			for (TomTopicOption tomTopicOption : optionlist) {
				// 缓存题目选项
				RedisUtils.hset("tomTopicOption", tomTopicOption.getId().toString(), mapper.toJson(tomTopicOption));
			}
		}
	}

/**
 * 
 * @Title: putTopicDataToRedis 
 * @Description: 将新添加的题目及对应的选项放入缓存 
 * @author Acemon 
 * @date 2017年5月18日 下午4:19:28
 * @return void
 */
	private void putTopicDataToRedis(TomTopic tomTopic) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomTopicOption> topicOptions = tomTopicOptionMapper.selectByTopicIdNoStatus(tomTopic.getTopicId());
		tomTopic.setTopicOptions(topicOptions);
		RedisUtils.hset("tomTopic", tomTopic.getTopicId().toString() + ":" + tomTopic.getQuestionBankId().toString()
				+ ":" + tomTopic.getQuestionType(), mapper.toJson(tomTopic));

		topicOptions = tomTopicOptionMapper.selectByTopicId(tomTopic.getTopicId());// 查询带正确状态的选项
		tomTopic.setTopicOptions(topicOptions);
		RedisUtils.hset("tomTopicWithId", tomTopic.getTopicId().toString(), mapper.toJson(tomTopic));

		List<TomTopicOption> rightList = tomTopicOptionMapper.selectRightOption(tomTopic.getTopicId());
		;
		tomTopic.setTopicOptions(rightList);
		RedisUtils.hset("tomTopicRightOption", tomTopic.getTopicId().toString(), mapper.toJson(tomTopic));// 缓存正确答案
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
	
	/**
	 * 
	 * 功能描述：[题库导出为excel]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月9日 下午8:09:07
	 * @param topics
	 * @param fileName
	 * @return
	 */
	public String TopicsToExcel(List<TomTopic> topics,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("题目类型");
		headers.add("题目");
		headers.add("正确答案（数字表示“,”分隔）");
		int optionNum=0;
		for(TomTopic topic:topics){
			if(topic.getTopicOptions().size()>optionNum){
				optionNum=topic.getTopicOptions().size();
			}
		}
		for(int i=1;i<=optionNum;i++){
			headers.add((char)(64+i)+"");
		}
		
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
  
        for (int i = 0; i < topics.size(); i++)  
        {  
            row = sheet.createRow((int) i + 1);  
            TomTopic topic =  topics.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(MapManager.questionType(topic.getQuestionType()));  
            row.createCell((short) 1).setCellValue(topic.getTopicName());  
            String rightOption="";
            for(TomTopicOption topicOption:topic.getTopicOptions()){
            	if(topicOption.getRight().equals("Y")){
            		rightOption=rightOption+topicOption.getOptionName()+",";
            	}
            }
            rightOption=rightOption.substring(0,rightOption.lastIndexOf(","));
            row.createCell((short) 2).setCellValue(rightOption); 
            for(int j=0;j<topic.getTopicOptions().size();j++){
            	row.createCell((short) j+3).setCellValue(topic.getTopicOptions().get(j).getOptionName()); 
            }
        }  
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try  
        { 
        	
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
	
	/*
	 * 
	 * 解析excel返回json数据
	 * TYX
	 */
	@Transactional
	public String excle1(String filePath) throws Exception {
		String path=filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		String xls1 = null;
		if("xls".equals(path)){
			xls1=readXls1(filePath);
			
		}else if("xlsx".equals(path)){
			 xls1 = readXlsx1(filePath);
		}
		return xls1;
	}
	
	@Transactional
	public String readXlsx1(String path) throws Exception {

		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		
		
		String json = null;
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			if(xssfSheet.getLastRowNum()==0 && xssfSheet.getPhysicalNumberOfRows() == 0){
				continue;
			}
			// Read the Row
			List<QuestionDto> topics=new ArrayList<QuestionDto>();
//			TomTopic topic =null;
			
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				
				List<XSSFPictureData> pictures = xssfWorkbook.getAllPictures();//获得所有的图片
				
				int row=0;
				XSSFPictureData picData=null;
				XSSFClientAnchor anchor=null;

				List<OptionDto> tomTopicOptions=new ArrayList<OptionDto>();
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow!=null&&getValue(xssfRow.getCell(0))!= null&&!"".equals(getValue(xssfRow.getCell(0)))) {
					QuestionDto topic=new QuestionDto();
					XSSFCell type = xssfRow.getCell(0);
					XSSFCell name = xssfRow.getCell(1);
					XSSFCell rightCell = xssfRow.getCell(2);
					if(MapManager.TopicTypeParam(getValue(type))==null){
						throw new EleException("questionTypeError");
					}
					topic.setQuestionType(MapManager.TopicTypeParam(getValue(type)));
					topic.setQuestionName(getValue(name));
					int i=0;
					for(int y=0;y<xssfSheet.createDrawingPatriarch().getShapes().size();y++){
						XSSFShape shape = xssfSheet.createDrawingPatriarch().getShapes().get(y);
						anchor = (XSSFClientAnchor) shape.getAnchor();
						if(shape instanceof XSSFPicture){
							XSSFPicture pic = (XSSFPicture) shape;
							row = anchor.getRow1();
							 picData = pic.getPictureData();
						}
						i++;
					if(anchor.getCol1()==1 &&  anchor.getRow1() == rowNum || pictures==null){
						topic.setQuestionFileUrl(savePic(i, picData,path));
					}else{
						continue;
					}
					}
					boolean flag = false;
					if (topic.getQuestionType().equals("4")) {
//						TomTopicOption tomTopicOption = new TomTopicOption();
						OptionDto tomTopicOption=new OptionDto();
						if(rightCell==null||getValue(rightCell)==null||"".equals(getValue(rightCell))){
							tomTopicOption.setOptionName("略");
						}else{
							tomTopicOption.setOptionName(getValue(rightCell));
						}
						tomTopicOption.setRight("Y");
						tomTopicOptions.add(tomTopicOption);
						topic.setQuestionOptions(tomTopicOptions);
						flag=true;
//						tomTopicOptions.clear();
					}else{
						topic.setQuestionAnswer(getValue(rightCell));
						String rights[] = getValue(rightCell).split(",");
						List<String> rigthOpts = new ArrayList<String>();
						for (String s : rights) {
							rigthOpts.add(s);
						}
						int j = 3;
					
						while (true) {
							XSSFCell option = xssfRow.getCell(j);
							if (j > 28 || option == null|| "".equals(getValue(option))) {
								break;
							}
							OptionDto tomTopicOption = new OptionDto();
							tomTopicOption.setOptionName(getValue(option));
							tomTopicOption.setRight("N");
							for(int y=0;y<xssfSheet.createDrawingPatriarch().getShapes().size();y++){
								XSSFShape shape = xssfSheet.createDrawingPatriarch().getShapes().get(y);
								anchor = (XSSFClientAnchor) shape.getAnchor();
								if(shape instanceof XSSFPicture){
									XSSFPicture pic = (XSSFPicture) shape;
									row = anchor.getRow1();
									 picData = pic.getPictureData();
								}
								i++;
								if(anchor.getCol1()==j && anchor.getRow1() == rowNum || pictures==null ){
									tomTopicOption.setOptionFileUrl(savePic(i, picData,path));
								}else{
									continue;
								}
							}

							for (String right : rigthOpts) {
								if (right.equals((char) (62 + j) + "")) {
									tomTopicOption.setRight("Y");
									flag = true;
								}
							}
							tomTopicOptions.add(tomTopicOption);
     						tomTopicOption=null;
							j++;
						}
						topic.setQuestionOptions(tomTopicOptions);
					}
					topics.add(topic);
				}
		
		}
			 json =JSONArray.fromObject(topics).toString();
		}
			return json;
	}

	@Transactional
	public String readXls1(String path) throws Exception {
		InputStream is = new FileInputStream(path);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		
		String json = null;
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
		
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			List<QuestionDto> topics=new ArrayList<QuestionDto>();
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				
				List<HSSFPictureData> pictures = hssfWorkbook.getAllPictures();//获得所有的图片
				int row=0;
				HSSFPictureData picData=null;
				HSSFClientAnchor anchor=null;

				
				List<OptionDto> tomTopicOptions=new ArrayList<OptionDto>();
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow!=null&&getValue(hssfRow.getCell(0)) != null&&!"".equals(getValue(hssfRow.getCell(0)))) {
					HSSFCell type = hssfRow.getCell(0);
					HSSFCell name = hssfRow.getCell(1);
					HSSFCell rightCell = hssfRow.getCell(2);
					QuestionDto topic = new QuestionDto();
					if(MapManager.TopicTypeParam(getValue(type))==null){
						throw new EleException("questionTypeError");
					}
					topic.setQuestionType(MapManager.TopicTypeParam(getValue(type)));
					topic.setQuestionName(getValue(name));
					
					int i=0;
					for(int y=0;y<hssfSheet.createDrawingPatriarch().getChildren().size();y++){
						HSSFShape shape = hssfSheet.createDrawingPatriarch().getChildren().get(y);
						anchor = (HSSFClientAnchor) shape.getAnchor();
						if(shape instanceof HSSFPicture){
							HSSFPicture pic = (HSSFPicture) shape;
							row = anchor.getRow1();
							 picData = pic.getPictureData();
						}
						i++;
					if(anchor.getCol1()==1 &&  anchor.getRow1() == rowNum || pictures==null){
						topic.setQuestionFileUrl(savePic(i, picData,path));
					}else{
						continue;
					}
					}
					boolean flag=false;
					if (topic.getQuestionType().equals("4")) {
						OptionDto tomTopicOption = new OptionDto();
						if(rightCell==null||getValue(rightCell)==null||"".equals(getValue(rightCell))){
							tomTopicOption.setOptionName("略");
						}else{
							tomTopicOption.setOptionName(getValue(rightCell));
						}
						tomTopicOption.setRight("Y");
						tomTopicOptions.add(tomTopicOption);
						topic.setQuestionOptions(tomTopicOptions);
						flag=true;
						
					}else{
						topic.setQuestionAnswer(getValue(rightCell));
						String rights[]=getValue(rightCell).split(",");
						List<String> rigthOpts=new ArrayList<String>();
						for(String s:rights){
							rigthOpts.add(s);
						}
						int j = 3;
						
						while (true) {
							HSSFCell option = hssfRow.getCell(j);
							if (j>28 || option==null||"".equals(getValue(option))){
								break;
							}
							OptionDto tomTopicOption = new OptionDto();
							tomTopicOption.setOptionName(getValue(option));
							tomTopicOption.setRight("N");
							
							for(int y=0;y<hssfSheet.createDrawingPatriarch().getChildren().size();y++){
								HSSFShape shape = hssfSheet.createDrawingPatriarch().getChildren().get(y);
								anchor = (HSSFClientAnchor) shape.getAnchor();
								if(shape instanceof HSSFPicture){
									HSSFPicture pic = (HSSFPicture) shape;
									row = anchor.getRow1();
									 picData = pic.getPictureData();
								}
								i++;
								if(anchor.getCol1()==j && anchor.getRow1() == rowNum || pictures==null ){
									tomTopicOption.setOptionFileUrl(savePic(i, picData,path));
								}else{
									continue;
								}
							}
							
							for (String right : rigthOpts) {
								if(right.equals((char) (62 + j) + "")){
									tomTopicOption.setRight("Y");
									flag=true;
								}
							}
							
							tomTopicOptions.add(tomTopicOption);
							tomTopicOption=null;
							j++;
						}
						topic.setQuestionOptions(tomTopicOptions);
					}
					topics.add(topic);
				}
				
			}
			json =JSONArray.fromObject(topics).toString();
		}
		return json;
	
	}
	/*
	 * 
	 * 保存图片，返回图片路径
	 */
	private static String savePic(int i, PictureData pic,String path) throws Exception {
		String ext = pic.suggestFileExtension();
		byte[] date = pic.getData();
//		String name = path .substring(0,path .lastIndexOf("."));//去掉文件后缀名的路径
//		String picturePath = null;
//		String suffix = "";
		FileRest fileRest = new FileRest();
		String basePath1 = Config.getProperty("file_path");
		String[] fileAndFolder = fileRest.getFilePath("."+ext, "questionBank", 1);
		// 文件保存地址（带按规则生成后的文件名）
		String filePath = fileAndFolder[1];
		String path1 = StringUtils.replace(StringUtils.substringAfter(filePath, basePath1), "\\", "/");
		if(ext.equals("jpeg")){
			FileOutputStream out = new FileOutputStream(fileAndFolder[1]);
			out.write(date);
			out.close();
		}
		if(ext.equals("png")){
			FileOutputStream out = new FileOutputStream(fileAndFolder[1]);
			out.write(date);
			out.close();
		}
		if(ext.equals("jpg")){
			FileOutputStream out = new FileOutputStream(fileAndFolder[1]);
			out.write(date);
			out.close();
		}
		if(ext.equals("bmp")){
			FileOutputStream out = new FileOutputStream(fileAndFolder[1]);
			out.write(date);
			out.close();
		}
		if(ext.equals("gif")){
			FileOutputStream out = new FileOutputStream(fileAndFolder[1]);
			out.write(date);
			out.close();
		}
	
		return path1;
//		return picturePath;
	}
}