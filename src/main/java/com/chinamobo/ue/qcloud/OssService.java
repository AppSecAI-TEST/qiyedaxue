package com.chinamobo.ue.qcloud;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;

import com.aliyun.oss.OSSClient;
import com.chinamobo.ue.utils.Config;

public class OssService {
	
	private static final String endpoint = Config.getProperty("ossUrl");
	private static final String ossUrl= Config.getProperty("ossUrlInter");
	private static final String accessKeyId = Config.getProperty("ossKey");
	private static final String accessKeySecret = Config.getProperty("ossKeySecret");
	private static final String bucketName=Config.getProperty("ossBucketName");
	private static final String SLICEFILE_REMOTE_PATH =Config.getProperty("SLICEFILE_REMOTE_PATH");
	private static final String isLinux = Config.getProperty("isLinux");

	/**
	 * 
	 * 功能描述：[oss上传文件]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年9月13日 上午10:43:15
	 * @param filePath
	 * @return
	 */
	public String upload(String filePath){
		// 创建OSSClient实例
		String url="";
		if("true".equals(isLinux)){
			url=ossUrl;
		}else {
			url=endpoint;
		}
		OSSClient ossClient = new OSSClient(url, accessKeyId, accessKeySecret);
		DateFormat format=new SimpleDateFormat("HHmmssSSS");
		String date=new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());//获取当前时间年
		String date1=new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());//获取当前时间月
		String date2=new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());//获取当前时间日
		String creatpath= SLICEFILE_REMOTE_PATH+date+"/"+date1+"/"+date2+"/";//目录创建地址拼接，以年月日为创建目录，同一天时间创建的目录会自动覆盖同名目录
		String cosFilepath=creatpath+format.format(new Date()) + RandomUtils.nextInt(100, 999) + ".mp4";//全路径地址拼接

		// 上传文件
		ossClient.putObject(bucketName, cosFilepath, new File(filePath));
		// 关闭client
		ossClient.shutdown();
//		File file = new File(filePath);
//		file.delete();
		return cosFilepath;
	}
	
	/**
	 * 
	 * 功能描述：[授权url]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年9月13日 下午5:29:59
	 * @param key
	 * @return
	 */
	public String getUrl(String key){
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		// 设置URL过期时间为1小时
		Date expiration = new Date(new Date().getTime() + 3600 * 1000);

		// 生成URL
		URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
		return url.toString();
	}
	
	public void delete(String key){
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		ossClient.deleteObject(bucketName, key);
	}
}
