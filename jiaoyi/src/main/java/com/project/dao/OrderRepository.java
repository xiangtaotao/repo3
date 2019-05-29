package com.project.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.pojo.GoodsOrder;

public interface OrderRepository extends JpaRepository<GoodsOrder, Long> {
	Page<GoodsOrder> findAll(Pageable pageable);

	Optional<GoodsOrder> findById(Long id);

	Page<GoodsOrder> findAllByUserId(Long userId, Pageable pageable);

	List<GoodsOrder> findAllByUserId(Long userId);

	List<GoodsOrder> findAllByGoodsUserId(Long userId);

	Page<GoodsOrder> findAllByGoodsNameLike(String key, Pageable pageable);

}
