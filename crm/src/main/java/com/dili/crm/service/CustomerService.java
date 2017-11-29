package com.dili.crm.service;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 */
public interface CustomerService extends BaseService<Customer, Long> {

	/**
	 * 按需插入客户，返回BaseOutput
	 * @param customer
	 * @return
	 */
	BaseOutput insertSelectiveWithOutput(Customer customer);

	/**
	 * 按需修改客户，返回BaseOutput
	 * @param condtion
	 * @return
	 */
	BaseOutput updateSelectiveWithOutput(Customer condtion);

	/**
	 * 逻辑删除客户，返回BaseOutput
	 * @param aLong
	 * @return
	 */
	BaseOutput deleteWithOutput(Long aLong);
	
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByType();
	
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByMarket();
	
	BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByProfession();

	/**
	 * 根据客户名称(模糊)和客户id查询可以添加的成员客户，
	 * 过滤掉自己、所有的子id和所有父id
	 * @param customerId
	 * @return
	 */
	String listMembersPage(String name, Long customerId) throws Exception;
}