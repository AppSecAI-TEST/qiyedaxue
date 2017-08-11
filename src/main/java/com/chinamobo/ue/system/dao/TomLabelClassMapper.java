package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.system.entity.TomLabelClass;

public interface TomLabelClassMapper {
    int deleteByPrimaryKey(Integer classId);

    int insert(TomLabelClass record);

    int insertSelective(TomLabelClass record);

    TomLabelClass selectByPrimaryKey(Integer classId);

    int updateByPrimaryKeySelective(TomLabelClass record);

    int updateByPrimaryKey(TomLabelClass record);
    
    List<Tree> selectLabelClassifyAsTree();
    
    List<TomLabelClass> selectLabelClassList();
    
    //查询最大分类ID
    int classIdMax();
}