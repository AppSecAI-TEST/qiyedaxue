package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomEmp;

public interface TomDeptMapper {
	int deleteByPrimaryKey(String pkDept);

	int insert(TomDept record);

	int insertSelective(TomDept record);
	
//	int insertSelectives(TomDept tomDept,String orgcode,String topcode);
	int insertSelectives(TomDept tomDept);
//	int insertSelectives(Map<Object,Object> map);

	TomDept selectByPrimaryKey(String pkDept);

	int updateByPrimaryKeySelective(TomDept record);

	void updateByPrimaryKey(TomDept record);
//根据公司CODE查询
	List<Tree> selectByPkOrg(String pkorg);
	
	List<TomDept> select();
	
	List<Tree> selectByPkFather(String pkfather);
	
	Tree selectByPkDept(String pkdept);
	
	int insertList(List<TomDept> list);
	
	Tree selectByTree(String pkDept);
	
	List<Tree> selectByAllTree();
	
	int countByList(Map<Object,Object> map);
	List<TomDept> selectListByParam(Map<Object,Object> map);

	 void deleteAll();

	int countByPkDept(String code);
	
	List<Tree> selectAllForTree();
//增量时使用
	TomDept selectByPrimaryKey2(String pkDept);
	
	List<Tree> selectHaveEmp();
	
	TomDept selectPkDept();
	
	int MaxPkDept();
}