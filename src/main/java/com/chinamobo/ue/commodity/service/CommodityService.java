package com.chinamobo.ue.commodity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.commodity.dao.TomCommodityMapper;
import com.chinamobo.ue.commodity.entity.TomCommodity;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class CommodityService {

	@Autowired
	private TomCommodityMapper commodityMapper;
	
	/**
	 * 
	 * 功能描述：[添加商品]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月18日 下午2:13:39
	 * @param commodity
	 */
	@Transactional
	public void addCommodity(TomCommodity commodity) {
		commodityMapper.insertSelective(commodity);
	}

	/**
	 * 
	 * 功能描述：[修改商品]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月18日 下午2:29:50
	 * @param commodity
	 */
	@Transactional
	public void editCommodity(TomCommodity commodity) {
		commodityMapper.updateByPrimaryKeySelective(commodity);
	}

	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月19日 上午11:18:35
	 * @param pageNum
	 * @param pageSize
	 * @param commodityName
	 * @return
	 */
	public PageData<TomCommodity> findList(int pageNum, int pageSize, String commodityName) {
//		if(commodityName!=null){
//			commodityName=commodityName.replaceAll("/", "//");
//			commodityName=commodityName.replaceAll("%", "/%");
//			commodityName=commodityName.replaceAll("_", "/_");
//		}
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCommodity> page=new PageData<TomCommodity>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("commodityName", commodityName);
		
		int count =commodityMapper.countAll(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		
		List<TomCommodity> list=commodityMapper.selectByPage(map);
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}

	/**
	 * 
	 * 功能描述：[商品上下架]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年5月19日 下午12:33:35
	 * @param commodityId
	 */
	@Transactional
	public void updateStatus(int commodityId) {
		TomCommodity commodity = commodityMapper.selectByPrimaryKey(commodityId);
		if(commodity.getCommodityGrounding().equals("1")){
			commodity.setCommodityGrounding("2");
		}else{
			commodity.setCommodityGrounding("1");
		}
		commodityMapper.updateByPrimaryKeySelective(commodity);
	}

	/**
	 * 
	 * 功能描述：[查询详情]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月6日 下午2:27:03
	 * @param commodityId
	 * @return
	 */
	public TomCommodity findOne(int commodityId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return commodityMapper.selectByPrimaryKey(commodityId);
	}


}
