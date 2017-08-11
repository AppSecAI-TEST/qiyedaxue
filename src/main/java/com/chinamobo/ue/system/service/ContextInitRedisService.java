package com.chinamobo.ue.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomEmpOne;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;


@Component
public class ContextInitRedisService {
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private AbstractCacheManager redisCacheManager;//Redis缓存管理器;
	
	public static List<TomDept> deptList = new ArrayList<TomDept>();
	public static List<Tree> deptHaveEmp = new ArrayList<Tree>();
	public static List<TomEmpOne> empList = new ArrayList<TomEmpOne>();
	public void init() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomDept> select = tomDeptMapper.select();
		deptList.clear();
		deptList.addAll(select);
		List<TomEmpOne> selectAll = tomEmpMapper.selectAll();
		empList.clear();
		empList.addAll(selectAll);
		List<Tree> selectHaveEmp = tomDeptMapper.selectHaveEmp();
		deptHaveEmp.clear();
		deptHaveEmp.addAll(selectHaveEmp);
		
		if("true".equals(Config.getProperty("redisOn"))){
			System.out.println("开启缓存Redis");
			try {
				redisCacheManager.init();//缓存各个模块信息;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
