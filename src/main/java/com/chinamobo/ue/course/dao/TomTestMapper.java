package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.dto.TomExportTestDto;
import com.chinamobo.ue.course.entity.TomTest;

public interface TomTestMapper {
    int deleteByPrimaryKey(Integer testId);

    int insert(TomTest record);

    int insertSelective(TomTest record);

    TomTest selectByPrimaryKey(Integer testId);

    int updateByPrimaryKeySelective(TomTest record);

    int updateByPrimaryKey(TomTest record);
    
    List<TomTest> selectByCourseIdEX(Integer courseId);

	List<TomExportTestDto> selectByCourseId(Map<Object,Object> map);
	 int countQuestions (Map<Object,Object> map);
	 int countUsers (Map<Object,Object> map);
}