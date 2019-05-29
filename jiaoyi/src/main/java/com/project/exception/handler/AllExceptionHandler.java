package com.project.exception.handler;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.enums.ResultEnum;
import com.project.exception.UserLoginException;
import com.project.utils.BindExceptionUtil;
import com.project.utils.ResultVOUtil;
import com.project.vo.ResultVO;


/**
 * 
*    
* 项目名称：jiaoyi   
* 类名称：CrmlExceptionHandler   
* 类描述：   
* 创建人：XT   
* 创建时间：2019年3月20日 下午10:09:17   
* @version
 */
@ControllerAdvice
public class AllExceptionHandler {

	@ExceptionHandler(value = UserLoginException.class)
	@ResponseBody
	public ResultVO<?> handlerAuthorizeException(UserLoginException exception) {
		return ResultVOUtil.error(exception.getCode(), exception.getMessage());
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseBody
	public ResultVO<?>  constraintViolationException(BindException exception) {
		return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), BindExceptionUtil.getMessage(exception));
	}
}
