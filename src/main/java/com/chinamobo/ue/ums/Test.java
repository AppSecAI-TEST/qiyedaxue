package com.chinamobo.ue.ums;




import com.chinamobo.ue.utils.MessagePost;


public class Test {

	public static void main(String[] args) throws Exception {
		
		

//		String mobile ="13756137942,15210337132,";
	

		String mobile ="15210337132";
	    

		String msg ="你在干嘛？";
		try{
		String res=MessagePost.putMessage(mobile, msg);
		System.out.println(res);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
