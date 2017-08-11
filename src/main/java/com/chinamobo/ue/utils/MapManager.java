package com.chinamobo.ue.utils;

import java.util.Map;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 数据字典
 *
 * 作者: JCX
 * 创建时间: 2016年3月8日 上午11:43:46
 */
public class MapManager {
	/**
	 * 获取业务编号代码
	 */
	public static Map<String,String> numberMap = new HashMap<String,String>(){
		{
			put("KC", "0");//课程
			put("KCFL", "1");//课程分类
			put("JSPF", "2");//讲师评分
			put("KCPF", "3");//课程评分
			put("SJ", "4");//试卷
			put("TK", "5");//题库
			put("KS", "6");//考试
			put("RWB", "7");//任务包
			put("PXHD", "8");//培训活动
			put("TLQ", "9");//讨论圈
			put("TKFL", "10");//题库分类
			put("JS", "11");//讲师
			put("GS", "12");//公司
			put("BM", "13");//公司
		}
	};
	public static String numberType(String key){
		if(key!=null){
			return numberMap.get(key);
		}else return "";
	}

	/**
	 * 课程字典
	 */
	public static Map<String,String> courseMap = new HashMap<String,String>(){
		{

			put("0", "线上");
			put("1", "线下");
			put("Y", "已上架");
			put("N", "已下架");
		}
	};
	public static String courseParam(String key){
		if(key!=null){
			return courseMap.get(key);
		}else return "";
	}

	/**
	 * 性别
	 */
	public static Map<String,String> genderMap = new HashMap<String,String>(){
		{
			put("0", "女");
			put("1", "男");
		}
	};
	public static String getGender(String key){
		if(key!=null){
			return genderMap.get(key);
		}else return "";
	}
	
	/**
	 * 讲师字典
	 */
	public static Map<String,String> lecturerMap = new HashMap<String,String>(){
		{
			put("F", "全职");
			put("P", "兼职");
			put("I", "内部");
			put("E", "外部");
			put("G", "金牌讲师");
			put("S", "银牌讲师");
			put("C", "铜牌讲师");
		}
	};
	public static String lecturerParam(String key){
		if(key!=null){
			return lecturerMap.get(key);
		}else return "";
	}
	
	/**
	 * 题型
	 */
	public static Map<String,String> questionTypeMap = new HashMap<String,String>(){
		{
			put("1", "单选题");
			put("2", "多选题");
			put("3", "填空题");
			put("4", "简答题");
			put("5", "判断题");
		}
	};
	public static String questionType(String key){
		if(key!=null){
			return questionTypeMap.get(key);
		}else return "";
	}
	
	/**
	 * 试卷类型
	 */
	public static Map<String,String> examTypeMap = new HashMap<String,String>(){
		{
			put("1", "固定考试");
			put("2", "随机考试");
			put("3", "固定调研");
			put("4", "随机调研");
			put("5", "固定总结");
			put("6", "随机总结");
		}
	};
	public static String examType(String key){
		if(key!=null){
			return examTypeMap.get(key);
		}else return "";
	}
	
	/**
	 * 题型顺序
	 */
	public static Map<String,String> orderMap = new HashMap<String,String>(){
		{
			put("1", "1");
			put("2", "2");
			put("3", "3");
			put("4", "4");
			put("5", "5");
		}
	};
	public static String order(String key){
		if(key!=null){
			return orderMap.get(key);
		}else return "";
	}
	
	/**
	 * 商品兑换
	 */
	public static Map<String,String> exchangeMap = new HashMap<String,String>(){
		{
			put("0", "邮寄");
			put("1", "自提");
			put("Y", "已兑换");
			put("N", "未兑换");
		}
	};
	public static String exchangeParam(String key){
		if(key!=null){
			return exchangeMap.get(key);
		}else return "";
	}
	
	/**
	 * 培训活动
	 */
	public static Map<String,String> activityMap = new HashMap<String,String>(){
		{
			//是否需要报名
			put("1", "是");
			put("0", "否");
		}
	};
	public static String activityParam(String key){
		if(key!=null){
			return activityMap.get(key);
		}else return "";
	}
	
	/**
	 * 考试成绩
	 */
	public static Map<String,String> ScoreMap = new HashMap<String,String>(){
		{
			//是否合格
			put("Y", "合格");
			put("N", "不合格");
		}
	};
	public static String ScoreParam(String key){
		if(key!=null){
			return ScoreMap.get(key);
		}else return "";
	}
	
	/**
	 * 题库模板
	 */
	public static Map<String,String> TopicTypeMap = new HashMap<String,String>(){
		{
			//是否合格
			put("单选题", "1");
			put("多选题", "2");
			put("填空题", "3");
			put("简答题", "4");
			put("判断题", "5");
		}
	};
	public static String TopicTypeParam(String key){
		if(key!=null){
			return TopicTypeMap.get(key);
		}else return "";
	}
}