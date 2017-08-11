package com.chinamobo.ue.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.exam.dao.TomExamAnswerDetailsMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamAnswerDetails;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.ums.DBContextHolder;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 答题详情
 *
 * 作者: JCX
 * 创建时间: 2016年4月9日 上午10:35:22
 */
@Service
public class AnswerDetailsService {
	
	@Autowired
	private TomExamAnswerDetailsMapper answerDetailsMapper;
	@Autowired
	private TomTopicOptionMapper topicOptionMapper;
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomExamQuestionMapper examQuestionMapper;
	@Autowired
	private TomTopicMapper topicMapper;

	/**
	 * 
	 * 功能描述：[显示答题详情]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月9日 上午10:35:09
	 * @param examId
	 * @param code
	 * @return
	 */
	public List<TomExamAnswerDetails> selectAnswerDetails(int examId, String code,String gradeState) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExamAnswerDetails examAnswerDetailsExample=new TomExamAnswerDetails();
		examAnswerDetailsExample.setCode(code);
		examAnswerDetailsExample.setExamId(examId);
		if(gradeState.equals("null")){
			examAnswerDetailsExample.setStatus("N");
		}
		List<TomExamAnswerDetails> list= answerDetailsMapper.selectByExample(examAnswerDetailsExample);
	
		TomExam exam;
		for(TomExamAnswerDetails examAnswerDetails:list){
			TomTopic topic=topicMapper.selectByPrimaryKey(examAnswerDetails.getTopicId());
			exam=examMapper.selectByPrimaryKey(examId);
			examAnswerDetails.setTopicOptions(topicOptionMapper.selectByTopicId(examAnswerDetails.getTopicId()));
			examAnswerDetails.setExamName(exam.getExamName());
			if(topic.getQuestionType().equals("1")||topic.getQuestionType().equals("2")||topic.getQuestionType().equals("5")){
				//设置状态：非主观题
				examAnswerDetails.setTopicType("O");
			}else{
				//设置状态，主观题
				examAnswerDetails.setTopicType("S");
			}
			
			
			TomExamQuestion example=new TomExamQuestion();
			example.setExamPaperId(exam.getExamPaperId());
			example.setQuestionType(topic.getQuestionType());
			examAnswerDetails.setRightScore(examQuestionMapper.selectByExample(example).getScore());
			
			if(examAnswerDetails.getEmpAnswer()!=null&&!examAnswerDetails.getEmpAnswer().equals("")){
				String[] array=examAnswerDetails.getEmpAnswer().split(",");
				for(int i=0;i<array.length;i++){
					if(i==0){
						examAnswerDetails.setEmpAnswerName(topicOptionMapper.selectByPrimaryKey(Integer.parseInt(array[i])).getOptionName().split(" ")[0]);
					}else{
						String s=examAnswerDetails.getEmpAnswerName()+","+topicOptionMapper.selectByPrimaryKey(Integer.parseInt(array[i])).getOptionName().split(" ")[0];
						examAnswerDetails.setEmpAnswerName(s);
					}
				}
			}
		}
		
		return list;
	}

}
