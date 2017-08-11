package com.chinamobo.ue.system.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.course.service.LecturerService;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomEmpOne;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class UserLogServise {
	@Autowired
	private TomUserLogMapper tomUserLogMapper;
	@Autowired
	private LecturerService lecturerService;
	@Autowired
	private AdminServise adminServise;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomUserInfoMapper tomUserInfoMapper;
//	@Autowired
//	private EmpServise empServise;
	@Transactional
	public void updateByCode(TomUserLog tomUserLog) {

		tomUserLogMapper.updateByCode(tomUserLog);
	}
	@Transactional
	public void updateByCodeTwo(String code, String status) {
		TomUserLog tomUserLog = new TomUserLog();
		tomUserLog.setCode(code);
		tomUserLog.setStatus(status);
		TomAdmin tomAdmin = adminServise.selectByCode(code);
		TomLecturer tomLecturer = lecturerService.selectByCode(code);
		if(null!=adminServise.selectByCode(code)){
			tomAdmin.setStatus(status);
			adminServise.updateStatus(tomAdmin);
		}
		if(null!=lecturerService.selectByCode(code)){
			tomLecturer.setStatus(status);
			lecturerService.updateStatus(tomLecturer);
		}
		TomEmpOne empOne=new TomEmpOne();
		empOne.setCode(code);
		empOne.setPoststat(status);
		empOne.setUpdateTime(new Date());
		tomEmpMapper.updateByPrimaryKeySelective(empOne);
		TomUserInfo tomUserInfo = tomUserInfoMapper.selectByPrimaryKey(code);
		tomUserInfo.setStatus(status);
		tomUserInfoMapper.updateByCode(tomUserInfo);
		tomUserLogMapper.updateByCode(tomUserLog);
		
	}
	
	@Transactional
	public void updateByCodeTwo(String code) {
		TomUserLog tomUserLog = new TomUserLog();
		tomUserLog.setCode(code);
		TomEmp tomEmp = new TomEmp();
//		tomEmp = empServise.selectByCode(code);
		String id = tomEmp.getId();
		id = id.substring(id.length() - 6, id.length());
		tomUserLog.setPassword(id);
		tomUserLogMapper.updateByCode(tomUserLog);
		
	}
	
	@Transactional
	public void updatePassword(TomUserLog tomUserLog){
		tomUserLogMapper.updatePassword(tomUserLog);
	}

	/**
	 * 判断手机号是否存在于用户表中
	 * 创建者: zhj
	 * @param phone
	 * @return
	 */
	public boolean phoneExists(String phone){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomUserLog tomUserLog =  tomUserLogMapper.selectUserByPhone(phone);
		if (tomUserLog != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public TomUserLog selectUserByPhone(String phone){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomUserLogMapper.selectUserByPhone(phone);
	}
	
	public void insertUserLog(TomUserLog record) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		tomUserLogMapper.insertSelective(record);
	}
	
	public void updateUserLog(TomUserLog tomUserLog){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		tomUserLogMapper.updateByPrimaryKeySelective(tomUserLog);
	}
	
}
