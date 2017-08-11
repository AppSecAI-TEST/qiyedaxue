//package com.chinamobo.ue.system.service;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import javax.validation.ConstraintViolationException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.chinamobo.ue.activity.dao.TomActivityDeptMapper;
//import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
//import com.chinamobo.ue.system.dao.TomDeptMapper;
//import com.chinamobo.ue.system.dao.TomEmpMapper;
//import com.chinamobo.ue.system.dao.TomInterfLogMapper;
//import com.chinamobo.ue.system.dao.TomMessagesMapper;
//import com.chinamobo.ue.system.dao.TomOrgMapper;
//import com.chinamobo.ue.system.dao.TomUserInfoMapper;
//import com.chinamobo.ue.system.dao.TomUserLogMapper;
//import com.chinamobo.ue.system.entity.TomDept;
//import com.chinamobo.ue.system.entity.TomEmpOne;
//import com.chinamobo.ue.system.entity.TomInterfLog;
//import com.chinamobo.ue.system.entity.TomOrg;
//import com.chinamobo.ue.system.entity.TomUserInfo;
//import com.chinamobo.ue.system.entity.TomUserLog;
//import com.chinamobo.ue.system.webservise.DeptInterfaceServise;
//import com.chinamobo.ue.system.webservise.EmpInterfaceServise;
//import com.chinamobo.ue.system.webservise.OrgInterfaceServise;
//import com.chinamobo.ue.utils.InterfaceJsonToObject;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class TimerServise {
//
//	@Autowired
//	private contextInitRedisService contextInitRedisService;
//	@Autowired
//	private TomUserLogMapper tomUserLogMapper;
//	@Autowired
//	private TomUserInfoMapper tomUserInfoMapper;
//	@Autowired
//	private TomInterfLogMapper tomInterfLogMapper;
//	ObjectMapper mapper = new ObjectMapper();
//	@Autowired
//	private TomEmpMapper tomEmpMapper;
//	@Autowired
//	private EmpInterfaceServise empInterfaceServise;
//	@Autowired
//	private TomActivityEmpsRelationMapper tomActivityEmpsRelationMapper;
//	@Autowired
//	private TomActivityDeptMapper tomActivityDeptMapper;
//	@Autowired
//	private TomMessagesMapper tomMessagesMapper;
//	@Autowired
//	private TomDeptMapper tomDeptMapper;
//	@Autowired
//	private TomOrgMapper tomOrgMapper;
//	@Autowired
//	private DeptInterfaceServise deptInterfaceServise;
//	@Autowired
//	private SendMessageService sendMessageService;
//	private static Logger logger = LoggerFactory.getLogger(InterfaceJsonToObject.class);
//
//	@Transactional
//	public void add() throws Exception {
//
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();
//
//		List<TomOrg> orgList = OrgInterfaceServise.getOrgList();
//		for (TomOrg tomOrg : orgList) {
//			try {
//				TomOrg selectByPrimaryKey = tomOrgMapper.selectById(tomOrg.getCode());
//				if (null == selectByPrimaryKey) {
//					tomOrgMapper.insertOrg(tomOrg);
//				} else {
//					tomOrgMapper.updateByPrimaryKeySelective(tomOrg);
//				}
//			} catch (Exception e) {
//				logger.info("异常信息：" + e.toString());
//				continue;
//			}
//		}
//
//		List<TomDept> deptList = deptInterfaceServise.getDeptList();
//		for (TomDept tomDept : deptList) {
//			try {
//				TomDept dept = tomDeptMapper.selectByPrimaryKey2(tomDept.getCode());
//				if (null == dept) {
//					tomDeptMapper.insertSelective(tomDept);
//				} else {
//					tomDeptMapper.updateByPrimaryKeySelective(tomDept);
//				}
//			} catch (Exception e) {
//				logger.info("异常信息：" + e.toString());
//				continue;
//			}
//		}
//
//		TomInterfLog tomInterfLog = tomInterfLogMapper.selectByPrimaryKey(4);
//		List<TomEmpOne> selectAll = contextInitRedisService.empList;
//		List<TomEmpOne> list = empInterfaceServise.getPsndoconeJson();
//
//		for (TomEmpOne tomEmpOne : list) {
////			 if(tomEmpOne.getCode().equals("E016958")||tomEmpOne.getCode().equals("E021465")){
////			 System.out.println("~~~!!!!!~~~~");
////			 }
//			try {
//				int count = 0;
//				for (int i = 0; i < selectAll.size(); i++) {
//					TomEmpOne tomEmpOne2 = selectAll.get(i);
//					if (tomEmpOne.getCode().equals(tomEmpOne2.getCode())) {
//						count++;
//
//						if (tomEmpOne.equals(tomEmpOne2)) {
//
//						} else {
//							// 更改
//							tomEmpOne.setUpdateTime(new Date());
//							tomEmpMapper.updateByPrimaryKeySelective(tomEmpOne);
//							TomUserLog tomUserLog = new TomUserLog();
//							tomUserLog.setCode(tomEmpOne.getCode());
//							tomUserLog.setEmail(tomEmpOne.getSecretEmail());
//							tomUserLog.setName(tomEmpOne.getName());
//							tomUserLog.setPhone(tomEmpOne.getMobile());
//							tomUserLog.setStatus(tomEmpOne.getPoststat());
//							tomUserLogMapper.updateByCode(tomUserLog);
//							TomUserInfo tomUserInfo = new TomUserInfo();
//							tomUserInfo.setCode(tomEmpOne.getCode());
//							tomUserInfo.setStatus(tomEmpOne.getPoststat());
//							tomUserInfoMapper.updateByCode(tomUserInfo);
//						}
//						selectAll.remove(selectAll.get(i));
//						break;
//					}
//
//				}
//				if (count == 0) {
//					// 新增
//					tomEmpOne.setUpdateTime(new Date());
//					tomEmpOne.setCreateTime(new Date());
//					tomEmpMapper.insertSelective(tomEmpOne);
//					TomUserLog tomUserLog = new TomUserLog();
//					tomUserLog.setCode(tomEmpOne.getCode());
//					tomUserLog.setEmail(tomEmpOne.getSecretEmail());
//					tomUserLog.setName(tomEmpOne.getName());
//					tomUserLog.setPhone(tomEmpOne.getMobile());
//					tomUserLog.setStatus("Y");
//					tomUserLogMapper.insertSelective(tomUserLog);
//					TomUserInfo tomUserInfo = new TomUserInfo();
//					tomUserInfo.setCode(tomEmpOne.getCode());
//					tomUserInfo.setAddUpENumber(0);
//					tomUserInfo.setContinuousSignInNumber(0);
//					tomUserInfo.setCourseNumber("0");
//					tomUserInfo.setLearningTime("0");
//					tomUserInfo.seteNumber(0);
//					tomUserInfo.setSumSignInTimes(0);
//					tomUserInfo.setSex(tomEmpOne.getSex());
//					tomUserInfo.setPhoneNumber(tomEmpOne.getMobile());
//					tomUserInfo.setName(tomEmpOne.getName());
//					tomUserInfo.setAnonymity("Y");
//					tomUserInfo.setAddress(tomEmpOne.getCityname());
//					tomUserInfo.setLastSignInTime(format.parse("2016-01-01"));
//					tomUserInfo.setStatus("Y");
//					tomUserInfoMapper.insertSelective(tomUserInfo);
//				}
//			} catch (Exception e) {
//				logger.info("异常信息：" + e.toString());
//				continue;
//			}
//		}
//		if (selectAll.size() != 0) {
//			for (TomEmpOne tomEmpOne : selectAll) {
//				tomEmpOne.setUpdateTime(new Date());
//				tomEmpOne.setPoststat("N");
//				tomEmpMapper.updateByPrimaryKeySelective(tomEmpOne);
//				TomUserLog tomUserLog = new TomUserLog();
//				TomUserInfo tomUserInfo = new TomUserInfo();
//				tomUserInfo.setStatus("N");
//				tomUserLog.setStatus("N");
//				tomUserLog.setCode(tomEmpOne.getCode());
//				tomUserInfo.setCode(tomEmpOne.getCode());
//				tomUserLogMapper.updateByCode(tomUserLog);
//				tomUserInfoMapper.updateByCode(tomUserInfo);
//			}
//		}
//		tomInterfLog.setCreateTime(date);
//		tomInterfLog.setLogId(4);
//		tomInterfLogMapper.updateByPrimaryKeySelective(tomInterfLog);
//		contextInitRedisService.init();
//	}
//
//}
