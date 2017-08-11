/**
 * 
 */
package com.chinamobo.ue.cache.cacher.interfaces;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.exam.entity.TomExam;

/**
 * @ClassName: TomExamIntf 
 * @Description: TODO 
 * @author Acemon
 * @date 2017年7月4日 上午10:01:10  
 */
public interface TomExamIntf {
	public List<TomExam> initUserExams(Map<Object,Object> paramMap)throws Exception;
}
