package com.chinamobo.ue.course.dao;

import com.chinamobo.ue.course.entity.TomLearnTimeLog;

public interface TomLearnTimeLogMapper {
    int deleteByPrimaryKey(Integer logId);

    int insert(TomLearnTimeLog record);

    int insertSelective(TomLearnTimeLog record);

    TomLearnTimeLog selectByPrimaryKey(Integer logId);

    int updateByPrimaryKeySelective(TomLearnTimeLog record);

    int updateByPrimaryKey(TomLearnTimeLog record);
}