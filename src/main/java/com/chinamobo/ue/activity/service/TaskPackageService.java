package com.chinamobo.ue.activity.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomTaskCoursesRelationMapper;
import com.chinamobo.ue.activity.dao.TomTaskExamPaperRelationMapper;
import com.chinamobo.ue.activity.dao.TomTaskPackageMapper;
import com.chinamobo.ue.activity.dto.TomTaskPackageDto;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.activity.entity.TomTaskCoursesRelation;
import com.chinamobo.ue.activity.entity.TomTaskExamPaperRelation;
import com.chinamobo.ue.activity.entity.TomTaskPackage;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomLecturerMapper;
import com.chinamobo.ue.course.dto.TomCoursesDto;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dto.ExamPaperDto2;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.MapManager;
import com.google.common.collect.Maps;


/**
 * 版本: [1.0]
 * 功能说明: 任务包管理
 *
 * 作者: Wanghb
 * 创建时间: 2016年4月19日
 */
@Service
public class TaskPackageService {
	
	@Autowired
	private TomTaskPackageMapper taskPackageMapper;
	@Autowired
	private TomTaskExamPaperRelationMapper tomTaskExamPaperRelationMapper;
	@Autowired
	private TomTaskCoursesRelationMapper taskCoursesRelationMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomLecturerMapper lecturerMapper;
	@Autowired
	private TomExamPaperMapper tomExamPaperMapper;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	/**
	 * 功能描述：[添加任务包]			
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:46:33
	 * @param taskPackage
	 */
	@Transactional
	public void addTask(TomTaskPackageDto dto) throws Exception {
		TomTaskPackage taskPackage = new TomTaskPackage();
		String number=numberRecordService.getNumber(MapManager.numberType("RWB"));
		Date date =new Date();
		
		ShiroUser user=ShiroUtils.getCurrentUser();
		taskPackage.setAdmins(dto.getAdmins());
		taskPackage.setPackageNumber(number);
		taskPackage.setCreator(user.getName());
		taskPackage.setOperator(user.getName());
		taskPackage.setPackageName(dto.getPackageName());
		taskPackage.setStatus("Y");
		taskPackage.setCreatorId(user.getCode());
		taskPackage.setCreateTime(date);
		taskPackage.setUpdateTime(date);
		
		taskPackage.setTaskCount(dto.getCoursesId().size()+dto.getExamPaperId().size());
		/**
		 * 任务包
		 */
		//设置主从库查询
		taskPackageMapper.insertSelective(taskPackage);//任务包
		int taskPackageId = taskPackage.getPackageId();
		
		/**
		 * 任务课程关联表
		 */
		if(dto.getCoursesId().size()>0){
			for(int i = 0;i<dto.getCoursesId().size();i++){
				TomTaskCoursesRelation taskCoursesRelation = new TomTaskCoursesRelation();
				taskCoursesRelation.setCourseId(dto.getCoursesId().get(i));
				taskCoursesRelation.setPackageId(taskPackageId);
				taskCoursesRelation.setCreator(user.getName());
				taskCoursesRelation.setOperator(user.getName());
				taskCoursesRelation.setStatus("Y");
				taskCoursesRelation.setCreateTime(date);
				taskCoursesRelation.setUpdateTime(date);
				addTaskCoursesRelation(taskCoursesRelation,"add");
			}
		}
		
		
		/**
		 * 任务包考试试卷关联表
		 */
		if(dto.getExamPaperId().size()>0){
			for(int i = 0;i<dto.getExamPaperId().size();i++){
				TomTaskExamPaperRelation taskExamPaperRelation = new TomTaskExamPaperRelation();
				taskExamPaperRelation.setExamPaperId(dto.getExamPaperId().get(i));
				taskExamPaperRelation.setPackageId(taskPackageId);
				taskExamPaperRelation.setCreator(user.getName());
				taskExamPaperRelation.setOperator(user.getName());
				taskExamPaperRelation.setStatus("Y");
				taskExamPaperRelation.setCreateTime(date);
				taskExamPaperRelation.setUpdateTime(date);
				addTaskExamPaperRelation(taskExamPaperRelation,"add");
			}
		}
	}
	/**
	 * 
	 * 功能描述：[添加任务包关联课程]
	 * 创建者：WangLg
	 * 创建时间: 2016年3月16日 下午3:06:49
	 */
	@Transactional
	public void addTaskCoursesRelation(TomTaskCoursesRelation taskCoursesRelation,String flag){
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if("add".equals(flag)){
			taskCoursesRelationMapper.insert(taskCoursesRelation);
		}else{
			taskCoursesRelationMapper.updateByPrimaryKeySelective(taskCoursesRelation);
		}
	}
	/**
	 * 
	 * 功能描述：[添加任务包关联考试试卷]
	 * 创建者：WangLg
	 * 创建时间: 2016年3月16日 下午3:08:11
	 */
	@Transactional
	public void addTaskExamPaperRelation(TomTaskExamPaperRelation taskExamPaperRelation,String flag){
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if("add".equals(flag)){
			tomTaskExamPaperRelationMapper.insert(taskExamPaperRelation);
		}else{
			tomTaskExamPaperRelationMapper.updateByPrimaryKeySelective(taskExamPaperRelation);
		}
	}
	
