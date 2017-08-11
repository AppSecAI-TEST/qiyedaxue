package com.chinamobo.ue.course.dao;

import java.util.List;

import com.chinamobo.ue.course.entity.TomCourseClassifyRelation;

public interface TomCourseClassifyRelationMapper {
    int insert(TomCourseClassifyRelation record);

    int insertSelective(TomCourseClassifyRelation record);

    /**
     * 
     * 功能描述：[条件查询课程分类关联]
     *
     * 创建者：JCX
     * 创建时间: 2016年4月12日 下午2:25:57
     * @param courseClassifyRelation
     * @return
     */
	List<TomCourseClassifyRelation> selectByExample(TomCourseClassifyRelation courseClassifyRelation);

	/**
	 * 
	 * 功能描述：[更新]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月12日 下午2:29:42
	 * @param courseClassifyRelation
	 */
	void updateSelective(TomCourseClassifyRelation courseClassifyRelation);

	/**
	 * 
	 * 功能描述：[删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月13日 下午12:59:53
	 * @param classifyRelationExample
	 */
	void deleteByExample(TomCourseClassifyRelation classifyRelationExample);
}