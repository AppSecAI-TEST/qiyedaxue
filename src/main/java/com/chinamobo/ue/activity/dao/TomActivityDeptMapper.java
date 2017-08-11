package com.chinamobo.ue.activity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.entity.TomActivityDept;
import com.chinamobo.ue.activity.entity.TomActivityFees;
import com.chinamobo.ue.api.activity.dto.ActResults;
 
public interface TomActivityDeptMapper {//extends BaseDao<TomActivityDept>{
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ACTIVITY_DEPT
     *
     * @mbggenerated
     */
    int insert(TomActivityDept record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ACTIVITY_DEPT
     *
     * @mbggenerated
     */
    int insertSelective(TomActivityDept record);
    
    List<ActResults> findLeadEmp(Map<Object,Object> map);
    
    /**
     * 功能描述：[负责推送学员姓名]
     *
     * 创建者：Lg
     * 创建时间: 2016年3月22日 下午5:46:39
     * @param map
     * @return
     */
    List<ActResults> findLeadEmpFname(Map<Object,Object> map);
    
    int updateByPrimaryKeySelective(TomActivityDept record);
    
    void updateByUserActivity(Map<Object,Object> map);

	List<TomActivityDept> selectByActivityId(int activityId);

	/**
	 * 
	 * 功能描述：[根据活动ID删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月25日 下午4:22:08
	 * @param activityId
	 */
	void deleteByActivityId(Integer activityId);

	/**
	 * 
	 * 功能描述：[查询推送人]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月26日 下午2:37:10
	 * @param example
	 * @return
	 */
	TomActivityDept selectByExample(TomActivityDept example);
}
