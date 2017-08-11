/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.dto.RandomTopicDto;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.utils.JsonMapper;
/**
 * 版本: [1.0]
 * 功能说明: 缓存考试试卷
 * 作者: WChao 创建时间: 2017年6月15日 上午11:07:37
 */
public class TomExamPaperCacher implements Cacher{

	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private TomExamQuestionMapper examQuestionMapper;
	@Autowired
	private TomTopicMapper topicMapper;
	@Autowired
	private TomTopicOptionMapper topicOptionMapper;
	
	@Override
	public void init() throws Exception {
		
	}

	@Override
	public void doCache(AbstractCacheManager cacheManager) throws Exception {
		JsonMapper mapper = new JsonMapper();
		//缓存考试试卷
		List<TomExamPaper> examPaperList = examPaperMapper.selectAll();
		int examPaperSize=examPaperList.size();
		for (int i=0;i<examPaperSize;i++) {
			TomExamPaper examPaper = examPaperList.get(i);
			cacheManager.addCacheData(new RedisCacheData("tomExamPaper", examPaper.getExamPaperId().toString(), mapper.toJson(examPaper)));//缓存所有试卷
			if (examPaper.getExamPaperType().equals("1") || examPaper.getExamPaperType().equals("3")
					|| examPaper.getExamPaperType().equals("5")) {//固定考试
				List<TomTopic> topicList = topicMapper.selectByExamPaperId(examPaper.getExamPaperId());
				int topicSize=topicList.size();
				for (int j=0;j<topicSize;j++) {
					TomTopic topic = topicList.get(j);
					List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicIdNoStatus(topic.getTopicId());
					topic.setTopicOptions(topicOptions);
				}
				cacheManager.addCacheData(new RedisCacheData("examPaperTopic", examPaper.getExamPaperId().toString(), mapper.toJson(topicList)));// 缓存固定试卷所有题目
			} else if (examPaper.getExamPaperType().equals("2") || examPaper.getExamPaperType().equals("4")
					|| examPaper.getExamPaperType().equals("6")) {//随机考试
				List<TomExamQuestion> examQuestions;
				List<RandomTopicDto> randomTopicList=new ArrayList<RandomTopicDto>();
				try {
					examQuestions = sort(examQuestionMapper.selectByExamPaperId(examPaper.getExamPaperId()));
					int questionSize=examQuestions.size();
					for (int m=0;m<questionSize;m++) {
						TomExamQuestion examQuestion = examQuestions.get(m);
						RandomTopicDto dto=new RandomTopicDto();
						BeanUtils.copyProperties(dto,examQuestion);
						List<TomTopic> topicList = topicMapper.selectByqbIdAndType(examQuestion.getQuestionBankId(),examQuestion.getQuestionType());
						int topicSize=topicList.size();
						for (int j=0;j<topicSize;j++) {
							TomTopic topic = topicList.get(j);
							List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicIdNoStatus(topic.getTopicId());
							topic.setTopicOptions(topicOptions);
						}
						dto.setTopicList(topicList);
						randomTopicList.add(dto);
					}
					cacheManager.addCacheData(new RedisCacheData("randomExamPaper", examPaper.getExamPaperId().toString(), mapper.toJson(randomTopicList)));// 缓存随机试卷所有考题
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public List<TomExamQuestion> sort(List<TomExamQuestion> list) throws Exception {

		TomExamQuestion temp; // 记录临时中间值
		int size = list.size(); // 大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (Integer.parseInt(list.get(i).getSort()) > Integer.parseInt(list.get(j).getSort())) {
					temp = list.get(i);
					list.set(i, list.get(j));
					list.set(j, temp);
				}
			}
		}
		return list;
	}
}
