package com.chinamobo.ue.api.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 版本: [1.0]
 * 功能说明: 加密工具类
 *
 * 作者: WangLg
 * 创建时间: 2016年3月10日 下午6:04:01
 */
public class EncryptUtils {
	
	/**
	 * sha加密
	 * @param inputText
	 * @return
	 * @throws JsonProcessingException 
	 */
	public static String sha(Object obj) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper(); 
		String inputText = mapper.writeValueAsString(obj);
		return encrypt(inputText, "sha-1");
	}
	
	/**
	 * md5或者sha-1加密
	 * @param inputText	要加密的内容
	 * @param algorithmName 加密算法名称：md5或者sha-1，不区分大小写
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "sha";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}
	
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

}
