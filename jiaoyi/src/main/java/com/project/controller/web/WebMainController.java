package com.project.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dao.MessageRepository;
import com.project.pojo.Message;
import com.project.utils.ResultVOUtil;
import com.project.vo.ResultVO;

@RestController
@RequestMapping("/webmain")
public class WebMainController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageRepository messageRepository;

	@GetMapping("/total")
	public ResultVO<?> findAll() {
		String sql = " SELECT" + " (SELECT IFNULL(COUNT(a.`id`),0) FROM goods_order a) AS goodCount,"
				+ " (SELECT IFNULL(COUNT(a.`id`),0)  FROM `user` a) AS userCount,"
				+ " (SELECT IFNULL(COUNT(a.`id`),0)  FROM goods a) AS orderCount,"
				+ " (SELECT IFNULL(COUNT(a.`id`),0)  FROM message a) AS messageCount,"
				+ " (SELECT IFNULL(COUNT(a.`id`),0)  FROM `comment` a) AS commentCount,"
				+ " (SELECT IFNULL(COUNT(a.`id`),0)  FROM collection a) AS collectCount,"
				+ " (SELECT IFNULL(sum(a.`amount`),0)  FROM goods_order a where a.pay_status=1) AS moneyAll";
		return ResultVOUtil.success(jdbcTemplate.queryForMap(sql));
	}

	@GetMapping("/sendMsg")
	public ResultVO<?> sendMsg(String msg) {
		Message message = new Message();
		message.setAddTime(System.currentTimeMillis());
		message.setIsRead(1);
		message.setMessaage(msg);
		message.setUserId(-1L);
		messageRepository.save(message);
		return ResultVOUtil.success();
	}

}
