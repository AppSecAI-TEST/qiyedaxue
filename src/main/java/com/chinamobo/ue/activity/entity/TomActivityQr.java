package com.chinamobo.ue.activity.entity;

public class TomActivityQr {
    private Integer qrId;

    private Integer activityId;

    private String qrPath;

    private String qrName;

    public Integer getQrId() {
        return qrId;
    }

    public void setQrId(Integer qrId) {
        this.qrId = qrId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getQrPath() {
        return qrPath;
    }

    public void setQrPath(String qrPath) {
        this.qrPath = qrPath == null ? null : qrPath.trim();
    }

    public String getQrName() {
        return qrName;
    }

    public void setQrName(String qrName) {
        this.qrName = qrName == null ? null : qrName.trim();
    }
}