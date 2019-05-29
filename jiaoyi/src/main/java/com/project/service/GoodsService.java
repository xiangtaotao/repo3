package com.project.service;

import com.project.pojo.Goods;
import com.project.vo.PageQueryWeb;
import com.project.vo.PageResultVO;

public interface GoodsService {

	PageResultVO findAll(PageQueryWeb pageQuery);
	
	Goods findById(Long id);

	PageResultVO findAll(Long userId, PageQueryWeb pageQuery);

	PageResultVO findAllByTitle(String key, PageQueryWeb pageQuery);

}
