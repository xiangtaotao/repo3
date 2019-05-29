package com.project.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.pojo.Goods;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

	List<Goods> findAllByTitle(String title);
	
	Page<Goods> findAll(Pageable pageable);
	 
	Optional<Goods> findById(Long id);

	Page<Goods> findAllByUserId(Long userId, Pageable pageable);
	
	List<Goods> findAllByUserId(Long userId);
	
	Page<Goods> findAllByCategoryId(Long cateGoryId, Pageable pageable);
	
	List<Goods> findAllByCategoryId(Long cateGoryId);

	Page<Goods> findByTitleLike(String key, Pageable pageable);
}
