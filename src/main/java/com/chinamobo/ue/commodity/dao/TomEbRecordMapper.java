package com.chinamobo.ue.commodity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.commodity.entity.TomEbRecord;

public interface TomEbRecordMapper {
    int insert(TomEbRecord record);

    int insertSelective(TomEbRecord record);

	List<TomEbRecord> selectByUserId(Map<Object, Object> map);

	int countByUserId(Map<Object, Object> map);
	
	List<TomEbRecord> selectByRode(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[累计签到次数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月14日 下午2:55:13
	 * @param map
	 * @return
	 */
	int countTotalSignIn(Map<Object, Object> map);
}