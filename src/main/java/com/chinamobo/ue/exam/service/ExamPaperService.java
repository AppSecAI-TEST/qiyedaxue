package com.chinamobo.ue.exam.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.dao.TomExamTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.dto.ExamPaperDto;
import com.chinamobo.ue.exam.dto.RandomTopicDto;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomExamTopic;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.RedisUtils;
import com.chinamobo.ue.utils.StringUtil;

@Service
public class ExamPaperService {

	@Autowired
	private TomExamPaperMapper tomExamPaperMapper;
	@Autowired
	private TomTopicMapper tomTopicMapper;
	@Autowired
	private TomExamQuestionMapper examQuestionMapper;
	@Autowired
	private TomExamTopicMapper examTopicMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomTopicMapper topicMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomTopicOptionMapper topicOptionMapper;
	
	JsonMapper mapper=new JsonMapper();
	/**
	 * 
	 * 功能描述：[子表信息返回]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月9日 下午1:55:53
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ExamPaperDto findExamQuestion(int id) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExamPaper examPaper=tomExamPaperMapper.selectByPrimaryKey(id);
		ExamPaperDto dto=new ExamPaperDto();
		PropertyUtils.copyProperties(dto,examPaper); 
		List<TomExamQuestion> list=examQuestionMapper.selectByExamPaperId(id);
		List<Integer> questionBankId=new ArrayList<>();
		List<String> sort=Arrays.asList(examPaper.getSorts().split(","));
		List<String> questionType=new ArrayList<>();
		List<Integer> count=new ArrayList<>();
		List<Integer> score=new ArrayList<>();
		List<String> questionBankName=new ArrayList<>();
		for(TomExamQuestion examQuestion:list){//添加子表信息
			questionBankId.add(examQuestion.getQuestionBankId());
			questionType.add(examQuestion.getQuestionType());
//			sort.add(examQuestion.getSort());
			count.add(examQuestion.getCount());
			score.add(examQuestion.getScore());
			questionBankName.add(examQuestion.getQuestionBankName());
		}
		dto.setCount(count);
		dto.setQuestionBankId(questionBankId);
		dto.setQuestionType(questionType);
		dto.setScore(score);
		dto.setSort(sort);
		dto.setQuestionBankName(questionBankName);
		return dto;
	
	}
	
	public TomExamPaper findById(int id){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomExamPaperMapper.selectByPrimaryKey(id);
	}
	/**
	 * 
	 * 功能描述：[试卷添加]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月9日 下午1:56:11
	 * @param dto
	 * @throws Exception
	 */
	@Transactional
	public String addExamPaper(ExamPaperDto dto) throws Exception{
		for(int i =0;i<dto.getQuestionBankId().size();i++){
			for(int j=0;j<dto.getQuestionBankId().size();j++){
				if(j!=i){
//					System.out.println(dto.getQuestionBankId().get(i).equals(dto.getQuestionBankId().get(j)));
					if(dto.getQuestionBankId().get(i).equals(dto.getQuestionBankId().get(j))&&dto.getQuestionType().get(i).equals(dto.getQuestionType().get(j))){
						return "N";
					}
				}
			}
		}
		
		TomExamPaper examPaper=new TomExamPaper(); 
	    PropertyUtils.copyProperties(examPaper,dto); 
		ShiroUser user=ShiroUtils.getCurrentUser();
		examPaper.setCreateTime(new Date());
		examPaper.setUpdateTime(new Date());
		examPaper.setCreator(user.getName());
		examPaper.setCreatorId(user.getCode());
		examPaper.setLastOperator(user.getName());
		examPaper.setExamPaperNumber(numberRecordService.getNumber(MapManager.numberType("SJ")));
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		examPaper.setImmediatelyShow("Y");
		String sorts=StringUtil.listToString(dto.getSort());
		examPaper.setSorts(sorts);
		tomExamPaperMapper.insert(examPaper);
		RedisUtils.hset("tomExamPaper", examPaper.getExamPaperId().toString(), mapper.toJson(examPaper));//新添试卷到缓存
		int paperId=examPaper.getExamPaperId();
		String examPaperType=examPaper.getExamPaperType();
		for(int i =0;i<dto.getQuestionBankId().size();i++){
			
			TomExamQuestion examQuestion=new TomExamQuestion();
			examQuestion.setCreateTime(new Date());
			examQuestion.setExamPaperId(paperId);
			examQuestion.setCount(dto.getCount().get(i));
			examQuestion.setQuestionBankId(dto.getQuestionBankId().get(i));
			examQuestion.setQuestionType(dto.getQuestionType().get(i));
			examQuestion.setScore(dto.getScore().get(i));
			examQuestion.setSort(dto.getSort().get(Integer.parseInt(examQuestion.getQuestionType())-1));
			examQuestion.setQuestionBankName(dto.getQuestionBankName().get(i));
			addExamQuestion(examQuestion, paperId, examPaperType);
		}
		if (examPaper.getExamPaperType().equals("1") || examPaper.getExamPaperType().equals("3")
				|| examPaper.getExamPaperType().equals("5")) {
			List<TomTopic> topicList = topicMapper.selectByExamPaperId(examPaper.getExamPaperId());
			for (TomTopic topic : topicList) {
				List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicIdNoStatus(topic.getTopicId());
				topic.setTopicOptions(topicOptions);
			}
			RedisUtils.hset("examPaperTopic", examPaper.getExamPaperId().toString(), mapper.toJson(topicList));// 缓存固定试卷所有题目
		} else if (examPaper.getExamPaperType().equals("2") || examPaper.getExamPaperType().equals("4")
				|| examPaper.getExamPaperType().equals("6")) {
			List<TomExamQuestion> examQuestions;
			List<RandomTopicDto> randomTopicList=new ArrayList<RandomTopicDto>();
			try {
				examQuestions = sort(examQuestionMapper.selectByExamPaperId(examPaper.getExamPaperId()));
				for (TomExamQuestion examQuestion : examQuestions) {
					RandomTopicDto randomTopicDto=new RandomTopicDto();
					BeanUtils.copyProperties(randomTopicDto,examQuestion);
					List<TomTopic> topicList = topicMapper.selectByqbIdAndType(examQuestion.getQuestionBankId(),
							examQuestion.getQuestionType());
					for (TomTopic topic : topicList) {
						List<TomTopicOption> topicOptions = topicOptionMapper
								.selectByTopicIdNoStatus(topic.getTopicId());
						topic.setTopicOptions(topicOptions);
					}
					randomTopicDto.setTopicList(topicList);
					randomTopicList.add(randomTopicDto);
				}
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				RedisUtils.hset("randomExamPaper", examPaper.getExamPaperId().toString(), mapper.toJson(randomTopicList));// 缓存随机试卷所有考题
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RedisUtils.hset("tomExamPaper", examPaper.getExamPaperId().toString(), mapper.toJson(examPaper));
		return "Y";
	}
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
	 * 功能描述：[试卷修改]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月9日 下午1:56:22
	 * @param dto
	 * @throws Exception
	 */
	@Transactional
	public String updateExamPaper(ExamPaperDto dto) throws Exception{
		for(int i =0;i<dto.getQuestionBankId().size();i++){
			for(int j=0;j<dto.getQuestionBankId().size();j++){
				if(j!=i){
//					System.out.println(dto.getQuestionBankId().get(i).equals(dto.getQuestionBankId().get(j)));
					if(dto.getQuestionBankId().get(i).equals(dto.getQuestionBankId().get(j))&&dto.getQuestionType().get(i).equals(dto.getQuestionType().get(j))){
						return "N";
					}
				}
			}
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomExamPaper examPaper=new TomExamPaper(); 
	    PropertyUtils.copyProperties(examPaper,dto); 
		ShiroUser user=ShiroUtils.getCurrentUser();
		examPaper.setLastOperator(user.getName());
		examPaper.setUpdateTime(new Date());
		String sorts=StringUtil.listToString(dto.getSort());
		examPaper.setSorts(sorts);
		tomExamPaperMapper.updateByPrimaryKeySelective(examPaper);
		int paperId=examPaper.getExamPaperId();
		String examPaperType=examPaper.getExamPaperType();
		/*start 编辑重新生成试题*/
		examQuestionMapper.deleteAll(paperId);
		examTopicMapper.deleteAll(paperId);
		for(int i =0;i<dto.getQuestionBankId().size();i++){
			
			TomExamQuestion examQuestion=new TomExamQuestion();
			examQuestion.setCreateTime(new Date());
			examQuestion.setExamPaperId(paperId);
			examQuestion.setCount(dto.getCount().get(i));
			examQuestion.setQuestionBankId(dto.getQuestionBankId().get(i));
			examQuestion.setQuestionType(dto.getQuestionType().get(i));
			examQuestion.setScore(dto.getScore().get(i));
			examQuestion.setSort(dto.getSort().get(Integer.parseInt(examQuestion.getQuestionType())-1));
			examQuestion.setQuestionBankName(dto.getQuestionBankName().get(i));
			addExamQuestion(examQuestion, paperId, examPaperType);
		}
		/*end 编辑重新生成试题*/
		/*start 添加缓存*/
		if (examPaper.getExamPaperType().equals("1") || examPaper.getExamPaperType().equals("3")
				|| examPaper.getExamPaperType().equals("5")) {
			List<TomTopic> topicList = topicMapper.selectByExamPaperId(examPaper.getExamPaperId());
			for (TomTopic topic : topicList) {
				List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicIdNoStatus(topic.getTopicId());
				topic.setTopicOptions(topicOptions);
			}
			RedisUtils.hset("examPaperTopic", examPaper.getExamPaperId().toString(), mapper.toJson(topicList));// 缓存固定试卷所有题目
		} else if (examPaper.getExamPaperType().equals("2") || examPaper.getExamPaperType().equals("4")
				|| examPaper.getExamPaperType().equals("6")) {
			List<TomExamQuestion> examQuestions;
			List<RandomTopicDto> randomTopicList=new ArrayList<RandomTopicDto>();
			try {
				examQuestions = sort(examQuestionMapper.selectByExamPaperId(examPaper.getExamPaperId()));
				for (TomExamQuestion examQuestion : examQuestions) {
					RandomTopicDto randomTopicDto=new RandomTopicDto();
					BeanUtils.copyProperties(randomTopicDto,examQuestion);
					List<TomTopic> topicList = topicMapper.selectByqbIdAndType(examQuestion.getQuestionBankId(),
							examQuestion.getQuestionType());
					for (TomTopic topic : topicList) {
						List<TomTopicOption> topicOptions = topicOptionMapper
								.selectByTopicIdNoStatus(topic.getTopicId());
						topic.setTopicOptions(topicOptions);
					}
					randomTopicDto.setTopicList(topicList);
					randomTopicList.add(randomTopicDto);
				}
				RedisUtils.hset("randomExamPaper", examPaper.getExamPaperId().toString(), mapper.toJson(randomTopicList));// 缓存随机试卷所有考题
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RedisUtils.hset("tomExamPaper", examPaper.getExamPaperId().toString(), mapper.toJson(examPaper));
		/*end 添加缓存*/
		return "Y";
	}
	/**
	 * 
	 * 功能描述：[添加试卷题库关联]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月9日 下午1:57:06
	 * @param examQuestion
	 * @param examPaperId
	 * @param examPaperType
	 * @throws Exception
	 */
	@Transactional
	public void addExamQuestion(TomExamQuestion examQuestion,int examPaperId,String examPaperType)throws Exception{
		examQuestionMapper.insert(examQuestion);
		RedisUtils.hset("examQuestion", 
				examQuestion.getExamPaperId() + ":"+examQuestion.getQuestionBankId()+":" + examQuestion.getQuestionType(), 
				mapper.toJson(examQuestion));
		if("1".equals(examPaperType) || "3".equals(examPaperType) || "5".equals(examPaperType)){//固定试卷
			int questionBankId=examQuestion.getQuestionBankId();
			String type=examQuestion.getQuestionType();
			int num=examQuestion.getCount();
			List<TomExamTopic> examTopicList=random(questionBankId, type, num, examPaperId,examQuestion.getSort());
			examTopicMapper.insertList(examTopicList);
		}
	}
	/**
	 * 
	 * 功能描述：[随机抽取试题]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月9日 下午1:57:23
	 * @param questionBankId
	 * @param type
	 * @param num
	 * @param examId
	 * @return
	 */
	public List<TomExamTopic> random(int questionBankId,String type,int num,int examId,String sort){
		List<TomExamTopic> examTopicList=new ArrayList<TomExamTopic>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomTopic> topicList=tomTopicMapper.selectByqbIdAndType(questionBankId, type);
		for(int i=0;i<num;i++){
			int random=(int)Math.random()*topicList.size();
			TomExamTopic examTopic=new TomExamTopic();
			examTopic.setCreateTime(new Date());
			examTopic.setExamPaperId(examId);
			examTopic.setSort(sort);
			examTopic.setTopicId(topicList.get(random).getTopicId());
			examTopicList.add(examTopic);
			topicList.remove(random);
		}
		return examTopicList;
	}

	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月12日 下午1:43:58
	 * @param pageNum
	 * @param pageSize
	 * @param example
	 * @return
	 */
	public PageData<TomExamPaper> selectPage(int pageNum, int pageSize,TomExamPaper example) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomExamPaper> page=new PageData<TomExamPaper>();				
		int count=tomExamPaperMapper.countByExample(example);
		
		if(pageSize==-1){
			pageSize=count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("example", example);
		List<TomExamPaper> list = tomExamPaperMapper.selectListByPage(map);
	
		for(TomExamPaper examPaper:list){
			examPaper.setExamPaperType(MapManager.examType(examPaper.getExamPaperType()));
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;
	}

	/**
	 * 
	 * 功能描述：[删除试卷]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月12日 下午3:46:47
	 * @param examPaperId
	 */
	@Transactional
	public String deleteExamPaper(int examPaperId) {
		List<TomExam> examList=examMapper.selectByExamPaperId(examPaperId);
		if(examList.size()>0){
			return "{\"result\":\"protected\"}";
		}
		tomExamPaperMapper.deleteByPrimaryKey(examPaperId);
		examQuestionMapper.deleteAll(examPaperId);
		return "{\"result\":\"success\"}";
	}
	/**
	 * 
	 * 功能描述：[查看固定试卷题目]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年5月9日 下午2:02:00
	 * @param examPaperId
	 * @return
	 */
	public String examine(Integer examPaperId){
		JsonMapper jsonMapper = new JsonMapper();
		TomExamPaper examPaper=tomExamPaperMapper.selectByPrimaryKey(examPaperId);
		if(examPaper.getExamPaperType().equals("1")||examPaper.getExamPaperType().equals("3")||examPaper.getExamPaperType().equals("5")){
			List<TomTopic> topics=topicMapper.selectByExamPaperId(examPaper.getExamPaperId());
			for(TomTopic topic:topics){
				List<TomTopicOption> topicOptions=topicOptionMapper.selectByTopicId(topic.getTopicId());
				if(topicOptions!=null){
					topic.setTopicOptions(topicOptions);
				}
			}
			return jsonMapper.toJson(topics);
		}
		return "{\"result\":\"error\"}";
	}
}
