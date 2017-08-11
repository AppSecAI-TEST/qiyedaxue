/**
 * 
 */
package com.chinamobo.ue.common.servise;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import com.chinamobo.ue.ums.DBContextHolder;
/**
 * @author WChao
 * <!-- 第1步： AOP的配置 -->  
    <aop:config>  
        <!-- 第2步：配置一个切面 -->  
        <aop:aspect id="apiAspect" ref="apiAspectBean">  
            <!-- 第3步：定义切入点,指定切入点表达式 -->  
            <aop:pointcut id="allMethod"  expression="execution(* com.chinamobo.ue.system.restful.*.*(..))"/>   
            <!-- 第4步：应用前置通知 -->  
            <aop:before method="before" pointcut-ref="allMethod" />  
            <!-- 第4步：应用后置通知 -->  
            <aop:after-returning method="afterReturn" pointcut-ref="allMethod"/>  
            <!-- 第4步：应用最终通知 -->  
            <aop:after method="after" pointcut-ref="allMethod"/>  
            <!-- 第4步：应用抛出异常后通知 -->  
            <aop:after-throwing method="afterThrowing" pointcut-ref="allMethod"/>  
        </aop:aspect>  package com.chinamobo.ue.exam.service;
    </aop:config>  
 */
@Aspect
public class ApiAspect {
	//定义切入点，提供一个方法，这个方法的名字就是改切入点的id  
    @Pointcut("execution(* com.chinamobo.ue.*.*.*Service.*(..)) || execution(* com.chinamobo.ue.api.*.*.*Service.*(..))") 
    //com.chinamobo.ue.api.myInfo.service
	private void allMethod(){};
	//针对指定的切入点表达式选择的切入点应用前置通知  
    @Before("allMethod()")
    public void before(JoinPoint call) {  
		try {
			//获取目标对象上正在执行的方法名  
	        String methodName = call.getSignature().getName();
	        Method method = null;
	        Method[] methods = call.getTarget().getClass().getMethods();
	        for(Method methodObj : methods){
	        	if(methodObj.getName().equals(methodName)){
	        		method = methodObj;
	        		break;
	        	}
	        }
	        if(method != null){
	        	if(method.isAnnotationPresent(Master_Slave.class)){
	        		Master_Slave master_slave = method.getAnnotation(Master_Slave.class);
	        		DBContextHolder.setDbType(master_slave.value());  
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    } 
    //访问命名切入点来应用后置通知  
    @AfterReturning("allMethod()")  
    public void afterReturn() {  
       // System.out.println("后置通知:方法正常结束了");  
    }
    //应用最终通知
    @After("allMethod()") 
    public void after(){  
        //System.out.println("最终通知:不管方法有没有正常执行完成，一定会返回的");  
    }
    //应用异常抛出后通知  
    @AfterThrowing("allMethod()")
    public void afterThrowing() {  
        //System.out.println("异常抛出后通知:方法执行时出异常了");  
    }  
}
