package com.chinamobo.ue.activity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomTaskPackageMapper;
import com.chinamobo.ue.activity.entity.TomTaskPackage;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomLecturerMapper;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.ums.DBContextHolder;
/**
 * 版本: [1.0]
 * 功能说明: 选择列表工具类
 *
 * 作者: WangLg
 * 创建时间: 2016年3月15日 下午2:51:53
 */
@Service
public class ChoiceRestService {

	@Autowired
	private TomLecturerMapper lecturerMapper;
	@Autowired
	private TomTaskPackageMapper taskPackageMapper;
	@Autowired
	private TomDeptMapper deptMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomExamMapper examPaperMapper;
	
	/**
	 * 功能描述：[选择讲师]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午2:54:43
	 * @return
	 */
	public List<TomLecturer> selectAllName(){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return lecturerMapper.selectAllName();
	}
	
	/**
	 * 功能描述：[选择活动包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午2:58:43
	 * @return
	 */
	public List<TomTaskPackage> selectAllTask(){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return taskPackageMapper.selectAllTask();
	}
	

	/**
	 * 功能描述：[选择部门]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午2:58:43
	 * @return
	 */
	public List<TomDept> selectAllDept(){
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return deptMapper.select();
	}
	/**
	 * 功能描述：[选择]管理员
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午2:58:43
	 * @return
	 */
	public List<TomDept> findAdmin(){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//TODO
		return deptMapper.select();
	}
	/**
	 * 
	 * 功能描述：[选择课程]
	 * 创建者：Wanglg
	 * 创建时间: 2016年3月15日 下午4:23:34
	 */
	public List<TomCourses> findCourses(){
		
		//TODO
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return coursesMapper.findCourses();
	}
	/**
	 * 
	 * 功能描述：[选择试卷]
	 * 创建者：Wanglg
	 * 创建时间: 2016年3月15日 下午4:23:34
	 */
	public List<TomExamPaper> findExampaper(){
		//TODO
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return examPaperMapper.findExampaper();
	}
	
}
