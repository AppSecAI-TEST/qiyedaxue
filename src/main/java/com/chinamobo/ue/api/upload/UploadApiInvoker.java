/**
 * 
 */
package com.chinamobo.ue.api.upload;

import java.lang.reflect.Method;


import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.api.invoke.AbstractApiInvoker;
import com.chinamobo.ue.api.invoke.ApiContext;
import com.chinamobo.ue.api.invoke.HttpProtocol;
import com.chinamobo.ue.api.result.Result;

/**
 * 上传文件API调用;
 * @author WChao 2017年6月9日
 *
 */
public class UploadApiInvoker extends AbstractApiInvoker {

	@Autowired
	private UploadService uploadService;
	
	@Override
	public String Type() {
		return "upload";
	}

	@Override
	public void doInvoke(ApiContext context) throws Exception {
		if(!Type().equals(context.getValue(ApiContext.API_TYPE)))
			return;
		Result result = null;
	    if(HttpProtocol.POST.equals(context.getHttpMethod())){
			Method method= uploadService.getClass().getDeclaredMethod(String.valueOf(context.getValue(ApiContext.API_NAME)),new Class[]{String.class});
			result = (Result)method.invoke(uploadService, context.getJsonData().toJSONString());
		}
		context.setResult(result);
	}

}
