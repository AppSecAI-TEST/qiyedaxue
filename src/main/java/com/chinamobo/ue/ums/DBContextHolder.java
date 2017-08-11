package com.chinamobo.ue.ums;

public class DBContextHolder {
	
	private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
	public static final String MASTER = "master";  
	public static final String SLAVE = "slave";  
  
	public static String getDbType() {  
        String db = contextHolder.get();  
        if (db == null) {  
            db = MASTER;// 默认是读写库  
        }  
        return db;  
    }  
  
    /** 
     *  
     * 设置本线程的dbtype 
     *  
     * @param str 
     * @see [相关类/方法](可选) 
     * @since [产品/模块版本](可选) 
     */  
    public static void setDbType(String str) {  
        contextHolder.set(str);  
    }  
  
    /** 
     * clearDBType 
     *  
     * @Title: clearDBType 
     * @Description: 清理连接类型 
     */  
    public static void clearDBType() {  
        contextHolder.remove();  
    }  
}
