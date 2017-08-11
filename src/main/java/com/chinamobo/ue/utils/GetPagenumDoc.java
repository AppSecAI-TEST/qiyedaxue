package com.chinamobo.ue.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/*
 * 
 * 功能描述：获取word文档的页数
 * 
 */
public class GetPagenumDoc {
	
	//doc 文件页数
	public static  int  GetNumDoc(File file1,String filePath1){
		WordExtractor doc = null;
		try {
			doc = new WordExtractor(new FileInputStream(file1));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int pages = doc.getSummaryInformation().getPageCount();//总页数
       // System.out.println ("pages=" + pages);
		return pages;
	}
	
	//docx文件页数
	public static int GetNumDocx(File file1,String filePath1){
		  FileInputStream docx = null;
		try {
			docx = new FileInputStream(file1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  XWPFDocument docxfile = null;
		try {
			docxfile = new XWPFDocument(docx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  int xpages = docxfile.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
		return xpages;
		
	}
}
