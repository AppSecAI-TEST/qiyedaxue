/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.cache.redis.RedisGetData;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.utils.JsonMapper;
/**
 * 版本: [1.0]
 * 功能说明: 缓存试卷题库关联表
 * 作者: WChao 创建时间: 2017年6月15日 上午11:01:19
 */
public class TomExamQuestionCacher implements Cacher {

	@Autowired
	private TomExamQuestionMapper examQuestionMapper;
	
	@Override
	public void init() throws Exception {

	}

	@Override
	public void doCache(AbstractCacheManager cacheManager) throws Exception {
		JsonMapper mapper = new JsonMapper();
		//缓存试卷题库关联表
		List<TomExamQuestion> examQuestionList = examQuestionMapper.selectListByExample(null);
		int examQuestionSize=examQuestionList.size();
		for (int i =0;i<examQuestionSize;i++) {
			TomExamQuestion examQuestion = examQuestionList.get(i);
			cacheManager.addCacheData(new RedisCacheData("examQuestion",examQuestion.getExamPaperId() + ":"+examQuestion.getQuestionBankId()+":" + examQuestion.getQuestionType(),mapper.toJson(examQuestion)));
		}
	}

}
