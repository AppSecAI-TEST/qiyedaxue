/**
 * 
 */
package com.chinamobo.ue.api.invoke;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.RequestUtil;
import com.chinamobo.ue.utils.PathUtil;

/**
 * @author WChao
 * API操作上下文;
 *
 */
public class ApiContext {
	
	public static final String API_NAME = "apiName";//API方法名;
	public static final String API_TYPE = "apiType";//API类型;(course、exam、myinfo、...)
	public static final String CALLBACKE = "callback";//回调前端;
	public static final String USER_ID = "userId";//用户标识;
	public static final String TOKEN = "token";//用户凭证Token;
	
	private ApiInvokerListened invokerListed;//执行者被监听对象;
	private Map<String,Object> params = null;//请求参数相关信息;
	private HttpServletRequest request = null;//request请求对象;
	private HttpServletResponse response = null;//response响应对象;
	private JSONObject jsonData;//post数据;
	private String httpMethod = HttpProtocol.GET;//HTTP请求方式;
	private Result result = new Result();//返回结果集;
	
	public ApiContext(){}
	
	public ApiContext(HttpServletRequest request , HttpServletResponse response){
		this.init(request, response);
	}
	public ApiContext(HttpServletRequest request , HttpServletResponse response , String httpMethod){
		this(request, response);
		this.httpMethod = httpMethod;
	}
	
	public ApiContext init(HttpServletRequest request , HttpServletResponse response){
		this.params = RequestUtil.getParams(request);
		this.params.put(API_NAME, PathUtil.camelName(String.valueOf(params.get(API_NAME))));
		this.request = request;
		this.response = response;
		return this;
	}

	public Object getValue(String name){
		return this.params.get(name) == null ? jsonData == null ? "" : jsonData.get(name) == null ? "" : jsonData.get(name) : this.params.get(name);
	}
	
	public Map<String,Object> putValue(String name , Object value){
		this.params.put(name, value);
		return this.params;
	}
	
	public Map<String,Object> removeValue(String name){
		this.params.remove(name);
		return this.params;
	}
	
	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public ApiInvokerListened getInvokerListed() {
		return invokerListed;
	}

	public void setInvokerListed(ApiInvokerListened invokerListed) {
		this.invokerListed = invokerListed;
	}

	public JSONObject getJsonData() {
		return jsonData;
	}

	public void setJsonData(JSONObject jsonData) {
		this.jsonData = jsonData;
		if(HttpProtocol.POST.equals("post")){//设置POST请求参数数据;
			if(this.jsonData != null){
				this.params.putAll(jsonData);
			}
		}
		this.params.put(API_NAME, PathUtil.camelName(String.valueOf(params.get(API_NAME))));
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	
}
