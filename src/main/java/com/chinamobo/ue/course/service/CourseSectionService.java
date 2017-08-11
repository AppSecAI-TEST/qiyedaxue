package com.chinamobo.ue.course.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.course.dao.TomCourseSectionMapper;
import com.chinamobo.ue.course.entity.TomCourseSection;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class CourseSectionService {
	
	@Autowired
	private TomCourseSectionMapper courseSectionMapper;
	
	/**
	 * 
	 * 功能描述：[章节添加]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月21日 下午1:33:01
	 * @param courseSection
	 */
	@Transactional
	public void add(TomCourseSection courseSection) {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String[] ss=courseSection.getSectionName().split("\\.");
		courseSection.setSectionType(ss[ss.length-1]);
		courseSection.setStatus("Y");
		courseSection.setCreateTime(new Date());
		courseSection.setUpdateTime(new Date());
		courseSectionMapper.insertSelective(courseSection);
	}

	/**
	 * 
	 * 功能描述：[查询章节列表]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月31日 下午2:00:13
	 * @param example
	 * @return
	 */
	public List<TomCourseSection> selectListByEXample(TomCourseSection example) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomCourseSection> list=courseSectionMapper.selectListByExample(example);
		return list;
	}
}
