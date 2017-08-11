/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.utils.JsonMapper;
/**
 * 版本: [1.0]
 * 功能说明: 缓存补考信息
 * 作者: WChao 创建时间: 2017年6月15日 上午10:54:43
 */
public class TomRetakingExamCacher implements Cacher{

	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	
	@Override
	public void init() throws Exception {
		
	}

	@Override
	public void doCache(AbstractCacheManager cacheManager) throws Exception {
		JsonMapper mapper = new JsonMapper();
		//缓存补考
		List<TomRetakingExam> retakingExamList = retakingExamMapper.selectListByExample(null);
		int retakingSize=retakingExamList.size();
		for (int i =0;i<retakingSize;i++) {
			TomRetakingExam retakingExam = retakingExamList.get(i);
			cacheManager.addCacheData(new RedisCacheData("retakingExamSort", retakingExam.getExamId() + ":" + retakingExam.getSort(),mapper.toJson(retakingExam)));
			
			List<TomRetakingExam> retakingExams=retakingExamMapper.selectListByExample(retakingExam);
			cacheManager.addCacheData(new RedisCacheData("retakingExamsForOneExam", retakingExam.getExamId().toString(), mapper.toJson(retakingExams)));
		}
	}

}
