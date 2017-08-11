package com.chinamobo.ue.ums.shiro.realm;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomAuthorities;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.EmpServise;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.EleFormToken;
import com.chinamobo.ue.ums.shiro.ShiroPermission;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.shiro.realm.EleFormRealm.SysUser;
import com.chinamobo.ue.ums.util.Encodes;
import com.chinamobo.ue.utils.Config;

import net.sf.json.JSONObject;

import javax.net.ssl.SSLContext;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.ClientParamsStack;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;


public class EleFormRealm extends AuthorizingRealm {
	@Autowired
	private SystemService systemService;
	@Autowired
	private EmpServise empServise;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		// String username = (String)
		// arg0.fromRealm(getName()).iterator().next();
		// TODO 获取权限方法

		/*
		 * if (user != null) { TomAdmin admin =
		 * systemService.getAdminByName(user.getName()); if (admin != null &&
		 * "Y".equals(admin.getStatus())) { List<TomRole> troles =
		 * systemService.listRoleByAminId(admin.getAdminId());
		 * SimpleAuthorizationInfo simpleAuthorInfo = new
		 * SimpleAuthorizationInfo(); List<TomAuthorities> authorities =
		 * systemService.listAuthoritiesByRoleId(admin.getAdminId()); if
		 * (authorities != null && !authorities.isEmpty()) { for (TomAuthorities
		 * an : authorities) { //可访问的url
		 * simpleAuthorInfo.addStringPermission(an.getAuthorityUrl()); } }
		 * return simpleAuthorInfo; } }
		 */
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		if (primaryPrincipal instanceof SysUser) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			ShiroUser shiroUser = (ShiroUser) primaryPrincipal;
			// TomUserLog user =
			// systemService.getUserbyName(shiroUser.getName());
			for (ShiroPermission perm : shiroUser.getShiroPermissions()) {
				// 基于Role的权限信息
				// info.addRole(role.getRoleName());
				info.addRole(perm.getAuthorityurl());
				// 基于Permission的权限信息
				// info.addStringPermissions(role.getStringPermissions());
			}
			return info;
		}
		return null;
	}

	/*
	 * @Override protected AuthenticationInfo
	 * doGetAuthenticationInfo(AuthenticationToken authcToken) throws
	 * AuthenticationException {
	 * 
	 * // String username = (String) token.getPrincipal(); // String password =
	 * new String((char[])token.getCredentials()); // TODO Auto-generated method
	 * stub // String username = (String)authcToken.getPrincipal();
	 * if(authcToken instanceof EleFormToken){//只验证支持的token EleFormToken token =
	 * (EleFormToken) authcToken; String accountName = token.getUsername();
	 * String accountPassword = new String(token.getPassword()); TomUserLog user
	 * = systemService.getUserbyName(accountName); if (user != null
	 * &&"Y".equals(user.getStatus())) { //TODO 密码解密步骤 if
	 * (user.getPassword().equals(accountPassword)) { TomAdmin admin =
	 * systemService.getAdminByName(user.getName()); if (admin != null &&
	 * "Y".equals(admin.getStatus())) { // AuthenticationInfo authcInfo = new //
	 * SimpleAuthenticationInfo(user.getName(),user.getPassword(),getName());
	 * AuthenticationInfo authcInfo=null; try{ authcInfo = new
	 * SimpleAuthenticationInfo( new SysUser(admin.getAdminId(),
	 * admin.getCode(), accountName, admin.getName(),
	 * systemService.listRoleByAminId(admin.getAdminId()),
	 * systemService.listShiroPermissionByAdminId(admin.getAdminId())),
	 * user.getPassword(), getName()); }catch(Exception e){
	 * System.out.println("ex"); e.printStackTrace(); } return authcInfo; } else
	 * { //密码锁定或是管理员 throw new LockedAccountException(); } } else { //密码错误 throw
	 * new IncorrectCredentialsException(); } } else { //帐号不存在 throw new
	 * UnknownAccountException(); } } return null; }
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if (authcToken instanceof EleFormToken) {
			EleFormToken token = (EleFormToken) authcToken;
			String account = token.getUsername();
			String accountPassword = new String(token.getPassword());
			String accesskey = Config.getProperty("accesskey");
			String URL = Config.getProperty("URL");
			String httpUrl = URL + "/auth/check?token=" + accountPassword + "&accesskey=" + accesskey;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = new Date();
			TomUserLog tomUserLog = systemService.getUserByToken(accountPassword);
			if (tomUserLog == null) { // 数据库没有token信息
				try {

					HttpClient httpclient = new DefaultHttpClient();
					// Secure Protocol implementation.
					SSLContext ctx = SSLContext.getInstance("SSL");
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
						if (code != null && 0 == code) {
							if (personflag == 0) {
								TomUserLog user = systemService.getUserByEmail(account + "@ele.me");
								if (user != null) {
									user.setToken(accountPassword);
									Date temp = new Date();
									temp.setSeconds(temp.getSeconds() + expire);
									user.setValidity(temp);
									user.setPassword(accountPassword);
									systemService.updateByCode(user);
									account = user.getCode();
								} else {
									TomEmp tomEmp = empServise.selectByEmail(account + "@ele.me");

									TomUserLog userLog = new TomUserLog();
									userLog.setCode(account);
									userLog.setName(tomEmp.getName());
									userLog.setEmail(tomEmp.getSecretEmail());
									userLog.setStatus("Y");
									userLog.setPhone(tomEmp.getMobile());
									userLog.setPassword(accountPassword);
									userLog.setToken(accountPassword);
									Date temp = new Date();
									temp.setSeconds(temp.getSeconds() + expire);
									userLog.setValidity(temp);
									systemService.insertUser(userLog);
									account = userLog.getCode();
									token.setUsername(account);
								}
							} else if (personflag == 1) {
								TomUserLog user = systemService.getUserbyCode(account);
								if (user != null) {
									user.setToken(accountPassword);
									Date temp = new Date();
									temp.setSeconds(temp.getSeconds() + expire);
									user.setValidity(temp);
									user.setPassword(accountPassword);
									systemService.updateByCode(user);
								} else {
									TomEmp tomEmp = empServise.selectByCode(account);

									TomUserLog userLog = new TomUserLog();
									userLog.setCode(account);
									userLog.setName(tomEmp.getName());
									userLog.setEmail(tomEmp.getSecretEmail());
									userLog.setStatus("Y");
									userLog.setPhone(tomEmp.getMobile());
									userLog.setPassword(accountPassword);
									userLog.setToken(accountPassword);
									Date temp = new Date();
									temp.setSeconds(temp.getSeconds() + expire);
									userLog.setValidity(temp);
									systemService.insertUser(userLog);
									token.setUsername(account);
								}
							}
						}else {
							request.releaseConnection();
							throw new UnknownAccountException();
						}
					}
					request.releaseConnection();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (org.apache.http.ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (tomUserLog.getValidity().before(date)) {// token存在，时间过期了
				throw new UnknownAccountException();
			} else { // token存在，时间没过期
				account = tomUserLog.getCode();
			}
			TomUserLog user = systemService.getUserbyCode(account);
			if (user != null && "Y".equals(user.getStatus())) {
				TomAdmin admin = systemService.getAdminByCode(user.getCode());
				if (admin != null && "Y".equals(admin.getStatus())) {
					AuthenticationInfo authcInfo = null;
					try {
						authcInfo = new SimpleAuthenticationInfo(
								new SysUser(admin.getAdminId(), admin.getCode(), account, admin.getName(),
										systemService.listRoleByAminId(admin.getAdminId()),
										systemService.listShiroPermissionByAdminId(admin.getAdminId()),tomUserLog),
								user.getToken(), getName());
					} catch (Exception e) {
						System.out.println("ex");
						e.printStackTrace();
					}
					return authcInfo;
				} else {
					// 密码锁定或不是管理员
					throw new LockedAccountException();
				}
			} else {
				// 帐号不存在
				throw new UnknownAccountException();
			}
		}
		return null;
	}

	public static class SysUser extends ShiroUser implements Serializable {

		private static final long serialVersionUID = 4456227173253370487L;

		public SysUser(Integer id, String code, String loginName, String name, List<TomRole> roles,
				List<ShiroPermission> shiroPermissions,TomUserLog user) {
			super(id, code, loginName, name, roles, shiroPermissions,user);
		}

	}
}
