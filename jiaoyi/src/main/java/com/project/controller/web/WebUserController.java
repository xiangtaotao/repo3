package com.project.controller.web;

import java.time.Duration;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.project.dao.UserRepository;
import com.project.enums.ResultEnum;
import com.project.pojo.User;
import com.project.service.UserService;
import com.project.utils.ResultVOUtil;
import com.project.vo.PageQueryWeb;
import com.project.vo.PageResultVO;
import com.project.vo.ResultVO;

@RestController
@RequestMapping("/webuser")
public class WebUserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@PostMapping("/login")
	public ResultVO<?> login(String phone, String password) {
		User user = userService.findByPhone(phone);
		if (user == null) {
			return ResultVOUtil.error(ResultEnum.USER_NOT_EXISTS.getCode(), ResultEnum.USER_NOT_EXISTS.getMessage());
		}
		if (user.getType() != 0) {
			return ResultVOUtil.error(ResultEnum.USER_NOT_PERMISSION.getCode(),
					ResultEnum.USER_NOT_PERMISSION.getMessage());
		}
		if (!user.getPassword().equals(password)) {
			return ResultVOUtil.error(ResultEnum.LOGIN_FAIL.getCode(), ResultEnum.LOGIN_FAIL.getMessage());
		}
		String token = UUID.randomUUID().toString().replaceAll("-", "") + user.getPhone() + user.getId();
		stringRedisTemplate.opsForValue().set(token, JSONObject.toJSONString(user), Duration.ofHours(24L));
		return ResultVOUtil.success(token);
	}

	@GetMapping("/logout")
	public ResultVO<?> logout() {
		return ResultVOUtil.success();
	}

	@PostMapping("/findAll")
	public ResultVO<?> findAll(String key, @Valid PageQueryWeb pageQuery) {
		PageResultVO findAll = null;
		if (key != null) {
			findAll = userService.findByUserName(key, pageQuery);
		} else {
			findAll = userService.findAll(pageQuery);
		}
		return ResultVOUtil.successTwo(findAll);
	}

	@GetMapping("/deleteUser")
	public ResultVO<?> deleteUser(Long id) {
		if (id == null) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		userRepository.deleteById(id);

		return ResultVOUtil.success();
	}
	
	@GetMapping("/hasToken")
	public ResultVO<?> hasToken() {
		return ResultVOUtil.success();
	}
}
