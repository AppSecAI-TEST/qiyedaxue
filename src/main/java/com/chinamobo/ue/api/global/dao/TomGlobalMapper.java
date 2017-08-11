package com.chinamobo.ue.api.global.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.api.global.dto.SerchResults;

public interface TomGlobalMapper {
  /**
   * 课程列表，详情列表，考试列表（全局搜索）
   * @param Map
   * @return
   */
	List<SerchResults> findAllSerch(Map <Object,Object>Map);
}
