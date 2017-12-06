package com.dili.crm.service;

import com.dili.crm.domain.Customer;

public interface ICardETLService {
	/**
	 * @param latestCustomer 最新一条customer数据(做为从电子结算转移数据的基准,如果为null,则自动从crm获取)
	 * @param batchSize 从电子结算转移数据到crm时批量查询数量
	 * @return
	 */
	boolean transIncrementData(Customer latestCustomer,int batchSize);
}
