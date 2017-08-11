/**
 * 
 */
package com.chinamobo.ue.ums.shiro.token;

/**
 * 版本: [1.0]
 * 功能说明: Token协议定义;
 * 作者: WChao 创建时间: 2017年6月27日 下午2:10:41
 */
public interface TokenProtocol {
	
	public static final String DEFAULT_ENCODE_ISO = "ISO-8859-1";
	public static final String DEFAULT_ENCODE_GBK = "gbk";
	public static final String DEFAULT_ENCODE_UTF8 = "utf-8";
	
	public static final String DEFAULT_AUTH_BASIC = "Basic ";
	public static final String DEFAULT_AUTH_HEADER = "Authorization";
	public static final String DEFAULT_AUTH_PARAM = "token";
	public static final String DEFAULT_USER_NAME_PARAM = "userName";
	public static final String DEFAULT_USER_ID_PARAM = "userId";
	public static final String DEFAULT_USER_ROLE_TYPE_PARAM = "userRoleType";
	public static final String DEFAULT_ORGCODE_PARAM = "orgCode";
	public static final String DEFAULT_USER_ROLE_TYPE= "1";
	public static final String DEFAULT_AUTH_USER_SEP = "|";
	public static final String DEFAULT_USER_ID_NAME_SEP = ":";
	
	public static final String INNER_USER_TYPE = "1";
	public static final String API_USER_TYPE = "2";
}
