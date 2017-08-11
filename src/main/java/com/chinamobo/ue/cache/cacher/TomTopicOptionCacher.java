/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.utils.JsonMapper;
/**
 * 版本: [1.0]
 * 功能说明: 缓存所有选项信息
 * 作者: WChao 创建时间: 2017年6月15日 上午11:20:04
 */
public class TomTopicOptionCacher implements Cacher{

	@Autowired
	private TomTopicOptionMapper topicOptionMapper;
	
	@Override
	public void init() throws Exception {
		
	}

	@Override
	public void doCache(AbstractCacheManager cacheManager) throws Exception {
		JsonMapper mapper = new JsonMapper();
		List<TomTopicOption> optionList=topicOptionMapper.selectAll();
		int optionSize=optionList.size();
		for(int i =0;i<optionSize;i++){
			TomTopicOption option=optionList.get(i);
			cacheManager.addCacheData(new RedisCacheData("tomTopicOption", option.getId().toString(), mapper.toJson(option)));
		}
	}

}
