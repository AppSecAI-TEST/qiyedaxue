package com.chinamobo.ue.common.restful;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.chinamobo.ue.common.entity.UploadResult;
import com.chinamobo.ue.exam.service.QuestionBankService;
import com.chinamobo.ue.qcloud.OssService;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.DecompressionZip;
import com.chinamobo.ue.utils.DownloadAndUploadUtil;
import com.chinamobo.ue.utils.GetPagenumDoc;
import com.chinamobo.ue.utils.JepgTool;
import com.chinamobo.ue.utils.LibreofficeUtil;
import com.chinamobo.ue.utils.PPTUtil;
import com.chinamobo.ue.utils.PdfUtil;
import com.chinamobo.ue.utils.ThumbNail;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

@Path("/file")
@Scope("request")
@Component
public class FileRest {
	
	@Autowired
	private QuestionBankService questionBankService;

	private static Logger logger = LoggerFactory.getLogger(FileRest.class);
	// 操作json的对象
	ObjectMapper MAPPER = new ObjectMapper();

	// 图片格式
	private static final String IMAGE_TYPE[] = { ".jpg", ".jpeg", ".png", ".bmp", ".gif" };
	private static final String PDF_TYPE[] = { ".pdf" };
	private static final String PPT_TYPE[] = { ".ppt", ".pptx" };
	private static final String ELSE_TYPE[] = { ".xls", ".xlsx",  ".txt" };
	private static final String video_TYPE[] = { ".mp4" };
	private static final String  ZIP_TYPE[]={".zip"};
	private static final String DOC_TYPE[]={".doc",".docx"};
	private static final String AUDIO_TYPE[]={".mp3"};
	
	private static final String isLinux = Config.getProperty("isLinux");

	String basePath1 = Config.getProperty("file_path");
	String basePath2 = Config.getProperty("extra_path");
	// String fileFlag=Config.getProperty("file_flag");
	String backetName = Config.getProperty("bucketName");
	String appId = Config.getProperty("APP_ID");
	// "http://upload-10040296.file.myqcloud.com"
	String requestpath = "http://" + backetName + "-" + appId + ".file.myqcloud.com";
	ObjectMapper mapper = new ObjectMapper();

