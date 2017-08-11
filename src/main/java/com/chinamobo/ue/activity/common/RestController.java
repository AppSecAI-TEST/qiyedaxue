package com.chinamobo.ue.activity.common;

import javax.validation.ConstraintViolationException;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

/**
 * 版本: [1.0]
 * 功能说明: Rest控制器
 *
 * 作者: WangLg
 * 创建时间: 2016年3月11日 下午5:07:28
 */
public abstract class RestController {
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			//验证
		}catch(ConstraintViolationException ex){
			return false;
		}
		return true;
	}
}
