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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityOtherRelationMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityOtherRelation;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.statistics.entity.TomActivityStatistics;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

@Service
public class ActivityStatisticsService {
	
	@Autowired
	private TomActivityMapper activityMapper;
	
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;
	
	@Autowired
	private TomAdminMapper adminMapper;
	
	String filePath=Config.getProperty("file_path");
	
	/**
	 * 
	 * 功能描述：[活动统计分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月26日
	 * @param pageNum
	 * @param pageSize
	 * @param activityStatistics
	 * @return
	 */
	public PageData<TomActivityStatistics> queryActivityStatistics(int pageNum, int pageSize,String activityName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
//		if(activityName!=null){
//			activityName=activityName.replaceAll("/", "//");
//			activityName=activityName.replaceAll("%", "/%");
//			activityName=activityName.replaceAll("_", "/_");
//		}
		
		List<TomActivityStatistics> list = new ArrayList<TomActivityStatistics>();
		PageData<TomActivityStatistics> page = new PageData<TomActivityStatistics>();
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("activityName",activityName);
		int count = activityMapper.countByList(map);
		if(pageSize==-1){
			pageSize = count;
		}
		
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivity> activities = activityMapper.selectListByParam(map);
		
		for(TomActivity activity : activities){
			TomActivityStatistics activityStatistics = new TomActivityStatistics();
			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
			activityEmpsRelation.setApplyType("E");
			activityEmpsRelation.setActivityId(activity.getActivityId());
			activityStatistics.setActivityId(activity.getActivityId());
			activityStatistics.setActivityName(activity.getActivityName());
			activityStatistics.setNeedApply(activity.getNeedApply());
			activityStatistics.setActivityStartTime(activity.getActivityStartTime());
			activityStatistics.setActivityEndTime(activity.getActivityEndTime());
			activityStatistics.setActivityNumber(activity.getActivityNumber());
			if(activity.getNeedApply().equals("Y")){
				activityStatistics.setNeedApply("是");
				activityStatistics.setOpenNumber(String.valueOf(activityEmpsRelationMapper.countByExample(activityEmpsRelation)));
				activityEmpsRelation.setApplyStatus("Y");
				activityStatistics.setTotalEnrollment(String.valueOf(activityEmpsRelationMapper.countByExample(activityEmpsRelation)));
				activityStatistics.setNumberOfParticipants(String.valueOf(activity.getNumberOfParticipants()));
				if(activityStatistics.getNumberOfParticipants().equals("0")){
					activityStatistics.setRegistrationRate("0%");
				}else{
					NumberFormat nf = NumberFormat.getPercentInstance();
		            nf.setMaximumFractionDigits(2);
		            String cf = nf.format(Double.parseDouble(activityStatistics.getTotalEnrollment())/Double.parseDouble(activityStatistics.getNumberOfParticipants()));
					activityStatistics.setRegistrationRate(cf);
				}
			}else{
				activityStatistics.setNeedApply("否");
				activityStatistics.setOpenNumber("-");
				activityStatistics.setTotalEnrollment("-");
				activityStatistics.setNumberOfParticipants("-");
				activityStatistics.setRegistrationRate("-");
			}
			String[] id = activity.getAdmins().split(",");
			String names = "";
			for(int i=1;i<id.length;i++){
				TomAdmin admin = adminMapper.selectByPrimaryKey(Integer.parseInt(id[i]));
				if(admin!=null){
					String name=admin.getName();
					if(i==1){
						names = name;
					}else{
						names = names+","+name;
					}
				}
				
			}
			activityStatistics.setAdmins(names);
			list.add(activityStatistics);
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
//	public PageData<TomActivityStatistics> queryActivityStatistics(){
//		List<TomActivityStatistics> list = new ArrayList<TomActivityStatistics>();
//		PageData<TomActivityStatistics> page = new PageData<TomActivityStatistics>();
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		int count = activityMapper.countByList(map);
//		int pageSize = count;
//		int pageNum = 1;
//		map.put("startNum", (pageNum - 1) * pageSize);
//		map.put("endNum", pageNum * pageSize);
//		List<TomActivity> activities = activityMapper.selectListByParam(map);
//		for(TomActivity activity : activities){
//			TomActivityStatistics activityStatistics = new TomActivityStatistics();
//			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
//			activityEmpsRelation.setApplyType("E");
//			activityEmpsRelation.setActivityId(activity.getActivityId());
//			activityStatistics.setActivityName(activity.getActivityName());
//			activityStatistics.setNeedApply(activity.getNeedApply());
//			activityStatistics.setActivityStartTime(activity.getActivityStartTime());
//			activityStatistics.setActivityEndTime(activity.getActivityEndTime());
//			activityStatistics.setActivityNumber(activity.getActivityNumber());
//			if(activity.getNeedApply().equals("Y")){
//				activityStatistics.setNeedApply("是");
//				activityStatistics.setOpenNumber(String.valueOf(activityEmpsRelationMapper.countByExample(activityEmpsRelation)));
//				activityEmpsRelation.setApplyStatus("Y");
//				activityStatistics.setTotalEnrollment(String.valueOf(activityEmpsRelationMapper.countByExample(activityEmpsRelation)));
//				activityStatistics.setNumberOfParticipants(String.valueOf(activity.getNumberOfParticipants()));
//				if(activityStatistics.getNumberOfParticipants().equals("0")){
//					activityStatistics.setRegistrationRate("0%");
//				}else{
//					NumberFormat nf = NumberFormat.getPercentInstance();
//		            nf.setMaximumFractionDigits(2);
//		            String cf = nf.format(Double.parseDouble(activityStatistics.getTotalEnrollment())/Double.parseDouble(activityStatistics.getNumberOfParticipants()));
//					activityStatistics.setRegistrationRate(cf);
//				}
//			}else{
//				activityStatistics.setNeedApply("否");
//				activityStatistics.setOpenNumber("-");
//				activityStatistics.setTotalEnrollment("-");
//				activityStatistics.setNumberOfParticipants("-");
//				activityStatistics.setRegistrationRate("-");
//			}
//			String[] id = activity.getAdmins().split(",");
//			String names = "";
//			for(int i=1;i<id.length;i++){
//				TomAdmin admin = adminMapper.selectByPrimaryKey(Integer.parseInt(id[i]));
//				if(admin!=null){
//					String name=admin.getName();
//					if(i==1){
//						names = name;
//					}else{
//						names = names+","+name;
//					}
//				}
//			}
//			activityStatistics.setAdmins(names);
//			list.add(activityStatistics);
//		}
//		
//		page.setDate(list);
//		page.setPageNum(pageNum);
//		page.setPageSize(pageSize);
//		page.setCount(count);
//		return page;
//	}
//	
	/**
	 * 
	 * 功能描述：[导出活动统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月27日
	 * @param topics
	 * @param
	 * @return
	 */
	public String ActivityStatisticsExcel(List<TomActivityStatistics> activityStatistics,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("活动编号");
		headers.add("活动名称");
		headers.add("是否需要报名");
		headers.add("活动开始时间");
		headers.add("活动结束时间");
		headers.add("开放人数");
		headers.add("报名人数");
		headers.add("限额人数");
		headers.add("报名率");
		
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
        
        for (int i = 0; i < activityStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityStatistics activityStatisticss = activityStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(activityStatisticss.getActivityNumber());  
            row.createCell((short) 1).setCellValue(activityStatisticss.getActivityName());  
            row.createCell((short) 2).setCellValue(activityStatisticss.getNeedApply());  
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell((short) 3).setCellValue(format2.format(activityStatisticss.getActivityStartTime()));
            row.createCell((short) 4).setCellValue(format2.format(activityStatisticss.getActivityEndTime()));
            row.createCell((short) 5).setCellValue(activityStatisticss.getOpenNumber());
            row.createCell((short) 6).setCellValue(activityStatisticss.getTotalEnrollment());
            row.createCell((short) 7).setCellValue(activityStatisticss.getNumberOfParticipants());
            row.createCell((short) 8).setCellValue(activityStatisticss.getRegistrationRate());
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
	 * 功能描述：[查看活动统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月1日
	 * @param
	 * @param
	 * @return
	 */
	public PageData<TomActivityEmpsRelation> seeActivityStatistics(int pageNum, int pageSize, int activityId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityEmpsRelation> list = new ArrayList<TomActivityEmpsRelation>();
		PageData<TomActivityEmpsRelation> page = new PageData<TomActivityEmpsRelation>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("activityId",activityId);
		TomActivityEmpsRelation activityEmpsRelationss = new TomActivityEmpsRelation();
		activityEmpsRelationss.setActivityId(activityId);
		int count = activityEmpsRelationMapper.countByExample(activityEmpsRelationss);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		TomActivityEmpsRelation activityEmpsRelations = new TomActivityEmpsRelation();
		activityEmpsRelations.setActivityId(activityId);
		activityEmpsRelations.setApplyStatus("Y");
		List<TomActivityEmpsRelation> activityEmpsRelation = activityEmpsRelationMapper.selectListExample(map);
		page.setDate(activityEmpsRelation);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 
	 * 功能描述：[导出活动详细统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月1日
	 * @param topics
	 * @param
	 * @return
	 */
	public String ActivityStatisticsDetailedExcel(List<TomActivityEmpsRelation> activityEmpsRelation,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("员工编号");
		headers.add("姓名");
		headers.add("报名时间");
		
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
        
        for (int i = 0; i < activityEmpsRelation.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityEmpsRelation activityEmpsRelations = activityEmpsRelation.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(activityEmpsRelations.getCode());
            row.createCell((short) 1).setCellValue(activityEmpsRelations.getName());
            if(activityEmpsRelations.getApplyStatus().equals("Y")&&activityEmpsRelations.getIsRequired().equals("N")){
            	 DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String activityStartTime = format2.format(activityEmpsRelations.getUpdateTime());
                 row.createCell((short) 2).setCellValue(activityStartTime);
            }else{
            	row.createCell((short) 2).setCellValue("-");
            }
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
