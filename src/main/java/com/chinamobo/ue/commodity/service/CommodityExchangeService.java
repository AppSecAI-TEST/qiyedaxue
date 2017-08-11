package com.chinamobo.ue.commodity.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.commodity.dao.TomCommodityExchangeMapper;
import com.chinamobo.ue.commodity.entity.TomCommodityExchange;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

@Service
public class CommodityExchangeService {
	
	@Autowired
	private TomCommodityExchangeMapper exchangeMapper;
	
	String filePath=Config.getProperty("file_path");
	
	public PageData<TomCommodityExchange> queryCommodityExchange(int pageNum, int pageSize,String empCode,String postMethod,String exchangeState) {
		
//		if(empCode!=null){
//			empCode=empCode.replaceAll("/", "//");
//			empCode=empCode.replaceAll("%", "/%");
//			empCode=empCode.replaceAll("_", "/_");
//		}
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomCommodityExchange> list=new ArrayList<TomCommodityExchange>();
		PageData<TomCommodityExchange> page=new PageData<TomCommodityExchange>();
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("empNumber",empCode);
		map.put("postMethod", postMethod);
		map.put("exchangeState", exchangeState);
		int count = exchangeMapper.count(map);
		if(pageSize==-1){
			list = exchangeMapper.queryList(map);
			pageSize = count;
		}else{
			map.put("startNum", (pageNum - 1) * pageSize);
			map.put("endNum", pageSize);    // pageNum *                                        
			list = exchangeMapper.selectPage(map);
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 
	 * 功能描述：[修改兑换状态]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月20日
	 * @param commodityExchange
	 * @return
	 */
	@Transactional
	public void modifyCommodityExchangeState(TomCommodityExchange commodityExchange){
		exchangeMapper.updateExample(commodityExchange);
	}
	
	/**
	 * 
	 * 功能描述：[兑换记录生成excel]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月2日 下午6:10:22
	 * @param commodityExchanges
	 * @param fileName
	 * @return
	 */
	public String TopicsToExcel(List<TomCommodityExchange> commodityExchanges,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("序号");
		headers.add("员工姓名");
		headers.add("员工编号");
		headers.add("商品名称");
		headers.add("兑换码");
		headers.add("兑换方式");
		headers.add("兑换时间");
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
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
  
        for (int i = 0; i < commodityExchanges.size(); i++)  
        {  
            row = sheet.createRow((int) i + 1);  
            TomCommodityExchange commodityExchange =  commodityExchanges.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(i+1);  
            row.createCell((short) 1).setCellValue(commodityExchange.getEmpName());  
            row.createCell((short) 2).setCellValue(commodityExchange.getEmpNumber()); 
            row.createCell((short) 3).setCellValue(commodityExchange.getCommodityName()); 
            if(commodityExchange.getPostMethod().equals("2")){
            	row.createCell((short) 5).setCellValue("自提"); 
            	row.createCell((short) 4).setCellValue(commodityExchange.getCdkey()); 
            }else{
            	row.createCell((short) 4).setCellValue("-"); 
            	row.createCell((short) 5).setCellValue("邮寄"); 
            }
            
            row.createCell((short) 6).setCellValue(format.format(commodityExchange.getExchangeTime())); 
        }  
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try  
        { 
        	
//        	System.out.println(File.separator);
        	
        	File file = new File(path);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
            FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
            wb.write(fout);  
            fout.close();  
           
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        
        return path+"/"+fileName;
	}

	/**
	 * 
	 * 功能描述：[条件查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月2日 下午6:10:40
	 * @param example
	 * @return
	 */
	public List<TomCommodityExchange> selectByExample(
			TomCommodityExchange example) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return exchangeMapper.selectByExample(example);
	}
	
	public TomCommodityExchange selectByExchange(int commodityId, String code,String exchangeTime) throws ParseException {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TomCommodityExchange commodityExchange = new TomCommodityExchange();
		commodityExchange.setCode(code);
		commodityExchange.setCommodityId(commodityId);
		commodityExchange.setExchangeTime(format.parse(exchangeTime));
		return exchangeMapper.selectByPrimaryKey(commodityExchange);
	}
}
