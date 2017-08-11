package com.chinamobo.ue.activity.dao;

import com.chinamobo.ue.activity.entity.TomActivityQr;

public interface TomActivityQrMapper {
    int deleteByPrimaryKey(Integer qrId);

    int insert(TomActivityQr record);

    int insertSelective(TomActivityQr record);

    TomActivityQr selectByPrimaryKey(Integer qrId);

    int updateByPrimaryKeySelective(TomActivityQr record);

    int updateByPrimaryKey(TomActivityQr record);
    
    TomActivityQr selectByPrActivityId(Integer activityId);
}