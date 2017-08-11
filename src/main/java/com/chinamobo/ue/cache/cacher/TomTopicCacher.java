/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.RedisUtils;

/**
 * 版本: [1.0]
 * 功能说明:缓存题目及选项
 * 作者: WChao 创建时间: 2017年6月15日 上午11:15:23
 */
public class TomTopicCacher implements Cacher {
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
		//缓存题目及选项
		List<TomTopic> topicList=topicMapper.selectAll();
		int topicSize=topicList.size();
		for(int i =0;i<topicSize;i++){
			TomTopic tomTopic=topicList.get(i);
			List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicIdNoStatus(tomTopic.getTopicId());
			List<TomTopicOption> rightList = topicOptionMapper.selectRightOption(tomTopic.getTopicId());;
			tomTopic.setTopicOptions(topicOptions);
			cacheManager.addCacheData(new RedisCacheData("tomTopic", tomTopic.getTopicId().toString()+":"+tomTopic.getQuestionBankId().toString()+":"+tomTopic.getQuestionType(), mapper.toJson(tomTopic)));
			topicOptions =  topicOptionMapper.selectByTopicId(tomTopic.getTopicId());//查询带正确状态的选项
			tomTopic.setTopicOptions(topicOptions);
			cacheManager.addCacheData(new RedisCacheData("tomTopicWithId", tomTopic.getTopicId().toString(), mapper.toJson(tomTopic)));
			
			tomTopic.setTopicOptions(rightList);
			cacheManager.addCacheData(new RedisCacheData("tomTopicRightOption", tomTopic.getTopicId().toString(), mapper.toJson(tomTopic)));//缓存正确答案
		}
		RedisUtils.setString("tomTopicAll", mapper.toJson(topicList));
		cacheManager.addCacheData(new RedisCacheData("tomTopicAll", mapper.toJson(topicList)));
	}

}
