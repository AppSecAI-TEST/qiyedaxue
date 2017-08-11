package com.chinamobo.ue.statistics.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.statistics.entity.TomAttendanceStatistics;
import com.chinamobo.ue.statistics.entity.TomDetailedAttendanceStatistics;
import com.chinamobo.ue.statistics.entity.TomOpenCourseStatistic;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

@Service
public class CourseStatisticsService {
	
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;
	
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	
	@Autowired
	private TomCoursesMapper coursesMapper;
	
	@Autowired
	private TomActivityMapper activityMapper;
	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseClassifyMapper courseClassifyMapper;
	
	String filePath=Config.getProperty("file_path");
	
	/**
	 * 
	 * 功能描述：[课程分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月26日
	 * @param pageNum
	 * @param pageSize
	 * @param attendanceStatistics
	 * @return
	 */
	public PageData<TomAttendanceStatistics> queryAttendanceStatistics(int pageNum, int pageSize,String courseName,String courseOnline){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomAttendanceStatistics> list = new ArrayList<TomAttendanceStatistics>();
		PageData<TomAttendanceStatistics> page = new PageData<TomAttendanceStatistics>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseName",courseName);
		map.put("courseOnline", courseOnline);
		int count = activityPropertyMapper.countByExample(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivityProperty> activityProperty = activityPropertyMapper.InquiryActivityCurriculum(map);
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
		for(TomActivityProperty activityPropertys : activityProperty){
			TomAttendanceStatistics attendanceStatistics = new TomAttendanceStatistics();
			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
			activityEmpsRelation.setActivityId(activityPropertys.getActivityId());
			activityEmpsRelation.setApplyStatus("Y");
			
			TomActivity activity = activityMapper.selectByPrimaryKey(activityPropertys.getActivityId());
			TomCourses courses = coursesMapper.selectByPrimaryKey(activityPropertys.getCourseId());
			attendanceStatistics.setActivityId(activityPropertys.getActivityId());
			attendanceStatistics.setCourseId(activityPropertys.getCourseId());
			attendanceStatistics.setActivityName(activity.getActivityName());
			attendanceStatistics.setCourseName(courses.getCourseName());
			attendanceStatistics.setCourseNumber(courses.getCourseNumber());
			attendanceStatistics.setCourseStartTime(activityPropertys.getStartTime());
			attendanceStatistics.setCourseEndTime(activityPropertys.getEndTime());
			attendanceStatistics.setToBe(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
			Map<Object, Object> maps = new HashMap<Object, Object>();
			maps.put("activityId", activityPropertys.getActivityId());
			maps.put("courseId", activityPropertys.getCourseId());
			maps.put("learningDate", activityPropertys.getEndTime());
			if(courses.getCourseOnline().equals("Y")){
				attendanceStatistics.setCourseOnline("Y");
				attendanceStatistics.setTo(courseLearningRecordMapper.countByActivity(maps));
			}else{
				attendanceStatistics.setCourseOnline("N");
				attendanceStatistics.setTo(courseSignInRecordsMapper.countByExamples(maps));
			}
			if(attendanceStatistics.getToBe()!=0){
				attendanceStatistics.setPercentage(nf.format((double)attendanceStatistics.getTo()/(double)attendanceStatistics.getToBe()));
			}else{
				attendanceStatistics.setPercentage("0%");
			}
			
			list.add(attendanceStatistics);
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	
//public PageData<TomAttendanceStatistics> queryAttendanceStatistics(){
//		List<TomAttendanceStatistics> list = new ArrayList<TomAttendanceStatistics>();
//		PageData<TomAttendanceStatistics> page = new PageData<TomAttendanceStatistics>();
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		//int count = courseSignInRecordsMapper.countByExample(courseSignInRecords);
//		int count = activityPropertyMapper.countByExample(map);
//		int pageSize = count;
//		int pageNum = 1;
//		map.put("startNum", (pageNum - 1) * pageSize);
//		map.put("endNum", pageNum * pageSize);
//		List<TomActivityProperty> activityProperty = activityPropertyMapper.InquiryActivityCurriculum(map);
//		NumberFormat nf = NumberFormat.getPercentInstance();
//        nf.setMaximumFractionDigits(2);
//		for(TomActivityProperty activityPropertys : activityProperty){
//			TomAttendanceStatistics attendanceStatistics = new TomAttendanceStatistics();
//			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
//			activityEmpsRelation.setActivityId(activityPropertys.getActivityId());
//			activityEmpsRelation.setApplyStatus("Y");
//			
//			TomActivity activity = activityMapper.selectByPrimaryKey(activityPropertys.getActivityId());
//			TomCourses courses = coursesMapper.selectByPrimaryKey(activityPropertys.getCourseId());
//			attendanceStatistics.setActivityId(activityPropertys.getActivityId());
//			attendanceStatistics.setCourseId(activityPropertys.getCourseId());
//			attendanceStatistics.setActivityName(activity.getActivityName());
//			attendanceStatistics.setCourseName(courses.getCourseName());
//			attendanceStatistics.setCourseNumber(courses.getCourseNumber());
//			attendanceStatistics.setCourseStartTime(activityPropertys.getStartTime());
//			attendanceStatistics.setCourseEndTime(activityPropertys.getEndTime());
//			attendanceStatistics.setToBe(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
//			Map<Object, Object> maps = new HashMap<Object, Object>();
//			maps.put("activityId", activityPropertys.getActivityId());
//			maps.put("courseId", activityPropertys.getCourseId());
//			maps.put("learningDate", activityPropertys.getEndTime());
//			if(courses.getCourseOnline().equals("Y")){
//				attendanceStatistics.setCourseOnline("Y");
//				attendanceStatistics.setTo(courseLearningRecordMapper.countByActivity(maps));
//			}else{
//				attendanceStatistics.setCourseOnline("N");
//				attendanceStatistics.setTo(courseSignInRecordsMapper.countByExamples(maps));
//			}
//			if(attendanceStatistics.getToBe()!=0){
//				attendanceStatistics.setPercentage(nf.format((double)attendanceStatistics.getTo()/(double)attendanceStatistics.getToBe()));
//			}else{
//				attendanceStatistics.setPercentage("0%");
//			}
//			
//			list.add(attendanceStatistics);
//		}
//		
//		page.setDate(list);
//		page.setPageNum(pageNum);
//		page.setPageSize(pageSize);
//		page.setCount(count);
//		return page;
//	}
	
	/**
	 * 
	 * 功能描述：[导出课程统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月2日
	 * @param topics
	 * @param
	 * @return
	 */
	public String AttendanceStatisticsExcel(List<TomAttendanceStatistics> attendanceStatistics,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("课程编号");
		headers.add("课程名称");
		headers.add("所属活动");
		headers.add("类型");
		headers.add("课程开始时间");
		headers.add("课程结束时间");
		headers.add("应学人数");
		headers.add("完成人数");
		headers.add("完成率");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < attendanceStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomAttendanceStatistics attendanceStatisticss = attendanceStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(attendanceStatisticss.getCourseNumber());
            row.createCell((short) 1).setCellValue(attendanceStatisticss.getCourseName());
            row.createCell((short) 2).setCellValue(attendanceStatisticss.getActivityName());
            if(attendanceStatisticss.getCourseOnline().equals("Y")){
            	row.createCell((short) 3).setCellValue("线上");
            }else{
            	row.createCell((short) 3).setCellValue("线下");
            }
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell((short) 4).setCellValue(format2.format(attendanceStatisticss.getCourseStartTime()));
            row.createCell((short) 5).setCellValue(format2.format(attendanceStatisticss.getCourseEndTime()));
            row.createCell((short) 6).setCellValue(attendanceStatisticss.getToBe());
            row.createCell((short) 7).setCellValue(attendanceStatisticss.getTo());
            row.createCell((short) 8).setCellValue(attendanceStatisticss.getPercentage());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	
	/**
	 * 
	 * 功能描述：[查看课程学习统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月2日
	 * @param
	 * @param
	 * @return
	 */
	public PageData<TomDetailedAttendanceStatistics> seeAttendanceStatistics(int pageNum, int pageSize, int activityId,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomDetailedAttendanceStatistics> lists = new ArrayList<TomDetailedAttendanceStatistics>();
		PageData<TomDetailedAttendanceStatistics> page = new PageData<TomDetailedAttendanceStatistics>();
		TomCourses course=coursesMapper.selectByPrimaryKey(courseId);
		
		TomActivityProperty activityPropertyExample=new TomActivityProperty();
		activityPropertyExample.setActivityId(activityId);
		activityPropertyExample.setCourseId(courseId);
		TomActivityProperty activityProperty=activityPropertyMapper.selectByExample(activityPropertyExample).get(0);
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("activityId",activityId);
		map.put("applyStatus", "Y");
		TomActivityEmpsRelation activityEmpsRelationss = new TomActivityEmpsRelation();
		activityEmpsRelationss.setActivityId(activityId);
		activityEmpsRelationss.setApplyStatus("Y");
		int count = activityEmpsRelationMapper.countByExample(activityEmpsRelationss);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivityEmpsRelation> activityEmpsRelation = activityEmpsRelationMapper.selectListExample(map);
		for(TomActivityEmpsRelation activityEmps : activityEmpsRelation){
			TomDetailedAttendanceStatistics detailedAttendanceStatistics = new TomDetailedAttendanceStatistics();
			TomEmp emp=empMapper.selectByPrimaryKey(activityEmps.getCode());
			
			detailedAttendanceStatistics.setCode(activityEmps.getCode());
			detailedAttendanceStatistics.setName(activityEmps.getName());
			detailedAttendanceStatistics.setDeptName(emp.getDeptname());
			detailedAttendanceStatistics.setOneDeptName(emp.getOnedeptname());
			if(course.getCourseOnline().equals("Y")){
				TomCourseLearningRecord example=new TomCourseLearningRecord();
				example.setCourseId(courseId);
				example.setCode(activityEmps.getCode());
				example.setLearningDate(activityProperty.getEndTime());
				Map<Object, Object> map1 = new HashMap<Object, Object>();
				map1.put("courseId", courseId);
				map1.put("code", activityEmps.getCode());
				map1.put("learningDate", activityProperty.getEndTime());
				if(courseLearningRecordMapper.countByExample(example)!=0){
					detailedAttendanceStatistics.setAttendance("是");
					detailedAttendanceStatistics.setSignTime(format.format(courseLearningRecordMapper.selectLearnRecord(map1).get(0).getLearningDate()));
				}else{
					detailedAttendanceStatistics.setAttendance("否");
					detailedAttendanceStatistics.setSignTime("-");
				}
			}else{
				TomCourseSignInRecords courseSignInRecords = new TomCourseSignInRecords();
				courseSignInRecords.setCourseId(courseId);
				courseSignInRecords.setCode(activityEmps.getCode());
				courseSignInRecords.setCreateDate(activityProperty.getEndTime());
				if(courseSignInRecordsMapper.countByExample(courseSignInRecords)!=0){
					detailedAttendanceStatistics.setAttendance("是");
					detailedAttendanceStatistics.setSignTime(format.format(courseSignInRecordsMapper.selectByExample(courseSignInRecords).get(0).getCreateDate()));
				}else{
					detailedAttendanceStatistics.setAttendance("否");
					detailedAttendanceStatistics.setSignTime("-");
				}
			}
			
			lists.add(detailedAttendanceStatistics);
		}
		page.setDate(lists);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 
	 * 功能描述：[导出签到详细统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月3日
	 * @param topics
	 * @param
	 * @return
	 */
	public String AttendanceStatisticsDetailedExcel(List<TomDetailedAttendanceStatistics> detailedAttendanceStatistics,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("一级部门");
		headers.add("完成");
		headers.add("完成时间");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < detailedAttendanceStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomDetailedAttendanceStatistics detailedAttendanceStatisticss = detailedAttendanceStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(detailedAttendanceStatisticss.getName());
            row.createCell((short) 1).setCellValue(detailedAttendanceStatisticss.getCode());
            row.createCell((short) 2).setCellValue(detailedAttendanceStatisticss.getDeptName());
            row.createCell((short) 3).setCellValue(detailedAttendanceStatisticss.getOneDeptName());
            row.createCell((short) 4).setCellValue(detailedAttendanceStatisticss.getAttendance());
//            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String activitySignTime = format2.format(detailedAttendanceStatisticss.getSignTime());
            row.createCell((short) 5).setCellValue(detailedAttendanceStatisticss.getSignTime());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}


	/**
	 * 
	 * 功能描述：[公开课统计]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 上午11:26:10
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @return
	 */
	public PageData<TomOpenCourseStatistic> openCourseStatistics(int pageNum, int pageSize,String courseName) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
//		if(courseName!=null){
//			courseName=courseName.replaceAll("/", "//");
//			courseName=courseName.replaceAll("%", "/%");
//			courseName=courseName.replaceAll("_", "/_");
//		}
		
		PageData<TomOpenCourseStatistic> page=new PageData<TomOpenCourseStatistic>();
		List<TomOpenCourseStatistic> list=new ArrayList<TomOpenCourseStatistic>();
		
		TomCourses example=new TomCourses();
		example.setCourseName(courseName);
		example.setCourseOnline("Y");
		
		int count=coursesMapper.countByExample(example);
		if(pageSize==-1){
			pageSize = count;
		}
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("example", example);
		map.put("listOrder", "UPDATE_TIME");
		map.put("orderBy", "desc");
		List<TomCourses> courses=coursesMapper.selectListByPage(map);
		
		TomCourseLearningRecord learningRecordExample;
		for(TomCourses course:courses){
			TomOpenCourseStatistic openCourseStatistic=new TomOpenCourseStatistic();
			openCourseStatistic.setCourseId(course.getCourseId());
			openCourseStatistic.setCourseNumber(course.getCourseNumber());
			openCourseStatistic.setCourseName(course.getCourseName());
			
			learningRecordExample=new TomCourseLearningRecord();
			learningRecordExample.setCourseId(course.getCourseId());
			openCourseStatistic.setLearningNumber(courseLearningRecordMapper.countByExample(learningRecordExample));
			
			List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(course.getCourseId());
			String courseType="";
			for(int i=0;i<courseClassifies.size();i++){
				if(i==0){
					courseType+=courseClassifies.get(i).getClassifyName();
				}else{
					courseType+=","+courseClassifies.get(i).getClassifyName();
				}
			}
			openCourseStatistic.setCourseType(courseType);
			
			list.add(openCourseStatistic);
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}

	/**
	 * 
	 * 功能描述：[公开课学习人员统计]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午12:31:31
	 * @param pageNum
	 * @param pageSize
	 * @param courseId
	 * @return
	 */
	public PageData<TomDetailedAttendanceStatistics> openCourseLearnStatistics(int pageNum, int pageSize,int courseId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomDetailedAttendanceStatistics> page=new PageData<TomDetailedAttendanceStatistics>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		TomCourseLearningRecord example=new TomCourseLearningRecord();
		example.setCourseId(courseId);
		int count=courseLearningRecordMapper.countByExample(example);
		if(pageSize==-1){
			pageSize = count;
		}
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseId", courseId);
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomCourseLearningRecord> courseLearningRecords=courseLearningRecordMapper.selectByPage(map);
		
		
		List<TomDetailedAttendanceStatistics> list=new ArrayList<TomDetailedAttendanceStatistics>();
		for(TomCourseLearningRecord courseLearningRecord:courseLearningRecords){
			TomEmp emp=empMapper.selectByPrimaryKey(courseLearningRecord.getCode());
			TomDetailedAttendanceStatistics detailedAttendanceStatistics=new TomDetailedAttendanceStatistics();
			detailedAttendanceStatistics.setCode(emp.getCode());
			detailedAttendanceStatistics.setName(emp.getName());
			detailedAttendanceStatistics.setDeptName(emp.getDeptname());
			detailedAttendanceStatistics.setOneDeptName(emp.getOnedeptname());
			detailedAttendanceStatistics.setEmail(emp.getSecretEmail());
			detailedAttendanceStatistics.setPhoneNumber(emp.getMobile());
			detailedAttendanceStatistics.setSignTime(format.format(courseLearningRecord.getLearningDate()));
			
			list.add(detailedAttendanceStatistics);
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}

	/**
	 * 
	 * 功能描述：[公开课统计课程列表下载]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午12:53:09
	 * @param openCourseStatistics
	 * @param fileName
	 * @return
	 */
	public String openCourseStatisticsExcel(List<TomOpenCourseStatistic> openCourseStatistics, String fileName) {
		List<String> headers=new ArrayList<String>();
		headers.add("课程编号");
		headers.add("课程名称");
		headers.add("课程分类");
		headers.add("完成人数");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < openCourseStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomOpenCourseStatistic openCourseStatistic = openCourseStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(openCourseStatistic.getCourseNumber());
            row.createCell((short) 1).setCellValue(openCourseStatistic.getCourseName());
            row.createCell((short) 2).setCellValue(openCourseStatistic.getCourseType());
            row.createCell((short) 3).setCellValue(openCourseStatistic.getLearningNumber());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}

	/**
	 * 
	 * 功能描述：[公开课学习记录导出]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午1:18:21
	 * @param detailedAttendanceStatistics
	 * @param fileName
	 * @return
	 */
	public String openCourseLearnExcel(List<TomDetailedAttendanceStatistics> detailedAttendanceStatistics,String fileName) {
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("一级部门");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("完成时间");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < detailedAttendanceStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomDetailedAttendanceStatistics detailedAttendanceStatisticss = detailedAttendanceStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(detailedAttendanceStatisticss.getName());
            row.createCell((short) 1).setCellValue(detailedAttendanceStatisticss.getCode());
            row.createCell((short) 2).setCellValue(detailedAttendanceStatisticss.getDeptName());
            row.createCell((short) 3).setCellValue(detailedAttendanceStatisticss.getOneDeptName());
            row.createCell((short) 4).setCellValue(detailedAttendanceStatisticss.getEmail());
            row.createCell((short) 5).setCellValue(detailedAttendanceStatisticss.getPhoneNumber());
            row.createCell((short) 6).setCellValue(detailedAttendanceStatisticss.getSignTime());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
}
