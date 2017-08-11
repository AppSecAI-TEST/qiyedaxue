//package com.chinamobo.ue.system.webservise;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.beust.jcommander.internal.Lists;
//import com.beust.jcommander.internal.Maps;
//import com.chinamobo.ele.json.InvokeInterface;
//import com.chinamobo.ue.system.dao.TomEmpMapper;
//import com.chinamobo.ue.system.dao.TomUserInfoMapper;
//import com.chinamobo.ue.system.dao.TomUserLogMapper;
//import com.chinamobo.ue.system.entity.TomEmpOne;
//import com.chinamobo.ue.system.entity.TomUserInfo;
//import com.chinamobo.ue.system.entity.TomUserLog;
//import com.chinamobo.ue.utils.InterfaceJsonToObject;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//@Service
//public class EmpInterfaceServise {
//
//	@Autowired
//	private TomEmpMapper tomEmpMapper;
//	@Autowired
//	private TomUserLogMapper tomUserLogMapper;
//	@Autowired
//	private TomUserInfoMapper tomUserInfoMapper;
//	private static Logger logger = LoggerFactory.getLogger(InterfaceJsonToObject.class);
//
//	@Transactional
//	public String insertList() throws Exception {
//		String last = "2016-01-01";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		List<TomEmpOne> members = getPsndoconeJson();
//		int batchCount = 500;// 每批commit的个数
//		int batchLastIndex = batchCount;// 每批最后一个的下标
//		try {
//			for (int index = 0; index < members.size();) {
//				if (batchLastIndex >= members.size()) {
//					List<TomUserLog> list = new ArrayList<TomUserLog>();
//					List<TomUserInfo> list1 = new ArrayList<TomUserInfo>();
//					batchLastIndex = members.size();
//					 tomEmpMapper.insertList(members.subList(index,
//					 batchLastIndex));// 批量插入
//					 //循环添加userlog,userinfo表
//					for (TomEmpOne tomEmpOne : members.subList(index, batchLastIndex)) {
//						TomUserLog tomUserLog = new TomUserLog();
//						TomUserInfo tomUserInfo = new TomUserInfo();
//						 tomUserLog.setCode(tomEmpOne.getCode());
//						 tomUserLog.setEmail(tomEmpOne.getSecretEmail());
//						 tomUserLog.setName(tomEmpOne.getName());
//						 tomUserLog.setPhone(tomEmpOne.getMobile());
//						 tomUserLog.setStatus("Y");
//						tomUserInfo.setCode(tomEmpOne.getCode());
//						tomUserInfo.setAddUpENumber(0);
//						tomUserInfo.setContinuousSignInNumber(0);
//						tomUserInfo.setCourseNumber("0");
//						tomUserInfo.setLearningTime("0");
//						tomUserInfo.seteNumber(0);
//						tomUserInfo.setSumSignInTimes(0);
//						tomUserInfo.setSex(tomEmpOne.getSex());
//						tomUserInfo.setPhoneNumber(tomEmpOne.getMobile());
//						tomUserInfo.setName(tomEmpOne.getName());
//						tomUserInfo.setAnonymity("Y");
//						tomUserInfo.setAddress(tomEmpOne.getCityname());
//						tomUserInfo.setLastSignInTime(sdf.parse(last.toString()));
//						tomUserInfo.setStatus("Y");
//						 list.add(tomUserLog);
//						list1.add(tomUserInfo);
//					}
//					 tomUserLogMapper.insertList(list);
//					tomUserInfoMapper.insertList(list1);
//					System.out.println("index:" + index + " batchLastIndex:" + batchLastIndex);
//					break;// 数据插入完毕，退出循环
//				} else {
//					 tomEmpMapper.insertList(members.subList(index,
//					 batchLastIndex));
//					List<TomUserLog> list = new ArrayList<TomUserLog>();
//					List<TomUserInfo> list1 = new ArrayList<TomUserInfo>();
//					for (TomEmpOne tomEmpOne : members.subList(index, batchLastIndex)) {
//						TomUserLog tomUserLog = new TomUserLog();
//						TomUserInfo tomUserInfo = new TomUserInfo();
//						 tomUserLog.setCode(tomEmpOne.getCode());
//						 tomUserLog.setEmail(tomEmpOne.getSecretEmail());
//						 tomUserLog.setName(tomEmpOne.getName());
//						 tomUserLog.setPhone(tomEmpOne.getMobile());
//						 tomUserLog.setStatus("Y");
//						tomUserInfo.setCode(tomEmpOne.getCode());
//						tomUserInfo.setAddUpENumber(0);
//						tomUserInfo.setContinuousSignInNumber(0);
//						tomUserInfo.setCourseNumber("0");
//						tomUserInfo.setLearningTime("0");
//						tomUserInfo.seteNumber(0);
//						tomUserInfo.setSumSignInTimes(0);
//						tomUserInfo.setPhoneNumber(tomEmpOne.getMobile());
//						tomUserInfo.setName(tomEmpOne.getName());
//						tomUserInfo.setAnonymity("Y");
//						tomUserInfo.setAddress(tomEmpOne.getCityname());
//						tomUserInfo.setSex(tomEmpOne.getSex());
//						tomUserInfo.setLastSignInTime(sdf.parse(last.toString()));
//						tomUserInfo.setStatus("Y");
//						 list.add(tomUserLog);
//						list1.add(tomUserInfo);
//					}
//					 tomUserLogMapper.insertList(list);
//					tomUserInfoMapper.insertList(list1);
//					System.out.println("index:" + index + " batchLastIndex:" + batchLastIndex);
//					index = batchLastIndex;// 设置下一批下标
//					batchLastIndex = index + (batchCount - 1);
//					Thread.sleep(1000);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		 //insertCrossEvaluation(list);
//		return null;
//
//	}
//	//处理得到的数据
//	public static List<TomEmpOne> getPsndoconeJson() throws Exception {
//
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode rootNode = mapper.readTree(InvokeInterface.getPsndoconeJson());
//		JsonNode arrNode = rootNode.path("psnall");
//
//		List<TomEmpOne> list = Lists.newArrayList();
//		for (JsonNode node : arrNode) {
//			list.add(mapper.readValue(node.traverse(), TomEmpOne.class));
//		}
//
//		Map<String, TomEmpOne> map = Maps.newHashMap();
//		for (TomEmpOne one : list) {
//			if (!map.containsKey(one.getCode())) {
//				map.put(one.getCode(), one);
//			} else {
//				logger.info("存在重复记录人员前code：" + one.getCode() + ",info: " + one);
//				logger.info("存在重复记录人员后code：" + one.getCode() + ",info: " + map.get(one.getCode()));
//			}
//		}
//		list.clear();
//		list.addAll(map.values());
//		return list;
//	}
//	public static List<TomEmpOne> getBasPsnAdd(String str) throws Exception {
//
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode rootNode = mapper.readTree(str);
//		JsonNode arrNode = rootNode.path("BasPsnAdd");
//
//		List<TomEmpOne> list = Lists.newArrayList();
//		for (JsonNode node : arrNode) {
//			list.add(mapper.readValue(node.traverse(), TomEmpOne.class));
//		}
//
//		Map<String, TomEmpOne> map = Maps.newHashMap();
//		for (TomEmpOne one : list) {
//			if (!map.containsKey(one.getCode())) {
//				map.put(one.getCode(), one);
//			} else {
//			}
//		}
//		list.clear();
//		list.addAll(map.values());
//		return list;
//	}
//}
