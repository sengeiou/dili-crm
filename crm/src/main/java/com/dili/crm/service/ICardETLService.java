package com.dili.crm.service;

import com.dili.crm.domain.Customer;

public interface ICardETLService {
	boolean transIncrementData(Customer latestCustomer,int batchSize);
}
