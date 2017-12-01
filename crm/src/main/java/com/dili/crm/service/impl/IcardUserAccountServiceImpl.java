package com.dili.crm.service.impl;

import com.dili.crm.dao.IcardUserAccountMapper;
import com.dili.crm.domain.IcardUserAccount;
import com.dili.crm.service.IcardUserAccountService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.datasource.SwitchDataSource;

import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-30 15:46:52.
 */
@Service
@SwitchDataSource("icard")
public class IcardUserAccountServiceImpl extends BaseServiceImpl<IcardUserAccount, Long> implements IcardUserAccountService {

    public IcardUserAccountMapper getActualDao() {
        return (IcardUserAccountMapper)getDao();
    }
}