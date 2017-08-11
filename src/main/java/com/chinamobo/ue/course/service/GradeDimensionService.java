package com.chinamobo.ue.course.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomGradeDimensionMapper;
import com.chinamobo.ue.course.dto.TomGradeDimensionDto;
import com.chinamobo.ue.course.entity.TomGradeDimension;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.MapManager;
import com.google.common.collect.Lists;

/**
 * 版本: [1.0]
 * 功能说明: 评分管理
 *
 * 作者: WangLg
 * 创建时间: 2016年3月2日 上午11:17:40
 */
@Service
public class GradeDimensionService {

	@Autowired
	private TomGradeDimensionMapper gradeDimensionMapper;//评分服务
	
	@Autowired
	private NumberRecordService numberRecordService;
	
	
	/**
	 * 功能描述：[删除讲师评分/课程评分]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月15日 下午8:41:20
	 * @param gradeDimension
	 */
	@Transactional
	public void deleteGradeDimension(int gradeDimensionId){
		gradeDimensionMapper.delete(gradeDimensionId);
	}
	
	/**
	 * 功能描述：[修改讲师评分/课程评分]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月2日 下午1:32:42
	 * @param tomGradeDimension
	 */
	@Transactional
	public void updateGradeDimension(TomGradeDimension gradeDimension){
		gradeDimension.setUpdateTime(new Date());
		gradeDimensionMapper.updateByPrimaryKeySelective(gradeDimension);
	}
	
	/**
	 * 功能描述：[添加讲师评分/课程评分]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月2日 下午1:32:55
	 * @param tomGradeDimension
	 */
	@Transactional
	public void saveGradeDimension(TomGradeDimension tomGradeDimension){
		tomGradeDimension.setGradeDimensionType(tomGradeDimension.getGradeDimensionType());
		tomGradeDimension.setCreateTime(new Date());
		tomGradeDimension.setUpdateTime(new Date());
		tomGradeDimension.setStatus("Y");
		if("C".equals(tomGradeDimension.getGradeDimensionType())){
			tomGradeDimension.setGradeDimensionNumber(numberRecordService.getNumber(MapManager.numberType("KCPF")));
		}else{
			tomGradeDimension.setGradeDimensionNumber(numberRecordService.getNumber(MapManager.numberType("JSPF")));
		}
		gradeDimensionMapper.insertSelective(tomGradeDimension);
	}

	/**
	 * 功能描述：[查询讲师评分/课程评分]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月2日 下午1:33:11
	 * @param gradeDimension
	 * @return
	 */
	public PageData<TomGradeDimension> findGradeDimenComList(int pageNum ,int pageSize,Map<Object, Object> map) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomGradeDimension> page = new PageData<TomGradeDimension>();
		int count=gradeDimensionMapper.countByList(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		List<TomGradeDimension> list = gradeDimensionMapper.selectListByParam(map);
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;
	}
	
	public List<TomGradeDimensionDto> findGradeDimenModel(Map<Object, Object> map) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomGradeDimensionDto dto =new TomGradeDimensionDto();
		List<TomGradeDimensionDto> list = Lists.newArrayList();
		List<TomGradeDimension> dimensList = gradeDimensionMapper.findGradeDimenModel(map);
		for(TomGradeDimension dimens :dimensList){
			dto.setGdimenId(dimens.getGradeDimensionId());
			dto.setGdimenName(dimens.getGradeDimensionName());
			list.add(dto);
		}
		return list;
	}
	
	
	public TomGradeDimensionDto findDetails(Map<Object,Object> map) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomGradeDimensionDto dto = gradeDimensionMapper.findDetails(map);
		return dto;
	}
}
