package com.chinamobo.ue.activity.service;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chinamobo.ue.activity.dao.TomCertificateMapper;
import com.chinamobo.ue.activity.dto.TomCertificateDto;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.RedisUtils;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 描述 [证书模版业务处理]
 * 创建者 LXT
 * 创建时间 2017年6月6日 下午3:55:53
 */
@Service
public class CertificateService {
	
	@Autowired
	private TomCertificateMapper tomCertificateMapper;
	
	
	ShiroUser user=ShiroUtils.getCurrentUser();
	String sysPath=Config.getProperty("certificate_path");
	String baseFilePath="certificate/base/";//数据库字段存储值
	String afourFilePath="certificate/afour/";//A4图片数据库字段存储地址
	Jedis jedis=RedisUtils.getJedis();
	
	/**
	 * 功能描述 [查询证书模版 分页]
	 * 创建者 LXT
	 * 创建时间 2017年6月6日 下午3:58:14
	 * @param pageNum 页码
	 * @param pageSize 每页大小
	 * @return
	 */
	public String certificateList(int pageNum,int pageSize){
		int count=tomCertificateMapper.findListCount();
		if(pageSize==-1){
			pageSize=count;
		}
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		List<TomCertificateDto> list=tomCertificateMapper.listTomCertificate(map);
		PageData<TomCertificateDto> page = new PageData<TomCertificateDto>();	
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		
		return jsonMapper.toJson(page);
	}
	
