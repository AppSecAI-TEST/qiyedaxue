package com.chinamobo.ue.cache;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 缓存管理器接口;
 * 作者: WChao 创建时间: 2017年6月14日 下午2:41:15
 */
public interface CacheManager {
	/**
		 * 功能描述：[注册缓存者]
		 * 创建者：WChao 创建时间: 2017年6月14日 下午2:41:42
		 * @param cacher
		 *
	 */
	public void registerCacher(Cacher cacher);
	/**
	 * 
		 * 功能描述：[删除缓存者]
		 * 创建者：WChao 创建时间: 2017年6月14日 下午2:43:07
		 * @param cacher
		 *
	 */
	public void removeCacher(Cacher cacher);
	
	
}
