package com.dili.crm.dao;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerAddressDto;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.ss.base.MyMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomerMapper extends MyMapper<Customer> {
	List<CustomerChartDTO>selectCustomersGroupByType(List<String>firmCodes);
	List<CustomerChartDTO>selectCustomersGroupByMarket(List<String>firmCodes);
	List<CustomerChartDTO>selectCustomersGroupByProfession(List<String>firmCodes );

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

	/**
	 * 查询客户及地址信息
	 * @return
	 */
	List<CustomerAddressDto> selectCustomerAddress();

	/**
	 * 批量用户的父客户为某一客户
	 * @param params 参数，key-parentId(更新后的父客户ID),key-ids(需要执行更新操作的数据ID集);
	 */
	void updateParentIdById(Map<String,Object> params);

	/**
	 * 根据客户ID过滤客户ID数据
	 * @param ids 需要排除的ID集
	 * @return
	 */
	Set<Long> queryMemberIds(List ids);

	/**
	 * 根据客户ID集，级联删除客户相关信息
	 * @param ids 客户ID集合
	 */
	Integer deleteWithCascade(List<Long> ids);

}