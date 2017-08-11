package com.chinamobo.ue.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;

public class FileDownloadUtil {
	/**
	 * 
	 * 功能描述：[对流文件进行加密]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月25日 下午8:48:01
	 * @param file
	 * @param key
	 * @param temp
	 * @throws Exception
	 */
	public static String download(File file,String filePath) throws Exception{
		DateFormat format = new SimpleDateFormat("HHmmssSSS");
		String returnPath=filePath+File.separator+format.format(new Date()) + RandomUtils.nextInt(100, 999) + ".mp4";
		String key=Config.getProperty("key");
		if("".equals(key) || key==null){
			key="ele_tom";
		}
		InputStream ins= new FileInputStream(file);
		FileOutputStream  out=new FileOutputStream(new File(returnPath));
        byte[] bytes=new byte[1024];
        int len=0;
        int i=0;
        try{
        	while((len = ins.read(bytes))>0){
        		i++;
        		if(i==1){//对第一块流文件进行加密处理
        			bytes=AESEcode.encode(bytes, key);
        		}
        		out.write(bytes,0,len);
        	}
        	ins.close();
        	out.close();
        }catch(Exception e){
        	throw new Exception();
        }
        file.delete();//删除未加密视频文件
        return returnPath;
	}
}
