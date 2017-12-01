package com.dili.crm.service;

import com.dili.crm.domain.Customer;

public interface ICardETLService {
	Customer transAllData(int batchSize);
	Customer transIncrementData(Customer latestCustomer,int batchSize);
}
