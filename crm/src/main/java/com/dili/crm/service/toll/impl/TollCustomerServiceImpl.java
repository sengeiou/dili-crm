package com.dili.crm.service.toll.impl;

import com.dili.crm.dao.toll.TollCustomerMapper;
import com.dili.crm.domain.toll.TollCustomer;
import com.dili.crm.service.toll.TollCustomerService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.datasource.SwitchDataSource;

import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-12-14 10:44:02.
 */
@Service
@SwitchDataSource("toll")
public class TollCustomerServiceImpl extends BaseServiceImpl<TollCustomer, Long> implements TollCustomerService {

    public TollCustomerMapper getActualDao() {
        return (TollCustomerMapper)getDao();
    }
}