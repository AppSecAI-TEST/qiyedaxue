package com.chinamobo.ue.course.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomLecturerMapper;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.MapManager;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 讲师管理业务层
 *
 * 作者: JCX
 * 创建时间: 2016年3月1日 上午9:56:22
 */
@Service
public class LecturerService {
	@Autowired
	private TomLecturerMapper lecturerMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private CourseService courseService;
	
	/**
	 * 
	 * 功能描述：[讲师添加]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:55:51
	 * @param lecturer
	 */
	@Transactional
	public void addLecturer(TomLecturer lecturer){
		//自动生成业务编号
		String lecturerNumber= numberRecordService.getNumber(MapManager.numberType("JS"));
		lecturer.setLecturerNumber(lecturerNumber);	
//		char a='';
//		int i=(int)a;
		lecturer.setCreateTime(new Date());
		lecturer.setUpdateTime(new Date());
		//状态设置为可用
		lecturer.setStatus("Y");
		lecturerMapper.insertSelective(lecturer);
	}
	
	/**
	 * 
	 * 功能描述：[按id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:56:39
	 * @param lecturerId
	 * @return
	 */
	public TomLecturer selectLecturerById(int lecturerId){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return lecturerMapper.selectByPrimaryKey(lecturerId);
	}
	
	/**
	 * 
	 * 功能描述：[按条件分页查询,页大小为-1时不分页]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:56:53
	 * @param pageNum
	 * @param pageSize
	 * @param lecturerName
	 * @return
	 */
	public PageData<TomLecturer> selectListByPage(int pageNum ,int pageSize,String lecturerName){
	/*	if(lecturerName!=null){
			lecturerName=lecturerName.replaceAll("/", "//");
			lecturerName=lecturerName.replaceAll("%", "/%");
			lecturerName=lecturerName.replaceAll("_", "/_");
		}*/
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomLecturer example=new TomLecturer();
		PageData<TomLecturer> page=new PageData<TomLecturer>();				
		example.setLecturerName(lecturerName);	
		int count=lecturerMapper.countByExample(example);
		
		List<TomLecturer> list;
		if (pageSize!=-1) {
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("startNum", (pageNum - 1) * pageSize);
			map.put("endNum", pageSize);// pageNum *
			map.put("example", example);
			list = lecturerMapper.selectListByPage(map);
		}else{
			list=lecturerMapper.selectAll(example);
			pageNum=1;
			pageSize=count;
		}
		
		for(TomLecturer lecturer:list){
			lecturer.setLecturerScore(lecturerMapper.selectLecturerAvg(lecturer.getLecturerId()));
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;
	}

	/**
	 * 
	 * 功能描述：[更新]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:57:04
	 * @param lecturer
	 */
	@Transactional
	public void updateLecturer(TomLecturer lecturer) {
		lecturer.setUpdateTime(new Date());
		lecturerMapper.updateByPrimaryKeySelective(lecturer);
	}

	/**
	 * 
	 * 功能描述：[伪删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 上午9:57:13
	 * @param lecturer
	 */
	public void updateStatus(TomLecturer lecturer) {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		lecturerMapper.updateByPrimaryKeySelective(lecturer);
	}
	
	public Double getLecturerScore(int lecturerId){
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomCourses> list=courseService.selectCourseByLecturerId(lecturerId);
		Double totalScore=0d;
		int count=0;
		if(list.size()>0){
			for(TomCourses courses:list ){
				if(courses.getLecturerAverage()!=null){
					totalScore+=courses.getLecturerAverage();
					count++;
				}	
			}
//			System.out.println(totalScore+"  "+count+"  "+totalScore/count);
			return totalScore/count;
		}else{
			return null;
		}
		
	}
	public TomLecturer selectByCode(String code) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return lecturerMapper.selectByCode(code);
		
	}
}
