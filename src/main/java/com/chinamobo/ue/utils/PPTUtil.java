package com.chinamobo.ue.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PPTUtil {
	public static int PPTtoImage(File file,String filePath){ 
	        try {  
	  
	            FileInputStream is = new FileInputStream(file);  
	            SlideShow ppt = new SlideShow(is);  
	            is.close();  
	            Dimension pgsize = ppt.getPageSize();  
	            org.apache.poi.hslf.model.Slide[] slide = ppt.getSlides();  
	            for (int i = 0; i < slide.length; i++) {  
//	                System.out.print("第" + (i+1) + "页。");  
	                  
	                TextRun[] truns = slide[i].getTextRuns();     
	                for ( int k=0;k<truns.length;k++){     
	                   RichTextRun[] rtruns = truns[k].getRichTextRuns();     
	                  for(int l=0;l<rtruns.length;l++){     
	                       int index = rtruns[l].getFontIndex();     
	                        String name = rtruns[l].getFontName();               
	                        rtruns[l].setFontIndex(1);     
	                        rtruns[l].setFontName(new String("宋体".getBytes(),"utf-8"));   
//	                        System.out.println(rtruns[l].getFontName());
	                   }     
	                }     
	                BufferedImage img = new BufferedImage(pgsize.width,pgsize.height, BufferedImage.TYPE_INT_RGB);  
	  
	                Graphics2D graphics = img.createGraphics();  
	                graphics.setPaint(Color.white);  
	                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));  
	                slide[i].draw(graphics);  
	  
	                // 这里设置图片的存放路径和图片的格式(jpeg,png,bmp等等),注意生成文件路径  
	                FileOutputStream out = new FileOutputStream(filePath+"/" + (i+1) + ".jpg");  
	                javax.imageio.ImageIO.write(img, "jpeg", out);  
	                out.close();  
	  
	            }  
	            System.out.println("\nsuccess!!");  
	            return slide.length;  
	        } catch (FileNotFoundException e) {  
	            System.out.println(e);  
	            // System.out.println("Can't find the image!");  
	        } catch (IOException e) {  
        }
			return 0;
	}
	
	public static int PPTXtoImage(File file,String filePath){ 
		try {

			FileInputStream is = new FileInputStream(file);
			XMLSlideShow xmlSlideShow = new XMLSlideShow(is);
			XSLFSlide[] xslfSlides = xmlSlideShow.getSlides();
			Dimension pageSize = xmlSlideShow.getPageSize();
			is.close();
			int num= xslfSlides.length;  
			for (int i = 0; i < xslfSlides.length; i++) {
				for(XSLFShape shape : xslfSlides[i].getShapes()){
					if(shape instanceof XSLFTextShape){
						XSLFTextShape textShape = (XSLFTextShape) shape;
						for(XSLFTextParagraph textPar:textShape.getTextParagraphs()){
							List<XSLFTextRun> runList=textPar.getTextRuns();
							for(XSLFTextRun run:runList){
								run.setFontFamily(new String("宋体".getBytes(),"utf-8"));
							}
						}
					}
				}
//				System.out.println("第" + (i+1) + "页。");
				BufferedImage img = new BufferedImage(pageSize.width,pageSize.height,
						BufferedImage.TYPE_INT_RGB);

				Graphics2D graphics = img.createGraphics();
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0,pageSize.width, pageSize.height));
				xslfSlides[i].draw(graphics);

				FileOutputStream out = new FileOutputStream(filePath+"/" + (i+1) + ".jpg");
				javax.imageio.ImageIO.write(img, "jpeg", out);
				out.close();
			}
			return num;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
