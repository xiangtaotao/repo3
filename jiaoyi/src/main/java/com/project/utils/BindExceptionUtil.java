package com.project.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

/**
 * 
*    
* 项目名称：jiaoyi   
* 类名称：BindExceptionUtil   
* 类描述：   
* 创建人：XT   
* 创建时间：2019年3月20日 下午10:33:43   
* @version
 */
public class BindExceptionUtil {

	/**
	 * 获取批量异常信息
	 * 
	 * @param e
	 * @return
	 */
	public static String getMessage(BindException e) {
		List<String> msgList = new ArrayList<>();
		for (ObjectError objectError : e.getBindingResult().getAllErrors()) {
			msgList.add(objectError.getDefaultMessage());
		}
		String messages = StringUtils.join(msgList.toArray(), ",");
		return messages;
	}

}
