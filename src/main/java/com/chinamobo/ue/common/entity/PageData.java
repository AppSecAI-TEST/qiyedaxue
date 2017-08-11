package com.chinamobo.ue.common.entity;

import java.util.List;

public class PageData<T> {
	
	private Integer pageNum; 
	private Integer pageSize; 
	private Integer pageCount; 
	private Integer count; 
	private List<T> data;
	
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
		if(pageSize!=0){
			if(count%pageSize==0){
				return count/pageSize;
			}else{
				return count/pageSize+1;
			}
		}else{
			return 1;
		}
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
		return data;
	}
	public void setDate(List<T> data) {
		this.data = data;
	} 
	
}
