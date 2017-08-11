package com.chinamobo.ue.common.servise;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.service.ContextInitRedisService;
import com.chinamobo.ue.system.service.DeptServise;
import com.chinamobo.ue.ums.DBContextHolder;
@Service
public class TreeServise {
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private DeptServise deptServise;
	public Tree recursiveTree( Tree t,List<Tree> tree) {
		t.setChildren(new ArrayList<Tree>());
			for (Tree child : tree) {
				if(child.getFathercode()!=null){
					if(child.getFathercode().equals(t.getCode())){
						
						t.getChildren().add(recursiveTree(child,tree));
				}
				}
			}
		return t;
	}
	public Tree recursiveTree(Tree tree) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		tree.setChildren(new ArrayList<Tree>());

		List<Tree> childTreeNodes = tomDeptMapper.selectByPkFather(tree.getCode()); 
		//遍历子节点
		for(Tree child : childTreeNodes){
		Tree n = recursiveTree(child); //递归
		tree.getChildren().add(n);
		}
		 
		return tree;
		}
	public Tree recursiveTreeOrg( Tree t,List<Tree> tree) {
		t.setChildren(new ArrayList<Tree>());
			for (Tree child : tree) {
				if(child.getFathercode()!=null){
					if(child.getFathercode().equals(t.getCode())){
						
						t.getChildren().add(recursiveTree(child,tree));
				}
				}
			}
		return t;
	}
	
	public Tree recursiveTreeForOrg( Tree t,List<Tree> tree) {
		if(null==t.getChildren()){
		t.setChildren(new ArrayList<Tree>());
		}
			for (Tree child : tree) {
				if(child.getFathercode()!=null){
					if(child.getFathercode().equals(t.getCode())){
						List<Tree> list = new ArrayList<Tree>();
						for(TomDept tomDept:ContextInitRedisService.deptList){
							Tree t1 = new Tree();
							if(child.getCode().equals(tomDept.getPkOrg())){
								t1.setCode(tomDept.getCode());
								t1.setName(tomDept.getName());
								if(null!=tomDept.getTopcode()){
									t1.setFathercode(tomDept.getTopcode());
								}
								t1.setStatuss("3");
								list.add(t1);
							}
						}
					//	List<Tree> selectByPkOrg = deptServise.selectByPkOrg(child.getCode());
				
						if(null==child.getChildren()){
						child.setChildren(new ArrayList<Tree>());
						}
						if (!list.isEmpty()) {
							for (Tree tom : list) {

								if (tom.getFathercode() == null) {
									Tree t1 = new Tree();
									for(TomDept tomDept:ContextInitRedisService.deptList){
										if(tomDept.getCode().equals(tom.getCode())){
											t1.setCode(tomDept.getCode());
											
											t1.setName(tomDept.getName());
											t1.setStatuss("3");
										}
									}
									//deptServise.selectByPkDept(tom.getCode());
									Tree tomDept =recursiveTree(t1, list);
									child.getChildren().add(tomDept);
								}
							}

						}
						
						t.getChildren().add(recursiveTreeForOrg(child,tree));
						//t.getChildren().add(child);
				}
				}
			}
		return t;
	}
}
