package com.dili.crm.service.impl;

import com.dili.crm.dao.IcardUserCardMapper;
import com.dili.crm.domain.IcardUserCard;
import com.dili.crm.service.IcardUserCardService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-30 15:47:56.
 */
@Service
public class IcardUserCardServiceImpl extends BaseServiceImpl<IcardUserCard, Long> implements IcardUserCardService {

    public IcardUserCardMapper getActualDao() {
        return (IcardUserCardMapper)getDao();
    }
}