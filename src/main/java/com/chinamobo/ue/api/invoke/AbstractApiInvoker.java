/**
 * 
 */
package com.chinamobo.ue.api.invoke;

/**
 * 版本: [1.0]
 * 功能说明: 抽象API调用者类;
 * 作者: WChao 创建时间: 2017年6月16日 上午9:34:29
 */
public abstract class AbstractApiInvoker implements ApiInvoker {
	
	protected ApiContext context;
	
	@Override
	public void InvokeApiMethod(ApiContext context) throws Exception {
		this.context = context;
		doInvoke(context);
	}
	
	public abstract void doInvoke(ApiContext context)throws Exception;

}
