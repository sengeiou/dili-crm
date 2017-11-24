package com.dili.crm.service;

import java.util.List;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 */
public interface CustomerService extends BaseService<Customer, Long> {

	BaseOutput insertSelectiveWithOutput(Customer customer);

	BaseOutput updateSelectiveWithOutput(Customer condtion);

	BaseOutput deleteWithOutput(Long aLong);
	
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByType();
	
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByMarket();
	
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByProfession();
}