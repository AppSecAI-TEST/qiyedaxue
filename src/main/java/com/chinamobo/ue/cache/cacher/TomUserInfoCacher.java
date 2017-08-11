/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.utils.JsonMapper;
/**
 * 版本: [1.0]
 * 功能说明: 用户信息缓存模块;
 * 作者: WChao 创建时间: 2017年6月14日 下午6:02:28
 */
public class TomUserInfoCacher implements Cacher {
	
	@Autowired
	private TomUserInfoMapper userInfoMapper;
	
	@Override
	public void init() throws Exception {

	}

	@Override
	public void doCache(AbstractCacheManager cacheManager) throws Exception {
		JsonMapper mapper = new JsonMapper();
		List<TomUserInfo> userInfos = userInfoMapper.selectAll();
		int size=userInfos.size();
		for (int i=0;i<size;i++) {
			TomUserInfo userInfo=userInfos.get(i);
			cacheManager.addCacheData(new RedisCacheData("userInfo", userInfo.getCode(), mapper.toJson(userInfo)));
		}
	}

}
