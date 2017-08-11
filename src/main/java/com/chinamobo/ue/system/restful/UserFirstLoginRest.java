package com.chinamobo.ue.system.restful;

import java.util.Random;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.UserLogServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.MessagePost;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;


/**
 * 用来处理用户第一次登录,包括 1 验证手机号 2 发送验证码 3 重置密码
 * 
 * @author zhj
 *
 */

@Path("/userFirstLog")
@Scope("request")
@Component
public class UserFirstLoginRest {
	private static Logger logger = LoggerFactory.getLogger(UserFirstLoginRest.class);

	// 保存手机号
	private static String empPhone = null;

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private UserLogServise userLogServise;

	/**
	 * 发送验证码
	 * 
	 * @author zhj
	 * @param phone
	 * @return
	 */
	@POST
	@Path("/sendCaptchaByPhone")
	public String sendCaptchaByPhone(String jsonString) {
		
		JSONObject object = JSONObject.fromObject(jsonString);
		String phone = object.getString("phone").toString();
		if (phone == null || phone.equals("")) {
			return "{\"result\":\"手机号不能为空!\"}";
		}

		if (!userLogServise.phoneExists(phone)) {
			return "{\"result\":\"该手机号不存在\"}";
		}

		String captcha = generateCaptcha();
		String msg = "您的验证码为:" + captcha;
		
		try {
			String result = MessagePost.putMessage(phone, msg);
			if (result != null) {
				empPhone = phone;
				return "{\"result\":\"验证码已经发送\",\"captcha\":\"" + captcha + "\"}";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return "{\"result\":\"error\"}";
	}

	/**
	 * 生成随机6位数 创建者: zhj 创建者:zhj
	 * 
	 * @return
	 */
	public String generateCaptcha() {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < 6; i++) {
			result += random.nextInt(10);
		}

		return result;
	}

	/**
	 * 更新密码
	 * 
	 * @author zhj
	 * @param password
	 * @param confirmedPwd
	 * @return
	 */
	@POST
	@Path("/updatePassword")
	public String updatePassword(String jsonString) {
		JSONObject object = JSONObject.fromObject(jsonString);
		String password = object.getString("password").toString();
		String phone=object.getString("phone").toString();
		
		if (password == null || password.equals("")) {
			return "{\"result\":\"密码不能为空!\"}";
		}

		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			TomUserLog tomUserLog = userLogServise.selectUserByPhone(phone);
			tomUserLog.setPassword(password);
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			userLogServise.updatePassword(tomUserLog);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return "{\"result\":\"error\"}";
	}

}
