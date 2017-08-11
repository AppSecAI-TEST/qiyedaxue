package com.chinamobo.ue.activity.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * 版本: [1.0]
 * 功能说明: 分页类
 *
 * 作者: WangLg
 * 创建时间: 2016年3月11日 下午4:47:21
 * @param <T>
 */
public class PageData<T> {
 
	private Integer pageNum; //当前页码
	//private Integer pageSize = Integer.valueOf(Global.getConfig("page.pageSize"));//// 页面大小，设置为“-1”表示不进行分页（分页无效）
	private Integer pageSize;  //分页  Integer.valueOf(Global.getConfig("page.pageSize")); 
	private Integer pageCount; //总页数
	private Integer count; //总条数
	private List<T> list;
	
	
	/**
	 * 获取本页数据对象列表
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置本页数据对象列表
	 * @param list
	 */
	public PageData<T> setList(List<T> list) {
		this.list = list;
		return this;
	}

	
	public PageData(){
	}
	
	public PageData(HttpServletRequest request, HttpServletResponse response){
		this(request, response, -2);
	}

	public PageData(HttpServletRequest request, HttpServletResponse response, int defaultPageSize){
		String num = request.getParameter("pageNum");
		String size = request.getParameter("pageSize");
		
		//TODO 默认查询参数
		if(StringUtils.isNotBlank(num)){
			//cookie获取
			this.setPageNum(Integer.parseInt(num));
		}else{
			this.setPageNum(Integer.parseInt(num));
		}
		
		if(StringUtils.isNotBlank(size)){
			//cookie获取
			this.setPageNum(Integer.parseInt(size));
		}else{
			this.setPageNum(Integer.parseInt(size));
		}
	}
	
	public PageData(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}
	
	public PageData(Integer pageNum, Integer pageSize,Integer count) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.count = count;
	}
	
	public PageData(Integer pageNum, Integer pageSize, Integer pageCount,List<T> list) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.list = list;
	}
	
	public PageData(Integer pageNum, Integer pageSize, Integer pageCount, Integer count) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.count = count;
	}

	public PageData(Integer pageNum, Integer pageSize, Integer pageCount, Integer count, List<T> list) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.count = count;
		this.list = list;
	}

	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageCount() {
		return count/pageSize+1;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<T> getData() {
		return list;
	}
	public void setDate(List<T> data) {
		this.list = data;
	} 
}
