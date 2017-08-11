package com.chinamobo.ue.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
/**
 * 
 * 版本: [1.0]
 * 功能说明: AES加解密工具类，视频文件对截取文件流进行加密，解密截取文件流需填充密钥长度16否则无法解密。
 *
 * 作者: wjx
 * 创建时间: 2016年3月25日 下午4:12:46
 */
public class AESEcode {

	/**
	 * 
	 * 功能描述：[加密算法]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月25日 下午4:11:03
	 * @param bytes
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encode(byte[] bytes,String encryptKey) throws Exception{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(encryptKey.getBytes()));
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		return cipher.doFinal(bytes);
	}
	/**
	 * 
	 * 功能描述：[解密算法]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月25日 下午4:11:12
	 * @param bytes
	 * @param decryptKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode(byte[] bytes,String decryptKey) throws Exception{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(decryptKey.getBytes()));
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		return cipher.doFinal(bytes);
	}
}
