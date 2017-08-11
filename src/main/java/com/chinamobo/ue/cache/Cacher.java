/**
 * 
 */
package com.chinamobo.ue.cache;

/**
 * 版本: [1.0]
 * 功能说明: 抽象缓存者;
 *
 * 作者: WChao
 * 创建时间: 2017年6月14日 下午2:33:44
 */
public interface Cacher {

	/**
	 * 
		 * 功能描述：[初始化Cacher方法]
		 * 创建者：WChao 创建时间: 2017年6月14日 下午3:01:23
		 *
	 */
	public void init()throws Exception;
	/**
	 * 
		 * 功能描述：[执行缓存方法]
		 * 创建者：WChao 创建时间: 2017年6月14日 下午3:01:41
		 *
	 */
	public void doCache(AbstractCacheManager cacheManager)throws Exception;
}
