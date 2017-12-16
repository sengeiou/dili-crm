package com.dili.crm.domain.dto;

import com.dili.crm.domain.Customer;

import javax.persistence.Table;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-11-13 17:11:21.
 */
@Table(name = "`customer`")
public interface CustomerTreeDto extends Customer {

	/**
	 * 客户树的展开状态
	 * @return
	 */
	String getState();

	void setState(String state);

}