	/**
	 * 功能描述：[更新任务包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:51:37
	 * @param taskPackage
	 */
	@Transactional
	public String updateTask(TomTaskPackageDto dto) throws Exception{
		Date date =new Date();
		ShiroUser user = ShiroUtils.getCurrentUser();
		TomTaskPackage taskPackage = new TomTaskPackage();
		taskPackage.setPackageId(dto.getPackageId());
		taskPackage.setPackageName(dto.getPackageName());
		taskPackage.setAdmins(dto.getAdmins());
		taskPackage.setUpdateTime(date);
		taskPackage.setOperator(user.getName());
		taskPackage.setTaskCount(dto.getCoursesId().size()+dto.getExamPaperId().size());
		
		TomActivityProperty example=new TomActivityProperty();
		example.setPackageId(taskPackage.getPackageId());
		List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(example);
		if(activityProperties!=null&&activityProperties.size()>0){
			return "protected";
		}
		taskPackageMapper.updateByPrimaryKeySelective(taskPackage);
		/**
		 * 任务包课程关联表
		 */
		List<TomTaskCoursesRelation> tomTaskCourseList =taskCoursesRelationMapper.selectByPackageId(dto.getPackageId());
		if(tomTaskCourseList.size()>0){
			for(TomTaskCoursesRelation tomTask:tomTaskCourseList){
				taskCoursesRelationMapper.deleteByPrimaryKey(tomTask);
			}
		}
		if(dto.getCoursesId().size()>0){
			for(int i = 0;i<dto.getCoursesId().size();i++){
				TomTaskCoursesRelation tomTaskCourses=new TomTaskCoursesRelation();
				tomTaskCourses.setCourseId(dto.getCoursesId().get(i));
				tomTaskCourses.setPackageId(dto.getPackageId());
				tomTaskCourses.setCreator(user.getName());
				tomTaskCourses.setOperator(user.getName());
				tomTaskCourses.setStatus("Y");
				tomTaskCourses.setCreateTime(date);
				tomTaskCourses.setUpdateTime(date);
				addTaskCoursesRelation(tomTaskCourses,"add");
			}
			
		}
		
		/**
		 * 任务包考试试卷关联表
		 */
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomTaskExamPaperRelation> tomTaskExamPaperList =tomTaskExamPaperRelationMapper.selectByPackageId(dto.getPackageId());
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if(tomTaskExamPaperList.size()>0){
			for(TomTaskExamPaperRelation tomTask:tomTaskExamPaperList){
				tomTaskExamPaperRelationMapper.deleteByPrimaryKey(tomTask);
			}
		}
		if(dto.getExamPaperId().size()>0){
			for(int i = 0;i<dto.getExamPaperId().size();i++){
				TomTaskExamPaperRelation tomTaskExamPaper=new TomTaskExamPaperRelation();
				tomTaskExamPaper.setExamPaperId(dto.getExamPaperId().get(i));
				tomTaskExamPaper.setPackageId(dto.getPackageId());
				tomTaskExamPaper.setCreator(user.getName());
				tomTaskExamPaper.setOperator(user.getName());
				tomTaskExamPaper.setStatus("Y");
				tomTaskExamPaper.setCreateTime(date);
				tomTaskExamPaper.setUpdateTime(date);
				addTaskExamPaperRelation(tomTaskExamPaper,"add");
			}
		} 
		
		return "Y";
	}
	
