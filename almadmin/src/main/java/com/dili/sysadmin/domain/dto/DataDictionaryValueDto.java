package com.dili.sysadmin.domain.dto;

import java.io.Serializable;

public class DataDictionaryValueDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7989198050972449951L;

	private Long id;

	private Integer orderNumber;

	private String code;

	private String value;

	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer sort) {
		this.orderNumber = sort;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}