package com.chinamobo.ue.utils;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class LibreofficeUtil {

	public static boolean trans(String fullName , String filePath, String type,int num) {
		String [] temp=fullName.split("\\.");
		String fileName=temp[0];
		boolean res = true;
		StringBuffer sb = new StringBuffer();
		String basePath = Config.getProperty("file_path");
		
		sb.append("cd ");
		sb.append(filePath);
		if (".ppt".equals(type) || ".pptx".equals(type)|| ".doc".equals(type)|| ".docx".equals(type)) {
			sb.append(" && export DISPLAY=:0.0 && libreoffice --headless --invisible --convert-to pdf ");
			sb.append(fileName);
			sb.append(type);
		}
		sb.append("&& gs -q -dQUIET -dSAFER -dBATCH -dNOPAUSE -dNOPROMPT -sDEVICE=jpeg -r250 -sOutputFile=")
		.append(filePath)
		.append("/img-%d.jpg ")
		.append(fileName)
		.append(".pdf");
		if(num==1){
			sb.append("&& mv img-1.jpg img.jpg");
		}else {
			for(int i=1;i<=num;i++){
				sb.append("&& mv img-")
				.append(i)
				.append(".jpg img-")
				.append(i-1)
				.append(".jpg");
			}
		}
		try {
			Process pro = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c",
					sb.toString() });
			pro.waitFor();
			LineNumberReader br = new LineNumberReader(new InputStreamReader(pro.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			System.out.println("INFO:"+sb.toString()); 
		} catch (Exception e) {
			e.printStackTrace();
			res = false;
		}
		return res;
	}
}