	/**
	 * 功能描述：[删除任务包]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午9:51:54
	 * @param taskPackage
	 */
	@Transactional
	public String deleteActivity(int packageId) {
		TomActivityProperty example=new TomActivityProperty();
		example.setPackageId(packageId);
		List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(example);
		if(activityProperties!=null&&activityProperties.size()>0){
			return "protected";
		}
		
		TomTaskPackage tomTaskPackage = taskPackageMapper.selectByPrimaryKey(packageId);
		tomTaskPackage.setStatus("N");
		taskPackageMapper.updateByPrimaryKeySelective(tomTaskPackage);
		
		return "Y";
	}
	
	
	/**
	 * 功能描述：[查看任务包明细]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月10日 上午10:00:03
	 * @param example
	 * @return
	 */
	public TomTaskPackageDto findTaskDetails(int packageId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object,Object> map = Maps.newHashMap();
		map.put("packageId", packageId);	
		TomTaskPackage  taskPackage = taskPackageMapper.selectByPrimaryKey(packageId);
		
		List<TomCourses>  dtoCourselist = taskPackageMapper.selectDetailsCourse(map);
		List<TomExamPaper>  dtoExamlist = taskPackageMapper.selectDetailsExamPaper(map);
		
		TomTaskPackageDto dtos = new TomTaskPackageDto();
		List<Object> objList=new ArrayList<Object>();
		for(TomCourses tomCourses:dtoCourselist){
			objList.add(tomCourses);
		}
		for(TomExamPaper tomExamPaper:dtoExamlist){
			objList.add(tomExamPaper);
		}
		dtos.setObjectList(objList);
		dtos.setAdmins(taskPackage.getAdmins());
		dtos.setPackageName(taskPackage.getPackageName());
		return dtos;
	}

	
	/**
	 * 功能描述：[查询任务包列表]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月16日 下午5:09:22
	 * @param pageNum
	 * @param pageSize
	 * @param packageName
	 * @return
	 */
	public PageData<TomTaskPackage> selectListByParam(int pageNum ,int pageSize,String packageName)throws Exception{
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomTaskPackage> page = new PageData<TomTaskPackage>();		
		
	/*	if(packageName!=null){
			packageName=packageName.replaceAll("/", "//");
			packageName=packageName.replaceAll("%", "/%");
			packageName=packageName.replaceAll("_", "/_");
			
		}*/
		
		Map<Object, Object> map = Maps.newHashMap();
		map.put("packageName", packageName);
		int count=taskPackageMapper.countByList(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomTaskPackage> list = taskPackageMapper.selectListByParam(map);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);

		return page;
	}
	
	/**
	 * 功能描述：[查询添加活动页面的任务包信息]
	 *
	 * 创建者：Wanghb
	 * 创建时间: 2016年3月16日 下午5:09:22
	 * @param pageNum
	 * @param pageSize
	 * @param packageName
	 * @return
	 */
	public PageData<TomTaskPackageDto> selectActivityTaskListByParam(int pageNum, int pageSize, String packageName) {
		PageData<TomTaskPackageDto> page = new PageData<TomTaskPackageDto>();		
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = Maps.newHashMap();
		map.put("packageName", packageName);
		int count=taskPackageMapper.countByList(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomTaskPackage> list = taskPackageMapper.selectListByParam(map);
		List<TomTaskPackageDto> taskPackageDtoList=new ArrayList<TomTaskPackageDto>();
		List<TomLecturer> lecturers=null;
		List<Object> objList=null;
		if(list.size()>0){
			for (TomTaskPackage tomTaskPackage : list) {
				TomTaskPackageDto tomTaskPackageDto=new TomTaskPackageDto();
				try {
					BeanUtils.copyProperties(tomTaskPackageDto, tomTaskPackage);
					List<TomTaskCoursesRelation> taskCourses = taskCoursesRelationMapper.selectByPackageId(tomTaskPackage.getPackageId());
					objList=new ArrayList<Object>();
					if(taskCourses.size()>0){
						TomCoursesDto tomCoursesDto=null;
						for (TomTaskCoursesRelation tomTaskCoursesRelation : taskCourses) {
							TomCourses tomCourses = coursesMapper.selectByPrimaryKey(tomTaskCoursesRelation.getCourseId());
							tomCoursesDto=new TomCoursesDto();
							BeanUtils.copyProperties(tomCoursesDto, tomCourses);
							String lecturersId=tomCourses.getLecturers();
							if (null != lecturersId && !"".equals(lecturersId)) {
								String[] ids=lecturersId.split(",");
								lecturers=new ArrayList<TomLecturer>();
								for (String id : ids) {
									if(!"".equals(id)){
										TomLecturer tomLecturer =lecturerMapper.selectByPrimaryKey(Integer.valueOf(id));
										if(tomLecturer!=null){
											lecturers.add(tomLecturer);
										}
									}
							    }
							}
							tomCoursesDto.setLecturerLists(lecturers);
							tomCoursesDto.setIsCourse("Y");
							objList.add(tomCoursesDto);
						}
						
					}
					List<TomTaskExamPaperRelation> taskExamPapers = tomTaskExamPaperRelationMapper.selectByPackageId(tomTaskPackage.getPackageId());
					if(taskExamPapers.size()>0){
						for (TomTaskExamPaperRelation tomTaskExamPaperRelation : taskExamPapers) {
							TomExamPaper tomExamPaper = tomExamPaperMapper.selectByPrimaryKey(tomTaskExamPaperRelation.getExamPaperId());
							ExamPaperDto2 examPaperDto2=new ExamPaperDto2();
							if(tomExamPaper!=null){
								BeanUtils.copyProperties(examPaperDto2, tomExamPaper);
								examPaperDto2.setIsExamPaper("Y");
								objList.add(examPaperDto2);
							}
						}
					}
					tomTaskPackageDto.setTaskCoursesOrExamPapers(objList);
					
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				taskPackageDtoList.add(tomTaskPackageDto);
			}
		}
		page.setDate(taskPackageDtoList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
}
