package com.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.project.dao.UserRepository;
import com.project.pojo.User;
import com.project.service.UserService;
import com.project.vo.PageQueryWeb;
import com.project.vo.PageResultVO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByPhone(String phone) {
		return userRepository.findByPhone(phone);
	}

	@Override
	public PageResultVO findAll(PageQueryWeb pageQuery) {
		Page<User> dataList = userRepository.findByType(1, pageQuery.getPageable());
		if (dataList.getContent() == null) {
			return PageResultVO.ofEmpty(pageQuery.getLimit());
		}
		return PageResultVO.of(dataList.getContent(), dataList);
	}




	@Override
	public PageResultVO findByUserName(String userName, PageQueryWeb pageQuery) {
		Page<User> dataList = userRepository.findByUserNameLike("%"+userName+"%", pageQuery.getPageable());
		if (dataList.getContent() == null) {
			return PageResultVO.ofEmpty(pageQuery.getLimit());
		}
		return PageResultVO.of(dataList.getContent(), dataList);
	}

}
