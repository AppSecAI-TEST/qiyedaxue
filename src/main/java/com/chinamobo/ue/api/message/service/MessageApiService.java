package com.chinamobo.ue.api.message.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.api.message.dto.MessageDto;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MessageApiService {
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	ObjectMapper mapper = new ObjectMapper();
/**
 * 
 * 功能描述：[消息列表]
 *
 * 创建者：cjx
 * 创建时间: 2016年7月18日 下午4:13:19
 * @param request
 * @param response
 * @return
 */
	public Result eleMyMessagesList(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<Object, Object> queryMap = new HashMap<Object, Object>();
			PageData<MessageDto> page = new PageData<MessageDto>();
			if (request.getParameter("firstIndex") == null) {
				queryMap.put("startNum", 0);
			} else {
				queryMap.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
			}
			if (request.getParameter("pageSize") == null) {
				queryMap.put("endNum", 10);
				page.setPageSize(10);
			} else {
				queryMap.put("endNum", Integer.parseInt(request.getParameter("pageSize")));//(int) queryMap.get("startNum") +
				page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
			}
			String code = request.getParameter("userId");
			queryMap.put("code", code);
			int count = tomMessagesMapper.selectByCount(code);
			List<MessageDto> list = tomMessagesMapper.selectByCode(queryMap);
			page.setCount(count);
			page.setDate(list);
			page.setPageNum((int) queryMap.get("startNum") / (int) queryMap.get("endNum") + 1);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String pageJson = mapper.writeValueAsString(page);
			for (MessageDto message : list) {
				pageJson = pageJson.replaceAll(String.valueOf(message.getCreateTime().getTime()),
						"\"" + format.format(message.getCreateTime()) + "\"");
			}
			pageJson = pageJson.replaceAll(":null", ":\"\"");
			return new Result("Y", pageJson, ErrorCode.SUCCESS_CODE, "");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "系统繁忙！");
		}
	}
/**
 * 
 * 功能描述：[消息详情]
 *
 * 创建者：cjx
 * 创建时间: 2016年7月18日 下午4:13:33
 * @param request
 * @param response
 * @return
 */
	@Transactional
	public Result eleMyMessageDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<Object, Object> map = new HashMap<Object, Object>();
			int id = Integer.parseInt(request.getParameter("messageId"));
			map.put("userId", request.getParameter("userId"));
			map.put("messageId", Integer.parseInt(request.getParameter("messageId")));
			TomMessagesEmployees selectStatus = tomMessagesEmployeesMapper.selectStatus(map);
			if (null != selectStatus.getIsView() && "N".equals(selectStatus.getIsView())) {
				tomMessagesEmployeesMapper.updateStatus(map);
				TomMessages messageCount = tomMessagesMapper.selectByPrimaryKey(id);
				Integer viewCount = messageCount.getViewCount();
				viewCount = viewCount + 1;
				messageCount.setViewCount(viewCount);
				tomMessagesMapper.updateByPrimaryKeySelective(messageCount);
			}
			TomMessages selectByPrimaryKey = tomMessagesMapper.selectByPrimaryKey(id);
			String pageJson = mapper.writeValueAsString(selectByPrimaryKey);
			pageJson = pageJson.replaceAll(":null", ":\"\"");
			return new Result("Y", pageJson, ErrorCode.SUCCESS_CODE, "");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "系统繁忙！");
		}
	}
	/**
	 * 
	 * 功能描述：[是否有新消息]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年7月18日 下午4:14:51
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 public Result newMessage(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 DBContextHolder.setDbType(DBContextHolder.SLAVE);
		 String userId = request.getParameter("userId");
		 int countByView = tomMessagesEmployeesMapper.countByView(userId);
			Map<Object, Object> map = new HashMap<Object, Object>();
		 if(countByView==0){
			 map.put("newMessage", "N");
			 map.put("messageNum", countByView);
			 String pageJson = mapper.writeValueAsString(map);
				return new Result("Y", pageJson, ErrorCode.SUCCESS_CODE, "");
		 }else{
			 map.put("newMessage", "Y");
			 map.put("messageNum", countByView);
			 String pageJson = mapper.writeValueAsString(map);
				return new Result("Y", pageJson, ErrorCode.SUCCESS_CODE, "");
		 }	 
	 }
}
