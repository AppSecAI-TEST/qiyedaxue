package com.chinamobo.ue.exam.dao;

import java.util.List;

import com.chinamobo.ue.exam.entity.TomTopic;

public interface TomTopicMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer topicId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC
     *
     * @mbggenerated
     */
    int insert(TomTopic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC
     *
     * @mbggenerated
     */
    int insertSelective(TomTopic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC
     *
     * @mbggenerated
     */
    TomTopic selectByPrimaryKey(Integer topicId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomTopic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomTopic record);
    
    List<TomTopic> selectByqbIdAndType(Integer questionBankId,String questionType);

    /**
     * 
     * 功能描述：[根据试卷id查询题目]
     *
     * 创建者：JCX
     * 创建时间: 2016年3月18日 下午3:31:31
     * @param examPaperId
     * @return
     */
	List<TomTopic> selectByExamPaperId(Integer examPaperId);
	/**
	 * 
	 * 功能描述：[根据题库id查询题目]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月31日 上午10:36:18
	 * @param questionBankId
	 * @return
	 */
	List<TomTopic> selectByBankId(Integer questionBankId);

	/**
	 * 
	 * 功能描述：[条件查询题目]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月13日 上午10:25:10
	 * @param topicExample
	 * @return
	 */
	List<TomTopic> selectByExample(TomTopic topicExample);

	/**
	 * 
	 * 功能描述：[通过题库id删除题目]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月11日 上午11:34:56
	 * @param id
	 */
	void deleteByQuestionBankId(int id);
	
	List<TomTopic> selectAll();
}