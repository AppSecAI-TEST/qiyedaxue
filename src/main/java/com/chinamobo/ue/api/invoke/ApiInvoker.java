/**
 * 
 */
package com.chinamobo.ue.api.invoke;

/**
 * @author WChao
 * 抽象API方法
 */
public interface ApiInvoker {
	/**
	 * Api类型由子类指定;
	 * @return
	 */
	public String Type();
	/**
	 * 子类执行API具体逻辑调用方法;
	 * @param context
	 * @throws Exception
	 */
	public void InvokeApiMethod(ApiContext context)throws Exception;
	
}
