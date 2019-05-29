package com.project.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class Goods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;// 标题

	private String detail;// 详情

	private String goodsImage;// 商品图片

	private String goodsAddress;// 商品位置

	private BigDecimal goodsPrice;// 商品价格

	private BigDecimal dispacherPrice;// 运费
	
	private Integer count;//库存

	private Long userId;

	private Long categoryId;
	private Long addTime;
	@Transient
	private List<Map<String, Object>> cList;
	
	private String userPhone;//联系人电话

}
