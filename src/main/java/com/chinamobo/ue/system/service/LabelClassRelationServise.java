package com.chinamobo.ue.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.dao.TomLabelClassMapper;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelMapper;
import com.chinamobo.ue.system.entity.TomLabel;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class LabelClassRelationServise {
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	@Autowired
	private TomLabelClassMapper tomLabelClassMapper;
	@Autowired
	private TomLabelMapper tomLabelMapper;
	
	//标签分类树形菜单
	@Transactional
	public Tree getClassifyTree() {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<Tree> list = new ArrayList<Tree>();
		//全查标签分类表
		list = tomLabelClassMapper.selectLabelClassifyAsTree();
		Tree root=new Tree();
		for(Tree tree : list){
			tree.setType("L");
//			tree.setClassId(tree.getCode());
			tree.setFathercode("0");
			if(tree.getCode().equals("1")){
				tree.setStatuss("1");
			}else{
				tree.setStatuss("2");
			}
			//根据标签分类查询标签
			List<Tree> labelList = tomLabelClassRelationMapper.selectLabelAsTree(Integer.valueOf(tree.getCode()));
			for(Tree labelTree : labelList){
				labelTree.setType("L");
				labelTree.setFathercode("1");
				labelTree.setTopcode(tree.getCode());
				if(labelTree.getClassId().equals("1")){
					labelTree.setStatuss("4");
				}else{
					labelTree.setStatuss("5");
				}
			}
			tree.setChildren(labelList);
		}
		//查询全部标签
		List<TomLabel> labelListAll = tomLabelMapper.selectAll();
		for(TomLabel label : labelListAll){
			Tree tree=new Tree();
			//根据标签id查询标签分类关联表
			int countTagId = tomLabelClassRelationMapper.countBytagId(label.getTagId());
			if(countTagId==0){
				tree.setType("L");
				tree.setCode(label.getTagId());
				tree.setName(label.getTagName());
				tree.setFathercode("0");
				tree.setStatuss("3");
				list.add(tree);
			}
		}
		root.setChildren(list);
		return root;
	}
}
