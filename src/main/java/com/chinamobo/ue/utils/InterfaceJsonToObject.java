//package com.chinamobo.ue.utils;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.beust.jcommander.internal.Lists;
//import com.beust.jcommander.internal.Maps;
//import com.chinamobo.ele.json.InvokeInterface;
//import com.chinamobo.ue.system.entity.TomDept;
//import com.chinamobo.ue.system.entity.TomEmpOne;
//import com.chinamobo.ue.system.entity.TomOrg;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class InterfaceJsonToObject {
//	
//	private static Logger logger = LoggerFactory.getLogger(InterfaceJsonToObject.class);
//	
//	public static List<TomOrg> getOrgList() throws Exception {
//
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode rootNode = mapper.readTree(InvokeInterface.getOrgJson());
//		JsonNode arrNode = rootNode.path("Org");
//		
//		List<TomOrg> list  = Lists.newArrayList();
//        for(JsonNode node : arrNode){
//            list.add(mapper.readValue(node.traverse(), TomOrg.class));
//        }
//       
//        Map<String, TomOrg> map = Maps.newHashMap();
//		for(TomOrg one : list) {
//			if (!map.containsKey(one.getCode())) {
//				map.put(one.getCode(), one);
//			} else{
//				logger.info("存在重复记录前code：" + one.getCode() + ",info: "+ one);
//				logger.info("存在重复记录后code：" + one.getCode() + ",info: "+  map.get(one.getCode()));
//			}
//		}
//		list.clear();
//		list.addAll(map.values());
//        return list;
//	}
//
//	public static List<TomDept> getDeptList() throws Exception {
//
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode rootNode = mapper.readTree(InvokeInterface.getDeptJson());
//		JsonNode arrNode = rootNode.path("Dept");
//		
//		List<TomDept> list  = Lists.newArrayList();
//        for(JsonNode node : arrNode){
//           list.add(mapper.readValue(node.traverse(), TomDept.class));
//        }
//
//        Map<String, TomDept> map = Maps.newHashMap();
//		for(TomDept one : list) {
//			if (!map.containsKey(one.getCode())) {
//				map.put(one.getCode(), one);
//			} else{
//				logger.info("存在重复记录前code：" + one.getCode() + ",info: "+ one);
//				logger.info("存在重复记录后code：" + one.getCode() + ",info: "+  map.get(one.getCode()));
//			}
//		}
//		list.clear();
//		list.addAll(map.values());
//        return list;
//	}
//	
//
//	public static List<TomEmpOne> getPsndoconeJson() throws Exception {
//
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode rootNode = mapper.readTree(InvokeInterface.getPsndoconeJson());
//		JsonNode arrNode = rootNode.path("psnall");
//		
//		List<TomEmpOne> list  = Lists.newArrayList();
//        for(JsonNode node : arrNode){
//            list.add(mapper.readValue(node.traverse(), TomEmpOne.class));
//        }
//        
//        Map<String, TomEmpOne> map = Maps.newHashMap();
//		for(TomEmpOne one : list) {
//			if (!map.containsKey(one.getCode())) {
//				map.put(one.getCode(), one);
//			} else{
//				logger.info("存在重复记录人员前code：" + one.getCode() + ",info: "+ one);
//				logger.info("存在重复记录人员后code：" + one.getCode() + ",info: "+  map.get(one.getCode()));
//			}
//		}
//		list.clear();
//		list.addAll(map.values());
//        return list;
//	}
//	
//	public static void main(String[] args) throws Exception {
//		//System.out.println(Arrays.toString(getOrgList().toArray()).toString());
//		System.out.println("==================================");
//		//System.out.println(Arrays.toString(getDeptList().toArray()).toString());
//		
//		List<TomOrg> list = getOrgList();
//		for(TomOrg org:list){
//			System.out.println(org.getCode()+"!!!!!!!"+org.getPkOrg()+"~~~~~"+org.getPkFatherorg());
//		}
////		Map<String, TomEmpOne> map = Maps.newHashMap();
////		System.out.println(list.size());
////		for(TomEmpOne one : list) {
////			if (!map.containsKey(one.getCode())) {
////				map.put(one.getCode(), one);
////			} else{
////				
////				System.out.println("存在重复记录：" + one.getCode());
////				System.out.println("前：" + one);
////				System.out.println("后：" + map.get(one.getCode()));
////			}
////				
////		}
////		System.out.println(map.size());
//		
//	}
//	
//	
//	
//	public void operation(List<TomEmpOne> list ) {
//		// 人员表操作流程
//		// 1. 判断是否存在全量数据
//		
//		// select count(*) from tom_interf_log where INTERFACE_NAME='人员' and OPERATION_SIGN=-1
//		int count=0;
//		if ( count==0) {
//			// saveAll；批量入库
//		} else {
//			// 循环 判断每条记录是否存在
//			for (TomEmpOne one : list) {
//				// select count(*) from tb where one.code=?
//				// if(count>0) {
//				//update
//				//else  insert
//			}
//		}
//		
//		
//		
//	}
//}
