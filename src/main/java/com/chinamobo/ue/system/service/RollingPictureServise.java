package com.chinamobo.ue.system.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.system.dao.TomNewsMapper;
import com.chinamobo.ue.system.dao.TomRollingPictureMapper;
import com.chinamobo.ue.system.dao.TomRollingPictureRecordMapper;
import com.chinamobo.ue.system.dto.TomRollingPictureDto;
import com.chinamobo.ue.system.entity.TomNews;
import com.chinamobo.ue.system.entity.TomRollingPicture;
import com.chinamobo.ue.system.entity.TomRollingPictureRecord;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;

@Service
public class RollingPictureServise {
	@Autowired
	private TomRollingPictureMapper tomRollingPictureMapper;
	@Autowired
	private TomRollingPictureRecordMapper tomRollingPictureRecordMapper;
	@Autowired
	private TomNewsMapper tomNewsMapper;
	@Autowired
	private TomCoursesMapper tomCoursesMapper;
	@Transactional
	public void insert(TomRollingPicture tomRollingPicture) {
		ShiroUser user = ShiroUtils.getCurrentUser();
		Date date = new Date();
		tomRollingPicture.setCreateTime(date);
		tomRollingPicture.setCreatorId(user.getCode());
		tomRollingPicture.setCreater(user.getName());
		tomRollingPicture.setDeleted("Y");
		tomRollingPicture.setViewCount(0);
		tomRollingPicture.setIsRelease("N");
		tomRollingPicture.setIsTop("N");
		tomRollingPicture.setDeleted("Y");
		tomRollingPicture.setOperator(user.getName());
		tomRollingPicture.setUpdateTime(date);
		tomRollingPictureMapper.insertSelective(tomRollingPicture);
	}

	@Transactional
	public void update(TomRollingPicture tomRollingPicture) {
		ShiroUser user = ShiroUtils.getCurrentUser();
		Date date = new Date();
		tomRollingPicture.setDeleted("Y");
		tomRollingPicture.setViewCount(0);
		tomRollingPicture.setIsRelease("N");
		tomRollingPicture.setIsTop("N");
		tomRollingPicture.setDeleted("Y");
		tomRollingPicture.setOperator(user.getName());
		tomRollingPicture.setUpdateTime(date);
		tomRollingPictureMapper.updateByPrimaryKeySelective(tomRollingPicture);
	}

	@Transactional
	public void updateStatus(int pictureId, String deleted, String isTop, String isRelease) {
		ShiroUser user = ShiroUtils.getCurrentUser();
		Date date = new Date();
		TomRollingPicture tomRollingPicture = new TomRollingPicture();
		tomRollingPicture.setUpdateTime(date);
		tomRollingPicture.setOperator(user.getName());
		tomRollingPicture.setPictureId(pictureId);
		if (null != deleted) {
			tomRollingPicture.setDeleted(deleted);
		}
		if (null != isTop) {
			tomRollingPicture.setIsTop(isTop);
		}
		if (null != isRelease) {
			tomRollingPicture.setReleaseTime(date);
			tomRollingPicture.setReleaser(user.getName());
			tomRollingPicture.setIsRelease(isRelease);
		}
		tomRollingPictureMapper.updateForStatus(tomRollingPicture);
	}

	@Transactional
	public void delete(int pictureId, String deleted) {
		ShiroUser user = ShiroUtils.getCurrentUser();
		Date date = new Date();
		TomRollingPicture tomRollingPicture = new TomRollingPicture();
		tomRollingPicture.setUpdateTime(date);
		tomRollingPicture.setOperator(user.getName());
		tomRollingPicture.setPictureId(pictureId);
		tomRollingPicture.setDeleted(deleted);
		tomRollingPictureMapper.updateForStatus(tomRollingPicture);
	}

	public PageData<TomRollingPicture> findPage(int pageNum, int pageSize, String resourceName) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomRollingPicture> page = new PageData<TomRollingPicture>();
		// if
		Map<Object, Object> map1 = new HashMap<Object, Object>();

//		if (resourceName != null) {
//			resourceName = resourceName.replaceAll("/", "//");
//			resourceName = resourceName.replaceAll("%", "/%");
//			resourceName = resourceName.replaceAll("_", "/_");
//		}

		map1.put("resourceName", resourceName);
		int count = tomRollingPictureMapper.countByPage(map1);

		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("resourceName", resourceName);

		List<TomRollingPicture> list = tomRollingPictureMapper.selectByPage(map);

		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;

	}

	public List<TomRollingPictureRecord> findByCount(int pictureId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomRollingPictureRecordMapper.selectById(pictureId);
	}

	public TomRollingPictureDto findByTitle(int pictureId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomRollingPicture picture = tomRollingPictureMapper.selectByPrimaryKey(pictureId);
		TomRollingPictureDto dto = new TomRollingPictureDto();
		dto.setPictureId(pictureId);
		dto.setResourceName(picture.getResourceName());
		dto.setReleaseTime(picture.getReleaseTime());
		if ("N".equals(picture.getPictureCategory())) {
			TomNews news = tomNewsMapper.selectByPrimaryKey(picture.getResourceId());
			dto.setDetails(news.getNewsDetails());
		} else {
			dto.setDetails("请到课程中查看");
		}
		return dto;
	}

	public String isRelease(int pictureId, String isRelease) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		ShiroUser user = ShiroUtils.getCurrentUser();
		Date date = new Date();
		TomRollingPicture selectByPrimaryKey = tomRollingPictureMapper.selectByPrimaryKey(pictureId);
		String status = "Y";
		if("Y".equals(isRelease)){
			if("N".equals(selectByPrimaryKey.getPictureCategory())){
				TomNews news = tomNewsMapper.selectByPrimaryKey(selectByPrimaryKey.getResourceId());
				if("N".equals(news.getIsRelease())){
					status="N";
				}
			}else if("U".equals(selectByPrimaryKey.getPictureCategory())){
				status="Y";
			}else{
			
				TomCourses course = tomCoursesMapper.selectByPrimaryKey(selectByPrimaryKey.getResourceId());
				if("N".equals(course.getStatus())){
					status="C";
				}
			}
		}
		TomRollingPicture tomRollingPicture = new TomRollingPicture();
		tomRollingPicture.setUpdateTime(date);
		tomRollingPicture.setOperator(user.getName());
		tomRollingPicture.setPictureId(pictureId);
		tomRollingPicture.setIsRelease(isRelease);
		tomRollingPicture.setReleaser(user.getName());
		tomRollingPicture.setReleaseTime(date);
		if(status.equals("Y")){
		tomRollingPictureMapper.updateForStatus(tomRollingPicture);
		return status;
		}else{
		return status;
		}
	}

	public TomRollingPicture findById(int pictureId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomRollingPictureMapper.selectByPrimaryKey(pictureId);
	}

}
