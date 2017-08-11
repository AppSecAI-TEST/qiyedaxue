package com.chinamobo.ue.statistics.service;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSectionLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourseSectionLearningRecord;
import com.chinamobo.ue.exam.dao.TomJoinExamRecordMapper;
import com.chinamobo.ue.statistics.entity.TomWholeStatistics;
import com.chinamobo.ue.system.dao.TomActiveUserMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.dto.TomSendMessage;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class WholeStatisticsService {

	@Autowired
	private TomUserInfoMapper userInfoMapper;
	@Autowired
	private TomUserLogMapper userLogMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomCourseSectionLearningRecordMapper courseSectionLearningRecordMapper;
	@Autowired
	private TomJoinExamRecordMapper joinExamRecordMapper;
	@Autowired
	private TomEbRecordMapper ebRecordMapper;
	@Autowired
	private TomActiveUserMapper activeUserMapper;
	
	public TomWholeStatistics getWholeStatistics(String beginTime,String endTime) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		
		Date nowDate=new Date();
		String nowStr=format1.format(nowDate);
		
		Map<Object,Object> map=new HashMap<Object, Object>();
		if(null==beginTime||"".equals(beginTime)){
			beginTime=nowStr+" 00:00:00:000";
		}else{
			beginTime=beginTime+" 00:00:00:000";
		}
		map.put("beginTime",format2.parse(beginTime));
		
		if(null==endTime||"".equals(endTime)){
			endTime=nowStr+" 23:59:59:999";
		}else{
			endTime=endTime+" 23:59:59:999";
		}
		map.put("endTime", format2.parse(endTime));
		
		TomWholeStatistics wholeStatistics=new TomWholeStatistics();
		//活跃用户数（按接口访问时间）
		wholeStatistics.setActiveUsers(activeUserMapper.countActiveUsers(map));
		//累计用户数（按token数量）
		wholeStatistics.setTotalUsers(userLogMapper.countTotalUsers());
		//活跃率（活跃用户/累计用户）
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
		wholeStatistics.setActiveRate(nf.format(((double)wholeStatistics.getActiveUsers())/((double)wholeStatistics.getTotalUsers())));
		//累计课程数（创建数量）
		wholeStatistics.setTotalCourses(coursesMapper.countTotalCourses(map));
		//累计播放次数（学习章节次数）
		wholeStatistics.setTotalViews(courseSectionLearningRecordMapper.countTotalViews(map));
		//累计考试数
		wholeStatistics.setTotalExams(joinExamRecordMapper.countTotalExams(map));
		//签到数
		wholeStatistics.setTotalSignIn(ebRecordMapper.countTotalSignIn(map));
		
		return wholeStatistics;
	}
}
