package com.project.service;

import javax.validation.Valid;

import com.project.pojo.GoodsOrder;
import com.project.vo.PageQueryWeb;
import com.project.vo.PageResultVO;

public interface GoodsOrderService {

	PageResultVO findAll(PageQueryWeb pageQuery);
	
	GoodsOrder findById(Long id);

	PageResultVO findAll(Long userId, PageQueryWeb pageQuery);

	PageResultVO findAllByNameLike(String key, @Valid PageQueryWeb pageQuery);

//	PageResultVO findAllByTitle(String key, @Valid PageQueryWeb pageQuery);

}
