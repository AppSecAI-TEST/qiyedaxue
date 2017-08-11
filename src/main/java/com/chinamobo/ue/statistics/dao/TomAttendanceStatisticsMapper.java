package com.chinamobo.ue.statistics.dao;

import com.chinamobo.ue.statistics.entity.TomActivityStatistics;

public interface TomAttendanceStatisticsMapper {
	
	int insert(TomActivityStatistics record);
	
	int insertSelective(TomActivityStatistics record);
}
