package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.exam.entity.TomQuestionBank;
import com.chinamobo.ue.system.entity.TomOrgGroups;

public interface TomOrgGroupsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ORG_GROUPS
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ORG_GROUPS
     *
     * @mbggenerated
     */
    int insert(TomOrgGroups record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ORG_GROUPS
     *
     * @mbggenerated
     */
    int insertSelective(TomOrgGroups record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ORG_GROUPS
     *
     * @mbggenerated
     */
    TomOrgGroups selectByPrimaryKey(String code);
    
    TomOrgGroups selectPrimary();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ORG_GROUPS
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomOrgGroups record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_ORG_GROUPS
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomOrgGroups record);
    
   /**
    * 
    * 功能描述：[查询所有集团]
    *
    * 创建者：cjx
    * 创建时间: 2016年3月1日 下午2:34:34
    * @return
    */
    List<Tree> select();
    	
}