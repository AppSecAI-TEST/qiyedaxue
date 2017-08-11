/**
 * 
 */
package com.chinamobo.ue.api.invoke;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author WChao 2017年6月9日
 *
 */
public class ApiInvokerManager implements ApiInvokerListened {

	private List<ApiInvoker> invokers = new ArrayList<ApiInvoker>();
	
	@Override
	public void addApiInvoker(ApiInvoker invoker) {
		invokers.add(invoker);
	}

	@Override
	public void removeApiInvoker(ApiInvoker invoker) {
		invokers.remove(invoker);
	}

	@Override
	public void notifyApiInvokers(ApiContext context)throws Exception{
		context.setInvokerListed(this);
		for(ApiInvoker apiInvoker : invokers){
			apiInvoker.InvokeApiMethod(context);
		}
	}

	public List<ApiInvoker> getInvokers() {
		return invokers;
	}

	public void setInvokers(List<ApiInvoker> invokers) {
		this.invokers = invokers;
	}
}
