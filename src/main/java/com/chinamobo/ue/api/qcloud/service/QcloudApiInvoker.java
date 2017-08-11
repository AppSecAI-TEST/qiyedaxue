/**
 * 
 */
package com.chinamobo.ue.api.qcloud.service;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.api.invoke.AbstractApiInvoker;
import com.chinamobo.ue.api.invoke.ApiContext;
import com.chinamobo.ue.api.invoke.HttpProtocol;
import com.chinamobo.ue.api.result.Result;

/**
 * QcloudAPI调用;
 * @author WChao 2017年6月9日
 *
 */
public class QcloudApiInvoker extends AbstractApiInvoker {
	
	@Autowired
	private ApiQcloudService qcloudService;
	
	@Override
	public void doInvoke(ApiContext context)throws Exception{
		if(!Type().equals(context.getValue(ApiContext.API_TYPE)))
			return;
		Result result = null;
		if(HttpProtocol.GET.equals(context.getHttpMethod())){
			Method method= qcloudService.getClass().getDeclaredMethod(String.valueOf(context.getValue(ApiContext.API_NAME)),new Class[]{HttpServletRequest.class,HttpServletResponse.class});
			result = (Result)method.invoke(qcloudService, context.getRequest(),context.getResponse());
		}else if(HttpProtocol.POST.equals(context.getHttpMethod())){
			Method method= qcloudService.getClass().getDeclaredMethod(String.valueOf(context.getValue(ApiContext.API_NAME)),new Class[]{String.class});
			result = (Result)method.invoke(qcloudService, context.getJsonData().toJSONString());
		}
		context.setResult(result);
	}

	@Override
	public String Type() {
		
		return "qcloud";
	}

}
