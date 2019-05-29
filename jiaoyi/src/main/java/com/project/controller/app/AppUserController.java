package com.project.controller.app;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
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
import com.project.utils.ValidateUtils;
import com.project.vo.ResultVO;

@RestController
@RequestMapping("/appuser")
public class AppUserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@PostMapping("/login")
	public ResultVO<?> login(String phone, String password) {
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		User user = userService.findByPhone(phone);
		if (user == null) {
			return ResultVOUtil.error(ResultEnum.USER_NOT_EXISTS.getCode(), ResultEnum.USER_NOT_EXISTS.getMessage());
		}
		if (!user.getPassword().equals(password)) {
			return ResultVOUtil.error(ResultEnum.LOGIN_FAIL.getCode(), ResultEnum.LOGIN_FAIL.getMessage());
		}
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		stringRedisTemplate.opsForValue().set(token, JSONObject.toJSONString(user), Duration.ofHours(24L));
		return ResultVOUtil.success(token);
	}

	@PostMapping("/regist")
	public ResultVO<?> regist(User user) {
		if (user == null) {
			return ResultVOUtil.error(ResultEnum.USER_NOT_EXISTS.getCode(), ResultEnum.USER_NOT_EXISTS.getMessage());
		}
		if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())) {
			return ResultVOUtil.error(ResultEnum.USER_NOT_EXISTS.getCode(), ResultEnum.USER_NOT_EXISTS.getMessage());
		}
		if (!ValidateUtils.valideMobile(user.getPhone())) {
			return ResultVOUtil.error(ResultEnum.NOT_VALIDE_PHONE.getCode(), ResultEnum.NOT_VALIDE_PHONE.getMessage());
		}
		User user1 = userRepository.findByPhone(user.getPhone());
		if (user1 != null) {
			return ResultVOUtil.error(ResultEnum.USER_ISEXSITS.getCode(), ResultEnum.USER_ISEXSITS.getMessage());
		}
		user.setType(1);
		user.setAddTime(System.currentTimeMillis());
		user.setUserName(user.getPhone());
		userRepository.save(user);
		return ResultVOUtil.success("注册成功");
	}

	@GetMapping("/logout")
	public ResultVO<?> logout() {
		return ResultVOUtil.success();
	}

}
