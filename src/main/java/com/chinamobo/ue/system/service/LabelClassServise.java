package com.chinamobo.ue.system.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.system.dao.TomLabelClassMapper;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelEmpRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelMapper;
import com.chinamobo.ue.system.entity.TomLabelClass;
import com.chinamobo.ue.system.entity.TomLabelClassRelation;

@Service
public class LabelClassServise {
	@Autowired
	private TomLabelClassMapper tomLabelClassMapper;
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	@Autowired
	private TomLabelMapper tomLabelMapper;
	@Autowired
	private TomLabelEmpRelationMapper tomLabelEmpRelationMapper;
	
	//根据分类id查询
	@Transactional
	public TomLabelClass selectClass(Integer classId){
		TomLabelClass labelClass = tomLabelClassMapper.selectByPrimaryKey(classId);
		//根据分类id查询标签分类关联表
		List<TomLabelClassRelation> list = tomLabelClassRelationMapper.selectByLabelClassRelation(classId);
		if(list.size()>0){
			String tagIds = "";
			for(TomLabelClassRelation labelClassRelation : list){
				tagIds += labelClassRelation.getTagId();
				tagIds+=",";
			}
			tagIds = tagIds.substring(0,tagIds.length()-1);
			labelClass.setTagIds(tagIds);
		}
		return labelClass;
	}
	
	//全查标签分类忽略微信分类
	@Transactional
	public List<TomLabelClass> selectLabelClass(){
		return tomLabelClassMapper.selectLabelClassList();
	}
	
	//添加标签分类
	@Transactional
	public void addLabelClass(TomLabelClass labelClass){
		labelClass.setCreateTime(new Date());
		labelClass.setUpdateTime(new Date());
		tomLabelClassMapper.insertSelective(labelClass);
		if(labelClass.getTagIds()!=null){
			String[] tagIds=labelClass.getTagIds().substring(1, labelClass.getTagIds().length()).split(",");
			for(int i=0;i<tagIds.length;i++){
				TomLabelClassRelation labelClassRelation = new TomLabelClassRelation();
				labelClassRelation.setClassId(tomLabelClassMapper.classIdMax());
				labelClassRelation.setClassName(labelClass.getClassName());
				labelClassRelation.setTagId(tagIds[i]);
				labelClassRelation.setTagName(tomLabelMapper.selectByPrimaryKey(labelClassRelation.getTagId()).getTagName());
				labelClassRelation.setCreateTime(new Date());
				labelClassRelation.setUpdateTime(new Date());
				tomLabelClassRelationMapper.insertSelective(labelClassRelation);
			}
		}
	}
	
	//编辑标签分类
	@Transactional
	public void editLabelClass(TomLabelClass labelClass){
		labelClass.setUpdateTime(new Date());
		tomLabelClassMapper.updateByPrimaryKeySelective(labelClass);
		//删除原标签分类关联表
		tomLabelClassRelationMapper.deleteByPrimary(labelClass.getClassId());
		if(!labelClass.getTagIds().equals("")){
			String labelId = labelClass.getTagIds();
			if(labelId.charAt(0)==','){
				labelId = labelClass.getTagIds().substring(1, labelClass.getTagIds().length());
			}
			String[] tagIds=labelId.split(",");
			for(int i=0;i<tagIds.length;i++){
				TomLabelClassRelation labelClassRelation = new TomLabelClassRelation();
				labelClassRelation.setClassId(labelClass.getClassId());
				labelClassRelation.setClassName(labelClass.getClassName());
				labelClassRelation.setTagId(tagIds[i]);
				labelClassRelation.setTagName(tomLabelMapper.selectByPrimaryKey(tagIds[i]).getTagName());
				labelClassRelation.setCreateTime(new Date());
				labelClassRelation.setUpdateTime(new Date());
				tomLabelClassRelationMapper.insertSelective(labelClassRelation);
			}
		}
	}
	
	//删除标签分类
	@Transactional
	public void deleteLabelClass(Integer classId){
		//删除标签分类表
		tomLabelClassMapper.deleteByPrimaryKey(classId);
		//查询标签分类关联表
		List<TomLabelClassRelation> list = tomLabelClassRelationMapper.selectByLabelClassRelation(classId);
		for(TomLabelClassRelation labelClassRelation : list){
			//根据标签ID查询标签分类关联数量
			int count = tomLabelClassRelationMapper.countBytagId(labelClassRelation.getTagId());
			if(count<2){
				//删除标签
				tomLabelMapper.deleteByPrimaryKey(labelClassRelation.getTagId());
				//删除标签人员关联表
				tomLabelEmpRelationMapper.deleteByPrimary(labelClassRelation.getTagId());
			}
		}
		//删除标签分类关联表
		tomLabelClassRelationMapper.deleteByPrimary(classId);
	}
}