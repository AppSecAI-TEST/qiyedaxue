package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.system.entity.TomMessageDetails;

public interface TomMessageDetailsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tom_message_details
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tom_message_details
     *
     * @mbggenerated
     */
    int insert(TomMessageDetails record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tom_message_details
     *
     * @mbggenerated
     */
    int insertSelective(TomMessageDetails record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tom_message_details
     *
     * @mbggenerated
     */
    TomMessageDetails selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tom_message_details
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomMessageDetails record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tom_message_details
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomMessageDetails record);
    
    List <TomMessageDetails> selectList ();
}