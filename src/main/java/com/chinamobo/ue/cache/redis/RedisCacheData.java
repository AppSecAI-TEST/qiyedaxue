/**
 * 
 */
package com.chinamobo.ue.cache.redis;

import com.chinamobo.ue.cache.CacheData;

/**
 * 版本: [1.0]
 * 功能说明: Redis缓存数据格式;
 * 作者: WChao 创建时间: 2017年6月14日 下午3:48:05
 */
public class RedisCacheData extends CacheData{
	protected String key ;//域名;
	protected String field;//字段;
	protected String value;//值;
	
	public RedisCacheData(){};
	public RedisCacheData(String key , String field , String value){
		this.key = key;
		this.field = field;
		this.value = value;
	}
	public RedisCacheData(String key ,  String value){
		this.field = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public String getField() {
		return field;
	}
	public String getValue() {
		return value;
	}
}
