package com.chinamobo.ue.course.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.dao.TomCourseClassifyRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseCommentMapper;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseOtherRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseSectionMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomTestMapper;
import com.chinamobo.ue.course.dto.CourseJsonDto;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourseClassifyRelation;
import com.chinamobo.ue.course.entity.TomCourseComment;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseOtherRelation;
import com.chinamobo.ue.course.entity.TomCourseSection;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomTest;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomLabelClassMapper;
import com.chinamobo.ue.system.dao.TomLabelClassRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelEmpRelationMapper;
import com.chinamobo.ue.system.dao.TomLabelMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.dao.TomRollingPictureMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomLabel;
import com.chinamobo.ue.system.entity.TomLabelClass;
import com.chinamobo.ue.system.entity.TomLabelEmpRelation;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.QRCodeUtil;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程service
 *
 * 作者: JCX
 * 创建时间: 2016年3月3日 下午3:39:13
 */
@Service
public class CourseService {
	
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private CourseClassifyService classifyService;
	@Autowired
	private TomCourseCommentMapper commentMapper;
	@Autowired
	private CourseSectionService courseSectionService;
	@Autowired
	private TomCourseSectionMapper courseSectionMapper;
	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	@Autowired
	private TomCourseClassifyMapper courseClassifyMapper;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	@Autowired
	private TomCourseClassifyRelationMapper courseClassifyRelationMapper;
	@Autowired
    private TomRollingPictureMapper rollingPictureMapper;
	@Autowired
	private TomTestMapper tomTestMapper;
	@Autowired
	private TomOrgMapper tomOrgMapper;
	@Autowired
	private TomCourseOtherRelationMapper tomCourseOtherRelationMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private TomLabelMapper tomLabelMapper;
	@Autowired
	private TomLabelClassMapper tomLabelClassMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomLabelEmpRelationMapper tomLabelEmpRelationMapper;
	@Autowired
	private TomLabelClassRelationMapper tomLabelClassRelationMapper;
	
