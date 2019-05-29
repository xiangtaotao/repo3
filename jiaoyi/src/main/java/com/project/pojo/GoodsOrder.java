package com.project.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class GoodsOrder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer payStatus;// 支付状态 1已支付 2待支付 3已取消

	private BigDecimal amount;// 订单金额
	
	private String goodsName;// 商品名称  多个商品默认第一个
	
	private Long goodsId;// 商品id  多个商品默认第一个

	private Long userId;// 下单用户id

	private Long goodsUserId;// 发布商品的用户编号

	private Long addTime;

	@Column(columnDefinition = "json")
	@JsonRawValue
	private String detail;// 订单详情

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderDetail {
		private Long goodsId;// 商品id

		private String goodsName;// 商品名称

		private Integer count;// 购买数量

		private BigDecimal unitPrice;// 单价
	}

}
