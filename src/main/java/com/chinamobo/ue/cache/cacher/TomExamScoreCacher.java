/**
 * 
 */
package com.chinamobo.ue.cache.cacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheData;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.utils.JsonMapper;

/**
 * 版本: [1.0]
 * 功能说明: 缓存考试成绩信息
 * 作者: WChao 创建时间: 2017年6月15日 上午10:52:01
 */
public class TomExamScoreCacher implements Cacher {

	@Autowired
	private TomExamScoreMapper examScoreMapper;
	
	@Override
	public void init() throws Exception {

	}

	@Override
	public void doCache(AbstractCacheManager cacheManager) throws Exception {
		JsonMapper mapper = new JsonMapper();
		//缓存考试成绩
		List<TomExamScore> examScoreList = examScoreMapper.selectListByExample(null);
		int scoreSize=examScoreList.size();
		for (int i =0;i<scoreSize;i++) {
			TomExamScore examScore=examScoreList.get(i);
			cacheManager.addCacheData(new RedisCacheData("examScore", examScore.getExamId() + ":" + examScore.getCode()+":"+examScore.getGradeState(),mapper.toJson(examScore)));
		}
	}

}
