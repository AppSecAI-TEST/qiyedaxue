package com.chinamobo.ue.activity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.dto.TomCertificateDto;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.DataMasterSlaveAnnotation;

import net.sf.json.JSONObject;

/**
 * 描述 [证书持久类]
 * 创建者 LXT
 * 创建时间 2017年6月8日 上午9:54:37
 */
public interface TomCertificateMapper {
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	List<TomCertificateDto> listTomCertificate(Map<String,Object> map);
	
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	int findListCount();
	
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	TomCertificateDto findOne(Integer id);
	
	@DataMasterSlaveAnnotation(name=DBContextHolder.MASTER)
	void insert(TomCertificateDto tomCertificateDto);
	
	@DataMasterSlaveAnnotation(name=DBContextHolder.MASTER)
	void update(TomCertificateDto tomCertificateDto);
	
	@DataMasterSlaveAnnotation(name=DBContextHolder.MASTER)
	void delete(Integer id);
	/**
	 * 
	 * @Title: selectByAct 
	 * @Description: 根据活动id查询证书信息
	 * @author Acemon 
	 * @date 2017年6月16日 下午4:54:25
	 * @return TomCertificateDto
	 */
	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
	TomCertificateDto selectByAct(int activityId);
}