	/**
	 * 功能描述 [添加证书模版]
	 * 取出原模版，将新增的数据添加到模版中，以此创建新的证书模版
	 * 创建者 LXT
	 * 创建时间 2017年6月6日 下午3:50:37
	 * @param jsonStr
	 * @return
	 */
	public String insert(String jsonStr){
		JSONObject json=JSONObject.fromObject(jsonStr);
		JSONObject attributeJson=json.getJSONObject("attribute");//前端页面来的标签属性值
		
		long l=System.currentTimeMillis();
		String baseAddress=baseFilePath+l+".jpg";//数据库字段存储值
		String afourBaseAddress=afourFilePath+l+".jpg";//A4数据库字段存储值
		TomCertificateDto tomCertificateDto=tomCertificateMapper.findOne(json.getInt("id"));
		String oldAddress=tomCertificateDto.getAddress();//原型图片的地址
		String oldAfourAddress=tomCertificateDto.getAfourAddress();//原型图A4地址
		int multiple=tomCertificateDto.getMultiple();//A4所需放大的倍数
		/* 根据前端的标签属性值 和 倍数值 来计算A4的标签属性值*/
		JSONObject afourAttributeJson=new JSONObject();//创建A4的属性
		Iterator<String> it= attributeJson.keys();
		while(it.hasNext()){
			JSONObject afourKeyJson=new JSONObject();//创建A4的key属性
			String key=it.next();
			JSONObject keyJson=attributeJson.getJSONObject(key);
			//前端传来的y轴需要重新计算，需要去掉标签框中字体离边框的距离
			int y=keyJson.getInt("y");
			int height=keyJson.getInt("height");
			int fontSize=keyJson.getInt("fontSize");
			//int newY=y+height-((height-fontSize)/2);
			int newY=y+height-((height-fontSize));
			keyJson.put("y", newY);
			
			afourKeyJson.put("name", keyJson.getString("name"));
			afourKeyJson.put("x", keyJson.getInt("x")*multiple);
			afourKeyJson.put("y", newY*multiple);
			afourKeyJson.put("width", keyJson.getInt("width")*multiple);
			afourKeyJson.put("height", height*multiple);
			afourKeyJson.put("fontSize", fontSize*multiple);
			afourKeyJson.put("fontFamily", keyJson.getString("fontFamily"));
			afourKeyJson.put("fontStyle", keyJson.getString("fontStyle"));
			afourAttributeJson.put(key, afourKeyJson);
		}
		tomCertificateDto.setAddress(baseAddress);//设置图片数据库存储地址
		tomCertificateDto.setIsEnable("Y");//设置为启用
		tomCertificateDto.setIsBase("N");//设置为非基础版本
		tomCertificateDto.setAttribute(attributeJson.toString());//设置新的标签属性值
		tomCertificateDto.setAfourAddress(afourBaseAddress);//设置A4图片数据库存储地址
		tomCertificateDto.setAfourAttribute(afourAttributeJson.toString());//设置A4的新的标签属性值
		tomCertificateDto.setCreateUserId(user.getCode());
		tomCertificateDto.setCreateTime(new Date());
		try {
			tomCertificateMapper.insert(tomCertificateDto);
		} catch (Exception e) {
			return "{\"result\":\"error\",\"message\":\"向数据库中插入证书记录时失败\"}";
		}
		
		int id=tomCertificateDto.getId();
		//添加到redis中
		if(jedis!=null){
			JSONObject jsonRedis=JSONObject.fromObject(tomCertificateDto);
			jedis.hset("TomCertificate", id+"", jsonRedis.toString());
		}
		//制作标准模版水印
		JSONObject jsonMap=new JSONObject();
		jsonMap.put("address", oldAddress);//需要制作的原图地址
		jsonMap.put("certificateAddress",baseAddress);//新增模版的输出位置 imageAddress
		jsonMap.put("attribute", attributeJson.toString());//标签属性
		jsonMap.put("width", tomCertificateDto.getImageWidth());//图片宽度
		jsonMap.put("height", tomCertificateDto.getImageHeight());//图片高度
		try {
			this.addWatermark(jsonMap); //制作水印图片，如果制图失败将删除数据库记录并返回错误信息
		} catch (Exception e) {
			jedis.hdel("TomCertificate", id+"");
			tomCertificateMapper.delete(id);
			return "{\"result\":\"error\",\"message\":\"制作证书图片时失败\"}";
		}
		
		
		//制作A4模版水印
		JSONObject jsonMapA4=new JSONObject();
		jsonMapA4.put("address", oldAfourAddress);//需要制作的A4原图地址
		jsonMapA4.put("certificateAddress",afourBaseAddress);//A4模版的输出位置 afourImageAddress
		jsonMapA4.put("attribute", afourAttributeJson.toString());//标签属性
		jsonMapA4.put("width", tomCertificateDto.getAfourImageWidth());//A4图片宽度
		jsonMapA4.put("height", tomCertificateDto.getAfourImageHeight());//A4图片高度
		try {
			this.addWatermark(jsonMapA4);
		} catch (Exception e) {
			jedis.hdel("TomCertificate", id+"");
			tomCertificateMapper.delete(id);
			return "{\"result\":\"error\",\"message\":\"制作证书图片时失败\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 功能描述 [修改证书模版]
	 * 创建者 LXT
	 * 创建时间 2017年6月6日 下午3:51:41
	 * @param jsonStr
	 * @return
	 */
	public String update(String jsonStr){
		return "";
	}
	
	/**
	 * 功能描述 [删除证书模版]
	 * 假删，将启用状态改为N
	 * 创建者 LXT
	 * 创建时间 2017年6月6日 下午3:53:04
	 * @param jsonStr
	 * @return
	 */
	public String delete(Integer id){
		try {
			tomCertificateMapper.delete(id);
		} catch (Exception e) {
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 功能描述 [制作证书图片]
	 * 创建者 LXT
	 * 创建时间 2017年7月3日 下午3:51:48
	 * @param jsonMap
	 */
	void addWatermark(JSONObject jsonMap){
		try {
			File file1= new File(sysPath+baseFilePath);
			if(!file1.exists()){
				file1.mkdirs();
			}
			File file2= new File(sysPath+afourFilePath);
			if(!file2.exists()){
				file2.mkdirs();
			}
			File srcImgFile = new File(sysPath+jsonMap.getString("address"));
			System.out.println(srcImgFile.getPath());
	        Image srcImg = ImageIO.read(srcImgFile);  
	        int width = jsonMap.getInt("width");  
	        int height = jsonMap.getInt("height");
	        // 加水印  
	        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
	        Graphics2D g = bufImg.createGraphics();  
	        g.drawImage(srcImg, 0, 0, width, height, null);  
	        g.setColor(Color.BLACK); //设置颜色 
	        //遍历属性
	        JSONObject jsonAll=jsonMap.getJSONObject("attribute");
	        Set<String> keySet=jsonAll.keySet();
	        for(String keyStr:keySet){
	        	if(keyStr.equals("user")){
	        		continue;
	        	}
	        	JSONObject json=jsonAll.getJSONObject(keyStr);
	        	String name=json.getString("name");
	        	if(null==name || "null".equals(name) || "".equals(name)){
	        		//如果name为空则从jsonMap取值
	        		continue;
	        	}
	        	String  fontStyle=json.getString("fontStyle");
	        	int fontStyle1=Font.PLAIN;
	        	if(fontStyle.equalsIgnoreCase("normal")){
	        		fontStyle1=Font.PLAIN;
	        	}else if(fontStyle.equalsIgnoreCase("bold")){
	        		fontStyle1=Font.BOLD;
	        	}else if(fontStyle.equalsIgnoreCase("italic")){
	        		fontStyle1=Font.ITALIC;
	        	}else if(fontStyle.equalsIgnoreCase("bold|italic")){
	        		fontStyle1=Font.BOLD | Font.ITALIC;
	        	}
	        	Font font = new Font(json.getString("fontFamily"), fontStyle1, json.getInt("fontSize"));
        		g.setFont(font);
        		g.drawString(name,json.getInt("x"), json.getInt("y"));
	        }
            g.dispose(); 
	        // 输出图片  
            FileOutputStream outImgStream = new FileOutputStream(sysPath+jsonMap.getString("certificateAddress"));  
            ImageIO.write(bufImg, "jpg", outImgStream);  
            outImgStream.flush();  
            outImgStream.close();  
		 } catch (Exception e) {  
	         e.printStackTrace();  
	     }
	}
}
