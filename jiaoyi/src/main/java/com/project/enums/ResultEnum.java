package com.project.enums;

import lombok.Getter;

/**
 * 
 * @author Administrator
 *
 */
@Getter
public enum ResultEnum {

	SUCCESS(1000, "成功"),

	LOGIN_FAIL(1001, "登录失败, 用户名或密码错误"),

	PARAM_ERROR(1002, "参数不正确"),

	UN_LOGIN(1003, "尚未登陆"),

	LOGOUT_TIMEOUT(1004, "身份已过期，请重新登陆"),

	LOGOUT_SUCCESS(1005, "登出成功"),

	LOGIN_SUCCESS(1006, "登陆成功"),

	LOGIN_EXISTS(1007, "已登陆，请勿重复登陆"),

	USER_EXISTS(1008, "用户已存在，请勿重复添加"),

	USER_NOT_EXISTS(1009, "该用户不存在"),

	USER_NOT_PERMISSION(1010, "权限不足，请联系管理员"),

	NOT_VALIDE_PHONE(1011, "手机号码格式不正确"), 
	
	USER_ISEXSITS(1012, "用户已存在"),;

	private Integer code;

	private String message;

	ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
