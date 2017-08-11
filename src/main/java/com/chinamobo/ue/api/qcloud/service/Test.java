package com.chinamobo.ue.api.qcloud.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcloud.cosapi.api.CosCloud;
import com.qcloud.cosapi.sign.Sign;

import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws Exception {
		String filePath="E:\\1460947303003513910683.mp4";
		int APP_ID=10030081;
		 String SECRET_ID="AKIDRU67knMm7jtpb18thiYQjavk8bZIrjSj";
		  String SECRET_KEY ="pXAAQ1EdsIFC92RXqguxC3HAfctrAuej";
		  String bucketName="elearning";
		  String SLICEFILE_REMOTE_PATH ="/enterpriseuniversity/";
		CosCloud cos=new CosCloud(APP_ID, SECRET_ID, SECRET_KEY,60);
		DateFormat format=new SimpleDateFormat("HHmmssSSS");
		String date=new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());//获取当前时间年
		String date1=new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());//获取当前时间月
		String date2=new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());//获取当前时间日
		String creatpath= SLICEFILE_REMOTE_PATH+date+"/"+date1+"/"+date2+"/";//目录创建地址拼接，以年月日为创建目录，同一天时间创建的目录会自动覆盖同名目录
		String cosFilepath=creatpath+format.format(new Date()) + RandomUtils.nextInt(100, 999) + ".mp4";//全路径地址拼接
        
        
         System.out.println(cosFilepath);
		
		
		
        String result=cos.sliceUploadFile(bucketName, cosFilepath, filePath ,1024*1024);
		JSONObject json=JSONObject.fromObject(result);
		
		ObjectMapper mapper=new ObjectMapper();
		String dataString=json.getString("data");
		JSONObject data=JSONObject.fromObject(dataString);
		
		String url=data.getString("access_url");
//		.append("a=").append(appId)
//        .append("&k=").append(secretId)
//        .append("&e=").append(expired)
//        .append("&t=").append(now)
//        .append("&r=").append(rdm)
//        .append("&f=").append(fileId)
//        .append("&b=").append(bucketName)
		
		long expired = System.currentTimeMillis() / 1000 + 20*1000;
		String sign = Sign.appSignature(APP_ID, SECRET_ID, SECRET_KEY, expired, bucketName);
		System.out.println(sign);
		String Url1="http://"+bucketName+"-"+APP_ID+".file.myqcloud.com"+cosFilepath+"?sign="+sign;
		//String Url1="http://upload-10040296.file.myqcloud.com"+cosFilepath+"?sign="+sign;		
		System.out.println(Url1);
		try {
		URL ur=new URL(Url1);
	 		HttpURLConnection con=(HttpURLConnection) ur.openConnection();
	     	con.setDoInput(true);
		con.setRequestMethod("GET");
		
		//用缓存获取文件输入流并输出
		BufferedInputStream bfinput=new BufferedInputStream(con.getInputStream());
		FileOutputStream out=new  FileOutputStream(new File("F:/ee.mp4"));
		byte[] bytes=new byte[5120];
		int size=0;
		while((size=bfinput.read(bytes))!=-1){
			out.write(bytes, 0, size);
		}
		bfinput.close();
		out.close();
		con.disconnect();
			String info=String.format("下载成功，filepath="+"d:/ee.mp4");
	} catch (Exception e) {
			 String error = String.format("下载媒体文件失败：%s", e);  
			        System.out.println(error); 
	}
	
		
//		String data=new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
//		System.out.println(data);
//		String data1=new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
//		System.out.println(data1);
//		String data2=new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
//		System.out.println(data2);


	}

}
