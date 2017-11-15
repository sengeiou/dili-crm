package com.dili.crm.service.impl;

import com.dili.crm.dao.AddressMapper;
import com.dili.crm.domain.Address;
import com.dili.crm.service.AddressService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-15 11:16:14.
 */
@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, Long> implements AddressService {

    public AddressMapper getActualDao() {
        return (AddressMapper)getDao();
    }
}