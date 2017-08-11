//package com.chinamobo.ue.system.webservise;
//
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
//import com.beust.jcommander.internal.Maps;
//import com.chinamobo.ele.json.InvokeInterface;
//import com.chinamobo.ue.system.dao.TomDeptMapper;
//import com.chinamobo.ue.system.entity.TomDept;
//import com.chinamobo.ue.utils.InterfaceJsonToObject;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import jersey.repackaged.com.google.common.collect.Lists;
//@Service
//public class DeptInterfaceServise {
//	
//	@Autowired
//	private TomDeptMapper deptMapper;
//	private static Logger logger = LoggerFactory.getLogger(InterfaceJsonToObject.class);
//	//批量插入
//	@Transactional
//	public String insertList() throws Exception {
//		List<TomDept> members = getDeptList();
//		  int batchCount = 500;// 每批commit的个数
//	      int batchLastIndex = batchCount;// 每批最后一个的下标
//	      try{
//	      for (int index = 0; index < members.size();) {
//	          if (batchLastIndex >= members.size()) {
//	              batchLastIndex = members.size();
//	              deptMapper.insertList(members.subList(index, batchLastIndex));
//	              System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
//	              break;// 数据插入完毕，退出循环
//	          } else {
//	        	  deptMapper.insertList(members.subList(index, batchLastIndex));
//	              System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
//	              index = batchLastIndex;// 设置下一批下标
//	              batchLastIndex = index + (batchCount - 1);
//	          }
//	      }
//	      }catch(Exception e){
//	    	  e.printStackTrace();
//	      }
//		 //insertCrossEvaluation(list);
//		return null;  
//
//	}
//
//	public static List<TomDept> getDeptList() throws Exception {
//
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode rootNode = mapper.readTree(InvokeInterface.getDeptJson());
//		JsonNode arrNode = rootNode.path("Dept");
//		
//		List<TomDept> list  = Lists.newArrayList();
//	    for(JsonNode node : arrNode){
//	       list.add(mapper.readValue(node.traverse(), TomDept.class));
//	    }
//	    Map<String, TomDept> map = Maps.newHashMap();
//		for (TomDept one : list) {
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
//}
