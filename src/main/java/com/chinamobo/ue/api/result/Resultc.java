package com.chinamobo.ue.api.result;
/**
 * 
 * @ClassName: Results 
 * @Description: 提供证书相关接口返回信息 
 * @author Acemon
 * @date 2017年6月20日 下午1:50:50
 */
public class Resultc extends Result{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean confer;
	private String certificateInf;
	public Resultc() {
	}
	public Resultc(String status, String results, String error_code,String error_msg,boolean confer,String certificateInf){
		super(status,results,error_code,error_msg);
		this.confer = confer;
		this.certificateInf = certificateInf;
	}
	public boolean isConfer() {
		return confer;
	}
	public void setConfer(boolean confer) {
		this.confer = confer;
	}
	public String getCertificateInf() {
		return certificateInf;
	}
	public void setCertificateInf(String certificateInf) {
		this.certificateInf = certificateInf;
	}
	@Override
	public String toJSONString() {
		if (!"".equals(certificateInf)) {
			return "{\"status\":\"" + status + "\", \"results\":" + results + ", \"error_code\":\"" + error_code + "\", \"error_msg\":\""
					+ error_msg + "\",\"confer\":\"" + confer + "\", \"certificateInf\":" + certificateInf + "}";
		}else{
			return "{\"status\":\"" + status + "\", \"results\":" + results + ", \"error_code\":\"" + error_code + "\", \"error_msg\":\""
					+ error_msg + "\",\"confer\":\"" + confer + "\", \"certificateInf\":\"" + certificateInf + "\"}";
		}
		
	}
	
}
