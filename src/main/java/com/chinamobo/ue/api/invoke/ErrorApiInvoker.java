/**
 * 
 */
package com.chinamobo.ue.api.invoke;

import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;

/**
 * 
 * 异常API调用者;
 * @author WChao 2017年6月9日
 *
 */
public class ErrorApiInvoker extends AbstractApiInvoker {

	@Override
	public void doInvoke(ApiContext context) throws Exception {
		 if(context.getResult() == null){
			 context.setResult(new Result("N",""));
		 }
		 if("Y".equals(context.getResult().getStatus()))
			 return;
		 ApiInvokerManager listened = (ApiInvokerManager)context.getInvokerListed();
		 boolean isContainsApiType = false;
		 for(ApiInvoker apiInvoker : listened.getInvokers()){
			 if(apiInvoker.Type().equals(context.getValue(ApiContext.API_TYPE))){
				 isContainsApiType = true;
				 break;
			 }
		 }
		 if(!isContainsApiType){
			 context.setResult(new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),"apiType不正确"));
			 return;
		 }
		 if("N".equals(context.getResult().getStatus())){
			 return;
		 }else{
			 context.getResult().setStatus("Y");
		 }
	}

	@Override
	public String Type() {
		return "error";
	}

}
