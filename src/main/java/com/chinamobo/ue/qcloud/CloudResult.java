package com.chinamobo.ue.qcloud;

import java.util.List;

public class CloudResult {
	private List<CloudResult> cloudlist;
	private String session;//唯一标识此文件传输过程的id
	private int offset;//请求包体里的传输的位移,调用方如果用多线程等方式传输,可以用来唯一确定本次分片结果
	private String access_url;//生成的文件下载url
	private String url;//操作文件的url
	private String resource_path;//资源路径. 格式:/appid/bucket/(只需要这部分xxx) 
	public List<CloudResult> getCloudlist() {
		return cloudlist;
	}
	public void setCloudlist(List<CloudResult> cloudlist) {
		this.cloudlist = cloudlist;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getAccess_url() {
		return access_url;
	}
	public void setAccess_url(String access_url) {
		this.access_url = access_url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getResource_path() {
		return resource_path;
	}
	public void setResource_path(String resource_path) {
		this.resource_path = resource_path;
	}
	
	
}
