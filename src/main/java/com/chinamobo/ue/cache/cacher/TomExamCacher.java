/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.cacher.interfaces.TomExamIntf;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.RedisUtils;

/**
 * 版本: [1.0]
 * 功能说明: 缓存考试信息;
 * 作者: WChao 创建时间: 2017年6月15日 上午10:47:14
 */
public class TomExamCacher implements TomExamIntf,Cacher {

	@Autowired
	private TomExamMapper examMapper;
	private AbstractCacheManager cacheManager;
	private JsonMapper mapper = null;
	
	@Override
	public void init() throws Exception {
		mapper = new JsonMapper();
	}

	@Override
	public void doCache(AbstractCacheManager cacheManager) throws Exception {
		this.cacheManager = cacheManager;
		RedisUtils.setString("concurrency", "N");
		//缓存考试
		List<TomExam> examList = examMapper.selectAll();
		int examSize=examList.size();
		for (int i=0;i<examSize;i++) {
			TomExam tomExam=examList.get(i);
			cacheManager.addCacheData(new RedisCacheData("exam", tomExam.getExamId().toString(), mapper.toJson(tomExam)));
		}

	}

	@Override
	public List<TomExam> initUserExams(Map<Object, Object> paramMap)throws Exception{
		List<TomExam> list=examMapper.selectByUser(paramMap);
		if(this.cacheManager != null && paramMap.get("userId") != null)
		cacheManager.addCacheData(new RedisCacheData("exam", paramMap.get("userId").toString(), mapper.toJson(list)));
		return list;
	}

}
