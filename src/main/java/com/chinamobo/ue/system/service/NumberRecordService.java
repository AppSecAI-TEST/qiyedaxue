package com.chinamobo.ue.system.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.exception.EleException;
import com.chinamobo.ue.system.dao.TomNumberRecordMapper;
import com.chinamobo.ue.system.entity.TomNumberRecord;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class NumberRecordService {
	@Autowired
	private TomNumberRecordMapper numberRecordMapper;
	
//	public final static String KC="0";
//	public final static String KCFL="1";
//	public final static String JSPF="2";
//	public final static String KCPF="3";
//	public final static String SJ="4";
//	public final static String TK="5";
//	public final static String KS="6";
//	public final static String RWB="7";
//	public final static String PXHD="8";
//	public final static String TLQ="9";
//	public final static String TKFL="10";
//	public final static String JS="11";
	 
	/**
	 * 
	 * @param numberId(0课程编号；1课程分类编号；2讲师评分编号；3课程评分编号；4试卷编号；5题库编号；6考试编号；7任务包编号；8培训活动编号；9讨论圈版块编号;10题库类型;11讲师编号)
	 * @return
	 */
	public String getNumber(String belong){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		int i=Integer.parseInt(belong);
		String head="";
		switch(i){
		case 0:
			head="KC";
			break;
		case 1:
			head="KCFL";
			break;
		case 2:
			head="JSPF";
			break;
		case 3:
			head="KCPF";
			break;
		case 4:
			head="SJ";
			break;
		case 5:
			head="TK";
			break;
		case 6:
			head="KS";
			break;
		case 7:
			head="RWB";
			break;
		case 8:
			head="PXHD";
			break;
		case 9:
			head="TLQ";
			break;	
		case 10:
			head="TKFL";
			break;
		case 11:
			head="JS";
			break;	
		case 12:
			head="GS";
			break;	
		case 13:
			head="BM";
			break;	
		}
		String number="";	
		TomNumberRecord numberRecord=numberRecordMapper.selectByBelong(belong);
		
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String nowDate=format.format(new Date());
		if(numberRecord.getTailNumber()<1000&&numberRecord.getTailNumber()>=0){
			if(nowDate.equals(format.format(numberRecord.getCreateTime()))){
				String tail=String.valueOf(numberRecord.getTailNumber()+1);
				if(tail.length()==1){
					number=head+nowDate+"0"+tail;
				}else if(tail.length()==2){
					number=head+nowDate+tail;
				}else if(tail.length()==3){
					number=head+nowDate+tail;
				}
				numberRecord.setTailNumber(numberRecord.getTailNumber()+1);
			}else{
				numberRecord.setCreateTime(new Date());
				numberRecord.setTailNumber(1);
				number=head+nowDate+"01";
			}
			numberRecordMapper.updateByPrimaryKeySelective(numberRecord);
			return number;
		}else{
			throw new EleException("尾号越界！--只允许1000以下的尾号。");
		}
			
	}
}
