package com.chinamobo.ue.course.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseCommentMapper;
import com.chinamobo.ue.course.entity.TomCourseComment;
import com.chinamobo.ue.ums.DBContextHolder;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程评论service
 *
 * 作者: JCX
 * 创建时间: 2016年3月15日 下午2:37:43
 */
@Service
public class CourseCommentService {
	@Autowired
	private TomCourseCommentMapper commentMapper;

	@Autowired
	private CourseService courseService;
	/**
	 * 
	 * 功能描述：[分页查询课程评论]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月15日 下午2:54:32
	 * @param pageNum
	 * @param pageSize
	 * @param courseId 
	 * @return
	 */
	public PageData<TomCourseComment> selectByPage(int pageNum, int pageSize, int courseId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourseComment example=new TomCourseComment();
		example.setCourseId(courseId);
		
		PageData<TomCourseComment> page=new PageData<TomCourseComment>();	
		List<TomCourseComment> list;
		int count=commentMapper.countByExample(example);
		
		if(pageSize==-1){
			pageSize=count;
		}
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		map.put("example", example);
		list = commentMapper.selectListByPage(map);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}

	/**
	 * 
	 * 功能描述：[更改评论禁用状态]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月15日 下午3:54:42
	 * @param courseCommentId
	 * @param status
	 */
	public void update(int courseCommentId, String status) {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomCourseComment courseComment=commentMapper.selectByPrimaryKey(courseCommentId);
		courseComment.setStatus(status);
		commentMapper.updateByPrimaryKeySelective(courseComment);
		
		courseService.updateComment(courseComment.getCourseId());
	}

	@Transactional
	public void addCourseComment(TomCourseComment courseComment) {
		courseComment.setCreateTime(new Date());
		courseComment.setStatus("Y");
		commentMapper.insertSelective(courseComment);
		
		courseService.updateComment(courseComment.getCourseId());
	}
}
