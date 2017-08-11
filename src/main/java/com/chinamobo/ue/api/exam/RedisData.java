package com.chinamobo.ue.api.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.utils.RedisUtils;
import com.chinamobo.ue.utils.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 从redis中获取数据
 * 
 * @author Winter
 *
 */
public class RedisData {

	public RedisData() {
	}

	/**
	 * 从缓存中读取考试
	 * 
	 * @param examId
	 * @return
	 */
	public TomExam getExamFromRedis(String examId) {
		/* start 读取redis缓存考试 */
		String redisString = RedisUtils.hget("exam", examId);
		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString, TomExam.class);
		}
		/* end 读取redis缓存 */

		return null;
	}

	/**
	 * 从缓存中读取考试成绩
	 * 
	 * @param examId
	 * @param code
	 * @param gradeState
	 * @return
	 */
	public TomExamScore getExamScoreFromRedis(String examId, String code, String gradeState) {
		/* start 读取redis缓存考试成绩 */
		String redisString = RedisUtils.hget("examScore", examId + ":" + code + ":" + gradeState);
		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString.toString(), TomExamScore.class);
		}
		/* end 读取redis缓存 */

		return null;
	}

	/**
	 * 从缓存中读取非未审阅状态的考试成绩
	 * 
	 * @param examId
	 * @param code
	 * @param gradeState
	 * @return
	 */
	public TomExamScore getExamScoreNotPFromRedis(String examId, String code) {
		TomExamScore examScore = getExamScoreFromRedis(examId, code, "Y");
		if (examScore == null) {
			examScore = getExamScoreFromRedis(examId, code, "N");
		}
		if (examScore == null) {
			examScore = getExamScoreFromRedis(examId, code, "D");
		}

		return examScore;
	}
	
	/**
	 * 从缓存中读取第一条考试成绩
	 * 
	 * @param examId
	 * @param code
	 * @param gradeState
	 * @return
	 */
	public TomExamScore getFirstExamScoreFromRedis(String examId, String code) {
		/* start 读取redis缓存考试成绩 */
		String redisString = RedisUtils.hget("firstExamScore", examId + ":" + code);
		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString.toString(), TomExamScore.class);
		}
		
		return null;
	}
	
	/**
	 * 从缓存中读取补考信息
	 * 
	 * @param examId
	 * @param date
	 * @return
	 */
	// TODO
	public TomRetakingExam getRetakingExamFromRedis(String examId, Date date) {
		List<TomRetakingExam> retakingExams = getRetakingExamsForExamFromRedis(examId);
		if (retakingExams != null && retakingExams.size() > 0) {
			for (TomRetakingExam retakingExam : retakingExams) {
				if (retakingExam.getRetakingExamBeginTime().getTime() < date.getTime()
						&& retakingExam.getRetakingExamEndTime().getTime() >= date.getTime()) {
					return retakingExam;
				}
			}
		}

		return null;
	}

	/**
	 * 从缓存中读取补考信息
	 * 
	 * @param examId
	 * @param date
	 * @return
	 */
	public TomRetakingExam getRetakingExamFromRedis(String examId, String sort) {
		String redisString = RedisUtils.hget("retakingExamSort", examId + ":" + sort);
		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString, TomRetakingExam.class);
		}

		return null;
	}

	/**
	 * 从缓存中读取某考试的所有补考信息
	 * 
	 * @param examId
	 * @return
	 */
	public List<TomRetakingExam> getRetakingExamsForExamFromRedis(String examId) {
		String retakingExamsForOneExam = RedisUtils.hget("retakingExamsForOneExam", examId);
		List<TomRetakingExam> resultList = new ArrayList<TomRetakingExam>();
		if (StringUtil.isNotBlank(retakingExamsForOneExam)) {
			JSONArray jsonArray = JSONArray.fromObject(retakingExamsForOneExam);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String jsonString = JSON.toJSONString(jsonObject);
				TomRetakingExam retakingExam = JSON.parseObject(jsonString, TomRetakingExam.class);
				if (retakingExam != null) {
					resultList.add(retakingExam);
				}
			}
		}

		return resultList;
	}

	/**
	 * 从缓存中读取试卷信息
	 * 
	 * @param examId
	 * @return
	 */
	public TomExamPaper getExamPaperFromRedis(String examPaperId) {
		String redisString = RedisUtils.hget("tomExamPaper", examPaperId);
		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString, TomExamPaper.class);
		}

		return null;
	}

	/**
	 * 从缓存中读取题目
	 * 
	 * @param topicId
	 * @return
	 */
	public TomTopic getTopicFromRedis(String topicId) {
		String redisString = RedisUtils.hget("tomTopicWithId", topicId);
		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString, TomTopic.class);
		}

		return null;
	}

	/**
	 * 从缓存中读取题目的正确选项
	 * 
	 * @param topicId
	 * @return
	 */
	public List<TomTopicOption> getRightOptionsFromRedis(String topicId) {
		String redisString = RedisUtils.hget("tomTopicRightOption", topicId);

		if (StringUtil.isNotBlank(redisString)) {
			TomTopic topic = JSON.parseObject(redisString, TomTopic.class);
			return topic.getTopicOptions();
		}

		return null;
	}

	/**
	 * 从缓存中读取所有选项
	 * 
	 * @param topicId
	 * @return
	 */
	public List<TomTopicOption> getTopicOptionsFromRedis(String topicId) {
		String redisString = RedisUtils.hget("tomTopicWithId", topicId);
		if (StringUtil.isNotBlank(redisString)) {
			TomTopic topic = JSON.parseObject(redisString, TomTopic.class);
			return topic.getTopicOptions();
		}

		return null;
	}

	/**
	 * 从缓存中读取指定选项
	 * 
	 * @param optionId
	 * @return
	 */
	public TomTopicOption getOneOptionFromRedis(String topicId, String optionId) {
		List<TomTopicOption> list = getTopicOptionsFromRedis(topicId);
		for (TomTopicOption topicOption : list) {
			if (topicOption.getId().toString().equals(optionId)) {
				return topicOption;
			}
		}

		return null;
	}

	/**
	 * 从缓存中读取指定题库
	 * 
	 * @param examPaperId
	 * @param questionType
	 * @return
	 */
	public TomExamQuestion getExamQuestionFromRedis(String examPaperId, String bankId, String questionType) {
		String redisString = RedisUtils.hget("examQuestion", examPaperId + ":" + bankId + ":" + questionType);

		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString, TomExamQuestion.class);
		}

		return null;
	}

	/**
	 * 从缓存读取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public TomUserInfo getUserInfoFromRedis(String userId) {
		String redisString = RedisUtils.hget("userInfo", userId);
		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString, TomUserInfo.class);
		}

		return null;
	}

	/**
	 * 从缓存读取登录信息
	 * 
	 * @param userId
	 * @return
	 */
	public TomUserLog getUserLogFromRedis(String userId) {
		String redisString = RedisUtils.hget("tomUserLogCode", userId);
		if (StringUtil.isNotBlank(redisString)) {
			return JSON.parseObject(redisString, TomUserLog.class);
		}

		return null;
	}
}
