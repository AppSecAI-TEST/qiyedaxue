package com.chinamobo.ue.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinamobo.ue.api.invoke.ApiContext;
import com.chinamobo.ue.api.invoke.ApiInvokerListened;
import com.chinamobo.ue.api.invoke.HttpProtocol;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.cache.redis.RedisGetData;
import com.chinamobo.ue.common.servise.Master_Slave;
import com.chinamobo.ue.system.dao.TomActiveUserMapper;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.entity.TomActiveUser;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.StringUtil;

@Component
@Path("/api")
@Scope("request")
public class IndexApiRest {
	private static Logger logger = LoggerFactory.getLogger(IndexApiRest.class);
	@Autowired
	private TomActiveUserMapper activeUserMapper;
	@Autowired
	private ApiInvokerListened apiInvokerManager;
	@Autowired
	private TomUserLogMapper tomUserLogMapper;
	@Autowired
	private AbstractCacheManager redisCacheManager;//Redis缓存管理器;
	/**
	 * 
	 * 功能描述：[接口get入口]
	 *
	 * 创建者：wjx
	 * 修改: WChao
	 * 创建时间: 2016年3月17日 下午4:45:56
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@Path("/getMethod")
	@Consumes(MediaType.APPLICATION_JSON)  
    @Produces(MediaType.APPLICATION_JSON)
	@Master_Slave
	public String getMethod(@Context HttpServletRequest request,@Context HttpServletResponse response){
		long start = System.currentTimeMillis();
		JsonMapper jsonMapper=new JsonMapper();
		ApiContext context = new ApiContext(request, response , HttpProtocol.GET);
		Object apiName=context.getValue(ApiContext.API_NAME);
		Object callback=context.getValue(ApiContext.CALLBACKE);
		try {
			Result validUserResult = validUserInfo(context);//获取校验用户信息结果;
			if(!"Y".equals(validUserResult.getStatus())){
				return callback+"("+jsonMapper.toJson(validUserResult)+")";
			}
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			apiInvokerManager.notifyApiInvokers(context);
			System.out.println("耗时:"+(System.currentTimeMillis()-start)/1000.0+"秒");
			return context.getValue(ApiContext.CALLBACKE)+"("+context.getResult().toJSONString()+")";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return callback+"("+jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),apiName+"出现异常！"))+")";
		} 
	}
	/**
	 * 
	 * 功能描述：[接口post入口]
	 *
	 * 创建者：wjx
	 * 修改: WChao
	 * 创建时间: 2016年3月17日 下午4:46:08
	 * @param jsonData
	 * @return
	 */
	@POST
	@Path("/postMethod")
	@Master_Slave
	public String postMethod(String jsonData,@Context HttpServletResponse response,@Context HttpServletRequest request){
		JsonMapper jsonMapper=new JsonMapper();
		ApiContext context = new ApiContext(request, response , HttpProtocol.POST);
		JSONObject jsonObject = JSONObject.parseObject(jsonData);
		jsonObject.put("userAgent", context.getValue(HttpProtocol.USER_AGENT));
		context.setJsonData(jsonObject);//设置POST数据;
		Object apiName=context.getValue(ApiContext.API_NAME);
		try {
			Result validUserResult = validUserInfo(context);//获取校验用户信息结果;
			if(!"Y".equals(validUserResult.getStatus())){
				return jsonMapper.toJson(validUserResult);
			}
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			apiInvokerManager.notifyApiInvokers(context);
			return context.getResult().toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),apiName+"出现异常！"));
		} 
	}
	/**
	 * 判断用户信息count+Token;
	 * @param context
	 * @return
	 */
	public Result validUserInfo(ApiContext context)throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String userId=context.getValue(ApiContext.USER_ID).toString();
		Object token=context.getValue(ApiContext.TOKEN);
		if(userId!=null && !"".equals(userId)){
			//TomUserLog userLog=systemService.getUserbyCode(userId);
			TomUserLog tomUserLog = null;
			if("true".equals(Config.getProperty("redisOn"))){//是否开启缓存;
				Object userLogCache = redisCacheManager.getCacheData(new RedisGetData("tomUserLogCode",userId));
				if (userLogCache != null && StringUtil.isNotBlank(userLogCache.toString())) {
					tomUserLog = JSON.parseObject(userLogCache.toString(), TomUserLog.class);
				}else{
					tomUserLog = tomUserLogMapper.getUserByCode(userId);//根据用户ID查询用户信息;
				}
			}else{
				tomUserLog = tomUserLogMapper.getUserByCode(userId);//根据用户ID查询用户信息;
			}
			if(tomUserLog == null){
				return new Result("N", "",String.valueOf(ErrorCode.USER_ILLEGAL),"用户不存在");
			}
			
			if(!token.equals(tomUserLog.getToken())){
				return new Result("N", "",String.valueOf(ErrorCode.SIGN_ILLEGAL),"token不匹配");
			}
			Map<Object, Object> map = new HashMap<Object, Object>();
			SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
			map.put("code", userId);
			map.put("date", simple.format(new Date()));
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			if("true".equals(Config.getProperty("redisOn"))){//是否开启缓存;
				Object activeUser = redisCacheManager.getCacheData(new RedisGetData("activeUser",userId+"_"+simple.format(new Date())));
				JsonMapper mapper = new JsonMapper();
				TomActiveUser us=new TomActiveUser();
				us.setCode(userId);
				us.setCreateDate(new Date());
				if(activeUser != null){
					redisCacheManager.addCacheData(new RedisCacheData("activeUser", userId+"_"+simple.format(new Date())+"_end", mapper.toJson(us)));
				}else{
					redisCacheManager.addCacheData(new RedisCacheData("activeUser", userId+"_"+simple.format(new Date()), mapper.toJson(us)));
				}
			}else{
				List<TomActiveUser> acUser=activeUserMapper.select(map);
				if(acUser.size()==0){
					TomActiveUser us=new TomActiveUser();
					us.setCode(userId);
					us.setCreateDate(new Date());
					activeUserMapper.insert(us);
				}
			}
		}
		return new Result("Y","");
	}
}