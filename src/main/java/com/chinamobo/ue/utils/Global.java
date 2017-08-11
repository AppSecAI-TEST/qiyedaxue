package com.chinamobo.ue.utils;

/**
 * 版本: [1.0]
 * 功能说明: 全局配置类
 *
 * 作者: WangLg
 * 创建时间: 2016年3月10日 下午1:35:23
 */
public class Global{
	
	/*
	 * D 负责人报名  E 员工自行报名
	 */
	public static final String FZRBM = "D";
	public static final String YGZXBM = "E";
	
	/**
	 * ***培训活动***
	 */
	public static final String WYBM = "我要报名";
	public static final String DDPX = "等待培训（已经报名了）";
	public static final String WKSBM = "未开始报名";
	public static final String BMZ = "报名中";
	public static final String YJBM = "已经报名（课程包还没有开始，活动包开始了就叫活动进行中）";
	public static final String BMJZ = "报名截止	我的活动-报名截止";
	public static final String HDJXZ = "活动进行中（时间未到，活动没有完成）";
	public static final String YWCHD = "已完成活动（完成所有任务）";
	public static final String WWCHD = "任务失败(时间到了，没有完成所有任务)";
	
	public static final String DDBM ="等待报名";
	public static final String WYQX ="我要取消";
	public static final String YTG ="未通过";
	public static final String WTG ="已经通过";
	public static final String ZZPX = "正在培训";
	
	
	
	
	public static final String WBM = "N";//未报名
	public static final String YBM = "Y";//已报名
	public static final String CKPF ="CKPF";
	
	public static final String JSPF ="JSPF";
	
	//private static PropertyLoader propertiesLoader = new PropertyLoader("test.properties");TODO
	/**
	 * Y : 关联 		 签订    需要报名		正常  	必修        已学习	收藏			正常 	是		点赞...
	 * N : 取消关联  不签订  不需要报名		删除		选修 	未学习	取消收藏		停用		不是		取消点赞...
	 */
	public static final String GL ="Y";
	public static final String QXGL ="N";
	public static final String QD ="Y";
	public static final String BQD ="N";
	public static final String XYBM ="Y";
	public static final String BXYBM ="N";
	public static final String ZC ="Y";
	public static final String BZC ="N";
	public static final String BX ="Y";
	public static final String XX ="N";
	
	
	public static final String YXX ="Y";
	public static final String WXX ="N";
	public static final String SC ="Y";
	public static final String QXSC ="N";
	
	public static final String TY ="N";
	
	public static final String YES ="Y";
	public static final String NO ="N";
	
	public static final String DZ ="Y";
	public static final String QXDZ ="N";
	
	
	/**
	 * 0: 线下开始 
	 * 1: 线上考试
	 */
	public static final String ZERO ="0";
	public static final String ONE ="1";
	
	/**
	 * 负责人报名
	 */
	public static final String D ="D";
	
	/**
	 * 员工自行报名
	 */
	public static final String E ="E";
	    
	/**
	 * 课程评分
	 */
	public static final String CP ="C";
	
	/**
	 * 讲师评分
	 */
	public static final String LP ="L";
	
	/**
	 * token
	 */
	public static final String TOKEN ="token";
	
	/**
	 * 任务包编号
	 */
	public static final String RWB ="RWB";
	
	/**
	 * 培训活动编号
	 */
	public static final String PXHD ="PXHD";
	/**
	 * 课程
	 */
	public static final String COURSE="课程";
	/**
	 * 考试
	 */
	public static final String EXAM="考试";
	/**
	 * 已完成
	 */
	public static final String ONEN="1";
	/**
	 * 未完成
	 */
	public static final String TWO="2";
	/**
	 * 考试统计通过状态
	 */
	public static final String Yes="是";
	/**
	 * 考试统计通过状态
	 */
	public static final String No="否";
}
