package com.chinamobo.ue.api.upload;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.common.entity.UploadResult;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.DownloadAndUploadUtil;
import com.chinamobo.ue.utils.PPTUtil;
import com.chinamobo.ue.utils.PdfUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

@Service
public class UploadService {

	String basePath1 = Config.getProperty("file_path");
	ObjectMapper mapper = new ObjectMapper();	
	 public Result eleUploadUserimg(String json){
		 try{
		 JSONObject jsonObject=JSONObject.fromObject(json);
		  BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
		  String[] split = jsonObject.getString("data").split("base64,");
		  
		        byte[] bytes1 = decoder.decodeBuffer(split[1]);
//		        System.out.println(jsonObject.getString("data"));
		        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);    
		        BufferedImage bi1 =ImageIO.read(bais); 
		       

		  DateFormat format2 = new SimpleDateFormat("HHmmssSSS");
		  DateFormat format1 = new SimpleDateFormat("yyyyMMdd");

			String baseFolder1 = basePath1 + "file/" + "headImage/" + format1.format(new Date());
	        File dir=new File(baseFolder1);
	        if(!dir.exists()){
	         dir.mkdirs();
	        }
	        String fileName=basePath1 + "file/" + "headImage" +"/"+ format1.format(new Date())+"/"+format2.format(new Date()) + RandomUtils.nextInt(100, 999) + ".jpg";
	        File w2 = new File(fileName);//可以是jpg,png,gif格式    
	        ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动   
		    String headImg ="file/" + "headImage" +"/"+ format1.format(new Date())+"/"+format2.format(new Date()) + RandomUtils.nextInt(100, 999) + ".jpg";
		   
	        return new Result("Y", headImg, "0", "");
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "系统繁忙！");
		 }
}
