package com.chinamobo.ue.system.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.entity.WxMessage;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MessagePost;
import com.chinamobo.ue.utils.WeChatUtil;

import net.sf.json.JSONObject;


@Component
public class SendMessageService {
	private static Logger logger = LoggerFactory.getLogger(SendMessageService.class);
	 /** 
     * 绕过验证 
     *   
     * @return 
     * @throws NoSuchAlgorithmException  
     * @throws KeyManagementException  
     */  
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
        SSLContext sc = SSLContext.getInstance("SSLv3");  
  
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
        X509TrustManager trustManager = new X509TrustManager() {  
            @Override  
            public void checkClientTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
  
            @Override  
            public void checkServerTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
  
            @Override  
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                return null;  
            }  
        };  
        sc.init(null, new TrustManager[] { trustManager }, null);  
        return sc;  
    }  

    /**
     * 
     * 功能描述：[企聊推送入口]
     *
     * 创建者：wjx
     * 创建时间: 2016年7月4日 上午9:35:53
     * @param code
     * @param payload
     * @throws Exception
     */
	public boolean itsm(List<String>code,String payload) {
		boolean res=false;
		String appKey=Config.getProperty("appKey");
		String url=Config.getProperty("itsmUrl");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appKey", appKey);
		map.put("users", code);
		map.put("groups", new ArrayList<String>());
		map.put("payload", payload);
		try {
			send(url, map, "UTF-8");
			res=true;
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			res=false;
		}
		return res;
	}
	
	public boolean wxMessage(List<String>code,String payload){
		boolean res=false;
		String json="";
		JsonMapper mapper=new JsonMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> textMap = new HashMap<String, String>();
		StringBuffer users=new StringBuffer();
		for(String codes : code){
			users.append(codes);
			users.append("|");
		}
		map.put("touser", users.substring(0, users.length()-1));
		map.put("msgtype", "text");
		map.put("agentid", Integer.valueOf(Config.getProperty("agentid")));
		textMap.put("content", payload);
		map.put("text", textMap);
		json=mapper.toJson(map);
		try{
			WeChatUtil.sendMessage(json);
			res=true;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			res=false;
		}
		return res;
	}
	
	
	//消息推送
		public String wxNewsMessage(List<String>code,List<WxMessage> payload){
			String sendMessage="";
			String json="";
			JsonMapper mapper=new JsonMapper();
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> newsMap = new HashMap<String, Object>();
			StringBuffer users=new StringBuffer();
			for(String codes : code){
				users.append(codes);
				users.append("|");
			}
			map.put("touser", users.substring(0, users.length()-1));
			map.put("msgtype", "news");
			map.put("agentid", Integer.valueOf(Config.getProperty("agentid")));
			newsMap.put("articles", payload);
			map.put("news", newsMap);
			json=mapper.toJson(map);
			try{
				 sendMessage = WeChatUtil.sendMessage(json);
			}catch(Exception e){
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			return sendMessage;
		}
	/**
	 * 
	 * 功能描述：[企聊推送方法]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年7月4日 上午9:36:02
	 * @param url
	 * @param map
	 * @param encoding
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void send(String url, Map<String,Object> map,final String encoding) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {  
		JsonMapper jsonMapper=new JsonMapper();
        //绕过证书验证，处理https请求  
        SSLContext sslcontext = createIgnoreVerifySSL();  
          
        // 设置协议http和https对应的处理socket链接工厂的对象  
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()  
                .register("http", NoopIOSessionStrategy.INSTANCE)  
                .register("https", new SSLIOSessionStrategy(sslcontext))  
                .build();  
        //配置io线程  
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors()).build();  
        //设置连接池大小  
        ConnectingIOReactor ioReactor;  
        ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);  
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor, null, sessionStrategyRegistry, null);  
        RequestConfig defaultRequestConfig=RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setExpectContinueEnabled(true).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
        final CloseableHttpAsyncClient client = HttpAsyncClients.custom().setConnectionManager(connManager)
        .setDefaultRequestConfig(defaultRequestConfig).build();
          
        //创建post方式请求对象  
        HttpPost httpPost = new HttpPost(url);  
          
        httpPost.setHeader(HTTP.CONTENT_TYPE,"application/json;charset=utf-8");
        StringEntity entity = new StringEntity(jsonMapper.toJson(map), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        //设置header信息  
        //指定报文头【Content-type】、【User-Agent】  
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
        client.start();  
        //执行请求操作，并拿到结果（异步）  
        client.execute(httpPost, new FutureCallback<HttpResponse>() {  
              
            @Override  
            public void failed(Exception e) {  
            	logger.info(Thread.currentThread().getName()+"--失败了--"+e.getClass().getName()+"--"+e.getMessage());
                close(client);  
            }  
              
            @Override  
            public void completed(HttpResponse resp) {  
                String body="";  
                //这里使用EntityUtils.toString()方式时会大概率报错，原因：未接受完毕，链接已关  
                try {  
                    HttpEntity entity = resp.getEntity();  
                    if (entity != null) {  
                        final InputStream instream = entity.getContent();  
                        try {  
                            final StringBuilder sb = new StringBuilder();  
                            final char[] tmp = new char[1024];  
                            final Reader reader = new InputStreamReader(instream,encoding);  
                            int l;  
                            while ((l = reader.read(tmp)) != -1) {  
                                sb.append(tmp, 0, l);  
                            }  
                            body = sb.toString(); 
                            logger.info( Thread.currentThread().getName()+"--获取企聊返回的内容："+body);
                            if(!body.isEmpty()){
                       		//接收返回信息
                       		@SuppressWarnings("unchecked")
                       		Map<String,Object> MsgMap =new JsonMapper().fromJson(body, Map.class);
                       		logger.info(MsgMap.toString());
                       		//返回结果处理  0 表示成功，其他为失败  <发送消息成功{"msg":"发送消息成功","code":0}
                       	     }else{
                       		logger.info("企聊返回了null。。。。。。");
                       	    } 
                            
                        } finally {  
                        	
                            instream.close();  
                            EntityUtils.consume(entity);  
                        }  
                    }  
                } catch (ParseException | IOException e) {  
                    e.printStackTrace();  
                }  
                close(client);  
            }

			@Override
			public void cancelled() {
				
			}  
              
        });  
    } 
	/** 
     * 关闭client对象 
     *  
     * @param client 
     */  
    private static void close(CloseableHttpAsyncClient client) {  
        try {  
            client.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    } 
    
    /**
     * 
     * 功能描述：[短信推送]
     *
     * 创建者：wjx
     * 创建时间: 2016年7月4日 上午11:05:38
     * @param message
     * @param mobile
     * @return
     */
    public static boolean sendMessage(String message, String mobile) {
    	
    	try{
    		MessagePost.putMessage(mobile, message);
    		return true;
    	}catch (Exception e){
    		logger.error(e.getMessage());
    		return false;
    	}
    	
//		String MOBIEL_URL=Config.getProperty("mobiel_url");
//		String SENDER_KEY=Config.getProperty("sender_key");
//		String MOBILE_TOKEN=Config.getProperty("mobile_token");
//		Boolean flag = false;
//	    //header
//	    Map<String, String> headers = new HashMap<>();
//	    headers.put("X-Authentication-Token", MOBILE_TOKEN);
//	    try {
//	        List<BasicNameValuePair> formparams = new ArrayList<>();
//	        // 用户11位手机号
//	        formparams.add(new BasicNameValuePair("receiver", mobile));
//	        // 发送内容编码为UTF-8
//	        String urlencode = URLEncoder.encode(message, "UTF-8");
//	        // 发送内容
//	        formparams.add(new BasicNameValuePair("message", urlencode));
//	        // 用来标识接口调用者，一串Hash
//	        formparams.add(new BasicNameValuePair("sender_key", SENDER_KEY));
//	        // 请求参数集合
//	        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
//	        int resp = HttpClientUtil.postForm(MOBIEL_URL, headers, entity);
//	        // 发送成功
//	        if(resp == 200) {
//	        	flag = true;
//	        }
//	    } catch (IOException e1) {
//	        logger.error(e1.getMessage(), e1);
//	    }
//	    return flag;
	}

    //判断消息是否发送成功
    public List<String> sendStatus(List<String>codes,String wxMessage) throws Exception {
    	 List<String> listApp = new ArrayList<String>();
		 listApp.addAll(codes);
			JSONObject jsonObject = new JSONObject();
			JSONObject jsonO = jsonObject.fromObject(wxMessage);
			String errcode = jsonO.getString("errcode");
			if("0".equals(errcode)){
				return new ArrayList<String>();
			}else if("60011".equals(errcode)){
				String json="";
				String invaliduser = jsonO.getString("invaliduser");
				String[] split = invaliduser.split("\\|");
				for(String str:split){
					for(int i = 0;i<listApp.size();i++){
						if(str.equalsIgnoreCase(listApp.get(i))){
							listApp.remove(i);
						}
					}
				}
				return listApp;
    }else{
    	throw new Exception("消息发送失败");
    }
}}
