package com.chinamobo.ue.statistics.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
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

import com.chinamobo.ue.activity.common.PageData;
import com.chinamobo.ue.activity.dao.TomActivityFeesMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.statistics.entity.TomActivityCostDetailStatistics;
import com.chinamobo.ue.statistics.entity.TomActivityCostStatistics;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

@Service
public class ActivityCostStatisticsService {
   
	@Autowired
	
	private TomActivityMapper costMapper;
	@Autowired
	
	private TomActivityFeesMapper feemapper;
	/**
	 * 功能介绍[活动费用分页查询]
	 * 
	 * 创建者：LG
	 * 创建时间：2016年6月3日 15：00
	 * @param pageNum
	 * @param pageSize
	 * @param TomActivityCostStatistics
	 * @return
	 */
	
	public PageData<TomActivityCostStatistics> findActityCostList(int pageNum ,int pageSize ,String activityName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
//		if(activityName!=null){
//			activityName=activityName.replaceAll("/", "//");
//			activityName=activityName.replaceAll("%", "/%");
//			activityName=activityName.replaceAll("_", "/_");
//		}
		
		List<TomActivityCostStatistics> list=new ArrayList<TomActivityCostStatistics>();
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		
		map.put("activityName", activityName);
		
		int count=costMapper.selectCount(map);
		if(pageSize==-1){
			pageSize = count;
		}
		
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		list=costMapper.selectactivityCostList(map);
		List<TomActivityCostStatistics> lists=new ArrayList<TomActivityCostStatistics>();
		for(TomActivityCostStatistics cost :list){
			
			TomActivityCostStatistics tom=new TomActivityCostStatistics();
			tom.setActitvtyId(cost.getActitvtyId());
			tom.setActivityName(cost.getActivityName());
			tom.setActivityStarttime(cost.getActivityStarttime());
			tom.setAllFee(cost.getAllFee());
			
			
			lists.add(tom);
		}
		PageData<TomActivityCostStatistics> page=new PageData<TomActivityCostStatistics>();
		page.setCount(count);
		page.setDate(lists);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
//	public  PageData<TomActivityCostStatistics> findActityCostList(){
//        List<TomActivityCostStatistics> list=new ArrayList<TomActivityCostStatistics>();
//		PageData<TomActivityCostStatistics> page=new PageData<TomActivityCostStatistics>();
//		Map<Object,Object> map=new HashMap<Object,Object>();
//		
//		int count=costMapper.selectCount(map);
//		int pageSize = count;
//		int pageNum = 1;
//		map.put("startNum", (pageNum - 1) * pageSize);
//		map.put("endNum", pageNum * pageSize);
//		list=costMapper.selectactivityCostList(map);
//		List<TomActivityCostStatistics> lists=new ArrayList<TomActivityCostStatistics>();
//		for(TomActivityCostStatistics cost :list){
//			
//			TomActivityCostStatistics tom=new TomActivityCostStatistics();
//			tom.setActitvtyId(cost.getActitvtyId());
//			tom.setActivityName(cost.getActivityName());
//			tom.setActivityStarttime(cost.getActivityStarttime());
//			tom.setAllFee(cost.getAllFee());
//			
//			
//			lists.add(tom);
//		}
//		page.setCount(count);
//		page.setDate(lists);
//		page.setPageNum(pageNum);
//		page.setPageSize(pageSize);
//		return page;
//	}
	
	/**
	 * 功能介绍：[活动费用导出]
	 * 创建人：LG
	 * 创建时间：2016年6月3日 16：20
	 * @param
	 * @return 
	 */
	public String downloadFeeStatisticsExcel(List<TomActivityCostStatistics> costlist,String filename){
		List<String> list=new ArrayList<String>();
		list.add("活动名称");
		list.add("活动时间");
		list.add("活动总花费");
		
		HSSFWorkbook work=new HSSFWorkbook();
		HSSFSheet sheet=work.createSheet(filename);
		
		//添加行
		HSSFRow row=sheet.createRow((int)0);
		//列
		HSSFCellStyle style=work.createCellStyle();
		
		//居中格式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 HSSFCell cell ;
	        for(int i=0;i<list.size();i++){
	        	cell= row.createCell((short) i);  
	            cell.setCellValue(list.get(i));  
	            cell.setCellStyle(style);  
	        }
	        for(int i=0;i<costlist.size();i++){
	        	row=sheet.createRow((int)i+1);
	        	TomActivityCostStatistics cos=costlist.get(i);
	        	row.createCell((short) 0).setCellValue(cos.getActivityName());
	        	row.createCell((short) 1).setCellValue(cos.getActivityStarttime());
	        	row.createCell((short) 2).setCellValue(cos.getAllFee());
	        	
	        }
	        
	        DateFormat format=new SimpleDateFormat("yyyyMMdd");
	        String path=Config.getProperty("file_path")+"file/download/"+format.format(new Date());
	        
    		try {

    	        File file=new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
				FileOutputStream out = new FileOutputStream(path+"/"+filename);
				
				work.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
		return path+"/"+filename;
		
	}
	/**
	 * 功能介绍[活动项目细节费用分页查询]
	 * 
	 * 创建者：LG
	 * 创建时间：2016年6月6日 10：30
	 * @param pageNum
	 * @param pageSize
	 * @param TomActivityCostDetailStatistics
	 * @return
	 */
	public PageData<TomActivityCostDetailStatistics> findActityCostDetailList(int pageNum ,int pageSize ,int activityId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		 List<TomActivityCostDetailStatistics> list=new ArrayList<TomActivityCostDetailStatistics>();
		 PageData<TomActivityCostDetailStatistics> page=new PageData<TomActivityCostDetailStatistics>();
		 Map<Object,Object> map=new HashMap<Object,Object>();
		 
		 map.put("activityId", activityId);
		 
		 int count =feemapper.selectBycount(map);
			if(pageSize==-1){
				pageSize = count;
			}
			
			map.put("startNum", (pageNum - 1) * pageSize);
			map.put("endNum", pageSize);//pageNum * 
		 list=feemapper.selectByList(map);
		 List<TomActivityCostDetailStatistics> lists=new ArrayList<TomActivityCostDetailStatistics>();
		 for(TomActivityCostDetailStatistics cost:list){
			 TomActivityCostDetailStatistics fee=new TomActivityCostDetailStatistics();
			 fee.setActivityId(cost.getActivityId());
			 fee.setFeeName(cost.getFeeName());
			 fee.setFee(cost.getFee());
			 lists.add(fee);
		 }
		 page.setCount(count);
		 page.setDate(lists);
		 page.setPageNum(pageNum);
		 page.setPageSize(pageSize);
		return page;
	}
	
	/**
	 * 功能介绍：[活动项目明细费用导出]
	 * 创建人：LG
	 * 创建时间：2016年6月6日 11：10
	 * @param
	 * @return 
	 */
	public String downloadFeeDetailStatisticsExcel(List<TomActivityCostDetailStatistics> costlist,String filename){
		List<String> list=new ArrayList<String>();
		list.add("项目名称");
		list.add("花费");
		
		HSSFWorkbook work=new HSSFWorkbook();
		HSSFSheet sheet=work.createSheet(filename);
		
		//添加行
		HSSFRow row=sheet.createRow((int)0);
		//列
		HSSFCellStyle style=work.createCellStyle();
		
		//居中格式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 HSSFCell cell ;
	        for(int i=0;i<list.size();i++){
	        	cell= row.createCell((short) i);  
	            cell.setCellValue(list.get(i));  
	            cell.setCellStyle(style);  
	        }
	        for(int i=0;i<costlist.size();i++){
	        	row=sheet.createRow((int)i+1);
	        	TomActivityCostDetailStatistics cos=costlist.get(i);
	        	row.createCell((short)0).setCellValue(cos.getFeeName());
	        	row.createCell((short) 1).setCellValue(cos.getFee());

	        	
	        }
	        
	        DateFormat format=new SimpleDateFormat("yyyyMMdd");
	        String path=Config.getProperty("file_path")+"file/download/"+format.format(new Date());
	        
    		try {

    	        File file=new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
				FileOutputStream out = new FileOutputStream(path+"/"+filename);
				
				work.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
		return path+"/"+filename;
		
	}
}
