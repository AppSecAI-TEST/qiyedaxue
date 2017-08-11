package com.chinamobo.ue.commodity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.api.commodity.dto.CommodityExchangeResult;
import com.chinamobo.ue.commodity.entity.TomCommodityExchange;

public interface TomCommodityExchangeMapper {
    int insert(TomCommodityExchange record);

    int insertSelective(TomCommodityExchange record);
    
    /**
     * 
     * 功能描述：[查询数量]
     *
     * 创建者：GW
     * 创建时间: 2016年5月18日
     * @param
     * @return
     */
    int count(Map<Object,Object> map);
    
    /**
     * 
     * 功能描述：[分页查询]
     *
     * 创建者：GW
     * 创建时间: 2016年5月18日
     * @param map
     * @return
     */
    List<TomCommodityExchange> selectPage(Map<Object,Object> map);
    
    /**
     * 
     * 功能描述：[查询全部]
     *
     * 创建者：GW
     * 创建时间: 2016年5月18日
     * @param 
     * @return
     */
    List<TomCommodityExchange> queryList(Map<Object,Object> map);
    int countByMap(Map<Object, Object> map);
    
    List<CommodityExchangeResult> selectByCode(Map<Object, Object> map);
    
   List< CommodityExchangeResult> selectExchangeDeails(Map<Object, Object> map);

    /**
     * 
     * 功能描述：[条件查询]
     *
     * 创建者：JCX
     * 创建时间: 2016年6月2日 下午6:12:25
     * @param example
     * @return
     */
	List<TomCommodityExchange> selectByExample(TomCommodityExchange example);
	
	/**
	 * 
	 * 功能描述：根据人员Code商品ID查询人员的详细信息
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月9日
	 * @param code
	 * @return
	 */
	TomCommodityExchange selectByPrimaryKey(TomCommodityExchange example);
	
	int updateExample(TomCommodityExchange example);
}