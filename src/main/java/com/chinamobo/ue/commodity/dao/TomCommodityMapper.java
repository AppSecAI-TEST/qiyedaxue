package com.chinamobo.ue.commodity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.api.commodity.dto.CommodityDetatilResult;
import com.chinamobo.ue.api.commodity.dto.CommodityResult;
import com.chinamobo.ue.commodity.entity.TomCommodity;

public interface TomCommodityMapper {
    int deleteByPrimaryKey(Integer commodityId);

    int insert(TomCommodity record);

    int insertSelective(TomCommodity record);

    TomCommodity selectByPrimaryKey(Integer commodityId);

    int updateByPrimaryKeySelective(TomCommodity record);

    int updateByPrimaryKey(TomCommodity record);

    /**
     * 
     * 功能描述：[分页查询]
     *
     * 创建者：JCX
     * 创建时间: 2016年5月19日 上午11:36:26
     * @param map
     * @return
     */
	List<TomCommodity> selectByPage(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[查询总记录数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月19日 上午11:38:36
	 * @param map
	 * @return
	 */
	int countAll(Map<Object, Object> map);
	/**
	 * 商城列表查询总条数
	 * 创建者：LG
	 * 
	 */
	int countByuserId(Map<Object, Object> map);
	/**
	 * 商城列表分页查询
	 * 创建者：LG
	 */
	List<CommodityResult> selectByuserId(Map<Object, Object> map);
  
	/**
	 * 根据userId,commodityId查询E币商品详情
	 * 创建者：LG
	 */
	List<CommodityDetatilResult> selectBycommodity(Map<Object, Object> map);
}