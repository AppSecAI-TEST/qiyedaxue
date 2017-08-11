package com.chinamobo.ue.utils;

import org.apache.commons.codec.binary.Base64;

public class Base {
	 public static String getEncryptString(String originalString){
	        byte[] arr = Base64.encodeBase64(originalString.getBytes(), true);
	        return new String(arr);
	  }
	 
	 public static String getDecryptString(String encryptString){
	        byte[] arr = Base64.decodeBase64(encryptString.getBytes());
	        return new String(arr);
	  }
	 
	public static void main(String[] args) {
		String str="GUOWEI19920529";
        String str1=Base.getEncryptString(str);
        System.out.println("经Base64加密后的密文为"+str1);
        String str2=Base.getDecryptString(str1);
        System.out.println("经Base64解密后的原文为"+str2);
	}
}
