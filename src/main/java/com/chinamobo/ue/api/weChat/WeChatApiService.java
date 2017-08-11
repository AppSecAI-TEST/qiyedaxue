package com.chinamobo.ue.api.weChat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.restful.SystemRest;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.WeChatUtil;

import net.sf.json.JSONObject;

@Service
public class WeChatApiService {
	@Autowired
	private static final String appid=Config.getProperty("appId");
	@Autowired
	private static final String secret=Config.getProperty("appsecret");
	@Autowired
	private SystemService systemService;
	@Autowired
	private TomUserLogMapper tomUserLogMapper;
	
	private static Logger logger = LoggerFactory.getLogger(SystemRest.class);
	
	public static String GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=a123#wechat_redirect";
	
	String CODE_TO_USERINFO  = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE&agentid=AGENTID";
	
	String USERID_TO_OPENID  = "https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token=ACCESS_TOKEN";
	
	/**
	 * 
	 * 功能描述：[微信config注入参数获取]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年9月19日 下午3:44:47
	 * @param request
	 * @param response
	 * @return
	 */
	public Result config(HttpServletRequest request, HttpServletResponse response){
		JsonMapper mapper = new JsonMapper();
		String url=URLDecoder.decode(request.getParameter("url"));
		String token=WeChatUtil.getToken();
		String ticket=WeChatUtil.getTicket(token);
		Map<String, String> res=sign(ticket,url);
		res.put("appId", Config.getProperty("CorpID"));
		String json=mapper.toJson(res);
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}
	
	private static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
//        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
   
    /**
     * 根据access_token、auto_code获取userid
     * @param access_token
     * @param code
     * @param agentid
     * @return
     */
    public TomUserLog getuserinfo(HttpServletRequest request,HttpServletResponse response2){
    	DBContextHolder.setDbType(DBContextHolder.MASTER);
    	HttpClient httpclient = new DefaultHttpClient();
    	String auth_code = request.getParameter("code");
//    	System.out.println("code="+auth_code);
    	String userid = null;
    	String token = "";
    	try{
    		String access_token = WeChatUtil.getToken();
    		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+access_token+"&code="+auth_code;
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
			
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));

            HttpGet get = new HttpGet(url);
            HttpResponse response = httpclient.execute(get);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                	String result = EntityUtils.toString(resEntity, "UTF-8");
                	JSONObject jsonobject = JSONObject.fromObject(result);
                	userid = jsonobject.getString("UserId");
//                	System.out.println("userid="+userid);
                	TomUserLog user = systemService.getUserbyCode(userid);
                	if (user == null || user.getStatus().equals("N")) {
            			logger.error("用户不存在");
            			return null;
            		}else {
            			if (user.getValidity().before(new Date())) {
            				user.setToken(UUID.randomUUID().toString());
            				Calendar c = Calendar.getInstance();
            				c.add(Calendar.DAY_OF_MONTH, 30);
            				user.setValidity(c.getTime());
            				tomUserLogMapper.updateByCode(user);
            			}
            			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            			token = user.getToken();
            			return user;
            		}
                }  
            }  
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
}
