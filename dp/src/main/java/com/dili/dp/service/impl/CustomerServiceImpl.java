package com.dili.dp.service.impl;

import com.dili.dp.dao.CustomerMapper;
import com.dili.dp.domain.Customer;
import com.dili.dp.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    public CustomerMapper getActualDao() {
        return (CustomerMapper)getDao();
    }

    @Override
    public List<Customer> list(Customer condtion) {
	    condtion.setYn(1);
	    return super.list(condtion);
    }

}