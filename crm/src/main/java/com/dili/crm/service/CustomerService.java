package com.dili.crm.service;

import com.dili.crm.domain.Customer;
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
}