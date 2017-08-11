package com.chinamobo.ue.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.system.dao.TomGrpOrgRelationMapper;
import com.chinamobo.ue.system.entity.TomGrpOrgRelation;
import com.chinamobo.ue.ums.DBContextHolder;

//根据集团code查询组织集团关联表
@Service
public class GrpOrgRelationServise {
	
	
		@Autowired
		private TomGrpOrgRelationMapper tomGrpOrgRelationMapper;
		
		public PageData<TomGrpOrgRelation> selectByCode(int pageNum ,int pageSize,String grpCode){
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			TomGrpOrgRelation example=new TomGrpOrgRelation();
			PageData<TomGrpOrgRelation> page=new PageData<TomGrpOrgRelation>();				
			example.setGrpCode(grpCode);
			int count=tomGrpOrgRelationMapper.countByExample(example);

			if(pageSize==-1){
				pageSize=count;
			}
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("startNum", (pageNum-1)*pageSize);
			map.put("endNum", pageSize);//pageNum * 
			map.put("example", example);
			List<TomGrpOrgRelation> list = tomGrpOrgRelationMapper.selectByCode(map);
			
			page.setDate(list);
			page.setPageNum(pageNum);
			page.setPageSize(pageSize);
			page.setCount(count);
			return page;

		}
		
		//添加
		public void insertGrpOrg(TomGrpOrgRelation record){
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			tomGrpOrgRelationMapper.insertSelective(record);
		}
}
