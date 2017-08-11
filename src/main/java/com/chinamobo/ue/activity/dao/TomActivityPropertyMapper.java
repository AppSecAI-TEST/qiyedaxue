package com.chinamobo.ue.activity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.common.BaseDao;
import com.chinamobo.ue.activity.entity.TomActivityProperty;

public interface TomActivityPropertyMapper {//extends BaseDao<TomActivityProperty>{
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ACTIVITY_PROPERTY
     *
     * @mbggenerated
     */
    int insert(TomActivityProperty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ACTIVITY_PROPERTY
     *
     * @mbggenerated
     */
    int insertSelective(TomActivityProperty record);
    
    /**
     * 
     * ������������ѯ�γ�����ѧϰʱ��
     *
     * �����ߣ�ZXM
     * ����ʱ��: 2016��3��30������2:58:13
     * @param map
     * @return
     */
    TomActivityProperty selectByNewDate(Map<Object, Object> map);
   
    /**
     * 
     * 功能描述：根据课程id和活动id查询活动配置
     *
     * 创建者：ZXM
     * 创建时间: 2016年4月8日下午5:17:47
     * @param map
     * @return
     */
    TomActivityProperty selectByCourseIdAndActivityId(Map<Object, Object> map);
    
    int updateByPrimaryKeySelective(TomActivityProperty record);

	List<TomActivityProperty> selectByTaskPackageId(Integer packageId);

	/**
	 * 
	 * 功能描述：[根据条件查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月25日 下午8:39:14
	 * @param activityId
	 * @return
	 */
	List<TomActivityProperty> selectByExample(TomActivityProperty example);

	/**
	 * 
	 * 功能描述：[根据活动ID删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月25日 下午4:01:30
	 * @param activityId
	 */
	void deleteByActivityId(Integer activityId);
	
	List<TomActivityProperty> InquiryActivityCurriculum(Map<Object, Object> map);
	
	int countByExample(Map<Object, Object> map);
	
	List<TomActivityProperty> selectByActivityId(int activityId);
	
	TomActivityProperty selectTask(TomActivityProperty example);
	
	
	TomActivityProperty  selectByExamId(Integer id);
	/**
	 * 
	 * @Title: selectByCourseId 
	 * @Description: 判断某课程是不是在活动下
	 * @author Acemon 
	 * @date 2017年7月7日 下午3:04:15
	 * @return TomActivityProperty
	 */
	List<TomActivityProperty> selectByCourseId(int courseId);
}