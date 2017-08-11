package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.system.entity.TomEmpOne;
import com.chinamobo.ue.system.entity.TomUserLog;

public interface TomUserLogMapper {
    int insert(TomUserLog record);

    int insertSelective(TomUserLog record);
    
    /**根据用户名获取用户
     * @param name
     * @return
     */
    TomUserLog getUserbyName(String name);

	void updateByCode(TomUserLog tomUserLog);
	
	/**
	 * 功能描述:[更改用户密码]
	 * 创建者: zhj
	 * @param phone
	 * @return
	 */
	void updatePassword(TomUserLog tomUserLog);
	
	 void updateByPrimaryKeySelective(TomUserLog tomUserLog);

	/**
	 * 
	 * 功能描述：根据用户Code查找用户
	 *
	 * 创建者：ZXM
	 * 创建时间: 2016年3月23日上午10:19:44
	 * @param code
	 * @return
	 */
	TomUserLog getUserByCode(String code);
	
	/**
	 * 
	 * 功能描述：[email查询]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年6月7日 下午3:59:58
	 * @param email
	 * @return
	 */
	TomUserLog getUserByEmail(String email);
	
	/**
	 * 功能描述:[根据手机号查询用户信息]
	 * 创建者: zhj
	 * @param phone
	 * @return
	 */
	TomUserLog selectUserByPhone(String phone);
	
	TomUserLog selectByLoginName(String loginName);

	TomUserLog getUserByToken(String token);
	
	int insertList(List list);

	void deleteByCode(String code);
	
	/**
	 * 
	 * 功能描述：[查询累计用户数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月14日 下午1:12:45
	 * @param map
	 * @return
	 */
	int countTotalUsers();
	List<TomUserLog> selectAll();
	TomUserLog getUserByEMphone(String userName);
}