package com.chinamobo.ue.course.service;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
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
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.course.dao.TomTestMapper;
import com.chinamobo.ue.course.dto.TomExportTestDto;
import com.chinamobo.ue.course.dto.TomTestDto;
import com.chinamobo.ue.course.entity.TomTest;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;

import net.sf.json.JSONObject;


/**
 * 功能描述：测试题Service
 * @author Acemon
 * 2016年11月16日
 */

@Service
public class TestService {
	@Autowired
	private TomTestMapper tomTestMapper;
	String filePath=Config.getProperty("file_path");
	
	/**
	 * 功能描述：添加测试题
	 * @throws NoSuchMethodException 
	 * @throws Exception 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public  void addTest(String json) throws Exception{
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomTest tomTest = new TomTest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jo= JSONObject.fromObject(json);
		List<TomExportTestDto> dtoList=new ArrayList<TomExportTestDto>();
		List answerList=jo.getJSONArray("testAnswers");
		JsonMapper mapper =new JsonMapper();
		for (int i = 0; i < answerList.size(); i++) {
			//TomExportTestDto tomExportTestDto= mapper.fromJson(dtoList.get(i).toString(), TomExportTestDto.class);
			TomTestDto tomTestDto= mapper.fromJson(answerList.get(i).toString(), TomTestDto.class);
			tomTest.setCourseId(Integer.parseInt(jo.getString("courseId")));
			tomTest.setTestId(Integer.parseInt(jo.getString("testId")));
			tomTest.setTestName(jo.getString("testName"));
			tomTest.setTestUserId(jo.getString("testUserId"));
			tomTest.setTestUserName(jo.getString("testUserName"));
			tomTest.setTestQuestionId(tomTestDto.getTestQuestionId());
			tomTest.setTestQuestionName(tomTestDto.getTestQuestionName());
			tomTest.setTestStartTime(format.parse(jo.getString("testStartTime")));
			tomTest.setTestEndTime(format.parse(jo.getString("testEndTime")));
			tomTest.setTestUserAnswer(tomTestDto.getTestUserAnswer());
			
		  tomTestMapper.insert(tomTest);
		}
		
		
	}
	
	
	public String TopicsToExcel(List<TomExportTestDto> exportTestDto,String fileName,Integer courseId){
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int countQuestions=tomTestMapper.countQuestions(map);
		int countUsers=tomTestMapper.countUsers(map);
		List<String> headers=new ArrayList<String>();
		headers.add("序号");
		headers.add("用户ID");
		headers.add("用户姓名");
		
		for(int i=1;i<=countQuestions;i++ ){
			headers.add("题号");
			headers.add("题目");
			headers.add("答案");
		}
		
		headers.add("提交时间");
		headers.add("答题时间(分钟)");
		
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
  
        for (int i = 0; i <countUsers; i++)  
        {  
            row = sheet.createRow((int) i + 1);  
            TomExportTestDto tomTest =  exportTestDto.get(i*countQuestions);  
            
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(i+1);
            row.createCell((short) 1).setCellValue(tomTest.getTestUserId());
            row.createCell((short) 2).setCellValue(tomTest.getTestUserName());
            row.createCell((short) 3+3*countQuestions).setCellValue(format.format(tomTest.getTestEndTime()));
            row.createCell((short) 4+3*countQuestions).setCellValue(tomTest.getTestUseTime());
            int count=0;
            for (int j = 0; j < countQuestions; j++) {
				
            	tomTest=exportTestDto.get(i*countQuestions+count);
            row.createCell((short) 2+1+3*(j)).setCellValue(tomTest.getTestQuestionId());
            row.createCell((short) 3+1+3*(j)).setCellValue(tomTest.getTestQuestionName());  
            row.createCell((short) 4+1+3*(j)).setCellValue(tomTest.getTestUserAnswer()); 
            count++;
            }
            
            
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
	 * 功能描述：查询测试
	 * 
	 */
	
	public List<TomExportTestDto> selectByCourseId(Integer courseId){
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		return tomTestMapper.selectByCourseId(map);
		
	}
	
	
	

}
