package com.chinamobo.ue.exam.dto;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.exam.entity.TomExam;

public class MyExam {

	private Integer examCount0;
	
	private Integer examCount1;
	
	private Integer examCount2;
	
	private PageData<TomExam> examList;

	public Integer getExamCount0() {
		return examCount0;
	}

	public void setExamCount0(Integer examCount0) {
		this.examCount0 = examCount0;
	}

	public Integer getExamCount1() {
		return examCount1;
	}

	public void setExamCount1(Integer examCount1) {
		this.examCount1 = examCount1;
	}

	public Integer getExamCount2() {
		return examCount2;
	}

	public void setExamCount2(Integer examCount2) {
		this.examCount2 = examCount2;
	}

	public PageData<TomExam> getExamList() {
		return examList;
	}

	public void setExamList(PageData<TomExam> examList) {
		this.examList = examList;
	}

}
