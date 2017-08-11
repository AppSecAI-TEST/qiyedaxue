package com.chinamobo.ue.activity.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomCertificateManageMapper;
import com.chinamobo.ue.activity.dao.TomCertificateMapper;
import com.chinamobo.ue.activity.dto.TomCertificateDto;
import com.chinamobo.ue.activity.dto.TomCertificateManageDto;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.RedisUtils;
import com.chinamobo.ue.utils.WeChatUtil;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 描述 [证书管理]
 * 创建者 LXT
 * 创建时间 2017年6月8日 下午3:41:03
 */
@Service
public class CertificateManageService {
	@Autowired
	private TomCertificateMapper tomCertificateMapper;
	@Autowired
	private TomCertificateManageMapper tomCertificateManageMapper;
	@Autowired
	private TomActivityMapper tomActivityMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	
	String sysPath=Config.getProperty("certificate_path");
	String baseFilePath="certificate/baseEmp/";//数据库字段存储值
	String afourFilePath="certificate/afourEmp/";//A4图片数据库字段存储地址
	Jedis jedis=RedisUtils.getJedis();
	
	public String certificateManageList(int pageNum,int pageSize,String activityName){
		PageData<Map<String,Object>> page = new PageData<Map<String,Object>>();	
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("activityName", activityName);
		int count= tomCertificateManageMapper.findListCount(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum *
		List<Map<String,Object>> list =tomCertificateManageMapper.listTomCertificateManage(map);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return JSONObject.fromObject(page).toString();
	}
	
	/**
	 * 功能描述 [导出活动中学员获得的证书，压缩为zip文件发送给客户端]
	 * 创建者 LXT
	 * 创建时间 2017年7月25日 下午2:15:19
	 * @param activityId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void downExport(Integer activityId,HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<Map<String,Object>> list =tomCertificateManageMapper.findCertificateAddressByActivityId(activityId);
		if(list.size()==0){
			return;
		}
		Map<String,String> tomActivity=tomActivityMapper.findOne(activityId);
		List<File> files=new ArrayList<File>();
        for(int i=0;i<list.size();i++){
        	Map<String,Object> map=list.get(i);
        	String url=map.get("afourCertificateAddress").toString();//需要导出的图片所在路径
        	File inFile=new File(sysPath+url);
        	files.add(inFile);
        }
        String temporaryPath=sysPath+tomActivity.get("activityName")+"证书.zip";//在服务器上新建一个临时ZIP文件用来存放图片 tomActivity.get("activityName")
        File temporaryPathFile=new File(temporaryPath);
        FileOutputStream temporaryPathStream=new FileOutputStream(temporaryPathFile);//临时文件输出流
        org.apache.tools.zip.ZipOutputStream temporaryPathZip=new org.apache.tools.zip.ZipOutputStream(temporaryPathStream);//临时ZIP输出流
        temporaryPathZip.setEncoding("gbk");//设置ZIP的编码
        
        for(File file:files){
        	if(file.exists()){
        		FileInputStream fileIn=new FileInputStream(file);
        		BufferedInputStream bufferedIn=new BufferedInputStream(fileIn);
        		org.apache.tools.zip.ZipEntry zipEn=new org.apache.tools.zip.ZipEntry(file.getName());
        		temporaryPathZip.putNextEntry(zipEn);
        		final int MAX_BYTE=100*1024;//100KB
        		int total=bufferedIn.available();//图片的大小
        		int num=(int) Math.floor(total/MAX_BYTE);//图片大小除以100KB的数量
        		int remainder=total%MAX_BYTE;//余数
        		byte[] b;
        		if(num>0){
        			for(int i=0;i<num;i++){
        				b=new byte[MAX_BYTE];
        				bufferedIn.read(b, 0, MAX_BYTE);
        				temporaryPathZip.write(b, 0, MAX_BYTE);
         			}
        		}
        		b=new byte[remainder];
        		bufferedIn.read(b, 0, remainder);
				temporaryPathZip.write(b);
				bufferedIn.close();
				fileIn.close();
				temporaryPathZip.closeEntry();
        	}
        }
        //关闭流
        temporaryPathZip.close();//临时ZIP输出流
        temporaryPathStream.close();//临时文件输出流
        
        //最后重新读入压缩文件
        BufferedInputStream in=new BufferedInputStream(new FileInputStream(temporaryPathFile.getPath()));
        byte[] be=new byte[in.available()];
        in.read(be);
        in.close();
        response.reset();
        BufferedOutputStream out =new BufferedOutputStream(response.getOutputStream());
    	String downFileName = null;
		String userAgent=request.getHeader("USER-AGENT");
		if(userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1){
			downFileName = new String(temporaryPathFile.getName().getBytes("UTF-8"), "ISO-8859-1");
		}else{
			downFileName = URLEncoder.encode(temporaryPathFile.getName(),"UTF8");
		}
		response.setContentType("application/octet-stream");
		response.setHeader("content-disposition", "attachment;filename="+downFileName);
		out.write(be);
		out.flush();
		out.close();
		temporaryPathFile.delete();
		
		//return response;
	}
	
	
	/**
	 * 功能描述 [新增一个学员证书]
	 * 根据证书id获取证书模版对象，将信息填充到证书模版对象，另存起来。
	 * 步骤：先从redis缓存中获取证书模版对象，没有就从数据库中获取并保存到redis中。
	 * 创建者 LXT
	 * 创建时间 2017年6月12日 下午1:38:07
	 * @param jsonString key:code,name,certificateId,activityId
	 * @return
	 */
	public String insert(String jsonString){
		//测试
		JSONObject ceshi= new JSONObject();
		ceshi.put("activityId", 11);
		Date date1 =new Date();
		//图片保存的路径
		String date=new SimpleDateFormat("yyyy/MM/dd").format(date1);
     	String baseFilePathDate=baseFilePath+ date;//基本图片路径
		File file1= new File(sysPath+baseFilePathDate);
		if(!file1.exists()){
			file1.mkdirs();
		}
		String afourFilePathDate=afourFilePath+ date;//A4图片路径
		File file2= new File(sysPath+afourFilePathDate);
		if(!file2.exists()){
			file2.mkdirs();
		}
		
		JSONObject json=JSONObject.fromObject(jsonString);
		TomCertificateDto tomCertificateDto=tomCertificateMapper.findOne(json.getInt("certificateId"));
		
		String certificateAddress=baseFilePathDate+"/"+json.getString("code")+"_"+json.getInt("activityId")+".jpg";
		String afourCertificateAddress=afourFilePathDate+"/"+json.getString("code")+"_"+json.getInt("activityId")+".jpg";
		
		TomCertificateManageDto tomCertificateManageDto= new TomCertificateManageDto();
		tomCertificateManageDto.setActivityId(json.getInt("activityId"));
		tomCertificateManageDto.setCertificateAddress(certificateAddress);
		tomCertificateManageDto.setAfourCertificateAddress(afourCertificateAddress);
		tomCertificateManageDto.setCertificateId(json.getInt("certificateId"));
		tomCertificateManageDto.setCode(json.getString("code"));
		tomCertificateManageDto.setExportCount(0);
		tomCertificateManageDto.setState("Y");
		tomCertificateManageDto.setCreateTime(date1);
		try {
			tomCertificateManageMapper.insert(tomCertificateManageDto);
		
		} catch (Exception e) {
			return "{\"result\":\"error\",\"message\":\"向数据库插入学员证书记录时失败\"}";
		}
		long id=tomCertificateManageDto.getId();
		
		TomActivity tomActivity=tomActivityMapper.selectByPrimaryKey(json.getInt("activityId"));
		
		
		//查看模版是否为基础模版，如果是就需要动态添加属性，否则只添加学员姓名即可
		TomEmp tomEmp=tomEmpMapper.selectByPrimaryKey(json.getString("code"));
		JSONObject valueJson=new JSONObject();
		if("Y".equals(tomCertificateDto.getIsBase())){
			
			valueJson.put("user", tomEmp.getName());//添加学员姓名
			Calendar c= Calendar.getInstance();
			valueJson.put("year", c.get(Calendar.YEAR));
			valueJson.put("month", c.get(Calendar.MONTH));
			valueJson.put("day", c.get(Calendar.DAY_OF_MONTH));
			valueJson.put("activity", tomActivity.getActivityName());
			valueJson.put("company", tomEmp.getOrgname());
			valueJson.put("dateYear", c.get(Calendar.YEAR));
			valueJson.put("dateMonth", c.get(Calendar.MONTH));
			valueJson.put("dateDay", c.get(Calendar.DAY_OF_MONTH));
		}else{
			valueJson.put("user", tomEmp.getName());//添加学员姓名
		}
		
		//制作标准模版水印
		JSONObject jsonMap=new JSONObject();
		jsonMap.put("address", tomCertificateDto.getAddress());//需要制作的原图地址
		jsonMap.put("certificateAddress",certificateAddress);//新增模版的输出位置 imageAddress
		jsonMap.put("attribute", tomCertificateDto.getAttribute());//标签属性
		jsonMap.put("width", tomCertificateDto.getImageWidth());//图片宽度
		jsonMap.put("height", tomCertificateDto.getImageHeight());//图片高度
		jsonMap.put("valueJson", valueJson);//需要往证书上添加的属性
		try {
			this.addWatermark(jsonMap);
		} catch (Exception e) {
			tomCertificateManageMapper.delete(id);
			return "{\"result\":\"error\",\"message\":\"制作学员证书图片时失败\"}";
		}
		
		
		//制作A4模版水印
		JSONObject jsonMapA4=new JSONObject();
		jsonMapA4.put("address", tomCertificateDto.getAfourAddress());//需要制作的A4原图地址
		jsonMapA4.put("certificateAddress",afourCertificateAddress);//A4模版的输出位置 afourImageAddress
		jsonMapA4.put("attribute", tomCertificateDto.getAfourAttribute());//标签属性
		jsonMapA4.put("width", tomCertificateDto.getAfourImageWidth());//A4图片宽度
		jsonMapA4.put("height", tomCertificateDto.getAfourImageHeight());//A4图片高度
		jsonMapA4.put("valueJson", valueJson);//需要往证书上添加的属性
		try {
			this.addWatermark(jsonMapA4);
		} catch (Exception e) {
			tomCertificateManageMapper.delete(id);
			return "{\"result\":\"error\",\"message\":\"制作学员证书图片时失败\"}";
		}
		//证书制作完成推送消息
		try{
			pushMessage(json.getString("code"),tomActivity.getActivityName());
		}catch(Exception e){
			e.printStackTrace();
			return "{\"result\":\"error\",\"message\":\"证书制作完成，但消息推送失败\"}";
		}
		
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 功能描述 [修改]
	 * 创建者 LXT
	 * 创建时间 2017年6月8日 下午3:46:45
	 * @param jsonStr
	 * @return
	 */
	public String updateCertificateManage(String jsonString){
		try {
			TomCertificateManageDto tomCertificateManageDto= (TomCertificateManageDto) JSONObject.toBean(JSONObject.fromObject(jsonString), TomCertificateManageDto.class);
			tomCertificateManageMapper.update(tomCertificateManageDto);
		} catch (Exception e) {
			return "{\"result\":\"error\",\"message\":\"修改学员证书记录时失败\"}";
		}
		
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 功能描述 [删除：假删除  状态 state：N]
	 * 创建者 LXT
	 * 创建时间 2017年6月8日 下午3:46:51
	 * @param jsonStr
	 * @return
	 */
	public String deleteCertificateManage(Integer activityId){
		try {
			tomCertificateManageMapper.deleteByActivityId(activityId);
		} catch (Exception e) {
			return "{\"result\":\"error\",\"message\":\"删除学员证书记录时失败\"}";
		}
		
		return "{\"result\":\"success\"}";
	}
	
	
	/**
	 * 功能描述 [添加水印]
	 * 创建者 LXT
	 * 创建时间 2017年6月9日 下午3:48:51
	 * jsonMap 存储了可能要插入的数据，还有模版的数据
	 */
	void addWatermark(JSONObject jsonMap){
		try { 
			File srcImgFile = new File(sysPath+jsonMap.getString("address"));  
	        Image srcImg = ImageIO.read(srcImgFile);  
	        int width = jsonMap.getInt("width");  
	        int height = jsonMap.getInt("height"); 
	        // 加水印  
	        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
	        Graphics2D g = bufImg.createGraphics();  
	        g.drawImage(srcImg, 0, 0, width, height, null);  
	        g.setColor(Color.BLACK); //设置颜色 
	        //遍历需要往证书上添加的属性
	        JSONObject attributeJson=jsonMap.getJSONObject("attribute");//证书attribute字段的值
	        JSONObject valueJson=jsonMap.getJSONObject("valueJson");
	        Iterator<String> it= valueJson.keys();
	        while(it.hasNext()){
	        	String key=it.next();
	        	JSONObject keyJson=attributeJson.getJSONObject(key);//attribute字段中某个属性值
	        	String  fontWeight=keyJson.getString("fontStyle");
	         	int fontStyle=Font.PLAIN;
	         	if(fontWeight.equalsIgnoreCase("plain")){
	         		fontStyle=Font.PLAIN;
	         	}else if(fontWeight.equalsIgnoreCase("bold")){
	         		fontStyle=Font.BOLD;
	         	}else if(fontWeight.equalsIgnoreCase("italic")){
	         		fontStyle=Font.ITALIC;
	         	}
	         	Font font = new Font(keyJson.getString("fontFamily"), fontStyle, keyJson.getInt("fontSize"));
	    		g.setFont(font);
	    		g.drawString(valueJson.getString(key),keyJson.getInt("x"), keyJson.getInt("y"));
	        }
            g.dispose(); 
	        // 输出图片  
            FileOutputStream outImgStream = new FileOutputStream(sysPath+jsonMap.get("certificateAddress")+"");  
            ImageIO.write(bufImg, "jpg", outImgStream);  
            outImgStream.flush();  
            outImgStream.close();  
		 } catch (Exception e) {  
	         e.printStackTrace();  
	     }
	}
	
	public void pushMessage(String code,String activityName){
		TomMessages tomMessages=tomMessagesMapper.selectByPrimaryKey(0);
		//发送邮件
		if("Y".equals(tomMessages.getSendToEmail())){
			List<String> list = new ArrayList<String>();
			TomEmp emp = tomEmpMapper.selectByPrimaryKey(code);
			if(emp.getSecretEmail()!=null || !"".equals(emp.getSecretEmail())){
				list.add(emp.getSecretEmail());	
				try {
					SendMail sm = SendMailUtil.getMail(list,"证书获得消息提醒",new Date(),"蔚乐学", "恭喜您，完成"+activityName+"培训活动，获得荣誉证书。" );
					sm.sendMessage();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		//发送企业微信APP
		if("Y".equals(tomMessages.getSendToApp())){
			JSONObject jsonMessage=new JSONObject();
			JSONObject contentJson= new JSONObject();
			jsonMessage.put("touser", code);
			jsonMessage.put("msgtype", "text");
			jsonMessage.put("agentid", Integer.valueOf(Config.getProperty("agentid")));
			contentJson.put("content",  "恭喜您，完成"+activityName+"培训活动，获得荣誉证书。");
			jsonMessage.put("text", contentJson);
			 try {
				WeChatUtil.sendMessage(jsonMessage.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
