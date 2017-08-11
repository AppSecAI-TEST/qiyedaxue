package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.entity.TomOrg;

public interface TomOrgMapper {
	int deleteByPrimaryKey(String code);

	int insert(TomOrg record);

	//int insertOrg(TomOrg record);

	TomOrg selectByPrimaryKey(String code);

	int updateByPrimaryKeySelective(TomOrg record);

	int updateByPrimaryKey(TomOrg record);

	// 批量插入
	int insertList(List list);

	// 根据集团code查询
	List<Tree> selectByGrp_Code(String grp_code);

	// 查询所有公司
	List<TomOrg> select();

	TomOrg selectById(String id);

	void insertOrg(TomOrg tomOrg);

	Tree selectByTree(String code);

	Tree selectOrgByDeptCode(String code);

	List<Tree> selectByAllTree();

	void deleteAll();
	//查询所有公司树
	List<Tree> selectByAllOrgTree();
	
	TomOrg selectById2(String id);
	
	TomOrg selectPkOrg();
}