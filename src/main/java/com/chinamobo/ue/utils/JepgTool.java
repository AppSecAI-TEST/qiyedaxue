package com.chinamobo.ue.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/*
 * 
 * 功能描述：按比例生成缩略图
 */
public class JepgTool {
	
	private boolean isInitFlag=false;
	
	private String pic_big_pathfilename=null;
	
	private String pic_small_pathfilename=null;
	
	private int smallpicwidth=0;
	
	private int smallpicheight=0;
	
	private int pic_big_width=0;
	
	private int pic_big_height=0;
	
	private double picscale=0;
	
	public JepgTool(){
		this.isInitFlag=false;
	}
	
	private void resetJpegToolParams(){
		this.picscale=0;
		this.smallpicwidth=0;
		this.smallpicheight=0;
		this.isInitFlag=false;
	}
	//设置缩放比例
	public void SetScale(Double scale) throws JpegToolException{
		if(scale<=0){
			throw new JpegToolException("缩放比例不能为0或者负数");
		}
		
		this.resetJpegToolParams();
		this.picscale=scale;
		this.isInitFlag=true;
	}
	//设置缩放宽度
	public void SetSmallWidth(int smallpicwidth) throws JpegToolException{
		if(smallpicwidth<=0){
			throw new JpegToolException("缩放宽度不能为0或者负数");
		}
		
		this.resetJpegToolParams();
		this.smallpicwidth=smallpicwidth;
		this.isInitFlag=true;
	}
	//设置缩放高度
		public void SetSmallHeight(int smallpicheight) throws JpegToolException{
			if(smallpicheight<=0){
				throw new JpegToolException("缩放高度不能为0或者负数");
			}
			
			this.resetJpegToolParams();
			this.smallpicheight=smallpicheight;
			this.isInitFlag=true;
		}
		//返回大图片路径
		public String getpic_big_pathfilename(){
			return this.pic_big_pathfilename;
		}
		//返回小图片路径
		public String getpic_small_pathfilename(){
					return this.pic_small_pathfilename;
				}
		public int  getsrcw(){
			return this.pic_big_width;
		}
		public int getsrch(){
			return this.pic_big_height;
		}
		
		public void doFinal(String pic_big_pathfilename,String pic_small_pathfilename) throws JpegToolException{
				if(!this.isInitFlag){
					throw new JpegToolException("对象参数没有初始化");
				}
				if(pic_big_pathfilename==null || pic_small_pathfilename==null){
					throw new JpegToolException("包含文件名的路径为空");
				}
				if((!pic_big_pathfilename.toLowerCase().endsWith("jpg"))&&(!pic_big_pathfilename.toLowerCase().endsWith("jpeg"))&&(!pic_big_pathfilename.toLowerCase().endsWith("png"))&&(!pic_big_pathfilename.toLowerCase().endsWith("bmp"))&&(!pic_big_pathfilename.toLowerCase().endsWith("gif"))){
					throw new JpegToolException("只能处理规定的文件类型");
				}
				if((!pic_small_pathfilename.toLowerCase().endsWith("jpg"))&&(!pic_small_pathfilename.toLowerCase().endsWith("jpeg"))&&(!pic_big_pathfilename.toLowerCase().endsWith("png"))&&(!pic_big_pathfilename.toLowerCase().endsWith("bmp"))&&(!pic_big_pathfilename.toLowerCase().endsWith("gif"))){
					throw new JpegToolException("只能处理规定的文件类型");
				}
				this.pic_big_pathfilename=pic_big_pathfilename;
				this.pic_small_pathfilename=pic_small_pathfilename;
				int smallw=0;
				int smallh=0;
				
				File fi=new File(pic_big_pathfilename);
				File fo=new File(pic_small_pathfilename);
				
				AffineTransform transform=new AffineTransform();
				BufferedImage bsrc=null;
				
				try{
					bsrc=ImageIO.read(fi);
				}catch(IOException ex){
					throw new JpegToolException("读取源文件出错");
				}
				this.pic_big_width=bsrc.getWidth();
				this.pic_big_height=bsrc.getHeight();
				double scale=(double)pic_big_width/pic_big_height;
				if(this.smallpicwidth!=0){
					smallw=this.smallpicwidth;
					smallh=(smallw*pic_big_height)/pic_big_width;
				}else if(this.smallpicheight!=0){
					smallh=this.smallpicheight;
					smallw=(smallh*pic_big_width)/pic_big_height;
				}else if(this.picscale!=0){
					smallw=(int) ((float)pic_big_width*this.picscale);
					smallh=(int) ((float)pic_big_height*this.picscale);
				}else{
					throw new JpegToolException("对象参数初始化不正确!!");
				}
				double sx=(double)smallw/pic_big_width;
				double sy=(double)smallh/pic_big_height;
				transform.setToScale(sx, sy);
				AffineTransformOp ato=new AffineTransformOp(transform, null);
				BufferedImage bsmall=new BufferedImage(smallw, smallh, BufferedImage.TYPE_3BYTE_BGR);
				ato.filter(bsrc, bsmall);
				
				try{
					ImageIO.write(bsmall, "jpeg", fo);
				}catch(IOException ex1){
					throw new JpegToolException("写入缩略图文件出错!!");
				}
			}
		
}
