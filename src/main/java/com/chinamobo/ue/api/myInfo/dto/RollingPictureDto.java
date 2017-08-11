package com.chinamobo.ue.api.myInfo.dto;


public class RollingPictureDto {

    private Integer pictureId;

    private Integer id;
   
    private String pictureCategory;
    
    private String pictureUrl;

    private String isTop;
    
    private String resourceName;
    
	public Integer getPictureId() {
		return pictureId;
	}

	public void setPictureId(Integer pictureId) {
		this.pictureId = pictureId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPictureCategory() {
		return pictureCategory;
	}

	public void setPictureCategory(String pictureCategory) {
		this.pictureCategory = pictureCategory;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
    
}
