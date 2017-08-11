package com.chinamobo.ue.system.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dto.DeptDto;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.WeChatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import jersey.repackaged.com.google.common.collect.Lists;

@Service
public class DeptServise {
	private static final String STR_FORMAT = "0000";  
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomDeptMapper deptMapper;
	@Autowired
	private ContextInitRedisService contextInitRedisService;
//	private static Logger logger = LoggerFactory.getLogger(InterfaceJsonToObject.class);
	ObjectMapper mapper = new ObjectMapper();
	public List<TomDept> select() {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return deptMapper.select();

	}

	public Tree selectByPkDept(String pkdept) {

		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return deptMapper.selectByPkDept(pkdept);
	}

	public List<Tree> selectByPkFather(String pkfather) {

		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return deptMapper.selectByPkFather(pkfather);
	}

	public List<Tree> selectByPkOrg(String pkorg) {

		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return deptMapper.selectByPkOrg(pkorg);
	}

	public TomDept selectByPKDept(String pk_dept) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return deptMapper.selectByPrimaryKey(pk_dept);
	}

	@Transactional
	public void updateByPrimaryKey(TomDept tomDept) {
		deptMapper.updateByPrimaryKey(tomDept);
	}

	public Tree selectByTree(String pk_dept) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return deptMapper.selectByPkDept(pk_dept);
	}

	/**
	 * 分页查询部门
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param name
	 * @return
	 */
	public PageData<TomDept> selectListByParam(int pageNum, int pageSize, String name) {

		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomDept> page = new PageData<TomDept>();
		Map<Object, Object> map = Maps.newHashMap();
		map.put("name", name);
		int count = deptMapper.countByList(map);
		if (pageSize == -1) {
			pageSize = count + 1;
		}
		List<TomDept> list = Lists.newArrayList();
		if (pageSize != -1) {
			map.put("startNum", (pageNum - 1) * pageSize);
			map.put("endNum", pageSize);//pageNum * 
			list = deptMapper.selectListByParam(map);
		}
//		System.out.println("pageNum" + pageNum);
//		System.out.println("pageSize" + pageSize);
//		System.out.println("count" + count);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	@Transactional
	public void updateStatus(String pk_dept, String hrcanceled) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TomDept tomDept = new TomDept();
		int pk = Integer.parseInt(pk_dept);
		tomDept.setPkDept(pk);
		if (hrcanceled != null) {
				tomDept.setHrcanceled("Y");
		}
		tomDept.setModifiedtime(time.format(new Date()).toString());
		deptMapper.updateByPrimaryKey(tomDept);
	}
	
	//添加部门
	@Transactional
		public void addDept(TomDept tomDept){
		try{
			if(tomDept.getTopcode().equals("1")){
				tomDept.setTopcode(null);
				tomDept.setTopname(null);
			}
			tomDept.setCode(Integer.toString(tomDept.getPkDept()));
			deptMapper.insertSelectives(tomDept);
			tomDept.setCode(String.valueOf(deptMapper.MaxPkDept()));
			tomDept.setPkDept(Integer.valueOf(tomDept.getCode()));
			deptMapper.updateByPrimaryKeySelective(tomDept);
			contextInitRedisService.init();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	//批量插入
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
//	}

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

	public void insertDept(TomDept tomDept) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		Date date =new Date();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = simple.format(date);
		tomDept.setCreationtime(time);
		tomDept.setModifiedtime(time);
		tomDept.setPkDept(Integer.parseInt(numberRecordService.getNumber(MapManager.numberType("BM"))));
		TomDept selectPkDept = deptMapper.selectPkDept();
		int parseInt = Integer.parseInt(selectPkDept.getCode());
		parseInt++;
		  DecimalFormat df = new DecimalFormat(STR_FORMAT);  
	      String format = df.format(parseInt);
	      tomDept.setCode(format);
	      deptMapper.insertSelective(tomDept);
	      contextInitRedisService.init();
	}

	//查询部门信息
	public TomDept selectDepartmentInformation(String pkDept) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return deptMapper.selectByPrimaryKey2(pkDept);
	}
	
	//同步部门
	public void synchronizationDept() throws Exception{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String access_token = WeChatUtil.getToken();
//		DeptDto dto = WeChatUtil.getDept(access_token);
		List<DeptDto> list = WeChatUtil.getDept(access_token);
		for(DeptDto dto:list){
			if(!dto.getParentid().equals("0")){
				TomDept tomDept = new TomDept();
				tomDept.setCode(dto.getId());
				tomDept.setName(dto.getName());
				tomDept.setPkDept(Integer.parseInt(dto.getId()));
				tomDept.setInnercode("200");
				tomDept.setHrcanceled("N");
				tomDept.setOrgcode("1");
				tomDept.setOrgname("摩博科技");
				tomDept.setPkOrg("1");
				Date date =new Date();
				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
				String time = simple.format(date);
				tomDept.setCreationtime(time);
				tomDept.setModifiedtime(time);
				if(dto.getParentid().equals("1")){
					tomDept.setTopcode(null);
					tomDept.setTopname(null);
				}else{
					if(deptMapper.selectByPrimaryKey2(dto.getParentid())!=null){
						TomDept dep = deptMapper.selectByPrimaryKey2(dto.getParentid());
						tomDept.setTopcode(dep.getCode());
						tomDept.setTopname(dep.getName());
					}else{
						tomDept.setTopcode(null);
						tomDept.setTopname(null);
					}
				}
				if(deptMapper.selectByPrimaryKey2(tomDept.getCode())!=null){
					deptMapper.updateByPrimaryKey(tomDept);
				}else{
					deptMapper.insertSelective(tomDept);
				}
			}
		}
		contextInitRedisService.init();
	}
}