	@POST
	@Path("/upload/{belong}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String upload(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition disposition, @PathParam("belong") String belong,
			@QueryParam("localPath") String localPath, @QueryParam("time") int time,
			@Context HttpServletRequest request) throws Exception {
		// String
		// rootPath=request.getSession().getServletContext().getRealPath("/");
		String suffix = "";
		UploadResult uploadResult = new UploadResult();
		// 取得文件名后缀
		if (disposition != null && disposition.getFileName() != null) {
			String[] ss = disposition.getFileName().split("\\.");
			suffix = "." + ss[ss.length - 1];
		}
	
		int type = 0;
		Boolean flag = false;
		// 检查文件后缀是否正确
		for (String suffix1 : IMAGE_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type = 1;
				break;
			}
		}
		for (String suffix1 : PDF_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type = 2;
				break;
			}
		}
		for (String suffix1 : PPT_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type = 3;
				break;
			}
		}
		for (String suffix1 : ELSE_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type = 4;
				break;
			}
		}
		for (String suffix1 : video_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type = 5;
				break;
			}
		}
		for (String suffix1 : ZIP_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type = 6;
				break;
			}
		}
		for (String suffix1 : DOC_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type = 7;
				break;
			}
		}
		for (String suffix1 : AUDIO_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type =8;
				break;
			}
		}
	

		// 上传失败
		if (type < 1 || type >8) {
			return "{\"result\":\"error\"}";
		}
		// 获取保存文件服务器的地址
		String[] fileAndFolder = getFilePath(suffix, belong, type);
		// 文件保存地址（带按规则生成后的文件名）
		String filePath1 = fileAndFolder[1];
		File file1 = new File(filePath1);

		// 把文件保存到服务器
		OutputStream out1 = new FileOutputStream(filePath1);
		FileCopyUtils.copy(fileInputStream, out1);

		if (type == 1) {
			try {
				// 通过上传的文件是否有宽高判断是否是图片文件
				BufferedImage image = ImageIO.read(file1);
				if (image != null) {
					String  height = String.valueOf(image.getHeight());
					String Width = String.valueOf(image.getWidth());
				/*	ThumbNail thumbnail = new ThumbNail();
					thumbnail.thumbnailImage(filePath1, 200, 200,true);*/
					JepgTool JepgTool=new JepgTool();
					JepgTool.SetScale(0.5);
					JepgTool.doFinal(filePath1,  fileAndFolder[0]+"//thumb_"+fileAndFolder[4]);
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 如果校验失败，删除上传的图片
			if (!flag) {
				file1.delete();
				return "{\"result\":\"error\"}";
			}
		} else if (type == 2) {
			String pdfPath = filePath1;
			// 获取按规则生成后的文件夹名用来存放pdf转化后的图片
			filePath1 = fileAndFolder[0];
			// 转换并得到图片数量
			uploadResult.setNum(PdfUtil.PdfToJpg(pdfPath, filePath1));
			if ("true".equals(isLinux)) {
				LibreofficeUtil.trans(fileAndFolder[4], filePath1, suffix,uploadResult.getNum());
			}
		} else if (type == 3) {
			// 获取按规则生成后的文件夹名用来存放ppt转化后的图片
			filePath1 = fileAndFolder[0];
			// 转换并得到图片数量
			if (suffix.equals(".ppt")) {
				uploadResult.setNum(PPTUtil.PPTtoImage(file1, filePath1));
			} else if (suffix.equals(".pptx")) {
				uploadResult.setNum(PPTUtil.PPTXtoImage(file1, filePath1));
			}
			if ("true".equals(isLinux)) {
				LibreofficeUtil.trans(fileAndFolder[4], filePath1, suffix,uploadResult.getNum());
			}
		} else if (type == 5) {
			Encoder encoder = new Encoder();
			MultimediaInfo m = encoder.getInfo(file1);
			long ls = m.getDuration();
			// 得到视频时长
			uploadResult.setNum((int) ls / 1000);
			if (Config.getProperty("cosStatus", "true").equals("true")) {
				DownloadAndUploadUtil upAnddown = new DownloadAndUploadUtil();
				filePath1 = upAnddown.upLoad(filePath1);
			} else {
				OssService oss = new OssService();
				filePath1 = oss.upload(filePath1);
			}
		}else if(type==6){
			DecompressionZip decZip=new DecompressionZip();
//			filePath1=decZip.demoZip(filePath1,fileAndFolder[0]);
			uploadResult.setNum(decZip.demoZip(filePath1, fileAndFolder[0]));
			filePath1=fileAndFolder[0];
		}else if(type==7){
				filePath1 = fileAndFolder[0];
				if(suffix.equals(".doc")){
					uploadResult.setNum(GetPagenumDoc.GetNumDoc(file1,filePath1));
				}else if(suffix.equals(".docx")){
					uploadResult.setNum(GetPagenumDoc.GetNumDocx(file1,filePath1));
				}
				if ("true".equals(isLinux)) {
					LibreofficeUtil.trans(fileAndFolder[4], filePath1, suffix,uploadResult.getNum());
				}
			}else if(type==8){
				Encoder encoder = new Encoder();
				MultimediaInfo m = encoder.getInfo(file1);
				long ls = m.getDuration();
				// 得到视频时长
				uploadResult.setNum((int) ls / 1000);
			}
		
		if (type != 5) {
			// 通过所有校验，设置error
			uploadResult.setUrl(StringUtils.replace(StringUtils.substringAfter(filePath1, basePath1), "\\", "/"));
		} else if (Config.getProperty("cosStatus", "true").equals("true")) {
			uploadResult.setUrl(StringUtils.substringAfter(filePath1, requestpath));
		} else {
			uploadResult.setUrl(filePath1);
		}
		String json = mapper.writeValueAsString(uploadResult);
		return json;

	}

	/**
	 * 
	 * 功能描述：[富文本编辑器上传]
	 *
	 * 创建者：JCX 创建时间: 2016年6月27日 下午1:39:23
	 * 
	 * @param fileInputStream
	 * @param disposition
	 * @param belong
	 * @param localPath
	 * @param time
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/editorUpload/{belong}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String editorUpload(@FormDataParam("upload") InputStream fileInputStream,
			@FormDataParam("upload") FormDataContentDisposition disposition, @PathParam("belong") String belong,
			@QueryParam("localPath") String localPath, @QueryParam("time") int time,
			@Context HttpServletRequest request) throws Exception {
		// String
		// rootPath=request.getSession().getServletContext().getRealPath("/");
		String suffix = "";
		// String scheme=request.getScheme();
		// String serverName=request.getServerName();
		// int port=request.getServerPort();
		if (disposition != null && disposition.getFileName() != null) {
			String[] ss = disposition.getFileName().split("\\.");
			suffix = "." + ss[ss.length - 1];
		}

		int type = 0;
		Boolean flag = false;
		// 检查文件后缀是否正确
		for (String suffix1 : IMAGE_TYPE) {
			if (StringUtils.endsWithIgnoreCase(suffix, suffix1)) {
				type = 1;
				break;
			}
		}

		// 上传失败
		if (type != 1) {
			return "{\"result\":\"error\"}";
		}
		// 获取保存文件服务器的地址
		String[] fileAndFolder = getFilePath(suffix, belong, type);
		String filePath1 = fileAndFolder[1];
		File file1 = new File(filePath1);
		// if (type!=5) {

		// 把文件保存到服务器
		OutputStream out1 = new FileOutputStream(filePath1);
		//
		FileCopyUtils.copy(fileInputStream, out1);
		// }

		try {
			// 通过上传的文件是否有宽高判断是否是图片文件
			BufferedImage image = ImageIO.read(file1);
			if (image != null) {
				String height = String.valueOf(image.getHeight());
				String Width = String.valueOf(image.getWidth());
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果校验失败，删除上传的图片
		if (!flag) {
			file1.delete();
			return "{\"result\":\"error\"}";
		}
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ "/enterpriseuniversity/"
				+ StringUtils.replace(StringUtils.substringAfter(filePath1, basePath1), "\\", "/");
		String result = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + 1 + ", '" + path
				+ "' , '');</script>";
		return result;
	}

	/**
	 * 
	 * 功能描述：[生成文件名]
	 *
	 * 创建者：JCX 创建时间: 2016年7月18日 上午10:07:30
	 * 
	 * @param originalFilename
	 * @param fileBelong
	 * @param type
	 * @return
	 */
	public String[] getFilePath(String originalFilename, String fileBelong, int type) {

		String baseFolder1 = basePath1 + "file/" + fileBelong;
		String baseFolder2 = basePath2 + "file/" + fileBelong;
		DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		DateFormat format2 = new SimpleDateFormat("HHmmssSSS");
		String fileFolder1;
		String fileFolder2;
		if (type == 1 || type == 4 ) {
			// 视频图片文档直接存储，生成当前日期的文件夹
			fileFolder1 = baseFolder1 + "/" + format1.format(new Date());
			fileFolder2 = baseFolder2 + "/" + format1.format(new Date());
		} else if(type==5){
			fileFolder1 = baseFolder1 +"/videoBack";
			fileFolder2= baseFolder1 +"/videoBack";
		} else if(type==8){
			fileFolder1 = baseFolder1 +"/musicBack";
			fileFolder2= baseFolder1 +"/musicBack";
		}else {
			// 对ppt，pdf生成当前日期的文件夹，并在文件夹下生成存放图片的文件夹
			fileFolder1 = baseFolder1 + "/" + format1.format(new Date()) + "/" + "imgs" + format2.format(new Date())
					+ RandomUtils.nextInt(1000, 9999);
			fileFolder2 = baseFolder2 + "/" + format1.format(new Date()) + "/" + "imgs" + format2.format(new Date())
					+ RandomUtils.nextInt(1000, 9999);
		}

		File file1 = new File(fileFolder1);

		// 文件夹不存在创建文件夹
		if (!file1.isDirectory()) {
			file1.mkdirs();
		}
		// if ("true".equals(fileFlag)) {
		// File file2 = new File(fileFolder2);
		// if (!file2.isDirectory()) {
		// file2.mkdirs();
		// }
		// }
		// 拼接文件名
		String fileName = format2.format(new Date()) + RandomUtils.nextInt(100, 999) + "."
				+ StringUtils.substringAfterLast(originalFilename, ".");
		// 直接存储使用ss[0],ss[2]作为带文件名的路径；转化成多个图片使用ss[1],ss[3]作为源文件路径，ss[0],ss[2]作为图片文件夹；ss[4]为文件名
		String[] ss = { fileFolder1, fileFolder1 + "/" + fileName, fileFolder2, fileFolder2 + "/" + fileName,
				fileName };
		return ss;
	}

	/**
	 * 
	 * 功能描述：[文件下载]
	 *
	 * 创建者：wjx 创建时间: 2016年3月25日 下午8:48:38
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	@GET
	@Path("/download")
	public Response fileDownload(@QueryParam("filePath") String filePath, @QueryParam("fileName") String fileName) {
		try {
			String path = Config.getProperty("file_path");
			if (fileName == null) {
				fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			}
			if (!filePath.startsWith("file") || filePath.indexOf("..") != -1) {
				return null;
			}
			File file = new File(path + filePath);
			ResponseBuilder response = Response.ok((Object) file);
			String downFileName = new String(fileName.getBytes(), "ISO-8859-1");
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition", "attachment; filename=\"" + downFileName + "\"");
			return response.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 功能描述：[删除文件]
	 *
	 * 创建者：cjx 创建时间: 2016年5月4日 下午6:09:04
	 * 
	 * @param path
	 * @return
	 */
	@GET
	@Path("/deleteFile")
	public String deleteFile(@QueryParam("path") String path) {

		if(!path.isEmpty() && path.indexOf("..") == -1){
			if(path.indexOf(".mp4")!=-1){
				OssService oss=new OssService();
				oss.delete(path);
				return "success";
			}
			File file = new File(basePath1 + path);
			// 判断目录或文件是否存在
			if (!file.exists()) { // 不存在返回 false
				return "{\"result\":\"error\"}";
			} else {
				deleteDir(file);
				return "success";
			}
		}else {
			return "{\"result\":\"File does not exist!\"}";
		}
	}

	/**
	 * 
	 * 功能描述：[递归删除文件]
	 *
	 * 创建者：wjx 创建时间: 2016年9月6日 上午10:18:01
	 * 
	 * @param dir
	 * @return
	 */
	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
}
