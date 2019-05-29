package com.project.controller.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dao.GoodsRepository;
import com.project.enums.ResultEnum;
import com.project.service.GoodsOrderService;
import com.project.utils.ResultVOUtil;
import com.project.vo.PageQueryWeb;
import com.project.vo.ResultVO;

@RestController
@RequestMapping("/weborder")
public class WebOrderController {

	@Autowired
	private GoodsOrderService userService;

	@Autowired
	private GoodsRepository goodsRepository;

	@PostMapping("/findAll")
	public ResultVO<?> findAll(String key, @Valid PageQueryWeb pageQuery) {
		if (key != null) {
			return ResultVOUtil.successTwo(userService.findAllByNameLike(key, pageQuery));
		} else
			return ResultVOUtil.successTwo(userService.findAll(pageQuery));
	}

	@GetMapping("/delete")
	public ResultVO<?> delete(Long id) {
		if (id == null) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		goodsRepository.deleteById(id);
		return ResultVOUtil.success();
	}

}
