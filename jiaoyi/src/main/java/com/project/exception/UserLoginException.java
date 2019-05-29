package com.project.exception;

import lombok.Getter;

@Getter
public class UserLoginException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;

	public UserLoginException(int code, String message) {
		super(message);
		this.code = code;
	}
}
