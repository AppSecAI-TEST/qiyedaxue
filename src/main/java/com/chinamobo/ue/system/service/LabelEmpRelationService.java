package com.chinamobo.ue.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelEmpRelationMapper;
import com.chinamobo.ue.system.entity.TomLabelClassRelation;
import com.chinamobo.ue.system.entity.TomLabelEmpRelation;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class LabelEmpRelationService {
	@Autowired
	private TomLabelEmpRelationMapper tomLabelEmpRelationMapper;
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	
	//分页查询标签人员
	public PageData<TomLabelEmpRelation> selectLabelEmpList(int pageNum, int pageSize, String code, String name, String sex, String begindate, String cityname, String statuss, String id){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomLabelEmpRelation> page = new PageData<TomLabelEmpRelation>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("code", code);
		map.put("name", name);
		map.put("sex", sex);
		map.put("begindate", begindate);
		map.put("cityname", cityname);
		if(statuss!=null){
			if(statuss.equals("1")){
				map.put("classId", Integer.valueOf(id));
				map.put("type", "WX");
			}else if(statuss.equals("2")){
				map.put("classId", Integer.valueOf(id));
			}else if(statuss.equals("4")){
				map.put("tagId", id);
				map.put("type", "WX");
			}else if(statuss.equals("3")){
				map.put("tagId", id);
			}else if(statuss.equals("5")){
				map.put("tagId", id);
			}
		}
		int count = tomLabelEmpRelationMapper.count(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomLabelEmpRelation> list = tomLabelEmpRelationMapper.selectLabelEmpList(map);
		for(TomLabelEmpRelation labelEmpRelation : list){
			//查询标签分类关联表
			TomLabelClassRelation labelClassRelation = tomLabelClassRelationMapper.selectByPrimary(labelEmpRelation.getTagId());
			if(null!=labelClassRelation){
				labelEmpRelation.setClassId(labelClassRelation.getClassId());
			}
			labelEmpRelation.setType("L");
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
}
