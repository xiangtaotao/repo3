package com.project.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

/**
 * 分页查询对象
 * 
 * @author lee
 * @data 2018年11月15日 下午4:08:05
 */

public class PageQuery {

	@Min(value = 0, message = "当前页码不合法")
	private Integer pageNum = 0;

	@Min(value = 1, message = "每页展示数量不合法")
	private Integer pageSize = 20;

	private String orderBy;

	@Pattern(regexp = "DESC|ASC", message = "排序方式参数有误")
	private String orderType = "DESC";

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getOffset() {
		return pageNum * pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Direction getOrderType() {
		return Direction.valueOf(orderType);
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Pageable getPageable() {
		if (StringUtils.isEmpty(orderBy))
			return PageRequest.of(this.getPageNum(), this.getPageSize());
		return PageRequest.of(this.getPageNum(), this.getPageSize(), this.getOrderType(), this.getOrderBy());
	}

}
