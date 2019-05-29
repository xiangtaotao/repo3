package com.project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {

	public static Boolean valideMobile(String phone) {
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(phone);
		boolean isMatch = m.matches();
		if (isMatch) {
			System.out.println("您的手机号" + phone + "是正确格式@——@");
		} else {
			System.out.println("您的手机号" + phone + "是错误格式！！！");
		}
		return isMatch;
	}

}
