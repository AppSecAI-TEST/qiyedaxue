package com.chinamobo.ue.api.exam.dto;

import java.util.List;

import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.exam.entity.TomExamAnswerDetails;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomJoinExamRecord;
import com.chinamobo.ue.system.entity.TomUserInfo;


public class ExamSubmitDto {

	private TomExamScore tomExamScore;
	private List<TomExamAnswerDetails> answerDetailList;
	private TomJoinExamRecord tomJoinExamRecord;
	private TomUserInfo	userInfo;
	private TomEbRecord ebRecord;
	private TomExamAnswerDetails deletingAnswerDetails;//需要删除的答案
	
	private boolean flag;//是否审阅, 为true时, 代表未审阅
	private Integer pointCompareResult;//当前考试与上一次考试得分比较, 当前考试得分低时 为 1 , 当前考试得分高时为-1, 相等时为0
	private Integer timeCompareResult;//当前考试与上一次考试所用时间比较, 当前考试用时长时 为 1 , 其他为-1,
	private Integer scoreResult;//考试不及格且没有补考时为0 , 及格时为 1, 其他为2
	private boolean reduceECoin;// 后台设置的扣除的e币不为空时为true
	private boolean existsLastExam;//上次考试是否存在
	
	
	public boolean isExistsLastExam() {
		return existsLastExam;
	}
	public void setExistsLastExam(boolean existsLastExam) {
		this.existsLastExam = existsLastExam;
	}
	public TomExamAnswerDetails getDeletingAnswerDetails() {
		return deletingAnswerDetails;
	}
	public void setDeletingAnswerDetails(TomExamAnswerDetails deletingAnswerDetails) {
		this.deletingAnswerDetails = deletingAnswerDetails;
	}
	public TomUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(TomUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public TomEbRecord getEbRecord() {
		return ebRecord;
	}
	public void setEbRecord(TomEbRecord ebRecord) {
		this.ebRecord = ebRecord;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public Integer getPointCompareResult() {
		return pointCompareResult;
	}
	public void setPointCompareResult(Integer pointCompareResult) {
		this.pointCompareResult = pointCompareResult;
	}
	public Integer getTimeCompareResult() {
		return timeCompareResult;
	}
	public void setTimeCompareResult(Integer timeCompareResult) {
		this.timeCompareResult = timeCompareResult;
	}
	public Integer getScoreResult() {
		return scoreResult;
	}
	public void setScoreResult(Integer scoreResult) {
		this.scoreResult = scoreResult;
	}
	public boolean isReduceECoin() {
		return reduceECoin;
	}
	public void setReduceECoin(boolean reduceECoin) {
		this.reduceECoin = reduceECoin;
	}
	public TomJoinExamRecord getTomJoinExamRecord() {
		return tomJoinExamRecord;
	}
	public void setTomJoinExamRecord(TomJoinExamRecord tomJoinExamRecord) {
		this.tomJoinExamRecord = tomJoinExamRecord;
	}
	public TomExamScore getTomExamScore() {
		return tomExamScore;
	}
	public void setTomExamScore(TomExamScore tomExamScore) {
		this.tomExamScore = tomExamScore;
	}
	public List<TomExamAnswerDetails> getAnswerDetailList() {
		return answerDetailList;
	}
	public void setAnswerDetailList(List<TomExamAnswerDetails> answerDetailList) {
		this.answerDetailList = answerDetailList;
	}
	
	
}
