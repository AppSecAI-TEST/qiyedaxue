package com.chinamobo.ue.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
 * 
 * 功能描述：解压ZIP 
 * 
 */
public class DecompressionZip {
		 public static  int demoZip(String url,String outurl) throws IOException{
			  
			 File pathFile = new File(url);  
			 
			 if(!pathFile.exists()){  
				         pathFile.mkdirs();  
			       }  
			   ZipFile zip = new ZipFile(url,Charset.forName("gbk"));  
				    for(Enumeration entries = zip.entries(); entries.hasMoreElements();){  
				    	ZipEntry entry = (ZipEntry)entries.nextElement();  
				         String zipEntryName = entry.getName();  
				         InputStream in = zip.getInputStream(entry);  
				         String outPath = (outurl+"/"+zipEntryName).replaceAll("\\*", "/");;  
				          //判断路径是否存在,不存在则创建文件路径  
				           File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));  
				           if(!file.exists()){  
				        	   file.mkdirs();  
				           } 
				           
				         //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
				         if(new File(outPath).isDirectory()){  
				        	 continue;  
				          }  
				         
				         //输出文件路径信息  
//			              System.out.println(outPath);  
				           OutputStream out = new FileOutputStream(outPath);  
				           byte[] buf1 = new byte[1024];  
				           int len;  
				            while((len=in.read(buf1))>0){  
				            	out.write(buf1,0,len);  
				            }  
				          in.close();  
				          out.close();  
				           }  
//				      System.out.println("******************解压完毕********************"); 
				      return zip.size();
		 }
}
		   

	

