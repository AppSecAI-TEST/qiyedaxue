/**
 * 
 */
package com.chinamobo.ue.ums.shiro.token;

import javax.servlet.ServletRequest;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 版本: [1.0]
 * 功能说明: 抽象公用Token
 * 作者: WChao 创建时间: 2017年6月27日 下午1:59:16
 */
public abstract class AbstractToken extends UsernamePasswordToken implements TokenProtocol{

	/**
	 */
	private static final long serialVersionUID = 1L;

	public AbstractToken(){
		//默认构造器;
	};
	public AbstractToken(final String username, final String password) {
		super(username, password);
	}
	
	public AbstractToken(final String username, final String password,final boolean rememberMe, final String host){
		super(username,password,rememberMe,host);
	}
	/**
	 * 
		 * 功能描述：[抽象创建Token方法]
		 * 创建者：WChao 创建时间: 2017年6月27日 下午2:06:17
		 * @return
		 *
	 */
	protected abstract AbstractToken createToken(ServletRequest request)throws Exception;
}
