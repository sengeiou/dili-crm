package com.dili.crm.dao;

import java.util.List;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.ss.base.MyMapper;

public interface CustomerMapper extends MyMapper<Customer> {
	public List<CustomerChartDTO>selectCustomersGroupByType();
	public List<CustomerChartDTO>selectCustomersGroupByMarket();
	public List<CustomerChartDTO>selectCustomersGroupByProfession();
}