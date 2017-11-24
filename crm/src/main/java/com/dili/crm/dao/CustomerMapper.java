package com.dili.crm.dao;

import java.util.List;

import com.dili.crm.domain.Customer;
import com.dili.ss.base.MyMapper;

public interface CustomerMapper extends MyMapper<Customer> {
	public List<Customer>selectCustomersGroupByType();
	public List<Customer>selectCustomersGroupByMarket();
	public List<Customer>selectCustomersGroupByProfession();
}