	String filePath1=Config.getProperty("file_path");
	/**
	 * 
	 * 功能描述：[添加课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:39:25
	 * @param course
	 * @throws Exception 
	 */
	@Transactional
	public void addCourse(TomCourses course) throws Exception {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		course.setCourseNumber(numberRecordService.getNumber(MapManager.numberType("KC")));
		course.setCreateTime(new Date());
		course.setUpdateTime(new Date());
		course.setThumbUpCount(0);
		course.setViewers(0);
		course.setCommentCount(0);
		course.setCourseAverage(0d);
		course.setTotScore(0d);
		course.setLecturerAverage(0d);
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		coursesMapper.insertSelective(course);
		
		
//		course.setSectionDetailsList(null);
		//存储章节子表
		if(course.getSectionAddressList()!=null || course.getSectionDetailsList()!=null){
			TomCourseSection courseSection;
			for(int i=0;i<course.getSectionAddressList().size();i++){
				courseSection=new TomCourseSection();
				courseSection.setCourseId(course.getCourseId());
				if(course.getSectionAddressList().get(i).equals("")){
					continue;
				}
				courseSection.setSectionAddress(course.getSectionAddressList().get(i));
//				courseSection.setSectionDetails(course.getSectionDetailsList().get(i));
				courseSection.setSectionName(course.getSectionNameList().get(i));
				courseSection.setCreator(course.getCreator());
				courseSection.setCreatorId(course.getCreatorId());
				courseSection.setSectionType(course.getSectionType().get(i));
				courseSection.setOperator(course.getOperator());
				courseSection.setSectionSize(course.getSectionSize().get(i));
				courseSection.setSectionType(course.getSectionType().get(i));
				courseSection.setStatus("Y");
				courseSection.setCreateTime(new Date());
				courseSection.setUpdateTime(new Date());
				courseSection.setSectionSort(course.getSectionSort().get(i));
				courseSectionMapper.insertSelective(courseSection);
			}
			for(int i=0;i<course.getSectionDetailsList().size();i++){
				courseSection=new TomCourseSection();
				courseSection.setCourseId(course.getCourseId());
//				courseSection.setSectionAddress(course.getSectionAddressList().get(i));
				if(course.getSectionDetailsList().get(i).equals("")){
					continue;
				}
				courseSection.setSectionDetails(course.getSectionDetailsList().get(i));
				courseSection.setSectionName(course.getSectionNameList().get(i));
				courseSection.setCreator(course.getCreator());
				courseSection.setCreatorId(course.getCreatorId());
				courseSection.setSectionType(course.getSectionType().get(i));
				courseSection.setOperator(course.getOperator());
				courseSection.setSectionSize(course.getSectionSize().get(i));
				courseSection.setSectionType(course.getSectionType().get(i));
				courseSection.setStatus("Y");
				courseSection.setCreateTime(new Date());
				courseSection.setUpdateTime(new Date());
				courseSection.setSectionSort(course.getSectionSort().get(i));
				courseSectionMapper.insertSelective(courseSection);
			
				
			}
		}
		//存储关联关系
		TomCourseEmpRelation courseEmpRelation;
		List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
		for(String code:course.getEmpIds()){
			courseEmpRelation=new TomCourseEmpRelation();
			courseEmpRelation.setCode(code);
			courseEmpRelation.setCourseId(course.getCourseId());
			courseEmpRelation.setCreateTime(course.getCreateTime());
			courseEmpRelation.setFinishStatus("N");
			courseEmpRelation.setStatus("Y");
			courseEmpRelation.setCreator(course.getCreator());
			courseEmpRelation.setOperator(course.getOperator());
			courseEmpRelation.setUpdateTime(course.getCreateTime());
			courseEmpRelations.add(courseEmpRelation);
		}
		courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
		//存储课程分类子表
		TomCourseClassifyRelation courseClassifyRelation;
		for(int classifyId:course.getClassifyIds()){
			courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(course.getCreateTime());
			courseClassifyRelation.setUpdateTime(course.getCreateTime());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		if("N".equals(course.getCourseOnline())){

			QRCodeUtil.courseEncode("ele-sign-"+course.getCourseId(),course.getCourseName()+"签到二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setSignInTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"签到二维码-"+course.getCourseId()+".jpg");
			QRCodeUtil.courseEncode("ele-grade-"+course.getCourseId(),course.getCourseName()+"评分二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setGradeTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"评分二维码-"+course.getCourseId()+".jpg");
			coursesMapper.updateByPrimaryKeySelective(course);
		}
			
	}

	@Transactional
	public void addCourseNew(String json) throws Exception {
		ShiroUser user=ShiroUtils.getCurrentUser();
		JsonMapper mapper = new JsonMapper();
		CourseJsonDto courseJsonDto=mapper.fromJson(json, CourseJsonDto.class);
		TomCourses course=new TomCourses();
		PropertyUtils.copyProperties(course,courseJsonDto);
		course.setCourseNumber(numberRecordService.getNumber(MapManager.numberType("KC")));
		course.setCreateTime(new Date());
		course.setUpdateTime(new Date());
		course.setThumbUpCount(0);
		course.setViewers(0);
		course.setCommentCount(0);
		course.setCourseAverage(0d);
		course.setTotScore(0d);
		course.setLecturerAverage(0d);
		course.setCreator(user.getName());
		course.setCreatorId(user.getCode());
		course.setOperator(user.getName());
		course.setCourseDownloadable("N");
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {
		    ids+=id+",";
		}
		course.setCourseType(ids);
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		coursesMapper.insertSelective(course);
		
		//存储章节子表
		for(TomCourseSection courseSection:courseJsonDto.getSectionList()){
			courseSection.setCreateTime(new Date());
			courseSection.setCreator(course.getCreator());
			courseSection.setCreatorId(course.getCreatorId());
			courseSection.setCourseId(course.getCourseId());
			courseSection.setOperator(course.getOperator());
			courseSection.setStatus("Y");
			courseSection.setUpdateTime(new Date());
			courseSectionMapper.insertSelective(courseSection);
		}
		
		//关联部门下的人员
		if(course.getEmpIds().size()>0){
			TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
			List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
			for(String code:course.getEmpIds()){
				courseEmpRelation=new TomCourseEmpRelation();
				courseEmpRelation.setCode(code);
				courseEmpRelation.setCourseId(course.getCourseId());
				courseEmpRelation.setCreateTime(course.getCreateTime());
				courseEmpRelation.setFinishStatus("N");
				courseEmpRelation.setStatus("Y");
				courseEmpRelation.setCreator(course.getCreator());
				courseEmpRelation.setOperator(course.getOperator());
				courseEmpRelation.setUpdateTime(course.getCreateTime());
				courseEmpRelation.setType("E");
				if(courseEmpRelationMapper.selectByExample(courseEmpRelation).size()<1){
					courseEmpRelations.add(courseEmpRelation);
				}
			}
			courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
		}
		
		//关联部门
		List<String> deptIds = course.getDeptIds();
		if(deptIds.size()>0){
			for(String deptId : deptIds){
				TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
				List<TomEmp> empList = new ArrayList<TomEmp>();
				//添加课程其它关联表
				if(deptId.equals("1")){
					//查询公司信息
					TomOrg org = tomOrgMapper.selectByPrimaryKey(deptId);
					courseOtherRelation.setName(org.getName());
					//全查人员
					empList = tomEmpMapper.selectAllDept();
				}else{
					//查询部门信息
					TomDept dept = tomDeptMapper.selectByPrimaryKey(deptId);
					courseOtherRelation.setName(dept.getName());
					//查询部门下所有人
					empList = tomEmpMapper.selectByDeptCode(deptId);
				}
				courseOtherRelation.setCourseId(course.getCourseId());
				courseOtherRelation.setCode(deptId);
				courseOtherRelation.setStatus("Y");
				courseOtherRelation.setCreator(user.getName());
				courseOtherRelation.setCreatorId(user.getCode());
				courseOtherRelation.setCreateTime(new Date());
				courseOtherRelation.setOperator(user.getName());
				courseOtherRelation.setOperatorId(user.getCode());
				courseOtherRelation.setUpdateTime(new Date());
				courseOtherRelation.setType("D");
				Map<Object,Object> map = new HashMap<Object,Object>();
				map.put("courseId", course.getCourseId());
				map.put("code", deptId);
				map.put("type", "D");
				List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
				if(list.size()<1){
					tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
				}
				//课程人员关联表
				if(empList.size()>0){
					TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
					List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
					for(TomEmp emp : empList){
						courseEmpRelation=new TomCourseEmpRelation();
						courseEmpRelation.setCode(emp.getCode());
						courseEmpRelation.setCourseId(course.getCourseId());
						courseEmpRelation.setCreateTime(course.getCreateTime());
						courseEmpRelation.setFinishStatus("N");
						courseEmpRelation.setStatus("Y");
						courseEmpRelation.setCreator(course.getCreator());
						courseEmpRelation.setOperator(course.getOperator());
						courseEmpRelation.setUpdateTime(course.getCreateTime());
						courseEmpRelation.setType("D");
						if(courseEmpRelationMapper.selectByExample(courseEmpRelation).size()<1){
							courseEmpRelations.add(courseEmpRelation);
						}
					}
					courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
				}
			}
		}
		
		//关联标签下的人员
		List<String> labelEmps = course.getLabelEmps();
		if(labelEmps.size()>0){
			TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
			List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
			for(String labelEmp : labelEmps){
				courseEmpRelation=new TomCourseEmpRelation();
				courseEmpRelation.setCode(labelEmp);
				courseEmpRelation.setCourseId(course.getCourseId());
				courseEmpRelation.setCreateTime(course.getCreateTime());
				courseEmpRelation.setFinishStatus("N");
				courseEmpRelation.setStatus("Y");
				courseEmpRelation.setCreator(course.getCreator());
				courseEmpRelation.setOperator(course.getOperator());
				courseEmpRelation.setUpdateTime(course.getCreateTime());
				courseEmpRelation.setType("LE");
				if(courseEmpRelationMapper.selectByExample(courseEmpRelation).size()<1){
					courseEmpRelations.add(courseEmpRelation);
				}
			}
			courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
		}
		
		//关联标签
		List<String> labelIds = course.getLabelIds();
		if(labelIds.size()>0){
			for(String labelId : labelIds){
				TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
				//查询标签信息
				TomLabel label = tomLabelMapper.selectByPrimaryKey(labelId);
				courseOtherRelation.setCourseId(course.getCourseId());
				courseOtherRelation.setCode(labelId);
				courseOtherRelation.setName(label.getTagName());
				courseOtherRelation.setStatus("Y");
				courseOtherRelation.setCreator(user.getName());
				courseOtherRelation.setCreatorId(user.getCode());
				courseOtherRelation.setCreateTime(new Date());
				courseOtherRelation.setOperator(user.getName());
				courseOtherRelation.setOperatorId(user.getCode());
				courseOtherRelation.setUpdateTime(new Date());
				courseOtherRelation.setType("L");
				Map<Object,Object> map = new HashMap<Object,Object>();
				map.put("courseId", course.getCourseId());
				map.put("code", labelId);
				map.put("type", "L");
				List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
				if(list.size()<1){
					tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
				}
				//查询标签下的人员
				List<TomLabelEmpRelation> labelEmpList = tomLabelEmpRelationMapper.selectBytagIdList(labelId);
				TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
				List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
				//课程人员关联表
				if(labelEmpList.size()>0){
					for(TomLabelEmpRelation labelEmpRelation : labelEmpList){
						courseEmpRelation=new TomCourseEmpRelation();
						courseEmpRelation.setCode(labelEmpRelation.getCode());
						courseEmpRelation.setCourseId(course.getCourseId());
						courseEmpRelation.setCreateTime(course.getCreateTime());
						courseEmpRelation.setFinishStatus("N");
						courseEmpRelation.setStatus("Y");
						courseEmpRelation.setCreator(course.getCreator());
						courseEmpRelation.setOperator(course.getOperator());
						courseEmpRelation.setUpdateTime(course.getCreateTime());
						courseEmpRelation.setType("L");
						if(courseEmpRelationMapper.selectByExample(courseEmpRelation).size()<1){
							courseEmpRelations.add(courseEmpRelation);
						}
					}
					courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
				}
			}
		}
		
		//关联标签分类
		List<String> labelCladdIds = course.getLabelClassIds();
		if(labelCladdIds.size()>0){
			for(String labelClassId : labelCladdIds){
				TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
				//查询标签分类信息
				TomLabelClass labelClass = tomLabelClassMapper.selectByPrimaryKey(Integer.valueOf(labelClassId));
				courseOtherRelation.setCourseId(course.getCourseId());
				courseOtherRelation.setCode(labelClassId);
				courseOtherRelation.setName(labelClass.getClassName());
				courseOtherRelation.setStatus("Y");
				courseOtherRelation.setCreator(user.getName());
				courseOtherRelation.setCreatorId(user.getCode());
				courseOtherRelation.setCreateTime(new Date());
				courseOtherRelation.setOperator(user.getName());
				courseOtherRelation.setOperatorId(user.getCode());
				courseOtherRelation.setUpdateTime(new Date());
				courseOtherRelation.setType("C");
				Map<Object,Object> map = new HashMap<Object,Object>();
				map.put("courseId", course.getCourseId());
				map.put("code", labelClassId);
				map.put("type", "C");
				List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
				if(list.size()<1){
					tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
				}
				//查询标签分类下的人员
				List<TomLabelEmpRelation> labelClassEmpList = tomLabelClassRelationMapper.selectLabelClassEmpList(Integer.valueOf(labelClassId));
				TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
				List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
				if(labelClassEmpList.size()>0){
					for(TomLabelEmpRelation labelEmpRelation : labelClassEmpList){
						courseEmpRelation=new TomCourseEmpRelation();
						courseEmpRelation.setCode(labelEmpRelation.getCode());
						courseEmpRelation.setCourseId(course.getCourseId());
						courseEmpRelation.setCreateTime(course.getCreateTime());
						courseEmpRelation.setFinishStatus("N");
						courseEmpRelation.setStatus("Y");
						courseEmpRelation.setCreator(course.getCreator());
						courseEmpRelation.setOperator(course.getOperator());
						courseEmpRelation.setUpdateTime(course.getCreateTime());
						courseEmpRelation.setType("C");
						if(courseEmpRelationMapper.selectByExample(courseEmpRelation).size()<1){
							courseEmpRelations.add(courseEmpRelation);
						}
					}
					courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
				}
			}
		}
		
		//存储课程分类子表
		TomCourseClassifyRelation courseClassifyRelation;
		for(int classifyId:course.getClassifyIds()){
			courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(course.getCreateTime());
			courseClassifyRelation.setUpdateTime(course.getCreateTime());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		if("N".equals(course.getCourseOnline())){
			QRCodeUtil.courseEncode("ele-sign-"+course.getCourseId(),course.getCourseName()+"签到二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setSignInTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"签到二维码-"+course.getCourseId()+".jpg");
			QRCodeUtil.courseEncode("ele-grade-"+course.getCourseId(),course.getCourseName()+"评分二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setGradeTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"评分二维码-"+course.getCourseId()+".jpg");
			coursesMapper.updateByPrimaryKeySelective(course);
		}
	}
	
	/**
	 * 
	 * 功能描述：[查询课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:39:40
	 * @param courseId
	 * @return
	 */
	public TomCourses selectCourseById(int courseId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourses courses= coursesMapper.selectByPrimaryKey(courseId);
		
		//封装章节信息
		TomCourseSection example=new TomCourseSection();
		example.setCourseId(courseId);
		List<TomCourseSection> courseSections=courseSectionService.selectListByEXample(example);
		List<Integer> sectionId=new ArrayList<Integer>();
		List<String> sectionNameList=new ArrayList<String>();
		List<String> sectionAddressList=new ArrayList<String>();
		List<String> sectionDetailsList=new ArrayList<String>();
		List<String> sectionType=new ArrayList<String>();
		List<Integer> sectionSize=new ArrayList<Integer>();
		List<Integer> sectionSort=new ArrayList<Integer>();
		for(TomCourseSection courseSection:courseSections){
			sectionId.add(courseSection.getSectionId());
			sectionNameList.add(courseSection.getSectionName());
			sectionAddressList.add(courseSection.getSectionAddress());
			sectionDetailsList.add(courseSection.getSectionDetails());
			sectionType.add(courseSection.getSectionType());
			sectionSize.add(courseSection.getSectionSize());
			sectionSort.add(courseSection.getSectionSort());
		}
		courses.setSectionAddressList(sectionAddressList);
 		courses.setSectionDetailsList(sectionDetailsList);
		courses.setSectionId(sectionId);
		courses.setSectionNameList(sectionNameList);
		courses.setSectionType(sectionType);
		courses.setSectionSize(sectionSize);
		courses.setSectionSort(sectionSort);
		//封装员工关联
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		map.put("createTime", courses.getCreateTime());
		map.put("status", "Y");
		List<TomEmp> emps = empMapper.selectListByCourseId(map);
		List<String> codes=new ArrayList<String>();
		List<String> names=new ArrayList<String>();
		List<String> citynames=new ArrayList<String>();
		List<String> deptnames=new ArrayList<String>();
		for(TomEmp emp:emps){
			codes.add(emp.getCode());
			names.add(emp.getName());
			citynames.add(emp.getCityname());
			deptnames.add(emp.getDeptname());
		}
		courses.setEmpIds(codes);
		courses.setEmpNames(names);
		courses.setCityname(citynames);
		courses.setDeptname(deptnames);
		//封装课程分类
		List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courseId);
		List<Integer> classifyIds=new ArrayList<Integer>();
		List<String> classifyNames=new ArrayList<String>();
		for(TomCourseClassify courseClassify:courseClassifies){
			classifyIds.add(courseClassify.getClassifyId());
			classifyNames.add(courseClassify.getClassifyName());
		}
		courses.setClassifyIds(classifyIds);
		courses.setClassifyNames(classifyNames);
		
		return courses;
	}
	
	public CourseJsonDto selectCourseDetail(int courseId) throws Exception {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		CourseJsonDto dto=new CourseJsonDto();
		TomCourses courses= coursesMapper.selectByPrimaryKey(courseId);
		
		//封装章节信息
		TomCourseSection example=new TomCourseSection();
		example.setCourseId(courseId);
		List<TomCourseSection> courseSections=courseSectionService.selectListByEXample(example);
		List<Integer> sectionId=new ArrayList<Integer>();
		List<String> sectionNameList=new ArrayList<String>();
		List<String> sectionAddressList=new ArrayList<String>();
		List<String> sectionDetailsList=new ArrayList<String>();
		List<String> sectionType=new ArrayList<String>();
		List<Integer> sectionSize=new ArrayList<Integer>();
		List<Integer> sectionSort=new ArrayList<Integer>();
		for(TomCourseSection courseSection:courseSections){
			sectionId.add(courseSection.getSectionId());
			sectionNameList.add(courseSection.getSectionName());
			sectionAddressList.add(courseSection.getSectionAddress());
			sectionDetailsList.add(courseSection.getSectionDetails());
			sectionType.add(courseSection.getSectionType());
			sectionSize.add(courseSection.getSectionSize());
			sectionSort.add(courseSection.getSectionSort());
		}
		courses.setSectionAddressList(sectionAddressList);
 		courses.setSectionDetailsList(sectionDetailsList);
		courses.setSectionId(sectionId);
		courses.setSectionNameList(sectionNameList);
		courses.setSectionType(sectionType);
		courses.setSectionSize(sectionSize);
		courses.setSectionSort(sectionSort);
		//封装员工关联
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
//		List<TomEmp> emps = empMapper.selectListByCourseId(mapDeptEmp);
//		List<String> codes=new ArrayList<String>();
//		List<String> names=new ArrayList<String>();
//		List<String> citynames=new ArrayList<String>();
//		List<String> deptnames=new ArrayList<String>();
//		for(TomEmp emp:emps){
//			codes.add(emp.getCode());
//			names.add(emp.getName());
//			citynames.add(emp.getCityname());
//			deptnames.add(emp.getDeptname());
//		}
//		courses.setEmpIds(codes);
//		courses.setEmpNames(names);
//		courses.setCityname(citynames);
//		courses.setDeptname(deptnames);
		
		//查询部门下的人员
		Map<Object,Object> mapDeptEmp = new HashMap<Object,Object>();
		mapDeptEmp.put("courseId", courseId);
		mapDeptEmp.put("createTime", courses.getCreateTime());
		mapDeptEmp.put("status", "Y");
		mapDeptEmp.put("type", "E");
		List<TomEmp> deptEmps = empMapper.selectListByCourseId(mapDeptEmp);
		if(deptEmps.size()>0){			
			for(TomEmp deptEmp : deptEmps){
				Map<String, String> deptEmpMap = new HashMap<String, String>();
				deptEmpMap.put("code", deptEmp.getCode());
				deptEmpMap.put("name", deptEmp.getName());
				deptEmpMap.put("type", "E");
				emps.add(deptEmpMap);
			}
		}
		
		//查询部门关联
		Map<Object,Object> mapDept = new HashMap<Object,Object>();
		mapDept.put("courseId", courseId);
		mapDept.put("type", "D");
		List<TomCourseOtherRelation> deptList = tomCourseOtherRelationMapper.selectOtherList(mapDept);
		if(deptList.size()>0){			
			for(TomCourseOtherRelation courseOtherRelation : deptList){
				Map<String, String> deptMap = new HashMap<String, String>();
				deptMap.put("code", courseOtherRelation.getCode());
				deptMap.put("name", courseOtherRelation.getName());
				deptMap.put("type", "D");
				deptMap.put("statuss", "1");
				emps.add(deptMap);
			}
		}
		
		//查询标签下的人员
		Map<Object,Object> mapLabelEmp = new HashMap<Object,Object>();
		mapLabelEmp.put("courseId", courseId);
		mapLabelEmp.put("createTime", courses.getCreateTime());
		mapLabelEmp.put("status", "Y");
		mapLabelEmp.put("type", "LE");
		List<TomEmp> labelEmps = empMapper.selectListByCourseId(mapLabelEmp);
		if(labelEmps.size()>0){			
			for(TomEmp labelEmp : labelEmps){
				Map<String, String> labelEmpMap = new HashMap<String, String>();
				labelEmpMap.put("code", labelEmp.getCode());
				labelEmpMap.put("name", labelEmp.getName());
				labelEmpMap.put("type", "LE");
				emps.add(labelEmpMap);
			}
		}
		
		//查询标签关联
		Map<Object,Object> mapLabel = new HashMap<Object,Object>();
		mapLabel.put("courseId", courseId);
		mapLabel.put("type", "L");
		List<TomCourseOtherRelation> labelList = tomCourseOtherRelationMapper.selectOtherList(mapLabel);
		if(labelList.size()>0){			
			for(TomCourseOtherRelation courseOtherRelation : labelList){
				Map<String, String> labelMap = new HashMap<String, String>();
				labelMap.put("code", courseOtherRelation.getCode());
				labelMap.put("name", courseOtherRelation.getName());
				labelMap.put("type", "L");
				labelMap.put("statuss", "3");
				emps.add(labelMap);
			}
		}
		
		//查询标签分类关联
		Map<Object,Object> mapLabelClass = new HashMap<Object,Object>();
		mapLabelClass.put("courseId", courseId);
		mapLabelClass.put("type", "C");
		List<TomCourseOtherRelation> labelClassList = tomCourseOtherRelationMapper.selectOtherList(mapLabelClass);
		if(labelClassList.size()>0){			
			for(TomCourseOtherRelation courseOtherRelation : labelClassList){
				Map<String, String> labelClassMap = new HashMap<String, String>();
				labelClassMap.put("code", courseOtherRelation.getCode());
				labelClassMap.put("name", courseOtherRelation.getName());
				labelClassMap.put("type", "C");
				labelClassMap.put("statuss", "1");
				emps.add(labelClassMap);
			}
		}
		
		courses.setEmps(emps);
		
		//封装课程分类
		List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courseId);
		List<Integer> classifyIds=new ArrayList<Integer>();
		List<String> classifyNames=new ArrayList<String>();
		for(TomCourseClassify courseClassify:courseClassifies){
			classifyIds.add(courseClassify.getClassifyId());
			classifyNames.add(courseClassify.getClassifyName());
		}
		courses.setClassifyIds(classifyIds);
		courses.setClassifyNames(classifyNames);
		PropertyUtils.copyProperties(dto,courses);
		dto.setSectionList(courseSections);
		return dto;
	}

	/**
	 * 
	 * 功能描述：[分页查询课程,pageSize=-1则不分页]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:39:59
	 * @param pageNum
	 * @param pageSize
	 * @param example
	 * @return
	 */
	public PageData<TomCourses> selectListByPage(int pageNum, int pageSize,TomCourses example) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page=new PageData<TomCourses>();	
		List<TomCourses> list;
		int count=coursesMapper.countByExample(example);
		
		if(pageSize==-1){
			pageSize=count;
		}
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum",pageSize);//  pageNum*
		map.put("example", example);
		map.put("listOrder", "UPDATE_TIME");
		map.put("orderBy", "desc");
		list = coursesMapper.selectListByPage(map);
		
		TomCourseLearningRecord learningRecordExample;
		TomCourseSignInRecords courseSignInRecordExample;
		for(TomCourses courses:list){
			List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courses.getCourseId());
			String courseType="";
			for(int i=0;i<courseClassifies.size();i++){
				if(i==0){
					courseType+=courseClassifies.get(i).getClassifyName();
				}else{
					courseType+=","+courseClassifies.get(i).getClassifyName();
				}
			}
			courses.setCourseType(courseType);
			
			if(courses.getCourseOnline().equals("Y")){
				learningRecordExample=new TomCourseLearningRecord();
				learningRecordExample.setCourseId(courses.getCourseId());
				courses.setLearnedNum(courseLearningRecordMapper.countByExample(learningRecordExample));
			}else{
				courseSignInRecordExample=new TomCourseSignInRecords();
				courseSignInRecordExample.setCourseId(courses.getCourseId());
				courses.setLearnedNum(courseSignInRecordsMapper.countByExample(courseSignInRecordExample));
			}
//			Map<Object,Object> map2 = new HashMap<Object,Object>();
//			List<TomExportTestDto> tomTest=tomTestMapper.selectByCourseId(map2);
//判断表tom_test是否有数据			
			List<TomTest> tomTest=tomTestMapper.selectByCourseIdEX(courses.getCourseId());
			
			if(tomTest.size()>0){
				courses.setTestIfNull("Y");
			}else{
				courses.setTestIfNull("N");
			}
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}

	/**
	 * 
	 * 功能描述：[更新课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:40:13
	 * @param course
	 * @throws Exception 
	 */
	@Transactional
	public void updateCourse(TomCourses course) throws Exception {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		course.setUpdateTime(new Date());
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		
		coursesMapper.updateByPrimaryKeySelective(course);
		
		
		TomCourseSection courseSectionExample=new TomCourseSection();
		//更新章节子表

		if(course.getSectionAddressList()!=null || course.getSectionDetailsList()!=null){
			
			courseSectionExample.setStatus("N");
			courseSectionExample.setCourseId(course.getCourseId());
			courseSectionMapper.updateByPrimaryKeySelective(courseSectionExample);
			
			TomCourseSection courseSection;
			for(int i=0;i<course.getSectionAddressList().size();i++){
				courseSection=new TomCourseSection();
				if(course.getSectionId().get(i)!=-1){
					courseSection.setSectionId(course.getSectionId().get(i));
					courseSection.setStatus("Y");
					courseSectionMapper.updateByPrimaryKeySelective(courseSection);
					
				}else{
					courseSection.setCourseId(course.getCourseId());
					if(course.getSectionAddressList().get(i).equals("")){
						continue;
					}
					courseSection.setSectionAddress(course.getSectionAddressList().get(i));
//					courseSection.setSectionDetails(course.getSectionDetailsList().get(i));
					courseSection.setSectionName(course.getSectionNameList().get(i));
					courseSection.setCreator(course.getCreator());
					courseSection.setOperator(course.getOperator());
					courseSection.setStatus("Y");
					courseSection.setSectionSize(course.getSectionSize().get(i));
					courseSection.setSectionType(course.getSectionType().get(i));
					courseSectionMapper.insertSelective(courseSection);
				}
			}
			//更新删除的章节
//			TomCourseSection example=new TomCourseSection();
//			example.setCourseId(course.getCourseId());
//			List<TomCourseSection> courseSections=courseSectionMapper.selectListByExample(example);
//			for(TomCourseSection courseSection2:courseSections){
//				boolean isDelete=true;
//				for(int id:course.getSectionId()){
//					if(id==courseSection2.getSectionId()){
//						isDelete=false;
//						break;
//					}
//				}
//				if(isDelete==true){
//					courseSection2.setStatus("N");
//					courseSection2.setUpdateTime(new Date());
//					courseSectionMapper.updateByPrimaryKeySelective(courseSection2);
//				}
//			}

		}

		 if(course.getSectionDetailsList()!=null){
			courseSectionExample.setStatus("N");
			courseSectionExample.setCourseId(course.getCourseId());
			courseSectionMapper.updateByPrimaryKeySelective(courseSectionExample);
			
			TomCourseSection courseSection;
			for(int i=0;i<course.getSectionDetailsList().size();i++){
				courseSection=new TomCourseSection();
				if(course.getSectionId().get(i)!=-1){
					courseSection.setSectionId(course.getSectionId().get(i));
					courseSection.setStatus("Y");
					courseSectionMapper.updateByPrimaryKeySelective(courseSection);
					
				}else{
					courseSection.setCourseId(course.getCourseId());
//					courseSection.setSectionAddress(course.getSectionAddressList().get(i));
					courseSection.setSectionDetails(course.getSectionDetailsList().get(i));
					courseSection.setSectionName(course.getSectionNameList().get(i));
					courseSection.setCreator(course.getCreator());
					courseSection.setOperator(course.getOperator());
					courseSection.setStatus("Y");
					courseSection.setSectionSize(course.getSectionSize().get(i));
					courseSection.setSectionType(course.getSectionType().get(i));
					courseSectionMapper.insertSelective(courseSection);
				}
			}
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//伪删除员工关联
		TomCourses courses=coursesMapper.selectByPrimaryKey(course.getCourseId());
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomCourseEmpRelation courseEmpRelationExample=new TomCourseEmpRelation();
		courseEmpRelationExample.setCourseId(course.getCourseId());
		courseEmpRelationExample.setCreateTime(courses.getCreateTime());
		courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
		if ("Y".equals(course.getCourseOnline())) {
			//删除签到记录
			TomCourseSignInRecords courseSignInRecords=new TomCourseSignInRecords();
			courseSignInRecords.setCourseId(course.getCourseId());
			courseSignInRecordsMapper.deleteByExample(courseSignInRecords);
			//更新员工关联关系
			TomCourseEmpRelation courseEmpRelation;
			for (String code : course.getEmpIds()) {
				courseEmpRelation = new TomCourseEmpRelation();
				courseEmpRelation.setCode(code);
				courseEmpRelation.setCourseId(course.getCourseId());
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.SLAVE);
				List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper.selectByExample(courseEmpRelation);
				//设置主从库查询
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				if(courseEmpRelations.size()==0){
					courseEmpRelation.setCreateTime(courses.getCreateTime());
					courseEmpRelation.setFinishStatus("N");
					courseEmpRelation.setStatus("Y");
					courseEmpRelation.setUpdateTime(new Date());
					courseEmpRelation.setCreator(course.getCreator());
					courseEmpRelation.setOperator(course.getOperator());
					courseEmpRelationMapper.insertSelective(courseEmpRelation);
				}else{
					courseEmpRelation=courseEmpRelations.get(0);
					courseEmpRelation.setStatus("Y");
					courseEmpRelation.setUpdateTime(new Date());
					courseEmpRelation.setOperator(course.getOperator());
					courseEmpRelationMapper.update(courseEmpRelation);
				}
				
			}
		}else{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			//删除线上课程学习记录
			TomCourseLearningRecord courseLearningRecord=new TomCourseLearningRecord();
			courseLearningRecord.setCourseId(course.getCourseId());
			courseLearningRecordMapper.deleteByExample(courseLearningRecord);
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		//删除课程分类
		TomCourseClassifyRelation classifyRelationExample=new TomCourseClassifyRelation();
		classifyRelationExample.setCourseId(course.getCourseId());
		courseClassifyRelationMapper.deleteByExample(classifyRelationExample);
		//存储课程分类子表
		TomCourseClassifyRelation courseClassifyRelation;
		for(int classifyId:course.getClassifyIds()){
			courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(new Date());
			courseClassifyRelation.setUpdateTime(new Date());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		
		//修改轮播图名称
		Map<Object,Object> map=new HashMap<Object, Object>();
	    map.put("resourceId",course.getCourseId());
	    map.put("pictureCategory", "C");
	    map.put("resourceName", course.getCourseName());
	    rollingPictureMapper.updateResourceName(map);
		if("N".equals(course.getCourseOnline())){

			QRCodeUtil.courseEncode("ele-sign-"+course.getCourseId(),course.getCourseName()+"签到二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setSignInTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"签到二维码-"+course.getCourseId()+".jpg");
			QRCodeUtil.courseEncode("ele-grade-"+course.getCourseId(),course.getCourseName()+"评分二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setGradeTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"评分二维码-"+course.getCourseId()+".jpg");
			coursesMapper.updateByPrimaryKeySelective(course);
		}
			
	}

	@Transactional
	public void updateCourseNew(String json) throws Exception {
		ShiroUser user=ShiroUtils.getCurrentUser();
		JsonMapper mapper = new JsonMapper();
		CourseJsonDto courseJsonDto=mapper.fromJson(json, CourseJsonDto.class);
		TomCourses course=new TomCourses();
		PropertyUtils.copyProperties(course,courseJsonDto);
		course.setUpdateTime(new Date());
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		course.setOperator(user.getName());
		course.setCourseDownloadable("N");
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		coursesMapper.updateByPrimaryKeySelective(course);
		
		//更新章节子表
		TomCourseSection courseSectionExample=new TomCourseSection();
		courseSectionExample.setStatus("N");
		courseSectionExample.setCourseId(course.getCourseId());
		courseSectionMapper.updateByPrimaryKeySelective(courseSectionExample);
		for(TomCourseSection courseSection:courseJsonDto.getSectionList()){
			if(courseSection.getSectionId()!=-1){
				courseSection.setStatus("Y");
				courseSectionMapper.updateByPrimaryKeySelective(courseSection);
			}else {
				courseSection.setCourseId(course.getCourseId());
				courseSection.setCreator(course.getCreator());
				courseSection.setOperator(course.getOperator());
				courseSection.setStatus("Y");
				courseSection.setCreateTime(new Date());
				courseSection.setUpdateTime(new Date());
				courseSectionMapper.insertSelective(courseSection);
			}
		}
		
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//伪删除员工关联
		TomCourses courses=coursesMapper.selectByPrimaryKey(course.getCourseId());
		TomCourseEmpRelation courseEmpRelationExample=new TomCourseEmpRelation();
		courseEmpRelationExample.setCourseId(course.getCourseId());
		courseEmpRelationExample.setCreateTime(courses.getCreateTime());
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
		//删除课程其它关联
		tomCourseOtherRelationMapper.deleteByCourseId(course.getCourseId());
		if ("Y".equals(course.getCourseOnline())) {
			//删除签到记录
			TomCourseSignInRecords courseSignInRecords=new TomCourseSignInRecords();
			courseSignInRecords.setCourseId(course.getCourseId());
			courseSignInRecordsMapper.deleteByExample(courseSignInRecords);
			//更新员工关联关系
//			List<TomCourseEmpRelation> tomCourseEmpRelations = new ArrayList<TomCourseEmpRelation>();
//			for (String code : course.getEmpIds()) {
//				TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
//				courseEmpRelation.setCode(code);
//				courseEmpRelation.setCourseId(course.getCourseId());
//				courseEmpRelation.setCreateTime(courses.getCreateTime());
//				courseEmpRelation.setFinishStatus("N");
//				courseEmpRelation.setStatus("Y");
//				courseEmpRelation.setUpdateTime(new Date());
//				courseEmpRelation.setCreator(course.getCreator());
//				courseEmpRelation.setOperator(course.getOperator());
//				tomCourseEmpRelations.add(courseEmpRelation);
//			}
//			if(tomCourseEmpRelations != null && tomCourseEmpRelations.size() > 0)
//			courseEmpRelationMapper.insertSelectiveBatch(tomCourseEmpRelations);
			//关联部门下的人员
			if(course.getEmpIds().size()>0){
				TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
				List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
				for(String code:course.getEmpIds()){
					courseEmpRelation=new TomCourseEmpRelation();
					courseEmpRelation.setCode(code);
					courseEmpRelation.setCourseId(course.getCourseId());
					courseEmpRelation.setCreateTime(course.getCreateTime());
					courseEmpRelation.setFinishStatus("N");
					courseEmpRelation.setStatus("Y");
					courseEmpRelation.setCreator(course.getCreator());
					courseEmpRelation.setOperator(course.getOperator());
					courseEmpRelation.setUpdateTime(course.getCreateTime());
					courseEmpRelation.setType("E");
					List<TomCourseEmpRelation> courseEmpRelationList = courseEmpRelationMapper.selectByExample(courseEmpRelation);
					if(courseEmpRelationList.size()<1){
						courseEmpRelations.add(courseEmpRelation);
					}else{
						for(TomCourseEmpRelation courseEmp : courseEmpRelationList){
							if(courseEmp.getStatus().equals("N")){
								courseEmpRelationMapper.update(courseEmpRelation);
							}
						}
					}
				}
				if(courseEmpRelations.size()>0){
					courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
				}
			}
			
			//关联部门
			List<String> deptIds = course.getDeptIds();
			if(deptIds.size()>0){
				for(String deptId : deptIds){
					TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
					List<TomEmp> empList = new ArrayList<TomEmp>();
					//添加课程其它关联表
					if(deptId.equals("1")){
						//查询公司信息
						TomOrg org = tomOrgMapper.selectByPrimaryKey(deptId);
						courseOtherRelation.setName(org.getName());
						//全查人员
						empList = tomEmpMapper.selectAllDept();
					}else{
						//查询部门信息
						TomDept dept = tomDeptMapper.selectByPrimaryKey(deptId);
						courseOtherRelation.setName(dept.getName());
						//查询部门下所有人
						empList = tomEmpMapper.selectByDeptCode(deptId);
					}
					courseOtherRelation.setCourseId(course.getCourseId());
					courseOtherRelation.setCode(deptId);
					courseOtherRelation.setStatus("Y");
					courseOtherRelation.setCreator(user.getName());
					courseOtherRelation.setCreatorId(user.getCode());
					courseOtherRelation.setCreateTime(new Date());
					courseOtherRelation.setOperator(user.getName());
					courseOtherRelation.setOperatorId(user.getCode());
					courseOtherRelation.setUpdateTime(new Date());
					courseOtherRelation.setType("D");
					Map<Object,Object> map = new HashMap<Object,Object>();
					map.put("courseId", course.getCourseId());
					map.put("code", deptId);
					map.put("type", "D");
					List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
					if(list.size()<1){
						tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
					}
					//课程人员关联表
					if(empList.size()>0){
						TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
						List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
						for(TomEmp emp : empList){
							courseEmpRelation=new TomCourseEmpRelation();
							courseEmpRelation.setCode(emp.getCode());
							courseEmpRelation.setCourseId(course.getCourseId());
							courseEmpRelation.setCreateTime(course.getCreateTime());
							courseEmpRelation.setFinishStatus("N");
							courseEmpRelation.setStatus("Y");
							courseEmpRelation.setCreator(course.getCreator());
							courseEmpRelation.setOperator(course.getOperator());
							courseEmpRelation.setUpdateTime(course.getCreateTime());
							courseEmpRelation.setType("D");
							if(courseEmpRelationMapper.selectByExample(courseEmpRelation).size()<1){
								courseEmpRelations.add(courseEmpRelation);
							}
						}
						courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
					}
				}
			}
			
			//关联标签下的人员
			List<String> labelEmps = course.getLabelEmps();
			if(labelEmps.size()>0){
				TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
				List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
				for(String labelEmp : labelEmps){
					courseEmpRelation=new TomCourseEmpRelation();
					courseEmpRelation.setCode(labelEmp);
					courseEmpRelation.setCourseId(course.getCourseId());
					courseEmpRelation.setCreateTime(course.getCreateTime());
					courseEmpRelation.setFinishStatus("N");
					courseEmpRelation.setStatus("Y");
					courseEmpRelation.setCreator(course.getCreator());
					courseEmpRelation.setOperator(course.getOperator());
					courseEmpRelation.setUpdateTime(course.getCreateTime());
					courseEmpRelation.setType("LE");
					List<TomCourseEmpRelation> courseEmpRelationList = courseEmpRelationMapper.selectByExample(courseEmpRelation);
					if(courseEmpRelationList.size()<1){
						courseEmpRelations.add(courseEmpRelation);
					}else{
						for(TomCourseEmpRelation courseEmp : courseEmpRelationList){
							if(courseEmp.getStatus().equals("N")){
								courseEmpRelationMapper.update(courseEmpRelation);
							}
						}
					}
				}
				if(courseEmpRelations.size()>0){
					courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
				}
			}
			
			//关联标签
			List<String> labelIds = course.getLabelIds();
			if(labelIds.size()>0){
				for(String labelId : labelIds){
					TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
					//查询标签信息
					TomLabel label = tomLabelMapper.selectByPrimaryKey(labelId);
					courseOtherRelation.setCourseId(course.getCourseId());
					courseOtherRelation.setCode(labelId);
					courseOtherRelation.setName(label.getTagName());
					courseOtherRelation.setStatus("Y");
					courseOtherRelation.setCreator(user.getName());
					courseOtherRelation.setCreatorId(user.getCode());
					courseOtherRelation.setCreateTime(new Date());
					courseOtherRelation.setOperator(user.getName());
					courseOtherRelation.setOperatorId(user.getCode());
					courseOtherRelation.setUpdateTime(new Date());
					courseOtherRelation.setType("L");
					Map<Object,Object> map = new HashMap<Object,Object>();
					map.put("courseId", course.getCourseId());
					map.put("code", labelId);
					map.put("type", "L");
					List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
					if(list.size()<1){
						tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
					}
					//查询标签下的人员
					List<TomLabelEmpRelation> labelEmpList = tomLabelEmpRelationMapper.selectBytagIdList(labelId);
					TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
					List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
					//课程人员关联表
					if(labelEmpList.size()>0){
						for(TomLabelEmpRelation labelEmpRelation : labelEmpList){
							courseEmpRelation=new TomCourseEmpRelation();
							courseEmpRelation.setCode(labelEmpRelation.getCode());
							courseEmpRelation.setCourseId(course.getCourseId());
							courseEmpRelation.setCreateTime(course.getCreateTime());
							courseEmpRelation.setFinishStatus("N");
							courseEmpRelation.setStatus("Y");
							courseEmpRelation.setCreator(course.getCreator());
							courseEmpRelation.setOperator(course.getOperator());
							courseEmpRelation.setUpdateTime(course.getCreateTime());
							courseEmpRelation.setType("L");
							if(courseEmpRelationMapper.selectByExample(courseEmpRelation).size()<1){
								courseEmpRelations.add(courseEmpRelation);
							}
						}
						courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
					}
				}
			}
			
			//关联标签分类
			List<String> labelCladdIds = course.getLabelClassIds();
			if(labelCladdIds.size()>0){
				for(String labelClassId : labelCladdIds){
					TomCourseOtherRelation courseOtherRelation = new TomCourseOtherRelation();
					//查询标签分类信息
					TomLabelClass labelClass = tomLabelClassMapper.selectByPrimaryKey(Integer.valueOf(labelClassId));
					courseOtherRelation.setCourseId(course.getCourseId());
					courseOtherRelation.setCode(labelClassId);
					courseOtherRelation.setName(labelClass.getClassName());
					courseOtherRelation.setStatus("Y");
					courseOtherRelation.setCreator(user.getName());
					courseOtherRelation.setCreatorId(user.getCode());
					courseOtherRelation.setCreateTime(new Date());
					courseOtherRelation.setOperator(user.getName());
					courseOtherRelation.setOperatorId(user.getCode());
					courseOtherRelation.setUpdateTime(new Date());
					courseOtherRelation.setType("C");
					Map<Object,Object> map = new HashMap<Object,Object>();
					map.put("courseId", course.getCourseId());
					map.put("code", labelClassId);
					map.put("type", "C");
					List<TomCourseOtherRelation> list = tomCourseOtherRelationMapper.selectOtherList(map);
					if(list.size()<1){
						tomCourseOtherRelationMapper.insertSelective(courseOtherRelation);
					}
					//查询标签分类下的人员
					List<TomLabelEmpRelation> labelClassEmpList = tomLabelClassRelationMapper.selectLabelClassEmpList(Integer.valueOf(labelClassId));
					TomCourseEmpRelation courseEmpRelation = new TomCourseEmpRelation();
					List<TomCourseEmpRelation> courseEmpRelations = new ArrayList<TomCourseEmpRelation>();
					if(labelClassEmpList.size()>0){
						for(TomLabelEmpRelation labelEmpRelation : labelClassEmpList){
							courseEmpRelation=new TomCourseEmpRelation();
							courseEmpRelation.setCode(labelEmpRelation.getCode());
							courseEmpRelation.setCourseId(course.getCourseId());
							courseEmpRelation.setCreateTime(course.getCreateTime());
							courseEmpRelation.setFinishStatus("N");
							courseEmpRelation.setStatus("Y");
							courseEmpRelation.setCreator(course.getCreator());
							courseEmpRelation.setOperator(course.getOperator());
							courseEmpRelation.setUpdateTime(course.getCreateTime());
							courseEmpRelation.setType("C");
							if(courseEmpRelationMapper.selectByExample(courseEmpRelation).size()<1){
								courseEmpRelations.add(courseEmpRelation);
							}
						}
						courseEmpRelationMapper.insertSelectiveBatch(courseEmpRelations);
					}
				}
			}
			
		}else{
			//设置主从库查询
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			//删除线上课程学习记录
			TomCourseLearningRecord courseLearningRecord=new TomCourseLearningRecord();
			courseLearningRecord.setCourseId(course.getCourseId());
			courseLearningRecordMapper.deleteByExample(courseLearningRecord);
		}
		//设置主从库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		//删除课程分类
		TomCourseClassifyRelation classifyRelationExample=new TomCourseClassifyRelation();
		classifyRelationExample.setCourseId(course.getCourseId());
		courseClassifyRelationMapper.deleteByExample(classifyRelationExample);
		//存储课程分类子表
		TomCourseClassifyRelation courseClassifyRelation;
		for(int classifyId:course.getClassifyIds()){
			courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(new Date());
			courseClassifyRelation.setUpdateTime(new Date());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		
		//修改轮播图名称
		Map<Object,Object> map=new HashMap<Object, Object>();
	    map.put("resourceId",course.getCourseId());
	    map.put("pictureCategory", "C");
	    map.put("resourceName", course.getCourseName());
	    rollingPictureMapper.updateResourceName(map);
		if("N".equals(course.getCourseOnline())){
			QRCodeUtil.courseEncode("ele-sign-"+course.getCourseId(),course.getCourseName()+"签到二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setSignInTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"签到二维码-"+course.getCourseId()+".jpg");
			QRCodeUtil.courseEncode("ele-grade-"+course.getCourseId(),course.getCourseName()+"评分二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setGradeTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"评分二维码-"+course.getCourseId()+".jpg");
			coursesMapper.updateByPrimaryKeySelective(course);
		}
	}
	
	/**
	 * 
	 * 功能描述：[课程上架/下架处理]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:40:30
	 * @param course
	 */
	public void updateStatus(TomCourses course) {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		course.setUpdateTime(new Date());
		coursesMapper.updateByPrimaryKeySelective(course);
	}

	
	/**
	 * 功能描述：[课程统计]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月9日 上午10:16:49
	 * @param courseName
	 */
	public List<TomCourses> courseCountsList(String courseName) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return coursesMapper.courseCountsList(courseName);		
	}

	public List<TomCourses> selectCourseByLecturerId(int lecturerId) {
		//设置从库查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return coursesMapper.selectByLecturerId(lecturerId);
	}

	/**
	 * 
	 * 功能描述：[更新课程评论数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月15日 下午3:59:04
	 * @param course
	 */
	@Transactional
	public void updateComment(Integer courseId) {
		//设置主库查询
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomCourseComment example=new TomCourseComment();
		example.setCourseId(courseId);
//		example.setStatus("Y");
		int commentCount=commentMapper.countByExample(example);
		
		TomCourses course=new TomCourses();
		course.setCourseId(courseId);
		course.setCommentCount(commentCount);
		coursesMapper.updateByPrimaryKeySelective(course);
	}

public PageData<TomCourses> selectListByPageForRooling(int pageNum, int pageSize,TomCourses example) {
	//设置从库查询
	DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page=new PageData<TomCourses>();	
		List<TomCourses> list;
		int count=coursesMapper.countByExampleForRooling(example);
		
		if(pageSize==-1){
			pageSize=count;
		}
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		map.put("example", example);
		map.put("listOrder", "UPDATE_TIME");
		map.put("orderBy", "desc");
		list = coursesMapper.selectListByPageForRooling(map);
		
		TomCourseLearningRecord learningRecordExample;
		for(TomCourses courses:list){
			List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courses.getCourseId());
			String courseType="";
			for(int i=0;i<courseClassifies.size();i++){
				if(i==0){
					courseType+=courseClassifies.get(i).getClassifyName();
				}else{
					courseType+=","+courseClassifies.get(i).getClassifyName();
				}
			}
			courses.setCourseType(courseType);
			
			learningRecordExample=new TomCourseLearningRecord();
			learningRecordExample.setCourseId(courses.getCourseId());
			courses.setLearnedNum(courseLearningRecordMapper.countByExample(learningRecordExample));
			
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}
}
