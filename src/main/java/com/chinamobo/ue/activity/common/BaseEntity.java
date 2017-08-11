package com.chinamobo.ue.activity.common;

import java.io.Serializable;
import java.util.Map;

import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.google.common.collect.Maps;

/**
 * 
 * 版本: [1.0]
 * 功能说明: Entity支持类
 *
 * 作者: WangLg
 * 创建时间: 2016年3月3日 下午3:44:35
 * @param <T>
 */
public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 当前用户
	 */
	protected String user; 
	
//	/**
//	 * 当前实体分页对象
//	 */
//	protected PageData<T> page;
	
	
//	/**
//	 * 自定义SQL（SQL标识，SQL内容）
//	 */
//	protected Map<String, String> sqlMap;

	public String getCurrentUser() {
		if(user == null){
			//user  = ShiroUtils.getCurrentUser().getName();
			user  = "4";
		}
		return user;
	}
	
	public void setCurrentUser(String user) {
		this.user = user;
	}

	
//	public PageData<T> getPage() {
//		if (page == null){
//			page = new PageData<T>();
//		}
//		return page;
//	}
//	
//	public PageData<T> setPage(PageData<T> page) {
//		this.page = page;
//		return page;
//	}
//
//	public Map<String, String> getSqlMap() {
//		if (sqlMap == null){
//			sqlMap = Maps.newHashMap();
//		}
//		return sqlMap;
//	}
//
//	public void setSqlMap(Map<String, String> sqlMap) {
//		this.sqlMap = sqlMap;
//	}

}
