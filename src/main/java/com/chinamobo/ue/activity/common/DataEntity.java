package com.chinamobo.ue.activity.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Global;
import com.chinamobo.ue.utils.MapManager;

/**
 * 版本: [1.0]
 * 功能说明: 数据Entity类--所有实体类继承此抽象类
 *
 * 作者: WangLg
 * 创建时间: 2016年3月3日 下午4:11:35
 * @param <T>
 */
public abstract class DataEntity<T> extends BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String creator;
    
    private Date createTime;

    private String operator;

    private Date updateTime;

	public DataEntity() {
		super();
	}

	/**
	 * 插入前调用
	 */
	public void preInsert(){
		this.operator = user;
		this.creator = user;
		this.updateTime = new Date();
		this.createTime = new Date();
	}
	
	/**
	 * 插入后调用
	 */
	public void preUpdate(){
		this.operator = user;
		this.updateTime = new Date();
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
