/**
 * 
 */
package com.chinamobo.ue.common.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本: [1.0]
 * 功能说明: 抽象Node节点;
 * 作者: WChao 创建时间: 2017年6月16日 下午4:10:38
 */
public class Node<T> {
	
	protected String id;//节点ID;
	protected String name;//名称;
	protected List<T> childrens;//子节点集合;
	
	public Node(){
		this.childrens = new ArrayList<T>();
	};
	//构造器赋名称、ID;
	public Node(String id, String name){
		this.id = id;
		this.name = name;
		this.childrens = new ArrayList<T>();
	}
	
	public void addNode(T node)throws Exception{
		this.childrens.add(node);
	}
	//显示节点:叶子与非叶子节点均有此方法;
	public void  display(){
		System.out.println(this.id+":"+this.name);
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public List<T> getChildrens(){
		return this.childrens;
	}
}
