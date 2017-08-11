package com.chinamobo.ue.api.result;

import java.io.Serializable;


/**
 * 版本: [1.0]
 * 功能说明: 提供给API接口返回信息
 * 
 * 作者: WangLg
 * 创建时间: 2016年3月9日 下午4:27:11
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String status;//	状态信息	Y表示正确,N表示错误
	protected String results;//	返回信息	正确返回接口数据
	protected String error_code;//	错误代码1返回空字符串    2 错误代码	
	protected String error_msg;//	错误信息	返回1空字符串	2错误信息
	
	public Result() {
	}

	/**
	 * 版本: [1.0]
	 * 说明: 只返回接状态码和错误信息
	 *
	 * 作者: WangLg
	 * 创建时间: 2016年3月9日 下午4:35:03
	 * @param status
	 * @param error_msg
	 */
	public Result(String status, String error_msg) {
		super();
		this.status = status;
		this.error_msg = error_msg;
	}

	/**
	 * 版本: [1.0]
	 * 说明: 返回所有信息
	 *
	 * 作者: WangLg
	 * 创建时间: 2016年3月9日 下午4:35:32
	 * @param status
	 * @param results
	 * @param error_code
	 * @param error_msg
	 */
	public Result(String status, String results, String error_code, String error_msg) {
		super();
		this.status = status;
		this.results = results;
		this.error_code = error_code;
		this.error_msg = error_msg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public String toJSONString() {
		if (results == null || "".equals(results)) {
			return "{\"status\":\"" + status + "\", \"results\":\"" + results +"\", \"error_code\":\"" + error_code + "\", \"error_msg\":\""
					+ error_msg + "\"}";
		}else{
			return "{\"status\":\"" + status + "\", \"results\":" + results +", \"error_code\":\"" + error_code + "\", \"error_msg\":\""
					+ error_msg + "\"}";
		}
	}
	
}
