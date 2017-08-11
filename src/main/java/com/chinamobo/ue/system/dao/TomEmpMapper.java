package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.system.dto.TomEmpDto;
import com.chinamobo.ue.system.dto.TomSendMessage;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomEmpOne;

public interface TomEmpMapper {
    int deleteByPrimaryKey(String code);

    int insert(TomEmp record);

    void insertSelective(TomEmpOne tomEmpOne);

    TomEmp selectByPrimaryKey(String code);
    
    TomEmpOne selectByPrimaryKeys(String code);

    TomEmp selectByEmail(String email);
    
    void updateByPrimaryKeySelective(TomEmpOne tomEmpOne);

    int updateByPrimaryKey(TomEmp record);
    
    List<TomEmp> selectByMany(Map <Object,Object> map);
    
    List<TomEmp> selectByWxDept(Map <Object,Object> map);
    
    int countByExample(Map <Object,Object> map);

	TomEmp selectByCodeOnetoOne(String code);

	List<TomActivity> selectByEmpActivity(Map<Object,Object> map);
	
 int	CountByEmpActivity (Map<Object,Object> map );

	List<TomEmpDto> selectByEmpExam(Map<Object,Object> map);
	
	int	countByEmpExam (Map<Object,Object> map);
	
	int	countByEmpCourses (Map<Object,Object> map);
	
	List<TomEmpDto> selectByEmpCourses(Map<Object,Object> map);
	
	int insertList(List<TomEmpOne> list);
	TomEmp selectByEmp(Map <Object,Object> map);

	/**
	 * 
	 * 功能描述：[根据课程id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午2:17:44
	 * @param courseId
	 * @return
	 */
	List<TomEmp> selectListByCourseId(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[根据考试id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午2:17:56
	 * @param id
	 * @return
	 */
	List<TomEmp> selectByExamId(int id);
	
	List<TomEmp> selectByExamIdType(Map <Object,Object> map);
	
	  List<TomEmp> selectByAdmin(Map <Object,Object> map);
	    
	    int countByAdmin(Map <Object,Object> map);

		int countByLecture(Map<Object, Object> map1);

		List<TomEmp> selectByLecture(Map<Object, Object> map);
		
		 List<TomEmp> selectEmp(Map <Object,Object> map);
		    
		    int countSelectEmp(Map <Object,Object> map);

			int selectOne(String code);

			List<TomEmpOne> selectAll();
			/**
			 * 
			 * 功能描述：[查找提示报名的活动]
			 *
			 * 创建者：cjx
			 * 创建时间: 2016年6月27日 下午3:48:59
			 * @return
			 */
			 List<TomSendMessage>	selectApplicationStart();
			 
			 List<TomSendMessage>	selectApplicationEnd();
			 
			 List<TomSendMessage>	selectActivityStart();

			List<TomSendMessage> selectActivityEnd();

//			List<TomSendMessage> selectTaskStart();
			
			List<TomSendMessage> selectTaskStart(List<String> list);
			
			List<TomSendMessage> selectTaskStart2(List<String> list);

			List<TomSendMessage> selectTaskEnd();
			
			List<TomSendMessage> selectExamBegin();
			
			List<TomSendMessage> selectExamEnd(List<String> list);

//			List<TomSendMessage> selectExamEnd();
			
			List<TomSendMessage> selectExamBegin(List<String> list);
			
			
			List<TomEmp>	 selectByMessageId(int messageId);
		
			TomEmp selectCode();
		
			List<TomEmp> selectAllDept();
		
			// 线上课程开始5分钟前推送
			List<TomSendMessage> selectCourseNow();
			
			//必修线上考试开始5分钟前推送
			List<TomSendMessage> selectExamNow();
			
			//查找5分钟之内结束的课程
			List<TomSendMessage> selectCourseEnd();
			
			//活动
			List<TomSendMessage> selectAct( List<String> list);
			
			List<TomSendMessage> selectEndAct( List<String> list);
			
			//线上课程开始前时间
			List<TomSendMessage> selectCOnline(List<String> list);
			
			List<TomSendMessage> selectCOnlineEnd(List<String> list);
			
			List<TomEmp> selectByDeptCode(String deptCode);
}