package com.dili.crm.domain.dto;

import com.dili.crm.domain.Customer;

import javax.persistence.Table;

/**
 * 客户默认地址
 * 
 * This file was generated on 2018-1-5
 */
@Table(name = "`customer`")
public interface CustomerAddressDto extends Customer {

	/**
	 * 纬度
	 * @return
	 */
	String getLat();

	void setLat(String lat);

	/**
	 * 经度
	 * @return
	 */
	String getLng();

	void setLng(String lng);

}