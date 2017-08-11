/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.StringUtil;

/**
 * 版本: [1.0]
 * 功能说明: 用户登录信息缓存模块;
 * 作者: WChao 创建时间: 2017年6月14日 下午3:11:40
 */
public class TomUserLogCacher implements Cacher {
	
	@Autowired
	private TomUserLogMapper tomUserLogMapper;
	
	@Override
	public void init() {
		
	}

	@Override
	public void doCache(AbstractCacheManager cacheManager)throws Exception{
		JsonMapper mapper = new JsonMapper();
		//缓存用户登录信息
		List<TomUserLog> logList = tomUserLogMapper.selectAll();
		int logSize=logList.size();
		for(int i =0;i< logSize;i++){
			TomUserLog log=logList.get(i);
			cacheManager.addCacheData(new RedisCacheData("tomUserLogCode", log.getCode(), mapper.toJson(log)));// code为key缓存log
			if (StringUtil.isNotBlank(log.getToken())) {
				cacheManager.addCacheData(new RedisCacheData("tomUserLogToken", log.getToken(), mapper.toJson(log)));// token为key缓存log
			}
		}
	}

}
