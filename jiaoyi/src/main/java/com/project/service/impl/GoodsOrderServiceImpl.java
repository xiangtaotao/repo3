package com.project.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.project.dao.OrderRepository;
import com.project.pojo.GoodsOrder;
import com.project.service.GoodsOrderService;
import com.project.vo.PageQueryWeb;
import com.project.vo.PageResultVO;

@Service
public class GoodsOrderServiceImpl implements GoodsOrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public PageResultVO findAll(PageQueryWeb pageQuery) {
		Page<GoodsOrder> dataList = orderRepository.findAll(pageQuery.getPageable());
		if (dataList.getContent() == null) {
			return PageResultVO.ofEmpty(pageQuery.getLimit());
		}
		return PageResultVO.of(dataList.getContent(), dataList);
	}

	@Override
	public GoodsOrder findById(Long id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public PageResultVO findAll(Long userId, PageQueryWeb pageQuery) {
		Page<GoodsOrder> dataList = orderRepository.findAllByUserId(userId, pageQuery.getPageable());
		if (dataList.getContent() == null) {
			return PageResultVO.ofEmpty(pageQuery.getLimit());
		}
		return PageResultVO.of(dataList.getContent(), dataList);
	}

	@Override
	public PageResultVO findAllByNameLike(String key, @Valid PageQueryWeb pageQuery) {
		Page<GoodsOrder> dataList = orderRepository.findAllByGoodsNameLike(key, pageQuery.getPageable());
		if (dataList.getContent() == null) {
			return PageResultVO.ofEmpty(pageQuery.getLimit());
		}
		return PageResultVO.of(dataList.getContent(), dataList);
	}

}
