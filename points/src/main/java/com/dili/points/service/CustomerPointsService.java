package com.dili.points.service;

import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:30.
 */
public interface CustomerPointsService extends BaseService<CustomerPoints, Long> {
	public EasyuiPageOutput listCustomerPointsByCustomer(CustomerApiDTO customer);
	public DTO findCustomerPointsByCertificateNumber(String certificateNumber);
}