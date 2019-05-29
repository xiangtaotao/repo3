package com.project.utils;

import com.project.enums.ResultEnum;
import com.project.vo.PageResultVO;
import com.project.vo.ResultVO;

/**
 * 
 * 
 * 项目名称：jiaoyi 类名称：ResultVOUtil 类描述： 创建人：XT 创建时间：2019年3月20日 下午10:06:38
 * 
 * @version
 */
public class ResultVOUtil {

	public static ResultVO success(Object object) {
		ResultVO resultVO = new ResultVO();
		resultVO.setData(object);
		resultVO.setCode(ResultEnum.SUCCESS.getCode());
		resultVO.setMsg(ResultEnum.SUCCESS.getMessage());
		return resultVO;
	}

	public static ResultVO successTwo(PageResultVO object) {
		ResultVO resultVO = new ResultVO();
		resultVO.setData(object.getDataList());
		resultVO.setCode(0);
		resultVO.setMsg("");
		resultVO.setCount(object.getTotalElements().intValue());
		return resultVO;
	}

	public static ResultVO success() {
		return success(null);
	}

	public static ResultVO error(Integer code, String msg) {
		ResultVO resultVO = new ResultVO();
		resultVO.setCode(code);
		resultVO.setMsg(msg);
		return resultVO;
	}
	
	public static ResultVO error(String msg) {
		ResultVO resultVO = new ResultVO();
		resultVO.setCode(500);
		resultVO.setMsg(msg);
		return resultVO;
	}
}
