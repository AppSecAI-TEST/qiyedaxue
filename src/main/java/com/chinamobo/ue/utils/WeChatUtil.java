package com.chinamobo.ue.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.chinamobo.ue.system.dto.DeptDto;
import com.chinamobo.ue.system.dto.EmpDto;
import com.chinamobo.ue.system.dto.LabelDto;
import com.chinamobo.ue.system.dto.LabelEmpDto;
import com.sun.enterprise.module.bootstrap.Main;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class WeChatUtil {
	
	private static final String baseUrl=Config.getProperty("WeChatUrl");
	private static final String CorpID=Config.getProperty("CorpID");
	private static final String Secret=Config.getProperty("Secret");
	public static String REDIRECT_URI = "";
	
	//获取token
	public static String getToken(){
		String turl = String.format(baseUrl+"/cgi-bin/gettoken?corpid=" + CorpID + "&corpsecret=" + Secret);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(turl);
		JsonMapper jsonMapper=new JsonMapper();
		String result = null;
		try{
			HttpResponse res = client.execute(get);
			String responseContent = null;
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JSONObject jsonObject = JSONObject.fromObject(responseContent);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				if (Integer.valueOf(jsonObject.get("errcode").toString())==0){
					result = jsonObject.get("access_token").toString();
				}
//				if (jsonObject.get("errcode") != null){
//					
//				}else{
//					result = jsonObject.get("access_token").toString();
//				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			client.getConnectionManager().shutdown();
			return result;
		}
	}
	
	//获取部门信息
	public static List<DeptDto> getDept(String access_token){
		String url = baseUrl+ "/cgi-bin/department/list?access_token="+access_token;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		JsonMapper jsonMapper=new JsonMapper();
		String result = null;
		try{
	        HttpResponse res = client.execute(get);
			String responseContent = null;
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JSONObject object=JSONObject.fromObject(responseContent);
			JSONArray jsonArray = object.getJSONArray("department");
			List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
			List<DeptDto> list = new ArrayList<DeptDto>();
			DeptDto dto =new DeptDto();
			for(int i=0;i<jsonArray.size();i++){
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 dto=jsonMapper.fromJson(jsonObject.toString(), DeptDto.class);
//				 Map<String, String> map = new HashMap<String, String>();
//				 for(Iterator<?> iter = jsonObject.keys(); iter.hasNext();){
//					 String key = (String) iter.next();
//	                 String value = jsonObject.get("department").toString();
//	                 map.put("department", value);
//				 }
//				 rsList.add(map);
				 list.add(dto);
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	//获取部门成员
	public static List<EmpDto> getEmp(String access_token,String pkDept){
		//fetch_child:1/0：是否递归获取子部门下面的成员 
		//status:0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加,未填写则默认为4 
		String url = baseUrl+ "/cgi-bin/user/list?access_token="+access_token+"&department_id="+pkDept+"&fetch_child=1"+"&status=0";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		JsonMapper jsonMapper=new JsonMapper();
		String result = null;
		try{
			HttpResponse res = client.execute(get);
			String responseContent = null;
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JSONObject object=JSONObject.fromObject(responseContent);
			JSONArray jsonArray = object.getJSONArray("userlist");
			List<EmpDto> list = new ArrayList<EmpDto>();
			EmpDto emp =new EmpDto();
			for(int i=0;i<jsonArray.size();i++){
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 emp=jsonMapper.fromJson(jsonObject.toString(), EmpDto.class);
				 list.add(emp);
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	//获取标签
	public static List<LabelDto> getLabel(String access_token){
		String url = baseUrl+ "/cgi-bin/tag/list?access_token="+access_token;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		JsonMapper jsonMapper=new JsonMapper();
		String result = null;
		try{
			HttpResponse res = client.execute(get);
			String responseContent = null;
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JSONObject object=JSONObject.fromObject(responseContent);
			JSONArray jsonArray = object.getJSONArray("taglist");
			List<LabelDto> labelList = new ArrayList<LabelDto>();
			LabelDto label = new LabelDto();
			for(int i=0;i<jsonArray.size();i++){
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 label=jsonMapper.fromJson(jsonObject.toString(), LabelDto.class);
				 labelList.add(label);
			}
			return labelList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	//获取标签成员
	public static List<LabelEmpDto> getLabelEmp(String access_token,String tagid){
		String url = baseUrl+ "/cgi-bin/tag/get?access_token="+access_token+"&tagid="+tagid;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		JsonMapper jsonMapper=new JsonMapper();
		String result = null;
		try{
			HttpResponse res = client.execute(get);
			String responseContent = null;
			HttpEntity entity = res.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			JSONObject object=JSONObject.fromObject(responseContent);
			JSONArray jsonArray = object.getJSONArray("userlist");
			List<LabelEmpDto> labelEmpList = new ArrayList<LabelEmpDto>();
			LabelEmpDto labelEmp =new LabelEmpDto();
			
			for(int i=0;i<jsonArray.size();i++){
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 labelEmp=jsonMapper.fromJson(jsonObject.toString(), LabelEmpDto.class);
				 labelEmpList.add(labelEmp);
			}
			return labelEmpList;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getTicket(String access_token) {
	    String ticket = null;  
	    String url = baseUrl+"cgi-bin/get_jsapi_ticket?access_token="+ access_token ;//这个url链接和参数不能变  
	    try {  
	        URL urlGet = new URL(url);
	        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  
	        http.setRequestMethod("GET"); // 必须是get方式请求  
	        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
	        http.setDoOutput(true);  
	        http.setDoInput(true);  
	        http.connect();  
	        InputStream is = http.getInputStream();  
	        int size = is.available();  
	        byte[] jsonBytes = new byte[size];  
	        is.read(jsonBytes);  
	        String message = new String(jsonBytes, "UTF-8");  
	        JSONObject demoJson = JSONObject.fromObject(message);  
	        ticket = demoJson.getString("ticket");  
	        is.close();  
	    } catch (Exception e) {  
	            e.printStackTrace();  
	    }  
	    return ticket;  
	} 
	public static String URLEncoder(String str){
		String result = str ;
		try {
		result = java.net.URLEncoder.encode(result,"UTF-8");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 企业号消息推送
	 * @param json
	 * @return 
	 * @throws Exception
	 */
	public static String sendMessage(String json) throws Exception{
//		System.out.println(json);
		String url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+getToken();
		HttpClient httpclient = new DefaultHttpClient();
		// Secure Protocol implementation.
		SSLContext ctx = SSLContext.getInstance("TLS");
		// Implementation of a trust manager for X509 certificates
		X509TrustManager tm = new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {

			}

			public void checkServerTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx);

		ClientConnectionManager ccm = httpclient.getConnectionManager();
		// register https protocol in httpclient's scheme registry
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, ssf));
    	HttpPost httpPost = new HttpPost(url);
        StringEntity postEntity = new StringEntity(json, "UTF-8");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(postEntity);
        
        httpPost.setConfig(RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(30000).build());
       
    	HttpResponse response;
	
		response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
//		System.out.println(EntityUtils.toString(entity, "UTF-8"));
	}
}
