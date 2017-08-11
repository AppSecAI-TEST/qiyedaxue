package com.chinamobo.ue.activity.common;

import java.util.List;

/**
 * 
 * 版本: [1.0]
 * 功能说明: DAO支持类实现
 *
 * 作者: WangLg
 * 创建时间: 2016年3月3日 下午3:43:08
 * @param <T>
 */
public interface BaseDao<T> {

	/**
	 * 功能描述：[获取数据]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月3日 下午3:43:08
	 * @param entity
	 * @return
	 */
	public T getG(T entity);
	public T getG(String id);
	
	
	/**
	 * 
	 * 功能描述：[ 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月3日 下午3:43:08
	 * @param entity
	 * @return
	 */
	public List<T> findListF(T entity);
	
	/**
	 * 
	 * 功能描述：[查询所有数据列表]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月3日 下午3:43:08
	 * @return
	 */
	public List<T> findAllListF();
	
	/**
	 * 
	 * 功能描述：[插入数据]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月3日 下午3:43:08
	 * @param entity
	 * @return
	 */
	public int insertI(T entity);
	
	/**
	 * 
	 * 功能描述：[更新数据]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月3日 下午3:43:08
	 * @param entity
	 * @return
	 */
	public int updateU(T entity);
	
	/**
	 * 
	 * 功能描述：[删除数据（一般为逻辑删除，更新del_flag字段为1）]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月3日 下午3:43:08
	 * @param entity
	 * @return
	 */
	public int deleteD(T entity);
	public int deleteD(String id);
	
	/**
	 * 功能描述：[批量删除]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月11日 下午4:19:34
	 * @param idList
	 * @return
	 */
	public int deleteBatchD(List<String> idList);
}