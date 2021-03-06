package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.chinamobo.ue.course.dto.TomGradeDimensionDto;
import com.chinamobo.ue.course.entity.TomGradeDimension;
public interface TomGradeDimensionMapper {//extends BaseDao<TomGradeDimension>{
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_GRADE_DIMENSION
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer gradeDimensionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_GRADE_DIMENSION
     *
     * @mbggenerated
     */
    int insert(TomGradeDimension record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_GRADE_DIMENSION
     *
     * @mbggenerated
     */
    int insertSelective(TomGradeDimension record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_GRADE_DIMENSION
     *
     * @mbggenerated
     */
    TomGradeDimension selectByPrimaryKey(Integer gradeDimensionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_GRADE_DIMENSION
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomGradeDimension record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_GRADE_DIMENSION
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomGradeDimension record);
 
    
    /**
     * 功能描述：[评分关咯]
     *
     * 创建者：WangLg
     * 创建时间: 2016年3月7日 下午3:48:45
     * @param record
     */
    void updateByPrimaryParam(TomGradeDimension record);
    
    List<TomGradeDimension> selectByPrimaryParam(Map queryMap);
    
    //Test��װ
    Page<TomGradeDimension> selectByPrimaryParam11(Map queryMap);
    
    List<TomGradeDimension> selectByPrimaryParam2(List<String> idList);
    
    /**
     * 功能描述：[评论管理-查询]
     *
     * 创建者：WangLg
     * 创建时间: 2016年3月15日 下午8:13:22
     * @param task
     * @return
     */
    int countByList(Map<Object,Object> map);
    List<TomGradeDimension> selectListByParam(Map<Object,Object> map);
    List<TomGradeDimension> selectAllList(Map<Object,Object> map);
    
    /**
     * 功能描述：[评价维度模板调用]
     *
     * 创建者：WangLg
     * 创建时间: 2016年4月5日 下午8:13:22
     * @param task
     * @return
     */
    List<TomGradeDimension> findGradeDimenModel(Map<Object,Object> map);
    
	TomGradeDimensionDto  findDetails(Map<Object,Object> map);
	/**
	 * 
	 * 功能描述：[伪删除]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年4月28日 下午3:38:40
	 * @param gradeDimensionId
	 * @return
	 */
	int delete(Integer gradeDimensionId);
    
}