package com.chinamobo.ue.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomLabelClassMapper;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelEmpRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelMapper;
import com.chinamobo.ue.system.dto.LabelDto;
import com.chinamobo.ue.system.dto.LabelEmpDto;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomLabel;
import com.chinamobo.ue.system.entity.TomLabelClass;
import com.chinamobo.ue.system.entity.TomLabelClassRelation;
import com.chinamobo.ue.system.entity.TomLabelEmpRelation;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.WeChatUtil;

@Service
public class LabelServise {
	@Autowired
	private TomLabelMapper tomLabelMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomLabelEmpRelationMapper tomLabelEmpRelationMapper;
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	@Autowired
	private TomLabelClassMapper tomLabelClassMapper;
	ShiroUser user=ShiroUtils.getCurrentUser();
	
	//根据标签id查询
	@Transactional
	public TomLabel selectLabel(String tagId){
		TomLabel label = tomLabelMapper.selectByPrimaryKey(tagId);
		//根据标签id查询标签分类关联表
		TomLabelClassRelation labelClassRelation = tomLabelClassRelationMapper.selectByPrimary(tagId);
		if(labelClassRelation!=null){
			label.setClassId(labelClassRelation.getClassId());
		}
		//根据标签id查询标签人员关联表
		List<TomLabelEmpRelation> labelEmpList = tomLabelEmpRelationMapper.selectBytagIdList(tagId);
		if(labelEmpList.size()>0){
			List<String> empIds = new ArrayList<String>();
			for(TomLabelEmpRelation labelEmpRelation : labelEmpList){
				empIds.add(labelEmpRelation.getCode());
			}
			label.setEmpIds(empIds);
		}
		if(labelEmpList.size()>0){
			List<Map<String,String>> lists = new ArrayList<Map<String,String>>();
//			List <String > list = new ArrayList<String>();
			for(TomLabelEmpRelation labelEmpRelation : labelEmpList){
				Map<String,String> mapString = new HashMap<String,String>();
				mapString.put("code", labelEmpRelation.getCode());
				mapString.put("name", labelEmpRelation.getName());
				lists.add(mapString);
//				list.add(labelEmpRelation.getCode());
			}
//			label.setLabelEmps(list);
			label.setEmps(lists);
		}
		return label;
	}
	
	//同步标签
	@Transactional
	public void syncTag(){
		String access_token = WeChatUtil.getToken();
		//删除原标签
		tomLabelMapper.deleteWX();
		//删除原标签分类关联表
		tomLabelClassRelationMapper.deleteWxLabelClassRelation();
		//删除原标签人员关联表
		tomLabelEmpRelationMapper.deleteWxLabelEmpRelation();
		//获取标签
		List<LabelDto> labelList = WeChatUtil.getLabel(access_token);
		if(labelList.size()>0){
			for(LabelDto labelDto:labelList){
				//存储标签表
				TomLabel label = new TomLabel();
				label.setTagId("WX"+labelDto.getTagid());
				label.setTagName(labelDto.getTagname());
				label.setCreatorId(user.getCode());
				label.setCreator(user.getName());
				label.setOperator(user.getName());
				label.setCreateTime(new Date());
				label.setUpdateTime(new Date());
				label.setType("WX");
				tomLabelMapper.insertSelective(label);
				//存储标签分类关联表
				TomLabelClassRelation labelClassRelation = new TomLabelClassRelation();
				labelClassRelation.setClassId(1);
				labelClassRelation.setClassName("微信标签");
				labelClassRelation.setTagId("WX"+labelDto.getTagid());
				labelClassRelation.setTagName(labelDto.getTagname());
				labelClassRelation.setCreateTime(new Date());
				labelClassRelation.setUpdateTime(new Date());
				tomLabelClassRelationMapper.insertSelective(labelClassRelation);
				//获取标签人员
				List<LabelEmpDto> labelEmpList = WeChatUtil.getLabelEmp(access_token, labelDto.getTagid());
				if(labelEmpList.size()>0){
					for(LabelEmpDto labelEmpDto:labelEmpList){
						//存储标签人员关联表
						TomLabelEmpRelation labelEmpRelation = new TomLabelEmpRelation();
						labelEmpRelation.setTagId("WX"+labelDto.getTagid());
						labelEmpRelation.setTagName(labelDto.getTagname());
						labelEmpRelation.setCode(labelEmpDto.getUserid());
						labelEmpRelation.setName(labelEmpDto.getName());
						//查询人员信息
						TomEmp emp = tomEmpMapper.selectByPrimaryKey(labelEmpDto.getUserid());
						labelEmpRelation.setMobile(emp.getMobile());
						labelEmpRelation.setDeptname(emp.getDeptname());
						labelEmpRelation.setType("WX");
						labelEmpRelation.setCreateTime(new Date());
						labelEmpRelation.setUpdateTime(new Date());
						tomLabelEmpRelationMapper.insertSelective(labelEmpRelation);
					}
				}
			}
		}
	}
	
