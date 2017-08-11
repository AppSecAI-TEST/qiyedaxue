/**
 * 
 */
package com.chinamobo.ue.cache.redis;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.CacheData;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.utils.RedisUtils;

/**
 * 版本: [1.0]
 * 功能说明: Redis缓存管理器;
 *
 * 作者: WChao
 * 创建时间: 2017年6月14日 下午2:25:10
 */
@SuppressWarnings({"static-access"})
public class RedisCacheManager extends AbstractCacheManager {

	private RedisUtils client = null;//Redis缓存客户端;
	
	@Override
	public void init()throws Exception{
		this.client = new RedisUtils();
		this.client.getJedis();
		for(Cacher cacher : cachers){
			cacher.init();//初始化相关资源;
			cacher.doCache(this);//初始化模块Cache缓存信息;
		}
	}
	
	@Override
	public void addCacheData(CacheData data)throws Exception{
		if(client == null){
			throw new RuntimeException("Redis缓存未启动或启动错误,请检查配置!");
		}
		if(data == null){
			throw new RuntimeException("Redis缓存数据不能为空!");
		}
		if(!(data instanceof RedisCacheData)){
			throw new RuntimeException("缓存数据"+data.getClass()+"不为RedisCacheData类型!");
		}
		RedisCacheData redisCacheData = (RedisCacheData)data;
		if(redisCacheData.getKey() != null && redisCacheData.getField() != null){
			client.hset(redisCacheData.getKey(), redisCacheData.getField(), redisCacheData.getValue());
		}else if(redisCacheData.getKey() == null && redisCacheData.getField() != null){
			client.setString(redisCacheData.getField(), redisCacheData.getValue());
		}
	}

	@Override
	public  Object getCacheData(CacheData get) throws Exception {
		if(client == null){
			throw new RuntimeException("Redis缓存未启动或启动错误,请检查配置!");
		}
		if(get == null){
			throw new RuntimeException("Redi获取参数不能为空!");
		}
		if(!(get instanceof RedisGetData)){
			throw new RuntimeException("数据参数:"+get.getClass()+"不为RedisGetData类型!");
		}
		RedisGetData redisGetData = (RedisGetData)get;
		if(redisGetData.getKey() != null && redisGetData.getField() != null){
			return client.hget(redisGetData.getKey(), redisGetData.getField());
		}else if(redisGetData.getKey() == null && redisGetData.getField() != null){
			return client.getString(redisGetData.getField());
		}
		return null;
	}
}
