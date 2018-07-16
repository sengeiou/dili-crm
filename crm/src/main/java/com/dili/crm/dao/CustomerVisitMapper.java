package com.dili.crm.dao;

import java.util.List;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.domain.dto.CustomerVisitChartDTO;
import com.dili.ss.base.MyMapper;

public interface CustomerVisitMapper extends MyMapper<CustomerVisit> {
	public List<CustomerVisitChartDTO>selectCustomerVisitGroupByMode(List<String>firmCodes);
	public List<CustomerVisitChartDTO>selectCustomerVisitGroupByState(List<String>firmCodes);
}