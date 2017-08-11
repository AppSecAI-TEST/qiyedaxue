package com.chinamobo.ue.utils;

import java.util.List;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 *
 * 作者: WangLg
 * 创建时间: 2016年3月3日 下午8:07:58
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {

    /**
     * 是否包含字符串
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs){
    	if (str != null){
        	for (String s : strs){
        		if (str.equals(trim(s))){
        			return true;
        		}
        	}
    	}
    	return false;
    }

    /**
     * 
     * 功能描述：[String List to String]
     *
     * 创建者：wjx
     * 创建时间: 2016年5月4日 下午2:34:24
     * @param stringList
     * @return
     */
    public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
    
}
