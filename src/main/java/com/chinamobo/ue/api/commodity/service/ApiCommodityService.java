package com.chinamobo.ue.api.commodity.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.api.commodity.dto.CommodityDetatilResult;
import com.chinamobo.ue.api.commodity.dto.CommodityExchangeResult;
import com.chinamobo.ue.api.commodity.dto.CommodityResult;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.commodity.dao.TomCommodityExchangeMapper;
import com.chinamobo.ue.commodity.dao.TomCommodityMapper;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomCommodity;
import com.chinamobo.ue.commodity.entity.TomCommodityExchange;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.exam.service.ExamScoreService;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;

import net.sf.json.JSONObject;

@Service
public class ApiCommodityService {
	private static Logger logger = LoggerFactory.getLogger(ApiCommodityService.class);
	@Autowired
	private TomCommodityExchangeMapper tomCommodityExchangeMapper;
	@Autowired
	private TomCommodityMapper tomCommodityMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomUserInfoMapper tomUserInfoMapper;
	@Autowired
	private TomEbRecordMapper tomEbRecordMapper;
	@Autowired
	private ExamScoreService examScoreService;
	/**
	 * 
	 * 功能描述：[6.6.兑换记录列表]
	 *
	 * 创建者：cjx 创建时间: 2016年5月18日 下午3:11:07
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleExchangeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<CommodityExchangeResult> page = new PageData<CommodityExchangeResult>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		String keyWord = request.getParameter("keyWord");
//		if (keyWord != null) {
//			keyWord = keyWord.replaceAll("/", "//");
//			keyWord = keyWord.replaceAll("%", "/%");
//			keyWord = keyWord.replaceAll("_", "/_");
//
//		}
		String userId = request.getParameter("userId");
		map.put("userId", userId);
		map.put("keyWord", keyWord);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));//(int) map.get("startNum") + 
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		int count = tomCommodityExchangeMapper.countByMap(map);
		List<CommodityExchangeResult> list = tomCommodityExchangeMapper.selectByCode(map);
		for (CommodityExchangeResult commodityExchangeResult : list) {
			if (null != commodityExchangeResult) {
				if ("1".equals(commodityExchangeResult.getPostMethod())) {
					commodityExchangeResult.setPostMethod("邮寄");
					if ("Y".equals(commodityExchangeResult.getExchange_state())) {
						commodityExchangeResult.setExchange_state("已邮寄");
					} else {
						commodityExchangeResult.setExchange_state("未邮寄");
					}
				} else {
					commodityExchangeResult.setPostMethod("自提");
					if ("Y".equals(commodityExchangeResult.getExchange_state())) {
						commodityExchangeResult.setExchange_state("已领取");
					} else {
						commodityExchangeResult.setExchange_state("待领取");
					}
				}
				
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：[6.7.兑换详情]
	 *
	 * 创建者：cjx 创建时间: 2016年5月19日 上午9:37:18
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleExchangeDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String userId = request.getParameter("userId");
		String commodityId = request.getParameter("commodityId");
		String exchangeTime = request.getParameter("exchangeTime");
		map.put("userId", userId);
		map.put("commodityId", Integer.parseInt(commodityId));
		map.put("exchangeTime", simple.parse(exchangeTime));
		List<CommodityExchangeResult> list = tomCommodityExchangeMapper.selectExchangeDeails(map);
		for (CommodityExchangeResult commodityExchangeResult : list) {
			if (null != commodityExchangeResult) {
				if ("1".equals(commodityExchangeResult.getPostMethod())) {
					commodityExchangeResult.setPostMethod("邮寄");
				} else {
					commodityExchangeResult.setPostMethod("自提");
				}
				if ("Y".equals(commodityExchangeResult.getExchange_state())) {
					commodityExchangeResult.setExchange_state("已领取");
				} else {
					commodityExchangeResult.setExchange_state("待领取");
				}
			}
		}
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(list);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：[邮寄兑换商品]
	 *
	 * 创建者：cjx 创建时间: 2016年5月19日 下午2:12:12
	 * 
	 * @param json
	 * @return
	 */
	@Transactional
	public synchronized Result eleCommodityExchange(String json) throws Exception {
		String fail ="";
		JSONObject jsonObject = JSONObject.fromObject(json);
		Date date = new Date();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TomCommodityExchange tomCommodityExchange = new TomCommodityExchange();
		TomUserInfo selectByPrimaryKey = tomUserInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
		int price = tomCommodityMapper.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("commodityId")))
				.getCommodityPrice();
		TomCommodity commodity = tomCommodityMapper
				.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("commodityId")));
	if("1".equals(commodity.getCommodityGrounding())){
		if (commodity.getCommodityNumber()> 0) {
			if (selectByPrimaryKey.geteNumber() >= price) {
				String commodityName = tomCommodityMapper
						.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("commodityId"))).getCommodityName();
				tomCommodityExchange.setCommodityName(commodityName);
				String name = tomEmpMapper.selectByPrimaryKey(jsonObject.getString("userId")).getName();
				tomCommodityExchange.setEmpName(name);
				tomCommodityExchange.setCommodityId(Integer.parseInt(jsonObject.getString("commodityId")));
				tomCommodityExchange.setEmpNumber(jsonObject.getString("userId"));
				tomCommodityExchange.setExchangeState("N");
//				if (json.indexOf("exchangeTime") != -1) {
//					tomCommodityExchange.setExchangeTime(simple.parse(jsonObject.getString("exchangeTime")));
//				} else {
					tomCommodityExchange.setExchangeTime(date);
			//	}
				tomCommodityExchange.setPostMethod("1");
				tomCommodityExchange.setExpressCompany("等待发货");
				tomCommodityExchange.setExpressNumber("等待发货");
				tomCommodityExchange.setAddress(jsonObject.getString("consigneeAddress"));
				tomCommodityExchange.setName(jsonObject.getString("consigneeName"));
				tomCommodityExchange.setSex(jsonObject.getString("consigneeGender"));
				tomCommodityExchange.setPhoneNumber(jsonObject.getString("consigneeTel"));
				tomCommodityExchange.setCommodityId(Integer.parseInt(jsonObject.getString("commodityId")));
				tomCommodityExchange.setCode(jsonObject.getString("userId"));
				tomCommodityExchangeMapper.insertSelective(tomCommodityExchange);
				//
				// int
				// i=tomCommodityExchangeMapper.insertSelective(tomCommodityExchange);
				// if(i!=-1){
				// System.err.println("兑换成功");
				// }
				TomCommodity tomCommodity = new TomCommodity();
				TomCommodity selectByPrimaryKey2 = tomCommodityMapper
						.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("commodityId")));
				tomCommodity.setCommodityId(Integer.parseInt(jsonObject.getString("commodityId")));
				tomCommodity.setCommodityNumber(selectByPrimaryKey2.getCommodityNumber() - 1);
				tomCommodityMapper.updateByPrimaryKeySelective(tomCommodity);
				TomEbRecord ebRecord = new TomEbRecord();
				ebRecord.setCode(jsonObject.getString("userId"));
				ebRecord.setExchangeNumber(-price);
				ebRecord.setRoad("10");
				ebRecord.setUpdateTime(new Date());
				tomEbRecordMapper.insertSelective(ebRecord);
				TomUserInfo tomUserInfo = new TomUserInfo();
				tomUserInfo.setCode(jsonObject.getString("userId"));
				tomUserInfo.seteNumber(selectByPrimaryKey.geteNumber() - price);
				tomUserInfoMapper.updateByCode(tomUserInfo);
				JsonMapper jsonMapper = new JsonMapper();
				String time = simple.format(date);
				Map<String,String> map = new HashMap<String,String>();
				map.put("exchangeTime", time);
				String exchangeTime = jsonMapper.toJson(map);
				return new Result("Y", exchangeTime, "0", "");

			} else {
				fail="亲，您的积分不足";
				
				return new Result("N","" , "0", fail);
			}
		} else {
			fail="亲，此商品已经没有了！";
			return new Result("N", "", "0", fail);
		}
	}else{
		fail="亲，此商品已经下架了，看看其他的商品吧！";
		return new Result("N", "", "0", fail);
	}
	}

	/**
	 * 功能描述：[获取商品列表]
	 * 
	 * 创建者：LG
	 * 
	 * 创建时间：2016.5.27 11：00
	 * 
	 */
	public Result eleCommodityList(HttpServletRequest request, HttpServletResponse respones) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		PageData<CommodityResult> page = new PageData<CommodityResult>();
		JsonMapper mapper = new JsonMapper();

		String code = request.getParameter("userId");
		
		//扫描过期考试扣除e币
		examScoreService.reSetStatus(code);
		
		logger.info("ApiCommodityService----" + code);
		map.put("code", code);
		CommodityResult res = new CommodityResult();
		res = tomUserInfoMapper.selectBycode(map);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));// (int) map.get("startNum") +
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		int count = tomCommodityMapper.countByuserId(map);
		List<CommodityResult> list = tomCommodityMapper.selectByuserId(map);
		List<CommodityResult> alist = new ArrayList<CommodityResult>();
		for (CommodityResult ls : list) {
			CommodityResult tomCommodity = new CommodityResult();
			tomCommodity.setCommodityId(ls.getCommodityId());
			tomCommodity.setCommodityName(ls.getCommodityName());
			tomCommodity.setCommodityPrice(ls.getCommodityPrice());
			tomCommodity.setCommodityNumber(ls.getCommodityNumber());
			tomCommodity.setCommodityPicture(ls.getCommodityPicture());
			tomCommodity.setArayacak(ls.getArayacak());
			tomCommodity.setPost(ls.getPost());
			alist.add(tomCommodity);
			page.setCount(count);
			page.setDate(alist);
			page.setPageNum((int) map.get("startNum") / (int) map.get("endNum") + 1);

		}
		Map<Object, Object> querymap = new HashMap<Object, Object>();
		querymap.put("userId", res.getUserId());
		querymap.put("eCoin", res.geteCoin());
		querymap.put("commodityList", page);
		String json = mapper.toJson(querymap);

		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[自提商品接口]
	 *
	 * 创建者：cjx 创建时间: 2016年5月30日 下午2:17:06
	 * 
	 * @param json
	 * @return
	 */
	@Transactional
	public synchronized Result eleCommodityArayacakCdkey(String json) throws Exception {
		try {
			String fail ="";
			Date date = new Date();
			Random rand = new Random();
			SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat simple1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date1 = simple.format(date);
			JSONObject jsonObject = JSONObject.fromObject(json);
			TomCommodityExchange tomCommodityExchange = new TomCommodityExchange();
			TomUserInfo selectByPrimaryKey = tomUserInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
			TomCommodity commodity1 = tomCommodityMapper
					.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("commodityId")));
			if("1".equals(commodity1.getCommodityGrounding())){
			if (commodity1.getCommodityNumber() >0) {
				if (selectByPrimaryKey.geteNumber() >= commodity1.getCommodityPrice()) {
					TomCommodity commodity = tomCommodityMapper
							.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("commodityId")));
					tomCommodityExchange.setCommodityName(commodity.getCommodityName());
					String name = tomEmpMapper.selectByPrimaryKey(jsonObject.getString("userId")).getName();
					tomCommodityExchange.setEmpName(name);
					tomCommodityExchange.setCommodityId(Integer.parseInt(jsonObject.getString("commodityId")));
					tomCommodityExchange.setEmpNumber(jsonObject.getString("userId"));
					tomCommodityExchange.setExchangeState("N");
//					if (json.indexOf("exchangeTime") != -1) {
//						tomCommodityExchange.setExchangeTime(simple1.parse(jsonObject.getString("exchangeTime")));
//					} else {
						tomCommodityExchange.setExchangeTime(date);
			//		}
					tomCommodityExchange.setPostMethod("2");
					tomCommodityExchange.setCdkey(
							date1.concat(String.valueOf((int) (rand.nextDouble() * (100000 - 10000) + 10000))));
					tomCommodityExchange.setCommodityId(Integer.parseInt(jsonObject.getString("commodityId")));
					tomCommodityExchange.setCode(jsonObject.getString("userId"));
					tomCommodityExchangeMapper.insertSelective(tomCommodityExchange);

					TomCommodity tomCommodity = new TomCommodity();
					TomCommodity selectByPrimaryKey2 = tomCommodityMapper
							.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("commodityId")));
					tomCommodity.setCommodityId(Integer.parseInt(jsonObject.getString("commodityId")));
					tomCommodity.setCommodityNumber(selectByPrimaryKey2.getCommodityNumber() - 1);
					tomCommodityMapper.updateByPrimaryKeySelective(tomCommodity);

					TomEbRecord ebRecord = new TomEbRecord();
					ebRecord.setCode(jsonObject.getString("userId"));
					ebRecord.setExchangeNumber(-commodity1.getCommodityPrice());
					ebRecord.setRoad("10");
					ebRecord.setUpdateTime(new Date());
					tomEbRecordMapper.insertSelective(ebRecord);
					TomUserInfo tomUserInfo = new TomUserInfo();
					tomUserInfo.setCode(jsonObject.getString("userId"));
					tomUserInfo.seteNumber(selectByPrimaryKey.geteNumber() - commodity1.getCommodityPrice());
					tomUserInfoMapper.updateByCode(tomUserInfo);
					JsonMapper jsonMapper = new JsonMapper();
					String time = simple1.format(date);
					Map<String,String> map = new HashMap<String,String>();
					map.put("exchangeTime", time);
					String exchangeTime = jsonMapper.toJson(map);
					return new Result("Y", exchangeTime, "0", "");
				} else {
					fail="亲,您的积分数量不足！";
					return new Result("N", "", "0", fail);
				}
			} else {
				fail="亲,商品剩余数量不足，暂时无法兑换哦！";
				return new Result("N", "", "0", fail);
			}
			}else{
				fail="亲，此商品已经下架了，看看其他的商品吧！";
				return new Result("N", "", "0", fail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 功能描述：[获取商品自提地址]
	 *
	 * 创建者：cjx 创建时间: 2016年5月30日 下午3:24:30
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCommodityArayacakAddress(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCommodity commodity = new TomCommodity();
		String commodityId = request.getParameter("commodityId");
		commodity = tomCommodityMapper.selectByPrimaryKey(Integer.parseInt(commodityId));
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(commodity);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 功能描述：[E币排行榜 ELE_TOP_GOLD_LIST]
	 * 
	 * 创建人：LG
	 * 
	 * 创建时间：2016 年5月31 14：00
	 * 
	 * @param request
	 * @param response
	 */
	public Result eleTopGoldList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		PageData<CommodityResult> page = new PageData<CommodityResult>();
		JsonMapper mapper = new JsonMapper();
		List<CommodityResult> list = new ArrayList<CommodityResult>();
		String code = request.getParameter("userId");

		if (null == code || "".equals(code)) {
			return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
		}
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));// (int) map.get("startNum") +
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}				
		int count = tomUserInfoMapper.selectCountByeNumber();
		list = tomUserInfoMapper.selectByeNumber(map);

		List<CommodityResult> cList = new ArrayList<CommodityResult>();
		for (CommodityResult comm : list) {
			TomEmp emp = tomEmpMapper.selectByPrimaryKey(comm.getUserId());
			CommodityResult com = new CommodityResult();
			com.setUserId(comm.getUserId());
			com.setHeadImg(comm.getHeadImg());
			com.setName(emp.getName());
			com.seteNumber(comm.geteNumber());
			com.setAddUpEName(comm.getAddUpEName());
			com.setDeptName(comm.getDeptName());
			cList.add(com);
		}
		page.setCount(count);
		page.setDate(cList);
		page.setPageNum((int) map.get("startNum") / (int) map.get("endNum") + 1);
		String json = mapper.toJson(page);
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 功能描述：[获取商品详情 ELE_COMMODITY_DETAILS]
	 * 
	 * 创建人：LG 创建时间：2016年6月1日 10：00
	 * 
	 * @param request
	 * @param response
	 */
	public Result eleCommodityDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		List<CommodityDetatilResult> list = new ArrayList<CommodityDetatilResult>();
		JsonMapper Mapper = new JsonMapper();
		String userId = request.getParameter("userId");
		int commodityId = Integer.parseInt(request.getParameter("commodityId"));

		if (null == userId || "".equals(userId) || "".equals(commodityId)) {
			return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "非法参数！");
		}

		map.put("userId", userId);
		map.put("commodityId", commodityId);

		list = tomCommodityMapper.selectBycommodity(map);
		Map<Object, Object> queryMap = new HashMap<Object, Object>();
		for (CommodityDetatilResult com : list) {
			queryMap.put("commodity_id", com.getCommodityId());
			queryMap.put("commodity_name", com.getCommodityName());
			queryMap.put("commodity_price", com.getCommodityPrice());
			queryMap.put("commodity_synopsis", com.getCommoditySynopsis());
			queryMap.put("commodity_picture", com.getCommodityPicture());
			queryMap.put("arayacak", com.getArayacak());
			queryMap.put("post", com.getPost());
			queryMap.put("commodityNumber", com.getCommodityNumber());
		}
		String json = Mapper.toJson(queryMap);

		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[展示收货地址]
	 *
	 * 创建者：cjx 创建时间: 2016年6月12日 下午5:06:28
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleMyAddress(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String userId = request.getParameter("userId");
		TomUserInfo userInfo = tomUserInfoMapper.selectByPrimaryKey(userId);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(userInfo);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
}
