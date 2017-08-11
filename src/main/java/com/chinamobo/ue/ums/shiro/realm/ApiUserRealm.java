package com.chinamobo.ue.ums.shiro.realm;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


import com.chinamobo.ue.system.dao.TomApiUserMapper;
import com.chinamobo.ue.system.entity.TomApiUser;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.EmpServise;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ApiUserToken;
import com.chinamobo.ue.ums.shiro.ShiroPermission;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.shiro.realm.EleFormRealm.SysUser;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.RedisUtils;

import net.sf.json.JSONObject;


public class ApiUserRealm extends AuthorizingRealm {

	@Autowired
	private TomApiUserMapper tomApiUserMapper;
	@Autowired
	private SystemService systemService;
	@Autowired
	private EmpServise empServise;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		if (primaryPrincipal instanceof ApiUser) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			ShiroUser shiroUser = (ShiroUser) primaryPrincipal;
			for (ShiroPermission perm : shiroUser.getShiroPermissions()) {
				info.addRole(perm.getAuthorityurl());
			}
			return info;
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if(arg0 instanceof ApiUserToken){//只验证支持的token
			ApiUserToken apiUserToken = (ApiUserToken) arg0;
			if(apiUserToken.getPassword() == null){
				System.out.println("token为空");
				return null;
			}
			String host = apiUserToken.getHost();
			String auth = new String(apiUserToken.getPassword());
			String account = apiUserToken.getUsername();
			String accountPassword = new String(apiUserToken.getPassword());
			String accesskey=Config.getProperty("pc_accesskey");
			String URL=Config.getProperty("URL");
			String httpUrl=URL+"/auth/check?token="+accountPassword+"&accesskey="+accesskey;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			if(StringUtils.isNotBlank(auth)){
				TomUserLog tomUserLog = systemService.getUserByToken(auth);
				if(tomUserLog==null){							//数据库没有token信息
					try {
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

						HttpGet request = new HttpGet(httpUrl);
						HttpResponse response = httpclient.execute(request);
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							String strResult = EntityUtils.toString(response.getEntity());
							JSONObject jsonResult = JSONObject.fromObject(strResult);
							account = (String) jsonResult.get("account");
							Integer expire = (Integer) jsonResult.get("expire");
							Integer code = (Integer) jsonResult.get("code");
							Integer personflag = (Integer) jsonResult.get("personflag");
						    if(code!=null && 0==code){
						    	if(personflag==0){
						    		tomUserLog=systemService.getUserByEmail(account+"@ele.me");
						    		if(tomUserLog!=null){
						    			tomUserLog.setToken(accountPassword);
								    	Date temp=new Date();
								    	temp.setSeconds(temp.getSeconds()+expire);
								    	tomUserLog.setValidity(temp);
								    	tomUserLog.setPassword(accountPassword);
								    	systemService.updateByCode(tomUserLog);
								    	account=tomUserLog.getCode();
								    }else {
									    TomEmp tomEmp= empServise.selectByEmail(account+"@ele.me");
									    
									    tomUserLog=new TomUserLog();
									    tomUserLog.setCode(account);
									    tomUserLog.setName(tomEmp.getName());
									    tomUserLog.setEmail(tomEmp.getSecretEmail());
									    tomUserLog.setStatus("Y");
									    tomUserLog.setPhone(tomEmp.getMobile());
									    tomUserLog.setPassword(accountPassword);
									    tomUserLog.setToken(accountPassword);
								        Date temp=new Date();
								    	temp.setSeconds(temp.getSeconds()+expire);
								    	tomUserLog.setValidity(temp);
								        systemService.insertUser(tomUserLog);
								        account=tomUserLog.getCode();
								        apiUserToken.setUsername(account);
									    }
						    	}else if(personflag==1){
						    		tomUserLog=systemService.getUserbyCode(account);
								    if(tomUserLog!=null){
								    	tomUserLog.setToken(accountPassword);
								    	Date temp=new Date();
								    	temp.setSeconds(temp.getSeconds()+expire);
								    	tomUserLog.setValidity(temp);
								    	tomUserLog.setPassword(accountPassword);
								    	systemService.updateByCode(tomUserLog);
								    }else {
									    TomEmp tomEmp= empServise.selectByCode(account);
									    
									    tomUserLog=new TomUserLog();
									    tomUserLog.setCode(account);
									    tomUserLog.setName(tomEmp.getName());
									    tomUserLog.setEmail(tomEmp.getSecretEmail());
									    tomUserLog.setStatus("Y");
									    tomUserLog.setPhone(tomEmp.getMobile());
									    tomUserLog.setPassword(accountPassword);
									    tomUserLog.setToken(accountPassword);
								        Date temp=new Date();
								    	temp.setSeconds(temp.getSeconds()+expire);
								    	tomUserLog.setValidity(temp);
								        systemService.insertUser(tomUserLog);
								        apiUserToken.setUsername(account);
									    }
						    	}
						    }else {
						    	throw new UnknownAccountException();
						    }
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (org.apache.http.ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (KeyManagementException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(tomUserLog.getValidity().before(new Date())){//token存在，时间过期了
					System.out.println("token过期"+auth);
					throw new UnknownAccountException();
				}else {											//token存在，时间没过期
					 account=tomUserLog.getCode();
				}
				return new SimpleAuthenticationInfo(new ApiUser(0, account, account, tomUserLog.getName(), systemService.apiRole(),
						systemService.apiPermission(),null,accountPassword,null,tomUserLog),auth,getName());
			}else{
				System.out.println("token为空");
				throw new UnknownAccountException(auth);
			}
		}
		return null;
	}
	
	public static class ApiUser extends ShiroUser {
		private static final long serialVersionUID = -3727753166155527817L;
		private String authType;
		private String authKey;
		private String apiCompany;
		
		public ApiUser(int id, String code, String loginName, String name, List<TomRole> roles,
				List<ShiroPermission> shiroPermissions,String authType,String authKey,String apiCompany,TomUserLog user) {
			super(id, code, loginName, name, roles, shiroPermissions,user);
			this.authType = authType;
			this.authKey = authKey;
			this.apiCompany = apiCompany;
		}

		public String getAuthType() {
			return authType;
		}

		public void setAuthType(String authType) {
			this.authType = authType;
		}

		public String getAuthKey() {
			return authKey;
		}

		public void setAuthKey(String authKey) {
			this.authKey = authKey;
		}

		public String getApiCompany() {
			return apiCompany;
		}

		public void setApiCompany(String apiCompany) {
			this.apiCompany = apiCompany;
		}
		
		public String toString() {
			return this.getAuthKey();
		}
		
	}
}
