package com.project.service;

import com.project.pojo.User;
import com.project.vo.PageQueryWeb;
import com.project.vo.PageResultVO;

public interface UserService {
	User findByPhone(String phone);

	PageResultVO findAll(PageQueryWeb pageQuery);
	
	PageResultVO findByUserName(String userName, PageQueryWeb pageQuery);
}
