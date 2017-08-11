package com.chinamobo.ue.activity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.dto.TomActivityDto;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.api.activity.dto.ActResults;
import com.chinamobo.ue.api.activity.dto.MyCertificateDto;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.statistics.entity.TomActivityCostStatistics;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.DataMasterSlaveAnnotation;

public interface TomActivityMapper {// extends BaseDao<TomActivity>{
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int deleteByPrimaryKey(Integer activityId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int insert(TomActivity record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int insertSelective(TomActivity record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	TomActivity selectByPrimaryKey(Integer activityId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int updateByPrimaryKeySelective(TomActivity record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int updateByPrimaryKey(TomActivity record);

	/**
	 * 
	 * ����������[��б�]
	 *
	 * �����ߣ�WangLg ����ʱ��: 2016��3��10�� ����5:23:53
	 * 
	 * @param activityId
	 * @return
	 */
	PageData<TomActivity> selectListByParames();

	/**
	 * ����������[API-����ѧԱ�Ļ�б�]
	 *
	 * �����ߣ�WangLg ����ʱ��: 2016��3��10�� ����5:36:19
	 * 
	 * @param activityId
	 * @return
	 */
	PageData<TomActivity> selectListByParames1(TomActivity activityId);

	/**
	 * 培训活动列表
	 * 
	 * @param task
	 * @return
	 */
	int countByList(TomActivity act);

	List<TomActivity> selectListByParam(Map<Object, Object> map);

	List<TomActivity> selectAllList(TomActivity act);

	List<TomActivity> selectList();

	int countByList(Map<Object, Object> map);
	
	int countByListGlobel(Map<Object, Object> map);

	List<ActResults> selectByUserListGlobel(Map<Object, Object> map);

	/**
	 * API 活动详情
	 */
	ActResults selectByUser(Map<Object, Object> map);

	List<ActResults> selectByUserList(Map<Object, Object> map);

	// 培训活动个人报名
	ActResults selectByUserRe(Map<Object, Object> map);

	ActResults selectByUserDetail(Map<Object, Object> map);

	List<TomActivityDto> selectDetails(Map<Object, Object> map);

	List<ActResults> FindAllBylist(Map<Object, Object> Map);

	/**
	 * API 查询活动通过未通过 通过考试成绩状态来查询
	 */

	int FindByCoursecount(Map<Object, Object> Map);

	List<ActResults> FindByCourseList(Map<Object, Object> map);

	/**
	 * API 查询负责人活动报名推送的活动人员 通过登录人员ID查询
	 */

	int FindByUserIdCount(Map<Object, Object> Map);

	List<ActResults> FindByUserIdList(Map<Object, Object> Map);

	/**
	 * 
	 * 功能描述：[任务中活动数量]
	 *
	 * 创建者：JCX 创建时间: 2016年5月26日 下午2:18:05
	 * 
	 * @param queryMap
	 * @return
	 */
	int countByUserList(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[根据员工查出其所有活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 上午10:14:09
	 * 
	 * @param code
	 * @return
	 */
	List<TomActivity> selectAllByEmp(String code);

	/**
	 * 
	 * 功能描述：[统计未开始活动数]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 上午11:22:57
	 * 
	 * @param Map
	 * @return
	 */
	int countNotStart(Map<Object, Object> Map);

	/**
	 * 
	 * 功能描述：[统计进行中活动数]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 上午11:44:25
	 * 
	 * @param queryMap
	 * @return
	 */
	int countUnderway(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[统计已完成活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 上午11:56:11
	 * 
	 * @param queryMap
	 * @return
	 */
	int countFinished(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[统计未通过活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:29:09
	 * 
	 * @param queryMap
	 * @return
	 */
	int countNotPass(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[查询未开始活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:41:46
	 * 
	 * @param queryMap
	 * @return
	 */
	List<TomActivity> selectNotStart(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[查询已开始活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:46:35
	 * 
	 * @param queryMap
	 * @return
	 */
	List<TomActivity> selectUnderway(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[查询已完成活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:50:14
	 * 
	 * @param queryMap
	 * @return
	 */
	List<TomActivity> selectFinished(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[查询未通过活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:52:29
	 * 
	 * @param queryMap
	 * @return
	 */
	List<TomActivity> selectNotPass(Map<Object, Object> queryMap);

	/**
	 * 功能介绍[根据视图分页查询] 创建者：LG 创建时间：2016年6月3日 15：21
	 */
	List<TomActivityCostStatistics> selectactivityCostList(Map<Object, Object> map);

	int selectCount(Map<Object, Object> map);
	
	List<TomActivity> selectByEmpActivity(Map<Object,Object> map);
	
 int	CountByEmpActivity (Map<Object,Object> map );
 
 	@DataMasterSlaveAnnotation(name=DBContextHolder.SLAVE)
 	Map<String,String> findOne(Integer activityId);
 	/**
 	 * 
 	 * @Title: selectMyCertificateDto 
 	 * @Description: 查询我的证书 
 	 * @author Acemon 
 	 * @date 2017年6月15日 下午3:55:49
 	 * @return List<MyCertificateDto>
 	 */
 	List<MyCertificateDto> selectMyCertificate(Map<Object,Object> map);
 	int countMyCertificate(Map<Object,Object> map);
}