package com.project.vo;

import lombok.Data;

/**
 * http请求返回的最外层对象
 * 
 * @author XT
 * @time 2018年8月12日
 *
 */
@Data
public class ResultVO<T> {

	/** 错误码. */
	private Integer code;

	/** 提示信息. */
	private String msg;

	/** 具体内容. */
	private T data;
	
	private Integer count;

}
