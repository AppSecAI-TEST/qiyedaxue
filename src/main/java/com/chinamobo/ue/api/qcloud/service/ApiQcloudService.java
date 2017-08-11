package com.chinamobo.ue.api.qcloud.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.api.qcloud.dto.qcloudResult;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.course.dao.TomCourseSectionMapper;
import com.chinamobo.ue.qcloud.OssService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.DownloadAndUploadUtil;
import com.chinamobo.ue.utils.JsonMapper;
import com.qcloud.cosapi.sign.Sign;

@Service
public class ApiQcloudService {
	@Autowired

	private TomCourseSectionMapper sectionMapper;

	private static Logger logger = LoggerFactory.getLogger(ApiQcloudService.class);

	/**
	 * 功能描述：[5.24 视频点播]
	 * 
	 * 创建者：LG
	 * 
	 * 创建时间：2016.6.1 11：26
	 * 
	 * @param request
	 * @return
	 */

	public Result eleQcloudMedia(HttpServletRequest request, HttpServletResponse response) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		JsonMapper mapper = new JsonMapper();
		qcloudResult res = new qcloudResult();
		try {
			String courseSectionId = request.getParameter("courseSectionId");

			if (null == courseSectionId || "".equals(courseSectionId)) {
				return new Result("N", "", String.valueOf(ErrorCode.PARAM_MISS), "非法参数！");
			}
			map.put("courseSectionId", Integer.parseInt(courseSectionId));

			res = sectionMapper.selectBySectionId(map);
			String filepath = res.getSectionAddress();
			String url = null;
			if (Config.getProperty("cosStatus", "true").equals("true")) {
				DownloadAndUploadUtil down = new DownloadAndUploadUtil();
//				System.out.println(down.expired);
				String sign = Sign.appSignature(down.APP_ID, down.SECRET_ID, down.SECRET_KEY, down.expired,
						down.bucketName);
				// 视频示例地址："http://upload-10040296.file.myqcloud.com"
				url = "http://" + down.bucketName + "-" + down.APP_ID + ".file.myqcloud.com" + filepath + "?sign="
						+ sign;
			} else {
				OssService oss=new OssService();
				url = oss.getUrl(filepath);
			}
			String json = mapper.toJson(url);
			return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "系统繁忙！");
		}
	}

}
