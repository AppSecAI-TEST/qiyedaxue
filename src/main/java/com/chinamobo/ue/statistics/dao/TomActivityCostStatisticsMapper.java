package com.chinamobo.ue.statistics.dao;

import com.chinamobo.ue.statistics.entity.TomActivityCostStatistics;


public interface TomActivityCostStatisticsMapper {
int insert(TomActivityCostStatistics record);
	
	int insertSelective(TomActivityCostStatistics record);
	
}
