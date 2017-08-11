package com.chinamobo.ue.system.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;

public class TomLabel {
	@FormParam("tagId")
    private String tagId;
	@FormParam("tagName")
    private String tagName;
    private String creatorId;
    private String creator;
    private String operator;
    private String admins;
    private String adminNames;
    private Date createTime;
    private Date updateTime;
	@FormParam("type")
    private String type;
	@FormParam("labelEmps[]")
    private List<String> labelEmps;
	@FormParam("empIds[]")
    private List<String> empIds;
	@FormParam("classId")
    private Integer classId;
	@FormParam("className")
    private String className;
	@FormParam("deptManagers")
	private List<Map<String,String>> emps;

	public List<Map<String, String>> getEmps() {
		return emps;
	}

	public void setEmps(List<Map<String, String>> emps) {
		this.emps = emps;
	}

	public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getAdmins() {
        return admins;
    }

    public void setAdmins(String admins) {
        this.admins = admins == null ? null : admins.trim();
    }

    public String getAdminNames() {
        return adminNames;
    }

    public void setAdminNames(String adminNames) {
        this.adminNames = adminNames == null ? null : adminNames.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
    
    public List<String> getLabelEmps() {
		return labelEmps;
	}

	public void setLabelEmps(List<String> labelEmps) {
		this.labelEmps = labelEmps;
	}
	
	public List<String> getEmpIds() {
		return empIds;
	}

	public void setEmpIds(List<String> empIds) {
		this.empIds = empIds;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}