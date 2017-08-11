/**
 * 
 */
package com.chinamobo.ue.common.tree.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.common.tree.Node;
import com.chinamobo.ue.common.tree.Tree;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.ums.DBContextHolder;

/**
 * 版本: [1.0]
 * 功能说明: 课程分类树形结构;
 * 作者: WChao 创建时间: 2017年6月16日 下午4:29:56
 */
@Service
public class CourseClassifyTree extends Tree<TomCourseClassify>{
	@Autowired
	private TomCourseClassifyMapper courseClassifyMapper;

	@Override
	public void CreateTree(Node<TomCourseClassify> node) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomCourseClassify> list=courseClassifyMapper.selectByParentId(Integer.valueOf(node.getId()));
		if(list != null && list.size() > 0){
			for(TomCourseClassify tomCourseClassify : list){
				node.addNode(tomCourseClassify);
				this.CreateTree(tomCourseClassify);//递归调用;
			}
		}
	}
}
