package com.dili.crm.dao;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface CustomerMapper extends MyMapper<Customer> {
	List<CustomerChartDTO>selectCustomersGroupByType();
	List<CustomerChartDTO>selectCustomersGroupByMarket();
	List<CustomerChartDTO>selectCustomersGroupByProfession();

	/**
	 * 获取父客户id，以逗号分隔,包含自己
	 * @param id
	 * @return
	 */
	String getParentCustomers(Long id);

	/**
	 * 获取子客户id字符串，以逗号分隔,包含自己
	 * @param id
	 * @return
	 */
	String getChildCustomerIds(Long id);

	/**
	 * 获取子客户，包含自己
	 * @param id
	 * @return
	 */
	List<Customer> getChildCustomers(Long id);

}