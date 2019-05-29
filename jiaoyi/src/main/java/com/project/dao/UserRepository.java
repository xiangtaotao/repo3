package com.project.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.pojo.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Page<User> findByUserNameLike(String username, Pageable pageQuery);

	Page<User> findByUserNameLikeAndType(String username, Integer type, Pageable pageQuery);

	Page<User> findByType(Integer type, Pageable pageQuery);

	User findByPhone(String phone);
}
