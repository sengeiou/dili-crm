package com.dili.crm.service.impl;

import com.dili.crm.dao.CustomerMapper;
import com.dili.crm.domain.Customer;
import com.dili.crm.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    public CustomerMapper getActualDao() {
        return (CustomerMapper)getDao();
    }
}