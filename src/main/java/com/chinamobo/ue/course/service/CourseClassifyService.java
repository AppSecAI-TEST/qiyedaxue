package com.chinamobo.ue.course.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.common.servise.TreeServise;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.dao.TomCourseClassifyRelationMapper;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourseClassifyRelation;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.MapManager;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程分类service
 *
 * 作者: JCX
 * 创建时间: 2016年3月1日 下午2:04:36
 */
@Service
public class CourseClassifyService {

	@Autowired
	private TomCourseClassifyMapper courseClassifyMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomCourseClassifyRelationMapper courseClassifyRelationMapper;
	
	/**
	 * 
	 * 功能描述：[添加]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:04:54
	 * @param courseClassify
	 */
	@Transactional
	public void addCourseClassify(TomCourseClassify courseClassify) {
		String classifyNumber= numberRecordService.getNumber(MapManager.numberType("KCFL"));
		courseClassify.setClassifyNumber(classifyNumber);
		courseClassify.setStatus("Y");
		courseClassify.setCreateTime(new Date());
		courseClassify.setUpdateTime(new Date());
		courseClassifyMapper.insertSelective(courseClassify);
	}

	/**
	 * 
	 * 功能描述：[按id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:05:06
	 * @param classifyId
	 * @return
	 */
	public TomCourseClassify selectById(int classifyId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourseClassify courseClassify=courseClassifyMapper.selectByPrimaryKey(classifyId);
		if(courseClassify.getParentClassifyId()==0){
			courseClassify.setParentClassifyName("-");
		}else{
			courseClassify.setParentClassifyName(courseClassifyMapper.selectByPrimaryKey(courseClassify.getParentClassifyId()).getClassifyName());
		}
		return courseClassify;
	}

	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:05:21
	 * @param pageNum
	 * @param pageSize
	 * @param classifyName
	 * @param classifyId
	 * @return
	 */
	public PageData<TomCourseClassify> selectByPage(int pageNum, int pageSize, String classifyName,int classifyId) {
	/*	if(classifyName!=null){
			classifyName=classifyName.replaceAll("/", "//");
			classifyName=classifyName.replaceAll("%", "/%");
			classifyName=classifyName.replaceAll("_", "/_");
			
		}*/
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourseClassify example=new TomCourseClassify();
		PageData<TomCourseClassify> page=new PageData<TomCourseClassify>();				
		example.setClassifyName(classifyName);	
		
		List<TomCourseClassify> list=selectByParentClassifyId(courseClassifyMapper.selectAll(),classifyId);
		//获取符合查询条件的id
		String ids="-1";
		for(TomCourseClassify courseClassify:list){
			ids=ids+","+courseClassify.getClassifyId();
		}
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("example", example);
		map.put("ids", ids);
		int count =courseClassifyMapper.countByExample(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		
		List<TomCourseClassify> pageList =courseClassifyMapper.selectListByPage(map);
		for(TomCourseClassify courseClassify:pageList){
			if(courseClassify.getParentClassifyId()!=0){
				courseClassify.setParentClassifyName(courseClassifyMapper.selectByPrimaryKey(courseClassify.getParentClassifyId()).getClassifyName());
			}else{
				courseClassify.setParentClassifyName("-");
			}
			
		}
		page.setDate(pageList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}

	/**
	 * 
	 * 功能描述：[更新]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:06:15
	 * @param courseClassify
	 */
	@Transactional
	public void updateCourseClassify(TomCourseClassify courseClassify) {
		courseClassify.setUpdateTime(new Date());
		courseClassifyMapper.updateByPrimaryKeySelective(courseClassify);
		
	}

	/**
	 * 
	 * 功能描述：[伪删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:06:30
	 * @param courseClassify
	 */
	public String updateStatus(TomCourseClassify courseClassify) {
		List<TomCourseClassify> list = courseClassifyMapper.selectAll();
		List<TomCourseClassify> childrenList=new ArrayList<TomCourseClassify>();
		for(int i=0;i<list.size();i++){
			if(isChild(list.get(i),courseClassify.getClassifyId())==true){
				childrenList.add(list.get(i));
			}
		}
		TomCourseClassifyRelation courseClassifyRelation;
		courseClassifyRelation=new TomCourseClassifyRelation();
		courseClassifyRelation.setClassifyId(courseClassify.getClassifyId());
		if(courseClassifyRelationMapper.selectByExample(courseClassifyRelation).size()!=0){
			return "protected";
		}else if(childrenList.size()>1){
			return "hasChildren";
		}
		courseClassify.setUpdateTime(new Date());
		courseClassify.setStatus("N");
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		courseClassifyMapper.updateByPrimaryKeySelective(courseClassify);
		return "success";
	}
	
	/**
	 * 
	 * 功能描述：[检查是否是当前单击的分类的子节点]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:06:40
	 * @param courseClassify
	 * @param classifyId
	 * @return
	 */
	public boolean isChild(TomCourseClassify courseClassify,int classifyId){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(courseClassify.getParentClassifyId()==classifyId||classifyId==courseClassify.getClassifyId()){
			return true;
		}else if(courseClassify.getParentClassifyId()==0){
			return false;
		}else{
		   TomCourseClassify classify=courseClassifyMapper.getParent(courseClassify.getParentClassifyId());
		   if(classify==null){
			   System.out.println(courseClassify.getClassifyId()+"的父节点"+courseClassify.getParentClassifyId());
			   return false;
		   }else{
			   return isChild(classify, classifyId);
		   }
		}
	}
	
	/**
	 * 
	 * 功能描述：[从根据分类名筛选出的课程分类列表中进行子节点选择]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:07:11
	 * @param list
	 * @param classifyId
	 * @return
	 */
	public List<TomCourseClassify> selectByParentClassifyId(List<TomCourseClassify> list,int classifyId){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomCourseClassify> childrenList=new ArrayList<TomCourseClassify>();

		//扫描子节点和本身
		for(TomCourseClassify courseClassify:list){
			if(isChild(courseClassify, classifyId)||courseClassify.getClassifyId()==classifyId){
				childrenList.add(courseClassify);
			}
		}
	
		return childrenList;
	}

	/**
	 * 
	 * 功能描述：[根据父id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月8日 下午3:37:11
	 * @param i
	 * @return
	 */
	public List<TomCourseClassify> selectByParentClassifyId(int i) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return courseClassifyMapper.selectByParentId(i);
	}
	
	/**
	 * 
	 * 功能描述：[课程分类树形菜单]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月12日 上午10:34:26
	 * @return
	 */
	public Tree getClassifyTree() {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Tree root=new Tree();
		root.setCode("0");
		List<Tree> list=courseClassifyMapper.selectClassifyAsTree();
		TreeServise treeServise=new TreeServise();
		Tree tree=treeServise.recursiveTree(root, list);
		return tree;
	}
	
	/**
	 * 
	 * 功能描述：[获取分类的所有父分类id]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月22日 下午2:51:16
	 * @param classifyIds
	 * @return
	 */
	public HashSet<Integer> getFathers(List<Integer> classifyIds){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashSet<Integer> set=new HashSet<Integer>();
		for(int classifyId:classifyIds){
			while(classifyId!=0){
				set.add(classifyId);
				classifyId=courseClassifyMapper.selectByPrimaryKey(classifyId).getParentClassifyId();
			}
		}
		 
		return set;
	}

}
