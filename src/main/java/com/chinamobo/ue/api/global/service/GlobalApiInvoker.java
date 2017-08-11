/**
 * 
 */
package com.chinamobo.ue.api.global.service;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.api.invoke.AbstractApiInvoker;
import com.chinamobo.ue.api.invoke.ApiContext;
import com.chinamobo.ue.api.result.Result;

/**
 * 全局信息API调用;
 * @author WChao 2017年6月9日
 *
 */
public class GlobalApiInvoker extends AbstractApiInvoker {
	
	@Autowired
	private ApiGlobalService apiGlobalService;
	
	@Override
	public void doInvoke(ApiContext context)throws Exception{
		if(!Type().equals(context.getValue(ApiContext.API_TYPE)))
			return;
		Method method= apiGlobalService.getClass().getDeclaredMethod(String.valueOf(context.getValue(ApiContext.API_NAME)),new Class[]{HttpServletRequest.class,HttpServletResponse.class});
		Result result = (Result)method.invoke(apiGlobalService, context.getRequest(),context.getResponse());
		context.setResult(result);
	}

	@Override
	public String Type() {
		
		return "global";
	}

}
