package com.chinamobo.ue.utils;

public class JpegToolException extends Exception {
	private String errMsg="";
	public  JpegToolException(String errMsg){
		this.errMsg=errMsg;
	}
	public String getErrMsg() {
		return "JpegToolException:"+this.errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


}
