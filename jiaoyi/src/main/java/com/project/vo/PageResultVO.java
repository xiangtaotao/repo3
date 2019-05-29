package com.project.vo;

import java.util.List;

import org.springframework.data.domain.Page;

import com.google.common.collect.Lists;

/**
 * 分页查询返回结果
 * 
 * @author lee
 * @data 2018年11月15日 下午4:07:41
 */
public class PageResultVO {

	private List<? extends Object> dataList;
	// 当前页数
	private Integer pageNum;
	// 每页长度
	private Integer pageSize;
	// 本页元素个数
	private Integer pageElements;
	// 总页数
	private Integer totalPages;
	// 总元素
	private Long totalElements;

	public PageResultVO() {
		super();
	}

	public static PageResultVO ofEmpty(Integer pageSize) {
		return new PageResultVO(Lists.newArrayList(), 0, pageSize, 0, 0, 0L);
	}

	public static PageResultVO of(List<? extends Object> dataList, Page<?> page) {
		return new PageResultVO(dataList, page);
	}

	private PageResultVO(List<? extends Object> dataList, Page<?> page) {
		super();
		this.dataList = dataList;
		if (page != null) {
			this.pageNum = page.getNumber();
			this.pageSize = page.getSize();
			this.pageElements = page.getNumberOfElements();
			this.totalPages = page.getTotalPages();
			this.totalElements = page.getTotalElements();
		}
	}

	public static PageResultVO of(List<? extends Object> dataList, Integer pageNum, Integer pageSize,
			Integer pageElements, Integer totalPages, Long totalElements) {
		return new PageResultVO(dataList, pageNum, pageSize, pageElements, totalPages, totalElements);
	}

	private PageResultVO(List<? extends Object> dataList, Integer pageNum, Integer pageSize, Integer pageElements,
			Integer totalPages, Long totalElements) {
		super();
		this.dataList = dataList;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.pageElements = pageElements;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
	}

	public List<? extends Object> getDataList() {
		return dataList;
	}

	public void setDataList(List<? extends Object> dataList) {
		this.dataList = dataList;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageElements() {
		return pageElements;
	}

	public void setPageElements(Integer pageElements) {
		this.pageElements = pageElements;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

}
