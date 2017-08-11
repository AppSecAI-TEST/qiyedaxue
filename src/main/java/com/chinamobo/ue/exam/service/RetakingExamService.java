package com.chinamobo.ue.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.ums.DBContextHolder;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 补考信息service
 *
 * 作者: JCX
 * 创建时间: 2016年4月1日 下午1:28:05
 */
@Service
public class RetakingExamService {

	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	
	/**
	 * 
	 * 功能描述：[查询不考列表]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月1日 下午1:36:01
	 * @param example
	 * @return
	 */
	public List<TomRetakingExam> selectListByExample(TomRetakingExam example) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return retakingExamMapper.selectListByExample(example);
	}

	
}
