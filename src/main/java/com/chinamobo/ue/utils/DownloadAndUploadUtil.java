package com.chinamobo.ue.utils;

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
import com.qcloud.cosapi.api.CosCloud;

import net.sf.json.JSONObject;


//public static final int APP_ID=10030081;
//public static final String SECRET_ID="AKIDRU67knMm7jtpb18thiYQjavk8bZIrjSj";
//public static final String SECRET_KEY ="pXAAQ1EdsIFC92RXqguxC3HAfctrAuej";
//public static final String bucketName="elearning";
//public static final String SLICEFILE_REMOTE_PATH ="/test_files/";
//public static final long  expired=System.currentTimeMillis() / 1000 + 30;

public class DownloadAndUploadUtil {
	public  int APP_ID=Integer.parseInt(Config.getProperty("APP_ID"));
	public  String SECRET_ID=Config.getProperty("SECRET_ID");
	public  String SECRET_KEY =Config.getProperty("SECRET_KEY");
	public  String bucketName=Config.getProperty("bucketName");
	public  String SLICEFILE_REMOTE_PATH =Config.getProperty("SLICEFILE_REMOTE_PATH");
	public  long  expired=System.currentTimeMillis() / 1000 + Long.parseLong(Config.getProperty("expired"));
	
/**
    * 创建者：LG
	* 创建时间：2016.5.10
    * 功能描述：上传视频到COS
    * 创建者：LG
    * @param filePath 存储地址
    * @throws Exception 
    */
	
	
	
	
	public  String  upLoad(String filePath) throws Exception{
		CosCloud cos=new CosCloud(APP_ID, SECRET_ID, SECRET_KEY,600);
		DateFormat format=new SimpleDateFormat("HHmmssSSS");
		String date=new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());//获取当前时间年
		String date1=new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());//获取当前时间月
		String date2=new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());//获取当前时间日
		String creatpath= SLICEFILE_REMOTE_PATH+date+"/"+date1+"/"+date2+"/";//目录创建地址拼接，以年月日为创建目录，同一天时间创建的目录会自动覆盖同名目录
		String cosFilepath=creatpath+format.format(new Date()) + RandomUtils.nextInt(100, 999) + ".mp4";//全路径地址拼接

        String result=cos.sliceUploadFile(bucketName, cosFilepath, filePath,1024*1024);
//        System.out.println(result);
		JSONObject json=JSONObject.fromObject(result);
		
		String dataString=json.getString("data");
		JSONObject data=JSONObject.fromObject(dataString);
		File file = new File(filePath);
		file.delete();
		return data.getString("access_url");
	}
	/**
	 * 创建者：LG
	 * 创建时间：2016.5.10
	 *功能描述：下载视频
	 *@param mediapath 存储到cos的视频路径
	 */
	public  String downLoad(String mediapath){
		
		//下载储存的新地址
		String filepath=null;
		
		DateFormat formate=new SimpleDateFormat("HHmmssSSS");
		
		String downloadpath=mediapath+File.separator+formate.format(new Date())+RandomUtils.nextInt(100, 999) + ".mp4";//下载地址可直接访问观看
		
		
 		try {
 			//通过HTTP地址访问服务器文件
 			URL url=new URL(mediapath);
 	 		HttpURLConnection con=(HttpURLConnection) url.openConnection();
 	     	con.setDoInput(true);
			con.setRequestMethod("GET");
			
			//用缓存获取文件输入流并输出
			BufferedInputStream bfinput=new BufferedInputStream(con.getInputStream());
			FileOutputStream out=new  FileOutputStream(new File(filepath));
			byte[] bytes=new byte[5120];
			int size=0;
			while((size=bfinput.read(bytes))!=-1){
				out.write(bytes, 0, size);
			}
			bfinput.close();
			out.close();
			con.disconnect();
 			String info=String.format("下载成功，filepath="+filepath);
		} catch (Exception e) {
			filepath=null;
 			 String error = String.format("下载媒体文件失败：%s", e);  
// 			        System.out.println(error); 
		}

		return downloadpath;
		
	}
	/**
		 * 创建者：LG
		 * 创建时间：2016.5.10
		 *功能描述：点播视频
		 *
		 */
//	public String  OnMedia(String ){
//		
//	}
}
