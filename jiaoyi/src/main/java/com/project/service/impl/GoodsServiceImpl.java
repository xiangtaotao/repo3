package com.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.project.dao.GoodsRepository;
import com.project.pojo.Goods;
import com.project.service.GoodsService;
import com.project.vo.PageQueryWeb;
import com.project.vo.PageResultVO;

@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsRepository goodsRepository;

	@Override
	public PageResultVO findAll(PageQueryWeb pageQuery) {
		Page<Goods> dataList = goodsRepository.findAll(pageQuery.getPageable());
		if (dataList.getContent() == null) {
			return PageResultVO.ofEmpty(pageQuery.getLimit());
		}
		return PageResultVO.of(dataList.getContent(), dataList);
	}

	@Override
	public PageResultVO findAll(Long userId, PageQueryWeb pageQuery) {
		Page<Goods> dataList = goodsRepository.findAllByUserId(userId, pageQuery.getPageable());
		if (dataList.getContent() == null) {
			return PageResultVO.ofEmpty(pageQuery.getLimit());
		}
		return PageResultVO.of(dataList.getContent(), dataList);
	}

	@Override
	public Goods findById(Long id) {
		return goodsRepository.findById(id).orElse(null);
	}

	@Override
	public PageResultVO findAllByTitle(String key, PageQueryWeb pageQuery) {
		Page<Goods> dataList = goodsRepository.findByTitleLike("%"+key+"%",pageQuery.getPageable());
		if (dataList.getContent() == null) {
			return PageResultVO.ofEmpty(pageQuery.getLimit());
		}
		return PageResultVO.of(dataList.getContent(), dataList);
	}

}
