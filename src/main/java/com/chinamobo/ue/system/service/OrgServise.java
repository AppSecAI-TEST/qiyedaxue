package com.chinamobo.ue.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.dao.TomGrpOrgRelationMapper;
import com.chinamobo.ue.system.dao.TomOrgGroupsMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomGrpOrgRelation;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.system.entity.TomOrgGroups;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.MapManager;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class OrgServise {
	private static final String STR_FORMAT = "000";  
	@Autowired
	private TomOrgMapper orgMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomGrpOrgRelationMapper tomGrpOrgRelationMapper ;
	@Autowired
	private TomOrgGroupsMapper tomOrgGroupsMapper;
//	private static Logger logger = LoggerFactory.getLogger(InterfaceJsonToObject.class);
	ObjectMapper mapper = new ObjectMapper();
	String basePath1 = Config.getProperty("file_path");

	public List<Tree> selectByGrp_Code(String grp_code) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return orgMapper.selectByGrp_Code(grp_code);
	}

	public List<TomOrg> select() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return orgMapper.select();
	}

	public TomOrg selectById(String pk_org) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return orgMapper.selectById(pk_org);
	}
	
	public TomOrg selectById2(String pk_org) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return orgMapper.selectById2(pk_org);
	}
	@Transactional
	public void insertOrg(TomOrg tomOrg) {
		TomOrgGroups record = tomOrgGroupsMapper.selectByPrimaryKey(tomOrg.getCode());
		tomOrg.setCode(numberRecordService.getNumber(MapManager.numberType("GS")));
		tomOrg.setPkOrg(tomOrg.getCode());
//		TomOrg selectPkOrg = orgMapper.selectPkOrg();
//		 String pkOrg = selectPkOrg.getPkOrg();
//		 Integer intHao = Integer.parseInt(pkOrg);  
//	        intHao++;  
//	        DecimalFormat df = new DecimalFormat(STR_FORMAT);  
//	        String format = df.format(intHao);
//	        tomOrg.setPkOrg(format);
		orgMapper.insertOrg(tomOrg);
		TomGrpOrgRelation tomGrpOrgRelation = new TomGrpOrgRelation();
		tomGrpOrgRelation.setCode(tomOrg.getCode());
		tomGrpOrgRelation.setStatus("Y");
		tomGrpOrgRelation.setAdministrator(record.getCreator());
		tomGrpOrgRelation.setGrpCode(record.getCode());
		tomGrpOrgRelationMapper.insertSelective(tomGrpOrgRelation);
	}

	public Tree selectByTree(String pk_org) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return orgMapper.selectByTree(pk_org);
	}

	public Tree selectOrgByDeptCode(String code) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		// TODO Auto-generated method stub
		return orgMapper.selectOrgByDeptCode(code);
	}

//	@Transactional
//	public  void  insertList() throws Exception {
//		List<TomOrg> members = getOrgList();
//		  int batchCount =500;// 每批commit的个数
//          int batchLastIndex = batchCount;// 每批最后一个的下标
//          try{
//          for (int index = 0; index < members.size();) {
//              if (batchLastIndex >= members.size()) {
//                  batchLastIndex = members.size();
//                  orgMapper.insertList(members.subList(index, batchLastIndex));
//                  System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
//                  break;// 数据插入完毕，退出循环
//              } else {
//            	  orgMapper.insertList(members.subList(index, batchLastIndex));
//                  System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
//                  index = batchLastIndex;// 设置下一批下标
//                  batchLastIndex = index + (batchCount - 1);
//              }
//          }
//          }catch(Exception e){
//        	  e.printStackTrace();
//          }
//		 //insertCrossEvaluation(list);
//		
//	
//
//	}
	
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
//        Map<String, TomOrg> map = Maps.newHashMap();
//        for (TomOrg one : list) {
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

	public void updateOrg(TomOrg tomOrg) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		orgMapper.updateByPrimaryKeySelective(tomOrg);		
	}
}
