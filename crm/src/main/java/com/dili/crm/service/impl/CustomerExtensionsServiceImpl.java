package com.dili.crm.service.impl;

import com.dili.crm.dao.CustomerExtensionsMapper;
import com.dili.crm.domain.CustomerExtensions;
import com.dili.crm.service.CustomerExtensionsService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-28 14:51:20.
 */
@Service
public class CustomerExtensionsServiceImpl extends BaseServiceImpl<CustomerExtensions, Long> implements CustomerExtensionsService {

    public CustomerExtensionsMapper getActualDao() {
        return (CustomerExtensionsMapper)getDao();
    }
}