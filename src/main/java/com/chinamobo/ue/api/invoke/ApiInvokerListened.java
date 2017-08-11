/**
 * 
 */
package com.chinamobo.ue.api.invoke;

/**
 * @author WChao
 * 抽象API被调用者接口
 */
public interface ApiInvokerListened {
	
	public void addApiInvoker(ApiInvoker invoker);
	
	public void removeApiInvoker(ApiInvoker invoker);
	
	public void notifyApiInvokers(ApiContext context)throws Exception;
	
}
