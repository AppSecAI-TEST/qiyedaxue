package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.api.commodity.dto.CommodityResult;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.TomUserLog;

public interface TomUserInfoMapper {
    int insert(TomUserInfo record);

    int insertSelective(TomUserInfo record);

	void updateByCode(TomUserInfo tomUserInfo);
	
	/**
	 * 
	 * 功能描述：根据人员Code查询人员的详细信息
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月22日下午2:31:04
	 * @param code
	 * @return
	 */
	TomUserInfo selectByPrimaryKey(String code);
	
	
	/**
	 * 
	 * 功能描述：根据e币数查询比自己e币多的人员
	 *
	 * 创建者：guowei
	 * 创建时间: 2016年3月31日 下午2:00:57
	 * @param eNumber
	 * @return
	 */
	int countByENumber(int eNumber);
	
	/**
	 * 
	 * 功能描述：根据累计e币数计算大于并且等于自己e币数的人员
	 *
	 * 创建者：guowei
	 * 创建时间: 2016年3月31日 下午2:03:43
	 * @param eNumber
	 * @param addUpENumber
	 * @return
	 */
	int countByAddUpENumber(int eNumber, int addUpENumber);
	
	
	/**
	 * 
	 * 功能描述：查询记录条数
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月24日上午11:55:31
	 * @param example
	 * @return
	 */
	int countByExample(TomUserInfo example);
	
	/**
	 * 
	 * 功能描述：查询排行榜信息
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月24日上午11:54:17
	 * @param map
	 * @return
	 */
	List<TomUserInfo> selectTopTime(Map<Object, Object> map);
	
	TomUserInfo selectByUserLog(String code);
	/**
	 * E币排行榜
	 * 创建者：LG
	 */
   List<CommodityResult> selectByeNumber(Map<Object, Object> map);
   
   int selectCountByeNumber();

   void insertList(List<TomUserInfo> list1);

   Integer rankByLearnTime(int learningTime);

  /**
   * 查询当前用户以及E币数量
   * 创建者：LG
   * 创建时间：2016年6月12日 11：16
   */
   CommodityResult selectBycode(Map<Object, Object> map);

   /**
    * 
    * 功能描述：[根据code删除]
    *
    * 创建者：JCX
    * 创建时间: 2016年6月14日 上午9:44:37
    * @param code
    */
   void deleteByCode(String code);
   
   void updateByPrimaryKeySelective(TomUserInfo tomUserInfo);
   List<TomUserInfo> selectAll();
}

