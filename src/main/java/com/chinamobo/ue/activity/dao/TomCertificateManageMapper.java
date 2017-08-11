package com.chinamobo.ue.activity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.dto.TomCertificateDto;
import com.chinamobo.ue.activity.dto.TomCertificateManageDto;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.DataMasterSlaveAnnotation;

/**
 * 描述 [证书管理持久类]
 * 创建者 LXT
 * 创建时间 2017年6月8日 上午9:53:00
 */
public interface TomCertificateManageMapper {
	/**
	 * 功能描述 [以活动为维度，查询有证书的活动]
	 * 创建者 LXT
	 * 创建时间 2017年6月13日 下午5:13:15
	 * @param map
	 * @return
	 */
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	List<Map<String,Object>> listTomCertificateManage(Map<String,Object> map);
	/**
	 * 功能描述 [总数 : 以活动为维度，查询有证书的活动]
	 * 创建者 LXT
	 * 创建时间 2017年6月13日 下午5:30:27
	 * @param map
	 * @return
	 */
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	int findListCount(Map<String,Object> map);
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	TomCertificateManageDto findOne(Integer id);
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	List<Map<String,Object>> findCertificateAddressByActivityId(Integer activityId);
	@DataMasterSlaveAnnotation(name=DBContextHolder.MASTER)
	void insert(TomCertificateManageDto tomCertificateManageDto);
	@DataMasterSlaveAnnotation(name=DBContextHolder.MASTER)
	void update(TomCertificateManageDto tomCertificateManageDto);
	@DataMasterSlaveAnnotation(name=DBContextHolder.MASTER)
	void deleteByActivityId(Integer activityId);
	@DataMasterSlaveAnnotation(name=DBContextHolder.MASTER)
	void delete(long id);
	/**
	 * 
	 * @Title: countByCodeAct 
	 * @Description: 统计一个人在一次活动有没有获得证书 
	 * @author Acemon 
	 * @date 2017年6月20日 上午11:26:33
	 * @return int
	 */
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	int countByCodeAct(Map<Object,Object> map);
}