	//添加标签
	@Transactional
	public void addLabel(TomLabel label){
		label.setCreateTime(new Date());
		label.setUpdateTime(new Date());
		//查询最大标签id排除微信标签
		int maxTagId = tomLabelMapper.maxTagId();
		label.setTagId(String.valueOf(Integer.valueOf(maxTagId)+1));
		tomLabelMapper.insertSelective(label);
		//插入标签分类关联表
		TomLabelClassRelation labelClassRelation = new TomLabelClassRelation();
		if(null!=label.getClassId()){
			labelClassRelation.setClassId(label.getClassId());
			//查询标签分类
			TomLabelClass labelClass = tomLabelClassMapper.selectByPrimaryKey(label.getClassId());
			labelClassRelation.setClassName(labelClass.getClassName());
			labelClassRelation.setTagId(String.valueOf(label.getTagId()));
			labelClassRelation.setTagName(label.getTagName());
			labelClassRelation.setCreateTime(new Date());
			labelClassRelation.setUpdateTime(new Date());
			tomLabelClassRelationMapper.insertSelective(labelClassRelation);
		}
		//插入标签人员关联表
		List<String> empIds = label.getEmpIds();
		if(empIds.size()>0){
			for(String empId : empIds){
				TomLabelEmpRelation labelEmpRelation = new TomLabelEmpRelation();
				labelEmpRelation.setTagId(String.valueOf(label.getTagId()));
				labelEmpRelation.setTagName(label.getTagName());
				labelEmpRelation.setCode(empId);
				//查询员工信息
				TomEmp emp = tomEmpMapper.selectByPrimaryKey(empId);
				labelEmpRelation.setName(emp.getName());
				labelEmpRelation.setMobile(emp.getMobile());
				labelEmpRelation.setCityname(emp.getCityname());
				labelEmpRelation.setSecretEmail(emp.getSecretEmail());
				labelEmpRelation.setOrgcode(emp.getOrgcode());
				labelEmpRelation.setOrgname(emp.getOrgname());
				labelEmpRelation.setDeptcode(emp.getDeptcode());
				labelEmpRelation.setDeptname(emp.getDeptname());
				labelEmpRelation.setOnedeptcode(emp.getOnedeptcode());
				labelEmpRelation.setOnedeptname(emp.getOnedeptname());
				labelEmpRelation.setPoststat(emp.getPoststat());
				labelEmpRelation.setSex(emp.getSex());
				labelEmpRelation.setCreateTime(new Date());
				labelEmpRelation.setUpdateTime(new Date());
				tomLabelEmpRelationMapper.insertSelective(labelEmpRelation);
			}
		}
	}
	
	//修改标签
	@Transactional
	public void updateLabel(TomLabel label){
		label.setUpdateTime(new Date());
		tomLabelMapper.updateByPrimaryKeySelective(label);
		//删除标签分类关联表
		tomLabelClassRelationMapper.deleteByTagId(label.getTagId());
		//删除原标签人员
		tomLabelEmpRelationMapper.deleteByPrimary(label.getTagId());
		if(label.getClassId()!=null){
			TomLabelClassRelation labelClassRelation = new TomLabelClassRelation();
			labelClassRelation.setClassId(label.getClassId());
			//查询标签分类
			TomLabelClass labelClass =  tomLabelClassMapper.selectByPrimaryKey(label.getClassId());
			labelClassRelation.setClassName(labelClass.getClassName());
			labelClassRelation.setTagId(label.getTagId());
			labelClassRelation.setTagName(label.getTagName());
			labelClassRelation.setCreateTime(new Date());
			labelClassRelation.setUpdateTime(new Date());
			tomLabelClassRelationMapper.insertSelective(labelClassRelation);
		}
		//插入标签人员关联表
		List<String> empIds = label.getEmpIds();
		if(empIds.size()>0){
			for(String empId : empIds){
				TomLabelEmpRelation labelEmpRelation = new TomLabelEmpRelation();
				labelEmpRelation.setTagId(String.valueOf(label.getTagId()));
				labelEmpRelation.setTagName(label.getTagName());
				labelEmpRelation.setCode(empId);
				//查询员工信息
				TomEmp emp = tomEmpMapper.selectByPrimaryKey(empId);
				labelEmpRelation.setName(emp.getName());
				labelEmpRelation.setMobile(emp.getMobile());
				labelEmpRelation.setCityname(emp.getCityname());
				labelEmpRelation.setSecretEmail(emp.getSecretEmail());
				labelEmpRelation.setOrgcode(emp.getOrgcode());
				labelEmpRelation.setOrgname(emp.getOrgname());
				labelEmpRelation.setDeptcode(emp.getDeptcode());
				labelEmpRelation.setDeptname(emp.getDeptname());
				labelEmpRelation.setOnedeptcode(emp.getOnedeptcode());
				labelEmpRelation.setOnedeptname(emp.getOnedeptname());
				labelEmpRelation.setPoststat(emp.getPoststat());
				labelEmpRelation.setSex(emp.getSex());
				labelEmpRelation.setCreateTime(new Date());
				labelEmpRelation.setUpdateTime(new Date());
				tomLabelEmpRelationMapper.insertSelective(labelEmpRelation);
			}
		}
	}
	
	//删除标签
	@Transactional
	public void deleteLabel(String tagId){
		//删除标签表
		tomLabelMapper.deleteByPrimaryKey(tagId);
		//删除标签人员关联表
		tomLabelEmpRelationMapper.deleteByPrimary(tagId);
		//删除标签分类关联表
		tomLabelClassRelationMapper.deleteByTagId(tagId);
	}
	
}
