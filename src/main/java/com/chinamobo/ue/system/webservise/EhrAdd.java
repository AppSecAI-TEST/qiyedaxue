//package com.chinamobo.ue.system.webservise;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.chinamobo.ele.json.InvokeInterface;
//import com.chinamobo.ue.system.entity.TomEmpOne;
//
//@Service
//public class EhrAdd {
//	@Autowired
//	private EmpInterfaceServise empInterfaceServise;
//	
//	public void operation() throws Exception {
//		Date date =new Date();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String basPsnAdd = InvokeInterface.getBasPsnAdd("2016-06-01", format.format(date));
//		List<TomEmpOne> basPsnAdd2 = empInterfaceServise.getBasPsnAdd(basPsnAdd);
//		for(TomEmpOne tomEmpOne:basPsnAdd2){
//			System.out.println(tomEmpOne.getName());
//		}
//		
//	}
//}
