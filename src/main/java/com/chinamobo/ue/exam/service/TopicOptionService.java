package com.chinamobo.ue.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.ums.DBContextHolder;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 题目选项service
 *
 * 作者: JCX
 * 创建时间: 2016年4月5日 上午11:50:59
 */
@Service
public class TopicOptionService {

	@Autowired
	private TomTopicOptionMapper topicOptionMapper;
	
	/**
	 * 
	 * 功能描述：[根据id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月5日 上午11:51:19
	 * @param id
	 * @return
	 */
	public TomTopicOption selectById(int id) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return topicOptionMapper.selectByPrimaryKey(id);
	}

}
