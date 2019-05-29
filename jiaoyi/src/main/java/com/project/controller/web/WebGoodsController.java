package com.project.controller.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dao.GoodsRepository;
import com.project.enums.ResultEnum;
import com.project.service.GoodsService;
import com.project.utils.ResultVOUtil;
import com.project.vo.PageQueryWeb;
import com.project.vo.ResultVO;

@RestController
@RequestMapping("/webgoods")
public class WebGoodsController {

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private GoodsRepository goodsRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping("/findAll")
	public ResultVO<?> findAll(String key, @Valid PageQueryWeb pageQuery) {
		if (key != null) {
			return ResultVOUtil.successTwo(goodsService.findAllByTitle(key, pageQuery));
		} else
			return ResultVOUtil.successTwo(goodsService.findAll(pageQuery));
	}

	@GetMapping("/delete")
	public ResultVO<?> delete(Long id) {
		if (id == null) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		goodsRepository.deleteById(id);
		return ResultVOUtil.success();
	}
	
	@GetMapping("/detail")
	public ResultVO<?> detail(Long id) {
		if (id == null) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		String sql="SELECT a.*,b.`username` FROM goods a INNER JOIN USER b ON a.`user_id`=b.`id`"
				+ " WHERE a.`id`=?";
		return ResultVOUtil.success(jdbcTemplate.queryForMap(sql, id));
	}

}
