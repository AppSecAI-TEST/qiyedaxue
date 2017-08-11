/**
 * 
 */
package com.chinamobo.ue.cache.redis;

/**
 * 版本: [1.0]
 * 功能说明: 获取Redis数据模型;
 * 作者: WChao 创建时间: 2017年6月15日 下午4:11:25
 */
public class RedisGetData extends RedisCacheData {

	public RedisGetData(String key, String field) {
		this.key = key;
		this.field = field;
	}
	
	public RedisGetData(String key) {
		this.field = key;
	}
}
