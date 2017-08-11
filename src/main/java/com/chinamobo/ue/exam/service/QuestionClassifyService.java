package com.chinamobo.ue.exam.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.common.servise.TreeServise;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.exam.dao.TomQuestionBankMapper;
import com.chinamobo.ue.exam.dao.TomQuestionClassificationMapper;
import com.chinamobo.ue.exam.entity.TomQuestionBank;
import com.chinamobo.ue.exam.entity.TomQuestionClassification;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 
 * 版本: [1.0]
 * 功能说明: 题库分类service
 *
 * 作者: JCX
 * 创建时间: 2016年4月7日 下午3:25:46
 */
@Service
public class QuestionClassifyService {
	
	@Autowired
	private TomQuestionClassificationMapper classificationMapper;

	@Autowired
	private TomQuestionClassificationMapper questionClassificationMapper;
	
	@Autowired
	private QuestionBankService questionBankService;
	@Autowired
	private TomQuestionBankMapper questionBankMapper;
	/**
	 * 
	 * 功能描述：[分页]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午4:21:23
	 * @param pageNum
	 * @param pageSize
	 * @param questionClassificationName
	 * @param questionClassificationId
	 * @return
	 */
	public PageData<TomQuestionClassification> selectByPage(int pageNum, int pageSize,
			String questionClassificationName, int questionClassificationId) {
		
		DBContextHolder.setDbType(DBContextHolder.SLAVE); 
//		if(questionClassificationName!=null){
//			questionClassificationName=questionClassificationName.replaceAll("/", "//");
//			questionClassificationName=questionClassificationName.replaceAll("%", "/%");
//			questionClassificationName=questionClassificationName.replaceAll("_", "/_");
//		}
		
		TomQuestionClassification example=new TomQuestionClassification();
		PageData<TomQuestionClassification> page=new PageData<TomQuestionClassification>();				
		example.setQuestionClassificationName(questionClassificationName);	
		
		List<TomQuestionClassification> list=selectByParentClassifyId(questionClassificationMapper.selectListByExample(new TomQuestionClassification()),questionClassificationId);
		//获取符合查询条件的id
		String ids="-1";
		for(TomQuestionClassification questionClassification:list){
			ids=ids+","+questionClassification.getQuestionClassificationId();
		}
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("example", example);
		map.put("ids", ids);
		int count =questionClassificationMapper.countByMap(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		
		List<TomQuestionClassification> pageList =questionClassificationMapper.selectListByPage(map);
		for(TomQuestionClassification questionClassification:pageList){
			if(questionClassification.getParentClassificationId()!=0){
				questionClassification.setParentClassificationName(questionClassificationMapper.selectByPrimaryKey(questionClassification.getParentClassificationId()).getQuestionClassificationName());
			}else{
				questionClassification.setParentClassificationName("-");
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
	 * 功能描述：[从根据查询结果中进行子节点选择]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午3:47:16
	 * @param list
	 * @param questionClassificationId
	 * @return
	 */
	public List<TomQuestionClassification> selectByParentClassifyId(List<TomQuestionClassification> list,int questionClassificationId){
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomQuestionClassification> childrenList=new ArrayList<TomQuestionClassification>();

		//扫描子节点和本身
		for(TomQuestionClassification questionClassification:list){
			if(isChild(questionClassification, questionClassificationId)||questionClassification.getQuestionClassificationId()==questionClassificationId){
				childrenList.add(questionClassification);
			}
		}
	
		return childrenList;
	}
	
	/**
	 * 
	 * 功能描述：[题库分类删除]
	 *
	 * 创建者：wjx 创建时间: 2016年3月2日 下午5:01:47
	 * 
	 * @param questionClassificationId
	 */
	@Transactional
	public String deleteClassification(int questionClassificationId) {
		List<TomQuestionClassification> list = questionClassificationMapper.selectList();
		List<TomQuestionClassification> childrenList=new ArrayList<TomQuestionClassification>();
		for(int i=0;i<list.size();i++){
			if(isChild(list.get(i),questionClassificationId)==true){
				childrenList.add(list.get(i));
			}
		}
		
		TomQuestionBank questionBank=new TomQuestionBank();
		questionBank.setQuestionClassificationId(questionClassificationId);
		if(questionBankMapper.count(questionBank)>0){
			return "protected";
		}else if(childrenList.size()>1){
			return "hasChildren";
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		classificationMapper.deleteByPrimaryKey(questionClassificationId);
		return "success";
	}
	
	/**
	 * 
	 * 功能描述：[判断一个节点是否是某个节点的后代]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午3:42:01
	 * @param questionClassification 需要验证的节点
	 * @param questionClassificationId 父节点id
	 * @return
	 */
	public boolean isChild(TomQuestionClassification questionClassification,int questionClassificationId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(questionClassificationId==questionClassification.getParentClassificationId()||questionClassificationId==questionClassification.getQuestionClassificationId()){
			return true;
		}else if(questionClassification.getParentClassificationId()==0){
			return false;
		}else{
			TomQuestionClassification classify=questionClassificationMapper.selectByPrimaryKey(questionClassification.getParentClassificationId());
		    if(classify==null){
			   System.out.println(questionClassification.getQuestionClassificationId()+"的父节点"+questionClassification.getParentClassificationId());
			   return false;
		    }
		   return isChild(classify, questionClassificationId);
		}
	}

	/**
	 * 
	 * 功能描述：[题库分类树]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午4:57:57
	 * @return
	 */
	public Tree getClassifyTree() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Tree root=new Tree();
		root.setCode("0");
		List<Tree> list=questionClassificationMapper.selectClassifyAsTree();
		TreeServise treeServise=new TreeServise();
		Tree tree=treeServise.recursiveTree(root, list);
		return tree;
	}
	
}
