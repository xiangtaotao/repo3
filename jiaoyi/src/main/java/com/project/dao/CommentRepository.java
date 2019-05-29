package com.project.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.pojo.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByGoodIdOrderByIdDesc(Long id);

}
