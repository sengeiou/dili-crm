package com.dili.crm.service.impl;

import com.dili.crm.dao.ContactsMapper;
import com.dili.crm.domain.Contacts;
import com.dili.crm.service.ContactsService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-28 14:49:44.
 */
@Service
public class ContactsServiceImpl extends BaseServiceImpl<Contacts, Long> implements ContactsService {

    public ContactsMapper getActualDao() {
        return (ContactsMapper)getDao();
    }
}