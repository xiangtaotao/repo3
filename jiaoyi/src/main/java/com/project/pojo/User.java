package com.project.pojo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -897852564885046086L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", columnDefinition = "varchar(32) default null")
	private String userName;// 用户名

	@Column(name = "password", columnDefinition = "varchar(32) default null")
	private String password;// 登录密码

	@Column(name = "nickname", columnDefinition = "varchar(32) default null")
	private String nickName;// 昵称
	
	@Column(name = "school", columnDefinition = "varchar(32) default null")
	private String school;// 学校

	@Column(name = "address", columnDefinition = "varchar(32) default null")
	private String address;// 地址

	@Column(name = "phone", columnDefinition = "varchar(32) default null")
	private String phone;// 联系方式

	@Column(name = "type", columnDefinition = "tinyint(2) default null")
	private Integer type;// 0管理员 1用户

	@Column(name = "headimg", columnDefinition = "varchar(255) default null")
	private String headImg;// 头像
	
	private Long addTime;

}
