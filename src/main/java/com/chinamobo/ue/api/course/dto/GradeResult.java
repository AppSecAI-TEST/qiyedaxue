package com.chinamobo.ue.api.course.dto;

/**
 * 评论接口参数评分集合
 * @author ZXM
 *
 */
public class GradeResult {
	
	private String gradeDimensionId;
	
	private String score;

	public String getGradeDimensionId() {
		return gradeDimensionId;
	}

	public void setGradeDimensionId(String gradeDimensionId) {
		this.gradeDimensionId = gradeDimensionId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	

}
